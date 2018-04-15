package com.lanking.uxb.service.imperial02.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.mq.common.constants.MqYoomathHomeworkRegistryConstants;
import com.lanking.cloud.component.mq.common.constants.MqYoomathPushRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionKnowledge;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExamination2Question;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivity;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityAward;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityLottery;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityPrizes;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityRank;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityUser;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationGrade;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationHomework;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationHomeworkStudent;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcess;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcessTime;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationRoom;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationUserProcess;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.HomeworkCorrectingType;
import com.lanking.cloud.domain.yoomath.push.api.PushHandleAction;
import com.lanking.cloud.domain.yoomath.push.api.PushHandleForm;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.code.api.QuestionKnowledgeService;
import com.lanking.uxb.service.imperial.api.ImperialExamination2QuestionService;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityAwardService;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityLotteryService;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityPrizesService;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityRankService;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityService;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityStudentService;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityUserService;
import com.lanking.uxb.service.imperial.api.ImperialExaminationHomeworkService;
import com.lanking.uxb.service.imperial.api.ImperialExaminationHomeworkStudentService;
import com.lanking.uxb.service.imperial.convert.ImperialExaminationActivityAwardConvert;
import com.lanking.uxb.service.imperial.convert.ImperialExaminationClazzConvert;
import com.lanking.uxb.service.imperial.convert.ImperialExaminationOutlineConvert;
import com.lanking.uxb.service.imperial.convert.ImperialExaminationScoreConvert;
import com.lanking.uxb.service.imperial.form.ImperialExaminationActivityUserForm;
import com.lanking.uxb.service.imperial.value.VExaminationTime;
import com.lanking.uxb.service.imperial.value.VImperialExaminationActivityAward;
import com.lanking.uxb.service.imperial.value.VImperialExaminationOutline;
import com.lanking.uxb.service.imperial.value.VImperialExaminationScore;
import com.lanking.uxb.service.imperial02.convert.ImperialExaminationActivityConvert2;
import com.lanking.uxb.service.imperial02.value.VImperialExaminationActivity2;
import com.lanking.uxb.service.imperial02.value.VImperialExaminationActivityLottery;
import com.lanking.uxb.service.imperial02.value.VImperialExaminationAwardDatas2;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.convert.UserConvertOption;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.wx.api.ZyWXMessageService;
import com.lanking.uxb.service.zuoye.api.ZyCorrectUserService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.form.PublishHomeworkForm;

/**
 * 科举活动02教师端接口.
 * 
 * @author peng.zhao
 * @version 2017年11月8日
 */
@RestController
@RequestMapping("zy/m/t/imperial02")
public class ZyMTeaImperialExamination02Controller {

	@Autowired
	private ImperialExaminationActivityService imperialService;
	@Autowired
	private ImperialExaminationActivityConvert2 imperialExaminationActivityConvert;
	@Autowired
	private ImperialExaminationActivityUserService activityUserService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private ZyHomeworkClassService classService;
	@Autowired
	private ZyHomeworkStudentClazzService homeworkStudentClazzService;
	@Autowired
	private ImperialExaminationActivityStudentService imperialExamActivityStudentService;
	@Autowired
	private ImperialExaminationActivityPrizesService imperialExaminationActivityPrizesService;
	@Autowired
	private ImperialExaminationActivityLotteryService imperialExaminationActivityLotteryService;
	@Autowired
	private ImperialExaminationActivityAwardService activityAwardService;
	@Autowired
	private ImperialExaminationActivityAwardConvert activityAwardConvert;
	@Autowired
	private ImperialExaminationHomeworkService examHkService;
	@Autowired
	private HomeworkService hkService;
	@Autowired
	private ImperialExaminationOutlineConvert outlineConvert;
	@Autowired
	private ImperialExaminationClazzConvert examClazzConvert;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private ImperialExaminationActivityRankService rankService;
	@Autowired
	private ImperialExaminationScoreConvert scoreConvert;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private ImperialExamination2QuestionService imperialExamination2QuestionService;
	@Autowired
	private ZyHomeworkService homeworkService;
	@Autowired
	private QuestionKnowledgeService questionKnowledgeService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	@Qualifier("executor")
	private Executor executor;
	@Autowired
	private MqSender mqSender;
	@Autowired
	private ZyCorrectUserService zyCorrectUserService;
	@Autowired
	private ZyWXMessageService zyWXMessageService;
	@Autowired
	private ImperialExaminationHomeworkStudentService examnHomeworkStudentService;
	@Autowired
	private StudentHomeworkService studentHomeworkService;

	/**
	 * 根据当前时间比较，不同阶段获取不同倒计时
	 * 
	 * @param code
	 *            活动code
	 * @return
	 */
	@RolesAllowed(anyone = true)
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "getCountdown", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getCountdown(Long code) {
		ImperialExaminationActivity activity = imperialService.getActivity(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		ImperialExaminationProcessTime time = activity.getCfg().getCurretStage();
		if (time == null) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_TIME_ERROR));
		}
		Map<ImperialExaminationUserProcess, Long> map = imperialService.getCountDownTime(code, time);

		return new Value(map);
	}

	/**
	 * 获取活动配置
	 * 
	 * @param code
	 * @return
	 */
	@RolesAllowed(anyone = true)
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "getActivityCfg", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getActivityCfg(Long code) {
		ImperialExaminationActivity activity = imperialService.getActivity(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		VImperialExaminationActivity2 v = imperialExaminationActivityConvert.to(activity);
		List<ImperialExaminationProcessTime> timeList = activity.getCfg().getTimeList();
		for (ImperialExaminationProcessTime time : timeList) {
			if (time.getProcess() == ImperialExaminationProcess.PROCESS_SIGNUP) {
				v.setSignUpStartTime(time.getStartTime());
				v.setSignUpEndTime(time.getEndTime());
				break;
			}
		}
		v.setTimeList(imperialService.queryExamTime2(code));
		return new Value(v);
	}

	/**
	 * 获取当前时间对应的阶段，用来判断使用哪一个页面、调哪一个接口
	 * 
	 * @param code
	 * @return 0.报名页,1.活动页,2.颁奖页
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "getCurretStage", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getCurretStage(Long code) {
		ImperialExaminationActivity activity = imperialService.getActivity(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		Map<String, Object> data = new HashMap<String, Object>(1);

		// 0.报名页,1.活动页,2.颁奖页
		int flag = 0;
		// 活动结束时间
		Long endTime_signup = imperialService.get(ImperialExaminationProcess.PROCESS_SIGNUP, code).getEndTime()
				.getTime();
		// 颁奖开始时间
		Long awardTime = imperialService.get(ImperialExaminationProcess.PROCESS_AWARDS, code).getStartTime().getTime();
		// 未到报名截止时间,都在报名页
		if (new Date().getTime() < endTime_signup) {
			ImperialExaminationActivityUser activityUser = activityUserService.getUser(code, Security.getUserId());
			// 如果在报名时间内，用户没有报名进入看到的就是报名页，如果已经报名进来看到的就是活动页
			if (activityUser == null) {
				flag = 0;
			} else {
				flag = 1;
			}

		} else if (new Date().getTime() > endTime_signup && new Date().getTime() < awardTime) {
			flag = 1;
		} else {
			flag = 2;
		}

		data.put("curretStage", flag);
		return new Value(data);
	}

	/**
	 * 获取报名相关数据 1.教师姓名 2.手机号码 3.参赛年级 4.教材版本 5.参赛班级
	 * 
	 * @param code
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "querySignUpInfo", method = { RequestMethod.POST, RequestMethod.GET })
	public Value querySignUpInfo(Long code) {
		ImperialExaminationActivity activity = imperialService.getActivity(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}

		Map<String, Object> data = new HashMap<String, Object>();
		Account account = accountService.getAccountByUserId(Security.getUserId());
		if (account != null) {
			data.put("mobile", account.getMobile());
		}

		User user = accountService.getUserByUserId(Security.getUserId());
		if (user != null) {
			data.put("name", user.getName());
		}

		data.put("grades", activity.getCfg().getGrades());

		// 教材版本 可选【苏科版】15、【北师新版】31、【人教新版】30、【沪科新版】27
		data.put("textbookCategoryCodes", activity.getCfg().getTextbookCategoryCodes());

		List<HomeworkClazz> clazzList = classService.listClazzsOrderByStuNum(Security.getUserId());
		List<Map<String, Object>> classMapList = new ArrayList<Map<String, Object>>();
		if (CollectionUtils.isNotEmpty(clazzList)) {
			for (HomeworkClazz clazz : clazzList) {
				Map<String, Object> clazzMap = new HashMap<String, Object>();
				clazzMap.put("className", clazz.getName());
				clazzMap.put("classId", clazz.getId());
				if (clazz.getStudentNum() > 19) {
					clazzMap.put("enough", true);
				} else {
					clazzMap.put("enough", false);
				}
				classMapList.add(clazzMap);
			}
		}
		data.put("classList", classMapList);

		return new Value(data);
	}

	/**
	 * 报名
	 * 
	 * @param form
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "signUp", method = { RequestMethod.POST, RequestMethod.GET })
	public Value signUp(ImperialExaminationActivityUserForm form) {
		ImperialExaminationActivity activity = imperialService.getActivity(form.getCode());
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		ImperialExaminationProcessTime time = activity.getCfg().getCurretStage();
		if (time == null) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_TIME_ERROR));
		}
		if (time.getProcess() != ImperialExaminationProcess.PROCESS_SIGNUP) {
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_PROCESS_SIGNUP_ERROR));
		}
		if (StringUtils.isBlank(form.getMobile()) || StringUtils.isBlank(form.getName())
				|| CollectionUtils.isEmpty(form.getClassList())) {
			return new Value(new MissingArgumentException());
		}
		if (form.getName().length() > 15) {
			// 教师姓名长度超出限制
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_AWARDS_CONTACT_ERROR));
		}
		if (form.getMobile().length() != 11 || form.getMobile().charAt(0) != '1') {
			// 手机号码不正确
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_AWARDS_MOBILE_ERROR));
		}
		ImperialExaminationActivityUser activityUser = activityUserService.getUser(form.getCode(),
				Security.getUserId());
		if (activityUser != null) {
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_REPEAT_SIGNUP_ERROR));
		}
		form.setUserId(Security.getUserId());
		// 考场
		List<ImperialExaminationRoom> rooms = activity.getCfg().getRooms();
		for (ImperialExaminationRoom value : rooms) {
			if (value.getTextbookCategoryCodes().contains(form.getTextbookCategoryCode())) {
				form.setRoom(value.getRoom());
				break;
			}
		}

		// 保存教师报名信息
		activityUserService.signUp(form);

		// 保存学生报名信息
		List<Long> clazzs = form.getClassList();
		Set<Long> studentIds = Sets.newHashSet();
		for (Long classId : clazzs) {
			List<Long> ids = homeworkStudentClazzService.listClassStudents(classId);
			for (Long studentId : ids) {
				studentIds.add(studentId);
			}
		}

		List<Long> studentIdList = studentIds.stream().collect(Collectors.toList());
		List<Long> signUpedIds = imperialExamActivityStudentService.getUsers(form.getCode(), studentIdList);
		for (Long value : signUpedIds) {
			studentIdList.remove(value);
		}
		imperialExamActivityStudentService.signUpByUsers(form.getCode(), studentIdList);

		Map<String, Object> data = new HashMap<String, Object>(1);
		// 1.添加奖券
		ImperialExaminationActivityLottery lottery = imperialExaminationActivityLotteryService.addLottery(
				Security.getUserId(), ImperialExaminationProcess.PROCESS_SIGNUP, Security.getUserType(),
				form.getCode());
		// 2.抽奖
		lottery = imperialExaminationActivityLotteryService.luckDraw(form.getCode(), lottery.getId(), form.getRoom(),
				Security.getUserType());

		VImperialExaminationActivityLottery vLottery = new VImperialExaminationActivityLottery();
		vLottery.setId(lottery.getId());
		vLottery.setCode(lottery.getActivityCode());
		vLottery.setName(lottery.getName());
		data.put("lottery", vLottery);

		// 前端要求返回活动配置
		List<VExaminationTime> timeList = imperialService.queryExamTime(form.getCode());
		for (VExaminationTime value : timeList) {
			if (value.getType() == ImperialExaminationType.PROVINCIAL_EXAMINATION) {
				data.put("startTime", value.getStartTime());
				break;
			}
		}

		return new Value(data);
	}

	/**
	 * 添加奖品
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "addPrize", method = { RequestMethod.POST, RequestMethod.GET })
	public Value addPrize(ImperialExaminationActivityPrizes entity) {
		entity.setActivityCode(10099);
		imperialExaminationActivityPrizesService.addPrize(entity);
		return new Value();
	}

	/**
	 * 确认抽奖
	 * 
	 * @param lotteryId
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "confirmLottery", method = { RequestMethod.POST, RequestMethod.GET })
	public Value confirmLottery(Long lotteryId) {
		if (lotteryId == null) {
			return new Value(new MissingArgumentException());
		}

		ImperialExaminationActivityLottery lottery = imperialExaminationActivityLotteryService
				.confirmLottery(lotteryId);
		if (lottery == null) {
			return new Value(new EntityNotFoundException());
		}

		return new Value();
	}

	/**
	 * 获奖数据.
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "awardData", method = { RequestMethod.POST, RequestMethod.GET })
	public Value awardData(Long code) {
		if (code == null) {
			return new Value(new MissingArgumentException());
		}

		// 获取活动配置
		ImperialExaminationActivity activity = imperialService.getActivity(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}

		// 判断是否报名(未报名也可看颁奖页)
		ImperialExaminationActivityUser activityUser = activityUserService.getUser(code, Security.getUserId());
		// 用户没有报名，判断用户当前所设置的版本，当前为“人教新版、沪科新版、北师新版”的用户，展示第二考场的排名。其余版本展示第一考场的排名
		int room = 1;
		if (activityUser != null) {
			room = activityUser.getRoom();
		} else {
			Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
			if (teacher.getTextbookCategoryCode() != null) {
				// 【苏科版】15、【北师新版】31、【人教新版】30、【沪科新版】27、【华师大版】23
				if (teacher.getTextbookCategoryCode() == 31 || teacher.getTextbookCategoryCode() == 30
						|| teacher.getTextbookCategoryCode() == 27 || teacher.getTextbookCategoryCode() == 23) {
					room = 2;
				}
			}
		}

		List<ImperialExaminationProcessTime> timeList = activity.getCfg().getTimeList();
		for (ImperialExaminationProcessTime time : timeList) {
			if (time.getProcess() == ImperialExaminationProcess.PROCESS_AWARDS) {
				if (time.getStartTime().getTime() > System.currentTimeMillis()) {
					// 未到颁奖时间
					return new Value(new YoomathMobileException(
							YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_AWARDS_TIME_ERROR));
				}
			}
		}

		// 获奖页面数据
		VImperialExaminationAwardDatas2 awardDatas = new VImperialExaminationAwardDatas2();

		// 奖品设置
		int num = 6; // 需要取得获奖的人数固定前6个
		List<ImperialExaminationActivityAward> awardList = activityAwardService.awardListByRoom(code, num, room);
		List<VImperialExaminationActivityAward> vawardList = activityAwardConvert.to(awardList);
		if (CollectionUtils.isNotEmpty(vawardList)) {
			List<VImperialExaminationActivityAward> awardLevel1 = Lists.newArrayList();
			List<VImperialExaminationActivityAward> awardLevel2 = Lists.newArrayList();
			List<VImperialExaminationActivityAward> awardLevel3 = Lists.newArrayList();
			// 出现冻结用户情况,需要特殊处理
			int awardCount = 1;
			for (VImperialExaminationActivityAward value : vawardList) {
				if (awardCount == 1) {
					awardLevel1.add(value);
				} else if (awardCount == 2 || awardCount == 3) {
					awardLevel2.add(value);
				} else if (awardCount == 4 || awardCount == 5 || awardCount == 6) {
					awardLevel3.add(value);
				}
				awardCount++;
				// if (value.getRank() == 1L) {
				// awardLevel1.add(value);
				// } else if (value.getRank() == 2L || value.getRank() == 3L) {
				// awardLevel2.add(value);
				// } else if (value.getRank() == 4L || value.getRank() == 5L ||
				// value.getRank() == 6L) {
				// awardLevel3.add(value);
				// }
			}

			awardDatas.setAwardLevel1(awardLevel1);
			awardDatas.setAwardLevel2(awardLevel2);
			awardDatas.setAwardLevel3(awardLevel3);
		}

		// 当前用户获奖信息
		if (activityUser != null) {
			VImperialExaminationActivityAward selfAward = null;
			for (VImperialExaminationActivityAward vAward : vawardList) {
				if (vAward.getUserId() == Security.getUserId()) {
					selfAward = vAward;
				} else {
					vAward.setAwardContact(null);
					vAward.setAwardContactNumber(null);
					vAward.setAwardDeliveryAddress(null);
				}
			}
			if (selfAward == null) {
				// 可能为冻结用户、未获奖用户
				selfAward = activityAwardConvert.to(activityAwardService.getByUser(code, Security.getUserId()));
				if (selfAward.getStatus() != Status.ENABLED) {
					selfAward.setRank(0L); // 冻结用户没有排名
				}
			}
			awardDatas.setSelf(selfAward);
		}

		// 当前用户报名信息
		awardDatas.setName(activityUser == null ? "" : activityUser.getName());
		awardDatas.setMobile(activityUser == null ? "" : activityUser.getMobile());

		return new Value(awardDatas);
	}

	/**
	 * 完善中奖联系信息.
	 * 
	 * @param awardId
	 *            获奖排名ID
	 * @param contact
	 *            收货人
	 * @param mobile
	 *            收货人手机
	 * @param address
	 *            收货人地址
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "improve", method = { RequestMethod.POST, RequestMethod.GET })
	public Value improve(Long awardId, String contact, String mobile, String address) {
		if (awardId == null || StringUtils.isBlank(contact) || StringUtils.isBlank(mobile)
				|| StringUtils.isBlank(address)) {
			return new Value(new MissingArgumentException());
		}
		if (contact.length() > 15) {
			// 收货人姓名长度超出限制
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_AWARDS_CONTACT_ERROR));
		}
		if (address.length() > 50) {
			// 收货人地址长度超出限制
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_AWARDS_ADDRESS_ERROR));
		}
		if (mobile.length() != 11 || mobile.charAt(0) != '1') {
			// 收货人手机号码不正确
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_AWARDS_MOBILE_ERROR));
		}

		ImperialExaminationActivityAward award = activityAwardService.get(awardId);
		if (award == null) {
			return new Value(new EntityNotFoundException());
		} else if (award.getUserId() != Security.getUserId()) {
			return new Value(new NoPermissionException());
		}

		try {
			activityAwardService.improve(awardId, contact, mobile, address);
			return new Value();
		} catch (AbstractException e) {
			return new Value(e);
		}

	}

	/**
	 * 活动页首页接口<br>
	 * 1.老师查看每一个阶段考试中数据<br>
	 * 2.每一个阶段的成绩公布
	 * 
	 * @param code
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "examData", method = { RequestMethod.POST, RequestMethod.GET })
	public Value examData(Long code) {
		ImperialExaminationActivity activity = imperialService.getActivity(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		ImperialExaminationProcessTime time = activity.getCfg().getCurretStage();
		if (time == null) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_TIME_ERROR));
		}

		List<ImperialExaminationProcessTime> timeList = activity.getCfg().getTimeList();
		ImperialExaminationActivityUser activityUser = activityUserService.getUser(code, Security.getUserId());
		for (ImperialExaminationProcessTime time1 : timeList) {
			if (time1.getProcess() == ImperialExaminationProcess.PROCESS_SIGNUP) {
				if (time1.getEndTime().getTime() > System.currentTimeMillis() && activityUser == null) {
					// 当前阶段属于报名阶段
					return new Value(new YoomathMobileException(
							YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_ACTIVITY_TIME_ERROR));
				}
			}
		}

		Map<String, Object> data = new HashMap<String, Object>();
		// 说明没有报名
		if (activityUser == null) {
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_NOT_SIGNUP_ERROR));
		}
		ImperialExaminationType type = ImperialExaminationType.PROVINCIAL_EXAMINATION;
		// 如果当前是报名阶段，已报名跳转到乡试阶段，理解为是乡试阶段准备阶段
		if (time.getProcess() == ImperialExaminationProcess.PROCESS_SIGNUP) {
			time = imperialService.get(ImperialExaminationProcess.PROCESS_PROVINCIAL1, code);
		}
		if (time.getProcess() == ImperialExaminationProcess.PROCESS_AWARDS) {
			time = imperialService.get(ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL5, code);
			data.put("isAwards", true);
		} else {
			data.put("isAwards", false);
		}
		if (time.getProcess() == ImperialExaminationProcess.PROCESS_TOTALRANKING) {
			time = imperialService.get(ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL5, code);
		}
		if (time.getProcess().getParentId() == 1) {
			// 乡试
			type = ImperialExaminationType.PROVINCIAL_EXAMINATION;
		} else if (time.getProcess().getParentId() == 2) {
			// 会试
			type = ImperialExaminationType.METROPOLITAN_EXAMINATION;
		} else if (time.getProcess().getParentId() == 3 || time.getProcess().getParentId() == 4) {
			// 殿试
			type = ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION;
		}

		// @since 11-23 如果当前位于乡试会试公布成绩阶段,公布成绩开始时间加3天后开放下一阶段
		if (time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL5) {
			long addTime = time.getStartTime().getTime() + 3 * 24 * 60 * 60 * 1000;
			if (System.currentTimeMillis() > addTime) {
				// 会试
				type = ImperialExaminationType.METROPOLITAN_EXAMINATION;
				for (ImperialExaminationProcessTime t : timeList) {
					if (t.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN1) {
						time = t;
						break;
					}
				}
			}
		} else if (time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN5) {
			long addTime = time.getStartTime().getTime() + 3 * 24 * 60 * 60 * 1000;
			if (System.currentTimeMillis() > addTime) {
				// 殿试
				type = ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION;
				for (ImperialExaminationProcessTime t : timeList) {
					if (t.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL1) {
						time = t;
						break;
					}
				}
			}
		}

		data.put("type", type);

		// 对应界面1.准备阶段/2.学生答题/3.批改下发/4.公布成绩
		int process = 1;
		Date processEndTime = null; // 对应着四个的结束时间，页面用来到截止时间刷新
		// 如果当前时间在乡试/会试/殿试的答题时间和批改下发时间范围内，查出题目的属性和学生的答题情况
		if (time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL2
				|| time.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL2
				|| time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN2
				|| time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL3
				|| time.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL3
				|| time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN3) {
			// 这边要关联tag,room感觉不用关联
			List<ImperialExaminationHomework> list = examHkService.list(code, type, Security.getUserId(), 1, null);
			Long homeworkId = list.get(0).getHomeworkId();
			Homework hk = hkService.get(homeworkId);
			// 活动概要
			data.put("outline", this.setTime(outlineConvert.to(hk), code, type));
			List<Long> hkIds = new ArrayList<Long>();
			for (ImperialExaminationHomework examHk : list) {
				hkIds.add(examHk.getHomeworkId());
			}
			// 作业对应信息，多个班级会有多条记录
			data.put("classList", examClazzConvert.to(hkService.mgetList(hkIds)));
			if (time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL2
					|| time.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL2
					|| time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN2) {
				// 学生答题
				process = 2;
			} else {
				// 作业下发批改
				process = 3;
				// 如果有班级试题没有下发,页面有下发按钮提示
				boolean isAllIssue = examHkService.isAllIssue(code, type, Security.getUserId(), 1, null);
				data.put("isAllIssue", isAllIssue);
			}
		} else if (time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL5
				|| time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN5
				|| time.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL5
				|| time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL4
				|| time.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL4
				|| time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN4) {
			process = 4;
			ImperialExaminationActivityRank myBestRank = rankService.queryBest(code, type, Security.getUserId());
			Date homeworkEndTime = new Date(); // 作业下发截止时间
			// 判断作业下发时间
			for (ImperialExaminationProcessTime processTime : timeList) {
				if (type == ImperialExaminationType.PROVINCIAL_EXAMINATION) {
					if (processTime.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL3) {
						homeworkEndTime = processTime.getEndTime();
					}
				} else if (type == ImperialExaminationType.METROPOLITAN_EXAMINATION) {
					if (processTime.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN3) {
						homeworkEndTime = processTime.getEndTime();
					}
				} else {
					if (processTime.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL3) {
						homeworkEndTime = processTime.getEndTime();
					}
				}
			}
			boolean isExistIssue = examHkService.isExistIssueByTag(code, type, Security.getUserId(), 1,
					homeworkEndTime);
			Homework hk = null;
			// 说明当前老师没有已下发的试题
			int tag = 1;
			if (myBestRank == null) {
				// isExistIssue = false;
				// sql中对tag进行排序,先取tag大的作业
				hk = hkService.get(examHkService.getHkId(code, type, Security.getUserId()));
			} else {
				// 取任意班级作业id，取试题概览,同一阶段，不同班级试题是一样的
				ImperialExaminationHomework examKk = examHkService.get(myBestRank.getActivityHomeworkId());
				tag = examKk.getTag();
				hk = hkService.get(examKk.getHomeworkId());
			}

			data.put("isExistIssue", isExistIssue);

			// 是否无人提交作业
			boolean notSubmit = true;
			if (hk.getCommitCount() > 0) {
				notSubmit = false;
			}
			data.put("notSubmit", notSubmit);

			UserConvertOption option = new UserConvertOption();
			option.setInitMemberType(true);
			VUser vUser = userConvert.get(Security.getUserId(), option);
			VImperialExaminationScore myBestScore = scoreConvert.to(hk);
			// 存在下发的试题，不然不会有成绩
			if (isExistIssue && myBestRank != null) {
				// 本场成绩
				myBestScore.setScore(myBestRank.getManualScore());
				myBestScore.setRank(rankService.getRank2(code, type, myBestRank.getManualScore(),
						myBestRank.getDoTime(), activityUser.getRoom()));
				myBestScore.setBestClassName(classService.get(hk.getHomeworkClassId()).getName());
				myBestScore.setTeacherName(vUser.getName().substring(0, 1) + "老师");
				myBestScore.setImageUrl(vUser.getAvatarUrl());
				myBestScore.setUserId(Security.getUserId());
				myBestScore.setMemberType(vUser.getMemberType());
				myBestScore.setTag(tag);
			} else {
				// 没成绩不返回
				// myBestScore.setScore(0);
			}
			data.put("myBestScore", myBestScore);

			// 成绩调整阶段页面不返回排名(页面显示还在统计中)，成绩公布阶段返回排名
			if (time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL5
					|| time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN5
					|| time.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL5) {
				// 排名前十
				List<Map> rankList = rankService.queryTopList2(code, type, 10, activityUser.getRoom());
				List<Long> userIds = new ArrayList<Long>();
				List<Long> classIds = new ArrayList<Long>();
				List<VImperialExaminationScore> scoreList = new ArrayList<VImperialExaminationScore>();
				for (Map map : rankList) {
					userIds.add(Long.parseLong(map.get("user_id").toString()));
					classIds.add(Long.parseLong(map.get("clazz_id").toString()));
					VImperialExaminationScore v = new VImperialExaminationScore();
					v.setBestClassId(Long.parseLong(map.get("clazz_id").toString()));
					v.setScore(Integer.parseInt(map.get("manual_score").toString()));
					v.setUserId(Long.parseLong(map.get("user_id").toString()));
					v.setHomeworkTime(Integer.parseInt(map.get("do_time").toString()));
					v.setTag(Integer.parseInt(map.get("tag").toString()));
					scoreList.add(v);
				}

				Map<Long, VUser> userMap = userConvert.mget(userIds, option);
				Map<Long, HomeworkClazz> clazzMap = classService.mget(classIds);
				for (VImperialExaminationScore v : scoreList) {
					v.setBestClassName(clazzMap.get(v.getBestClassId()).getName());
					v.setTeacherName(userMap.get(v.getUserId()).getName().substring(0, 1) + "老师");
					v.setImageUrl(userMap.get(v.getUserId()).getAvatarUrl());
					v.setMemberType(userMap.get(v.getUserId()).getMemberType());
				}
				data.put("rankList", scoreList);
			}

		}
		// 根据这个阶段，页面控制显示小贴士、考试提交情况、考试成绩情况
		data.put("process", process);
		processEndTime = imperialService.get(time.getProcess(), code).getEndTime();
		data.put("processEndTime", processEndTime);

		if (process == 4) {
			// 奖券信息 乡试会试殿试的公布成绩阶段才有此返回值
			ImperialExaminationActivityLottery lottery = null;
			// 乡试
			if (type == ImperialExaminationType.PROVINCIAL_EXAMINATION) {
				lottery = imperialExaminationActivityLotteryService.getLotteryByUser(code, Security.getUserId(),
						ImperialExaminationProcess.PROCESS_PROVINCIAL5, Status.ENABLED);
			} else if (type == ImperialExaminationType.METROPOLITAN_EXAMINATION) {
				// 会试
				lottery = imperialExaminationActivityLotteryService.getLotteryByUser(code, Security.getUserId(),
						ImperialExaminationProcess.PROCESS_METROPOLITAN5, Status.ENABLED);
			} else if (type == ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION) {
				// 殿试
				lottery = imperialExaminationActivityLotteryService.getLotteryByUser(code, Security.getUserId(),
						ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL5, Status.ENABLED);
			}

			data.put("lottery", lottery);

			// 冲刺题按钮,只有公布成绩阶段才有此按钮
			int dashCount = 0; // 剩余冲刺机会
			int sprintStatus = 0; // 0:不在冲刺中状态,1:第一次冲刺中,2:第二次冲刺中,3:不能冲刺
			// 校验时间
			if (checkSprintTime(activity.getCfg().getTimeList(), type)) {
				List<ImperialExaminationHomework> ieHomeworks = examHkService.list(code, type, Security.getUserId(), 2,
						null);

				if (CollectionUtils.isEmpty(ieHomeworks)) {
					dashCount = 2;
				} else {
					// 需要判断作业状态，如果还没下发的话不能继续冲刺
					Homework homework = hkService.get(ieHomeworks.get(0).getHomeworkId());
					if (HomeworkStatus.ISSUED == homework.getStatus()) {
						// 已下发说明第一次已经冲刺完，可以进行第二次冲刺
						ieHomeworks = examHkService.list(code, type, Security.getUserId(), 3, null);
						if (CollectionUtils.isEmpty(ieHomeworks)) {
							dashCount = 1;
						} else {
							homework = hkService.get(ieHomeworks.get(0).getHomeworkId());
							if (HomeworkStatus.ISSUED == homework.getStatus()) {
								dashCount = 0;
							} else {
								// 第二次冲刺中
								sprintStatus = 2;
							}
						}
					} else {
						// 第一次冲刺中
						sprintStatus = 1;
						dashCount = 1;
					}
				}
			} else {
				sprintStatus = 3;
			}
			data.put("sprintCount", dashCount);
			data.put("sprintStatus", sprintStatus);
		}

		return new Value(data);
	}

	/**
	 * 统一给VO设置答题开始时间和结束时间
	 * 
	 * @param v
	 * @param code
	 * @return
	 */
	public VImperialExaminationOutline setTime(VImperialExaminationOutline v, Long code, ImperialExaminationType type) {
		ImperialExaminationProcessTime time = null;
		if (type == ImperialExaminationType.PROVINCIAL_EXAMINATION) {
			time = imperialService.get(ImperialExaminationProcess.PROCESS_PROVINCIAL2, code);
		} else if (type == ImperialExaminationType.METROPOLITAN_EXAMINATION) {
			time = imperialService.get(ImperialExaminationProcess.PROCESS_METROPOLITAN2, code);
		} else if (type == ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION) {
			time = imperialService.get(ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL2, code);
		}
		v.setAnswerStartAt(time.getStartTime());
		v.setAnswerEndAt(time.getEndTime());
		return v;
	}

	/**
	 * 抽奖
	 * 
	 * @param code
	 *            活动code
	 * @param lotteryId
	 *            lottery表主键
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "luckyDraw", method = { RequestMethod.POST, RequestMethod.GET })
	public Value luckyDraw(Long code, Long lotteryId) {
		ImperialExaminationActivityUser activityUser = activityUserService.getUser(code, Security.getUserId());
		if (activityUser == null) {
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_NOT_SIGNUP_ERROR));
		}
		ImperialExaminationActivityLottery lottery = imperialExaminationActivityLotteryService.luckDraw(code, lotteryId,
				activityUser.getRoom(), Security.getUserType());
		if (lottery == null) {
			return new Value(new EntityNotFoundException());
		}

		Map<String, Object> data = new HashMap<String, Object>(1);
		// 1.添加奖券
		VImperialExaminationActivityLottery vLottery = new VImperialExaminationActivityLottery();
		vLottery.setId(lottery.getId());
		vLottery.setCode(lottery.getActivityCode());
		vLottery.setName(lottery.getName());
		data.put("lottery", vLottery);

		return new Value(data);
	}

	/**
	 * 提交冲刺
	 * 
	 * @param code
	 *            活动code
	 * @param type
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "sprintSubmit", method = { RequestMethod.POST, RequestMethod.GET })
	public Value sprintSubmit(Long code, ImperialExaminationType type, Integer sprint) {
		// 判断活动是否存在
		ImperialExaminationActivity activity = imperialService.getActivity(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		// 1.判断用户有没有报名
		ImperialExaminationActivityUser activityUser = activityUserService.getUser(code, Security.getUserId());
		if (activityUser == null) {
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_NOT_SIGNUP_ERROR));
		}
		List<Long> classIds = activityUser.getClassList();

		// 2.判断当前ImperialExaminationType,如果已经超过当前ImperialExaminationType的公布时间,则不能冲刺
		if (!checkSprintTime(activity.getCfg().getTimeList(), type)) {
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_ACTIVITY_SPRINT_TIMEOUT));
		}

		// 3.判断当前是否冲刺过,不能的话抛异常 用sprint判断
		if (sprint == null || sprint > 2 || sprint < 0) {
			return new Value(new MissingArgumentException());
		}
		sprint = sprint + 1; // 第x次冲刺要和tag对应
		List<ImperialExaminationHomework> ieHomeworks = examHkService.list(code, type, Security.getUserId(), sprint,
				null);
		if (CollectionUtils.isNotEmpty(ieHomeworks)) {
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_ACTIVITY_SPRINT_SAME));
		}

		// 4.从imperial_exam_2_question表取对应的题目id，组成练习
		List<ImperialExamination2Question> questions = imperialExamination2QuestionService.getQuestions(code, type,
				activityUser.getGrade(), activityUser.getTextbookCategoryCode(), sprint);
		if (CollectionUtils.isEmpty(questions)) {
			return new Value();
		}

		List<Long> questionIds = questions.stream().map(p -> p.getQuestionId()).collect(Collectors.toList());

		// 5.参照ProcessFinalExamination2Handle处理,需要走整个布置作业流程
		// 取作业名和截止时间
		Map<String, Object> nameMap = getSprintNameTime(activity.getCfg().getTimeList(), type, sprint);
		String homeworkName = (String) nameMap.get("name");
		long homeworkEndTime = (long) nameMap.get("endTime");
		// 知识点
		List<QuestionKnowledge> knowledges = questionKnowledgeService.findByQuestions(questionIds);
		Set<Long> knowledgesSet = new HashSet<Long>();
		for (QuestionKnowledge qk : knowledges) {
			knowledgesSet.add(qk.getKnowledgeCode());
		}
		List<Long> knowledgePoints = Lists.newArrayList(knowledgesSet);
		PublishHomeworkForm form = new PublishHomeworkForm();
		form.setqIds(questionIds);
		form.setHomeworkClassIds(classIds);
		form.setName(homeworkName);
		form.setKnowledgePoints(knowledgePoints);
		form.setStartTime(System.currentTimeMillis());
		form.setAutoIssue(false);
		form.setDeadline(homeworkEndTime);
		form.setDifficulty(getDifficulty(questionIds));
		form.setCreateId(Security.getUserId());
		form.setFromCar(false);

		// 布置作业
		try {
			// form.setCorrectLastHomework(Env.getBoolean("homework.correct"));
			form.setCorrectLastHomework(false);
			form.setCorrectingType(HomeworkCorrectingType.TEACHER);
			final List<Homework> homeworks = homeworkService.publish2(form);

			Homework homework = homeworks.get(0);
			executor.execute(new Runnable() {
				@Override
				public void run() {
					for (Homework hk : homeworks) {
						if (hk.getStatus() == HomeworkStatus.PUBLISH) {
							homeworkService.publishHomework2(hk.getId());
							JSONObject jo = new JSONObject();
							jo.put("teacherId", hk.getCreateId());
							jo.put("homeworkId", hk.getId());
							mqSender.send(MqYoomathHomeworkRegistryConstants.EX_YM_HOMEWORK,
									MqYoomathHomeworkRegistryConstants.RK_YM_HOMEWORK_PUBLISH,
									MQ.builder().data(jo).build());
						}
					}
				}
			});

			// 通知
			if (homework.getId() != 0) {
				zyCorrectUserService.asyncNoticeUser(homework.getId());
				mqSender.send(MqYoomathPushRegistryConstants.EX_YM_PUSH, MqYoomathPushRegistryConstants.RK_YM_PUSH,
						MQ.builder().data(new PushHandleForm(PushHandleAction.NEW_HOMEWORK, homework.getId())).build());
				zyWXMessageService.sendPublishHomeworkMessage(form.getHomeworkClassIds(), homework.getId(), null);
			}

			// 更新活动ImperialExaminationHomework表
			for (Homework value : homeworks) {
				ImperialExaminationHomework ieHomework = new ImperialExaminationHomework();
				ieHomework.setActivityCode(code);
				ieHomework.setClazzId(value.getHomeworkClassId());
				ieHomework.setGrade(activityUser.getGrade());
				ieHomework.setHomeworkId(value.getId());
				ieHomework.setRoom(activityUser.getRoom());
				ieHomework.setTag(sprint);
				ieHomework.setTextbookCategoryCode(activityUser.getTextbookCategoryCode());
				ieHomework.setType(type);
				ieHomework.setUserId(Security.getUserId());

				examHkService.save(ieHomework);

				// imperial_exam_homework_student表添加记录
				List<Long> studentIds = homeworkStudentClazzService.listClassStudents(value.getHomeworkClassId());
				List<ImperialExaminationHomeworkStudent> entitys = Lists.newArrayList();
				for (Long studentId : studentIds) {
					ImperialExaminationHomeworkStudent entity = new ImperialExaminationHomeworkStudent();
					entity.setActivityCode(code);
					entity.setClazzId(value.getHomeworkClassId());
					entity.setGrade(activityUser.getGrade());
					entity.setHomeworkId(value.getId());
					entity.setRoom(activityUser.getRoom());
					entity.setTag(sprint);
					entity.setTextbookCategoryCode(activityUser.getTextbookCategoryCode());
					entity.setType(type);
					entity.setUserId(studentId);

					entitys.add(entity);
				}
				examnHomeworkStudentService.save(entitys);
			}
			int dashCount = 2 - sprint; // 剩余冲刺机会
			int sprintStatus = sprint - 1; // 0:不在冲刺中状态,1:第一次冲刺中,2:第二次冲刺中,3:不能冲刺

			Map<String, Object> data = new HashMap<>();
			data.put("sprintCount", dashCount);
			data.put("sprintStatus", sprintStatus);
			return new Value(data);
		} catch (Exception e) {
			return new Value(new ServerException());
		}
	}

	/**
	 * 根据ImperialExaminationType校验是否超过相应成绩公布时间
	 * 
	 * @param processTimes
	 * @param type
	 */
	private boolean checkSprintTime(List<ImperialExaminationProcessTime> processTimes, ImperialExaminationType type) {
		long now = System.currentTimeMillis(); // 当前时间
		long provincial = 0L;
		long metropolitan = 0L;
		for (ImperialExaminationProcessTime time : processTimes) {
			if (time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL5) {
				provincial = time.getEndTime().getTime();
			} else if (time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN5) {
				metropolitan = time.getEndTime().getTime();
			}
		}

		boolean result = false;
		if (type == ImperialExaminationType.PROVINCIAL_EXAMINATION) {
			if (now <= provincial) {
				result = true;
			}
		} else if (type == ImperialExaminationType.METROPOLITAN_EXAMINATION) {
			if (now <= metropolitan) {
				result = true;
			}
		}

		return result;
	}

	/**
	 * 计算作业难度
	 * 
	 * @param ids
	 *            questionIds
	 */
	private BigDecimal getDifficulty(Collection<Long> ids) {
		List<Question> qList = questionService.mgetList(ids);
		double sum = 0.0;
		for (Question q : qList) {
			sum += q.getDifficulty();
		}
		return new BigDecimal(sum / qList.size());
	}

	/**
	 * 取冲刺题作业名和截止时间
	 * 
	 * @param processTimes
	 * @param type
	 * @param sprint
	 * @return map name:作业名,endTime:截止时间
	 */
	private Map<String, Object> getSprintNameTime(List<ImperialExaminationProcessTime> processTimes,
			ImperialExaminationType type, int sprint) {
		Map<String, Object> data = new HashMap<>();
		String name = "";
		long endTime = 0L;
		if (type == ImperialExaminationType.PROVINCIAL_EXAMINATION) {
			if (sprint == 2) {
				name = "科举大典-乡试冲刺考试A卷";
			} else {
				name = "科举大典-乡试冲刺考试B卷";
			}
			for (ImperialExaminationProcessTime time : processTimes) {
				if (time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL5) {
					endTime = time.getEndTime().getTime();
				}
			}
		} else if (type == ImperialExaminationType.METROPOLITAN_EXAMINATION) {
			if (sprint == 2) {
				name = "科举大典-会试冲刺考试A卷";
			} else {
				name = "科举大典-会试冲刺考试B卷";
			}
			for (ImperialExaminationProcessTime time : processTimes) {
				if (time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN5) {
					endTime = time.getEndTime().getTime();
				}
			}
		}

		data.put("name", name);
		data.put("endTime", endTime);
		return data;
	}

	/**
	 * 保存活动问题,临时用
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "saveQuestion", method = { RequestMethod.POST, RequestMethod.GET })
	public Value saveQuestion(Long code, ImperialExaminationGrade grade, Long questionId, Integer room,
			Integer sequence, Integer tag, ImperialExaminationType type) {
		ImperialExamination2Question question = new ImperialExamination2Question();
		question.setActivityCode(code);
		question.setGrade(grade);
		question.setQuestionId(questionId);
		question.setRoom(room);
		question.setSequence(sequence);
		question.setTag(tag);
		question.setTextbookCategoryCode(15);
		question.setType(type);
		imperialExamination2QuestionService.save(question);
		return new Value();
	}

	/***
	 * 切换乡试、会试、殿试.为保证代码清晰，和上一个接口分开,返回值一样<br>
	 * 1.当会试的时候可以查看乡试的公布成绩阶段<br>
	 * 2.当殿试的时候可以查看会试和乡试的公布成绩阶段<br>
	 * 3.再调用这个方法之前，需要前端记住默认的阶段是哪一个，最好默认的数据不需要再访问一次接口了
	 * 
	 * @param type
	 * @param code
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "change", method = { RequestMethod.POST, RequestMethod.GET })
	public Value change(ImperialExaminationType type, Long code) {
		ImperialExaminationActivity activity = imperialService.getActivity(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		ImperialExaminationProcessTime time = activity.getCfg().getCurretStage();
		if (time == null) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_TIME_ERROR));
		}
		// if (time.getProcess().getParentId() == 1) {
		// if (type == ImperialExaminationType.METROPOLITAN_EXAMINATION) {
		// return new Value(new YoomathMobileException(
		// YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_METROPOLITAN_AUTHORITY_ERROR));
		// } else if (type ==
		// ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION) {
		// return new Value(new YoomathMobileException(
		// YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_FINAL_AUTHORITY_ERROR));
		// }
		// return new Value();
		// } else if (time.getProcess().getParentId() == 2) {
		// if (type == ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION) {
		// return new Value(new YoomathMobileException(
		// YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_FINAL_AUTHORITY_ERROR));
		// }
		// } else if (time.getProcess().getParentId() == 0) {
		// if (type == ImperialExaminationType.METROPOLITAN_EXAMINATION) {
		// return new Value(new YoomathMobileException(
		// YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_METROPOLITAN_AUTHORITY_ERROR));
		// } else if (type ==
		// ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION) {
		// return new Value(new YoomathMobileException(
		// YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_FINAL_AUTHORITY_ERROR));
		// }
		// }

		ImperialExaminationActivityUser activityUser = activityUserService.getUser(code, Security.getUserId());
		// 说明没有报名
		if (activityUser == null) {
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_NOT_SIGNUP_ERROR));
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("type", type);
		int process = 4;
		ImperialExaminationActivityRank myBestRank = rankService.queryBest(code, type, Security.getUserId());

		Date homeworkEndTime = new Date(); // 作业下发截止时间
		// 判断作业下发时间
		List<ImperialExaminationProcessTime> timeList = activity.getCfg().getTimeList();
		for (ImperialExaminationProcessTime processTime : timeList) {
			if (type == ImperialExaminationType.PROVINCIAL_EXAMINATION) {
				if (processTime.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL3) {
					homeworkEndTime = processTime.getEndTime();
				}
			} else if (type == ImperialExaminationType.METROPOLITAN_EXAMINATION) {
				if (processTime.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN3) {
					homeworkEndTime = processTime.getEndTime();
				}
			} else {
				if (processTime.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL3) {
					homeworkEndTime = processTime.getEndTime();
				}
			}
		}
		boolean isExistIssue = examHkService.isExistIssueByTag(code, type, Security.getUserId(), 1, homeworkEndTime);
		Homework hk = null;
		// 说明当前老师没有已下发的试题
		int tag = 1;
		if (myBestRank == null) {
			isExistIssue = false;
			hk = hkService.get(examHkService.getHkId(code, type, Security.getUserId()));
		} else {
			// 取任意班级作业id，取试题概览,同一阶段，不同班级试题是一样的
			ImperialExaminationHomework examKk = examHkService.get(myBestRank.getActivityHomeworkId());
			tag = examKk.getTag();
			hk = hkService.get(examKk.getHomeworkId());
		}
		// 活动概要
		// data.put("outline", this.setTime(outlineConvert.to(hk), code, type));
		data.put("isExistIssue", isExistIssue);
		UserConvertOption option = new UserConvertOption();
		option.setInitMemberType(true);
		VUser vUser = userConvert.get(Security.getUserId(), option);
		VImperialExaminationScore myBestScore = scoreConvert.to(hk);
		// 存在下发的试题，不然不会有成绩
		if (isExistIssue) {
			// 本场成绩
			myBestScore.setScore(myBestRank.getManualScore());
			myBestScore.setRank(rankService.getRank2(code, type, myBestRank.getManualScore(), myBestRank.getDoTime(),
					activityUser.getRoom()));
		} else {
			// myBestScore.setScore(0);
		}
		myBestScore.setBestClassName(classService.get(hk.getHomeworkClassId()).getName());
		myBestScore.setTeacherName(vUser.getName().substring(0, 1) + "老师");
		myBestScore.setImageUrl(vUser.getAvatarUrl());
		myBestScore.setUserId(Security.getUserId());
		myBestScore.setTag(tag);
		myBestScore.setMemberType(vUser.getMemberType());
		data.put("myBestScore", myBestScore);

		// 排名前十
		List<Map> rankList = rankService.queryTopList2(code, type, 10, activityUser.getRoom());
		List<Long> userIds = new ArrayList<Long>();
		List<Long> classIds = new ArrayList<Long>();
		List<VImperialExaminationScore> scoreList = new ArrayList<VImperialExaminationScore>();
		for (Map map : rankList) {
			userIds.add(Long.parseLong(map.get("user_id").toString()));
			classIds.add(Long.parseLong(map.get("clazz_id").toString()));
			VImperialExaminationScore v = new VImperialExaminationScore();
			v.setBestClassId(Long.parseLong(map.get("clazz_id").toString()));
			v.setScore(Integer.parseInt(map.get("manual_score").toString()));
			v.setUserId(Long.parseLong(map.get("user_id").toString()));
			v.setTag(Integer.parseInt(map.get("tag").toString()));
			scoreList.add(v);
		}

		Map<Long, VUser> userMap = userConvert.mget(userIds, option);
		Map<Long, HomeworkClazz> clazzMap = classService.mget(classIds);
		for (VImperialExaminationScore v : scoreList) {
			v.setBestClassName(clazzMap.get(v.getBestClassId()).getName());
			v.setTeacherName(userMap.get(v.getUserId()).getName().substring(0, 1) + "老师");
			v.setImageUrl(userMap.get(v.getUserId()).getAvatarUrl());
			v.setMemberType(userMap.get(v.getUserId()).getMemberType());
		}
		data.put("rankList", scoreList);
		data.put("process", process);

		// 奖券信息 乡试会试殿试的公布成绩阶段才有此返回值
		ImperialExaminationActivityLottery lottery = null;
		// 乡试
		if (type == ImperialExaminationType.PROVINCIAL_EXAMINATION) {
			lottery = imperialExaminationActivityLotteryService.getLotteryByUser(code, Security.getUserId(),
					ImperialExaminationProcess.PROCESS_PROVINCIAL5, Status.ENABLED);
		} else if (type == ImperialExaminationType.METROPOLITAN_EXAMINATION) {
			// 会试
			lottery = imperialExaminationActivityLotteryService.getLotteryByUser(code, Security.getUserId(),
					ImperialExaminationProcess.PROCESS_METROPOLITAN5, Status.ENABLED);
		} else if (type == ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION) {
			// 殿试
			lottery = imperialExaminationActivityLotteryService.getLotteryByUser(code, Security.getUserId(),
					ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL5, Status.ENABLED);
		}

		data.put("lottery", lottery);

		// 冲刺题按钮,只有公布成绩阶段才有此按钮
		int dashCount = 0; // 剩余冲刺机会
		int sprintStatus = 0; // 0:不在冲刺中状态,1:第一次冲刺中,2:第二次冲刺中,3:不能冲刺
		// 校验时间
		if (checkSprintTime(activity.getCfg().getTimeList(), type)) {
			List<ImperialExaminationHomework> ieHomeworks = examHkService.list(code, type, Security.getUserId(), 2,
					null);

			if (CollectionUtils.isEmpty(ieHomeworks)) {
				dashCount = 2;
			} else {
				// 需要判断作业状态，如果还没下发的话不能继续冲刺
				Homework homework = hkService.get(ieHomeworks.get(0).getHomeworkId());
				if (HomeworkStatus.ISSUED == homework.getStatus()) {
					// 已下发说明第一次已经冲刺完，可以进行第二次冲刺
					ieHomeworks = examHkService.list(code, type, Security.getUserId(), 3, null);
					if (CollectionUtils.isEmpty(ieHomeworks)) {
						dashCount = 1;
					} else {
						homework = hkService.get(ieHomeworks.get(0).getHomeworkId());
						if (HomeworkStatus.ISSUED == homework.getStatus()) {
							dashCount = 0;
						} else {
							// 第二次冲刺中
							sprintStatus = 2;
						}
					}
				} else {
					// 第一次冲刺中
					sprintStatus = 1;
					dashCount = 1;
				}
			}
		} else {
			sprintStatus = 3;
		}
		data.put("sprintCount", dashCount);
		data.put("sprintStatus", sprintStatus);
		return new Value(data);
	}

	/**
	 * 分享接口,返回biz和bizId
	 * 
	 * @param code
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "share", method = { RequestMethod.POST, RequestMethod.GET })
	public Value share(Long code) {
		Map<String, Object> data = new HashMap<>();
		data.put("biz", Biz.ACTIVITY);
		data.put("bizId", code);

		return new Value(data);
	}
}

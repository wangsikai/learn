package com.lanking.uxb.service.imperial.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivity;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityAward;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityCfg;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityRank;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityUser;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationAwardType;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationHomework;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcess;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcessTime;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationUserProcess;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityAwardService;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityRankService;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityService;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityUserService;
import com.lanking.uxb.service.imperial.api.ImperialExaminationHomeworkService;
import com.lanking.uxb.service.imperial.convert.ImperialExaminationActivityAwardConvert;
import com.lanking.uxb.service.imperial.convert.ImperialExaminationActivityConvert;
import com.lanking.uxb.service.imperial.convert.ImperialExaminationClazzConvert;
import com.lanking.uxb.service.imperial.convert.ImperialExaminationOutlineConvert;
import com.lanking.uxb.service.imperial.convert.ImperialExaminationScoreConvert;
import com.lanking.uxb.service.imperial.form.ImperialExaminationActivityUserForm;
import com.lanking.uxb.service.imperial.value.VImperialExaminationActivity;
import com.lanking.uxb.service.imperial.value.VImperialExaminationActivityAward;
import com.lanking.uxb.service.imperial.value.VImperialExaminationAwardDatas;
import com.lanking.uxb.service.imperial.value.VImperialExaminationOutline;
import com.lanking.uxb.service.imperial.value.VImperialExaminationScore;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;

/**
 * 科举考试
 * 
 * @author wangsenhao
 *
 */
@RestController
@RequestMapping("zy/m/t/imperial")
public class ZyMTeaImperialExaminationController {

	@Autowired
	private ImperialExaminationActivityService imperialService;
	@Autowired
	private ImperialExaminationActivityUserService activityUserService;
	@Autowired
	private ImperialExaminationActivityConvert activityConvert;
	@Autowired
	private ImperialExaminationActivityAwardService activityAwardService;
	@Autowired
	private ImperialExaminationActivityAwardConvert activityAwardConvert;
	@Autowired
	private AccountService accountService;
	@Autowired
	private ZyHomeworkClassService classService;
	@Autowired
	private ImperialExaminationHomeworkService examHkService;
	@Autowired
	private HomeworkService hkService;
	@Autowired
	private ImperialExaminationActivityRankService rankService;
	@Autowired
	private ImperialExaminationOutlineConvert outlineConvert;
	@Autowired
	private ImperialExaminationScoreConvert scoreConvert;
	@Autowired
	private ImperialExaminationClazzConvert examClazzConvert;
	@Autowired
	private UserConvert userConvert;

	/**
	 * 获取当前时间对应的阶段，用来判断使用哪一个页面、调哪一个接口
	 * 
	 * @param code
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "getCurretStage", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getCurretStage(Long code) {
		ImperialExaminationActivity activity = imperialService.getActivity(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
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
		return new Value(flag);
	}

	/**
	 * 根据当前时间比较，不同阶段获取不同倒计时<br>
	 * 
	 * @param code
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "getCountDownTime", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getCountDownTime(Long code) {
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
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "getActivityCfg", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getActivityCfg(Long code) {
		ImperialExaminationActivity activity = imperialService.getActivity(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		VImperialExaminationActivity v = activityConvert.to(activity);
		List<ImperialExaminationProcessTime> timeList = activity.getCfg().getTimeList();
		for (ImperialExaminationProcessTime time : timeList) {
			if (time.getProcess() == ImperialExaminationProcess.PROCESS_SIGNUP) {
				v.setSignUpStartTime(time.getStartTime());
				v.setSignUpEndTime(time.getEndTime());
				break;
			}
		}
		v.setTimeList(imperialService.queryExamTime(code));
		return new Value(v);
	}

	/**
	 * 获取报名相关数据<br>
	 * 1.手机号码<br>
	 * 2.参赛年级<br>
	 * 3.参赛班级
	 * 
	 * @param code
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "querySignUpInfo", method = { RequestMethod.POST, RequestMethod.GET })
	public Value querySignUpInfo(Long code) {
		Map<String, Object> data = new HashMap<String, Object>();
		Account account = accountService.getAccountByUserId(Security.getUserId());
		data.put("mobile", account.getMobile());
		ImperialExaminationActivity activity = imperialService.getActivity(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		data.put("grades", activity.getCfg().getGrades());
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
		activityUserService.signUp(form);
		return new Value();
	}

	/**
	 * 临时接口，后面删除
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "createData", method = { RequestMethod.POST, RequestMethod.GET })
	public Value createData() {
		imperialService.createData();
		return new Value();
	}

	/**
	 * 获取活动信息, 临时测试使用.
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "getTestCfg", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getTestCfg(Long code) {
		if (code != null) {
			ImperialExaminationActivity activity = imperialService.getActivity(code);
			if (activity != null) {
				return new Value(activity.getCfg());
			}
		}
		return new Value();
	}

	/**
	 * 根据时间判断当前的阶段<br>
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
			List<ImperialExaminationHomework> list = examHkService.list(code, type, Security.getUserId(), null, null);
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
				boolean isAllIssue = examHkService.isAllIssue(code, type, Security.getUserId(), null, null);
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
			boolean isExistIssue = examHkService.isExistIssue(code, type, Security.getUserId());
			Homework hk = null;
			// 说明当前老师没有已下发的试题
			if (myBestRank == null) {
				isExistIssue = false;
				hk = hkService.get(examHkService.getHkId(code, type, Security.getUserId()));
			} else {
				// 取任意班级作业id，取试题概览,同一阶段，不同班级试题是一样的
				ImperialExaminationHomework examKk = examHkService.get(myBestRank.getActivityHomeworkId());
				hk = hkService.get(examKk.getHomeworkId());
			}
			// 活动概要
			data.put("outline", this.setTime(outlineConvert.to(hk), code, type));
			data.put("isExistIssue", isExistIssue);
			VUser vUser = userConvert.get(Security.getUserId());
			VImperialExaminationScore myBestScore = scoreConvert.to(hk);
			// 存在下发的试题，不然不会有成绩
			if (isExistIssue) {
				// 本场成绩
				myBestScore.setScore(myBestRank.getManualScore());
				myBestScore
						.setRank(rankService.getRank(code, type, myBestRank.getManualScore(), myBestRank.getDoTime()));
			} else {
				myBestScore.setScore(0);
			}
			myBestScore.setBestClassName(classService.get(hk.getHomeworkClassId()).getName());
			myBestScore.setTeacherName(vUser.getName().substring(0, 1) + "老师");
			myBestScore.setImageUrl(vUser.getAvatarUrl());
			myBestScore.setUserId(Security.getUserId());
			data.put("myBestScore", myBestScore);

			// 成绩调整阶段页面不返回排名(页面显示还在统计中)，成绩公布阶段返回排名
			if (time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL5
					|| time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN5
					|| time.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL5) {
				// 排名前十
				List<Map> rankList = rankService.queryTopList(code, type, 10);
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
					scoreList.add(v);
				}
				Map<Long, VUser> userMap = userConvert.mget(userIds);
				Map<Long, HomeworkClazz> clazzMap = classService.mget(classIds);
				for (VImperialExaminationScore v : scoreList) {
					v.setBestClassName(clazzMap.get(v.getBestClassId()).getName());
					v.setTeacherName(userMap.get(v.getUserId()).getName().substring(0, 1) + "老师");
					v.setImageUrl(userMap.get(v.getUserId()).getAvatarUrl());
				}
				data.put("rankList", scoreList);
			}

		}
		// 根据这个阶段，页面控制显示小贴士、考试提交情况、考试成绩情况
		data.put("process", process);
		processEndTime = imperialService.get(time.getProcess(), code).getEndTime();
		data.put("processEndTime", processEndTime);
		return new Value(data);
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
		if (time.getProcess().getParentId() == 1) {
			if (type == ImperialExaminationType.METROPOLITAN_EXAMINATION) {
				return new Value(new YoomathMobileException(
						YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_METROPOLITAN_AUTHORITY_ERROR));
			} else if (type == ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION) {
				return new Value(new YoomathMobileException(
						YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_FINAL_AUTHORITY_ERROR));
			}
			return new Value();
		} else if (time.getProcess().getParentId() == 2) {
			if (type == ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION) {
				return new Value(new YoomathMobileException(
						YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_FINAL_AUTHORITY_ERROR));
			}
		} else if (time.getProcess().getParentId() == 0) {
			if (type == ImperialExaminationType.METROPOLITAN_EXAMINATION) {
				return new Value(new YoomathMobileException(
						YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_METROPOLITAN_AUTHORITY_ERROR));
			} else if (type == ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION) {
				return new Value(new YoomathMobileException(
						YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_FINAL_AUTHORITY_ERROR));
			}
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("type", type);
		int process = 4;
		ImperialExaminationActivityRank myBestRank = rankService.queryBest(code, type, Security.getUserId());
		boolean isExistIssue = examHkService.isExistIssue(code, type, Security.getUserId());
		Homework hk = null;
		// 说明当前老师没有已下发的试题
		if (myBestRank == null) {
			isExistIssue = false;
			hk = hkService.get(examHkService.getHkId(code, type, Security.getUserId()));
		} else {
			// 取任意班级作业id，取试题概览,同一阶段，不同班级试题是一样的
			ImperialExaminationHomework examKk = examHkService.get(myBestRank.getActivityHomeworkId());
			hk = hkService.get(examKk.getHomeworkId());
		}
		// 活动概要
		data.put("outline", this.setTime(outlineConvert.to(hk), code, type));
		data.put("isExistIssue", isExistIssue);
		VUser vUser = userConvert.get(Security.getUserId());
		VImperialExaminationScore myBestScore = scoreConvert.to(hk);
		// 存在下发的试题，不然不会有成绩
		if (isExistIssue) {
			// 本场成绩
			myBestScore.setScore(myBestRank.getManualScore());
			myBestScore.setRank(rankService.getRank(code, type, myBestRank.getManualScore(), myBestRank.getDoTime()));
		} else {
			myBestScore.setScore(0);
		}
		myBestScore.setBestClassName(classService.get(hk.getHomeworkClassId()).getName());
		myBestScore.setTeacherName(vUser.getName().substring(0, 1) + "老师");
		myBestScore.setImageUrl(vUser.getAvatarUrl());
		myBestScore.setUserId(Security.getUserId());
		data.put("myBestScore", myBestScore);

		// 排名前十
		List<Map> rankList = rankService.queryTopList(code, type, 10);
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
			scoreList.add(v);
		}
		Map<Long, VUser> userMap = userConvert.mget(userIds);
		Map<Long, HomeworkClazz> clazzMap = classService.mget(classIds);
		for (VImperialExaminationScore v : scoreList) {
			v.setBestClassName(clazzMap.get(v.getBestClassId()).getName());
			v.setTeacherName(userMap.get(v.getUserId()).getName().substring(0, 1) + "老师");
			v.setImageUrl(userMap.get(v.getUserId()).getAvatarUrl());
		}
		data.put("rankList", scoreList);
		data.put("process", process);
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
		// if (activityUser == null) {
		// return new Value(
		// new
		// YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_NOT_SIGNUP_ERROR));
		// }

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

		// 活动设置
		ImperialExaminationActivityCfg config = activity.getCfg();
		Collections.sort(config.getAwardList(), new Comparator<ImperialExaminationAwardType>() {
			@Override
			public int compare(ImperialExaminationAwardType o1, ImperialExaminationAwardType o2) {
				return o1.getAwardLevel() > o2.getAwardLevel() ? 1 : -1;
			}
		});

		// 奖品设置
		int num = 0; // 需要取得获奖的人数
		List<ImperialExaminationAwardType> awardTypes = config.getAwardList();
		for (ImperialExaminationAwardType at : awardTypes) {
			num += at.getNum();
		}
		List<ImperialExaminationActivityAward> awardList = activityAwardService.awardList(code, num);
		List<VImperialExaminationActivityAward> vawardList = activityAwardConvert.to(awardList);

		// 获奖页面数据
		VImperialExaminationAwardDatas awardDatas = new VImperialExaminationAwardDatas();
		awardDatas.setAwardTypeList(config.getAwardList()); // 奖品设置
		int fromIndex = 0;
		if (vawardList.size() > 0) {
			for (ImperialExaminationAwardType awardType : config.getAwardList()) {
				if (vawardList.size() - fromIndex <= 0) {
					break;
				}
				List<VImperialExaminationActivityAward> temps = awardType.getNum() >= (vawardList.size() - fromIndex)
						? vawardList.subList(fromIndex, vawardList.size())
						: vawardList.subList(fromIndex, fromIndex + awardType.getNum());
				if (awardType.getAwardLevel() == 1) {
					awardDatas.setAwardLevel1(temps);
				} else if (awardType.getAwardLevel() == 2) {
					awardDatas.setAwardLevel2(temps);
				} else if (awardType.getAwardLevel() == 3) {
					awardDatas.setAwardLevel3(temps);
				} else {
					awardDatas.setAwardLevel4(temps);
				}
				fromIndex += awardType.getNum();
			}
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
}

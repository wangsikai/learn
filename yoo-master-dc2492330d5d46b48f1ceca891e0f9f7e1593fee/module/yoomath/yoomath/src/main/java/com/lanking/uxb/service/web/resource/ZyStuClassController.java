package com.lanking.uxb.service.web.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.component.mq.common.constants.MqYoomathDiagnoRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.stat.StudentHomeworkStat;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.VCursorPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.account.util.ValidateUtils;
import com.lanking.uxb.service.honor.api.UserHonorService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.zuoye.api.ZyClazzJoinRequestService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStatisticService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkStatService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClassConvertOption;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkStudentClazzConvert;
import com.lanking.uxb.service.zuoye.convert.ZyStudentHomeworkStatConvert;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;
import com.lanking.uxb.service.zuoye.value.VHomeworkStudentClazz;
import com.lanking.uxb.service.zuoye.value.VStudentHomeworkStat;

/**
 * 悠作业学生班级相关接口
 * 
 * @since yoomath V1.2
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月22日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/s/class")
public class ZyStuClassController {

	@Autowired
	private ZyHomeworkStudentClazzService hkStuClazzService;
	@Autowired
	private ZyHomeworkStudentClazzConvert hkStuClazzConvert;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private ZyHomeworkClazzConvert zyHkClassConvert;
	@Autowired
	private ZyStudentHomeworkStatService zyStuHkStatService;
	@Autowired
	private ZyStudentHomeworkStatConvert zyStuHkStatConvert;
	@Autowired
	private AccountService accountService;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private ZyClazzJoinRequestService clazzJoinRequestService;
	@Autowired
	private ZyHomeworkStatisticService homeworkStatisticService;
	@Autowired
	MqSender mqSender;
	@Autowired
	private UserHonorService userHonorService;

	/**
	 * 查找班级
	 * 
	 * @since yoomath V2.3.1
	 * @param code
	 *            班级码
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "find", method = { RequestMethod.POST, RequestMethod.GET })
	public Value find(String code) {
		boolean isMobile = true;
		try {
			ValidateUtils.validateMobile(code);
		} catch (AccountException e) {
			isMobile = false;
		}
		List<HomeworkClazz> clazzs = Lists.newArrayList();
		if (!isMobile) {
			// 班级号
			HomeworkClazz clazz = zyHkClassService.findByCode(code);
			if (clazz != null) {
				clazzs.add(clazz);
			}
		} else {
			// 手机号
			Account account = accountService.getAccount(GetType.MOBILE, code);
			if (account != null) {
				User u = accountService.getUserByAccountId(account.getId());
				List<HomeworkClazz> hkClazzs = zyHkClassService.listCurrentClazzs(u.getId());
				if (CollectionUtils.isNotEmpty(hkClazzs)) {
					clazzs.addAll(hkClazzs);
				}
			}
		}
		if (CollectionUtils.isEmpty(clazzs)) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_CLASS_NOT_EXIST));
		}
		List<VHomeworkClazz> vClazzs = zyHkClassConvert.to(clazzs,
				new ZyHomeworkClassConvertOption(true, false, false));

		List<HomeworkStudentClazz> hasClazzs = hkStuClazzService.listCurrentClazzs(Security.getUserId());
		for (VHomeworkClazz vc : vClazzs) {
			for (HomeworkStudentClazz hc : hasClazzs) {
				if (hc.getClassId() == vc.getId()) {
					vc.setJoined(true);
					break;
				}
			}
		}
		return new Value(vClazzs);
	}

	/**
	 * 渠道用户查询班级
	 *
	 * @return {@link Value}
	 */
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "findClass", method = { RequestMethod.GET, RequestMethod.POST })
	public Value findClass(String key) {

		VUser user = userConvert.get(Security.getUserId());
		// 非渠道用户不可以访问此接口
		if (!user.isChannelUser()) {
			return new Value(new IllegalArgException());
		}

		// 临时修改渠道学生用户班级列表逻辑
		VCursorPage<VHomeworkClazz> vpage = new VCursorPage<VHomeworkClazz>();
		CursorPageable<Long> cursorPageable = CP.cursor(Long.MAX_VALUE, 100);

		CursorPage<Long, HomeworkClazz> resultPage = zyHkClassService.findByChannel(Security.getUserId(), key,
				cursorPageable);

		List<VHomeworkClazz> vs = zyHkClassConvert.to(resultPage.getItems(),
				new ZyHomeworkClassConvertOption(true, false, false));
		List<HomeworkStudentClazz> hasClazzs = hkStuClazzService.listCurrentClazzs(Security.getUserId());
		for (VHomeworkClazz vc : vs) {
			for (HomeworkStudentClazz hc : hasClazzs) {
				if (hc.getClassId() == vc.getId()) {
					vc.setJoined(true);
					break;
				}
			}
		}
		vpage.setItems(vs);
		Map<String, Object> dataMap = new HashMap<String, Object>(2);
		dataMap.put("clazzs", vpage);
		dataMap.put("school", user.getSchool());

		return new Value(dataMap);
	}

	/**
	 * 加入班级
	 * 
	 * @since yoomath V2.3.1
	 * @param classId
	 *            班级ID
	 * @param realName
	 *            真实姓名
	 * @return {@link Value}
	 */
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "join", method = { RequestMethod.POST, RequestMethod.GET })
	public Value join(long classId, String realName) {
		HomeworkClazz homeworkClazz = zyHkClassService.get(classId);
		if (homeworkClazz == null || homeworkClazz.getStatus() != Status.ENABLED) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_CLASS_NOT_EXIST));
		}
		// 学生已经加入班级
		if (hkStuClazzService.isJoin(homeworkClazz.getId(), Security.getUserId())) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_CLASS_JOINED));
		}
		// 班级已经锁
		if (homeworkClazz.getLockStatus() != Status.ENABLED) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_CLASSS_LOCKED));
		}
		VUser user = userConvert.get(Security.getUserId());

		if (homeworkClazz.isNeedConfirm()) {
			// 需要教师进行确认
			if (StringUtils.isBlank(realName)) {
				return new Value(new IllegalArgException());
			}
			clazzJoinRequestService.request(Security.getUserId(), homeworkClazz.getId(), homeworkClazz.getTeacherId(),
					realName);
		} else {
			// 不需教师进行确认

			try {
				// 加入班级
				hkStuClazzService.join(homeworkClazz.getId(), Security.getUserId());

				// 学生加入班级，需要重新计算班级整体统计
				homeworkStatisticService.updateTeacherHomeworkStatByClass(homeworkClazz.getId());

				// 学生加入班级，需要重新更新学生诊断相应数据 数据跟随2017.7.6 senhao.wang
				JSONObject object = new JSONObject();
				object.put("classId", homeworkClazz.getId());
				object.put("studentId", Security.getUserId());
				mqSender.send(MqYoomathDiagnoRegistryConstants.EX_YM_DIAGNO_TASK,
						MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_STUDENT_JOIN,
						MQ.builder().data(object).build());
			} catch (ZuoyeException e) {
				// 班级已经超过人数上限，不可以再加入
				if (e.getCode() == ZuoyeException.ZUOYE_CLASSSTUDENT_MAXLIMIT) {
					Integer maxJoinNum = Env.getInt("student.max.per_class");
					return new Value(new ZuoyeException(ZuoyeException.ZUOYE_STUDENT_JOIN_CLASS_MAXLIMIT, maxJoinNum));
				} else {
					return new Value(new ZuoyeException(ZuoyeException.ZUOYE_CLASS_NOT_EXIST));
				}
			}
		}
		userHonorService.asyncUptUserHonor(GrowthAction.CLASS_STU_NUM, CoinsAction.CLASS_STU_NUM,
				homeworkClazz.getTeacherId(), Biz.CLASS, classId);
		return new Value();
	}

	/**
	 * 退出班级
	 * 
	 * @since yoomath V1.2
	 * @param classId
	 *            班级ID
	 * @return {@link Value}
	 */
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "exit", method = { RequestMethod.POST, RequestMethod.GET })
	public Value exit(long classId) {
		try {
			// hkStuClazzService.exit(classId, Security.getUserId(), null);
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_STUDENT_CANNOT_EXIT));
		} catch (ZuoyeException e) {
			return new Value(e);
		}
	}

	/**
	 * 我的班级列表
	 * 
	 * @since yoomath V1.2
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "my_clazzs", method = { RequestMethod.POST, RequestMethod.GET })
	public Value myClazzs() {
		Map<String, Object> data = new HashMap<String, Object>(1);
		List<HomeworkStudentClazz> stuClazzs = hkStuClazzService.listCurrentClazzs(Security.getUserId());
		if (CollectionUtils.isEmpty(stuClazzs)) {
			data.put("clazzs", Collections.EMPTY_LIST);
		} else {
			List<VHomeworkStudentClazz> vstuClazzs = hkStuClazzConvert.to(stuClazzs);
			// 封装班级信息
			List<Long> classIds = new ArrayList<Long>(stuClazzs.size());
			for (VHomeworkStudentClazz v : vstuClazzs) {
				classIds.add(v.getClassId());
			}
			Map<Long, HomeworkClazz> clazzs = zyHkClassService.mget(classIds);
			Map<Long, VHomeworkClazz> vclazzs = zyHkClassConvert.to(clazzs,
					new ZyHomeworkClassConvertOption(true, false, false));
			for (VHomeworkStudentClazz v : vstuClazzs) {
				v.setHomeworkClazz(vclazzs.get(v.getClassId()));
			}
			// 封装学生作业统计信息
			List<StudentHomeworkStat> stats = zyStuHkStatService.getByHomeworkClassIds(Security.getUserId(), classIds);
			List<VStudentHomeworkStat> vstats = zyStuHkStatConvert.to(stats);
			Map<Long, VStudentHomeworkStat> vstatsMap = new HashMap<Long, VStudentHomeworkStat>(vstats.size());
			for (VStudentHomeworkStat v : vstats) {
				vstatsMap.put(v.getHomeworkClassId(), v);
			}
			for (VHomeworkStudentClazz v : vstuClazzs) {
				VStudentHomeworkStat vstuStat = vstatsMap.get(v.getClassId());
				if (vstuStat == null) {
					vstuStat = new VStudentHomeworkStat();
					vstuStat.setHomeworkClassId(v.getClassId());
					vstuStat.setUserId(Security.getUserId());
				}
				v.setHomeworkStat(vstuStat);
			}
			data.put("clazzs", vstuClazzs);
		}
		return new Value(data);
	}
}

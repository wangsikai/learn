package com.lanking.uxb.service.clazz.resource;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.mq.common.constants.MqYoomathDiagnoRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
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
import com.lanking.uxb.core.annotation.RestController;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.honor.api.UserHonorService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.StudentService;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.form.EditProfileForm;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.zuoye.api.ZyClazzJoinRequestService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStatisticService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClassConvertOption;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

/**
 * 新的学生班级相关接口
 *
 * @author xinyu.zhou
 * @since 3.0.2
 */
@RestController
@RequestMapping(value = "zy/m/s/class/2")
public class ZyMStuClass2Controller {
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private ZyHomeworkClassService homeworkClassService;
	@Autowired
	private ZyHomeworkClazzConvert homeworkClazzConvert;
	@Autowired
	private ZyHomeworkStudentClazzService hkStuClazzService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private ZyClazzJoinRequestService clazzJoinRequestService;
	@Autowired
	private ParameterService parameterService;
	@Autowired
	private ZyHomeworkStatisticService homeworkStatisticService;
	@Autowired
	MqSender mqSender;
	@Autowired
	private UserHonorService userHonorService;

	/**
	 * 渠道用户查询班级
	 *
	 * @return {@link Value}
	 */
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "findClass", method = { RequestMethod.GET, RequestMethod.POST })
	public Value findClass(Long cursor, @RequestParam(value = "size", defaultValue = "20") int size, String key) {

		VUser user = userConvert.get(Security.getUserId());
		// 非渠道用户不可以访问此接口
		if (!user.isChannelUser()) {
			return new Value(new IllegalArgException());
		}

		// 临时修改渠道学生用户班级列表逻辑
		VCursorPage<VHomeworkClazz> vpage = new VCursorPage<VHomeworkClazz>();
		if (StringUtils.isNotBlank(key)) {
			cursor = cursor == null ? Long.MAX_VALUE : cursor;
			CursorPageable<Long> cursorPageable = CP.cursor(cursor, size);

			CursorPage<Long, HomeworkClazz> resultPage = homeworkClassService.findByChannel(Security.getUserId(), key,
					cursorPageable);

			List<VHomeworkClazz> vs = homeworkClazzConvert.to(resultPage.getItems(),
					new ZyHomeworkClassConvertOption(true, false, false));
			isJoin(Security.getUserId(), vs);
			vpage.setItems(vs);
			vpage.setCursor(resultPage.getCursor() == null ? Long.MAX_VALUE : resultPage.getCursor());
		} else {
			vpage.setItems(Collections.EMPTY_LIST);
		}

		Map<String, Object> dataMap = new HashMap<String, Object>(2);
		dataMap.put("clazzs", vpage);
		dataMap.put("school", user.getSchool());

		return new Value(dataMap);
	}

	/**
	 * 查询班级数据
	 *
	 * @param key
	 *            查询关键字
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "queryClass", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryClass(String key) {
		if (StringUtils.isEmpty(key)) {
			return new Value(new IllegalArgException());
		}
		VUser user = userConvert.get(Security.getUserId());
		// 渠道用户不可以访问此接口
		if (user.isChannelUser()) {
			return new Value(new IllegalArgException());
		}
		List<HomeworkClazz> clazzList = homeworkClassService.findByCodeOrMobile(key);

		List<VHomeworkClazz> vs = homeworkClazzConvert.to(clazzList,
				new ZyHomeworkClassConvertOption(true, false, false));
		isJoin(Security.getUserId(), vs);

		return new Value(vs);
	}

	@SuppressWarnings("unchecked")
	private void isJoin(long userId, List<VHomeworkClazz> vs) {
		List<HomeworkStudentClazz> clazzs = hkStuClazzService.listCurrentClazzs(Security.getUserId());
		Map<Long, HomeworkStudentClazz> clazzMap = null;
		if (CollectionUtils.isEmpty(clazzs)) {
			clazzMap = Collections.EMPTY_MAP;
		} else {
			clazzMap = new HashMap<Long, HomeworkStudentClazz>(clazzs.size());
			for (HomeworkStudentClazz c : clazzs) {
				clazzMap.put(c.getClassId(), c);
			}
		}

		for (VHomeworkClazz v : vs) {
			v.setJoined(clazzMap.get(v.getId()) != null);
		}
	}

	/**
	 * 加入班级
	 * 
	 * @param classId
	 *            班级id
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "join", method = { RequestMethod.GET, RequestMethod.POST })
	public Value join(long classId, String realName) {
		HomeworkClazz homeworkClazz = homeworkClassService.get(classId);
		// 班级不存在
		if (homeworkClazz == null || homeworkClazz.getStatus() != Status.ENABLED) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_CLZZCODE_WRONG));
		}
		// 学生已经加入班级
		if (hkStuClazzService.isJoin(homeworkClazz.getId(), Security.getUserId())) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_CLASS_JOINED));
		}
		// 班级已经锁
		if (homeworkClazz.getLockStatus() != Status.ENABLED) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_CLZZCODE_WRONG));
		}

		VUser user = userConvert.get(Security.getUserId());

		if (homeworkClazz.isNeedConfirm()) {
			// 需要教师进行确认
			if (StringUtils.isBlank(realName)) {
				return new Value(new IllegalArgException());
			}
			Integer maxJoinNum = Env.getInt("student.max.per_class");
			if (homeworkClazz.getStudentNum() >= maxJoinNum) {
				Parameter parameter = parameterService.get(Product.YOOMATH, "customer-service.TEL.1");
				String tel = parameter.getValue();
				return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_JOIN_CLASS_MAXLIMIT,
						maxJoinNum, tel));
			}

			clazzJoinRequestService.request(Security.getUserId(), homeworkClazz.getId(), homeworkClazz.getTeacherId(),
					realName);
		} else {
			// 不需教师进行确认
			if (!user.isChannelUser()) {
				if (StringUtils.isEmpty(realName)) {
					return new Value(new IllegalArgException());
				}

				EditProfileForm ef = new EditProfileForm();
				ef.setId(Security.getUserId());
				ef.setName(realName);
				studentService.updateStudent(ef);
			}

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
					Parameter parameter = parameterService.get(Product.YOOMATH, "customer-service.TEL.1");
					Integer maxJoinNum = Env.getInt("student.max.per_class");
					String tel = parameter.getValue();
					return new Value(new YoomathMobileException(
							YoomathMobileException.YOOMATH_MOBILE_JOIN_CLASS_MAXLIMIT, maxJoinNum, tel));
				} else {
					return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_CLZZCODE_WRONG));
				}
			}
		}
		userHonorService.asyncUptUserHonor(GrowthAction.CLASS_STU_NUM, CoinsAction.CLASS_STU_NUM,
				homeworkClazz.getTeacherId(), Biz.CLASS, classId);
		return new Value();
	}

}

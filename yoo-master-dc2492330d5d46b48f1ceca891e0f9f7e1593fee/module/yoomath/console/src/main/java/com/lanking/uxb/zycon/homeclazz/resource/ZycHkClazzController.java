package com.lanking.uxb.zycon.homeclazz.resource;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.component.mq.common.constants.MqYoomathDiagnoRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.base.message.api.PushPacket;
import com.lanking.cloud.domain.base.message.api.SmsPacket;
import com.lanking.cloud.domain.common.baseData.Phase;
import com.lanking.cloud.domain.common.baseData.Subject;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.stat.HomeworkStat;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.convert.PhaseConvert;
import com.lanking.uxb.service.code.convert.SubjectConvert;
import com.lanking.uxb.service.message.api.MessageSender;
import com.lanking.uxb.service.push.type.OpenPath;
import com.lanking.uxb.service.push.util.YmPushUrls;
import com.lanking.uxb.service.session.api.DeviceService;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.convert.UserProfileConvert;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStatisticService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzStatService;
import com.lanking.uxb.zycon.common.ex.YoomathConsoleException;
import com.lanking.uxb.zycon.homeclazz.api.ClazzQuery;
import com.lanking.uxb.zycon.homeclazz.api.ZycHkClazzService;
import com.lanking.uxb.zycon.homeclazz.api.ZycHomeworkClazzTransferService;

/**
 * 班级管理
 * 
 * @since yoomath V1.6
 * @author wangsenhao
 *
 */
@RestController
@RequestMapping("zyc/clazz")
public class ZycHkClazzController {
	@Autowired
	private ZycHkClazzService zycHkClazzService;
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private PhaseConvert phaseConvert;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private SubjectConvert subjectConvert;
	@Autowired
	private ZyHomeworkStatisticService homeworkStatisticService;
	@Autowired
	private ZyHomeworkStudentClazzStatService stuClazzStatService;
	@Autowired
	private ZycHomeworkClazzTransferService transferService;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private ZyHomeworkClassService classService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private UserProfileConvert userProfileConvert;
	@Autowired
	private MessageSender messageSender;
	@Autowired
	MqSender mqSender;

	/**
	 * 查询班级
	 * 
	 * @param cq
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "queryHkClazz")
	public Value queryHkClazz(ClazzQuery cq) {
		Page<Map> cp = zycHkClazzService.queryHkClazz(cq, P.index(cq.getPage(), cq.getPageSize()));
		VPage<Map> vp = new VPage<Map>();
		int tPage = (int) (cp.getTotalCount() + cq.getPageSize() - 1) / cq.getPageSize();
		vp.setPageSize(cq.getPageSize());
		vp.setCurrentPage(cq.getPage());
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		List<Long> hkClazzIds = new ArrayList<Long>();
		for (Map pa : cp.getItems()) {
			hkClazzIds.add(Long.parseLong(String.valueOf(pa.get("id"))));
		}
		if (hkClazzIds.size() > 0) {
			Map<Long, HomeworkStat> map = zycHkClazzService.mgetStat(hkClazzIds);
			for (Map pa : cp.getItems()) {
				Long clazzId = Long.parseLong(String.valueOf(pa.get("id")));
				pa.put("homeworkNum", map.get(clazzId) == null ? "0" : map.get(clazzId).getHomeWorkNum());
				pa.put("id", ((BigInteger) pa.get("id")).longValue());
				pa.put("schoolId", pa.get("school_id") == null ? null : ((BigInteger) pa.get("school_id")).longValue());
			}
		}
		vp.setItems(cp.getItems());

		return new Value(vp);
	}

	/**
	 * 某个班级的详情
	 * 
	 * @param clazzId
	 *            班级id
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "getHkClazz")
	public Value getHkClazz(Long clazzId) {
		Map map = zycHkClazzService.getHkClazz(clazzId);
		map.put("teacherid", ((BigInteger) map.get("teacherid")).longValue());
		map.put("schoolyearOption", getSchoolYear());
		return new Value(map);
	}

	/**
	 * 入学年份初始化
	 */
	private List<Subject> getSchoolYear() {
		List<Subject> schoolyearList = new ArrayList<>();
		Subject subject = null;

		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);

		for (int i = 0; i < 5; i++) {
			subject = new Subject();
			subject.setCode(year);
			subject.setName(String.valueOf(year));
			schoolyearList.add(subject);
			year--;
		}

		return schoolyearList;
	}

	/**
	 * 查询班级人员
	 * 
	 * @param clazzId
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "clazzMember")
	public Value clazzMember(Long clazzId) {
		List<Map> list = zycHkClazzService.queryClazzMember(clazzId);
		for (Map map : list) {
			Long id = ((BigInteger) map.get("id")).longValue();
			map.put("id", id);
		}
		return new Value(list);
	}

	/**
	 * 查询学生
	 * 
	 * @param name
	 *            登录名或者用户名
	 * @param clazzId
	 *            当前查询学生对应的班级
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "queryStudent")
	public Value queryStudent(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int pageSize, String name, Long clazzId) {
		// 查询到的
		Page<Map> cp = zycHkClazzService.queryStudent(name, P.index(page, pageSize));
		VPage<Map> vp = new VPage<Map>();
		int tPage = (int) (cp.getTotalCount() + pageSize - 1) / pageSize;
		vp.setPageSize(pageSize);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		List<Map> memberList = zycHkClazzService.queryClazzMember(clazzId);
		List<Long> members = new ArrayList<Long>();
		for (Map pa : memberList) {
			members.add(((BigInteger) pa.get("id")).longValue());
		}
		for (Map map : cp.getItems()) {
			Long id = ((BigInteger) map.get("id")).longValue();
			map.put("isMember", members.contains(id));
			map.put("id", id);
		}
		vp.setItems(cp.getItems());
		return new Value(vp);
	}

	/**
	 * 添加学生
	 * 
	 * @param studentId
	 *            学生id
	 * @param clazzId
	 *            班级id
	 * @return
	 */
	@RequestMapping(value = "addStu")
	public Value addStu(Long studentId, Long clazzId) {
		if (studentId == null || clazzId == null) {
			return new Value(new MissingArgumentException());
		}
		zycHkClazzService.addStu(studentId, clazzId);

		// 学生加入班级，需要重新计算班级整体统计
		homeworkStatisticService.updateTeacherHomeworkStatByClass(clazzId);
		stuClazzStatService.recoverByStudentId(studentId, clazzId);

		// 学生加入班级，需要重新更新学生诊断相应数据 数据跟随2017.7.6 senhao.wang
		JSONObject object = new JSONObject();
		object.put("classId", clazzId);
		object.put("studentId", studentId);
		mqSender.send(MqYoomathDiagnoRegistryConstants.EX_YM_DIAGNO_TASK,
				MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_STUDENT_JOIN, MQ.builder().data(object).build());
		return new Value();
	}

	/**
	 * 移除学生
	 * 
	 * @param studentId
	 *            学生id
	 * @param clazzId
	 *            班级id
	 * @return
	 */
	@RequestMapping(value = "delStu")
	public Value delStu(Long studentId, Long clazzId) {
		if (studentId == null || clazzId == null) {
			return new Value(new MissingArgumentException());
		}

		zycHkClazzService.delStudent(studentId, clazzId);

		// 学生退出班级，需要重新计算班级整体统计
		homeworkStatisticService.updateTeacherHomeworkStatByClass(clazzId);
		stuClazzStatService.removeByStudentId(studentId, clazzId);

		// 学生被移除班级，需要重新更新学生诊断相应数据 数据跟随2017.7.6 senhao.wang
		JSONObject object = new JSONObject();
		object.put("classId", clazzId);
		object.put("studentId", studentId);
		mqSender.send(MqYoomathDiagnoRegistryConstants.EX_YM_DIAGNO_TASK,
				MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_STUDENT_LEAVE, MQ.builder().data(object).build());
		return new Value();
	}

	/**
	 * 更新班级信息
	 * 
	 * @param clazzId
	 *            班级id
	 * @param name
	 *            班级名称
	 * @param status
	 *            班级状态
	 * @return
	 */
	@RequestMapping(value = "updateHkClazz")
	public Value updateHkClazz(Long clazzId, String name, Status status, Integer schoolYear) {
		// 校验
		if (schoolYear == null) {
			return new Value(new IllegalArgException());
		}
		zycHkClazzService.updateHkClazz(clazzId, name, status, schoolYear);
		return new Value();
	}

	/**
	 * 验证老师班级名称是否存在
	 * 
	 * @param teacherId
	 *            老师id
	 * @param name
	 *            班级名称
	 * @return
	 */
	@RequestMapping(value = "classNameIsExist")
	public Value classNameIsExist(Long teacherId, String name) {
		return new Value(zycHkClazzService.classNameIsExist(teacherId, name));
	}

	/**
	 * 获取阶段列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "getPhaseList", method = RequestMethod.POST)
	public Value getPhaseList() {
		List<Phase> phaselist = phaseService.getAll();

		Map<String, Object> map = new HashMap<>();
		map.put("schoolyear", getSchoolYear());
		map.put("phaselist", phaseConvert.to(phaselist));
		return new Value(map);
	}

	/**
	 * 根据阶段码查询对应科目
	 * 
	 * @param phaseCode
	 * @return
	 */
	@RequestMapping(value = "getSubjectList", method = RequestMethod.POST)
	public Value getSubjectList(@RequestParam(value = "phaseCode") int phaseCode) {
		List<Subject> subject = subjectService.findByPhaseCode(phaseCode);
		return new Value(subjectConvert.to(subject));
	}

	/**
	 * 班级导入学生
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "import")
	public Value importUsers(HttpServletRequest request, HttpServletResponse response)
			throws IOException, InvalidFormatException {
		Map<String, Object> excelMap = zycHkClazzService.getWb(request);
		if (excelMap == null) {
			return new Value(new IllegalArgException());
		}
		if (excelMap.get("dataList") == null) {
			return new Value(new MissingArgumentException());
		}
		if (excelMap.get("result") == "fail") {
			return new Value(excelMap.get("dataList"));
		}
		String clazzId = request.getParameter("clazzId");
		zycHkClazzService.save((List<List<String>>) excelMap.get("dataList"), Long.parseLong(clazzId));

		// 学生加入班级，需要重新计算班级整体统计
		homeworkStatisticService.updateTeacherHomeworkStatByClass(Long.parseLong(clazzId));
		return new Value();
	}

	/**
	 * 查询,精确匹配,这里需要过滤当前班级老师的ID
	 * 
	 * @param accountName
	 * @param schoolId
	 *            前台会判断，如果学校为空不可以转让班级。
	 * @param createId
	 *            当前班级对应的老师，不可以被查出来
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "getTransUser")
	public Value getTransUser(String accountName, Long schoolId) {
		Map map = transferService.findUser(accountName);
		Map<String, Object> data = new HashMap<String, Object>();
		if (map != null) {
			Integer userType = Integer.parseInt(String.valueOf(map.get("user_type")));
			// 1.教师用户
			if (userType != UserType.TEACHER.getValue()) {
				return new Value(new YoomathConsoleException(YoomathConsoleException.NOT_TEACHER_ERROR));
			}
			Integer channelCode = Integer.parseInt(String.valueOf(map.get("user_channel_code")));
			// 2.渠道用户
			if (channelCode == UserChannel.YOOMATH || channelCode == null) {
				return new Value(new YoomathConsoleException(YoomathConsoleException.NOT_CHANNEL_USER_ERROR));
			}
			// 3.当前学校
			Long userId = Long.parseLong(String.valueOf(map.get("id")));
			if (userConvert.get(userId).getSchool() == null
					|| userConvert.get(userId).getSchool().getId() != schoolId) {
				return new Value(new YoomathConsoleException(YoomathConsoleException.NOT_CURRENT_SCHOOL_ERROR));
			}
			String userName = String.valueOf(map.get("username"));
			Long mobile = map.get("mobile") == null ? null : Long.parseLong(map.get("mobile").toString());
			data.put("accountName", accountName);
			data.put("userName", userName);
			data.put("mobile", mobile);
			data.put("teacherId", userId);

		}
		return new Value(data);
	}

	/**
	 * 确定转让班级
	 * 
	 * @param classId
	 * @param teacherId
	 * @return
	 */
	@RequestMapping(value = "transfer")
	public Value transfer(Long classId, Long teacherId) {
		Long count = classService.currentCount(teacherId);
		if (count >= 5) {
			return new Value(new YoomathConsoleException(YoomathConsoleException.TEACHER_CLASSNUM_MAX_ERROR));
		}
		HomeworkClazz clazz = classService.get(classId);
		Long oldCreateId = clazz.getTeacherId();
		if (clazz.getStatus() != Status.ENABLED) {
			return new Value(new YoomathConsoleException(YoomathConsoleException.CLASS_STATUS_ABNORMAL_ERROR));
		}
		transferService.classTransfer(classId, teacherId);
		VUser oldTeacher = userConvert.get(oldCreateId);
		// 推送
		List<String> tokens = deviceService.findTokenByUserIds(Lists.newArrayList(teacherId), Product.YOOMATH);
		if (CollectionUtils.isNotEmpty(tokens)) {
			messageSender.send(new PushPacket(Product.YOOMATH, YooApp.MATH_TEACHER, tokens, 12000023,
					ValueMap.value("teacherName", oldTeacher.getName()),
					YmPushUrls.url(YooApp.MATH_TEACHER, OpenPath.CLASSMANAGER_HOME, null),
					ValueMap.value("className", clazz.getName()).put("teacherName", oldTeacher.getName())));
		}
		// 短信
		String mobile = userProfileConvert.get(teacherId).getAccount().getMobile();
		if (mobile != null) {
			messageSender.send(new SmsPacket(userProfileConvert.get(teacherId).getAccount().getMobile(), 10000027,
					ValueMap.value("teacherName", oldTeacher.getName()).put("className", clazz.getName())));
		}
		return new Value();
	}
}

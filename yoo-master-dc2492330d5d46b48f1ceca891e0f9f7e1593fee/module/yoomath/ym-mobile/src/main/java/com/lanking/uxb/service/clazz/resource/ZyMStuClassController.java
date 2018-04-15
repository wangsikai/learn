package com.lanking.uxb.service.clazz.resource;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.mq.common.constants.MqYoomathDiagnoRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.stat.StudentHomeworkStat;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.VCursorPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.common.convert.VStudentHomeworkConvert;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkService;
import com.lanking.uxb.service.holiday.convert.HolidayStuHomeworkConvert;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.resources.convert.StudentHomeworkConvert;
import com.lanking.uxb.service.resources.convert.StudentHomeworkConvertOption;
import com.lanking.uxb.service.resources.value.VStudentHomework;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.StudentService;
import com.lanking.uxb.service.user.form.EditProfileForm;
import com.lanking.uxb.service.web.resource.ZyStuClassController;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStatisticService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkQuery;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkService;
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
 * 悠数学移动端(班级相关接口)
 * 
 * @since yoomath(mobile) V1.0.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年11月9日
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@ApiAllowed
@RestController
@RequestMapping("zy/m/s/class")
public class ZyMStuClassController {
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
	private ZyStudentHomeworkService stuHkService;
	@Autowired
	private StudentHomeworkConvert stuHkConvert;
	@Autowired
	private StudentHomeworkService studentHomeworkService;
	@Autowired
	private HolidayStuHomeworkService holidayStuHomeworkService;
	@Autowired
	private HolidayStuHomeworkConvert holidayStuHomeworkConvert;
	@Autowired
	private VStudentHomeworkConvert vstudentHomeworkConvert;

	@Autowired
	private StudentService studentService;
	@Autowired
	private ZyStuClassController stuClassController;

	@Autowired
	private ZyHomeworkStatisticService homeworkStatisticService;
	@Autowired
	MqSender mqSender;

	/**
	 * 学生注册成功后完善资料
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param clazzCode
	 *            班级码
	 * @param realname
	 *            真实姓名
	 * @return {@link Value}
	 */
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "join", method = { RequestMethod.POST, RequestMethod.GET })
	public Value fillProfileAfterRegister(String clazzCode, String realname) {
		if (StringUtils.isBlank(clazzCode) || StringUtils.isBlank(realname)) {
			// 直接报非法参数
			return new Value(new IllegalArgException());
		}
		HomeworkClazz clazz = zyHkClassService.findByCode(clazzCode);
		if (clazz == null || clazz.getStatus() != Status.ENABLED) {
			// 班级不存在或者班级状态不对直接报YOOMATH_MOBILEException.YOOMATH_MOBILE_CLZZCODE_WRONG
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_CLZZCODE_WRONG));
		}
		if (hkStuClazzService.isJoin(clazz.getId(), Security.getUserId())) {
			// 不能重复 加入班级
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_CLASS_JOINED));
		}
		// 这里为了代码的模块化不将以下两个操作在一个事务中处理
		// 更新学生的正式姓名
		if (StringUtils.isNotBlank(realname)) {
			EditProfileForm ef = new EditProfileForm();
			ef.setId(Security.getUserId());
			ef.setName(realname);
			studentService.updateStudent(ef);
		}
		try {
			// 加入班级
			hkStuClazzService.join(clazz.getId(), Security.getUserId());

			// 学生加入班级，需要重新计算班级整体统计
			homeworkStatisticService.updateTeacherHomeworkStatByClass(clazz.getId());

			// 学生加入班级，需要重新更新学生诊断相应数据 数据跟随2017.7.6 senhao.wang
			JSONObject object = new JSONObject();
			object.put("classId", clazz.getId());
			object.put("studentId", Security.getUserId());
			mqSender.send(MqYoomathDiagnoRegistryConstants.EX_YM_DIAGNO_TASK,
					MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_STUDENT_JOIN, MQ.builder().data(object).build());
		} catch (ZuoyeException e) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_CLZZCODE_WRONG));
		}
		return new Value();
	}

	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "myClass", method = { RequestMethod.POST, RequestMethod.GET })
	public Value myClass() {
		return stuClassController.myClazzs();
	}

	@Deprecated
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Value detail(long clazzId, @RequestParam(value = "historySize", defaultValue = "20") int historySize) {
		HomeworkStudentClazz homeworkStudentClazz = hkStuClazzService.find(clazzId, Security.getUserId());
		if (homeworkStudentClazz == null) {
			return new Value(new EntityNotFoundException());
		}
		VHomeworkStudentClazz vhomeworkStudentClazz = hkStuClazzConvert.to(homeworkStudentClazz);
		HomeworkClazz homeworkClazz = zyHkClassService.get(homeworkStudentClazz.getClassId());
		if (homeworkClazz == null) {
			return new Value(new EntityNotFoundException());
		}
		VHomeworkClazz vhomeworkClazz = zyHkClassConvert.to(homeworkClazz,
				new ZyHomeworkClassConvertOption(true, true, false));
		vhomeworkStudentClazz.setHomeworkClazz(vhomeworkClazz);
		StudentHomeworkStat homeworkStat = zyStuHkStatService.findOne(Security.getUserId(),
				homeworkStudentClazz.getClassId());
		if (homeworkStat == null) {
			VStudentHomeworkStat vstuStat = new VStudentHomeworkStat();
			vstuStat.setHomeworkClassId(homeworkStudentClazz.getClassId());
			vstuStat.setUserId(Security.getUserId());
			vstuStat.getRightRateTitle();
			vstuStat.setRightRateTitle(StringUtils.EMPTY);
			vhomeworkStudentClazz.setHomeworkStat(vstuStat);
		} else {
			vhomeworkStudentClazz.setHomeworkStat(zyStuHkStatConvert.to(homeworkStat));
		}
		ValueMap vm = ValueMap.value("clazz", vhomeworkStudentClazz);

		Map<String, Object> dataMap = new HashMap<String, Object>(2);
		// 历史作业
		VCursorPage<VStudentHomework> vp = new VCursorPage<VStudentHomework>();
		ZyStudentHomeworkQuery historyQuery = new ZyStudentHomeworkQuery();
		historyQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.SUBMITED, StudentHomeworkStatus.ISSUED));
		historyQuery.setCourse(false);
		historyQuery.setStudentId(Security.getUserId());
		historyQuery.setCursorType("startTime");
		historyQuery.setClassId(clazzId);
		CursorPage<Long, Map> historyPage = stuHkService.queryUnionHolidayStuHk(historyQuery,
				CP.cursor(Long.MAX_VALUE, Math.min(historySize, 20)));
		if (historyPage.isEmpty()) {
			vp.setCursor(Long.MAX_VALUE);
			vp.setItems(Collections.EMPTY_LIST);
		} else {
			List<VStudentHomework> items = new ArrayList<VStudentHomework>(historySize);
			List<Long> ids = new ArrayList<Long>(historySize);
			List<Long> stuHkIds = Lists.newArrayList();
			List<Long> stuHolidayHkIds = Lists.newArrayList();
			List<Map> maps = historyPage.getItems();
			for (Map map : maps) {
				int type = ((BigInteger) map.get("type")).intValue();
				long id = ((BigInteger) map.get("id")).longValue();
				if (type == 1) {
					stuHkIds.add(id);
				} else if (type == 2) {
					stuHolidayHkIds.add(id);
				}
				ids.add(id);
			}
			Map<Long, VStudentHomework> vs = new HashMap<Long, VStudentHomework>(historySize);
			if (stuHkIds.size() > 0) {
				Map<Long, StudentHomework> map = studentHomeworkService.mgetMap(stuHkIds);
				vs.putAll(stuHkConvert.to(map, false, true, false, false));
			}
			if (stuHolidayHkIds.size() > 0) {
				Map<Long, HolidayStuHomework> map = holidayStuHomeworkService.mgetMap(stuHolidayHkIds);
				vs.putAll(vstudentHomeworkConvert.to(holidayStuHomeworkConvert.to(map)));
			}
			for (Long id : ids) {
				items.add(vs.get(id));
			}
			vp.setCursor(historyPage.getNextCursor());
			vp.setItems(items);
		}
		dataMap.put("history", vp);
		// 待批改作业
		ZyStudentHomeworkQuery todoQuery = new ZyStudentHomeworkQuery();
		todoQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.NOT_SUBMIT));
		todoQuery.setCourse(false);
		todoQuery.setStudentId(Security.getUserId());
		todoQuery.setCursorType("startTime");
		todoQuery.setClassId(clazzId);
		CursorPage<Long, Map> todoPage = stuHkService.queryUnionHolidayStuHk(todoQuery, CP.cursor(Long.MAX_VALUE, 50));
		if (todoPage.isEmpty()) {
			dataMap.put("todo", Collections.EMPTY_LIST);
		} else {
			int todoSize = todoPage.getItemSize();
			List<VStudentHomework> items = new ArrayList<VStudentHomework>(todoSize);
			List<Long> ids = new ArrayList<Long>(todoSize);
			List<Long> stuHkIds = Lists.newArrayList();
			List<Long> stuHolidayHkIds = Lists.newArrayList();
			List<Map> maps = todoPage.getItems();
			for (Map map : maps) {
				int type = ((BigInteger) map.get("type")).intValue();
				long id = ((BigInteger) map.get("id")).longValue();
				if (type == 1) {
					stuHkIds.add(id);
				} else if (type == 2) {
					stuHolidayHkIds.add(id);
				}
				ids.add(id);
			}
			Map<Long, VStudentHomework> vs = new HashMap<Long, VStudentHomework>(todoSize);
			if (stuHkIds.size() > 0) {
				Map<Long, StudentHomework> map = studentHomeworkService.mgetMap(stuHkIds);
				vs.putAll(stuHkConvert.to(map, false, true, false, false));
			}
			if (stuHolidayHkIds.size() > 0) {
				Map<Long, HolidayStuHomework> map = holidayStuHomeworkService.mgetMap(stuHolidayHkIds);
				vs.putAll(vstudentHomeworkConvert.to(holidayStuHomeworkConvert.to(map)));
			}
			for (Long id : ids) {
				items.add(vs.get(id));
			}
			dataMap.put("todo", items);
		}

		vm.put("homeworks", dataMap);
		return new Value(vm);
	}

	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "detailNoHoliday", method = { RequestMethod.POST, RequestMethod.GET })
	public Value detailNoHoliday(long clazzId,
			@RequestParam(value = "historySize", defaultValue = "20") int historySize) {
		HomeworkStudentClazz homeworkStudentClazz = hkStuClazzService.find(clazzId, Security.getUserId());
		if (homeworkStudentClazz == null) {
			return new Value(new EntityNotFoundException());
		}
		VHomeworkStudentClazz vhomeworkStudentClazz = hkStuClazzConvert.to(homeworkStudentClazz);
		HomeworkClazz homeworkClazz = zyHkClassService.get(homeworkStudentClazz.getClassId());
		if (homeworkClazz == null) {
			return new Value(new EntityNotFoundException());
		}
		VHomeworkClazz vhomeworkClazz = zyHkClassConvert.to(homeworkClazz,
				new ZyHomeworkClassConvertOption(true, true, false));
		vhomeworkStudentClazz.setHomeworkClazz(vhomeworkClazz);
		StudentHomeworkStat homeworkStat = zyStuHkStatService.findOne(Security.getUserId(),
				homeworkStudentClazz.getClassId());
		if (homeworkStat == null) {
			VStudentHomeworkStat vstuStat = new VStudentHomeworkStat();
			vstuStat.setHomeworkClassId(homeworkStudentClazz.getClassId());
			vstuStat.setUserId(Security.getUserId());
			vstuStat.getRightRateTitle();
			vstuStat.setRightRateTitle(StringUtils.EMPTY);
			vhomeworkStudentClazz.setHomeworkStat(vstuStat);
		} else {
			vhomeworkStudentClazz.setHomeworkStat(zyStuHkStatConvert.to(homeworkStat));
		}
		ValueMap vm = ValueMap.value("clazz", vhomeworkStudentClazz);

		Map<String, Object> dataMap = new HashMap<String, Object>(2);
		// 历史作业
		VCursorPage<VStudentHomework> vp = new VCursorPage<VStudentHomework>();
		ZyStudentHomeworkQuery historyQuery = new ZyStudentHomeworkQuery();
		historyQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.SUBMITED, StudentHomeworkStatus.ISSUED));
		historyQuery.setCourse(false);
		historyQuery.setStudentId(Security.getUserId());
		historyQuery.setCursorType("startTime");
		historyQuery.setClassId(clazzId);
		CursorPage<Long, Map> historyPage = stuHkService.queryStuHk(historyQuery,
				CP.cursor(Long.MAX_VALUE, Math.min(historySize, 20)));
		if (historyPage.isEmpty()) {
			vp.setCursor(Long.MAX_VALUE);
			vp.setItems(Collections.EMPTY_LIST);
		} else {
			List<VStudentHomework> items = new ArrayList<VStudentHomework>(historySize);
			List<Long> ids = new ArrayList<Long>(historySize);
			List<Long> stuHkIds = Lists.newArrayList();
			List<Map> maps = historyPage.getItems();
			for (Map map : maps) {
				long id = ((BigInteger) map.get("id")).longValue();
				stuHkIds.add(id);
				ids.add(id);
			}
			Map<Long, VStudentHomework> vs = new HashMap<Long, VStudentHomework>(historySize);
			if (stuHkIds.size() > 0) {
				Map<Long, StudentHomework> map = studentHomeworkService.mgetMap(stuHkIds);
				StudentHomeworkConvertOption stuHkOption = new StudentHomeworkConvertOption();
				stuHkOption.setInitStuHomeworkWrongAndCorrect(true);
				stuHkOption.setInitHomework(true);
				stuHkOption.setInitMessages(true);
				vs.putAll(stuHkConvert.to(map, stuHkOption));
			}
			for (Long id : ids) {
				items.add(vs.get(id));
			}
			vp.setCursor(historyPage.getNextCursor());
			vp.setItems(items);
		}
		dataMap.put("history", vp);
		// 待批改作业
		ZyStudentHomeworkQuery todoQuery = new ZyStudentHomeworkQuery();
		todoQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.NOT_SUBMIT));
		todoQuery.setCourse(false);
		todoQuery.setStudentId(Security.getUserId());
		todoQuery.setCursorType("startTime");
		todoQuery.setClassId(clazzId);
		CursorPage<Long, Map> todoPage = stuHkService.queryStuHk(todoQuery, CP.cursor(Long.MAX_VALUE, 50));
		if (todoPage.isEmpty()) {
			dataMap.put("todo", Collections.EMPTY_LIST);
		} else {
			int todoSize = todoPage.getItemSize();
			List<VStudentHomework> items = new ArrayList<VStudentHomework>(todoSize);
			List<Long> ids = new ArrayList<Long>(todoSize);
			List<Long> stuHkIds = Lists.newArrayList();
			List<Map> maps = todoPage.getItems();
			for (Map map : maps) {
				long id = ((BigInteger) map.get("id")).longValue();
				stuHkIds.add(id);
				ids.add(id);
			}
			Map<Long, VStudentHomework> vs = new HashMap<Long, VStudentHomework>(todoSize);
			if (stuHkIds.size() > 0) {
				Map<Long, StudentHomework> map = studentHomeworkService.mgetMap(stuHkIds);
				StudentHomeworkConvertOption stuHkOption = new StudentHomeworkConvertOption();
				stuHkOption.setInitStuHomeworkWrongAndCorrect(true);
				stuHkOption.setInitHomework(true);
				stuHkOption.setInitMessages(true);
				vs.putAll(stuHkConvert.to(map, stuHkOption));
			}
			for (Long id : ids) {
				items.add(vs.get(id));
			}
			dataMap.put("todo", items);
		}

		vm.put("homeworks", dataMap);
		return new Value(vm);
	}

	@Deprecated
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "queryHomeworks", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryHomeworks(long clazzId, long cursor,
			@RequestParam(value = "historySize", defaultValue = "20") int historySize) {
		VCursorPage<VStudentHomework> vp = new VCursorPage<VStudentHomework>();
		ZyStudentHomeworkQuery historyQuery = new ZyStudentHomeworkQuery();
		historyQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.SUBMITED, StudentHomeworkStatus.ISSUED));
		historyQuery.setCourse(false);
		historyQuery.setStudentId(Security.getUserId());
		historyQuery.setCursorType("startTime");
		historyQuery.setClassId(clazzId);
		CursorPage<Long, Map> historyPage = stuHkService.queryUnionHolidayStuHk(historyQuery,
				CP.cursor(cursor == 0 ? Long.MAX_VALUE : cursor, Math.min(historySize, 20)));

		if (historyPage.isEmpty()) {
			vp.setCursor(cursor);
			vp.setItems(Collections.EMPTY_LIST);
		} else {
			List<VStudentHomework> items = new ArrayList<VStudentHomework>(historySize);
			List<Long> ids = new ArrayList<Long>(historySize);
			List<Long> stuHkIds = Lists.newArrayList();
			List<Long> stuHolidayHkIds = Lists.newArrayList();
			List<Map> maps = historyPage.getItems();
			for (Map map : maps) {
				int type = ((BigInteger) map.get("type")).intValue();
				long id = ((BigInteger) map.get("id")).longValue();
				if (type == 1) {
					stuHkIds.add(id);
				} else if (type == 2) {
					stuHolidayHkIds.add(id);
				}
				ids.add(id);
			}
			Map<Long, VStudentHomework> vs = new HashMap<Long, VStudentHomework>(historySize);
			if (stuHkIds.size() > 0) {
				Map<Long, StudentHomework> map = studentHomeworkService.mgetMap(stuHkIds);
				vs.putAll(stuHkConvert.to(map, false, true, false, false));
			}
			for (Long id : ids) {
				items.add(vs.get(id));
			}
			vp.setCursor(historyPage.getNextCursor());
			vp.setItems(items);
		}
		return new Value(vp);
	}

	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "queryHomeworksNoHoliday", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryHomeworksNoHoliday(long clazzId, long cursor,
			@RequestParam(value = "historySize", defaultValue = "20") int historySize) {
		VCursorPage<VStudentHomework> vp = new VCursorPage<VStudentHomework>();
		ZyStudentHomeworkQuery historyQuery = new ZyStudentHomeworkQuery();
		historyQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.SUBMITED, StudentHomeworkStatus.ISSUED));
		historyQuery.setCourse(false);
		historyQuery.setStudentId(Security.getUserId());
		historyQuery.setCursorType("startTime");
		historyQuery.setClassId(clazzId);
		CursorPage<Long, Map> historyPage = stuHkService.queryStuHk(historyQuery,
				CP.cursor(cursor == 0 ? Long.MAX_VALUE : cursor, Math.min(historySize, 20)));

		if (historyPage.isEmpty()) {
			vp.setCursor(cursor);
			vp.setItems(Collections.EMPTY_LIST);
		} else {
			List<VStudentHomework> items = new ArrayList<VStudentHomework>(historySize);
			List<Long> ids = new ArrayList<Long>(historySize);
			List<Long> stuHkIds = Lists.newArrayList();
			List<Map> maps = historyPage.getItems();
			for (Map map : maps) {
				long id = ((BigInteger) map.get("id")).longValue();
				stuHkIds.add(id);
				ids.add(id);
			}
			Map<Long, VStudentHomework> vs = new HashMap<Long, VStudentHomework>(historySize);
			if (stuHkIds.size() > 0) {
				Map<Long, StudentHomework> map = studentHomeworkService.mgetMap(stuHkIds);
				StudentHomeworkConvertOption stuHkOption = new StudentHomeworkConvertOption();
				stuHkOption.setInitStuHomeworkWrongAndCorrect(true);
				stuHkOption.setInitHomework(true);
				stuHkOption.setInitMessages(true);
				vs.putAll(stuHkConvert.to(map, stuHkOption));
			}
			for (Long id : ids) {
				items.add(vs.get(id));
			}
			vp.setCursor(historyPage.getNextCursor());
			vp.setItems(items);
		}
		return new Value(vp);
	}
}

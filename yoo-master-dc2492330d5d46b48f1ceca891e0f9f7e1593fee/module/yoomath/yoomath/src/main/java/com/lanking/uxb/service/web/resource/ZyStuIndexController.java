package com.lanking.uxb.service.web.resource;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.resource.card.KnowledgePointCard;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoo.recommend.RecommendKnowpointCard;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserParameter;
import com.lanking.cloud.domain.yoo.user.UserParameterType;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPractise;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionClassStat;
import com.lanking.cloud.domain.yoomath.stat.StudentHomeworkStat;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.KnowledgePointCardService;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.convert.KnowledgePointCardConvert;
import com.lanking.uxb.service.common.convert.VStudentHomeworkConvert;
import com.lanking.uxb.service.diagnostic.api.DiagnosticStudentClassLatestHomeworkKnowpointService;
import com.lanking.uxb.service.diagnostic.api.DiagnosticStudentClassLatestHomeworkService;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticStudentClassLatestHomework;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticStudentClassLatestHomeworkKnowpoint;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkService;
import com.lanking.uxb.service.holiday.convert.HolidayStuHomeworkConvert;
import com.lanking.uxb.service.holiday.value.VHolidayStuHomework;
import com.lanking.uxb.service.ranking.api.DoQuestionRankingService;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.resources.convert.StudentHomeworkConvert;
import com.lanking.uxb.service.resources.value.VStudentHomework;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.StudentService;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.user.api.UserParameterService;
import com.lanking.uxb.service.web.api.DailyPractiseGenerateService;
import com.lanking.uxb.service.web.api.RecommendKnowpointCardService;
import com.lanking.uxb.service.zuoye.api.ZyHkStuClazzKnowpointStatService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.api.ZyMottoService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkQuery;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkStatService;
import com.lanking.uxb.service.zuoye.convert.ZyDailyPractiseConvert;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClassConvertOption;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkStudentClazzConvert;
import com.lanking.uxb.service.zuoye.convert.ZyStudentHomeworkStatConvert;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;
import com.lanking.uxb.service.zuoye.value.VHomeworkStudentClazz;
import com.lanking.uxb.service.zuoye.value.VStudentHomeworkPage;
import com.lanking.uxb.service.zuoye.value.VStudentHomeworkStat;

/**
 * 悠作业学生首页接口
 * 
 * @since yoomath V1.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月14日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/s/index")
public class ZyStuIndexController {

	@Autowired
	private ZyStudentHomeworkService stuHkService;
	@Autowired
	private StudentHomeworkConvert stuHkConvert;
	@Autowired
	private StudentHomeworkService studentHomeworkService;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private ZyHomeworkClazzConvert zyHkClassConvert;
	@Autowired
	private ZyHomeworkStudentClazzService zyHkStuClazzService;
	@Autowired
	private ZyHomeworkStudentClazzConvert zyHkStuClazzConvert;
	@Autowired
	private ZyStudentHomeworkStatService zyStuHkStatService;
	@Autowired
	private ZyStudentHomeworkStatConvert zyStuHkStatConvert;
	@Autowired
	private ZyStudentHomeworkService zyStuHkService;
	@Autowired
	private ZyMottoService mottoService;
	@Autowired
	private HolidayStuHomeworkService holidayStuHomeworkService;
	@Autowired
	private HolidayStuHomeworkConvert holidayStuHomeworkConvert;
	@Autowired
	private VStudentHomeworkConvert vstudentHomeworkConvert;
	@Autowired
	private UserParameterService upService;
	@Autowired
	private ZyHkStuClazzKnowpointStatService zyHkStuClazzKnowpointStatService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private DailyPractiseGenerateService dailyPractiseGenerateService;
	@Autowired
	private ZyDailyPractiseConvert dailyPractiseConvert;
	@Autowired
	private RecommendKnowpointCardService recommendKnowpointCardService;
	@Autowired
	private KnowledgePointCardConvert knowledgePointCardConvert;
	@Autowired
	private KnowledgePointService knowledgePointService;
	@Autowired
	private KnowledgePointCardService knowledgePointCardService;
	@Autowired
	private DoQuestionRankingService doQuestionRankingService;
	@Autowired
	private DiagnosticStudentClassLatestHomeworkService diagStuHkService;
	@Autowired
	private DiagnosticStudentClassLatestHomeworkKnowpointService diagStuKpService;

	/**
	 * 学生查看班级统计(学生首页、我的班级、查看单个作业的总体统计使用)
	 * 
	 * @since yoomathV1.2
	 * @param classId
	 *            班级ID
	 * @param needClassList
	 *            是否需要返回班级列表
	 * @param one
	 *            是否为一个班级（此时needClassList失效）
	 * @return
	 */
	@Deprecated
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@RequestMapping(value = "statistic", method = { RequestMethod.POST, RequestMethod.GET })
	public Value statistic(@RequestParam(required = false) Long classId,
			@RequestParam(defaultValue = "false") boolean needClassList,
			@RequestParam(defaultValue = "false") boolean one) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		if (needClassList && !one) {
			List<HomeworkStudentClazz> studentClazzs = zyHkStuClazzService.listCurrentClazzs(Security.getUserId());
			if (CollectionUtils.isNotEmpty(studentClazzs)) {
				List<VHomeworkStudentClazz> vstuClazzs = zyHkStuClazzConvert.to(studentClazzs);
				// 封装班级信息
				List<Long> classIds = new ArrayList<Long>(studentClazzs.size());
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
				List<StudentHomeworkStat> stats = zyStuHkStatService.getByHomeworkClassIds(Security.getUserId(),
						classIds);
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
				if (classId == null) {
					classId = vstuClazzs.get(0).getClassId();
				}
			}
			// 用于判断该学生是否从未加入过班级
			data.put("clazzCount", zyHkStuClazzService.countStudentClazz(Security.getUserId(), null));
		}
		if (classId != null) {
			// 字段为stu_right_rate class_right_rate homework_name homework_id
			List<Map> statisticList = zyStuHkService.statisticLatestHomework(classId, Security.getUserId(), 20);
			for (Map map : statisticList) {
				map.put("student_homework_id", ((BigInteger) map.get("student_homework_id")).longValue());
			}
			data.put("statistic", statisticList);
			if (one) {
				HomeworkStudentClazz studentClazz = zyHkStuClazzService.find(classId, Security.getUserId());
				if (studentClazz != null) {
					VHomeworkStudentClazz vStudentClazz = zyHkStuClazzConvert.to(studentClazz);
					vStudentClazz
							.setHomeworkClazz(zyHkClassConvert.to(zyHkClassService.get(vStudentClazz.getClassId())));
					StudentHomeworkStat homeworkStat = zyStuHkStatService.getByHomeworkClassId(Security.getUserId(),
							vStudentClazz.getClassId());
					if (homeworkStat == null) {
						VStudentHomeworkStat vStudentHomeworkStat = new VStudentHomeworkStat();
						vStudentHomeworkStat.setHomeworkClassId(vStudentClazz.getClassId());
						vStudentHomeworkStat.setUserId(Security.getUserId());
						vStudentClazz.setHomeworkStat(vStudentHomeworkStat);
					} else {
						vStudentClazz.setHomeworkStat(zyStuHkStatConvert.to(homeworkStat));
					}
					data.put("clazz", vStudentClazz);
				}

			}
		}
		return new Value(data);
	}

	/**
	 * 分页获取学生作业列表
	 * 
	 * @since 2.1
	 * @param courseId
	 *            课程ID
	 * @param pageNo
	 *            页码
	 * @param size
	 *            获取数量
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "query", method = { RequestMethod.POST, RequestMethod.GET })
	public Value query(@RequestParam(required = false) Long courseId, @RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "40") int size) {
		ZyStudentHomeworkQuery query = new ZyStudentHomeworkQuery();
		query.setCourseId(courseId);
		query.setStudentId(Security.getUserId());
		Page<StudentHomework> page = stuHkService.query(query, P.index(pageNo, Math.min(size, 40)));
		VPage<VStudentHomework> vpage = new VPage<VStudentHomework>();
		vpage.setCurrentPage(pageNo);
		List<VStudentHomework> vs = stuHkConvert.to(page.getItems(), false, true, false, false);
		vpage.setItems(vs);
		vpage.setPageSize(size);
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		return new Value(vpage);
	}

	/**
	 * 分页获取学生作业列表
	 * 
	 * @since 2.1
	 * @param classId
	 *            作业班级ID
	 * @param pageNo
	 *            页码
	 * @param size
	 *            获取数量
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "2/query", method = { RequestMethod.POST, RequestMethod.GET })
	public Value query2(@RequestParam(required = false) Long classId, @RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "40") int size, @RequestParam(defaultValue = "false") boolean motto) {
		ZyStudentHomeworkQuery query = new ZyStudentHomeworkQuery();
		query.setClassId(classId);
		query.setStudentId(Security.getUserId());
		query.setCourse(false);
		Page<StudentHomework> page = stuHkService.query(query, P.index(pageNo, Math.min(size, 40)));
		List<VStudentHomework> vs = stuHkConvert.to(page.getItems(), false, true, false, false);
		if (CollectionUtils.isNotEmpty(vs)) {
			List<Long> hkClassIds = new ArrayList<Long>(vs.size());
			for (VStudentHomework v : vs) {
				hkClassIds.add(v.getHomework().getHomeworkClazzId());
			}
			Map<Long, VHomeworkClazz> vmap = zyHkClassConvert.to(zyHkClassService.mget(hkClassIds));
			for (VStudentHomework v : vs) {
				v.getHomework().setHomeworkClazz(vmap.get(v.getHomework().getHomeworkClazzId()));
			}
		}
		VStudentHomeworkPage vpage = new VStudentHomeworkPage();
		vpage.setCurrentPage(pageNo);
		vpage.setItems(vs);
		vpage.setPageSize(size);
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		if (motto) {
			vpage.setMotto(mottoService.getOne());
		}
		return new Value(vpage);
	}

	/**
	 * 分页获取学生作业列表
	 * 
	 * @since yoomath
	 * @param classId
	 *            作业班级ID
	 * @param pageNo
	 *            页码
	 * @param size
	 *            获取数量
	 * @return {@link Value}
	 */
	@SuppressWarnings("rawtypes")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "3/query", method = { RequestMethod.POST, RequestMethod.GET })
	public Value query3(@RequestParam(required = false) Long classId, @RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "40") int size, @RequestParam(defaultValue = "false") boolean motto,
			@RequestParam(defaultValue = "false") boolean userParameter) {
		ZyStudentHomeworkQuery query = new ZyStudentHomeworkQuery();
		query.setClassId(classId);
		query.setStudentId(Security.getUserId());
		query.setCourse(false);
		Page<Map> page = stuHkService.queryUnionHolidayStuHk(query, P.index(pageNo, Math.min(size, 40)));
		List<Long> stuhomeworkIds = Lists.newArrayList();
		List<Long> holidayStuHomeworkIds = Lists.newArrayList();
		for (Map map : page.getItems()) {
			// type 1为普通作业
			if (Integer.valueOf(map.get("type").toString()) == 1) {
				stuhomeworkIds.add(Long.valueOf(map.get("id").toString()));
			}
			// type 2为假期作业
			if (Integer.valueOf(map.get("type").toString()) == 2) {
				holidayStuHomeworkIds.add(Long.valueOf(map.get("id").toString()));
			}
		}
		List<HolidayStuHomework> stuHolidaylist1 = new ArrayList<HolidayStuHomework>();
		Map<Long, HolidayStuHomework> ll = holidayStuHomeworkService.mgetMap(holidayStuHomeworkIds);
		for (Long id : holidayStuHomeworkIds) {
			stuHolidaylist1.add(ll.get(id));
		}
		List<VHolidayStuHomework> stuHolidaylist = holidayStuHomeworkConvert.to(stuHolidaylist1);
		List<VStudentHomework> temp = new ArrayList<VStudentHomework>(size);
		if (stuHolidaylist.size() > 0) {
			temp.addAll(vstudentHomeworkConvert.to(stuHolidaylist));
		}
		Map<Long, StudentHomework> map = studentHomeworkService.mgetMap(stuhomeworkIds);
		List<StudentHomework> shList = new ArrayList<StudentHomework>();
		for (Long id : stuhomeworkIds) {
			shList.add(map.get(id));
		}
		List<VStudentHomework> vs = stuHkConvert.to(shList, false, true, false, false);
		if (CollectionUtils.isNotEmpty(vs)) {
			List<Long> hkClassIds = new ArrayList<Long>(vs.size());
			for (VStudentHomework v : vs) {
				hkClassIds.add(v.getHomework().getHomeworkClazzId());
			}
			Map<Long, VHomeworkClazz> vmap = zyHkClassConvert.to(zyHkClassService.mget(hkClassIds));
			for (VStudentHomework v : vs) {
				v.getHomework().setHomeworkClazz(vmap.get(v.getHomework().getHomeworkClazzId()));
			}
		}
		temp.addAll(vs);
		List<VStudentHomework> tempList = new ArrayList<VStudentHomework>();
		for (Map newMap : page.getItems()) {
			Long id = Long.valueOf(newMap.get("id").toString());
			for (VStudentHomework v : temp) {
				if (v.getId() == id) {
					tempList.add(v);
				}
			}
		}
		VStudentHomeworkPage vpage = new VStudentHomeworkPage();
		vpage.setCurrentPage(pageNo);
		vpage.setItems(tempList);
		vpage.setPageSize(size);
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		if (pageNo == 1 && motto) {
			vpage.setMotto(mottoService.getOne());
		}
		if (pageNo == 1 && userParameter) {
			UserParameter parameter = upService.findOne(Product.YOOMATH, null, UserParameterType.STUDENT_WEB_USE,
					Security.getUserId());
			vpage.setShowParentLetter(parameter == null || !"1".equals(parameter.getP0()));
		}
		return new Value(vpage);
	}

	/**
	 * 学生首页班级相关数据<br/>
	 * 悠数学2.0版本班级列表详情数据
	 *
	 * @since 2.0.3
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "clazzDatas", method = { RequestMethod.GET, RequestMethod.POST })
	public Value clazzDatas() {
		Map<String, Object> data = new HashMap<String, Object>(3);
		List<HomeworkStudentClazz> studentClazzs = zyHkStuClazzService.listCurrentClazzs(Security.getUserId());
		if (CollectionUtils.isNotEmpty(studentClazzs)) {
			Map<Long, Object> statisticMap = new HashMap<Long, Object>(studentClazzs.size());
			Map<Long, Object> knowpointMap = new HashMap<Long, Object>(studentClazzs.size());
			List<VHomeworkStudentClazz> vstuClazzs = zyHkStuClazzConvert.to(studentClazzs);
			// 封装班级信息
			List<Long> classIds = new ArrayList<Long>(studentClazzs.size());
			for (VHomeworkStudentClazz v : vstuClazzs) {
				classIds.add(v.getClassId());
			}
			Map<Long, HomeworkClazz> clazzs = zyHkClassService.mget(classIds);
			// 2016.12.9需要返回作业中，提交率
			Map<Long, VHomeworkClazz> vclazzs = zyHkClassConvert.to(clazzs,
					new ZyHomeworkClassConvertOption(true, true, false));
			for (VHomeworkStudentClazz v : vstuClazzs) {
				v.setHomeworkClazz(vclazzs.get(v.getClassId()));
				List<Map> knowpointStat = zyHkStuClazzKnowpointStatService.getStuLowKnowpointStat(Security.getUserId(),
						v.getClassId());
				knowpointMap.put(v.getClassId(), knowpointStat);

				List<Map> statisticList = zyStuHkService.statisticLatestHomework(v.getClassId(), Security.getUserId(),
						20);
				for (Map map : statisticList) {
					map.put("student_homework_id", ((BigInteger) map.get("student_homework_id")).longValue());
				}

				statisticMap.put(v.getClassId(), statisticList);
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
					vstuStat.setRightRate(null);
				}
				v.setHomeworkStat(vstuStat);
			}
			data.put("clazzs", vstuClazzs);

			data.put("statistics", statisticMap);
			data.put("knowpoints", knowpointMap);
			if (CollectionUtils.isNotEmpty(classIds)) {
				// 新增返回学生在每个班级的7天练习数，取自练习榜数据
				Map<Long, DoQuestionClassStat> doStatMap = doQuestionRankingService.mgetDoQuestionClassStat(classIds,
						Security.getUserId(), 7);
				data.put("doStatMap", doStatMap);
				Map<Long, List<VDiagnosticStudentClassLatestHomework>> diagStuHkMap = diagStuHkService
						.findByClassIds(Security.getUserId(), classIds, 7);
				data.put("diagStuHkMap", diagStuHkMap);
				Map<Long, List<VDiagnosticStudentClassLatestHomeworkKnowpoint>> diagStuKpMap = diagStuKpService
						.findByClassIds(Security.getUserId(), classIds, 7);
				data.put("diagStuKpMap", diagStuKpMap);
				// 学生作业提交率
				Map<Long, Integer> rateMap = zyStuHkStatService.getSubmitRateByStuId(Security.getUserId(), classIds);
				data.put("rateMap", rateMap);
			}
		}
		return new Value(data);
	}

	/**
	 * 悠数学2.0 版本学生首页接口<br/>
	 * 此接口包含三块数据: 1. 今日任务. 2. 每日练. 3. 知识卡片相关内容. 4.知识点卡片推送
	 *
	 * @since 2.0.3
	 * @param classId
	 *            班级id
	 * @param refresh
	 *            是否是今日任务刷新操作
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "2/index", method = { RequestMethod.GET, RequestMethod.POST })
	public Value index2(Long classId, @RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		Map<String, Object> ret = new HashMap<String, Object>(4);

		ret.put("hks", getHomeworks(classId));
		if (refresh) {
			return new Value(ret);
		}

		Student student = (Student) studentService.getUser(UserType.STUDENT, Security.getUserId());
		List<HomeworkStudentClazz> studentClazzs = zyHkStuClazzService.listCurrentClazzs(Security.getUserId());
		if (studentClazzs.size() > 0 && studentClazzs.get(0) != null) {
			// 学生知识点卡片推送
			RecommendKnowpointCard recommendKnowpoint = recommendKnowpointCardService
					.getRecommendKnowpointCard(student.getId());
			if (recommendKnowpoint == null) {
				recommendKnowpoint = recommendKnowpointCardService.getLastRecommendKnowpointCard(student.getId());

			}
			KnowledgePointCard knowledgePointCard = null;
			if (null != recommendKnowpoint) {
				knowledgePointCard = knowledgePointCardService.get(recommendKnowpoint.getKnowpointCardId());
			}

			// 取值本阶段最近的知识点
			if (null == knowledgePointCard) {
				if (studentClazzs.size() > 0 && studentClazzs.get(0) != null) {
					Integer phaseCode = null;
					// 如果学生phaseCode不为空取值student
					if (student.getPhaseCode() != null) {
						phaseCode = student.getPhaseCode();
					} else {
						// phaseCode取值班级对应的教师
						HomeworkClazz hc = zyHkClassService.get(studentClazzs.get(0).getClassId());
						if (hc != null && hc.getTeacherId() != null) {
							// 如果有对应的老师 则取老师的阶段码,没有则不取
							Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, hc.getTeacherId());
							if (teacher != null) {
								phaseCode = teacher.getPhaseCode();
							}
						}
					}
					if (phaseCode != null) {
						knowledgePointCard = knowledgePointCardService.getFirstKnowpointCardByPhaseCode(phaseCode);
					}
				}
			}
			ret.put("recommendKnowpointCard", knowledgePointCardConvert.to(knowledgePointCard));
		}

		// 学生未设置版本教材->返回未设置
		if (student.getTextbookCode() == null) {
			ret.put("dpNeedSetting", true);
		} else {
			Map<String, Object> genMap = dailyPractiseGenerateService.generate(student, 20);
			ret.put("dpNeedSetting", genMap.get("setting"));
			if (genMap.get("finish") != null) {
				boolean finish = (boolean) genMap.get("finish");
				ret.put("dpFinish", finish);
				// 若已经完成当前教材的所有章节练习则不返回数据
				if (!finish) {
					DailyPractise dailyPractise = (DailyPractise) genMap.get("practise");
					if (dailyPractise != null) {
						ret.put("practise", dailyPractiseConvert.to(dailyPractise));
					}
				}
			}

		}
		return new Value(ret);
	}

	/**
	 * 悠数学版本2.0中首页获得学生待处理作业列表<br/>
	 * 所有待完成作业(普通作业,假期作业)以及已经下发但是还未查看过的作业.<br/>
	 *
	 * 若是已经下发了作业则更新学生查看状态为已查看. 每次只取20条数据
	 *
	 * @param classId
	 *            班级id
	 *
	 * @since 2.0.3
	 * @return 今日任务作业列表
	 */
	private List<VStudentHomework> getHomeworks(Long classId) {
		ZyStudentHomeworkQuery query = new ZyStudentHomeworkQuery();
		query.setClassId(classId);
		query.setStudentId(Security.getUserId());
		query.setCourse(false);

		List<Map> hks = stuHkService.queryUnionHolidayStuHkNew(query);

		List<Long> stuhomeworkIds = Lists.newArrayList();
		List<Long> holidayStuHomeworkIds = Lists.newArrayList();
		for (Map map : hks) {
			StudentHomeworkStatus status = StudentHomeworkStatus
					.findByValue(Integer.valueOf(map.get("status").toString()));
			Long id = ((BigInteger) map.get("id")).longValue();
			// type 1为普通作业
			if (Integer.valueOf(map.get("type").toString()) == 1) {
				stuhomeworkIds.add(id);
			}
			// type 2为假期作业
			if (Integer.valueOf(map.get("type").toString()) == 2) {
				holidayStuHomeworkIds.add(id);
			}
		}
		List<HolidayStuHomework> stuHolidaylist1 = new ArrayList<HolidayStuHomework>();
		Map<Long, HolidayStuHomework> ll = holidayStuHomeworkService.mgetMap(holidayStuHomeworkIds);
		for (Long id : holidayStuHomeworkIds) {
			stuHolidaylist1.add(ll.get(id));
		}
		List<VHolidayStuHomework> stuHolidaylist = holidayStuHomeworkConvert.to(stuHolidaylist1);
		List<VStudentHomework> temp = new ArrayList<VStudentHomework>(hks.size());
		if (stuHolidaylist.size() > 0) {
			temp.addAll(vstudentHomeworkConvert.to(stuHolidaylist));
		}
		Map<Long, StudentHomework> map = studentHomeworkService.mgetMap(stuhomeworkIds);
		List<StudentHomework> shList = new ArrayList<StudentHomework>();
		for (Long id : stuhomeworkIds) {
			shList.add(map.get(id));
		}
		List<VStudentHomework> vs = stuHkConvert.to(shList, false, true, false, false);
		if (CollectionUtils.isNotEmpty(vs)) {
			List<Long> hkClassIds = new ArrayList<Long>(vs.size());
			for (VStudentHomework v : vs) {
				hkClassIds.add(v.getHomework().getHomeworkClazzId());
			}
			Map<Long, VHomeworkClazz> vmap = zyHkClassConvert.to(zyHkClassService.mget(hkClassIds));
			for (VStudentHomework v : vs) {
				v.getHomework().setHomeworkClazz(vmap.get(v.getHomework().getHomeworkClazzId()));
			}
		}
		temp.addAll(vs);
		List<VStudentHomework> tempList = new ArrayList<VStudentHomework>();
		for (Map newMap : hks) {
			Long id = Long.valueOf(newMap.get("id").toString());
			for (VStudentHomework v : temp) {
				if (v.getId() == id) {
					tempList.add(v);
				}
			}
		}

		return tempList;
	}
}

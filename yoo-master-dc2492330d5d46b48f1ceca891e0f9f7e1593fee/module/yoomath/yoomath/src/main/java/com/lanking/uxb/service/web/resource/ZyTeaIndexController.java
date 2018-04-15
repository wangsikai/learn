package com.lanking.uxb.service.web.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.resource.book.BookCatalog;
import com.lanking.cloud.domain.common.resource.book.BookVersion;
import com.lanking.cloud.domain.yoo.common.BannerLocation;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomework;
import com.lanking.cloud.domain.yoomath.homework.Exercise;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.diagnostic.api.DiagnosticClassLatestHomeworkKnowpointService;
import com.lanking.uxb.service.diagnostic.api.DiagnosticClassLatestHomeworkService;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticClassLatestHomework;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticClassLatestHomeworkKnowpoint;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkService;
import com.lanking.uxb.service.interaction.api.InteractionService;
import com.lanking.uxb.service.interaction.value.VInteraction;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.convert.HomeworkConvert;
import com.lanking.uxb.service.resources.convert.HomeworkConvertOption;
import com.lanking.uxb.service.resources.value.VHomework;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.sys.api.BannerQuery;
import com.lanking.uxb.service.sys.api.BannerService;
import com.lanking.uxb.service.sys.convert.BannerConvert;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.zuoye.api.ZyBookService;
import com.lanking.uxb.service.zuoye.api.ZyExerciseService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkService;
import com.lanking.uxb.service.zuoye.convert.ZyBookCatalogConvert;
import com.lanking.uxb.service.zuoye.convert.ZyBookVersionConvert;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClassConvertOption;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

/**
 * 悠作业老师首页接口
 * 
 * @since yoomath V1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月14日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/t")
public class ZyTeaIndexController {

	@Autowired
	private ZyBookVersionConvert zyBookVersionConvert;
	@Autowired
	private ZyBookService zyBookService;
	@Autowired
	private ZyHomeworkService zyHomeworkService;
	@Autowired
	private HomeworkConvert homeworkConvert;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private ZyHomeworkClazzConvert zyHkClassConvert;
	@Autowired
	private HolidayHomeworkService holidayHomeworkService;
	@Autowired
	private HomeworkService homeworkService;
	@Autowired
	private ZyBookService bookService;
	@Autowired
	private ZyBookCatalogConvert bookCatalogConvert;
	@Autowired
	private BannerService bannerService;
	@Autowired
	private BannerConvert bannerConvert;
	@Autowired
	private InteractionService interactionService;
	@Autowired
	private DiagnosticClassLatestHomeworkService latestHkService;
	@Autowired
	private DiagnosticClassLatestHomeworkKnowpointService latestkpService;
	@Autowired
	private ZyExerciseService zyExerciseService;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private TeacherService teacherService;

	/**
	 * 首页接口
	 * 
	 * @since yoomath V1.2
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "2/index", method = { RequestMethod.POST, RequestMethod.GET })
	public Value index2() {
		Map<String, Object> data = new HashMap<String, Object>(2);
		// 处理待处理列表
		List<Homework> homeworks = zyHomeworkService.findProcessHomeworks(Security.getUserId(), 5, false);
		if (CollectionUtils.isEmpty(homeworks)) {
			data.put("todos", Collections.EMPTY_LIST);
		} else {
			List<VHomework> vHomeworks = homeworkConvert.to(homeworks, new HomeworkConvertOption(true));
			List<Long> homeworkClazzIds = new ArrayList<Long>(homeworks.size());
			for (Homework homework : homeworks) {
				homeworkClazzIds.add(homework.getHomeworkClassId());
			}
			Map<Long, VHomeworkClazz> clazzMap = zyHkClassConvert.to(zyHkClassService.mget(homeworkClazzIds));
			for (VHomework v : vHomeworks) {
				v.setHomeworkClazz(clazzMap.get(v.getHomeworkClazzId()));
			}
			data.put("todos", vHomeworks);
		}

		// 教师班级数量
		data.put("clazzs", zyHkClassService.currentCount(Security.getUserId()));
		return new Value(data);
	}

	/**
	 * 首页接口
	 * 
	 * @since yoomath V2.3
	 * @return {@link Value}
	 */
	@MemberAllowed
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "3/index", method = { RequestMethod.POST, RequestMethod.GET })
	public Value index3() {
		// 最新章节
		Map<String, Object> data = new HashMap<String, Object>(6);
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		Exercise exercise = zyExerciseService.findNoBooksIdLatestOne(Security.getUserId(), teacher.getTextbookCode());
		// 练习不为空的且有指定章节的清空下返回章节
		if (exercise != null && exercise.getSectionCode() != null) {
			data.put("latestSection", sectionConvert.to(sectionService.mgetListByChildId(exercise.getSectionCode())));
		}
		// 获取返回教材
		data.put("textbookCategoryCode", teacher.getTextbookCategoryCode());
		data.put("textbookCode", teacher.getTextbookCode());
		MemberType memberType = SecurityContext.getMemberType();
		List<BookVersion> vbList = null;
		if (memberType == MemberType.SCHOOL_VIP) {
			vbList = zyBookService.getUserBookList(teacher.getTextbookCategoryCode(), teacher.getTextbookCode(),
					teacher.getId());
		} else {
			vbList = zyBookService.getUserFreeBookList(teacher.getTextbookCategoryCode(), teacher.getTextbookCode(),
					teacher.getId());
		}
		// 如果没有选中的教材返回
		if (vbList.size() == 0) {
			Long schoolBookCount = 0L;
			data.put("freeBookCount",
					zyBookService.getFreeBookCount(teacher.getTextbookCategoryCode(), teacher.getTextbookCode()));
			if (teacher.getSchoolId() != null) {
				schoolBookCount = zyBookService.getSchoolBookCount(teacher.getTextbookCategoryCode(),
						teacher.getTextbookCode(), teacher.getSchoolId());
			}
			data.put("schoolBookCount", schoolBookCount);
		} else {
			data.put("bookChooseList", zyBookVersionConvert.to(vbList));
		}
		return new Value(data);
	}

	/**
	 * 获取用户班级
	 * 
	 * @since yoomath V1.2
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "2/clazzCount", method = { RequestMethod.POST, RequestMethod.GET })
	public Value clazzCount() {
		Map<String, Object> data = new HashMap<String, Object>(1);
		// 教师班级数量
		data.put("clazzs", zyHkClassService.currentCount(Security.getUserId()));
		return new Value(data);
	}

	/**
	 * 教师首页数据（代办）.
	 * 
	 * @since v2.0.3 (web v2.0) 2016-03-15
	 * @author wlche
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "index/queryTodo", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryTodo() {
		Map<String, Object> data = new HashMap<String, Object>(1);

		// 代办事项
		List<Homework> homeworks = zyHomeworkService.findProcessHomeworks2(Security.getUserId(), Integer.MAX_VALUE,
				false);
		if (CollectionUtils.isEmpty(homeworks)) {
			data.put("todos", Collections.EMPTY_LIST);
		} else {
			List<Map<String, Object>> vHomeworks = new ArrayList<Map<String, Object>>(homeworks.size());
			for (Homework homework : homeworks) {
				Map<String, Object> map = new HashMap<String, Object>(2);
				map.put("id", homework.getId());
				map.put("name", homework.getName());
				vHomeworks.add(map);
			}
			data.put("todos", vHomeworks);
		}
		return new Value(data);
	}

	/**
	 * 教师首页数据（跟踪）.
	 * 
	 * @since v2.0.3 (web v2.0) 2016-03-15
	 * @author wlche
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "index/queryTracking", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryTracking() {
		Map<String, Object> data = new HashMap<String, Object>(2);
		data.put("count", zyHomeworkService.queryHomeworkTrackingCount(Security.getUserId()));
		// 作业跟踪
		Page<Map> page = zyHomeworkService.queryHomeworkTracking(Security.getUserId(), P.index(1, 50));
		List<Long> homeworkIds = Lists.newArrayList();
		List<Long> holidayHomeworkIds = Lists.newArrayList();
		for (Map map : page.getItems()) {
			// type 1为普通作业
			if (Integer.valueOf(map.get("type").toString()) == 1) {
				homeworkIds.add(Long.valueOf(map.get("id").toString()));
			}
			// type 2为假期作业
			if (Integer.valueOf(map.get("type").toString()) == 2) {
				holidayHomeworkIds.add(Long.valueOf(map.get("id").toString()));
			}
		}

		// 假期作业构造Vhomework
		List<VHomework> vhomeworks2 = Lists.newArrayList();

		// 班级数据
		List<Long> clazzIds = Lists.newArrayList();
		Map<Long, HolidayHomework> holidayMap = holidayHomeworkService.mget(holidayHomeworkIds);
		for (HolidayHomework holidayHomework : holidayMap.values()) {
			clazzIds.add(holidayHomework.getHomeworkClassId());
		}
		Map<Long, VHomeworkClazz> vClazzMap = zyHkClassConvert.to(zyHkClassService.mget(clazzIds));
		for (HolidayHomework h : holidayMap.values()) {
			VHomework homework = new VHomework();
			homework.setName(h.getName());
			homework.setDifficulty(h.getDifficulty());
			homework.setStartTime(h.getStartTime());
			homework.setDeadline(h.getDeadline());
			homework.setStatus(h.getStatus());
			homework.setId(h.getId());
			homework.setHomeworkClazz(vClazzMap.get(h.getHomeworkClassId()));
			homework.setType(2); // 2是假期作业
			homework.setRightRate(h.getRightRate());
			homework.setCompletionRate(h.getCompletionRate() == null ? BigDecimal.valueOf(0) : h.getCompletionRate());
			vhomeworks2.add(homework);
		}
		// 日常作业
		List<VHomework> vHomeworks = new ArrayList<VHomework>();
		if (CollectionUtils.isNotEmpty(homeworkIds)) {
			vHomeworks = homeworkConvert.to(Lists.newArrayList(homeworkService.mget(homeworkIds).values()),
					new HomeworkConvertOption(true));
		}
		if (CollectionUtils.isNotEmpty(vHomeworks)) {
			List<Long> homeworkClassIds = new ArrayList<Long>(vHomeworks.size());
			for (VHomework v : vHomeworks) {
				homeworkClassIds.add(v.getHomeworkClazzId());
			}
			Map<Long, VHomeworkClazz> map = zyHkClassConvert.to(zyHkClassService.mget(homeworkClassIds));
			for (VHomework v : vHomeworks) {
				v.setHomeworkClazz(map.get(v.getHomeworkClazzId()));
			}
		}

		// 5份分已下发作业
		int limit = 50 - page.getItems().size();
		if (limit > 5) {
			limit = 5;
		}
		List<VHomework> hws = homeworkConvert.to(zyHomeworkService.getIssuedHomework(Security.getUserId(), limit));
		// 假期先
		List<VHomework> homeworkItems = Lists.newArrayList();
		for (Map map : page.getItems()) {
			Long id = Long.valueOf(map.get("id").toString());
			for (VHomework vHomework : vhomeworks2) {
				if (vHomework.getId() == id) {
					homeworkItems.add(vHomework);
					break;
				}
			}
		}
		for (Map map : page.getItems()) {
			Long id = Long.valueOf(map.get("id").toString());
			for (VHomework vHomework : vHomeworks) {
				if (vHomework.getId() == id) {
					homeworkItems.add(vHomework);
					break;
				}
			}
		}

		if (CollectionUtils.isNotEmpty(hws)) {
			homeworkItems.addAll(hws);
		}

		data.put("tracking", homeworkItems);
		return new Value(data);
	}

	/**
	 * 教师首页数据（班级推荐、统计数据）.
	 * 
	 * @since v2.0.3 (web v2.0) 2016-03-15
	 * @author wlche
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "index/clazzDatas", method = { RequestMethod.POST, RequestMethod.GET })
	public Value clazzDatas() {
		Map<String, Object> data = new HashMap<String, Object>(1);

		// 班级数据
		List<HomeworkClazz> homeworkClazzs = zyHkClassService.listCurrentClazzs(Security.getUserId()); // 当前班级
		List<VHomeworkClazz> vs = null;
		if (CollectionUtils.isEmpty(homeworkClazzs)) {
			vs = Collections.EMPTY_LIST;
		} else {
			vs = zyHkClassConvert.to(homeworkClazzs, new ZyHomeworkClassConvertOption(true, true, true));
		}

		Set<Long> clazzIds = new HashSet<Long>(vs.size());
		for (VHomeworkClazz vclazz : vs) {
			if (vclazz.getBookVersionId() != null && vclazz.getBookCataId() != null && vclazz.getBookCataId() >= 0) {
				Map<String, Object> recommendMap = bookService.getRecommendCatalogs(vclazz.getBookVersionId(),
						vclazz.getBookCataId(), Security.getUserId());
				if (recommendMap == null) {
					vclazz.setBookVersionId(null);
					vclazz.setBookCataId(null);
				} else {
					vclazz.setRecommendCatalogs(
							bookCatalogConvert.to((List<BookCatalog>) recommendMap.get("catalogs")));
					vclazz.setLevelOneCatalog(bookCatalogConvert.to((BookCatalog) recommendMap.get("levelOneCatalog")));
				}
			}

			clazzIds.add(vclazz.getId());
		}

		data.put("clazzs", vs); // 班级数据
		// 新增师生互动信息2016.12.12
		List<Long> classIds = new ArrayList<Long>();
		classIds.addAll(clazzIds);
		if (CollectionUtils.isNotEmpty(classIds)) {
			Map<Long, List<VInteraction>> interMap = interactionService.findByClassIds(Security.getUserId(), classIds);
			data.put("interactMap", interMap);
			// 新增教学诊断所有班级最近7次默认数据，2016.12.12
			Map<Long, List<VDiagnosticClassLatestHomework>> diagClassMap = latestHkService.findByClassIds(classIds, 7);
			data.put("diagClassMap", diagClassMap);
			// 所有班级对应的薄弱知识点
			Map<Long, List<VDiagnosticClassLatestHomeworkKnowpoint>> diagKpMap = latestkpService
					.findByClassIds(classIds, 7);
			data.put("diagKpMap", diagKpMap);
		}
		return new Value(data);
	}

	/**
	 * 新增广告位
	 * 
	 * @since 2.0
	 * @return
	 */
	@RequestMapping(value = "index/bannerList", method = { RequestMethod.POST, RequestMethod.GET })
	public Value bannerList() {
		return new Value(bannerConvert.to(bannerService.listEnable(new BannerQuery(BannerLocation.HOME))));
	}

}

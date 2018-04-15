package com.lanking.uxb.service.report.resource;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassStudent;
import com.lanking.cloud.domain.yoomath.stat.ClassStatisticsReport;
import com.lanking.cloud.domain.yoomath.stat.HomeworkRightRateStat;
import com.lanking.cloud.domain.yoomath.stat.HomeworkStat;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.KnowledgePointConvert;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.diagnostic.api.DiagnosticClassStudentService;
import com.lanking.uxb.service.diagnostic.api.DiagnosticClassTextbookService;
import com.lanking.uxb.service.diagnostic.convert.DiagnosticClassStudentConvert;
import com.lanking.uxb.service.diagnostic.value.MasterStatus;
import com.lanking.uxb.service.examPaper.api.SmartPaperService;
import com.lanking.uxb.service.examPaper.form.SmartExamPaperForm;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.report.api.ClassStatisticsReportService;
import com.lanking.uxb.service.report.api.HomeworkRightRateStatService;
import com.lanking.uxb.service.report.convert.HomeworkRightRateStatConvert;
import com.lanking.uxb.service.report.value.ScoreLevel;
import com.lanking.uxb.service.report.value.VStudyClazzData;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.thirdparty.api.ShareLogService;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.convert.UserConvertOption;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStatService;
import com.lanking.uxb.service.zuoye.api.ZyQuestionCarService;
import com.lanking.uxb.service.zuoye.api.ZyQuestionService;

/**
 * 新学情分析
 * 
 * @author wangsenhao
 *
 */
@RestController
@RequestMapping("zy/m/t/report/teaching2")
public class ZyMTeaTeachingReport2Controller {

	@Autowired
	private ZyHomeworkClassService clazzService;
	@Autowired
	private ZyHomeworkStatService hkStatService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private DiagnosticClassTextbookService diagClassTextbookService;
	@Autowired
	private TextbookService textbookService;
	@Autowired
	private TextbookConvert textbookConvert;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private ClassStatisticsReportService reportService;
	@Autowired
	private HomeworkRightRateStatService rightRateStatService;
	@Autowired
	private HomeworkRightRateStatConvert rightRateStatConvert;
	@Autowired
	private SmartPaperService smartPaperService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private DiagnosticClassStudentService studentService;
	@Autowired
	private DiagnosticClassStudentConvert studentConvert;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private ParameterService parameterService;
	@Autowired
	private ShareLogService shareLogService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private ZyQuestionService zyQuestionService;
	@Autowired
	private KnowledgePointConvert kpConvert;
	@Autowired
	private ZyQuestionCarService questionCarService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 学情分析--班级列表
	 * 
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "findClassList", method = { RequestMethod.POST, RequestMethod.GET })
	public Value findClassList() {
		Map<String, Object> data = new HashMap<String, Object>();
		List<HomeworkClazz> clazzs = clazzService.listCurrentClazzs(Security.getUserId());
		if (CollectionUtils.isEmpty(clazzs)) {
			return new Value(data);
		}
		List<Long> classIds = new ArrayList<Long>();
		for (HomeworkClazz clazz : clazzs) {
			classIds.add(clazz.getId());
		}
		Map<Long, HomeworkClazz> clazzMap = clazzService.mget(classIds);
		List<HomeworkStat> hkStats = hkStatService.getByHomeworkClassIds(classIds);
		Map<Long, HomeworkStat> hkStatMap = new HashMap<Long, HomeworkStat>();
		for (HomeworkStat stat : hkStats) {
			hkStatMap.put(stat.getHomeworkClassId(), stat);
		}
		List<VStudyClazzData> datalist = new ArrayList<VStudyClazzData>();
		try {
			for (Long classId : classIds) {
				HomeworkStat hkStat = hkStatMap.get(classId);
				VStudyClazzData v = new VStudyClazzData();
				v.setClazzId(classId);
				if (hkStat == null) {
					v.setClazzName(clazzMap.get(classId).getName());
					v.setHomeWorkNum(0);
					v.setStudentNum(clazzMap.get(classId).getStudentNum());
				} else {
					v.setClazzName(clazzMap.get(hkStat.getHomeworkClassId()).getName());
					v.setHomeWorkNum(hkStat.getHomeWorkNum());
					v.setRightRate(hkStat.getRightRate());
					v.setRightRateTitle(hkStat.getRightRate() == null ? null : hkStat.getRightRate() + "%");
					v.setStudentNum(clazzMap.get(hkStat.getHomeworkClassId()).getStudentNum());
					if (hkStat.getRightRate() != null) {
						v.setCommitRate(hkStat.getCompletionRate());
						v.setCommitRateTitle(hkStat.getCompletionRate() == null ? null : hkStat.getCompletionRate()
								.intValue() + "%");
					}
				}
				String[] args = new String[1];
				args[0] = URLEncoder.encode(String.valueOf(classId), "UTF-8");
				Parameter wholeUrl = parameterService.get(Product.YOOMATH, "tea.report.teaching2.h5.url", args);
				v.setUrl(wholeUrl.getValue());
				datalist.add(v);
			}
		} catch (Exception e) {
			logger.error("teacher report findClassList", e);
		}
		data.put("dataList", datalist);
		return new Value(data);
	}

	/**
	 * 班级基本信息
	 * 
	 * @param classId
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "classBase", method = { RequestMethod.POST, RequestMethod.GET })
	public Value classBase(Long classId) {
		HomeworkStat hs = hkStatService.getByHomeworkClassId(classId);
		HomeworkClazz clazz = clazzService.get(classId);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("className", clazz.getName());
		data.put("studentNum", clazz.getStudentNum());
		data.put("homeworkNum", hs == null ? 0 : hs.getHomeWorkNum());
		// 要有人提交，并且已下发
		if (hs != null && hs.getRightRate() != null) {
			data.put("completionRate", hs.getCompletionRate());
		}
		data.put("rightRate", hs == null ? null : hs.getRightRate());
		return new Value(data);
	}

	/**
	 * 整体情况(总的)
	 * 
	 * @param classId
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@MemberAllowed
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "whole", method = { RequestMethod.POST, RequestMethod.GET })
	public Value whole(Long classId) {
		Map<String, Object> data = new HashMap<String, Object>();
		HomeworkStat stat = hkStatService.getByHomeworkClassId(classId);
		if (stat == null || stat.getHomeWorkNum() == 0) {
			data.put("hasHomework", false);
			return new Value(data);
		}
		data.put("hasHomework", true);
		Teacher teacher = (Teacher) teacherService.getUser(Security.getUserId());
		String calDate = reportService.getLastest(classId);
		// ###月度学情报告###
		if (calDate == null) {
			data.put("month", 0);
		} else {
			data.put("month", Integer.parseInt(calDate.substring(5)));
		}
		// #####近期班级平均正确率#####,查询近三个月的
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -3);
		List<HomeworkRightRateStat> rightRateList = rightRateStatService.getStat(classId, cal.getTime(), new Date());
		// 若班级下发布置作业后暂未到周一，无数据的情况时，该模块暂时隐藏
		if (CollectionUtils.isNotEmpty(rightRateList)) {
			data.put("rightRateList", rightRateStatConvert.to(rightRateList));
		}
		// #####章节掌握情况#####
		Integer textbookCategory = teacher.getTextbookCategoryCode();
		if (textbookCategory == null) {
			// 提示报错
			return new Value();
		}
		MemberType memberType = SecurityContext.getMemberType();
		if (memberType == MemberType.NONE || memberType == null) {
			data.put("isMember", false);
			return new Value(data);
		}
		data.put("isMember", true);
		// 教材列表
		List<Integer> textbookCodes = diagClassTextbookService.getClassTextbooks(classId, textbookCategory);
		List<Integer> sortCodes = diagClassTextbookService.getClassSortTextbooks(classId, textbookCategory);
		if (CollectionUtils.isNotEmpty(textbookCodes)) {
			// 重新排一下序
			data.put("textbookList", textbookConvert.to(textbookService.mgetList(sortCodes)));
			// 最新选中的
			data.put("selectTextbook", textbookCodes.get(0));
			List<VSection> vList = sectionConvert.to(sectionService.findByTextbookCode(textbookCodes.get(0), 1));
			List<Long> sectionCodes = new ArrayList<Long>();
			for (VSection v : vList) {
				sectionCodes.add(v.getCode());
			}
			// 剔除没有练习的章节---当前班级
			List<VSection> hasDataList = new ArrayList<VSection>();
			Map<Long, Integer> sectionCountMap = reportService.getSectionDoCountMap(classId, sectionCodes);
			for (VSection v : vList) {
				if (sectionCountMap.get(v.getCode()) != null) {
					hasDataList.add(v);
				}
			}
			data.put("sectionList", hasDataList);
			Long maxSectionCode = reportService.getMaxSection(classId, textbookCodes.get(0));
			// 临时返给前台的数据，后面会改成最新一个有作业的章节
			List<Long> codes = sectionService.findSectionChildren(maxSectionCode);
			List<VSection> vsections = sectionConvert.to(sectionService.mgetList(codes));
			data.put("sections", reportService.handle(classId, vsections));
		}
		return new Value(data);
	}

	/**
	 * 章节掌握情况切换
	 * 
	 * @param classId
	 * @param textbookCode
	 * @param sectionCode
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "chapterMaster", method = { RequestMethod.POST, RequestMethod.GET })
	public Value chapterMaster(Long classId, Integer textbookCode, Long sectionCode) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<VSection> vList = sectionConvert.to(sectionService.findByTextbookCode(textbookCode, 1));
		List<Long> sectionCodes = new ArrayList<Long>();
		for (VSection v : vList) {
			sectionCodes.add(v.getCode());
		}
		// 剔除没有练习的章节---当前班级
		List<VSection> hasDataList = new ArrayList<VSection>();
		Map<Long, Integer> sectionCountMap = reportService.getSectionDoCountMap(classId, sectionCodes);
		for (VSection v : vList) {
			if (sectionCountMap.get(v.getCode()) != null) {
				hasDataList.add(v);
			}
		}
		data.put("sectionList", hasDataList);

		List<Long> codes = new ArrayList<Long>();
		if (sectionCode != null) {
			codes = sectionService.findSectionChildren(sectionCode);
		} else {
			Long maxSectionCode = reportService.getMaxSection(classId, textbookCode);
			if (maxSectionCode == null) {
				return new Value(data);
			}
			codes = sectionService.findSectionChildren(maxSectionCode);
		}
		List<VSection> vsections = sectionConvert.to(sectionService.mgetList(codes));
		data.put("sections", reportService.handle(classId, vsections));
		return new Value(data);
	}

	/**
	 * 章节推荐
	 * 
	 * @param sectionCode
	 * @param status
	 * @param classId
	 * 
	 *            最底下一层章节码
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "recommendBySection", method = { RequestMethod.POST, RequestMethod.GET })
	public Value recommendBySection(Long sectionCode, MasterStatus status, Long classId) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("masterStatus", status);
		List<Long> kpCodes = reportService.getWeakKpListBySectionCode(classId, sectionCode);
		// 薄弱知识点
		// 推荐拉出来的题目
		SmartExamPaperForm form = new SmartExamPaperForm();
		form.setChoiceNum(6);
		form.setFillBlankNum(4);
		form.setAnswerNum(0);
		if (status == MasterStatus.EXCELLENT) {
			form.setBasePercent(10);
			form.setRaisePercent(40);
			form.setSprintPercent(50);
		} else if (status == MasterStatus.GOOD) {
			form.setBasePercent(40);
			form.setRaisePercent(30);
			form.setSprintPercent(30);
		} else if (status == MasterStatus.COMMONLY) {
			form.setBasePercent(40);
			form.setRaisePercent(30);
			form.setSprintPercent(30);
		} else if (status == MasterStatus.WEAK) {
			form.setBasePercent(50);
			form.setRaisePercent(40);
			form.setSprintPercent(10);
		}
		// 推送的题目
		Set<Long> qIds = new HashSet<Long>();
		if (CollectionUtils.isNotEmpty(kpCodes)) {
			data.put("kpList", kpConvert.mgetList(kpCodes));
			// 平均每个知识点至少要有多少题目,数量不够除外。只做一轮平分，后面随机补位
			int avgCount = 10 / kpCodes.size();
			// 先每个知识点拉取10个，后续做平均、去重处理.
			// 实际获取到的所有题目
			Set<Long> qAllIds = new HashSet<Long>();
			for (Long kpCode : kpCodes) {
				form.setKnowledgeCodes(Lists.newArrayList(kpCode));
				List<Long> questionIds = smartPaperService.queryQuestionsByIndex(form);
				if (questionIds.size() >= avgCount) {
					qIds.addAll(questionIds.subList(0, avgCount));
				} else {
					qIds.addAll(questionIds);
				}
				qAllIds.addAll(questionIds);
			}
			if (qIds.size() < 10) {
				int need = 10 - qIds.size();
				int temp = 0;
				for (Long qId : qAllIds) {
					if (temp < need) {
						if (!qIds.contains(qId)) {
							qIds.add(qId);
							temp++;
						}
					}
				}
			}
		} else {
			form.setSectionCode(sectionCode);
			List<Long> questionIds = smartPaperService.queryQuestionsByIndex(form);
			qIds.addAll(questionIds);
		}

		QuestionConvertOption option = new QuestionConvertOption(false, true, true, true, null);
		option.setInitPublishCount(true);
		option.setInitQuestionSimilarCount(true);
		option.setInitExamination(true);
		option.setInitQuestionTag(true);
		List<VQuestion> vList = questionConvert.to(questionService.mgetList(qIds), option);
		// 设置题目是否被加入作业篮子
		List<Long> carQuestions = questionCarService.getQuestionCarIds(Security.getUserId());
		for (VQuestion q : vList) {
			if (q.getType() != Type.COMPOSITE) {
				q.setInQuestionCar(carQuestions.contains(q.getId()));
			}
		}
		data.put("questionList", vList);
		return new Value(data);
	}

	/**
	 * 一次性获取名次榜，进步榜，退步榜，勤奋榜学生数据
	 * 
	 * @param day0
	 *            最近多少天 0.全部 7.一周 30.一个月 90.三个月
	 * @param classId
	 * @param flag
	 *            0.名次榜 1.进步榜 2.退步榜 3.勤奋榜
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "queryRankList", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryRankList(Integer day0, Long classId, Integer flag) {
		Map<String, Object> data = new HashMap<String, Object>();

		HomeworkStat stat = hkStatService.getByHomeworkClassId(classId);
		if (stat == null) {
			data.put("hasHomework", false);
			return new Value();
		}
		data.put("hasHomework", true);
		if (flag == 0) {
			// 名次榜
			List<DiagnosticClassStudent> rankList = studentService.query(day0, 0, classId);
			data.put("rankList", studentConvert.to(rankList));
		}
		if (flag == 1) {
			// 进步榜
			List<DiagnosticClassStudent> improveList = studentService.query(day0, 1, classId);
			dealListRank(improveList, 1);
			data.put("improveList", studentConvert.to(improveList));
		}
		if (flag == 2) {
			// 退步榜
			List<DiagnosticClassStudent> backwardList = studentService.query(day0, 2, classId);
			dealListRank(backwardList, 1);
			data.put("backwardList", studentConvert.to(backwardList));
		}
		if (flag == 3) {
			// 勤奋榜(做题榜)
			List<DiagnosticClassStudent> doCountList = studentService.query(day0, 3, classId);
			dealListRank(doCountList, 2);
			data.put("doCountList", studentConvert.to(doCountList));
		}
		return new Value(data);
	}

	public void dealListRank(List<DiagnosticClassStudent> list, Integer flag) {
		int rank = 1;
		int sameRank = 1;
		Integer preTemp = -1;
		for (int i = 0; i < list.size(); i++) {
			DiagnosticClassStudent pa = list.get(i);
			Integer temp = flag == 1 ? pa.getFloatRank() : pa.getHomeworkCount();
			if (preTemp == -1) {
				pa.setRank(rank);
				rank++;
			} else {
				if (temp == preTemp) {
					pa.setRank(sameRank);
				} else {
					pa.setRank(rank);
					sameRank = rank;
				}
				rank++;
			}
			preTemp = temp;
		}
	}

	/**
	 * 获取老师对应的基本信息<br>
	 * 1.班级列表 2.月表日期列表
	 * 
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "findBaseList", method = { RequestMethod.POST, RequestMethod.GET })
	public Value findBaseList(Long classId) {

		Map<String, Object> dataMap = new HashMap<String, Object>();

		long userId = Security.getUserId();
		Teacher teacher = (Teacher) teacherService.getUser(userId);
		if (teacher == null) {
			return new Value();
		}
		if (teacher.getSubjectCode() == null) {
			return new Value(dataMap);
		}

		// 取最近12个月有数据的月份列表
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M");
		Date minDate = null;
		Date maxDate = null;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.add(Calendar.MONTH, -1);
		try {
			maxDate = format.parse(format.format(cal.getTime()));
		} catch (ParseException e) {
			logger.error("format date error:", e);
		}
		cal.add(Calendar.MONTH, -12);
		try {
			minDate = format.parse(format.format(cal.getTime()));
		} catch (ParseException e) {
			logger.error("format date error:", e);
		}
		List<HomeworkClazz> classList = clazzService.listCurrentClazzs(userId);
		List<Long> classIds = new ArrayList<Long>();
		for (HomeworkClazz hc : classList) {
			classIds.add(hc.getId());
		}
		// 班级列表,当前老师班级下有学情报表数据的集合
		List<HomeworkClazz> clazzs = reportService.getClazzByMinDate(classIds, minDate, maxDate);

		// 年月列表
		List<Map> dates = new ArrayList<Map>();
		if (clazzs.size() > 0 && classId == null) {
			dates = reportService.getDatesByMinDate(clazzs.get(0).getId(), minDate, maxDate);
		} else {
			dates = reportService.getDatesByMinDate(classId, minDate, maxDate);
		}

		dataMap.put("clazzs", clazzs);
		dataMap.put("dates", dates);
		return new Value(dataMap);

	}

	/**
	 * 根据班级ID获取最近一年的年月列表.
	 * 
	 * @param classId
	 *            班级ID.
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "findDates", method = { RequestMethod.POST, RequestMethod.GET })
	public Value findDates(Long classId) {
		if (classId == null) {
			return new Value(new MissingArgumentException());
		}

		long userId = Security.getUserId();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-M");
		Date minDate = null;
		Date maxDate = null;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.add(Calendar.MONTH, -1);
		try {
			maxDate = format.parse(format.format(cal.getTime()));
		} catch (ParseException e) {
			logger.error("format date error:", e);
		}
		cal.add(Calendar.MONTH, -12);
		try {
			minDate = format.parse(format.format(cal.getTime()));
		} catch (ParseException e) {
			logger.error("format date error:", e);
		}

		// 年月列表
		List<Map> dates = reportService.getDatesByMinDate(classId, minDate, maxDate);
		return new Value(dates);
	}

	/**
	 * 月度学情报告第一页<br>
	 * 本月总成绩、作业数等
	 * 
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@SuppressWarnings("unused")
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "getReport1", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getReport1(Long classId, String calDate, Boolean shareFlag) {
		Map<String, Object> data = new HashMap<String, Object>();
		HomeworkClazz clazz = clazzService.get(classId);
		if (clazz == null) {
			return new Value(new EntityNotFoundException());
		}
		Long userId = clazz.getTeacherId();
		Teacher teacher = (Teacher) teacherService.getUser(userId);
		if (calDate == null) {
			calDate = reportService.getLastest(classId);
		}
		Integer month = Integer.parseInt(calDate.substring(5));
		data.put("month", month);
		data.put("className", clazzService.get(classId).getName());
		ClassStatisticsReport report = reportService.getClassReport(teacher.getSubjectCode(), classId,
				Integer.parseInt(calDate.substring(0, 4)), month);
		// ###月度学情报告###
		if (calDate == null || report.getHomeworkNum() == 0 || report.getRightRate().intValue() == -1) {
			data.put("hasHomework", false);
			return new Value(data);
		}
		if (report == null) {
			return new Value(data);
		}
		if (shareFlag != null && shareFlag) {
			boolean isShare = shareLogService.isShare(Biz.CLASS_STATISTICS_REPORT, report.getId());
			if (!isShare) {
				return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_REPORT_SHARE_ERROR));
			}
		}
		if (report.getVersion() == 1) {
			// 如果是老版本的需要跳转到老的界面,下阶段完善
			data.put("version", 1);
			return new Value(data);
		}
		data.put("hasHomework", true);
		data.put("homeworkNum", report.getHomeworkNum());
		data.put("completeRate", report.getCompleteRate());
		data.put("rightRate", report.getRightRate());
		if (report.getHomeworkNum() < 3) {
			data.put("scoreLevel", ScoreLevel.MEDIUM);
		} else {
			Integer completeRate = report.getCompleteRate().intValue();
			Integer rightRate = report.getRightRate().intValue();
			if ((completeRate + rightRate) >= 180) {
				data.put("scoreLevel", ScoreLevel.EXCELLENT);
			} else if ((completeRate + rightRate) >= 141 && (completeRate + rightRate) <= 179) {
				data.put("scoreLevel", ScoreLevel.GOOD);
			} else if ((completeRate + rightRate) >= 101 && (completeRate + rightRate) <= 140) {
				data.put("scoreLevel", ScoreLevel.MEDIUM);
			} else {
				data.put("scoreLevel", ScoreLevel.WEAK);
			}
		}
		return new Value(data);
	}

	/**
	 * 月度学情报告第二页
	 * 
	 * @param classId
	 * @param calDate
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "getReport2", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getReport2(Long classId, String calDate) {
		HomeworkClazz clazz = clazzService.get(classId);
		if (clazz == null) {
			return new Value(new EntityNotFoundException());
		}
		Long userId = clazz.getTeacherId();
		Teacher teacher = (Teacher) teacherService.getUser(userId);
		if (calDate == null) {
			calDate = reportService.getLastest(classId);
		}
		Integer month = Integer.parseInt(calDate.substring(5));
		ClassStatisticsReport report = reportService.getClassReport(teacher.getSubjectCode(), classId,
				Integer.parseInt(calDate.substring(0, 4)), month);
		Map data = new HashMap();
		if (report == null) {
			return new Value(data);
		}
		data = (Map) JSON.parse(report.getStudentRightRates());
		if (CollectionUtils.isEmpty(data)) {
			return new Value(data);
		}
		Map minRightRate = (Map) data.get("minRightRate");
		minRightRate.put("user",
				userConvert.get(Long.parseLong(minRightRate.get("studentId").toString()), new UserConvertOption(true)));
		Map maxRightRate = (Map) data.get("maxRightRate");
		maxRightRate.put("user",
				userConvert.get(Long.parseLong(maxRightRate.get("studentId").toString()), new UserConvertOption(true)));
		List<Map> avgRightRate = (List<Map>) data.get("avgRightRate");
		data.put("totalCommitCount", avgRightRate.size());
		return new Value(data);
	}

	/**
	 * 月度学情报告第三页<br>
	 * 作业正确率变化趋势
	 * 
	 * 
	 * @param classId
	 * @param calDate
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@MemberAllowed
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "getReport3", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getReport3(Long classId, String calDate) {
		Map<String, Object> data = new HashMap<String, Object>();
		MemberType memberType = SecurityContext.getMemberType();
		if (memberType == MemberType.NONE) {
			data.put("isMember", false);
			return new Value(data);
		}
		data.put("isMember", true);
		HomeworkClazz clazz = clazzService.get(classId);
		if (clazz == null) {
			return new Value(new EntityNotFoundException());
		}
		Long userId = clazz.getTeacherId();
		Teacher teacher = (Teacher) teacherService.getUser(userId);
		if (calDate == null) {
			calDate = reportService.getLastest(classId);
		}
		Integer month = Integer.parseInt(calDate.substring(5));
		ClassStatisticsReport report = reportService.getClassReport(teacher.getSubjectCode(), classId,
				Integer.parseInt(calDate.substring(0, 4)), month);
		if (report == null) {
			return new Value(data);
		}
		List<Map> tempList = report.getRightRateMaps();
		List<Map> hkList = new ArrayList<Map>();
		for (Map pa : tempList) {
			Integer rightRate = Integer.parseInt(String.valueOf(pa.get("rightRate")));
			if (rightRate != -1) {
				hkList.add(pa);
			}
		}
		SimpleDateFormat format = new SimpleDateFormat("MM-dd");
		Integer maxRate = 0;
		Integer minRate = 100;
		Map maxRightRate = new HashMap();
		Map minRightRate = new HashMap();
		try {
			for (Map pa : hkList) {
				// 取作业开始时间
				Long startAt = Long.parseLong(String.valueOf(pa.get("startAt")));
				Integer rightRate = Integer.parseInt(String.valueOf(pa.get("rightRate")));
				Date statisticsTime = new Date(startAt);
				pa.put("statisticsTime", format.format(statisticsTime));
				if (rightRate >= maxRate) {
					maxRate = rightRate;
					maxRightRate = pa;
				}
				if (rightRate <= minRate) {
					minRate = rightRate;
					minRightRate = pa;
				}
			}
		} catch (Exception e) {
			logger.error("getReport3 time format error", e);
		}

		data.put("hkList", hkList);
		data.put("maxRightRate", maxRightRate);
		data.put("minRightRate", minRightRate);
		// 0,1,2分别对应UE三个评价
		if (hkList.size() == 1) {
			data.put("comment", 2);
		} else {
			if (maxRate - minRate >= 30) {
				data.put("comment", 0);
			} else {
				data.put("comment", 1);
			}
		}
		return new Value(data);
	}

	/**
	 * 月度学情报告第四页<br>
	 * 优秀榜
	 * 
	 * @param classId
	 * @param calDate
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "getReport4", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getReport4(Long classId, String calDate) {
		Map<String, Object> data = new HashMap<String, Object>();
		HomeworkClazz clazz = clazzService.get(classId);
		if (clazz == null) {
			return new Value(new EntityNotFoundException());
		}
		Long userId = clazz.getTeacherId();
		Teacher teacher = (Teacher) teacherService.getUser(userId);
		if (calDate == null) {
			calDate = reportService.getLastest(classId);
		}
		Integer month = Integer.parseInt(calDate.substring(5));
		ClassStatisticsReport report = reportService.getClassReport(teacher.getSubjectCode(), classId,
				Integer.parseInt(calDate.substring(0, 4)), month);
		Map temp = (Map) JSON.parse(report.getStudentRightRates());
		List<Map> avgRightRate = (List<Map>) temp.get("avgRightRate");
		// 前六名(小于等于)正确率和
		Double totalRate = 0.0;
		// 只取进步排行前六的
		if (avgRightRate.size() > 6) {
			avgRightRate = avgRightRate.subList(0, 6);
		}
		List<Long> studentIds = new ArrayList<Long>();
		for (Map pa : avgRightRate) {
			Long studentId = Long.parseLong(String.valueOf(pa.get("studentId")));
			Double rightRate = Double.parseDouble(String.valueOf(pa.get("rightRate")));
			studentIds.add(studentId);
			totalRate = totalRate + rightRate;
		}
		Map<Long, VUser> userMap = userConvert.mget(studentIds, new UserConvertOption(true));
		for (Map pa : avgRightRate) {
			Long studentId = Long.parseLong(String.valueOf(pa.get("studentId")));
			pa.put("user", userMap.get(studentId));
		}
		data.put("goodList", avgRightRate);
		// 获取上个月学生平均正确率集合，无人新上榜、无上个月数据，不返回newList
		int year = Integer.parseInt(calDate.substring(0, 4));
		if (month == 1) {
			year = year - 1;
			month = 12;
		}
		// 上一个月的数据
		ClassStatisticsReport preReport = reportService.getClassReport(teacher.getSubjectCode(), classId, year, month);
		List<String> newList = new ArrayList<String>();
		// 无上个月数据
		if (preReport != null) {
			Map preTemp = (Map) JSON.parse(preReport.getStudentRightRates());
			List<Map> preAvgRightRate = (List<Map>) preTemp.get("avgRightRate");
			if (preAvgRightRate.size() > 6) {
				preAvgRightRate = preAvgRightRate.subList(0, 6);
			}
			List<Long> newStudentIds = new ArrayList<Long>();
			for (Map pb : preAvgRightRate) {
				Long studentId = Long.parseLong(String.valueOf(pb.get("studentId")));
				if (!studentIds.contains(studentId)) {
					newStudentIds.add(studentId);
				}
			}
			Map<Long, VUser> newUserMap = userConvert.mget(newStudentIds);
			for (Long studentId : newStudentIds) {
				newList.add(newUserMap.get(studentId).getName());
			}
		}
		data.put("newList", newList);
		// 前六名的平均正确率
		data.put("avgRightRate", new BigDecimal(totalRate / avgRightRate.size()).setScale(0, BigDecimal.ROUND_HALF_UP));
		data.put("totalCommitCount", avgRightRate.size());
		return new Value(data);
	}

	/**
	 * 第五页，进步榜、退步榜
	 * 
	 * @param classId
	 * @param calDate
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "getReport5", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getReport5(Long classId, String calDate) {
		HomeworkClazz clazz = clazzService.get(classId);
		if (clazz == null) {
			return new Value(new EntityNotFoundException());
		}
		Long userId = clazz.getTeacherId();
		Teacher teacher = (Teacher) teacherService.getUser(userId);
		if (calDate == null) {
			calDate = reportService.getLastest(classId);
		}
		Integer month = Integer.parseInt(calDate.substring(5));
		ClassStatisticsReport report = reportService.getClassReport(teacher.getSubjectCode(), classId,
				Integer.parseInt(calDate.substring(0, 4)), month);
		Map<String, Object> data = new HashMap<String, Object>();
		Map temp = (Map) JSON.parse(report.getStudentRightRates());
		List<Map> avgRightRate = (List<Map>) temp.get("avgRightRate");
		List<Long> studentIds = new ArrayList<Long>();
		for (Map pa : avgRightRate) {
			Long studentId = Long.parseLong(String.valueOf(pa.get("studentId")));
			studentIds.add(studentId);
		}
		Map<Long, VUser> userMap = userConvert.mget(studentIds, new UserConvertOption(true));
		List<Map> improveList = new ArrayList<Map>();
		List<Map> backwardList = new ArrayList<Map>();

		int improve3RankCount = 0;
		int backward3RankCount = 0;
		// avgRightRate 是按rank顺序保存到数据库的
		for (Map pa : avgRightRate) {
			Long studentId = Long.parseLong(String.valueOf(pa.get("studentId")));
			Long rankingFloating = Long.parseLong(String.valueOf(pa.get("rankingFloating")));
			pa.put("user", userMap.get(studentId));
			if (rankingFloating > 0) {
				improveList.add(pa);
			}
			if (rankingFloating < 0) {
				backwardList.add(pa);
			}
			if (rankingFloating >= 3) {
				improve3RankCount++;
			}
			if (rankingFloating <= -3) {
				backward3RankCount++;
			}
		}
		Collections.sort(improveList, new rankFloatingDownComparator());
		if (improveList.size() > 3) {
			improveList = improveList.subList(0, 3);
		}
		data.put("improveList", improveList);
		Collections.sort(backwardList, new rankFloatingUpComparator());
		if (backwardList.size() > 3) {
			backwardList = backwardList.subList(0, 3);
		}
		data.put("backwardList", backwardList);
		data.put("improve3RankCount", improve3RankCount);
		data.put("backward3RankCount", backward3RankCount);
		data.put("totalCommitCount", avgRightRate.size());
		return new Value(data);
	}

	/**
	 * 按浮动排名降序
	 * 
	 * @author wangsenhao
	 *
	 */
	@SuppressWarnings("rawtypes")
	static class rankFloatingDownComparator implements Comparator {
		public int compare(Object object1, Object object2) {
			Map p1 = (Map) object1;
			Map p2 = (Map) object2;
			Integer a = Integer.parseInt(p1.get("rankingFloating").toString());
			Integer b = Integer.parseInt(p2.get("rankingFloating").toString());
			return b.compareTo(a);
		}
	}

	/**
	 * 按浮动排名升序
	 * 
	 * @author wangsenhao
	 *
	 */
	@SuppressWarnings("rawtypes")
	static class rankFloatingUpComparator implements Comparator {
		public int compare(Object object1, Object object2) {
			Map p1 = (Map) object1;
			Map p2 = (Map) object2;
			Integer a = Integer.parseInt(p1.get("rankingFloating").toString());
			Integer b = Integer.parseInt(p2.get("rankingFloating").toString());
			return a.compareTo(b);
		}
	}

	/**
	 * 第六页接口<br>
	 * 章节掌握情况
	 * 
	 * @param classId
	 * @param calDate
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "getReport6", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getReport6(Long classId, String calDate) {
		HomeworkClazz clazz = clazzService.get(classId);
		if (clazz == null) {
			return new Value(new EntityNotFoundException());
		}
		Long userId = clazz.getTeacherId();
		Teacher teacher = (Teacher) teacherService.getUser(userId);
		if (calDate == null) {
			calDate = reportService.getLastest(classId);
		}
		Integer month = Integer.parseInt(calDate.substring(5));
		ClassStatisticsReport report = reportService.getClassReport(teacher.getSubjectCode(), classId,
				Integer.parseInt(calDate.substring(0, 4)), month);
		List<Map> sectionAnalysisMaps = report.getSectionAnalysisMaps();
		List<Map> tempList = new ArrayList<Map>();
		// 只统计节，不统计小节
		int weakCount = 0;
		for (Map pa : sectionAnalysisMaps) {
			int masterStatus = Integer.parseInt(String.valueOf(pa.get("masterStatus")));
			MasterStatus tempStatus = MasterStatus.findByValue(masterStatus);
			pa.put("masterStatus", tempStatus);
			pa.put("level", 2);
			tempList.add(pa);
			if (tempStatus == MasterStatus.COMMONLY || tempStatus == MasterStatus.WEAK) {
				weakCount = weakCount + 1;
			}
			List<Map> children = (List<Map>) pa.get("children");
			if (CollectionUtils.isNotEmpty(children)) {
				for (Map pb : children) {
					int status = Integer.parseInt(String.valueOf(pb.get("masterStatus")));
					pb.put("masterStatus", MasterStatus.findByValue(status));
					pb.put("level", 3);
					tempList.add(pb);
				}
			}
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("sectionCount", sectionAnalysisMaps.size());
		data.put("weakCount", weakCount);
		data.put("sectionList", tempList);
		return new Value(data);
	}

	/**
	 * 第七页数据<br>
	 * 薄弱知识点
	 * 
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "getReport7", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getReport7(Long classId, String calDate) {
		HomeworkClazz clazz = clazzService.get(classId);
		if (clazz == null) {
			return new Value(new EntityNotFoundException());
		}
		Long userId = clazz.getTeacherId();
		Teacher teacher = (Teacher) teacherService.getUser(userId);
		if (calDate == null) {
			calDate = reportService.getLastest(classId);
		}
		Integer month = Integer.parseInt(calDate.substring(5));
		ClassStatisticsReport report = reportService.getClassReport(teacher.getSubjectCode(), classId,
				Integer.parseInt(calDate.substring(0, 4)), month);
		List<Map> knowpointAnalysisMaps = report.getKnowpointAnalysisMaps();
		Map<String, Object> data = new HashMap<String, Object>();
		if (CollectionUtils.isEmpty(knowpointAnalysisMaps)) {
			data.put("kpCount", 0);
			data.put("weakCount", 0);
			data.put("bizId", report.getId());
			data.put("biz", Biz.CLASS_STATISTICS_REPORT);
			return new Value(data);
		}
		Collections.sort(knowpointAnalysisMaps, new sectionCodeUpComparator());
		// 计算这个月的知识点数量，去重
		Set<Long> kpSet = new HashSet<Long>();
		Set<Long> weakSet = new HashSet<Long>();
		for (Map pa : knowpointAnalysisMaps) {
			int status = Integer.parseInt(String.valueOf(pa.get("masterStatus")));
			pa.put("masterStatus", MasterStatus.findByValue(status));
			List<Map> knowledgePoints = (List<Map>) pa.get("knowledgePoints");
			Collections.sort(knowledgePoints, new scoreUpComparator());
			// 1章最多显示8个薄弱知识点，若超过8个，则显示掌握度最低的8个知识点
			if (knowledgePoints.size() > 8) {
				knowledgePoints = knowledgePoints.subList(0, 8);
			}
			pa.put("knowledgePoints", knowledgePoints);
			if (CollectionUtils.isNotEmpty(knowledgePoints)) {
				for (Map child : knowledgePoints) {
					Long code = Long.parseLong(String.valueOf(child.get("code")));
					kpSet.add(code);
					int masterStatus = Integer.parseInt(String.valueOf(child.get("masterStatus")));
					MasterStatus tempStatus = MasterStatus.findByValue(masterStatus);
					child.put("masterStatus", tempStatus);
					if (tempStatus == MasterStatus.COMMONLY || tempStatus == MasterStatus.WEAK) {
						weakSet.add(code);
					}
				}

			}
		}
		data.put("list", knowpointAnalysisMaps);
		data.put("kpCount", kpSet.size());
		data.put("weakCount", weakSet.size());
		data.put("bizId", report.getId());
		data.put("biz", Biz.CLASS_STATISTICS_REPORT);
		return new Value(data);
	}

	/**
	 * 按score(掌握度)升序，薄弱的排前面
	 * 
	 * @author wangsenhao
	 *
	 */
	@SuppressWarnings("rawtypes")
	static class scoreUpComparator implements Comparator {
		public int compare(Object object1, Object object2) {
			Map p1 = (Map) object1;
			Map p2 = (Map) object2;
			Integer a = Integer.parseInt(p1.get("score").toString());
			Integer b = Integer.parseInt(p2.get("score").toString());
			return a.compareTo(b);
		}
	}

	/**
	 * 按章节排序
	 * 
	 * @author wangsenhao
	 *
	 */
	@SuppressWarnings("rawtypes")
	static class sectionCodeUpComparator implements Comparator {
		public int compare(Object object1, Object object2) {
			Map p1 = (Map) object1;
			Map p2 = (Map) object2;
			Long a = Long.parseLong(p1.get("sectionCode").toString());
			Long b = Long.parseLong(p2.get("sectionCode").toString());
			return a.compareTo(b);
		}
	}

	/**
	 * 获取示例习题
	 * 
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "getQuestionList", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getQuestionList() {
		String questions = Env.getString("class.report.questions");
		// 题目code集合
		List<String> codes = Lists.newArrayList();
		for (String code : questions.split(",")) {
			codes.add(code);
		}
		QuestionConvertOption option = new QuestionConvertOption(false, true, true, true, null);
		option.setInitPublishCount(true);
		option.setInitQuestionSimilarCount(true);
		option.setInitExamination(true);
		option.setInitQuestionTag(true);
		List<Question> questionList = zyQuestionService.findByCodes(codes);
		return new Value(questionConvert.to(questionList, option));
	}

}

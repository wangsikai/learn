package com.lanking.uxb.service.examPaper.resource;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaper;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsStatus;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassLatestHomeworkKnowpoint;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.KnowledgeSectionService;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.convert.KnowledgePointConvert;
import com.lanking.uxb.service.code.value.VKnowledgePoint;
import com.lanking.uxb.service.diagnostic.api.DiagnosticClassLatestHomeworkKnowpointService;
import com.lanking.uxb.service.examPaper.api.ExamPaperCountService;
import com.lanking.uxb.service.examPaper.api.ExamPaperService;
import com.lanking.uxb.service.examPaper.form.ExamQueryForm;
import com.lanking.uxb.service.mall.convert.ExamPaperGoodsConvert;
import com.lanking.uxb.service.mall.convert.ExamPaperGoodsConvertOption;
import com.lanking.uxb.service.mall.value.VExamPaperGoods;
import com.lanking.uxb.service.search.api.IndexService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkQuery;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClassConvertOption;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;

/**
 * 推荐试卷
 * 
 * @since yoomath 2.3.1
 * @author zemin.song
 *
 */
@RestController
@RequestMapping("zy/t/ep/recommend")
public class ZyTeaRecommendExamPaperController {

	private Logger logger = LoggerFactory.getLogger(ZyTeaRecommendExamPaperController.class);
	@Autowired
	private DiagnosticClassLatestHomeworkKnowpointService diagnosticClassLatestHomeworkKnowpointService;
	@Autowired
	private KnowledgeSectionService knowledgeSectionService;
	@Autowired
	private UserService userService;
	@Autowired
	private ExamPaperService examPaperService;
	@Autowired
	private ExamPaperGoodsConvert examPaperGoodsConvert;
	@Autowired
	private ZyHomeworkService zyHomeworkService;
	@Autowired
	private ExamPaperCountService examPaperCountService;
	@Autowired
	private ZyHomeworkClassService homeworkClassService;
	@Autowired
	private ZyHomeworkClazzConvert homeworkClazzConvert;
	@Autowired
	private KnowledgePointService KnowledgePointService;
	@Autowired
	private KnowledgePointConvert knowledgePointConvert;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private IndexService indexService;

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "index")
	public Value index(long classId, @RequestParam(defaultValue = "30") Integer times,
			@RequestParam(defaultValue = "6") Integer weakcount, @RequestParam(defaultValue = "5") Integer smartCount,
			@RequestParam(defaultValue = "5") Integer hotCount, @RequestParam(defaultValue = "true") Boolean noClazz) {
		Map<String, Object> retMap = new HashMap<String, Object>(5);
		Teacher teacher = (Teacher) userService.getUser(UserType.TEACHER, Security.getUserId());

		if (teacher.getTextbookCategoryCode() == null) {
			return new Value(new IllegalArgException());
		}
		if (noClazz) {
			List<HomeworkClazz> clazzs = homeworkClassService.listCurrentClazzs(Security.getUserId());
			retMap.put("clazzs", homeworkClazzConvert.to(clazzs, new ZyHomeworkClassConvertOption(false, true, false)));
		}
		// 薄弱知识点
		List<VExamPaperGoods> examPapers = Lists.newArrayList();
		// 根据班级查询薄弱知识点
		List<DiagnosticClassLatestHomeworkKnowpoint> Knowpoints = diagnosticClassLatestHomeworkKnowpointService
				.findByPage(classId, times, 6);
		List<Long> knowpointCodes = Lists.newArrayList();
		for (DiagnosticClassLatestHomeworkKnowpoint knowpoint : Knowpoints) {
			Double value = new Double((knowpoint.getRightCount() + 1) / (knowpoint.getDoCount() + 2));
			Double value2 = new Double("0.60");
			if (value.compareTo(value2) < 0) {
				knowpointCodes.add(knowpoint.getKnowpointCode());
			}
		}
		if (CollectionUtils.isEmpty(knowpointCodes)) {
			retMap.put("weakKnowpoints", examPapers);
		} else {
			// 薄弱章节
			List<Long> sectionCodes = Lists.newArrayList();

			// 薄弱章节
			List<Section> sections = knowledgeSectionService.findSections(knowpointCodes,
					teacher.getTextbookCategoryCode());

			// 薄弱章
			Set<Long> weakCodes = Sets.newHashSet();
			for (Section section : sections) {
				Section tamSection = section;
				sectionCodes.add(section.getCode());
				while (tamSection.getLevel() != 1) {
					tamSection = sectionService.getPSection(tamSection.getCode());
				}
				weakCodes.add(tamSection.getCode());
			}

			// 章节对应知识点(题目要展示薄弱知识点对应关系)
			Map<Long, List<Long>> sectionsMap = knowledgeSectionService.mGetKnowledgeSectionMap(sectionCodes);

			// 章节对应的知识点Vo
			List<VKnowledgePoint> vks = knowledgePointConvert.to(KnowledgePointService.mgetList(knowpointCodes));

			// 试卷 (取值)
			ExamQueryForm form = new ExamQueryForm();
			form.setSectionCodes(Lists.newArrayList(weakCodes));
			form.setPage(1);
			form.setPageSize(weakcount);
			form.setIsRandom(false);
			form.setOrderBy("clickCount");
			form.setOrder(true);
			form.setStatus(ResourcesGoodsStatus.PUBLISH);
			List<ExamPaper> papers = examPaperService.queryExam(form).getItems();
			Map<Long, List<VKnowledgePoint>> paperMap = new HashMap<Long, List<VKnowledgePoint>>(papers.size());
			for (ExamPaper paper : papers) {
				Set<Long> weakSets = Sets.newHashSet();
				if (paper.getSectionCode() == null) {
					continue;
				}
				Section section = sectionService.get(paper.getSectionCode());
				while (section.getLevel() != 1) {
					section = sectionService.getPSection(section.getCode());
				}
				for (Long code : sectionsMap.keySet()) {
					int length = code.toString().length() - 10;
					int codeMax = (int) (code / (Math.pow(10, length)));
					if (codeMax == section.getCode().longValue()) {
						weakSets.addAll(sectionsMap.get(code));
					}
				}
				List<VKnowledgePoint> vpt = Lists.newArrayList();
				for (VKnowledgePoint vk : vks) {
					for (Long code : weakSets) {
						if (vk.getCode() == code.longValue()) {
							vpt.add(vk);
						}
					}
				}
				paperMap.put(paper.getId(), vpt);
			}

			retMap.put("knowledgePoint", paperMap);
			ExamPaperGoodsConvertOption opn = new ExamPaperGoodsConvertOption();
			opn.setInitGoodsInfo(true);
			retMap.put("weakKnowpointsPaper", examPaperGoodsConvert.to(papers, opn));

		}

		List<VExamPaperGoods> examPapersSmart = Lists.newArrayList();
		List<Long> knowpointCodes2 = Lists.newArrayList();
		ZyHomeworkQuery query = new ZyHomeworkQuery();
		query.setClassId(classId);
		query.setStatus(Sets.newHashSet(HomeworkStatus.ISSUED));
		query.setTeacherId(Security.getUserId());
		// 最近三次已下发作业
		Page<Homework> page = zyHomeworkService.queryHomeworkWeb2(query, P.index(1, 3));
		for (Homework hk : page.getItems()) {
			if (CollectionUtils.isNotEmpty(hk.getKnowledgePoints())) {
				knowpointCodes2.addAll(hk.getKnowledgePoints());
			}
		}
		if (CollectionUtils.isEmpty(knowpointCodes2)) {
			retMap.put("smartPaper", examPapersSmart);
		} else {
			List<Section> sections = knowledgeSectionService.findSections(knowpointCodes2,
					teacher.getTextbookCategoryCode());
			// 薄弱章
			Set<Long> weakCodes = Sets.newHashSet();
			for (Section section : sections) {
				Section tamSection = section;
				while (tamSection.getLevel() != 1) {
					tamSection = sectionService.getPSection(tamSection.getCode());
				}
				weakCodes.add(tamSection.getCode());
			}
			List<Long> weakCodeLs = Lists.newArrayList(weakCodes);
			List<Long> newWeakCodes = Lists.newArrayList();
			Collections.reverse(weakCodeLs);
			if (weakCodeLs.size() > 3) {
				newWeakCodes.add(weakCodeLs.get(0));
				newWeakCodes.add(weakCodeLs.get(1));
				newWeakCodes.add(weakCodeLs.get(2));
			} else {
				newWeakCodes = weakCodeLs;
			}
			ExamQueryForm form = new ExamQueryForm();
			form.setSectionCodes(newWeakCodes);
			form.setPage(1);
			form.setPageSize(smartCount);
			form.setIsRandom(true);
			form.setStatus(ResourcesGoodsStatus.PUBLISH);
			List<ExamPaper> papers = examPaperService.queryExam(form).getItems();
			retMap.put("smartPaper", examPaperGoodsConvert.to(papers));
		}
		if (noClazz) {
			List<VExamPaperGoods> vExamPapers = Lists.newArrayList();
			List<ExamPaper> examPaperList = examPaperService.findRecommendByNdayHot(teacher.getSubjectCode(),
					teacher.getPhaseCode(), hotCount, true, 7);// 7表示一周
			if (CollectionUtils.isEmpty(examPaperList)) {
				examPaperList = examPaperService.findNewPaper(teacher.getSubjectCode(), teacher.getPhaseCode(),
						hotCount);
			}
			if (CollectionUtils.isEmpty(examPaperList)) {
				retMap.put("hotPaper", vExamPapers);
			} else {
				retMap.put("hotPaper", examPaperGoodsConvert.to(examPaperList));
			}
		}
		return new Value(retMap);
	}

	// 根据薄弱知识点查询推荐试卷
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "weakKnowpoints")
	public Value recommendByWeakKnowpoints(long classId, @RequestParam(defaultValue = "30") Integer times,
			@RequestParam(defaultValue = "6") Integer count) {
		Teacher teacher = (Teacher) userService.getUser(UserType.TEACHER, Security.getUserId());
		if (teacher.getTextbookCategoryCode() == null) {
			return new Value(new IllegalArgException());
		}
		Map<String, Object> retMap = new HashMap<String, Object>(2);
		List<VExamPaperGoods> examPapers = Lists.newArrayList();
		// 根据班级查询薄弱知识点
		List<DiagnosticClassLatestHomeworkKnowpoint> Knowpoints = diagnosticClassLatestHomeworkKnowpointService
				.findByPage(classId, times, 6);
		List<Long> knowpointCodes = Lists.newArrayList();
		for (DiagnosticClassLatestHomeworkKnowpoint knowpoint : Knowpoints) {
			Double value = new Double((knowpoint.getRightCount() + 1) / (knowpoint.getDoCount() + 2));
			Double value2 = new Double("0.60");
			if (value.compareTo(value2) < 0) {
				knowpointCodes.add(knowpoint.getKnowpointCode());
			}
		}
		if (CollectionUtils.isEmpty(knowpointCodes)) {
			return new Value(retMap.put("weakKnowpoints", examPapers));
		}
		// 薄弱章节
		List<Section> sections = knowledgeSectionService
				.findSections(knowpointCodes, teacher.getTextbookCategoryCode());
		if (sections.size() == 0) {
			return new Value();
		}
		List<Long> sectionCodes = Lists.newArrayList();
		Set<Long> weakCodes = Sets.newHashSet();
		for (Section section : sections) {
			sectionCodes.add(section.getCode());
			Section tamSection = section;
			while (tamSection.getLevel() != 1) {
				tamSection = sectionService.getPSection(tamSection.getCode());
			}
			weakCodes.add(tamSection.getCode());
		}
		// 章节对应知识点(题目要展示薄弱知识点对应关系)
		Map<Long, List<Long>> sectionsMap = knowledgeSectionService.mGetKnowledgeSectionMap(sectionCodes);
		List<VKnowledgePoint> vks = knowledgePointConvert.to(KnowledgePointService.mgetList(knowpointCodes));
		// 章节对应的知识点Vo
		ExamQueryForm form = new ExamQueryForm();
		form.setSectionCodes(Lists.newArrayList(weakCodes));
		form.setPage(1);
		form.setPageSize(count);
		form.setIsRandom(true);
		form.setStatus(ResourcesGoodsStatus.PUBLISH);
		List<ExamPaper> papers = examPaperService.queryExam(form).getItems();

		Map<Long, List<VKnowledgePoint>> paperMap = new HashMap<Long, List<VKnowledgePoint>>(papers.size());
		for (ExamPaper paper : papers) {
			Set<Long> weakSets = Sets.newHashSet();
			Section section = sectionService.get(paper.getSectionCode());
			if (paper.getSectionCode() == null) {
				continue;
			}
			while (section.getLevel() != 1) {
				section = sectionService.getPSection(section.getCode());
			}
			for (Long code : sectionsMap.keySet()) {
				int length = code.toString().length() - 10;
				int codeMax = (int) (code / (Math.pow(10, length)));
				if (codeMax == section.getCode().longValue()) {
					weakSets.addAll(sectionsMap.get(code));
				}
			}
			List<VKnowledgePoint> vpt = Lists.newArrayList();
			for (VKnowledgePoint vk : vks) {
				for (Long code : weakSets) {
					if (vk.getCode() == code.longValue()) {
						vpt.add(vk);
					}
				}
			}
			paperMap.put(paper.getId(), vpt);
		}

		retMap.put("knowledgePoint", paperMap);
		ExamPaperGoodsConvertOption opn = new ExamPaperGoodsConvertOption();
		opn.setInitGoodsInfo(true);
		retMap.put("weakKnowpointsPaper", examPaperGoodsConvert.to(papers, opn));
		return new Value(retMap);
	}

	// 根据近3次作业为您智能推荐
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "smart")
	public Value recommendBySmart(long classId, @RequestParam(defaultValue = "5") Integer count) {
		Teacher teacher = (Teacher) userService.getUser(UserType.TEACHER, Security.getUserId());
		if (teacher.getTextbookCategoryCode() == null) {
			return new Value(new IllegalArgException());
		}
		Map<String, Object> retMap = new HashMap<String, Object>(1);
		List<VExamPaperGoods> examPapers = Lists.newArrayList();
		List<Long> knowpointCodes = Lists.newArrayList();
		ZyHomeworkQuery query = new ZyHomeworkQuery();
		query.setClassId(classId);
		query.setStatus(Sets.newHashSet(HomeworkStatus.ISSUED));
		query.setTeacherId(Security.getUserId());
		// 最近三次已下发作业
		Page<Homework> page = zyHomeworkService.queryHomeworkWeb2(query, P.index(1, 3));
		for (Homework hk : page.getItems()) {
			if (CollectionUtils.isNotEmpty(hk.getKnowledgePoints())) {
				knowpointCodes.addAll(hk.getKnowledgePoints());
			}
		}
		if (CollectionUtils.isEmpty(knowpointCodes)) {
			return new Value(examPapers);
		}
		List<Section> sections = knowledgeSectionService
				.findSections(knowpointCodes, teacher.getTextbookCategoryCode());
		Set<Long> weakCodes = Sets.newHashSet();
		for (Section section : sections) {
			Section tamSection = section;
			while (tamSection.getLevel() != 1) {
				tamSection = sectionService.getPSection(tamSection.getCode());
			}
			weakCodes.add(tamSection.getCode());
		}
		List<Long> weakCodeLs = Lists.newArrayList(weakCodes);
		List<Long> newWeakCodes = Lists.newArrayList(weakCodes);
		Collections.reverse(weakCodeLs);
		if (weakCodeLs.size() > 3) {
			newWeakCodes.add(weakCodeLs.get(0));
			newWeakCodes.add(weakCodeLs.get(1));
			newWeakCodes.add(weakCodeLs.get(2));
		} else {
			newWeakCodes = weakCodeLs;
		}

		ExamQueryForm form = new ExamQueryForm();
		form.setSectionCodes(Lists.newArrayList(newWeakCodes));
		form.setPage(1);
		form.setPageSize(count);
		form.setIsRandom(true);
		form.setStatus(ResourcesGoodsStatus.PUBLISH);
		List<ExamPaper> papers = examPaperService.queryExam(form).getItems();
		retMap.put("smartPaper", examPaperGoodsConvert.to(papers));
		return new Value(retMap);
	}

	// 试卷点击次数统计
	@RequestMapping(value = "clickCount")
	public Value clickCount(long paperId) {
		Calendar c = Calendar.getInstance();
		// 0~6 7天 星期天为0
		int DAY_OF_WEEK = c.get(Calendar.DAY_OF_WEEK) - 1;
		boolean updateRet = false;
		try {
			examPaperCountService.addOneClick(paperId, DAY_OF_WEEK, 7);
			updateRet = true;
		} catch (Exception e) {
			logger.debug("update exam click count fail:", e);
		}
		if (!updateRet) {
			try {
				examPaperCountService.addOneClick(paperId, DAY_OF_WEEK, 7);
			} catch (Exception e) {
				logger.debug("update exam click count fail:", e);
			}
		}
		// 总数目
		boolean updateRet2 = false;
		try {
			examPaperCountService.addOneClick(paperId, 0, 0);
			updateRet2 = true;
		} catch (Exception e) {
			logger.debug("update exam click count fail:", e);
		}
		if (!updateRet2) {
			try {
				examPaperCountService.addOneClick(paperId, 0, 0);
				updateRet2 = true;
			} catch (Exception e) {
				logger.debug("update exam click count fail:", e);
			}
		}
		if (updateRet2) {
			indexService.syncUpdate(IndexType.EXAM_PAPER, paperId);
		}
		return new Value();
	}
}

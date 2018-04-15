package com.lanking.uxb.service.examPaper.resource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.domain.common.baseData.QuestionType;
import com.lanking.cloud.domain.common.baseData.ResourceCategory;
import com.lanking.cloud.domain.common.baseData.TextbookCategory;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaper;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperQuestion;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.DistrictService;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.KnowledgeSystemService;
import com.lanking.uxb.service.code.api.QuestionKnowledgeService;
import com.lanking.uxb.service.code.api.ResourceCategoryService;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.DistrictConvert;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VDistrict;
import com.lanking.uxb.service.code.value.VResourceCategory;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.examPaper.api.ExamPaperQuestionService;
import com.lanking.uxb.service.examPaper.api.ExamPaperService;
import com.lanking.uxb.service.examPaper.convert.ExamPaperQuestionConvert;
import com.lanking.uxb.service.examPaper.convert.KnowledgePointTreeNodeConvert;
import com.lanking.uxb.service.examPaper.convert.KnowledgeSystemTreeNodeConvert;
import com.lanking.uxb.service.examPaper.value.VExamPaperQuestion;
import com.lanking.uxb.service.examPaper.value.VKnowledgeTreeNode;
import com.lanking.uxb.service.mall.convert.ExamPaperGoodsConvert;
import com.lanking.uxb.service.mall.convert.ExamPaperGoodsConvertOption;
import com.lanking.uxb.service.mall.value.VExamPaperGoods;
import com.lanking.uxb.service.question.api.QuestionSectionService;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.api.QuestionTypeService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.convert.QuestionTypeConvert;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.zuoye.api.ZyQuestionBankService;
import com.lanking.uxb.service.zuoye.form.QuestionQueryForm;

/**
 * 手工组卷服务接口
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
@RestController
@RequestMapping(value = "zy/t/ep")
public class ZyTeaManualCustomExampaperController {

	@Autowired
	private UserService userService;
	@Autowired
	private ExamPaperService examPaperService;
	@Autowired
	private ExamPaperQuestionService examPaperQuestionService;
	@Autowired
	private ExamPaperQuestionConvert examPaperQuestionConvert;
	@Autowired
	private DistrictService districtService;
	@Autowired
	private DistrictConvert districtConvert;
	@Autowired
	private ResourceCategoryService resourceCategoryService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private TextbookCategoryService textbookCategoryService;
	@Autowired
	private TextbookService tbService;
	@Autowired
	private TextbookConvert tbConvert;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private QuestionTypeService questionTypeService;
	@Autowired
	private QuestionTypeConvert questionTypeConvert;
	@Autowired
	private QuestionSectionService questionSectionService;
	@Autowired
	private KnowledgeSystemService knowledgeSystemService;
	@Autowired
	private KnowledgePointService knowledgePointService;
	@Autowired
	private KnowledgeSystemTreeNodeConvert knowledgeSystemTreeNodeConvert;
	@Autowired
	private KnowledgePointTreeNodeConvert knowledgePointTreeNodeConvert;
	@Autowired
	private ZyQuestionBankService questionBankService;
	@Autowired
	private QuestionKnowledgeService questionKnowledgeService;
	@Autowired
	private ExamPaperGoodsConvert examPaperGoodsConvert;

	@RequestMapping(value = "queryResourceQuerySetting", method = { RequestMethod.GET, RequestMethod.POST })
	@RolesAllowed(userTypes = { "TEACHER" })
	public Value queryResourceQuerySetting(Long dtype) {
		Teacher teacher = (Teacher) userService.getUser(UserType.TEACHER, Security.getUserId());
		Integer phaseCode = teacher.getPhaseCode();
		Integer subjectCode = teacher.getSubjectCode();
		List<Long> districtCodes = null;
		if (dtype == null) {
			districtCodes = examPaperService.getDistricts(phaseCode, subjectCode);
		} else if (dtype.longValue() == 1) {
			districtCodes = examPaperService.getDistrictsByGoods(phaseCode, subjectCode);
		} else if (dtype.longValue() == 2) {
			districtCodes = examPaperService.getDistrictsByFavorite(phaseCode, subjectCode, Security.getUserId());
		}
		Map<String, Object> retMap = new HashMap<String, Object>(3);
		Map<Long, VDistrict> vdMap = districtConvert.to(districtService.mget(districtCodes));
		retMap.put("districts", vdMap.values());

		List<ResourceCategory> categories = resourceCategoryService.findAll();
		List<VResourceCategory> vcategories = new ArrayList<VResourceCategory>(categories.size());

		for (ResourceCategory r : categories) {
			VResourceCategory v = new VResourceCategory();
			v.setCode(r.getCode());
			v.setName(r.getName());
			v.setpCode(r.getPcode());

			vcategories.add(v);
		}

		retMap.put("categories", vcategories);
		retMap.put("year", new SimpleDateFormat("yyyy").format(new Date()));

		return new Value(retMap);
	}

	/**
	 * 查询中央资源库试卷中的题目
	 *
	 * @param examId
	 *            试卷id
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "queryResourceExamPaperQuestions", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryResourceExamPaperQuestions(long examId) {
		List<ExamPaperQuestion> questions = examPaperQuestionService.getExamQuestion(examId);

		List<VExamPaperQuestion> vs = examPaperQuestionConvert.to(questions);
		return new Value(vs);
	}

	/**
	 * 综合知识点组卷相关：查询综合知识点树型结构
	 *
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "knowledge/index", method = { RequestMethod.GET, RequestMethod.POST })
	public Value knowledgeIndex() {
		Map<String, Object> retMap = new HashMap<String, Object>(2);
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		// 如果学科为空直接返回空map
		if (teacher.getSubjectCode() == null) {
			return new Value(retMap);
		}
		List<KnowledgePoint> points = knowledgePointService.findBySubject(teacher.getSubjectCode());
		List<Long> codes = Lists.newArrayList();
		for (KnowledgePoint k : points) {
			codes.add(k.getCode());
		}
		List<Long> parentCodes = questionKnowledgeService.queryParentKnowledgeCodes(codes);
		List<KnowledgeSystem> systems = knowledgeSystemService.mgetList(parentCodes);
		List<VKnowledgeTreeNode> nodes = knowledgePointTreeNodeConvert.to(points);
		nodes.addAll(knowledgeSystemTreeNodeConvert.to(systems));
		List<QuestionType> qtList = questionTypeService.findBySubject(teacher.getSubjectCode());
		retMap.put("tree", nodes);
		retMap.put("questionTypes", qtList);
		return new Value(retMap);

	}

	/**
	 * 综合知识点查询题目数据
	 *
	 * @param form
	 *            {@link QuestionQueryForm}
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "queryQuestionsByKnowledge", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryQuestionsByKnowledge(QuestionQueryForm form) {
		form.setNewKeyQuery(true);
		VPage<VQuestion> questions = questionBankService.queryQuestionBankByIndex2(form);
		return new Value(questions);
	}

	/**
	 * 查询题目更具题目ids
	 *
	 * @param ids
	 *            <Long> 试卷ids
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "queryQuestionsByExamPaperIds", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryQuestionsByExamPaperIds(@RequestParam(value = "ids") List<Long> ids) {
		List<Question> questionList = questionService.mgetList(ids);
		QuestionConvertOption option = new QuestionConvertOption();
		option.setAnalysis(true);
		option.setInitExamination(true);
		option.setAnswer(true);
		List<VQuestion> vqlist = questionConvert.to(questionList, option);
		return new Value(vqlist);
	}

	/**
	 * 精品试卷查询题目
	 *
	 * @param examId
	 *            精品试卷id
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "excellentPaperQuestion", method = { RequestMethod.GET, RequestMethod.POST })
	public Value excellentPaperQuestion(long examId) {
		Map<String, Object> retMap = new HashMap<String, Object>(2);
		List<ExamPaperQuestion> questions = examPaperQuestionService.getExamQuestion(examId);
		List<VExamPaperQuestion> vs = examPaperQuestionConvert.to(questions);

		ExamPaper examPaper = examPaperService.get(examId);
		ExamPaperGoodsConvertOption convertOption = new ExamPaperGoodsConvertOption();
		convertOption.setInitGoodsInfo(true);
		VExamPaperGoods vpaper = examPaperGoodsConvert.to(examPaper, convertOption);

		retMap.put("questions", vs);
		retMap.put("paper", vpaper);

		return new Value(retMap);
	}

	/**
	 * 同步章节，获取老师教材版本章节
	 *
	 * @author wangsenhao
	 * @param textbookCode
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "syncChapter/index", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getQuestionIndex(@RequestParam(value = "textbookCode", required = false) Integer textbookCode) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Integer> qtCodes = new ArrayList<Integer>();
		if (textbookCode == null) {
			Teacher teacher = (Teacher) teacherService.getUser(Security.getUserId());
			List<QuestionType> qtList = questionTypeService.findBySubject(teacher.getSubjectCode());
			for (QuestionType questionType : qtList) {
				if (questionType.getName().equals("证明题") || questionType.getName().equals("解答题")
						|| questionType.getName().equals("计算题")) {
					qtCodes.add(questionType.getCode());
				}
			}
			Integer textbookCategory = teacher.getTextbookCategoryCode();
			Integer userTextbookCode = teacher.getTextbookCode();
			if (textbookCategory == null) {
				if (SubjectService.PHASE_2_MATH == teacher.getSubjectCode()) {
					textbookCategory = TextbookCategoryService.SU_KE_BAN;
				}
				if (SubjectService.PHASE_3_MATH == teacher.getSubjectCode()) {
					textbookCategory = TextbookCategoryService.SU_JIAO_BAN;
				}
			}
			TextbookCategory tbCate = textbookCategoryService.get(textbookCategory);
			data.put("textbookCategory", tbCate);
			List<VTextbook> textbooks = tbConvert.to(tbService.find(Product.YOOMATH, teacher.getPhaseCode(),
					teacher.getSubjectCode(), Lists.newArrayList(textbookCategory)));
			if (userTextbookCode != null) {
				textbookCode = userTextbookCode;
			} else {
				textbookCode = textbooks.get(0).getCode();
			}
			data.put("textbook", tbService.get(textbookCode));
			data.put("textbooks", textbooks);
			data.put("questionType", questionTypeConvert.to(qtList));

		}

		if (textbookCode == null) {
			data.put("sections", Collections.EMPTY_LIST);
		} else {
			List<VSection> vsections = sectionConvert.to(sectionService.findByTextbookCode(textbookCode));
			Map<Long, Long> countMap = questionSectionService.statisSectionQuestionCount(2, textbookCode, qtCodes);
			for (VSection v : vsections) {
				if (countMap.containsKey(v.getCode())) {
					v.setQuestionCount(countMap.get(v.getCode()).longValue());
				}
			}
			data.put("sections", sectionConvert.assemblySectionTreeFilterNoQuestion(vsections));
		}
		return new Value(data);
	}

}

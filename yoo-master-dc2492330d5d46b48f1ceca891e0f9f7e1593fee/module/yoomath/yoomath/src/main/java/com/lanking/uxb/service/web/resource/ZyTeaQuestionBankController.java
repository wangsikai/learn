package com.lanking.uxb.service.web.resource;

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

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.domain.common.baseData.Knowpoint;
import com.lanking.cloud.domain.common.baseData.QuestionType;
import com.lanking.cloud.domain.common.baseData.TextbookCategory;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.support.resources.question.QuestionErrorType;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.KnowledgeSectionService;
import com.lanking.uxb.service.code.api.KnowledgeSystemService;
import com.lanking.uxb.service.code.api.KnowpointService;
import com.lanking.uxb.service.code.api.QuestionKnowledgeService;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.KnowledgePointConvert;
import com.lanking.uxb.service.code.convert.KnowpointConvert;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.examPaper.convert.KnowledgePointTreeNodeConvert;
import com.lanking.uxb.service.examPaper.convert.KnowledgeSystemTreeNodeConvert;
import com.lanking.uxb.service.examPaper.value.VKnowledgeTreeNode;
import com.lanking.uxb.service.question.api.QuestionSectionService;
import com.lanking.uxb.service.resources.api.QuestionTypeService;
import com.lanking.uxb.service.resources.convert.QuestionTypeConvert;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.zuoye.api.ZyQuestionBankService;
import com.lanking.uxb.service.zuoye.api.ZyQuestionErrorService;
import com.lanking.uxb.service.zuoye.convert.ZyLevelKnowpointConvert;
import com.lanking.uxb.service.zuoye.form.QuestionQueryForm;
import com.lanking.uxb.service.zuoye.value.VLevelKnowpoint;

/**
 * 公共题库相关接口
 * 
 * @since yoomath V1.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年9月6日
 */
@RolesAllowed(userTypes = "TEACHER")
@ApiAllowed
@RestController
@RequestMapping("zy/t/qbank")
public class ZyTeaQuestionBankController {
	@Autowired
	private ZyQuestionErrorService zyQuestionErrorService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private ZyQuestionBankService zyQuestionBankService;
	@Autowired
	private KnowpointService knowPointService;
	@Autowired
	private KnowpointConvert knowPointConvert;
	@Autowired
	private ZyLevelKnowpointConvert zlkConvert;
	@Autowired
	private KnowledgeSystemTreeNodeConvert knowledgeSystemTreeNodeConvert;
	@Autowired
	private KnowledgePointService knowledgePointService;
	@Autowired
	private QuestionTypeService questionTypeService;
	@Autowired
	private KnowledgeSystemService knowledgeSystemService;
	@Autowired
	private QuestionTypeConvert questionTypeConvert;
	@Autowired
	private QuestionSectionService questionSectionService;
	@Autowired
	private QuestionKnowledgeService questionKnowledgeService;
	@Autowired
	private KnowledgePointTreeNodeConvert knowledgePointTreeNodeConvert;
	@Autowired
	private TextbookService tbService;
	@Autowired
	private TextbookConvert tbConvert;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private TextbookCategoryService textbookCategoryService;
	@Autowired
	private KnowledgeSectionService kSectionService;
	@Autowired
	private KnowledgePointConvert knowledgePointConvert;

	/**
	 * 查询公共题库 (查审核通过，默认苏教版，当前教师的阶段)
	 * 
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "query", method = { RequestMethod.POST, RequestMethod.GET })
	public Value query(QuestionQueryForm form) {
		return new Value(zyQuestionBankService.queryQuestionBankByIndex(form));
	}

	/**
	 * 查询公共题库 (包含解答题)
	 * 
	 * @since yoomath V1.9.1
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "2/query", method = { RequestMethod.POST, RequestMethod.GET })
	public Value query2(QuestionQueryForm form) {
		return new Value(zyQuestionBankService.queryQuestionBankByIndex2(form));
	}

	/**
	 * 
	 * 纠错
	 * 
	 * @param type
	 *            纠错类型
	 * @param questionId
	 *            问题id
	 * @param description
	 *            描述
	 * @return
	 */
	@RequestMapping(value = "questionError", method = { RequestMethod.POST, RequestMethod.GET })
	public Value questionError(@RequestParam(value = "types", required = false) List<QuestionErrorType> types,
			@RequestParam(value = "questionId") Long questionId,
			@RequestParam(value = "description", required = false) String description) {
		zyQuestionErrorService.saveError(description, types, questionId, Security.getUserId());
		return new Value();
	}

	/**
	 * 公共题库知识点树
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "knowPointTree", method = { RequestMethod.POST, RequestMethod.GET })
	public Value knowPointTree() {
		Map<String, Object> data = new HashMap<String, Object>(3);
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		List<Knowpoint> kpList = knowPointService.listAllBySubject(teacher.getSubjectCode());
		List<VLevelKnowpoint> vlkList = zlkConvert.to(knowPointConvert.to(kpList));
		data.put("knowpoints", zlkConvert.assemblySectionTree(vlkList));
		return new Value(data);
	}

	/**
	 * 公共题库新知识点树
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "knowledgePointTree", method = { RequestMethod.POST, RequestMethod.GET })
	public Value knowledgePointTree() {
		Map<String, Object> data = new HashMap<String, Object>(1);
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		if (teacher.getSubjectCode() == null) {
			return new Value(data);
		}
		List<KnowledgePoint> points = knowledgePointService.findBySubject(teacher.getSubjectCode());
		List<Long> codes = Lists.newArrayList();
		for (KnowledgePoint k : points) {
			codes.add(k.getCode());
		}
		List<Long> parentCodes = questionKnowledgeService.queryParentKnowledgeCodes(codes);
		List<KnowledgeSystem> systems = knowledgeSystemService.mgetList(parentCodes);
		List<VKnowledgeTreeNode> nodes = knowledgePointTreeNodeConvert.to(points);
		List<VKnowledgeTreeNode> newList = knowledgeSystemTreeNodeConvert.to(systems);
		newList.addAll(nodes);
		data.put("knowpoints", knowledgeSystemTreeNodeConvert.assemblyPointTree(newList));
		return new Value(data);

	}

	/**
	 * 获取当前老师对应的教材版本章节
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "index", method = { RequestMethod.POST, RequestMethod.GET })
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
			data.put("questionType",
					questionTypeConvert.to(questionTypeService.findBySubject(teacher.getSubjectCode())));

		}

		if (textbookCode == null) {
			data.put("sections", Collections.EMPTY_LIST);
		} else {
			List<VSection> vsections = sectionConvert.to(sectionService.findByTextbookCode(textbookCode));
			data.put("sections", sectionConvert.assemblySectionTree(vsections));
		}
		return new Value(data);
	}

	/**
	 * 通过章节、节、小节获取对应的知识点
	 * 
	 * @param code
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "getKpsByCode", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getKpsByCode(long code) {
		List<Long> codes = kSectionService.getBySection(code);
		return new Value(knowledgePointConvert.mgetList(codes));
	}
}

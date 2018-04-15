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
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.school.SchoolQuestion;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.KnowledgeSystemService;
import com.lanking.uxb.service.code.api.KnowpointService;
import com.lanking.uxb.service.code.api.QuestionKnowledgeService;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.KnowledgeSystemConvert;
import com.lanking.uxb.service.code.convert.KnowpointConvert;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.code.value.VTextbookCategory;
import com.lanking.uxb.service.examPaper.convert.KnowledgePointTreeNodeConvert;
import com.lanking.uxb.service.examPaper.convert.KnowledgeSystemTreeNodeConvert;
import com.lanking.uxb.service.examPaper.value.VKnowledgeTreeNode;
import com.lanking.uxb.service.resources.api.QuestionTypeService;
import com.lanking.uxb.service.resources.convert.QuestionTypeConvert;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.zuoye.api.ZySchoolQuestionService;
import com.lanking.uxb.service.zuoye.convert.ZyLevelKnowpointConvert;
import com.lanking.uxb.service.zuoye.convert.ZySchoolQuestionConvert;
import com.lanking.uxb.service.zuoye.form.QuestionQueryForm;
import com.lanking.uxb.service.zuoye.value.VLevelKnowpoint;
import com.lanking.uxb.service.zuoye.value.VSchoolQuestion;

/**
 * 学校题库
 * 
 * @since yoomath V1.4.2
 * @author wangsenhao
 *
 */
@ApiAllowed
@RestController
@RequestMapping("zy/t/qschool")
public class ZyTeaQuestionSchoolController {

	@Autowired
	private TeacherService teacherService;
	@Autowired
	private TextbookCategoryService tbcService;
	@Autowired
	private TextbookCategoryConvert tbcConvert;
	@Autowired
	private TextbookService tbService;
	@Autowired
	private TextbookConvert tbConvert;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private ZySchoolQuestionService zySchoolQuestionService;
	@Autowired
	private KnowpointService knowPointService;
	@Autowired
	private KnowpointConvert knowPointConvert;
	@Autowired
	private ZyLevelKnowpointConvert zlkConvert;
	@Autowired
	private QuestionTypeService questionTypeService;
	@Autowired
	private QuestionTypeConvert questionTypeConvert;
	@Autowired
	private ZySchoolQuestionConvert zySchoolQuestionConvert;
	@Autowired
	private KnowledgeSystemService knowledgeSystemService;
	@Autowired
	private KnowledgeSystemConvert knowledgeSystemConvert;
	@Autowired
	private QuestionKnowledgeService questionKnowledgeService;
	@Autowired
	private KnowledgeSystemTreeNodeConvert knowledgeSystemTreeNodeConvert;
	@Autowired
	private KnowledgePointTreeNodeConvert knowledgePointTreeNodeConvert;
	@Autowired
	private KnowledgePointService knowledgePointService;

	/**
	 * 查询学校题库
	 * 
	 * @param form
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "query", method = { RequestMethod.POST, RequestMethod.GET })
	public Value query(QuestionQueryForm form) {
		// 只有是校级会员才会去掉这个接口，不会出现学校Id为空的情况
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		form.setUserId(Security.getUserId());
		form.setSchoolId(teacher.getSchoolId());
		Page<SchoolQuestion> cp = zySchoolQuestionService.querySchoolQuestionByIndex(form,
				P.index(form.getPage(), form.getPageSize()));
		VPage<VSchoolQuestion> vp = new VPage<VSchoolQuestion>();
		int tPage = (int) (cp.getTotalCount() + form.getPageSize() - 1) / form.getPageSize();
		vp.setPageSize(form.getPageSize());
		vp.setCurrentPage(form.getPage());
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		vp.setItems(zySchoolQuestionConvert.to(cp.getItems()));
		return new Value(vp);
	}

	/**
	 * 查询学校题库(包含解答题)
	 * 
	 * @since yoomath V1.9.1
	 * @param form
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "2/query", method = { RequestMethod.POST, RequestMethod.GET })
	public Value query2(QuestionQueryForm form) {
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		form.setUserId(Security.getUserId());
		form.setSchoolId(teacher.getSchoolId());
		Page<SchoolQuestion> cp = zySchoolQuestionService.querySchoolQuestionByIndex2(form,
				P.index(form.getPage(), form.getPageSize()));
		VPage<VSchoolQuestion> vp = new VPage<VSchoolQuestion>();
		int tPage = (int) (cp.getTotalCount() + form.getPageSize() - 1) / form.getPageSize();
		vp.setPageSize(form.getPageSize());
		vp.setCurrentPage(form.getPage());
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		vp.setItems(zySchoolQuestionConvert.to(cp.getItems()));
		return new Value(vp);
	}

	/**
	 * 学校题库章节树(不包含解答题)
	 * 
	 * @param textbookCode
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "index", method = { RequestMethod.POST, RequestMethod.GET })
	public Value index(@RequestParam(value = "textbookCode", required = false) Integer textbookCode) {
		Map<String, Object> data = new HashMap<String, Object>();
		Teacher teacher = (Teacher) teacherService.getUser(Security.getUserId());
		if (teacher.getSchoolId() == null) {
			// 直接返回，页面学校题库tab页面不展示
			data.put("textbooks", new ArrayList<VTextbook>(0));
			return new Value(data);
		}
		List<QuestionType> qtList = questionTypeService.findBySubject(teacher.getSubjectCode());
		List<Integer> qtCodes = new ArrayList<Integer>();
		for (QuestionType questionType : qtList) {
			if (questionType.getName().equals("证明题") || questionType.getName().equals("解答题")
					|| questionType.getName().equals("计算题")) {
				qtCodes.add(questionType.getCode());
			}
		}
		if (textbookCode == null) {
			List<TextbookCategory> tbcList = null;
			Integer textbookCategory = teacher.getTextbookCategoryCode();
			Integer userTextbookCode = teacher.getTextbookCode();
			if (textbookCategory == null) {
				if (teacher.getSubjectCode() == SubjectService.PHASE_2_MATH) {
					textbookCategory = TextbookCategoryService.SU_KE_BAN;
					tbcList = Lists.newArrayList(tbcService.get(textbookCategory));
				} else if (teacher.getSubjectCode() == SubjectService.PHASE_3_MATH) {
					textbookCategory = TextbookCategoryService.SU_JIAO_BAN;
					tbcList = Lists.newArrayList(tbcService.get(textbookCategory));
				} else {
					tbcList = tbcService.getAll();
				}
			} else {
				tbcList = Lists.newArrayList(tbcService.get(textbookCategory));
			}

			List<VTextbook> textbooks = tbConvert.to(tbService.find(teacher.getPhaseCode(), textbookCategory,
					teacher.getSubjectCode()));
			List<Integer> tbCodes = new ArrayList<Integer>(textbooks.size());
			for (VTextbook v : textbooks) {
				tbCodes.add(v.getCode());
			}
			Map<Integer, Boolean> cacheMap = zySchoolQuestionService.statisTextbookExistSchoolWithCache(tbCodes,
					teacher.getSchoolId(), qtCodes);
			List<VTextbook> tbs = new ArrayList<VTextbook>(textbooks.size());
			for (VTextbook v : textbooks) {
				if (cacheMap.get(v.getCode())) {
					tbs.add(v);
				}
			}
			data.put("textbooks", tbs);
			// 版本修改，如果tbs为空，直接返回，页面学校题库tab页面不展示
			if (tbs.size() == 0) {
				return new Value(data);
			}
			List<VTextbookCategory> categories = tbcConvert.to(tbcList);
			data.put("textbookCategories", categories);
			data.put("questionType",
					questionTypeConvert.to(questionTypeService.findBySubject(teacher.getSubjectCode())));
			// 1.用户的教材不为空 2.学校题库里面有此教材的
			if (cacheMap.get(userTextbookCode) && userTextbookCode != null) {
				textbookCode = userTextbookCode;
			} else {
				for (VTextbook v : tbs) {
					if (v.getCategoryCode() == categories.get(0).getCode()) {
						textbookCode = v.getCode();
						break;
					}
				}
			}
			data.put("textbook", tbService.get(textbookCode));
		}
		if (textbookCode == null) {
			data.put("sections", Collections.EMPTY_LIST);
			data.put("total", 0);
		} else {
			List<VSection> vsections = sectionConvert.to(sectionService.findByTextbookCode(textbookCode));
			Map<Long, Long> countMap = zySchoolQuestionService.statisSectionSchool(textbookCode, teacher.getSchoolId(),
					qtCodes);
			long total = 0;
			for (VSection v : vsections) {
				if (countMap.containsKey(v.getCode())) {
					total += countMap.get(v.getCode());
					v.setSchoolQCount(countMap.get(v.getCode()).longValue());
				}
			}
			// 重新组装为树形结构
			data.put("sections", sectionConvert.assemblySectionTree(vsections));
			data.put("total", total);
		}
		return new Value(data);
	}

	/**
	 * 学校题库章节树(包含解答题)
	 * 
	 * @since yoomath V1.9.1
	 * @param textbookCode
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "2/index", method = { RequestMethod.POST, RequestMethod.GET })
	public Value index2(@RequestParam(value = "textbookCode", required = false) Integer textbookCode) {
		Map<String, Object> data = new HashMap<String, Object>();
		Teacher teacher = (Teacher) teacherService.getUser(Security.getUserId());
		if (teacher.getSchoolId() == null) {
			// 直接返回，页面学校题库tab页面不展示
			data.put("textbooks", new ArrayList<VTextbook>(0));
			return new Value(data);
		}
		if (textbookCode == null) {
			List<TextbookCategory> tbcList = null;
			Integer textbookCategory = teacher.getTextbookCategoryCode();
			Integer userTextbookCode = teacher.getTextbookCode();
			if (textbookCategory == null) {
				if (teacher.getSubjectCode() == SubjectService.PHASE_2_MATH) {
					textbookCategory = TextbookCategoryService.SU_KE_BAN;
					tbcList = Lists.newArrayList(tbcService.get(textbookCategory));
				} else if (teacher.getSubjectCode() == SubjectService.PHASE_3_MATH) {
					textbookCategory = TextbookCategoryService.SU_JIAO_BAN;
					tbcList = Lists.newArrayList(tbcService.get(textbookCategory));
				} else {
					tbcList = tbcService.getAll();
				}
			} else {
				tbcList = Lists.newArrayList(tbcService.get(textbookCategory));
			}

			List<VTextbook> textbooks = tbConvert.to(tbService.find(teacher.getPhaseCode(), textbookCategory,
					teacher.getSubjectCode()));
			List<Integer> tbCodes = new ArrayList<Integer>(textbooks.size());
			for (VTextbook v : textbooks) {
				tbCodes.add(v.getCode());
			}
			Map<Integer, Boolean> cacheMap = zySchoolQuestionService.statisTextbookExistSchoolWithCache2(tbCodes,
					teacher.getSchoolId());
			List<VTextbook> tbs = new ArrayList<VTextbook>(textbooks.size());
			for (VTextbook v : textbooks) {
				if (cacheMap.get(v.getCode())) {
					tbs.add(v);
				}
			}
			data.put("textbooks", tbs);
			// 版本修改，如果tbs为空，直接返回，页面学校题库tab页面不展示
			if (tbs.size() == 0) {
				return new Value(data);
			}
			List<VTextbookCategory> categories = tbcConvert.to(tbcList);
			data.put("textbookCategories", categories);
			data.put("questionType",
					questionTypeConvert.to(questionTypeService.findBySubject(teacher.getSubjectCode())));
			// 1.用户的教材不为空 2.学校题库里面有此教材的
			if (cacheMap.get(userTextbookCode) && userTextbookCode != null) {
				textbookCode = userTextbookCode;
			} else {
				for (VTextbook v : tbs) {
					if (v.getCategoryCode() == categories.get(0).getCode()) {
						textbookCode = v.getCode();
						break;
					}
				}
			}
			data.put("textbook", tbService.get(textbookCode));
		}
		if (textbookCode == null) {
			data.put("sections", Collections.EMPTY_LIST);
			data.put("total", 0);
		} else {
			List<VSection> vsections = sectionConvert.to(sectionService.findByTextbookCode(textbookCode));
			Map<Long, Long> countMap = zySchoolQuestionService.statisSectionSchool2(textbookCode,
					teacher.getSchoolId(), null);
			long total = 0;
			for (VSection v : vsections) {
				if (countMap.containsKey(v.getCode())) {
					total += countMap.get(v.getCode());
					v.setSchoolQCount(countMap.get(v.getCode()).longValue());
				}
			}
			// 重新组装为树形结构
			data.put("sections", sectionConvert.assemblySectionTreeFilterNoSchoolQuestion(vsections));
			data.put("total", total);
		}
		return new Value(data);
	}

	/**
	 * 我的收藏知识点树(不包含解答题)
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
		List<QuestionType> qtList = questionTypeService.findBySubject(teacher.getSubjectCode());
		List<Integer> qtCodes = new ArrayList<Integer>();
		for (QuestionType questionType : qtList) {
			if (questionType.getName().equals("证明题") || questionType.getName().equals("解答题")
					|| questionType.getName().equals("计算题")) {
				qtCodes.add(questionType.getCode());
			}
		}
		Map<Integer, Integer> countMaps = zySchoolQuestionService.statisKnowPointSchool(teacher.getSubjectCode(),
				teacher.getSchoolId(), qtCodes);
		for (VLevelKnowpoint v : vlkList) {
			if (countMaps.containsKey(v.getCode().intValue())) {
				v.setSchoolQCount((countMaps.get(v.getCode().intValue()).longValue()));
			}
		}
		data.put("knowpoints", zlkConvert.assemblySectionTree(vlkList));
		return new Value(data);
	}

	/**
	 * 我的收藏知识点树(包含解答题)
	 * 
	 * @since yoomath V1.9.1
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "2/knowPointTree", method = { RequestMethod.POST, RequestMethod.GET })
	public Value knowPointTree2() {
		Map<String, Object> data = new HashMap<String, Object>(3);
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		List<Knowpoint> kpList = knowPointService.listAllBySubject(teacher.getSubjectCode());
		List<VLevelKnowpoint> vlkList = zlkConvert.to(knowPointConvert.to(kpList));
		Map<Integer, Integer> countMaps = zySchoolQuestionService.statisKnowPointSchool2(teacher.getSubjectCode(),
				teacher.getSchoolId());
		for (VLevelKnowpoint v : vlkList) {
			if (countMaps.containsKey(v.getCode().intValue())) {
				v.setSchoolQCount((countMaps.get(v.getCode().intValue()).longValue()));
			}
		}
		data.put("knowpoints", zlkConvert.assemblySectionTree(vlkList));
		return new Value(data);
	}

	/**
	 * 新知识点树
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "newKnowPointTree", method = { RequestMethod.POST, RequestMethod.GET })
	public Value newKnowPointTree() {
		Map<String, Object> retMap = new HashMap<String, Object>(1);
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		// 如果学科为空直接返回空map
		if (teacher.getSubjectCode() == null) {
			return new Value(retMap);
		}
		List<KnowledgePoint> points = knowledgePointService.findBySubject(teacher.getSubjectCode());
		List<Long> codes = new ArrayList<Long>();
		for (KnowledgePoint k : points) {
			codes.add(k.getCode());
		}
		List<Long> parentCodes = questionKnowledgeService.queryParentKnowledgeCodes(codes);
		List<KnowledgeSystem> systems = knowledgeSystemService.mgetList(parentCodes);
		List<VKnowledgeTreeNode> nodes = knowledgePointTreeNodeConvert.to(points);
		Map<Long, Long> countMaps = zySchoolQuestionService.getNewKnowpointSchoolQCount(teacher.getSchoolId(),
				teacher.getSubjectCode());
		for (VKnowledgeTreeNode n : nodes) {
			if (countMaps.keySet().contains(n.getCode())) {
				n.setSchoolQCount(countMaps.get(n.getCode()));
			}
		}
		List<VKnowledgeTreeNode> newList = knowledgeSystemTreeNodeConvert.to(systems);
		newList.addAll(nodes);
		retMap.put("tree", knowledgeSystemTreeNodeConvert.assemblyPointTreeFilterNoSchoolQuestion(newList));
		return new Value(retMap);
	}
}

package com.lanking.uxb.service.web.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.domain.common.baseData.Knowpoint;
import com.lanking.cloud.domain.common.baseData.QuestionType;
import com.lanking.cloud.domain.common.baseData.TextbookCategory;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.fallible.TeacherFallibleQuestion;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.KnowledgeSystemService;
import com.lanking.uxb.service.code.api.KnowpointService;
import com.lanking.uxb.service.code.api.QuestionKnowledgeService;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.api.TextbookService;
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
import com.lanking.uxb.service.fallible.cache.TeacherFallibleCacheService;
import com.lanking.uxb.service.resources.api.QuestionTypeService;
import com.lanking.uxb.service.resources.convert.QuestionTypeConvert;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.zuoye.api.ZyTeacherFallibleQuestionQuery;
import com.lanking.uxb.service.zuoye.api.ZyTeacherFallibleQuestionService;
import com.lanking.uxb.service.zuoye.convert.ZyLevelKnowpointConvert;
import com.lanking.uxb.service.zuoye.convert.ZyTeacherFallibleQuestionConvert;
import com.lanking.uxb.service.zuoye.form.QuestionQueryForm;
import com.lanking.uxb.service.zuoye.value.VLevelKnowpoint;
import com.lanking.uxb.service.zuoye.value.VTeacherFallibleQuestion;

/**
 * 教师 题库 错题 相关接口
 * 
 * @since yoomath V1.3
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年9月7日 下午4:57:19
 */
@ApiAllowed
@RestController
@RequestMapping("zy/t/fallible")
public class ZyTeaFallibleQuestion2Controller {

	@Autowired
	private KnowpointService knowPointService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private KnowpointConvert knowPointConvert;
	@Autowired
	private ZyLevelKnowpointConvert zlkConvert;
	@Autowired
	private ZyTeacherFallibleQuestionService tfqService;
	@Autowired
	private QuestionTypeService questionTypeService;
	@Autowired
	private QuestionTypeConvert questionTypeConvert;
	@Autowired
	private ZyTeacherFallibleQuestionConvert tfqConvert;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private TextbookCategoryConvert tbcConvert;
	@Autowired
	private TextbookService tbService;
	@Autowired
	private TextbookConvert tbConvert;
	@Autowired
	private TextbookCategoryService tbcService;
	@Autowired
	private KnowledgeSystemService knowledgeSystemService;
	@Autowired
	private QuestionKnowledgeService questionKnowledgeService;
	@Autowired
	private KnowledgeSystemTreeNodeConvert knowledgeSystemTreeNodeConvert;
	@Autowired
	private KnowledgePointTreeNodeConvert knowledgePointTreeNodeConvert;
	@Autowired
	private KnowledgePointService knowledgePointService;
	@Autowired
	private TeacherFallibleCacheService teacherFallibleCacheService;

	/**
	 * 通过当前用户学科获取知识点列表（带层次 ） [章节部分]
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "2/sectionTree", method = { RequestMethod.POST, RequestMethod.GET })
	public Value sectionTree2(@RequestParam(value = "textbookCode", required = false) Integer textbookCode) {
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		Map<String, Object> data = new HashMap<String, Object>(7);
		if (textbookCode == null) {
			data.put("questionType",
					questionTypeConvert.to(questionTypeService.findBySubject(teacher.getSubjectCode())));
			List<TextbookCategory> tbcList = null;
			Integer textbookCategory = null;
			if (teacher.getTextbookCategoryCode() == null) {
				if (teacher.getSubjectCode() == 202) {
					textbookCategory = TextbookCategoryService.SU_KE_BAN;
					tbcList = Lists.newArrayList(tbcService.get(TextbookCategoryService.SU_KE_BAN));
				} else if (teacher.getSubjectCode() == 302) {
					textbookCategory = TextbookCategoryService.SU_JIAO_BAN;
					tbcList = Lists.newArrayList(tbcService.get(TextbookCategoryService.SU_JIAO_BAN));
				} else {
					tbcList = tbcService.getAll();
				}
			} else {
				textbookCategory = teacher.getTextbookCategoryCode();
				tbcList = Lists.newArrayList(tbcService.get(textbookCategory));
			}
			List<VTextbookCategory> categories = tbcConvert.to(tbcList);
			data.put("textbookCategories", categories);
			List<VTextbook> textbooks = tbConvert.to(tbService.find(Product.YOOMATH, teacher.getPhaseCode(),
					teacher.getSubjectCode(), Lists.newArrayList(teacher.getTextbookCategoryCode())));
			// 教材有错题时才返回,需求调整后需要优化
			List<Integer> tbCodes = new ArrayList<Integer>(textbooks.size());
			for (VTextbook v : textbooks) {
				tbCodes.add(v.getCode());
			}
			Map<Integer, Boolean> cacheMap = tfqService.statisTextbookExistFallibleWithCache(tbCodes,
					Security.getUserId());
			List<VTextbook> tbs = new ArrayList<VTextbook>(textbooks.size());
			for (VTextbook v : textbooks) {
				if (cacheMap.get(v.getCode())) {
					tbs.add(v);
				}
			}
			data.put("textbooks", tbs);

			for (VTextbook v : tbs) {
				if (v.getCode() == teacher.getTextbookCode()) {
					textbookCode = v.getCode();
					break;
				}
			}
			if (textbookCode == null && !tbs.isEmpty()) {
				textbookCode = tbs.get(0).getCode();
			}
		}
		if (textbookCode == null) {
			data.put("sections", Collections.EMPTY_LIST);
			data.put("total", 0);
		} else {
			List<VSection> vsections = sectionConvert.to(sectionService.findByTextbookCode(textbookCode));

			// 该阶段 排除 解答题（证明 计算 解答）
			List<QuestionType> qtList = questionTypeService.findBySubject(teacher.getSubjectCode());
			List<Integer> qtCodes = new ArrayList<Integer>();
			for (QuestionType questionType : qtList) {
				if (questionType.getName().equals("证明题") || questionType.getName().equals("解答题")
						|| questionType.getName().equals("计算题")) {
					qtCodes.add(questionType.getCode());
				}
			}
			Map<Long, Integer> countMap = tfqService.staticQuestionFallibleCount(Security.getUserId(), textbookCode,
					teacher.getSubjectCode(), qtCodes);
			long total = 0;
			for (VSection v : vsections) {
				if (countMap.containsKey(v.getCode())) {
					total += countMap.get(v.getCode());
					v.setFallibleCount(countMap.get(v.getCode()).longValue());
				}
			}
			// 重新组装为树形结构
			data.put("sections", sectionConvert.assemblySectionTree(vsections));
			data.put("total", total);
		}
		data.put("textbookCode", textbookCode);
		data.put("textbookCategoryCode", teacher.getTextbookCategoryCode());
		return new Value(data);
	}

	/**
	 * 通过当前用户学科获取知识点列表(包含解答题)
	 * 
	 * @since yoomath V1.9.1
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "3/sectionTree", method = { RequestMethod.POST, RequestMethod.GET })
	public Value sectionTree3(@RequestParam(value = "textbookCode", required = false) Integer textbookCode) {
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		Map<String, Object> data = new HashMap<String, Object>(7);
		if (textbookCode == null) {
			data.put("questionType",
					questionTypeConvert.to(questionTypeService.findBySubject(teacher.getSubjectCode())));
			List<TextbookCategory> tbcList = null;
			Integer textbookCategory = null;
			if (teacher.getTextbookCategoryCode() == null) {
				if (teacher.getSubjectCode() == 202) {
					textbookCategory = TextbookCategoryService.SU_KE_BAN;
					tbcList = Lists.newArrayList(tbcService.get(TextbookCategoryService.SU_KE_BAN));
				} else if (teacher.getSubjectCode() == 302) {
					textbookCategory = TextbookCategoryService.SU_JIAO_BAN;
					tbcList = Lists.newArrayList(tbcService.get(TextbookCategoryService.SU_JIAO_BAN));
				} else {
					tbcList = tbcService.getAll();
				}
			} else {
				textbookCategory = teacher.getTextbookCategoryCode();
				tbcList = Lists.newArrayList(tbcService.get(textbookCategory));
			}
			List<VTextbookCategory> categories = tbcConvert.to(tbcList);
			data.put("textbookCategories", categories);
			List<VTextbook> textbooks = tbConvert.to(tbService.find(Product.YOOMATH, teacher.getPhaseCode(),
					teacher.getSubjectCode(), Lists.newArrayList(teacher.getTextbookCategoryCode())));

			// 教材有错题时才返回,需求调整后需要优化
			List<VTextbook> teaFallibleTextbooks = new ArrayList<VTextbook>(textbooks.size());
			if (teacher.getTextbookCategoryCode() != null) {
				Map<Integer, Integer> fallCountMap = tfqService.statisTextbookFallible(teacher.getId(),
						teacher.getTextbookCategoryCode());
				if (fallCountMap.size() > 0) {
					for (VTextbook v : textbooks) {
						if (fallCountMap.containsKey(v.getCode())) {
							teaFallibleTextbooks.add(v);
						}
					}
				} else {
					teaFallibleTextbooks = textbooks;
				}
			} else {
				teaFallibleTextbooks = textbooks;
			}
			data.put("textbooks", teaFallibleTextbooks);

			// for (VTextbook v : teaFallibleTextbooks) {
			// if (v.getCode() == teacher.getTextbookCode()) {
			// textbookCode = v.getCode();
			// break;
			// }
			// }
			// if (textbookCode == null && !teaFallibleTextbooks.isEmpty()) {
			// textbookCode = teaFallibleTextbooks.get(0).getCode();
			// }

			if (textbookCode == null) {
				if (teaFallibleTextbooks.size() > 0 && teacher.getTextbookCategoryCode() != null) {
					int latestTextbook = teacherFallibleCacheService.getTextbookCurrent(Security.getUserId(),
							teacher.getTextbookCategoryCode());
					boolean useCache = false;
					for (VTextbook v : teaFallibleTextbooks) {
						if (v.getCode() == latestTextbook) {
							useCache = true;
							break;
						}
					}
					if (useCache) {
						textbookCode = latestTextbook;
					} else {
						textbookCode = teaFallibleTextbooks.get(0).getCode();
					}
				} else {
					textbookCode = textbooks.get(0).getCode();
				}
			}
		}

		List<VSection> vsections = sectionConvert.to(sectionService.findByTextbookCode(textbookCode));

		Map<Long, Integer> countMap = tfqService.staticQuestionFallibleCount2(Security.getUserId(), textbookCode,
				teacher.getSubjectCode());
		long total = 0;
		for (VSection v : vsections) {
			if (countMap.containsKey(v.getCode())) {
				total += countMap.get(v.getCode());
				v.setFallibleCount(countMap.get(v.getCode()).longValue());
			}
		}
		// 重新组装为树形结构
		data.put("sections", sectionConvert.assemblySectionTreeFilterNoFall(vsections));
		data.put("total", total);
		data.put("textbookCode", textbookCode);
		data.put("textbookCategoryCode", teacher.getTextbookCategoryCode());
		return new Value(data);
	}

	/**
	 * 通过当前用户学科获取知识点列表（带层次 ） 【知识点部分】
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "2/knowPointTree", method = { RequestMethod.POST, RequestMethod.GET })
	public Value knowPointTree2() {
		Map<String, Object> data = new HashMap<String, Object>(3);
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		// 该阶段 排除 解答题（证明 计算 解答）
		List<QuestionType> qtList = questionTypeService.findBySubject(teacher.getSubjectCode());
		List<Integer> qtCodes = new ArrayList<Integer>();
		for (QuestionType questionType : qtList) {
			if (questionType.getName().equals("证明题") || questionType.getName().equals("解答题")
					|| questionType.getName().equals("计算题")) {
				qtCodes.add(questionType.getCode());
			}
		}
		List<Knowpoint> kpList = knowPointService.listAllBySubject(teacher.getSubjectCode());
		List<VLevelKnowpoint> vlkList = zlkConvert.to(knowPointConvert.to(kpList));
		Map<Integer, Integer> countMaps = tfqService.getKnowpointFailCount(Security.getUserId(),
				teacher.getSubjectCode(), qtCodes);
		for (VLevelKnowpoint v : vlkList) {
			if (countMaps.containsKey(v.getCode().intValue())) {
				v.setFallibleCount(countMaps.get(v.getCode().intValue()).longValue());
			}
		}
		data.put("knowpoints", zlkConvert.assemblySectionTree(vlkList));
		return new Value(data);
	}

	/**
	 * 通过当前用户学科获取知识点列表(包含解答题)
	 * 
	 * @since yoomath V1.9.1
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "3/knowPointTree", method = { RequestMethod.POST, RequestMethod.GET })
	public Value knowPointTree3() {
		Map<String, Object> data = new HashMap<String, Object>(3);
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		List<Knowpoint> kpList = knowPointService.listAllBySubject(teacher.getSubjectCode());
		List<VLevelKnowpoint> vlkList = zlkConvert.to(knowPointConvert.to(kpList));
		Map<Integer, Integer> countMaps = tfqService.getKnowpointFailCount2(Security.getUserId(),
				teacher.getSubjectCode());
		for (VLevelKnowpoint v : vlkList) {
			if (countMaps.containsKey(v.getCode().intValue())) {
				v.setFallibleCount(countMaps.get(v.getCode().intValue()).longValue());
			}
		}
		data.put("knowpoints", zlkConvert.assemblySectionTree(vlkList));
		return new Value(data);
	}

	/**
	 * 新知识点树
	 * 
	 * @author wangsenhao
	 * @version 2016年11月23日 上午10:39:19
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
		Map<Integer, Integer> countMaps = tfqService.getNewKnowpointFailCount(Security.getUserId(),
				teacher.getSubjectCode());
		for (VKnowledgeTreeNode n : nodes) {
			n.setFallibleCount(countMaps.get(n.getCode().intValue()) == null ? 0L
					: countMaps.get(n.getCode().intValue()).longValue());
		}
		List<VKnowledgeTreeNode> newList = knowledgeSystemTreeNodeConvert.to(systems);
		newList.addAll(nodes);
		retMap.put("tree", knowledgeSystemTreeNodeConvert.assemblyPointTreeFilterNoFall(newList));
		return new Value(retMap);
	}

	/**
	 * 查询错题列表
	 * 
	 * @since 2.1
	 * @param form
	 *            查询条件
	 * @return {@link Value}
	 */
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "2/query", method = { RequestMethod.POST, RequestMethod.GET })
	public Value query2(QuestionQueryForm form) {
		ZyTeacherFallibleQuestionQuery query = new ZyTeacherFallibleQuestionQuery();
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		// 知识点CODES
		query.setTeacherId(Security.getUserId());
		List<Long> typeList = null;
		if (CollectionUtils.isNotEmpty(form.getTypeCodes())) {
			typeList = new ArrayList<Long>(form.getTypeCodes().size());
			typeList.addAll(form.getTypeCodes());
		}
		query.setTypeCodes(typeList);
		query.setLeRightRate(form.getLeRightRate());
		query.setReRightRate(form.getReRightRate());
		query.setLeDifficulty(form.getLeDifficulty());
		query.setReDifficulty(form.getReDifficulty());
		query.setTextbookCode(form.getTextbookCode());
		query.setIsRightRateDesc(form.getIsRightRateDesc());
		query.setIsCreateAtDesc(form.getIsCreateAtDesc());
		query.setTimeRange(form.getTimeRange());
		query.setMetaKnowpointCodes(form.getMetaknowCodes());
		query.setLeftOpen(form.isLeftOpen());
		query.setRightOpen(form.isRightOpen());
		query.setRateleftOpen(form.isRateleftOpen());
		query.setSearchInSection(form.isSearchInSection());
		query.setRaterightOpen(form.isRaterightOpen());
		if (CollectionUtils.isNotEmpty(form.getSectionCodes())) {
			Set<Long> sectionCodes = Sets.newHashSet(form.getSectionCodes());
			query.setSectionCodes(sectionCodes);
		}
		Page<TeacherFallibleQuestion> page = tfqService.queryFaliableQuestion(teacher.getSubjectCode(), query,
				P.index(form.getPage(), form.getPageSize()));
		VPage<VTeacherFallibleQuestion> vpage = new VPage<VTeacherFallibleQuestion>();
		vpage.setPageSize(form.getPageSize());
		if (page.isEmpty()) {
			vpage.setItems(Collections.EMPTY_LIST);
		} else {
			vpage.setItems(tfqConvert.to(page.getItems()));
		}
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		vpage.setCurrentPage(form.getPage());
		return new Value(vpage);
	}

	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = { "2/query2", "3/query" }, method = { RequestMethod.POST, RequestMethod.GET })
	public Value query3(QuestionQueryForm form) {
		ZyTeacherFallibleQuestionQuery query = new ZyTeacherFallibleQuestionQuery();
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		// 知识点CODES
		query.setTeacherId(Security.getUserId());
		List<Long> typeList = null;
		if (CollectionUtils.isNotEmpty(form.getTypeCodes())) {
			typeList = new ArrayList<Long>(form.getTypeCodes().size());
			typeList.addAll(form.getTypeCodes());
		}
		query.setTypeCodes(typeList);
		query.setLeRightRate(form.getLeRightRate());
		query.setReRightRate(form.getReRightRate());
		query.setLeDifficulty(form.getLeDifficulty());
		query.setReDifficulty(form.getReDifficulty());
		query.setTextbookCode(form.getTextbookCode());
		query.setIsRightRateDesc(form.getIsRightRateDesc());
		query.setIsCreateAtDesc(form.getIsCreateAtDesc());
		query.setTimeRange(form.getTimeRange());
		query.setMetaKnowpointCodes(form.getMetaknowCodes());
		query.setLeftOpen(form.isLeftOpen());
		query.setRightOpen(form.isRightOpen());
		query.setRateleftOpen(form.isRateleftOpen());
		query.setSearchInSection(form.isSearchInSection());
		query.setRaterightOpen(form.isRaterightOpen());
		query.setKey(form.getKey());
		if (CollectionUtils.isNotEmpty(form.getSectionCodes())) {
			Set<Long> sectionCodes = Sets.newHashSet(form.getSectionCodes());
			query.setSectionCodes(sectionCodes);
		}

		Page<TeacherFallibleQuestion> page = tfqService.queryFaliableQuestion2(teacher.getSubjectCode(), query,
				form.getPage(), form.getPageSize());
		VPage<VTeacherFallibleQuestion> vpage = new VPage<VTeacherFallibleQuestion>();
		vpage.setPageSize(form.getPageSize());
		if (page.isEmpty()) {
			vpage.setItems(Collections.EMPTY_LIST);
		} else {
			vpage.setItems(tfqConvert.to(page.getItems()));

		}
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		vpage.setCurrentPage(form.getPage());
		return new Value(vpage);
	}

	/**
	 * 查询错题列表(包含解答题)
	 * 
	 * @since yoomath V1.9.1
	 * @param form
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = { "2/query3", "4/query" }, method = { RequestMethod.POST, RequestMethod.GET })
	public Value query4(QuestionQueryForm form) {
		ZyTeacherFallibleQuestionQuery query = new ZyTeacherFallibleQuestionQuery();
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		// 知识点CODES
		query.setTeacherId(Security.getUserId());
		List<Long> typeList = null;
		if (CollectionUtils.isNotEmpty(form.getTypeCodes())) {
			typeList = new ArrayList<Long>(form.getTypeCodes().size());
			typeList.addAll(form.getTypeCodes());
		}
		query.setTypeCodes(typeList);
		query.setLeRightRate(form.getLeRightRate());
		query.setReRightRate(form.getReRightRate());
		query.setLeDifficulty(form.getLeDifficulty());
		query.setReDifficulty(form.getReDifficulty());
		query.setTextbookCode(form.getTextbookCode());
		query.setIsRightRateDesc(form.getIsRightRateDesc());
		query.setIsCreateAtDesc(form.getIsCreateAtDesc());
		query.setTimeRange(form.getTimeRange());
		query.setMetaKnowpointCodes(form.getMetaknowCodes());
		query.setLeftOpen(form.isLeftOpen());
		query.setRightOpen(form.isRightOpen());
		query.setRateleftOpen(form.isRateleftOpen());
		query.setSearchInSection(form.isSearchInSection());
		query.setRaterightOpen(form.isRaterightOpen());
		query.setKey(form.getKey());
		query.setNewKnowpointCodes(form.getNewKnowpointCodes());
		query.setNewKeyQuery(form.getNewKeyQuery());
		if (CollectionUtils.isNotEmpty(form.getSectionCodes())) {
			Set<Long> sectionCodes = Sets.newHashSet(form.getSectionCodes());
			query.setSectionCodes(sectionCodes);
		}

		Page<TeacherFallibleQuestion> page = tfqService.queryFaliableQuestion3(teacher.getSubjectCode(), query,
				form.getPage(), form.getPageSize());
		VPage<VTeacherFallibleQuestion> vpage = new VPage<VTeacherFallibleQuestion>();
		vpage.setPageSize(form.getPageSize());
		if (page.isEmpty()) {
			vpage.setItems(Collections.EMPTY_LIST);
		} else {
			vpage.setItems(tfqConvert.to(page.getItems()));

		}
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		vpage.setCurrentPage(form.getPage());
		return new Value(vpage);
	}
}

package com.lanking.uxb.service.web.resource;

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

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.domain.common.baseData.Knowpoint;
import com.lanking.cloud.domain.common.baseData.QuestionType;
import com.lanking.cloud.domain.common.baseData.TextbookCategory;
import com.lanking.cloud.domain.yoo.collection.QuestionCollection;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
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
import com.lanking.uxb.service.zuoye.api.ZyQuestionCollectionService;
import com.lanking.uxb.service.zuoye.convert.ZyLevelKnowpointConvert;
import com.lanking.uxb.service.zuoye.convert.ZyQuestionCollectionConvert;
import com.lanking.uxb.service.zuoye.form.QuestionQueryForm;
import com.lanking.uxb.service.zuoye.value.VLevelKnowpoint;
import com.lanking.uxb.service.zuoye.value.VQuestionCollection;

/**
 * 收藏题目相关接口
 * 
 * @since yoomath V1.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年9月6日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/t/qcollect")
public class ZyTeaQuestionCollectionController {

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
	private ZyQuestionCollectionService zyQuestionCollectionService;
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
	private ZyQuestionCollectionConvert zyQuestionCollectionConvert;
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
	 * 查询我的收藏
	 * 
	 * @param form
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "query", method = { RequestMethod.POST, RequestMethod.GET })
	public Value query(QuestionQueryForm form) {
		form.setUserId(Security.getUserId());
		Page<QuestionCollection> cp = zyQuestionCollectionService.queryCollection(form,
				P.index(form.getPage(), form.getPageSize()));
		VPage<VQuestionCollection> vp = new VPage<VQuestionCollection>();
		int tPage = (int) (cp.getTotalCount() + form.getPageSize() - 1) / form.getPageSize();
		vp.setPageSize(form.getPageSize());
		vp.setCurrentPage(form.getPage());
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		vp.setItems(zyQuestionCollectionConvert.to(cp.getItems()));
		return new Value(vp);
	}

	/**
	 * 查询我的收藏
	 * 
	 * @param form
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "2/query", method = { RequestMethod.POST, RequestMethod.GET })
	public Value query2(QuestionQueryForm form) {
		form.setUserId(Security.getUserId());
		Page<QuestionCollection> cp = zyQuestionCollectionService.queryCollectionByIndex(form,
				P.index(form.getPage(), form.getPageSize()));
		VPage<VQuestionCollection> vp = new VPage<VQuestionCollection>();
		int tPage = (int) (cp.getTotalCount() + form.getPageSize() - 1) / form.getPageSize();
		vp.setPageSize(form.getPageSize());
		vp.setCurrentPage(form.getPage());
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		vp.setItems(zyQuestionCollectionConvert.to(cp.getItems()));
		return new Value(vp);
	}

	/**
	 * 查询我的收藏(包含解答题)
	 * 
	 * @since yoomath V1.9.1
	 * @param form
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "3/query", method = { RequestMethod.POST, RequestMethod.GET })
	public Value query3(QuestionQueryForm form) {
		form.setUserId(Security.getUserId());
		Page<QuestionCollection> cp = zyQuestionCollectionService.queryCollectionByIndex2(form,
				P.index(form.getPage(), form.getPageSize()));
		VPage<VQuestionCollection> vp = new VPage<VQuestionCollection>();
		int tPage = (int) (cp.getTotalCount() + form.getPageSize() - 1) / form.getPageSize();
		vp.setPageSize(form.getPageSize());
		vp.setCurrentPage(form.getPage());
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		vp.setItems(zyQuestionCollectionConvert.to(cp.getItems()));
		return new Value(vp);
	}

	/**
	 * 收藏题目
	 * 
	 * @param questionId
	 * @return
	 */
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "collect", method = { RequestMethod.POST, RequestMethod.GET })
	public Value collect(@RequestParam(value = "questionId") Long questionId) {
		zyQuestionCollectionService.collect(questionId, Security.getUserId());
		return new Value();
	}

	/**
	 * 取消收藏
	 * 
	 * @param collectId
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "cancelCollect", method = { RequestMethod.POST, RequestMethod.GET })
	public Value cancelCollect(@RequestParam(value = "collectId") Long collectId) {
		zyQuestionCollectionService.cancel(collectId, Security.getUserId());
		return new Value();
	}

	/**
	 * 取消收藏
	 * 
	 * @since v2.3.1
	 * @param questionId
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "cancelCollectByQuestionId", method = { RequestMethod.POST, RequestMethod.GET })
	public Value cancelCollectByQuestionId(@RequestParam(value = "questionId") Long questionId) {
		zyQuestionCollectionService.cancelCollect(questionId, Security.getUserId());
		return new Value();
	}

	/**
	 * 我的收藏章节树
	 * 
	 * @param textbookCode
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "index", method = { RequestMethod.POST, RequestMethod.GET })
	public Value index(@RequestParam(value = "textbookCode", required = false) Integer textbookCode) {
		Map<String, Object> data = new HashMap<String, Object>();
		Teacher teacher = (Teacher) teacherService.getUser(Security.getUserId());
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
			// 现在注册是版本改为必选
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

			List<VTextbookCategory> categories = tbcConvert.to(tbcList);
			data.put("textbookCategories", categories);

			List<VTextbook> textbooks = tbConvert
					.to(tbService.find(teacher.getPhaseCode(), textbookCategory, teacher.getSubjectCode()));
			// 教材有收藏时才返回,需求调整后需要优化
			List<Integer> tbCodes = new ArrayList<Integer>(textbooks.size());
			for (VTextbook v : textbooks) {
				tbCodes.add(v.getCode());
			}
			Map<Integer, Boolean> cacheMap = zyQuestionCollectionService.statisTextbookExistCollectWithCache(tbCodes,
					Security.getUserId(), qtCodes);
			List<VTextbook> tbs = new ArrayList<VTextbook>(textbooks.size());
			for (VTextbook v : textbooks) {
				if (cacheMap.get(v.getCode())) {
					tbs.add(v);
				}
			}
			data.put("textbooks", tbs);
			data.put("questionType",
					questionTypeConvert.to(questionTypeService.findBySubject(teacher.getSubjectCode())));
			// 1.用户的教材不为空 2.收藏的里面有此教材的
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
			// 没有收藏题目的时候，不用返回
			if (tbs.size() > 0) {
				data.put("textbook", tbService.get(textbookCode));
			}
		}
		if (textbookCode == null) {
			data.put("sections", Collections.EMPTY_LIST);
			data.put("total", 0);
		} else {
			List<VSection> vsections = sectionConvert.to(sectionService.findByTextbookCode(textbookCode));
			Map<Long, Long> countMap = zyQuestionCollectionService.statisSectionCollect(textbookCode,
					Security.getUserId(), qtCodes);
			long total = 0;
			for (VSection v : vsections) {
				if (countMap.containsKey(v.getCode())) {
					total += countMap.get(v.getCode());
					v.setCollectCount(countMap.get(v.getCode()).longValue());
				}
			}
			// 重新组装为树形结构
			data.put("sections", sectionConvert.assemblySectionTree(vsections));
			data.put("total", total);
		}
		return new Value(data);
	}

	/**
	 * 我的收藏章节树(包含解答题)
	 * 
	 * @param textbookCode
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "2/index", method = { RequestMethod.POST, RequestMethod.GET })
	public Value index2(@RequestParam(value = "textbookCode", required = false) Integer textbookCode) {
		Map<String, Object> data = new HashMap<String, Object>();
		Teacher teacher = (Teacher) teacherService.getUser(Security.getUserId());
		if (textbookCode == null) {
			List<TextbookCategory> tbcList = null;
			// 现在注册是版本改为必选
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

			List<VTextbookCategory> categories = tbcConvert.to(tbcList);
			data.put("textbookCategories", categories);

			List<VTextbook> textbooks = tbConvert
					.to(tbService.find(teacher.getPhaseCode(), textbookCategory, teacher.getSubjectCode()));

			// 教材有收藏时才返回,需求调整后需要优化
			// List<Integer> tbCodes = new ArrayList<Integer>(textbooks.size());
			// for (VTextbook v : textbooks) {
			// tbCodes.add(v.getCode());
			// }
			// Map<Integer, Boolean> cacheMap =
			// zyQuestionCollectionService.statisTextbookExistCollectWithCache2(tbCodes,
			// Security.getUserId());
			// List<VTextbook> tbs = new ArrayList<VTextbook>(textbooks.size());
			// for (VTextbook v : textbooks) {
			// if (cacheMap.get(v.getCode())) {
			// tbs.add(v);
			// }
			// }

			// 2017-8-18 暂时保持与mobile一致，这个产品的问题，后面产品层面没有考虑一致，测试层面一律不考虑
			List<VTextbook> tbs = new ArrayList<VTextbook>(textbooks.size());
			if (teacher.getTextbookCategoryCode() != null) {
				Map<Integer, Integer> collectCountMap = zyQuestionCollectionService
						.statisTextbookCollect(teacher.getId(), teacher.getTextbookCategoryCode());
				if (collectCountMap.size() > 0) {
					for (VTextbook v : textbooks) {
						if (collectCountMap.containsKey(v.getCode())) {
							tbs.add(v);
						}
					}
				} else {
					for (VTextbook v : textbooks) {
						tbs.add(v);
					}
				}
			} else {
				for (VTextbook v : textbooks) {
					tbs.add(v);
				}
			}

			data.put("textbooks", tbs);
			data.put("questionType",
					questionTypeConvert.to(questionTypeService.findBySubject(teacher.getSubjectCode())));

			// 1.用户的教材不为空 2.收藏的里面有此教材的
			if (userTextbookCode != null) {
				for (VTextbook v : tbs) {
					if (v.getCategoryCode() == userTextbookCode) {
						textbookCode = v.getCode();
						break;
					}
				}
			}

			// 不传值取最后一条收藏题目的textbookCode
			if (textbookCode == null) {
				Map<String, BigInteger> codeMap = zyQuestionCollectionService.getLastTextbookCode(Security.getUserId(),
						teacher.getTextbookCategoryCode());
				if (codeMap == null) {
					textbookCode = textbooks.get(0).getCode();
				} else {
					textbookCode = codeMap.get("textbook_code").intValue();
				}
			}

			// 没有收藏题目的时候，不用返回
			if (tbs.size() > 0) {
				data.put("textbook", tbService.get(textbookCode));
			}
		}

		List<VSection> vsections = sectionConvert.to(sectionService.findByTextbookCode(textbookCode));
		Map<Long, Long> countMap = zyQuestionCollectionService.statisSectionCollect2(textbookCode,
				Security.getUserId());
		long total = 0;
		for (VSection v : vsections) {
			if (countMap.containsKey(v.getCode())) {
				total += countMap.get(v.getCode());
				v.setCollectCount(countMap.get(v.getCode()).longValue());
			}
		}
		// 重新组装为树形结构
		data.put("sections", sectionConvert.assemblySectionTreeFilterNoCollect(vsections));
		data.put("total", total);
		return new Value(data);
	}

	/**
	 * 我的收藏知识点树
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
		Map<Integer, Integer> countMaps = zyQuestionCollectionService.statisKnowPointCollect(teacher.getSubjectCode(),
				Security.getUserId(), qtCodes);
		for (VLevelKnowpoint v : vlkList) {
			if (countMaps.containsKey(v.getCode().intValue())) {
				v.setCollectCount(countMaps.get(v.getCode().intValue()).longValue());
			}
		}
		data.put("knowpoints", zlkConvert.assemblySectionTree(vlkList));
		return new Value(data);
	}

	/**
	 * 我的收藏知识点树
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
		Map<Integer, Integer> countMaps = zyQuestionCollectionService.statisKnowPointCollect2(teacher.getSubjectCode(),
				Security.getUserId());
		for (VLevelKnowpoint v : vlkList) {
			if (countMaps.containsKey(v.getCode().intValue())) {
				v.setCollectCount(countMaps.get(v.getCode().intValue()).longValue());
			}
		}
		data.put("knowpoints", zlkConvert.assemblySectionTree(vlkList));
		return new Value(data);
	}

	/**
	 * 新知识点
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
		Map<Long, Long> countMaps = zyQuestionCollectionService.statisNewKnowPointCollect(teacher.getSubjectCode(),
				Security.getUserId());
		for (VKnowledgeTreeNode n : nodes) {
			if (countMaps.keySet().contains(n.getCode())) {
				n.setCollectCount(countMaps.get(n.getCode()));
			}
		}
		List<VKnowledgeTreeNode> newList = knowledgeSystemTreeNodeConvert.to(systems);
		newList.addAll(nodes);
		retMap.put("tree", knowledgeSystemTreeNodeConvert.assemblyPointTreeFilterNoCollect(newList));
		return new Value(retMap);
	}
}

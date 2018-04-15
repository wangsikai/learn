package com.lanking.uxb.rescon.question.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.component.searcher.api.SearchService;
import com.lanking.cloud.domain.common.baseData.ExaminationPoint;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.KnowledgeReview;
import com.lanking.cloud.domain.common.baseData.KnowledgeSync;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.domain.common.baseData.Knowpoint;
import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;
import com.lanking.cloud.domain.common.baseData.QuestionCategory;
import com.lanking.cloud.domain.common.baseData.QuestionTag;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.common.resource.book.BookQuestion;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionSource;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.domain.type.AsciiStatus;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.code.core.CoreExceptionCode;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.IllegalArgFormatException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Order;
import com.lanking.cloud.sdk.data.Order.Direction;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.rescon.account.api.ResconVendorUserManage;
import com.lanking.uxb.rescon.account.cache.ResconAccountCacheService;
import com.lanking.uxb.rescon.account.convert.ResconVendorUserConvert;
import com.lanking.uxb.rescon.auth.api.DataPermission;
import com.lanking.uxb.rescon.basedata.api.ResconExaminationPointService;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgePointService;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgeReviewService;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgeSyncService;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgeSystemService;
import com.lanking.uxb.rescon.basedata.convert.ResconExaminationPointConvert;
import com.lanking.uxb.rescon.basedata.convert.ResconKnowledgePointConvert;
import com.lanking.uxb.rescon.basedata.convert.ResconKnowledgeSystemConvert;
import com.lanking.uxb.rescon.book.api.ResconBookQuestionManage;
import com.lanking.uxb.rescon.book.controller.ResconBookQuestionController;
import com.lanking.uxb.rescon.common.ex.ResourceConsoleException;
import com.lanking.uxb.rescon.error.api.ResconErrorManage;
import com.lanking.uxb.rescon.exam.api.ResconExamManage;
import com.lanking.uxb.rescon.exam.api.ResconExamPaperQuestionManage;
import com.lanking.uxb.rescon.question.api.ResconAnswerManage;
import com.lanking.uxb.rescon.question.api.ResconQuestionCategoryManage;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;
import com.lanking.uxb.rescon.question.api.ResconQuestionSceneManage;
import com.lanking.uxb.rescon.question.api.ResconQuestionTagManage;
import com.lanking.uxb.rescon.question.api.ResconQuestionTypeManage;
import com.lanking.uxb.rescon.question.api.ResconSchoolQuestionManage;
import com.lanking.uxb.rescon.question.convert.ResLevelKnowpointConvert;
import com.lanking.uxb.rescon.question.convert.ResconQuestionCategoryConvert;
import com.lanking.uxb.rescon.question.convert.ResconQuestionConvert;
import com.lanking.uxb.rescon.question.convert.ResconQuestionTagConvert;
import com.lanking.uxb.rescon.question.convert.ResconQuestionTypeConvert;
import com.lanking.uxb.rescon.question.form.QuestionForm;
import com.lanking.uxb.rescon.question.form.QuestionQueryForm;
import com.lanking.uxb.rescon.question.form.SelectDatasForm;
import com.lanking.uxb.rescon.question.value.VQuestion;
import com.lanking.uxb.rescon.question.value.VQuestionCheckCount;
import com.lanking.uxb.rescon.question.value.VResLevelKnowpoint;
import com.lanking.uxb.rescon.statistics.api.VendorUserStatisManage;
import com.lanking.uxb.service.code.api.KnowpointService;
import com.lanking.uxb.service.code.api.MetaKnowpointService;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.ResourceCategoryService;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.convert.KnowpointConvert;
import com.lanking.uxb.service.code.convert.PhaseConvert;
import com.lanking.uxb.service.code.convert.SubjectConvert;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.code.value.VPhase;
import com.lanking.uxb.service.counter.api.impl.ResourcePoolProvider;
import com.lanking.uxb.service.counter.api.impl.VendorUserCounterProvider;
import com.lanking.uxb.service.question.api.QuestionQRCodeService;
import com.lanking.uxb.service.question.api.QuestionWordMLService;
import com.lanking.uxb.service.question.cache.BaseQuestionCacheService;
import com.lanking.uxb.service.question.util.QuestionKatexUtils;
import com.lanking.uxb.service.search.api.IndexService;
import com.lanking.uxb.service.search.api.Page;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 习题.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年8月14日
 */
@RestController
@RequestMapping("rescon/que")
@RolesAllowed(userTypes = { "VENDOR_ADMIN", "VENDOR_HEAD", "VENDOR_BUILD", "VENDOR_CHECK" })
public class ResconQuestionController {

	@Autowired
	private ResconVendorUserManage vendorUserManage;
	@Autowired
	private ResconQuestionManage questionManage;
	@Autowired
	private BaseQuestionCacheService questionCacheService;
	@Autowired
	private ResourcePoolProvider resourcePoolProvider;
	@Autowired
	private IndexService indexService;
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private PhaseConvert phaseConvert;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private SubjectConvert subjectConvert;
	@Autowired
	private TextbookCategoryService textbookCategoryService;
	@Autowired
	private TextbookCategoryConvert textbookCategoryConvert;
	@Autowired
	private ResconQuestionTypeManage questionTypeManage;
	@Autowired
	private ResconQuestionTypeConvert questionTypeConvert;
	@Autowired
	private KnowpointService knowpointService;
	@Autowired
	private MetaKnowpointService metaKnowpointService;
	@Autowired
	private SearchService searchService;
	@Autowired
	private ResconQuestionConvert questionConvert;
	@Autowired
	private ResconVendorUserConvert vendorUserConvert;
	@Autowired
	private VendorUserStatisManage vendorUserStatisManage;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private ResconAccountCacheService accountCacheService;
	@Autowired
	private ResLevelKnowpointConvert resLevelKnowpointConvert;
	@Autowired
	private KnowpointConvert knowPointConvert;
	@Autowired
	private ResconErrorManage resconErrorManage;
	@Autowired
	private ResourceCategoryService resourceCategoryService;
	@Autowired
	private ResconSchoolQuestionManage resconSchoolQuestionManage;
	@Autowired
	private ResconKnowledgeSystemService knowledgeSystemService;
	@Autowired
	private ResconKnowledgePointService knowledgePointService;
	@Autowired
	private ResconExaminationPointService examinationPointService;
	@Autowired
	private ResconKnowledgePointConvert knowledgePointConvert;
	@Autowired
	private ResconExaminationPointConvert examinationPointConvert;
	@Autowired
	private ResconKnowledgeSystemConvert knowledgeSystemConvert;
	@Autowired
	private VendorUserCounterProvider vendorUserCounterProvider;
	@Autowired
	private ResconQuestionSceneManage questionSceneManage;
	@Autowired
	private ResconBookQuestionManage bookQuestionManage;
	@Autowired
	private ResconBookQuestionController bookQuestionController;
	@Autowired
	private ResconExamPaperQuestionManage examPaperQuestionManage;
	@Autowired
	private QuestionWordMLService questionWordMLService;
	@Autowired
	private ResconAnswerManage answerManage;
	@Autowired
	private QuestionQRCodeService questionQRCodeService;
	@Autowired
	private ResconExamManage resconExamManage;
	@Autowired
	private ResconQuestionTagManage questionTagManage;
	@Autowired
	private ResconQuestionTagConvert questionTagConvert;
	@Autowired
	private ResconQuestionCategoryManage questionCategoryManage;
	@Autowired
	private ResconQuestionCategoryConvert questionCategoryConvert;
	@Autowired
	private ResconKnowledgeSyncService knowledgeSyncService;
	@Autowired
	private ResconKnowledgeReviewService knowledgeReviewService;

	/**
	 * 获得基本的选择项数据.
	 * 
	 * @param SelectDatasForm
	 * @return
	 */
	@RequestMapping(value = "getBaseSelectDatas")
	public Value getBaseSelectDatas(SelectDatasForm form) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (form.getPhaseEnable()) {
			map.put("phases", phaseConvert.to(phaseService.getAll()));
		}
		if (form.getStatusEnable()) {
			List<Map<String, Object>> status = new ArrayList<Map<String, Object>>();
			for (CheckStatus checkStatus : CheckStatus.values()) {
				Map<String, Object> data = new HashMap<String, Object>(2);
				if (checkStatus != CheckStatus.CHECKING) {
					data.put("code", checkStatus);
					data.put("name", checkStatus.getName());
					status.add(data);
				}
			}
			map.put("status", status);
		}
		if (form.getSubjectEnable()) {
			map.put("subjects", subjectConvert.to(subjectService.getAll()));
		}
		if (form.getPhaseSubjectEnable()) {
			List<VPhase> vs = phaseConvert.to(phaseService.getAll());
			for (VPhase v : vs) {
				v.setSubjects(subjectConvert.to(subjectService.findByPhaseCode(v.getCode())));
			}
			map.put("phaseSubjects", vs);
		}
		if (form.getTypeEnable()) {
			List<Map<String, Object>> types = new ArrayList<Map<String, Object>>();
			for (Question.Type type : Question.Type.values()) {
				if (type != Question.Type.NULL) {
					Map<String, Object> data = new HashMap<String, Object>(2);
					data.put("code", type);
					data.put("name", type.getName());
					types.add(data);
				}
			}
			map.put("types", types);
		}
		if (form.getCategoryEnable()) {
			map.put("categorys", textbookCategoryConvert.to(textbookCategoryService.getAll()));
		}
		if (form.getVendorEnable()) {
			VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
			map.put("vendors", vendorUserManage.listVendorUsers(user.getVendorId(),
					Lists.newArrayList(UserType.VENDOR_BUILD, UserType.VENDOR_HEAD, UserType.VENDOR_ADMIN)));
		}
		if (form.getResourceCategoryEnable()) {
			map.put("resourceCategorys", resourceCategoryService.findCategoryByParent(5));
		}
		if (form.getQuestionCategoryEnable()) {
			List<QuestionCategory> questionCategorys = questionCategoryManage.listAll(Status.ENABLED);
			map.put("questionCategorys", questionCategoryConvert.to(questionCategorys));
		}
		if (form.getQuestionTagEnable()) {
			List<QuestionTag> questionTags = questionTagManage.listAll(Status.ENABLED, null);
			map.put("questionTags", questionTagConvert.to(questionTags));
		}

		return new Value(map);
	}

	/**
	 * 获取录入人/校验员信息
	 * 
	 * @param userType
	 * @return
	 */
	@RequestMapping(value = "getBuildList")
	public Value getBuildList() {
		Long vendorId = vendorUserManage.getVendorUser(Security.getUserId()).getVendorId();
		List<VendorUser> list = vendorUserManage.getVendorUserListByUserType(vendorId, null);
		return new Value(vendorUserConvert.to(list));
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "questionCheckCount")
	public Value questionCheckCount() {
		Long vendorId = vendorUserManage.getVendorUser(Security.getUserId()).getVendorId();
		List<Map> list = questionManage.getCountByCheckStaus(vendorId);
		VQuestionCheckCount vq = new VQuestionCheckCount();
		for (Map pa : list) {
			int status = Integer.parseInt(String.valueOf(pa.get("status")));
			if (status == CheckStatus.EDITING.getValue()) {
				vq.setEditingNum(Long.parseLong(String.valueOf(pa.get("count"))));
			}
			if (status == CheckStatus.NOPASS.getValue()) {
				vq.setNoPassNum(Long.parseLong(String.valueOf(pa.get("count"))));
			}
			if (status == CheckStatus.PASS.getValue()) {
				vq.setPassNum(Long.parseLong(String.valueOf(pa.get("count"))));
			}
			if (status == CheckStatus.ONEPASS.getValue()) {
				vq.setOnePassNum(Long.parseLong(String.valueOf(pa.get("count"))));
			}
			if (status == CheckStatus.DRAFT.getValue()) {
				vq.setDraftNum(Long.parseLong(String.valueOf(pa.get("count"))));
			}
		}
		vq.setTotal(vq.getEditingNum() + vq.getNoPassNum() + vq.getPassNum() + vq.getOnePassNum() + vq.getDraftNum());
		return new Value(vq);
	}

	/**
	 * 基础题库知识点条件
	 * 
	 * @return
	 */
	@RequestMapping(value = "knowPointTree", method = { RequestMethod.POST, RequestMethod.GET })
	public Value knowPointTree(Integer subjectCode) {
		List<Knowpoint> kpList = knowpointService.listAllBySubject(subjectCode);
		List<VResLevelKnowpoint> vlkList = resLevelKnowpointConvert.to(knowPointConvert.to(kpList));
		return new Value(resLevelKnowpointConvert.assemblySectionTree(vlkList));
	}

	/**
	 * 基础题库新知识点习题查询.
	 * 
	 * @return
	 */
	@RequestMapping(value = "knowledgeSystems", method = { RequestMethod.POST, RequestMethod.GET })
	public Value knowledgeSystems(Integer phaseCode, Integer subjectCode, Long pcode, Integer level) {
		List<KnowledgeSystem> list = knowledgeSystemService.findAll(phaseCode, subjectCode, pcode, level);
		return new Value(knowledgeSystemConvert.to(list));
	}

	/**
	 * 基础题库新知识点查询
	 * 
	 * @return
	 */
	@RequestMapping(value = "knowledgePoints", method = { RequestMethod.POST, RequestMethod.GET })
	public Value knowledgePoints(Integer phaseCode, Integer subjectCode, Long pcode) {
		List<KnowledgePoint> list = knowledgePointService.findPoint(phaseCode, subjectCode, pcode, Status.ENABLED);
		return new Value(knowledgePointConvert.to(list));
	}

	/**
	 * 基础题库新知识点查询
	 * 
	 * @return
	 */
	@RequestMapping(value = "examinationPoints", method = { RequestMethod.POST, RequestMethod.GET })
	public Value examinationPoints(Integer phaseCode, Integer subjectCode, Long pcode) {
		List<ExaminationPoint> list = examinationPointService.findPoint(phaseCode, subjectCode, pcode, Status.ENABLED);
		return new Value(examinationPointConvert.to(list));
	}

	/**
	 * 查询学科题型.
	 * 
	 * @param phaseCode
	 * @return
	 */
	@RequestMapping(value = "queryQuestionType")
	public Value queryQuestionType(Integer subjectCode, Question.Type type) {
		return new Value(questionTypeConvert.to(questionTypeManage.findBySubject(subjectCode, type)));
	}

	/**
	 * 获得添加题目使用的知识点列表.
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "getQueSelKnows", method = RequestMethod.POST)
	public Value getQueSelKnows(Integer subjectCode) {
		Map<String, Object> returnMap = new HashMap<String, Object>(2);
		List<Map> list = new ArrayList<Map>();
		List<Knowpoint> knows = knowpointService.listBySubject(subjectCode);
		Map<Integer, Map> tempK = new HashMap<Integer, Map>(knows.size());
		for (int i = 0; i < knows.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			Knowpoint knowpoint = knows.get(i);
			map.put("id", knowpoint.getCode());
			map.put("pId", knowpoint.getPcode());
			map.put("name", knowpoint.getName());
			map.put("nocheck", true); // 只有原知识点才能选择
			// map.put("isParent", true);
			list.add(map);
			tempK.put(knowpoint.getCode(), map);
		}

		List<Map> metaKnows = metaKnowpointService.listBySubject2(subjectCode);
		for (int i = 0; i < metaKnows.size(); i++) {
			Map temp = metaKnows.get(i);
			Integer pid = temp.get("pid") == null ? null : Integer.parseInt(temp.get("pid").toString());
			temp.put("pId", pid);
			if (pid != null && tempK.get(pid.intValue()) != null) {
				tempK.get(pid.intValue()).put("nocheck", true);
			}
		}
		for (int i = 0; i < knows.size(); i++) {
			Knowpoint knowpoint = knows.get(i);
			if (knowpoint.getPcode() != null) {
				if (tempK.get(knowpoint.getPcode().intValue()) != null) {
					tempK.get(knowpoint.getPcode().intValue()).put("nocheck", true);
				}
			}
		}
		list.addAll(metaKnows);
		returnMap.put("tree", list); // 树
		returnMap.put("metaKnowpoints", metaKnows); // 元知识点
		return new Value(returnMap);
	}

	/**
	 * 根据科目查询元知识点.
	 * 
	 * @param subjectCode
	 * @return
	 */
	@RequestMapping(value = "listMetaKnowpoint", method = RequestMethod.POST)
	public Value listMetaKnowpoint(@RequestParam(value = "subjectCode") int subjectCode) {
		List<MetaKnowpoint> metaKnowpoints = metaKnowpointService.listBySubject(subjectCode, "");
		List<Map<String, Object>> list = Lists.newArrayList();
		for (MetaKnowpoint metaKnowpoint : metaKnowpoints) {
			Map<String, Object> map = new HashMap<String, Object>(2);
			map.put("code", metaKnowpoint.getCode());
			map.put("name", metaKnowpoint.getName());
			list.add(map);
		}
		return new Value(list);
	}

	/**
	 * 创建题目.
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public Value create(String json, HttpServletRequest request, HttpServletResponse response) {
		QuestionForm questionForm = JSON.parseObject(json, QuestionForm.class);

		if (questionForm.getTypeCode() == null || questionForm.getType() == null
				|| questionForm.getSubjectCode() == null || questionForm.getPhaseCode() == null
				|| StringUtils.isBlank(questionForm.getContent()) || questionForm.getAnswers() == null
				|| questionForm.getCodePrefix() == null) {
			return new Value(new MissingArgumentException());
		}
		if (StringUtils.isNotBlank(questionForm.getAnalysis()) && questionForm.getAnalysis().length() > 65000) {
			return new Value(new ResourceConsoleException(ResourceConsoleException.ANALYSIS_OUT_OF_LENGTH));
		}
		if (StringUtils.isNotBlank(questionForm.getHint()) && questionForm.getHint().length() > 65000) {
			return new Value(new ResourceConsoleException(ResourceConsoleException.HINT_OUT_OF_LENGTH));
		}
		if (questionForm.getType() == Question.Type.SINGLE_CHOICE
				|| questionForm.getType() == Question.Type.MULTIPLE_CHOICE) {
			for (String choice : questionForm.getChoices()) {
				if (StringUtils.isNotBlank(choice) && choice.length() > 4000) {
					return new Value(new ResourceConsoleException(ResourceConsoleException.CHOICE_OUT_OF_LENGTH));
				}
			}
		} else if (questionForm.getType() == Question.Type.FILL_BLANK) {
			if (questionForm.getLatexAnswers() == null
					|| questionForm.getLatexAnswers().size() != questionForm.getAnswers().size()) {
				return new Value(new ResourceConsoleException(ResourceConsoleException.ASCII_LATEX_ERROR_LENGTH));
			}
		}

		// 需要对题目是否复合Katex解析规范做判断处理
		boolean contentIsLatexSpecs = QuestionKatexUtils.isLatexSpecs(questionForm.getContent());
		boolean analysisIsLatexSpecs = QuestionKatexUtils.isLatexSpecs(questionForm.getAnalysis());
		boolean hintIsLatexSpecs = QuestionKatexUtils.isLatexSpecs(questionForm.getHint());
		boolean choiceIsLatexSpecs = true;
		if (CollectionUtils.isNotEmpty(questionForm.getChoices())) {
			StringBuffer sb = new StringBuffer();
			for (String choice : questionForm.getChoices()) {
				sb.append(StringUtils.defaultIfBlank(choice));
			}
			choiceIsLatexSpecs = QuestionKatexUtils.isLatexSpecs(sb.toString());
		}
		boolean answerIsLatexSpecs = true;
		if (questionForm.getType() != Question.Type.SINGLE_CHOICE
				&& questionForm.getType() != Question.Type.MULTIPLE_CHOICE) {
			if (CollectionUtils.isNotEmpty(questionForm.getLatexAnswers())) {
				for (String answer : questionForm.getLatexAnswers()) {
					answerIsLatexSpecs = QuestionKatexUtils.isLatexSpecs(StringUtils.defaultIfBlank(answer));
					if (!answerIsLatexSpecs) {
						break;
					}
				}
			}
			if (answerIsLatexSpecs && CollectionUtils.isNotEmpty(questionForm.getAnswers())) {
				for (String answer : questionForm.getAnswers()) {
					answerIsLatexSpecs = QuestionKatexUtils.isLatexSpecs(StringUtils.defaultIfBlank(answer));
					if (!answerIsLatexSpecs) {
						break;
					}
				}
			}
		}
		if (!(contentIsLatexSpecs & analysisIsLatexSpecs & hintIsLatexSpecs & choiceIsLatexSpecs
				& answerIsLatexSpecs)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("contentIsLatexSpecs", contentIsLatexSpecs);
			map.put("analysisIsLatexSpecs", analysisIsLatexSpecs);
			map.put("hintIsLatexSpecs", hintIsLatexSpecs);
			map.put("choiceIsLatexSpecs", choiceIsLatexSpecs);
			map.put("answerIsLatexSpecs", answerIsLatexSpecs);
			return new Value(map);
		}

		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
		questionForm.setVendorId(user.getVendorId());
		try {
			Map<String, Object> questionMap = questionManage.saveQuestion(questionForm, user);
			Question question = (Question) questionMap.get("question");
			CheckStatus oldStatus = (CheckStatus) questionMap.get("oldStatus");

			if (questionForm.getId() == null) {
				// 题目编号处理
				this.createCode(question.getId(), questionForm.getCodePrefix());

				// 统计数据
				resourcePoolProvider.incrQuestionCount(user.getVendorId(), 1); // 资源池习题数
				vendorUserStatisManage.updateAfterBuild(user.getId(), false, question.getCreateAt()); // 首次录入后调用
			} else {
				if (oldStatus == CheckStatus.DRAFT && question.getStatus() == CheckStatus.EDITING) {
					// 由草稿转变为正式题目
					this.createCode(question.getId(), questionForm.getCodePrefix()); // 题目编号处理
					vendorUserStatisManage.updateAfterBuild(user.getId(), true, question.getCreateAt()); // 首次录入后调用
				} else if (oldStatus == CheckStatus.NOPASS && question.getStatus() == CheckStatus.EDITING) {
					// 由未通过变为待校验题目
					if (question.getQuestionSource() != QuestionSource.VENDOR) {
						vendorUserStatisManage.updateAfterNoPassBuild(user.getId(), question.getCreateAt()); // 审核不通过再次编辑
					}
				} else if (oldStatus == CheckStatus.EDITING && question.getStatus() == CheckStatus.ONEPASS) {
					// 一校校验修改
					if (question.getQuestionSource() != QuestionSource.VENDOR) {
						vendorUserStatisManage.updateAfterStep1Pass(question.getCreateId(), user.getId(), true,
								question.getCreateAt());
					}

					// 清空用户当前缓存的题目
					accountCacheService.invalidUserCheck(user.getId());
					vendorUserCounterProvider.incrOneCheck(user.getId(), 1); // 一校计数
				} else if (oldStatus == CheckStatus.ONEPASS && question.getStatus() == CheckStatus.PASS) {
					// 二校校验修改
					if (question.getQuestionSource() != QuestionSource.VENDOR) {
						vendorUserStatisManage.updateAfterStep2Pass(question.getCreateId(), user.getId(), true,
								question.getCreateAt());

						// 只有已通过的题目进行校本题目计数
						if (question.getSchoolId() != 0) {
							resconSchoolQuestionManage.addSchoolQuestion(question.getSchoolId(), question.getId());
						}

						// 添加WordML解析缓存
						String host = "http://" + request.getServerName() + ":" + request.getServerPort() + "/";
						questionWordMLService.asyncAdd(question, answerManage.getQuestionAnswers(question.getId()),
								host);

						// 添加二维码
						questionQRCodeService.asyncMakeQRCodeImage(question);
					}

					// 清空用户当前缓存的题目
					accountCacheService.invalidUserCheck(user.getId());
					vendorUserCounterProvider.incrTwoCheck(user.getId(), 1); // 二校计数
				} else if (oldStatus == CheckStatus.PASS && question.getStatus() == CheckStatus.PASS) {
					// 已通过修改
					// 添加WordML解析缓存
					String host = "http://" + request.getServerName() + ":" + request.getServerPort() + "/";
					questionWordMLService.asyncAdd(question, answerManage.getQuestionAnswers(question.getId()), host);

					// 处理更新已发布试卷的平均难度
					resconExamManage.updateExamAvgDifficultyByQuestion(question.getId());
				}

				if (oldStatus != CheckStatus.DRAFT && StringUtils.isBlank(question.getCode())) {
					// 如果还没有CODE
					this.createCode(question.getId(), questionForm.getCodePrefix()); // 题目编号处理
				}
			}

			// 纠错修改标记
			if (questionForm.getErrorFlag() == 1) {
				resconErrorManage.updateStatusByQuestionId(question.getId(), Status.DELETED);
			}

			// 索引
			this.addQuestionIndex(question.getId());

			// 相似题基础题库
			indexService.add(IndexType.QUESTION_SIMILAR_BASE, question.getId());

			// 相似题组索引，限制为已通过题目
			if (question.getStatus() == CheckStatus.PASS) {
				indexService.add(IndexType.QUESTION_SIMILAR, question.getId());
			}

			return new Value(question);
		} catch (ResourceConsoleException e) {
			return new Value(e);
		}
	}

	/**
	 * 题号处理.
	 * 
	 * @param questionId
	 * @param prefix
	 */
	private void createCode(long questionId, String prefix) {
		String nextCode = "";
		String maxCode = questionCacheService.getMaxCode(prefix);
		if (StringUtils.isBlank(maxCode)) {
			maxCode = questionManage.getMaxCode(prefix);
			if (StringUtils.isBlank(maxCode)) {
				maxCode = prefix + "A000000";
			}
		}
		nextCode = questionCacheService.setCode(prefix, questionCacheService.getNextCode(prefix, maxCode));
		try {
			questionManage.saveCode(questionId, nextCode);
		} catch (ResourceConsoleException e) {
			if (e.getCode() == CoreExceptionCode.ENTITY_EX) {
				maxCode = questionManage.getMaxCode(prefix);
				nextCode = questionCacheService.getNextCode(prefix, maxCode);
				nextCode = questionCacheService.setCode(prefix, nextCode);
				questionManage.saveCode(questionId, nextCode);
			}
		}
	}

	/**
	 * 创建草稿.
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "createDraft", method = RequestMethod.POST)
	public Value createDraft(String json) {
		QuestionForm questionForm = JSON.parseObject(json, QuestionForm.class);
		if (StringUtils.isNotBlank(questionForm.getAnalysis()) && questionForm.getAnalysis().length() > 65000) {
			return new Value(new ResourceConsoleException(ResourceConsoleException.ANALYSIS_OUT_OF_LENGTH));
		}
		if (StringUtils.isNotBlank(questionForm.getHint()) && questionForm.getHint().length() > 65000) {
			return new Value(new ResourceConsoleException(ResourceConsoleException.HINT_OUT_OF_LENGTH));
		}
		if (questionForm.getType() == Question.Type.SINGLE_CHOICE
				|| questionForm.getType() == Question.Type.MULTIPLE_CHOICE) {
			for (String choice : questionForm.getChoices()) {
				if (StringUtils.isNotBlank(choice) && choice.length() > 4000) {
					return new Value(new ResourceConsoleException(ResourceConsoleException.CHOICE_OUT_OF_LENGTH));
				}
			}
		}
		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
		questionForm.setVendorId(user.getVendorId());

		try {
			questionForm.setStatus(CheckStatus.DRAFT);
			Map<String, Object> questionMap = questionManage.saveQuestion(questionForm, user);
			Question question = (Question) questionMap.get("question");

			if (questionForm.getId() == null) {
				// 统计数据
				vendorUserStatisManage.updateAfterDraft(user.getId()); // 首次保存为草稿后调用

				// 索引
				this.addQuestionIndex(question.getId());
			} else {
				// 索引
				this.updateQuestionIndex(question.getId());
			}

			return new Value(question.getId());
		} catch (ResourceConsoleException e) {
			return new Value(e);
		}
	}

	/**
	 * 习题查询（录入）.
	 * 
	 * @param form
	 *            习题查询表单.
	 * @return
	 */
	@RequestMapping(value = "myQuestions")
	public Value myQuestions(QuestionQueryForm form) {
		form.setCreateId(Security.getUserId());
		form.setSearchFlag(1);
		return this.query(form);
	};

	/**
	 * 习题查询（校验）.
	 * 
	 * @param form
	 *            习题查询表单.
	 * @return
	 */
	@RolesAllowed(userTypes = { "VENDOR_ADMIN", "VENDOR_HEAD", "VENDOR_CHECK" })
	@RequestMapping(value = "myCheckQuestions")
	public Value myCheckQuestions(QuestionQueryForm form) {
		form.setVerifyId(Security.getUserId());
		form.setSearchFlag(2);
		return this.query(form);
	};

	/**
	 * 习题查询.
	 * 
	 * @param form
	 *            习题查询表单.
	 * @return
	 */
	@RequestMapping(value = "query")
	@RolesAllowed(userTypes = { "VENDOR_ADMIN", "VENDOR_HEAD", "VENDOR_CHECK", "VENDOR_BUILD" })
	public Value query(QuestionQueryForm form) {
		int offset = 0;
		int size = 0;
		List<Order> orders = new ArrayList<Order>();
		List<IndexTypeable> types = Lists.<IndexTypeable>newArrayList(IndexType.QUESTION); // 搜索习题
		BoolQueryBuilder qb = null;
		try {
			offset = (form.getPage() - 1) * form.getPageSize();
			size = form.getPageSize();
			orders = new ArrayList<Order>();
			qb = QueryBuilders.boolQuery();
			if (StringUtils.isNotBlank(form.getKey())) {
				orders.add(new Order("_score", Direction.DESC));
				if (form.getSelectType() == 1) {
					qb.must(QueryBuilders.multiMatchQuery(form.getKey(), "contents", "knowledgePoints", "code"));
				} else if (form.getSelectType() == 2) {
					qb.must(QueryBuilders.multiMatchQuery(form.getKey(), "knowledgePoints"));
				} else if (form.getSelectType() == 3) {
					qb.must(QueryBuilders.multiMatchQuery(form.getKey(), "contents"));
				} else if (form.getSelectType() == 4) {
					qb.must(QueryBuilders.multiMatchQuery(form.getKey(), "code"));
				}
			}

			if (form.getSearchFlag() == 3) {
				// 知识点修正-旧知识点体系
				orders.add(new Order("createAt", Direction.DESC));
			} else if (form.getSearchFlag() == 4) {
				// 知识点修正-新知识点体系
				orders.add(new Order("knowledgeCreateAt", Direction.DESC));
			} else {
				if (form.getSortType() == 1) {
					// 创建时间
					orders.add(new Order("createAt", form.getSort() == 0 ? Direction.ASC : Direction.DESC));
				} else if (form.getSortType() == 2) {
					// 难度系数
					orders.add(new Order("difficulty", form.getSort() == 0 ? Direction.ASC : Direction.DESC));
					orders.add(new Order("createAt", Direction.ASC));
				} else if (form.getSortType() == 3) {
					// 校验时间
					orders.add(new Order("verifyAt", form.getSort() == 0 ? Direction.ASC : Direction.DESC));
					orders.add(new Order("verify2At", form.getSort() == 0 ? Direction.ASC : Direction.DESC));
				}
			}

			if (form.getCategoryCode() != null) {
				qb.must(QueryBuilders.termQuery("textbookCategoryCode", form.getCategoryCode()));
			}
			if (form.getTextbookCode() != null) {
				qb.must(QueryBuilders.termQuery("textbookCode", form.getTextbookCode()));
			}
			if (form.getCheckStatus() != null) {
				qb.must(QueryBuilders.termQuery("checkStatus", form.getCheckStatus().getValue()));
			} else {
				Set<Integer> status = new HashSet<Integer>();
				if (form.getSearchFlag() == 0 || form.getSearchFlag() == 1) {
					status.add(CheckStatus.DRAFT.getValue());
					status.add(CheckStatus.EDITING.getValue());
				}
				if (null != form.getVerifyId()) {
					// 搜索我的校验
					status.add(CheckStatus.EDITING.getValue());
				}
				status.add(CheckStatus.ONEPASS.getValue());
				status.add(CheckStatus.PASS.getValue());
				status.add(CheckStatus.NOPASS.getValue());
				qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("checkStatus", status));
			}
			if (form.getSearchFlag() == 3) {
				// 知识点修正
				qb.filter(QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery("knowledgePointCodes")));
				// qb.filter(QueryBuilders.missingQuery("knowledgePointCodes").existence(true).nullValue(true));
			}
			if (StringUtils.isNotBlank(form.getDifficulty())) {
				double gte = 0;
				double lte = 0;
				try {
					lte = Double.parseDouble(form.getDifficulty().split("-")[0]);
					gte = Double.parseDouble(form.getDifficulty().split("-")[1]);
				} catch (NumberFormatException e) {
					return new Value(new IllegalArgFormatException());
				}
				qb.must(QueryBuilders.rangeQuery("difficulty").gte(gte).lte(lte));
			}
			if (null != form.getPhaseCode()) {
				qb.must(QueryBuilders.termQuery("phaseCode", form.getPhaseCode()));
			}
			if (null != form.getSubjectCode()) {
				qb.must(QueryBuilders.termQuery("subjectCode", form.getSubjectCode()));
			}
			if (null != form.getVendorId()) {
				qb.must(QueryBuilders.termQuery("vendorId", form.getVendorId()));
			} else {
				qb.must(QueryBuilders.termQuery("vendorId",
						vendorUserManage.getVendorUser(Security.getUserId()).getVendorId()));
			}
			if (null != form.getCreateId()) {
				qb.must(QueryBuilders.termQuery("createId", form.getCreateId()));
			}
			if (null != form.getVerifyId()) {
				qb.must(QueryBuilders.multiMatchQuery(form.getVerifyId(), "verifyId", "verify2Id"));
			}
			if (form.getCreateBt() != null && form.getCreateEt() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date(form.getCreateEt()));
				cal.add(Calendar.DAY_OF_YEAR, 1);
				qb.must(QueryBuilders.rangeQuery("createAt").gte(form.getCreateBt()).lte(cal.getTime().getTime()));
			}
			if (form.getVerifyBt() != null && form.getVerifyEt() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date(form.getVerifyEt()));
				cal.add(Calendar.DAY_OF_YEAR, 1);
				BoolQueryBuilder qbIn = QueryBuilders.boolQuery();
				qbIn.should(QueryBuilders.rangeQuery("verifyAt").gte(form.getVerifyBt()).lte(cal.getTime().getTime()));
				qbIn.should(QueryBuilders.rangeQuery("verify2At").gte(form.getVerifyBt()).lte(cal.getTime().getTime()));
				qb.must(qbIn);
			}
			if (StringUtils.isNotBlank(form.getType())) {
				qb.must(QueryBuilders.termQuery("type", Question.Type.valueOf(form.getType()).getValue()));
			}
			if (null != form.getMetaknowCode()) {
				qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("metaKnowpointCodes",
						Lists.newArrayList(form.getMetaknowCode())));
			}
			if (null != form.getKnowledgePointCode()) {
				qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowledgePointCodes",
						Lists.newArrayList(form.getKnowledgePointCode())));
			}
			if (null != form.getExaminationPointCode()) {
				qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("examinationPointCodes",
						Lists.newArrayList(form.getExaminationPointCode())));
			}
			if (null != form.getSectionCode()) {
				qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("sectionCodes",
						Lists.newArrayList(form.getSectionCode())));
			}
			if (StringUtils.isNotBlank(form.getCode())) {
				qb.must(QueryBuilders.queryStringQuery("code:\"" + form.getCode() + "\""));
			}
			if (StringUtils.isNotBlank(form.getSource())) {
				qb.must(QueryBuilders.termQuery("source", form.getSource()));
			}
			// 学科类型
			if (CollectionUtils.isNotEmpty(form.getTypeCodes())) {
				qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("typeCode", form.getTypeCodes()));
			}
			// 不包含的习题集合
			if (CollectionUtils.isNotEmpty(form.getNotHasQuestionIds())) {
				qb.mustNot(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("resourceId",
						form.getNotHasQuestionIds()));
			}
			// 题目类型
			if (CollectionUtils.isNotEmpty(form.getQuestionCategorys())) {
				qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("questionCategorys",
						form.getQuestionCategorys()));
			}

			if (CollectionUtils.isNotEmpty(form.getQuestionTags())) {
				qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("questionTags",
						form.getQuestionTags()));
			}
		} catch (Exception e) {
			return new Value(new IllegalArgFormatException());
		}

		Order[] orderArray = new Order[orders.size()];
		for (int i = 0; i < orders.size(); i++) {
			Order order = orders.get(i);
			orderArray[i] = order;
		}
		Page docPage = searchService.search(types, offset, size, qb, null, orderArray);

		// 查询数据库
		List<Long> qustionIds = new ArrayList<Long>();
		for (Document document : docPage.getDocuments()) {
			if (document.getId() != null && !document.getId().equals("null")) {
				qustionIds.add(Long.parseLong(document.getId()));
			}
		}

		Map<Long, VQuestion> qusetionMap = questionConvert.to(questionManage.mget(qustionIds));
		List<VQuestion> qusetionList = new ArrayList<VQuestion>(qustionIds.size());
		for (Long id : qustionIds) {
			VQuestion question = qusetionMap.get(id);

			// 判断转换标记，填空题、复合题需要判断
			if (question.getType() == Question.Type.FILL_BLANK) {
				// ...
			} else if (question.getType() == Question.Type.COMPOSITE) {
				question.setAsciiStatus(AsciiStatus.PASS);
				List<VQuestion> children = question.getChildren();
				for (VQuestion child : children) {
					if (child.getType() == Question.Type.FILL_BLANK && child.getAsciiStatus() == AsciiStatus.NOCHANGE) {
						question.setAsciiStatus(AsciiStatus.NOCHANGE);
						break;
					}
				}
			} else {
				// 其他题目不需要判断
				question.setAsciiStatus(AsciiStatus.PASS);
			}

			qusetionList.add(question);
		}

		VPage<VQuestion> vPage = new VPage<VQuestion>();
		vPage.setCurrentPage(form.getPage());
		vPage.setPageSize(form.getPageSize());
		vPage.setTotal(docPage.getTotalCount());
		vPage.setTotalPage(docPage.getTotalPage());
		vPage.setItems(qusetionList);
		return new Value(vPage);
	}

	/**
	 * 获得题目其他相关数据.
	 * 
	 * @param subjectCode
	 * @return
	 */
	@RequestMapping(value = "getQuestionOthers", method = RequestMethod.POST)
	public Value getQuestionOthers(@RequestParam(value = "id") Long questionId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (questionId != null) {
			Question question = questionManage.get(questionId);
			if (null != question) {
				List<Long> ids = new ArrayList<Long>(4);
				ids.add(question.getCreateId());
				ids.add(question.getUpdateId());
				ids.add(question.getVerifyId());
				ids.add(question.getVerify2Id());
				Map<Long, VendorUser> vendorMap = vendorUserManage.mgetVendorUser(ids);
				if (null != ids.get(0)) {
					VendorUser user = vendorMap.get(ids.get(0));
					if (user != null) {
						if (StringUtils.isBlank(user.getRealName())) {
							user.setRealName(user.getName());
						}
						map.put("creator", user);
					}
				}
				if (null != ids.get(1)) {
					VendorUser user = vendorMap.get(ids.get(1));
					if (user != null) {
						if (StringUtils.isBlank(user.getRealName())) {
							user.setRealName(user.getName());
						}
						map.put("updater", user);
					}
				}
				if (null != ids.get(2)) {
					VendorUser user = vendorMap.get(ids.get(2));
					if (user != null) {
						if (StringUtils.isBlank(user.getRealName())) {
							user.setRealName(user.getName());
						}
						map.put("verifyer", user);
					}
				}
				if (null != ids.get(3)) {
					VendorUser user = vendorMap.get(ids.get(3));
					if (user != null) {
						if (StringUtils.isBlank(user.getRealName())) {
							user.setRealName(user.getName());
						}
						map.put("verifyer2", user);
					}
				}

				if (question.getSectionCode() != null) {
					List<Section> sections = sectionService.mgetListByChildId(question.getSectionCode());
					map.put("sections", sections);
				}
			}
		}
		return new Value(map);
	}

	/**
	 * 删除题目.
	 * 
	 * @param questionId
	 *            题目ID
	 * @return
	 */
	@RequestMapping(value = "deleteQuestion", method = RequestMethod.POST)
	public Value deleteQuestion(@RequestParam(value = "id") Long questionId) {
		if (null == questionId) {
			return new Value(new MissingArgumentException());
		}
		Question question = questionManage.get(questionId);
		if (null == question) {
			return new Value(new IllegalArgException());
		}

		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
		DataPermission permission = null;
		if (StringUtils.isNotBlank(user.getPermissions())) {
			permission = (DataPermission) JSON.parseObject(user.getPermissions(), DataPermission.class);
			if (question.getStatus() == CheckStatus.DRAFT
					&& question.getCreateId().longValue() == user.getId().longValue()) {
				// 自己的草稿可以删除
				permission.setDeleteQuestion(1);
			}
		} else if (user.getType() == UserType.VENDOR_ADMIN) {
			// 管理员可以删除
			permission = new DataPermission();
			permission.setDeleteQuestion(1);
		} else if (question.getStatus() == CheckStatus.DRAFT
				&& question.getCreateId().longValue() == user.getId().longValue()) {
			// 自己的草稿可以删除
			permission = new DataPermission();
			permission.setDeleteQuestion(1);
		}

		if (permission == null || permission.getDeleteQuestion() != 1) {
			return new Value(new NoPermissionException());
		}

		try {
			questionManage.delete(questionId);

			// 统计数据
			resourcePoolProvider.incrQuestionCount(user.getVendorId(), -1); // 资源池习题数

			// 草稿统计数据
			if (question.getStatus() == CheckStatus.DRAFT) {
				vendorUserStatisManage.deleteAfterDraft(question.getCreateId());

				// 若草稿被书本引用，需要同时删除书本中的草稿引用关系、试卷中的草稿引用关系
				List<BookQuestion> bookQuestions = bookQuestionManage.queryByQuestionId(question.getId());
				if (bookQuestions.size() > 0) {
					for (BookQuestion bookQuestion : bookQuestions) {
						bookQuestionController.deleteQuestion(bookQuestion.getBookVersionId(),
								bookQuestion.getBookCatalogId(), questionId);
					}
				}
				examPaperQuestionManage.deleteQuestionFromExam(questionId);
			}

			// 删除索引
			this.deleteQuestionIndex(questionId);
		} catch (ResourceConsoleException e) {
			return new Value(e);
		}

		return new Value();
	}

	/**
	 * 获得单独的题目.
	 * 
	 * @param questionId
	 * @return
	 */
	@RequestMapping(value = "getQuestion", method = RequestMethod.POST)
	public Value getQuestion(long questionId) {
		Question question = questionManage.get(questionId);
		if (null == question) {
			return new Value(new IllegalArgException());
		}

		return new Value(questionConvert.to(question));
	}

	/**
	 * 新知识体系相关数据（知识点、考点）
	 * 
	 * @param phaseCode
	 *            阶段
	 * @param subjectCode
	 *            学科
	 * @since v2.0.1
	 * @return
	 */
	@RequestMapping(value = "queryNewKnowledgeDatas", method = RequestMethod.POST)
	public Value queryNewKnowledgeDatas(Integer phaseCode, Integer subjectCode) {
		Map<String, Object> returnMap = new HashMap<String, Object>(4);
		List<KnowledgeSystem> knowledgeSystems = knowledgeSystemService.findAll(phaseCode, subjectCode, null, null);
		List<KnowledgePoint> knowledgePoints = knowledgePointService.findAllByStatus(phaseCode, subjectCode,
				Status.ENABLED);
		List<ExaminationPoint> examinationPoints = examinationPointService.list(phaseCode, subjectCode, Status.ENABLED);
		List<Map<String, Object>> knowledgePointTree = new ArrayList<Map<String, Object>>(
				knowledgeSystems.size() + knowledgePoints.size());
		List<Map<String, Object>> examinationPointTree = new ArrayList<Map<String, Object>>(
				knowledgeSystems.size() + examinationPoints.size());

		for (int i = 0; i < knowledgeSystems.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			KnowledgeSystem knowledgeSystem = knowledgeSystems.get(i);
			map.put("id", knowledgeSystem.getCode());
			map.put("pId", knowledgeSystem.getPcode());
			map.put("name", knowledgeSystem.getName());
			map.put("nocheck", true); // 只有原知识点才能选择
			map.put("isParent", true);
			knowledgePointTree.add(map);
			examinationPointTree.add(map);
		}
		for (int i = 0; i < knowledgePoints.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			KnowledgePoint knowledgePoint = knowledgePoints.get(i);
			map.put("id", knowledgePoint.getCode());
			map.put("pId", knowledgePoint.getPcode());
			map.put("name", knowledgePoint.getName());
			map.put("nocheck", false); // 只有原知识点才能选择
			knowledgePointTree.add(map);
		}
		for (int i = 0; i < examinationPoints.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			ExaminationPoint examinationPoint = examinationPoints.get(i);
			map.put("id", examinationPoint.getId());
			map.put("pId", examinationPoint.getPcode());
			map.put("name", examinationPoint.getName());
			map.put("nocheck", false); // 只有考点才能选择
			examinationPointTree.add(map);
		}
		returnMap.put("knowledgePointTree", knowledgePointTree); // 知识点树
		returnMap.put("knowledgePoints", knowledgePoints); // 元知识点列表
		returnMap.put("examinationPointTree", examinationPointTree); // 考点树
		returnMap.put("examinationPoints", examinationPoints); // 考点列表

		// v3.0 同步知识点树
		List<KnowledgeSync> allKnowledgeSyncs = knowledgeSyncService.findAll(phaseCode, subjectCode, Status.ENABLED);
		List<Map<String, Object>> knowledgeSyncTree = new ArrayList<Map<String, Object>>(allKnowledgeSyncs.size());
		List<KnowledgeSync> knowledgeSyncs = new ArrayList<KnowledgeSync>();
		for (KnowledgeSync knowledgeSync : allKnowledgeSyncs) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", knowledgeSync.getCode());
			map.put("pId", knowledgeSync.getPcode());
			map.put("title", knowledgeSync.getName());
			map.put("name", knowledgeSync.getName());
			map.put("nocheck", knowledgeSync.getLevel() != 4);
			map.put("isParent", knowledgeSync.getLevel() != 4);
			knowledgeSyncTree.add(map);
			if (knowledgeSync.getLevel() == 4) {
				knowledgeSyncs.add(knowledgeSync);
			}
		}
		returnMap.put("v3SyncTree", knowledgeSyncTree); // 同步知识点树
		returnMap.put("knowledgeSyncs", knowledgeSyncs); // 同步元知识点列表

		// v3.0 复习知识点树
		List<KnowledgeReview> allKnowledgeReviews = knowledgeReviewService.findAll(phaseCode, subjectCode,
				Status.ENABLED);
		List<Map<String, Object>> knowledgeReviewTree = new ArrayList<Map<String, Object>>(allKnowledgeReviews.size());
		List<KnowledgeReview> knowledgeReviews = new ArrayList<KnowledgeReview>();
		for (KnowledgeReview knowledgeReview : allKnowledgeReviews) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", knowledgeReview.getCode());
			map.put("pId", knowledgeReview.getPcode());
			map.put("title", knowledgeReview.getName());
			map.put("name", knowledgeReview.getName());
			map.put("nocheck", knowledgeReview.getLevel() != 4);
			map.put("isParent", knowledgeReview.getLevel() != 4);
			knowledgeReviewTree.add(map);
			if (knowledgeReview.getLevel() == 4) {
				knowledgeReviews.add(knowledgeReview);
			}
		}
		returnMap.put("v3ReviewTree", knowledgeReviewTree); // 复习知识点树
		returnMap.put("knowledgeReviews", knowledgeReviews); // 复习元知识点列表

		return new Value(returnMap);
	}

	/**
	 * 获得习题应用场景.
	 * 
	 * @return
	 */
	@RequestMapping(value = "getQuestionScenes", method = RequestMethod.POST)
	public Value getQuestionScenes() {
		return new Value(questionSceneManage.findList());
	}

	/**
	 * 修补习题章节关系.
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "reQuestionSection", method = RequestMethod.POST)
	public Value reQuestionSection(Long p) {
		if (p == null || p != new Date().getHours()) {
			return new Value();
		}
		int offset = 0;
		int size = 200;
		com.lanking.cloud.sdk.data.Page<Question> page = questionManage.queryReQuestionSection(P.offset(offset, size));
		while (page.isNotEmpty()) {
			questionManage.saveReQuestionSection(page.getItems());
			offset = offset + size;
			page = questionManage.queryReQuestionSection(P.offset(offset, size));
		}
		return new Value();
	}

	/**
	 * 建立习题索引.
	 * 
	 * @param questionId
	 */
	private void addQuestionIndex(long questionId) {
		indexService.syncAdd(IndexType.QUESTION, questionId);
	}

	private void updateQuestionIndex(long questionId) {
		indexService.syncUpdate(IndexType.QUESTION, questionId);
	}

	/**
	 * 删除资源索引.
	 * 
	 * @param streamId
	 */
	@Async
	private void deleteQuestionIndex(long questionId) {
		indexService.syncDelete(IndexType.QUESTION, questionId);
	}
}
package com.lanking.uxb.rescon.basedata.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lanking.cloud.component.searcher.api.SearchService;
import com.lanking.cloud.domain.common.baseData.ExaminationPoint;
import com.lanking.cloud.domain.common.baseData.ExaminationPointKnowledgePoint;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.rescon.account.api.ResconVendorUserManage;
import com.lanking.uxb.rescon.basedata.api.ResconExaminationPointService;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgePointService;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgeSystemService;
import com.lanking.uxb.rescon.basedata.api.ResconQuestionExaminationPointService;
import com.lanking.uxb.rescon.basedata.convert.ResconKnowledgePointConvert;
import com.lanking.uxb.rescon.basedata.convert.ResconKnowledgeSystemConvert;
import com.lanking.uxb.rescon.basedata.form.ResconExaminationPointForm;
import com.lanking.uxb.rescon.common.ex.ResourceConsoleException;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;
import com.lanking.uxb.rescon.question.convert.ResconQuestionConvert;
import com.lanking.uxb.rescon.question.value.VQuestion;
import com.lanking.uxb.service.code.api.ExaminationPointKnowledgePointService;
import com.lanking.uxb.service.search.api.IndexService;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 考点知识体系.
 * 
 * @author wlche
 * @since v2.0.1
 */
@RestController
@RequestMapping("rescon/exampoint")
public class ResconExaminationPointController {

	@Autowired
	private ResconKnowledgeSystemConvert knowledgeSystemConvert;
	@Autowired
	private ResconKnowledgeSystemService knowledgeSystemService;
	@Autowired
	private ResconExaminationPointService resconExaminationPointService;
	@Autowired
	private ResconQuestionManage questionManage;
	@Autowired
	private ResconQuestionConvert questionConvert;
	@Autowired
	private ResconKnowledgePointService knowledgePointService;
	@Autowired
	private ResconKnowledgePointConvert knowledgePointConvert;
	@Autowired
	private ResconVendorUserManage vendorUserManage;
	@Autowired
	private ResconQuestionExaminationPointService questionExaminationPointService;
	@Autowired
	private ExaminationPointKnowledgePointService examinationPointKnowledgePointService;

	@Autowired
	private SearchService searchService;
	@Autowired
	private IndexService indexService;

	/**
	 * 初始化tree
	 *
	 * @param subjectCode
	 *            学科代码
	 * @param phaseCode
	 *            阶段代码
	 * @return {@link Value}
	 */
	@RequestMapping(value = "findAll", method = { RequestMethod.GET, RequestMethod.POST })
	public Value findAll(@RequestParam(value = "subjectCode") Integer subjectCode,
			@RequestParam(value = "phaseCode") Integer phaseCode) {
		List<KnowledgeSystem> knowledgeSystems = knowledgeSystemService.findAll(phaseCode, subjectCode, null, null);
		List<ExaminationPoint> examinationPoints = resconExaminationPointService.list(phaseCode, subjectCode, null);

		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>(knowledgeSystems.size());

		assembleData(knowledgeSystems, examinationPoints, datas);

		return new Value(datas);
	}

	private void assembleData(List<KnowledgeSystem> knowledgeSystems, List<ExaminationPoint> examinationPoints,
			List<Map<String, Object>> datas) {
		// 知识体系
		for (KnowledgeSystem ks : knowledgeSystems) {
			Map<String, Object> map = new HashMap<String, Object>(5);
			map.put("id", ks.getCode());
			map.put("name", ks.getName());
			map.put("parent", ks.getPcode());
			map.put("level", ks.getLevel());
			map.put("sequence", ks.getSequence());
			map.put("phaseCode", ks.getPhaseCode());
			map.put("subjectCode", ks.getSubjectCode());
			map.put("isParent", true);
			datas.add(map);
		}

		// 考点
		for (ExaminationPoint ep : examinationPoints) {
			Map<String, Object> map = new HashMap<String, Object>(5);
			map.put("id", ep.getId());
			map.put("name", ep.getName());
			map.put("parent", ep.getPcode());
			map.put("level", 4);
			map.put("phaseCode", ep.getPhaseCode());
			map.put("subjectCode", ep.getSubjectCode());
			map.put("status", ep.getStatus());
			map.put("examinationPoint", ep);
			datas.add(map);
		}
	}

	/**
	 * 查找已全部启用的树
	 *
	 * @param subjectCode
	 *            学科代码
	 * @param phaseCode
	 *            阶段代码
	 * @return {@link Value}
	 */
	@RequestMapping(value = "findAllUse", method = { RequestMethod.GET, RequestMethod.POST })
	public Value findAllUse(@RequestParam(value = "subjectCode") Integer subjectCode,
			@RequestParam(value = "phaseCode") Integer phaseCode) {
		List<KnowledgeSystem> knowledgeSystems = knowledgeSystemService.findAll(phaseCode, subjectCode, null, null);
		List<ExaminationPoint> examinationPoints = resconExaminationPointService.listUse(phaseCode, subjectCode);
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>(knowledgeSystems.size());

		assembleData(knowledgeSystems, examinationPoints, datas);

		return new Value(datas);
	}

	/**
	 * 保存考点.
	 * 
	 * @param form
	 *            考点表单.
	 * @return
	 */
	@RequestMapping(value = "save", method = { RequestMethod.POST })
	public Value save(String json) {
		if (StringUtils.isBlank(json)) {
			return new Value(new MissingArgumentException());
		}
		try {
			ResconExaminationPointForm form = JSONObject.parseObject(json, ResconExaminationPointForm.class);
			resconExaminationPointService.save(form);
		} catch (AbstractException e) {
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 保存考点状态.
	 * 
	 * @return
	 */
	@RequestMapping(value = "saveStatus", method = { RequestMethod.POST })
	public Value saveStatus(Long id, Status status) {
		try {
			List<Long> needUpdateQuestions = resconExaminationPointService.saveStatus(id, status);
			// 更新题目标签索引
			if (CollectionUtils.isNotEmpty(needUpdateQuestions)) {
				indexService.update(IndexType.QUESTION, needUpdateQuestions);
			}

			// 更新考点关联的习题索引
			resconExaminationPointService.asyncUpdateQuestionIndexByExaminationPoint(id);
		} catch (AbstractException e) {
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 启用所有考点.
	 * 
	 * @return
	 */
	@RequestMapping(value = "saveAll", method = { RequestMethod.POST })
	public Value saveAll() {
		try {
			List<Long> needUpdateQuestions = resconExaminationPointService.saveAll();
			// 更新题目的标签至索引中
			if (CollectionUtils.isNotEmpty(needUpdateQuestions)) {
				indexService.update(IndexType.QUESTION, needUpdateQuestions);
			}
		} catch (AbstractException e) {
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 删除考点.
	 * 
	 * @return
	 */
	@RequestMapping(value = "delete", method = { RequestMethod.POST })
	public Value delete(Long id) {
		try {
			resconExaminationPointService.delete(id);
		} catch (AbstractException e) {
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 添加例题.
	 * 
	 * @param id
	 *            考点ID
	 * @param code
	 *            例题CODE
	 * @return
	 */
	@RequestMapping(value = "addQuestion", method = { RequestMethod.POST })
	public Value addQuestion(Long id, String code) {
		if (id == null || code == null) {
			return new Value(new MissingArgumentException());
		}
		ExaminationPoint examinationPoint = resconExaminationPointService.get(id);
		List<Question> questionList = questionManage.findQuestionByCode(Lists.newArrayList(code), null);
		if (questionList.size() > 0 && examinationPoint != null) {
			Question question = questionList.get(0);

			VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
			if (question.getVendorId() != null && question.getVendorId().longValue() != user.getVendorId()) {
				// 供应商不匹配
				return new Value(new ResourceConsoleException(ResourceConsoleException.QUESTION_VENDOR_NOT_MATCH));
			}

			if (question.getSchoolId() > 0) {
				// 不能添加校本题
				return new Value(new ResourceConsoleException(ResourceConsoleException.QUESTION_SCHOOL_NOT_MATCH));
			}

			if (question.getPhaseCode().intValue() != examinationPoint.getPhaseCode().intValue()
					|| question.getSubjectCode().intValue() != examinationPoint.getSubjectCode().intValue()) {
				// 学科阶段不匹配
				return new Value(new ResourceConsoleException(ResourceConsoleException.SUBJECT_PHASE_NOT_MATCH));
			}
			if (question.getStatus() != CheckStatus.PASS) {
				// 题目状态不正确
				return new Value(new ResourceConsoleException(ResourceConsoleException.QUESTION_STATUS_NOT_MATCH));
			}
			try {
				resconExaminationPointService.addQuestion(id, question.getId());
				return new Value(questionConvert.to(question));
			} catch (AbstractException e) {
				return new Value(e);
			}
		} else if (questionList.size() == 0) {
			return new Value(new EntityNotFoundException());
		}
		return new Value();
	}

	/**
	 * 获取习题.
	 * 
	 * @param phaseCode
	 *            阶段
	 * @param subjectCode
	 *            学科
	 * @param code
	 *            例题CODE
	 * @return
	 */
	@RequestMapping(value = "getQuestion", method = { RequestMethod.POST })
	public Value getQuestion(Integer phaseCode, Integer subjectCode, String code) {
		if (phaseCode == null || subjectCode == null || code == null) {
			return new Value(new MissingArgumentException());
		}
		List<Question> questionList = questionManage.findQuestionByCode(Lists.newArrayList(code), null);
		if (questionList.size() > 0) {
			Question question = questionList.get(0);
			VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());

			if (question.getVendorId() != null && question.getVendorId().longValue() != user.getVendorId()) {
				// 供应商不匹配
				return new Value(new ResourceConsoleException(ResourceConsoleException.QUESTION_VENDOR_NOT_MATCH));
			}

			if (question.getSchoolId() > 0) {
				// 不能添加校本题
				return new Value(new ResourceConsoleException(ResourceConsoleException.QUESTION_SCHOOL_NOT_MATCH));
			}

			if (question.getPhaseCode().intValue() != phaseCode.intValue()
					|| question.getSubjectCode().intValue() != subjectCode.intValue()) {
				// 学科阶段不匹配
				return new Value(new ResourceConsoleException(ResourceConsoleException.SUBJECT_PHASE_NOT_MATCH));
			}
			if (question.getStatus() != CheckStatus.PASS) {
				// 题目状态不正确
				return new Value(new ResourceConsoleException(ResourceConsoleException.QUESTION_STATUS_NOT_MATCH));
			}
			return new Value(questionConvert.to(question));
		} else if (questionList.size() == 0) {
			return new Value(new EntityNotFoundException());
		}
		return new Value();
	};

	/**
	 * 删除习题.
	 * 
	 * @param id
	 *            考点ID
	 * @param questionId
	 *            习题ID
	 * @return
	 */
	@RequestMapping(value = "deleteQuestion", method = { RequestMethod.POST })
	public Value deleteQuestion(Long id, Long questionId) {
		if (id == null || questionId == null) {
			return new Value(new MissingArgumentException());
		}
		try {
			resconExaminationPointService.deleteQuestion(id, questionId);
		} catch (AbstractException e) {
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 获取考点例题、知识点等数据.
	 * 
	 * @param id
	 *            考点ID
	 * @return
	 */
	@RequestMapping(value = "getOtherDatas", method = { RequestMethod.POST })
	public Value getOtherDatas(Long id) {
		if (id == null) {
			return new Value(new MissingArgumentException());
		}

		ExaminationPoint examinationPoint = resconExaminationPointService.get(id);
		if (examinationPoint == null) {
			return new Value(new MissingArgumentException());
		}
		Map<String, Object> map = new HashMap<String, Object>(2);
		List<Long> questionIds = examinationPoint.getQuestions();
		List<ExaminationPointKnowledgePoint> examinationPointKnowledgePoints = examinationPointKnowledgePointService
				.findByExaminationPoint(examinationPoint.getId());
		List<Long> knowpointIds = new ArrayList<Long>(examinationPointKnowledgePoints.size());
		for (ExaminationPointKnowledgePoint ep : examinationPointKnowledgePoints) {
			knowpointIds.add(ep.getKnowledgePointCode());
		}

		// 习题
		List<VQuestion> questions = new ArrayList<VQuestion>(questionIds.size());
		if (questionIds.size() > 0) {
			Map<Long, VQuestion> questionMap = questionConvert.to(questionManage.mget(questionIds));
			for (Long questionId : questionIds) {
				questions.add(questionMap.get(questionId));
			}
		}
		map.put("questions", questions);

		// 知识点
		map.put("knowpoints", knowpointIds);

		return new Value(map);
	}

	/**
	 * 更新题目.
	 * 
	 * @param id
	 *            考点ID.
	 * @param questionIds
	 *            题目ID集合.
	 * @return
	 */
	@RequestMapping(value = "saveQuestions", method = { RequestMethod.POST })
	public Value saveQuestions(Long id, Long[] questionIds) {
		if (id == null || questionIds == null || questionIds.length == 0) {
			return new Value(new MissingArgumentException());
		}
		try {
			resconExaminationPointService.saveQuestions(id, Lists.newArrayList(questionIds));
		} catch (AbstractException e) {
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 获取统计数据.
	 * 
	 * @param subjectCode
	 *            学科
	 * @param phaseCode
	 *            阶段
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "queryCounts", method = { RequestMethod.POST })
	public Value queryCounts(@RequestParam(value = "subjectCode") Integer subjectCode,
			@RequestParam(value = "phaseCode") Integer phaseCode) {
		if (subjectCode == null || phaseCode == null) {
			return new Value(new MissingArgumentException());
		}
		Map map = resconExaminationPointService.queryCounts(phaseCode, subjectCode);
		return new Value(map);
	}
}

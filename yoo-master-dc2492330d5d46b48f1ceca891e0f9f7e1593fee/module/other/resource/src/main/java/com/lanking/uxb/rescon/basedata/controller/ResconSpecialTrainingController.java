package com.lanking.uxb.rescon.basedata.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.domain.common.baseData.ExaminationPoint;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.specialTraining.SpecialTraining;
import com.lanking.cloud.domain.common.resource.specialTraining.SpecialTrainingOperateLog;
import com.lanking.cloud.domain.common.resource.specialTraining.SpecialTrainingQuestion;
import com.lanking.cloud.domain.common.resource.specialTraining.SpecialTrainingStatus;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.rescon.account.api.ResconVendorUserManage;
import com.lanking.uxb.rescon.basedata.api.ResconExaminationPointService;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgePointService;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgeSystemService;
import com.lanking.uxb.rescon.basedata.api.ResconSpecialTrainingOperateLogService;
import com.lanking.uxb.rescon.basedata.api.ResconSpecialTrainingQuestionService;
import com.lanking.uxb.rescon.basedata.api.ResconSpecialTrainingService;
import com.lanking.uxb.rescon.basedata.convert.ResconExaminationPointConvert;
import com.lanking.uxb.rescon.basedata.convert.ResconKnowledgePointConvert;
import com.lanking.uxb.rescon.basedata.convert.ResconSpecialTrainingConvert;
import com.lanking.uxb.rescon.basedata.convert.ResconSpecialTrainingOperateLogConvert;
import com.lanking.uxb.rescon.basedata.form.ResconSpecialTrainingForm;
import com.lanking.uxb.rescon.basedata.value.VSpecialTraining;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;
import com.lanking.uxb.rescon.question.convert.ResconQuestionConvert;
import com.lanking.uxb.rescon.question.value.VQuestion;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.convert.PhaseConvert;
import com.lanking.uxb.service.code.convert.SubjectConvert;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 针对性训练
 * 
 * @author wangsenhao
 * @since 2.0.1
 */
@RestController
@RequestMapping(value = "/rescon/special")
public class ResconSpecialTrainingController {

	@Autowired
	private ResconExaminationPointService resconExaminationPointService;
	@Autowired
	private ResconExaminationPointConvert resconExaminationPointConvert;
	@Autowired
	private ResconKnowledgePointService resconKnowledgePointService;
	@Autowired
	private ResconKnowledgePointConvert resconKnowledgePointConvert;
	@Autowired
	private ResconSpecialTrainingService resconSpecialTrainingService;
	@Autowired
	private ResconSpecialTrainingConvert resconSpecialTrainingConvert;
	@Autowired
	private ResconSpecialTrainingQuestionService resconSpecialTrainingQuestionService;
	@Autowired
	private ResconQuestionConvert questionConvert;
	@Autowired
	private ResconSpecialTrainingOperateLogService logService;
	@Autowired
	private ResconSpecialTrainingOperateLogConvert logConvert;
	@Autowired
	private ResconKnowledgeSystemService resconKnowledgeSystemService;
	@Autowired
	private ResconQuestionManage resconQuestionManage;
	@Autowired
	private ResconVendorUserManage vendorUserManage;
	@Autowired
	private PhaseConvert phaseConvert;
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private SubjectConvert subjectConvert;
	@Autowired
	private ResconKnowledgeSystemService knowledgeSystemService;
	@Autowired
	private ResconKnowledgePointService knowledgePointService;
	@Autowired
	private ResconExaminationPointService examinationPointService;

	/**
	 * 获取当前学科和知识code对应的列表和统计数
	 * 
	 * @param code
	 *            小专题Code
	 * @return
	 */
	@RequestMapping(value = "list", method = {RequestMethod.GET, RequestMethod.POST})
	public Value list(Long code, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "25") int pageSize) {
		Page<SpecialTraining> cp = resconSpecialTrainingService.list(P.index(page, pageSize), code);
		VPage<VSpecialTraining> vp = new VPage<VSpecialTraining>();
		int tPage = (int) (cp.getTotalCount() + pageSize - 1) / pageSize;
		vp.setPageSize(pageSize);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		vp.setItems(resconSpecialTrainingConvert.to(cp.getItems()));
		return new Value(vp);
	}

	/**
	 * 获取小专题对应的学科
	 * 
	 * @param code
	 *            小专题code
	 * @return
	 */
	@RequestMapping(value = "stat", method = {RequestMethod.GET, RequestMethod.POST})
	public Value stat(Long code, Long subjectCode) {
		Map<String, Long> data = Maps.newHashMap();
		Map<Integer, Long> map = Maps.newHashMap();
		// 表示点击了第二层
		if (code != null) {
			map = resconSpecialTrainingService.getStat(code);
			data.put("total", resconSpecialTrainingService.total(code));
		} else {
			// 表示没有选择小专题，默认进入
			map = resconSpecialTrainingService.getStatBySubject(subjectCode);
			data.put("total", resconSpecialTrainingService.getTotalBySubject(subjectCode));
		}
		// 已发布
		data.put("passNum", map.get(SpecialTrainingStatus.PASS.getValue()) == null
				? 0
				: map.get(SpecialTrainingStatus.PASS.getValue()).longValue());
		// 待发布
		data.put("nocheckNum", map.get(SpecialTrainingStatus.NOCHECK.getValue()) == null
				? 0
				: map.get(SpecialTrainingStatus.NOCHECK.getValue()).longValue());
		// 录入中
		data.put("editNum", map.get(SpecialTrainingStatus.EDITING.getValue()) == null
				? 0
				: map.get(SpecialTrainingStatus.EDITING.getValue()).longValue());
		return new Value(data);
	}

	/**
	 * 创建针对性训练，并返回符合条件的默认20道题和对应的统计数据等
	 * 
	 * @param form
	 *            页面提交数据
	 * @return
	 */
	@RequestMapping(value = "createSpecial", method = {RequestMethod.GET, RequestMethod.POST})
	public Value createSpecial(ResconSpecialTrainingForm form) {
		form.setUserId(Security.getUserId());
		VendorUser admin = vendorUserManage.getVendorUser(Security.getUserId());
		form.setVendorId(admin.getVendorId());
		return new Value(resconSpecialTrainingService.createSpecial(form));
	}

	/**
	 * 删除对应的题目
	 * 
	 * @param trainId
	 * @param questionId
	 * @return
	 */
	@RequestMapping(value = "deleteQuestion", method = {RequestMethod.GET, RequestMethod.POST})
	public Value deleteQuestion(Long trainId, Long questionId) {
		resconSpecialTrainingQuestionService.deleteQuestion(trainId, questionId);
		return new Value();
	}

	/**
	 * 获取专项训练的详细题目列表
	 * 
	 * @param trainId
	 *            训练ID
	 * @return
	 */
	@RequestMapping(value = "trainList", method = {RequestMethod.GET, RequestMethod.POST})
	public Value trainList(Long specialTrainingId, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "25") int pageSize) {
		Page<SpecialTrainingQuestion> cp = resconSpecialTrainingQuestionService
				.queryQuestionList(P.index(page, pageSize), specialTrainingId);
		VPage<VQuestion> vp = new VPage<VQuestion>();
		int tPage = (int) (cp.getTotalCount() + pageSize - 1) / pageSize;
		vp.setPageSize(pageSize);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		if (CollectionUtils.isNotEmpty(cp.getItems())) {
			List<Long> questionIds = Lists.newArrayList();
			for (SpecialTrainingQuestion s : cp.getItems()) {
				questionIds.add(s.getQuestionId());
			}
			vp.setItems(questionConvert.mgetList(questionIds));
		} else {
			vp.setItems(null);
		}
		return new Value(vp);
	}

	/**
	 * 获取训练题目的统计
	 * 
	 * @param specialTrainingId
	 * @return
	 */
	@RequestMapping(value = "getTrainStat", method = {RequestMethod.GET, RequestMethod.POST})
	public Value getTrainStat(Long specialTrainingId) {
		Map<Integer, Long> map = resconSpecialTrainingQuestionService.getQuestionStat(specialTrainingId);
		Map<String, Long> data = Maps.newHashMap();
		data.put("passNum", map.get(CheckStatus.PASS.getValue()) == null ? 0 : map.get(CheckStatus.PASS.getValue()));
		data.put("nocheckNum",
				map.get(CheckStatus.EDITING.getValue()) == null ? 0 : map.get(CheckStatus.EDITING.getValue()));
		data.put("onepassNum",
				map.get(CheckStatus.ONEPASS.getValue()) == null ? 0 : map.get(CheckStatus.ONEPASS.getValue()));
		data.put("nopassNum",
				map.get(CheckStatus.NOPASS.getValue()) == null ? 0 : map.get(CheckStatus.NOPASS.getValue()));
		data.put("draftNum", map.get(CheckStatus.DRAFT.getValue()) == null ? 0 : map.get(CheckStatus.DRAFT.getValue()));
		return new Value(data);
	}

	/**
	 * 创建或编辑针对性训练时，考点和知识点基础数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "baseData", method = {RequestMethod.GET, RequestMethod.POST})
	public Value baseData(Integer phaseCode, Integer subjectCode, Long knowpointCode) {
		Map<String, Object> data = Maps.newHashMap();
		List<ExaminationPoint> examinationPoints = resconExaminationPointService.findPoint(phaseCode, subjectCode,
				knowpointCode, Status.ENABLED);
		List<KnowledgePoint> knowPoints = resconKnowledgePointService.findAll(phaseCode, subjectCode, knowpointCode);
		data.put("examPoints", resconExaminationPointConvert.to(examinationPoints));
		data.put("knowPoints", resconKnowledgePointConvert.to(knowPoints));
		return new Value(data);
	}

	/**
	 * 获取训练对象
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "getTrain", method = {RequestMethod.GET, RequestMethod.POST})
	public Value getTrain(long id) {
		VSpecialTraining v = resconSpecialTrainingConvert.to(resconSpecialTrainingService.get(id));
		List<SpecialTrainingOperateLog> list = logService.findLogList(id);
		v.setLogList(logConvert.to(list));
		Long knowpointCode = v.getKnowpointCode();
		KnowledgeSystem ks = resconKnowledgeSystemService.get(knowpointCode);
		v.setPhase(phaseConvert.to(phaseService.get(ks.getPhaseCode())));
		v.setSubject(subjectConvert.to(subjectService.get(ks.getSubjectCode())));
		v.setCatalog(resconKnowledgePointService.getSmallSpecialCataLog(knowpointCode));
		return new Value(v);
	}

	/**
	 * 发布或者提交训练
	 * 
	 * @param status
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "updateStatus", method = {RequestMethod.GET, RequestMethod.POST})
	public Value updateStatus(SpecialTrainingStatus status, long id) {
		resconSpecialTrainingService.updateStatus(status, id, Security.getUserId());
		return new Value();
	}

	/**
	 * 验证添加的题目合法性(针对性从基础题库中添加题目功能)
	 * 
	 * @param qCodes
	 *            题目code集合
	 * @param trainId
	 *            针对性训练ID
	 * @return
	 */
	@RequestMapping(value = "checkQuestions", method = {RequestMethod.POST, RequestMethod.GET})
	public Value checkQuestions(@RequestParam(value = "qCodes", required = false) List<String> qCodes,
			@RequestParam(value = "trainId", required = false) Long trainId) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		SpecialTraining train = resconSpecialTrainingService.get(trainId);
		KnowledgeSystem ks = resconKnowledgeSystemService.get(train.getKnowpointCode());
		List<String> notExistCodes = new ArrayList<String>();
		List<String> errorSchoolCodes = Lists.newArrayList(); // 不能加入校本习题
		List<String> statusCodes = Lists.newArrayList(); // 只能添加已通过题目
		if (qCodes == null) {
			return new Value(data);
		}
		List<String> qCodeList = new ArrayList<String>();
		List<String> subjectWrongList = new ArrayList<String>();
		List<Question> questions = resconQuestionManage.findQuestionByCode(qCodes, null);
		List<Long> questionIds = new ArrayList<Long>();

		// 习题在训练中已经存在
		List<SpecialTrainingQuestion> trainQuestionList = resconSpecialTrainingQuestionService.questionList(trainId);
		List<String> existedCode = Lists.newArrayList();
		for (SpecialTrainingQuestion trainQuestion : trainQuestionList) {
			for (Question question : questions) {
				if (trainQuestion.getQuestionId() == question.getId()) {
					existedCode.add(question.getCode());
				}
			}
		}
		boolean flag = CollectionUtils.isEmpty(notExistCodes) && CollectionUtils.isEmpty(existedCode);
		for (Question question : questions) {
			qCodeList.add(question.getCode());
			// 不能输入其他学科的题目
			if (!question.getSubjectCode().equals(ks.getSubjectCode())) {
				subjectWrongList.add(question.getCode());
			} else if (question.getSchoolId() > 0) {
				errorSchoolCodes.add(question.getCode());
			} else {
				// 正常时把questionIds返回
				if (flag) {
					questionIds.add(question.getId());
					data.put("questionIds", questionIds);
				}
			}
			if (question.getStatus() != CheckStatus.PASS) {
				statusCodes.add(question.getCode());
			}
		}
		for (String long1 : qCodes) {
			if (!qCodeList.contains(long1)) {
				notExistCodes.add(long1);
			}
		}
		data.put("notExistCode", notExistCodes);
		data.put("subjectWrongList", subjectWrongList);
		data.put("existedCodes", existedCode);
		data.put("errorSchoolCodes", errorSchoolCodes);
		data.put("statusCodes", statusCodes);
		return new Value(data);
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
	public Value queryNewKnowledgeDatas(Integer phaseCode, Integer subjectCode, Long knowpointCode) {
		Map<String, Object> returnMap = new HashMap<String, Object>(4);
		List<KnowledgeSystem> knowledgeSystems = knowledgeSystemService.findSmallSpecial(phaseCode, subjectCode,
				knowpointCode);
		List<KnowledgePoint> knowledgePoints = knowledgePointService.findAll(phaseCode, subjectCode, knowpointCode);
		List<ExaminationPoint> examinationPoints = examinationPointService.listBySmallSpecailCode(phaseCode,
				subjectCode, knowpointCode);
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
		return new Value(returnMap);
	}
}

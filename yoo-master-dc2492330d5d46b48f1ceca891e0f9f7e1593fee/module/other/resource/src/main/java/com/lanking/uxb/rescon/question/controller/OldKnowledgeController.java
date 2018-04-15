package com.lanking.uxb.rescon.question.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.baseData.ExaminationPoint;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.domain.common.baseData.Knowpoint;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.rescon.account.api.ResconVendorUserManage;
import com.lanking.uxb.rescon.account.cache.ResconAccountCacheService;
import com.lanking.uxb.rescon.basedata.api.ResconExaminationPointService;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgePointService;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgeSystemService;
import com.lanking.uxb.rescon.common.ex.ResourceConsoleException;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;
import com.lanking.uxb.rescon.question.form.QuestionQueryForm;
import com.lanking.uxb.rescon.question.value.VQuestion;
import com.lanking.uxb.service.code.api.KnowpointService;
import com.lanking.uxb.service.code.api.MetaKnowpointService;
import com.lanking.uxb.service.search.api.IndexService;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 新旧知识点题目转换使用.
 * 
 * @since rescon 2016-04-01
 * @author wlche
 *
 */
@RestController
@RequestMapping("rescon/okn")
@RolesAllowed(userTypes = { "VENDOR_ADMIN", "VENDOR_HEAD", "VENDOR_CHECK" })
public class OldKnowledgeController {
	@Autowired
	private KnowpointService knowpointService;
	@Autowired
	private MetaKnowpointService metaKnowpointService;
	@Autowired
	private ResconKnowledgeSystemService knowledgeSystemService;
	@Autowired
	private ResconKnowledgePointService knowledgePointService;
	@Autowired
	private ResconAccountCacheService accountCacheService;
	@Autowired
	private ResconVendorUserManage vendorUserManage;
	@Autowired
	private ResconQuestionController questionController;
	@Autowired
	private ResconQuestionManage resconQuestionManage;
	@Autowired
	private IndexService indexService;
	@Autowired
	private ResconExaminationPointService examinationPointService;

	/**
	 * 获得新旧知识点树结构.
	 * 
	 * @param subjectCode
	 *            阶段学科编码
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "getKnowsTree")
	public Value getKnowsTree(Integer subjectCode) {
		Map<String, Object> returnMap = new HashMap<String, Object>(2);
		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());

		//
		Map<Integer, Integer> oldCounts1 = resconQuestionManage.calNoKnowledgeL1(subjectCode, user.getVendorId());
		Map<Integer, Integer> newCounts1 = resconQuestionManage.calHasKnowledgeL1(subjectCode, user.getVendorId());

		// 旧树
		List<Map> list = new ArrayList<Map>();
		List<Knowpoint> oldKnows = knowpointService.listBySubject(subjectCode);
		Map<Integer, Map> tempK = new HashMap<Integer, Map>(oldKnows.size());
		for (int i = 0; i < oldKnows.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			Knowpoint knowpoint = oldKnows.get(i);
			int counts = 0;
			if (oldCounts1.get(knowpoint.getCode()) != null) {
				counts = oldCounts1.get(knowpoint.getCode());
			}
			map.put("counts", counts);
			map.put("id", knowpoint.getCode());
			map.put("pId", knowpoint.getPcode());
			map.put("title", knowpoint.getName());
			map.put("name", counts > 0 ? knowpoint.getName() + "(" + counts + ")" : knowpoint.getName());
			map.put("nocheck", false);

			list.add(map);
			tempK.put(knowpoint.getCode(), map);
		}

		List<Map> oldMetaKnows = metaKnowpointService.listBySubject2(subjectCode);
		for (int i = 0; i < oldMetaKnows.size(); i++) {
			Map temp = oldMetaKnows.get(i);
			Integer pid = temp.get("pid") == null ? null : Integer.parseInt(temp.get("pid").toString());
			temp.put("pId", pid);
			if (pid != null && tempK.get(pid.intValue()) != null) {
				tempK.get(pid.intValue()).put("nocheck", true);
			}
		}
		for (int i = 0; i < oldKnows.size(); i++) {
			Knowpoint knowpoint = oldKnows.get(i);
			if (knowpoint.getPcode() != null) {
				if (tempK.get(knowpoint.getPcode().intValue()) != null) {
					tempK.get(knowpoint.getPcode().intValue()).put("nocheck", true);
				}
			}
		}
		list.addAll(oldMetaKnows);

		returnMap.put("oldTree", list); // 旧知识点树

		// 新树
		int phaseCode = 2;
		if (subjectCode == 302) {
			phaseCode = 3;
		}
		List<KnowledgeSystem> knowledgeSystems = knowledgeSystemService.findAll(phaseCode, subjectCode, null, null);
		List<KnowledgePoint> knowledgePoints = knowledgePointService.findAllByStatus(phaseCode, subjectCode,
				Status.ENABLED);
		List<ExaminationPoint> examinationPoints = examinationPointService.list(phaseCode, subjectCode, Status.ENABLED);
		List<Map<String, Object>> examinationPointTree = new ArrayList<Map<String, Object>>(knowledgeSystems.size()
				+ examinationPoints.size());
		List<Map<String, Object>> knowledgePointTree = new ArrayList<Map<String, Object>>(knowledgeSystems.size()
				+ knowledgePoints.size());
		for (int i = 0; i < knowledgeSystems.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			KnowledgeSystem knowledgeSystem = knowledgeSystems.get(i);
			int counts = 0;
			if (newCounts1.get(knowledgeSystem.getCode().intValue()) != null) {
				counts = newCounts1.get(knowledgeSystem.getCode().intValue());
			}
			map.put("counts", counts);
			map.put("id", knowledgeSystem.getCode());
			map.put("pId", knowledgeSystem.getPcode());
			map.put("title", knowledgeSystem.getName());
			map.put("name", counts > 0 ? knowledgeSystem.getName() + "(" + counts + ")" : knowledgeSystem.getName());
			map.put("nocheck", true);
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
			map.put("title", knowledgePoint.getName());
			map.put("nocheck", false);
			knowledgePointTree.add(map);
		}
		returnMap.put("newTree", knowledgePointTree); // 知识点树
		returnMap.put("knowledgePoints", knowledgePoints); // 元知识点列表

		// 考点
		for (int i = 0; i < examinationPoints.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			ExaminationPoint examinationPoint = examinationPoints.get(i);
			map.put("id", examinationPoint.getId());
			map.put("pId", examinationPoint.getPcode());
			map.put("name", examinationPoint.getName());
			map.put("title", examinationPoint.getName());
			map.put("nocheck", false); // 只有考点才能选择
			examinationPointTree.add(map);
		}
		returnMap.put("examinationPointTree", examinationPointTree); // 考点树
		returnMap.put("examinationPoints", examinationPoints); // 考点列表

		return new Value(returnMap);
	}

	/**
	 * 获取统计数据等.
	 * 
	 * @return
	 */
	@RequestMapping(value = "calDatas")
	public Value calDatas(Integer phaseCode) {
		if (phaseCode == null) {
			return new Value(new MissingArgumentException());
		}
		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
		Map<String, Integer> map = resconQuestionManage.calNewKnowledgeDatas(user.getVendorId(), user.getId(),
				phaseCode);
		return new Value(map);
	}

	/**
	 * 获得需要转换的题目.
	 * 
	 * @param form
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "getQuestion")
	public Value getQuestion(QuestionQueryForm form) {
		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());

		// 避免获取到他人正在校验的题目
		// List<Long> notHasQuestionIds = new ArrayList<Long>();
		// Map cacheMap = accountCacheService.getUserKnowledge();
		// cacheMap.remove(user.getId().toString());
		// for (Object questionId : cacheMap.values()) {
		// if (null != questionId) {
		// notHasQuestionIds.add(Long.parseLong(questionId.toString()));
		// }
		// }
		// if (notHasQuestionIds.size() > 0) {
		// form.setNotHasQuestionIds(notHasQuestionIds);
		// }

		form.setPageSize(1);
		VPage<VQuestion> vPage = (VPage<VQuestion>) questionController.query(form).getRet();
		VQuestion vQuestion = null;
		if (vPage.getItems().size() > 0) {
			vQuestion = vPage.getItems().get(0);

			// 更新缓存
			// accountCacheService.setUserKnowledge(user.getId(),
			// vQuestion.getId());
		}

		return new Value(vQuestion);
	}

	/**
	 * 保存题目新知识点.
	 * 
	 * @param questionId
	 *            习题ID
	 * @param ids
	 *            新知识点
	 * @param eids
	 *            考点
	 * @return
	 */
	@RequestMapping(value = "saveCorrect")
	public Value saveCorrect(Long questionId, Long[] ids, Long[] eids) {
		if (null == questionId || null == ids || ids.length == 0) {
			return new Value(new MissingArgumentException());
		}
		Question question = resconQuestionManage.get(questionId);
		if (null == question) {
			return new Value(new EntityNotFoundException());
		}

		try {
			resconQuestionManage.saveQuestionKnowledgePoint(questionId, Lists.newArrayList(ids));
			if (eids != null && eids.length > 0) {
				resconQuestionManage.saveQuestionExaminationPoint(questionId, Lists.newArrayList(eids));
			}
			resconQuestionManage.updateKnowledgeCreator(questionId, Security.getUserId());

			// 更新习题索引
			this.addQuestionIndex(questionId);

			// 更新统计数据
		} catch (ResourceConsoleException e) {
			return new Value(e);
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
}

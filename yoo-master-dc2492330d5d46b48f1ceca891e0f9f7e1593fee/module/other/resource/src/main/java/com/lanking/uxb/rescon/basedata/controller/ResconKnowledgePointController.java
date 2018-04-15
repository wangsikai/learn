package com.lanking.uxb.rescon.basedata.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgePointService;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgeSystemService;
import com.lanking.uxb.rescon.basedata.convert.ResconKnowledgePointConvert;

/**
 * 新知识点.
 * 
 * @author wlche
 * @since v2.0.1
 */
@RestController
@RequestMapping(value = "/rescon/newkp/")
public class ResconKnowledgePointController {
	@Autowired
	private ResconKnowledgePointService knowledgePointService;
	@Autowired
	private ResconKnowledgePointConvert knowledgePointConvert;
	@Autowired
	private ResconKnowledgeSystemService knowledgeSystemService;

	/**
	 * 根据学科阶段查询元知识点集合.
	 * 
	 * @param phaseCode
	 *            阶段
	 * @param subjectCode
	 *            学科
	 * @return
	 */
	@RequestMapping(value = "listKnowpoint", method = RequestMethod.POST)
	public Value listMetaKnowpoint(Integer phaseCode, Integer subjectCode) {
		List<KnowledgePoint> knowledgePoints = knowledgePointService.findAllByStatus(phaseCode, subjectCode, null);

		List<Map<String, Object>> list = Lists.newArrayList();
		for (KnowledgePoint knowledgePoint : knowledgePoints) {
			Map<String, Object> map = new HashMap<String, Object>(3);
			map.put("code", knowledgePoint.getCode());
			map.put("pcode", knowledgePoint.getPcode());
			map.put("name", knowledgePoint.getName());
			list.add(map);
		}
		return new Value(list);
	}

	/**
	 * 获得使用的知识点树列表.
	 * 
	 * @return
	 */
	@RequestMapping(value = "getQueSelKnows", method = RequestMethod.POST)
	public Value getQueSelKnows(Integer phaseCode, Integer subjectCode) {
		Map<String, Object> returnMap = new HashMap<String, Object>(2);
		List<KnowledgeSystem> knowledgeSystems = knowledgeSystemService.findAll(phaseCode, subjectCode, null, null);
		List<KnowledgePoint> knowledgePoints = knowledgePointService.findAllByStatus(phaseCode, subjectCode, null);
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>(knowledgeSystems.size());

		for (int i = 0; i < knowledgeSystems.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			KnowledgeSystem knowledgeSystem = knowledgeSystems.get(i);
			map.put("id", knowledgeSystem.getCode());
			map.put("pId", knowledgeSystem.getPcode());
			map.put("name", knowledgeSystem.getName());
			map.put("nocheck", true); // 只有原知识点才能选择
			datas.add(map);
		}
		for (int i = 0; i < knowledgePoints.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			KnowledgePoint knowledgePoint = knowledgePoints.get(i);
			map.put("id", knowledgePoint.getCode());
			map.put("pId", knowledgePoint.getPcode());
			map.put("name", knowledgePoint.getName());
			map.put("nocheck", false); // 只有原知识点才能选择
			datas.add(map);
		}
		returnMap.put("tree", datas); // 树
		returnMap.put("knowledgePoints", knowledgePoints); // 元知识点
		return new Value(returnMap);
	}
}

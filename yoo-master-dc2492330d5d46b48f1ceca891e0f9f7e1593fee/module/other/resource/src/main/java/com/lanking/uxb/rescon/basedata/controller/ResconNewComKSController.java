package com.lanking.uxb.rescon.basedata.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgePointService;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgeSystemService;
import com.lanking.uxb.rescon.basedata.convert.ResconKnowledgePointConvert;
import com.lanking.uxb.rescon.basedata.convert.ResconKnowledgeSystemConvert;

/**
 * 综合知识体系
 * 
 * @author wangsenhao
 * @since 2.0.1
 */
@RestController
@RequestMapping(value = "rescon/newKnow")
public class ResconNewComKSController {
	@Autowired
	private ResconKnowledgePointService resconNewComKsService;
	@Autowired
	private ResconKnowledgeSystemConvert ksConvert;
	@Autowired
	private ResconKnowledgePointConvert kpConvert;
	@Autowired
	private ResconKnowledgeSystemService knowledgeSystemService;

	/**
	 * 根据条件查询对应的知识体系列表
	 * 
	 * @param phaseCode
	 * @param subjectCode
	 * @param pcode
	 *            父级code
	 * @param level
	 *            等级(公共的三级在knowledge_system里)
	 * @return
	 */
	@RequestMapping(value = "findAll", method = { RequestMethod.GET, RequestMethod.POST })
	public Value findAll(Integer phaseCode, Integer subjectCode,
			@RequestParam(value = "pcode", defaultValue = "0") Long pcode,
			@RequestParam(value = "level", defaultValue = "0") Integer level) {
		// 查询知识点
		if (level == 3) {
			List<KnowledgePoint> list = resconNewComKsService.findPoint(phaseCode, subjectCode, pcode, null);
			return new Value(kpConvert.to(list));
		} else {
			List<KnowledgeSystem> list = knowledgeSystemService.findAll(phaseCode, subjectCode, pcode, level);
			return new Value(ksConvert.to(list));
		}
	}

	/**
	 * 根据阶段科目查询对应的统计数据
	 * 
	 * @param phaseCode
	 * @param subjectCode
	 * @return
	 */
	@RequestMapping(value = "getStat", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getStat(Integer phaseCode, Integer subjectCode) {
		Map<Integer, Long> map = knowledgeSystemService.getStat(phaseCode, subjectCode);
		Map<String, Long> data = Maps.newHashMap();
		data.put("special", map.get(1) == null ? 0 : map.get(1).longValue());
		data.put("samllSpecial", map.get(2) == null ? 0 : map.get(2).longValue());
		data.put("knowledge", map.get(3) == null ? 0 : map.get(3).longValue());
		data.put("point", resconNewComKsService.getPointCount(phaseCode, subjectCode));
		return new Value(data);
	}
}

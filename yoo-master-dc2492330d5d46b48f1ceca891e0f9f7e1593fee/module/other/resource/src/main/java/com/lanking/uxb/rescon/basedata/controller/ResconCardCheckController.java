package com.lanking.uxb.rescon.basedata.controller;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.ExaminationPoint;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.resource.card.CardStatus;
import com.lanking.cloud.domain.common.resource.card.ExaminationPointCard;
import com.lanking.cloud.domain.common.resource.card.KnowledgePointCard;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.search.api.IndexService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.lanking.uxb.rescon.basedata.api.ResconExaminationPointCardService;
import com.lanking.uxb.rescon.basedata.api.ResconExaminationPointService;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgePointCardService;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgePointService;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgeSystemService;
import com.lanking.uxb.rescon.basedata.convert.ResconExaminationPointCardConvert;
import com.lanking.uxb.rescon.basedata.convert.ResconKnowledgePointCardConvert;

/**
 * 卡片校验
 * 
 * @author wangsenhao
 * @since 2.0.1
 *
 */
@RestController
@RequestMapping(value = "/rescon/card")
public class ResconCardCheckController {
	@Autowired
	private ResconExaminationPointCardService examService;
	@Autowired
	private ResconKnowledgePointCardService knowService;
	@Autowired
	private ResconExaminationPointCardConvert examConvert;
	@Autowired
	private ResconKnowledgePointCardConvert knowConvert;
	@Autowired
	private ResconKnowledgePointService resconKnowledgePointService;
	@Autowired
	private ResconKnowledgeSystemService resconKnowledgeSystemService;
	@Autowired
	private ResconExaminationPointService resconExaminationPointService;
	@Autowired
	private IndexService indexService;

	/**
	 * 卡片校验页面数据展示
	 * 
	 * @param type
	 *            卡片类型
	 * @param id
	 *            卡片ID
	 */
	@RequestMapping(value = "list", method = { RequestMethod.GET, RequestMethod.POST })
	public Value list(Integer type, Long id) {
		Map<String, Object> data = Maps.newHashMap();
		// 知识点卡片
		if (1 == type) {
			KnowledgePointCard kc = knowService.get(id);
			KnowledgePoint k = resconKnowledgePointService.get(kc.getKnowpointCode());
			data.put("data", knowConvert.to(kc));
			data.put("catalog", resconKnowledgePointService.getLevelDesc(kc.getKnowpointCode()));
			data.put("phaseCode", k.getPhaseCode());
		}
		// 考点卡片
		if (2 == type) {
			ExaminationPointCard ec = examService.get(id);
			ExaminationPoint k1 = resconExaminationPointService.get(ec.getExaminationPointId());
			data.put("data", examConvert.to(ec));
			data.put("catalog", resconExaminationPointService.getLevelDesc(ec.getExaminationPointId()));
			data.put("name", k1.getName());// 考点卡片名字就是卡片的名字
			data.put("phaseCode", k1.getPhaseCode());
		}
		return new Value(data);
	}

	/**
	 * 校验卡片
	 * 
	 * @param type
	 * @param id
	 * @param status
	 *            卡片状态
	 * @return
	 */
	@RequestMapping(value = "check", method = { RequestMethod.GET, RequestMethod.POST })
	public Value check(Integer type, Long id, CardStatus status) {
		// 知识点卡片
		if (1 == type) {
			List<Long> needUpdateQuestions = knowService.updateCardStatus(id, status);
			indexService.update(IndexType.QUESTION, needUpdateQuestions);
		}
		// 考点卡片
		if (2 == type) {
			examService.updateCardStatus(id, status);
		}
		return new Value();
	}
}

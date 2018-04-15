package com.lanking.uxb.service.common.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.baseData.ExaminationPointKnowledgePoint;
import com.lanking.cloud.domain.common.resource.card.CardStatus;
import com.lanking.cloud.domain.common.resource.card.KnowledgePointCard;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.ExaminationPointKnowledgePointService;
import com.lanking.uxb.service.code.api.ExaminationPointService;
import com.lanking.uxb.service.code.api.KnowledgePointCardService;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.convert.ExaminationPointConvert;
import com.lanking.uxb.service.code.convert.KnowledgePointCardConvert;
import com.lanking.uxb.service.code.convert.KnowledgePointConvert;
import com.lanking.uxb.service.code.value.VKnowledgePointCard;
import com.lanking.uxb.service.common.convert.QuestionBaseConvert;
import com.lanking.uxb.service.common.convert.QuestionBaseConvertOption;
import com.lanking.uxb.service.common.value.VQuestionBase;
import com.lanking.uxb.service.question.api.QuestionService;

/**
 * @since 2.3.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年8月22日
 */
@RestController
@RequestMapping("zy/knowledgePointCard")
public class ZuoyeKnowledgePointCardController {

	@Autowired
	private KnowledgePointCardService knowledgePointCardService;
	@Autowired
	private KnowledgePointCardConvert knowledgePointCardConvert;
	@Autowired
	private KnowledgePointService knowledgePointService;
	@Autowired
	private KnowledgePointConvert knowledgePointConvert;
	@Autowired
	private ExaminationPointKnowledgePointService examinationPointKnowledgePointService;
	@Autowired
	private ExaminationPointService examinationPointService;
	@Autowired
	private ExaminationPointConvert examinationPointConvert;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionBaseConvert<VQuestionBase> questionConvert;

	@SuppressWarnings("unchecked")
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "info", method = { RequestMethod.GET, RequestMethod.POST })
	public Value info(Long id) {
		if (id == null || id <= 0) {
			return new Value(ValueMap.value());
		}
		KnowledgePointCard card = knowledgePointCardService.get(id);
		if (card != null && card.getCheckStatus() == CardStatus.PASS && card.getDelStatus() == Status.ENABLED) {
			VKnowledgePointCard vcard = knowledgePointCardConvert.to(card);
			// 知识点
			vcard.setKnowledgePoint(knowledgePointConvert.to(knowledgePointService.get(vcard.getKnowpointCode())));
			// 相关知识卡片
			List<KnowledgePointCard> relateds = knowledgePointCardService
					.findByKnowledgePoint(vcard.getKnowpointCode());
			if (relateds != null && relateds.size() > 1) {
				List<KnowledgePointCard> _relateds = new ArrayList<KnowledgePointCard>(3);
				int size = 0;
				for (KnowledgePointCard c : relateds) {
					if (c.getId().longValue() != id.longValue() && c.getCheckStatus() == CardStatus.PASS
							&& c.getDelStatus() == Status.ENABLED) {
						_relateds.add(c);
						size++;
					}
					if (size == 3) {
						break;
					}
				}
				vcard.setRelated(knowledgePointCardConvert.to(_relateds));
			} else {
				vcard.setRelated(Collections.EMPTY_LIST);
			}
			// 相关考点
			List<ExaminationPointKnowledgePoint> examinationPointKnowledgePoints = examinationPointKnowledgePointService
					.findByKnowledgePoint(vcard.getKnowpointCode());
			if (CollectionUtils.isNotEmpty(examinationPointKnowledgePoints)) {
				List<Long> examinationPointIds = new ArrayList<Long>(examinationPointKnowledgePoints.size());
				for (ExaminationPointKnowledgePoint epkp : examinationPointKnowledgePoints) {
					examinationPointIds.add(epkp.getExaminationPointId());
				}
				vcard.setExaminationPointIds(examinationPointIds);
				vcard.setExaminationPoint(examinationPointConvert.to(examinationPointService
						.mgetList(examinationPointIds)));
			} else {
				vcard.setExaminationPointIds(Collections.EMPTY_LIST);
				vcard.setExaminationPoint(Collections.EMPTY_LIST);
			}
			// 关联题目
			if (CollectionUtils.isNotEmpty(vcard.getQuestionIds())) {
				Map<Long, VQuestionBase> questionBaseMap = questionConvert.to(questionService.mget(vcard
						.getQuestionIds()), new QuestionBaseConvertOption(false, true, true, false));
				vcard.setQuestions(new ArrayList<Object>(questionBaseMap.size()));
				for (Long qId : vcard.getQuestionIds()) {
					vcard.getQuestions().add(questionBaseMap.get(qId));
				}
			} else {
				vcard.setQuestions(Collections.EMPTY_LIST);
			}
			return new Value(ValueMap.value("card", vcard));
		}
		return new Value(ValueMap.value());
	}
}

package com.lanking.uxb.rescon.basedata.convert;

import com.lanking.cloud.domain.common.resource.card.CardStatus;
import com.lanking.cloud.domain.common.resource.card.KnowledgePointCard;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.basedata.value.VResconKnowledgePointCard;
import com.lanking.uxb.rescon.question.convert.ResconQuestionConvert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ResconKnowledgePointCard -> VResconKnowledgePointCard
 *
 * @author xinyu.zhou
 * @since 2.0.1
 */
@Component
public class ResconKnowledgePointCardConvert extends Converter<VResconKnowledgePointCard, KnowledgePointCard, Long> {
	@Autowired
	private ResconQuestionConvert questionConvert;

	@Override
	protected Long getId(KnowledgePointCard knowledgePointCard) {
		return knowledgePointCard.getId();
	}

	@Override
	protected VResconKnowledgePointCard convert(KnowledgePointCard knowledgePointCard) {
		VResconKnowledgePointCard v = new VResconKnowledgePointCard();
		v.setCheckStatus(knowledgePointCard.getCheckStatus());
		v.setCreateAt(knowledgePointCard.getCreateAt());
		v.setDescription(knowledgePointCard.getDescription());
		v.setHints(knowledgePointCard.getHints());
		v.setId(knowledgePointCard.getId());
		v.setUpdateAt(knowledgePointCard.getUpdateAt());
		v.setName(knowledgePointCard.getName());
		v.setStatus(knowledgePointCard.getDelStatus());
		String detailDescription = knowledgePointCard.getDetailDescription();
		v.setDetailDescription(detailDescription == null ? "" : detailDescription);

		switch (v.getCheckStatus()) {
		case DRAFT:
			v.setCheckStatusTitle("草稿");
			break;
		case NOPASS:
			v.setCheckStatusTitle("不通过");
			break;
		case EDITING:
			v.setCheckStatusTitle("未校验");
			break;
		case PASS:
			v.setCheckStatusTitle("已通过");
			break;
		default:
			v.setCheckStatusTitle("");
		}

		v.setQuestions(questionConvert.mgetList(knowledgePointCard.getQuestions()));
		return v;
	}
}

package com.lanking.uxb.rescon.basedata.convert;

import com.lanking.cloud.domain.common.resource.card.ExaminationPointCard;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.basedata.value.VResconExaminationPointCard;

import org.springframework.stereotype.Component;

/**
 * @author xinyu.zhou
 * @since 2.0.1
 */
@Component
public class ResconExaminationPointCardConvert extends
		Converter<VResconExaminationPointCard, ExaminationPointCard, Long> {

	@Override
	protected Long getId(ExaminationPointCard examinationPointCard) {
		return examinationPointCard.getId();
	}

	@Override
	protected VResconExaminationPointCard convert(ExaminationPointCard examinationPointCard) {
		VResconExaminationPointCard v = new VResconExaminationPointCard();
		v.setCheckStatus(examinationPointCard.getCheckStatus());
		v.setCreateAt(examinationPointCard.getCreateAt());
		v.setUpdateAt(examinationPointCard.getUpdateAt());
		v.setId(examinationPointCard.getId());
		v.setDescription(examinationPointCard.getDescription());
		v.setExaminationId(examinationPointCard.getExaminationPointId());

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

		return v;
	}
}

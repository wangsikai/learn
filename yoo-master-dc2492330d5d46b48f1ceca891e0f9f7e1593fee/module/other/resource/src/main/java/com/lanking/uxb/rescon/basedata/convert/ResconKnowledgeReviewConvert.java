package com.lanking.uxb.rescon.basedata.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.KnowledgeReview;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.basedata.value.VResconKnowledgeReview;

/**
 * 复习知识点convert
 * 
 *
 */
@Component
public class ResconKnowledgeReviewConvert extends Converter<VResconKnowledgeReview, KnowledgeReview, Long> {

	@Override
	protected Long getId(KnowledgeReview s) {
		return s.getCode();
	}

	@Override
	protected VResconKnowledgeReview convert(KnowledgeReview s) {
		if (s != null) {
			VResconKnowledgeReview v = new VResconKnowledgeReview();
			v.setCode(s.getCode());
			v.setLevel(s.getLevel());
			v.setName(s.getName());
			v.setPcode(s.getPcode());
			v.setPhaseCode(s.getPhaseCode());
			v.setSequence(s.getSequence());
			v.setStatus(s.getStatus());
			v.setStudyDifficulty(s.getStudyDifficulty());
			v.setSubjectCode(s.getSubjectCode());
			return v;
		}
		return null;
	}

}

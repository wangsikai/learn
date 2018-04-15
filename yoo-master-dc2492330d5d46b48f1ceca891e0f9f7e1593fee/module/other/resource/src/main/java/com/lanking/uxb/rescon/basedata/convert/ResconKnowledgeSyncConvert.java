package com.lanking.uxb.rescon.basedata.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.KnowledgeSync;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.basedata.value.VResconKnowledgeSync;

/**
 * 同步知识点convert
 * 
 *
 */
@Component
public class ResconKnowledgeSyncConvert extends Converter<VResconKnowledgeSync, KnowledgeSync, Long> {

	@Override
	protected Long getId(KnowledgeSync s) {
		return s.getCode();
	}

	@Override
	protected VResconKnowledgeSync convert(KnowledgeSync s) {
		if (s != null) {
			VResconKnowledgeSync v = new VResconKnowledgeSync();
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

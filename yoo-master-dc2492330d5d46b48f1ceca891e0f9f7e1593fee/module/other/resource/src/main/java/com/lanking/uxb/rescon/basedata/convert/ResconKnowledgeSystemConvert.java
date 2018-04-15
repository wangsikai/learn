package com.lanking.uxb.rescon.basedata.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.basedata.value.VKnowledgeSystem;

/**
 * 知识体系convert
 * 
 * @since 2.0.1
 * @author wangsenhao
 *
 */
@Component
public class ResconKnowledgeSystemConvert extends Converter<VKnowledgeSystem, KnowledgeSystem, Long> {

	@Override
	protected Long getId(KnowledgeSystem s) {
		return s.getCode();
	}

	@Override
	protected VKnowledgeSystem convert(KnowledgeSystem s) {
		VKnowledgeSystem v = new VKnowledgeSystem();
		v.setId(s.getCode());
		v.setName(s.getName());
		v.setLevel(s.getLevel());
		v.setPcode(s.getPcode());
		return v;
	}

}

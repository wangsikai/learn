package com.lanking.uxb.rescon.basedata.convert;

import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.basedata.api.ResconTTSType;
import com.lanking.uxb.rescon.basedata.value.VResconTTS;

import org.springframework.stereotype.Component;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@Component
public class ResconSectionConvert extends Converter<VResconTTS, Section, Long> {
	@Override
	protected Long getId(Section section) {
		return section.getCode();
	}

	@Override
	protected VResconTTS convert(Section section) {
		VResconTTS v = new VResconTTS();
		v.setId(section.getCode());
		if (section.getLevel() == 1) {
			v.setParent(section.getTextbookCode().longValue());
		} else {
			v.setParent(section.getPcode());
		}
		v.setName(section.getName());
		v.setType(ResconTTSType.SECTION);
		v.setLevel(section.getLevel());
		v.setPcode2(section.getTextbookCode());
		v.setSequence(section.getSequence());
		return v;
	}
}

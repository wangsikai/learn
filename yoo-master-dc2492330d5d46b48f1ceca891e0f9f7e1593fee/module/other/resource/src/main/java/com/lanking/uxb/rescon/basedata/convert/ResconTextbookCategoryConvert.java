package com.lanking.uxb.rescon.basedata.convert;

import com.lanking.cloud.domain.common.baseData.TextbookCategory;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.basedata.api.ResconTTSType;
import com.lanking.uxb.rescon.basedata.value.VResconTTS;

import org.springframework.stereotype.Component;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@Component
public class ResconTextbookCategoryConvert extends Converter<VResconTTS, TextbookCategory, Integer> {

	@Override
	protected Integer getId(TextbookCategory textbookCategory) {
		return textbookCategory.getCode();
	}

	@Override
	protected VResconTTS convert(TextbookCategory textbookCategory) {
		VResconTTS v = new VResconTTS();
		v.setId(textbookCategory.getCode().longValue());
		v.setParent(-1L);
		v.setName(textbookCategory.getName());
		v.setType(ResconTTSType.TEXTBOOKCATEGORY);
		v.setLevel(1);
		v.setSequence(textbookCategory.getSequence());
		return v;
	}
}

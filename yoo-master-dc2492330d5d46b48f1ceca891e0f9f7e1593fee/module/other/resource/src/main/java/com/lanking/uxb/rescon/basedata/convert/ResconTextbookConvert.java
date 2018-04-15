package com.lanking.uxb.rescon.basedata.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.basedata.api.ResconTTSType;
import com.lanking.uxb.rescon.basedata.value.VResconTTS;
import com.lanking.uxb.service.file.util.FileUtil;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@Component
public class ResconTextbookConvert extends Converter<VResconTTS, Textbook, Integer> {
	@Override
	protected Integer getId(Textbook textbook) {
		return textbook.getCode();
	}

	@Override
	protected VResconTTS convert(Textbook textbook) {
		VResconTTS v = new VResconTTS();
		v.setType(ResconTTSType.TEXTBOOK);
		v.setName(textbook.getName());
		v.setParent(textbook.getCategoryCode().longValue());
		v.setPhaseCode(textbook.getPhaseCode());
		v.setSubjectCode(textbook.getSubjectCode());
		v.setId(textbook.getCode().longValue());
		v.setSequence(textbook.getSequence());
		// 书本图标
		v.setIconUrl(FileUtil.getUrl(textbook.getIcon()));
		return v;
	}
}

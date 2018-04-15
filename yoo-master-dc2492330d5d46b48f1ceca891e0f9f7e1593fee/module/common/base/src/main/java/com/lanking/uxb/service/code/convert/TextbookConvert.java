package com.lanking.uxb.service.code.convert;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.file.util.FileUtil;

@Component
public class TextbookConvert extends Converter<VTextbook, Textbook, Integer> {
	@Autowired
	private TextbookService textbookService;

	@Override
	protected Integer getId(Textbook s) {
		return s.getCode();
	}

	@Override
	protected VTextbook convert(Textbook s) {
		VTextbook v = new VTextbook();
		v.setCode(s.getCode());
		v.setCategoryCode(s.getCategoryCode());
		v.setPhaseCode(s.getPhaseCode());
		v.setSubjectCode(s.getSubjectCode());
		v.setName(s.getName());
		v.setIcon(FileUtil.getUrl(s.getIcon()));
		v.setSequence(s.getSequence());
		v.setUrl("/images/categories/" + s.getCategoryCode().toString() + "/" + s.getCode() + ".jpg");
		return v;
	}

	@Override
	protected Textbook internalGet(Integer id) {
		return textbookService.get(id);
	}

	@Override
	protected Map<Integer, Textbook> internalMGet(Collection<Integer> ids) {
		return textbookService.mget(ids);
	}

}

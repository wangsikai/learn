package com.lanking.uxb.service.code.convert;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.TextbookCategory;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.value.VTextbookCategory;

/**
 * 教材分类.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年3月4日
 */
@Component
public class TextbookCategoryConvert extends Converter<VTextbookCategory, TextbookCategory, Integer> {

	@Autowired
	private TextbookCategoryService textbookCategoryService;

	@Override
	protected Integer getId(TextbookCategory s) {
		return s.getCode();
	}

	@Override
	protected VTextbookCategory convert(TextbookCategory s) {
		VTextbookCategory v = new VTextbookCategory();
		v.setCode(s.getCode());
		v.setSequence(s.getSequence());
		v.setName(s.getName());
		return v;
	}

	@Override
	protected TextbookCategory internalGet(Integer id) {
		return textbookCategoryService.get(id);
	}

	@Override
	protected Map<Integer, TextbookCategory> internalMGet(Collection<Integer> ids) {
		return textbookCategoryService.mget(ids);
	}

}

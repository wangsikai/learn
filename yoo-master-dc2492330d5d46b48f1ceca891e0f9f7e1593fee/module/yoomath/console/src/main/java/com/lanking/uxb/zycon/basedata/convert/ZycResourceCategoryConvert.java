package com.lanking.uxb.zycon.basedata.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.ResourceCategory;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.zycon.basedata.value.ZycResourceCategory;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@Component
public class ZycResourceCategoryConvert extends Converter<ZycResourceCategory, ResourceCategory, Integer> {
	@Override
	protected Integer getId(ResourceCategory resourceCategory) {
		return resourceCategory.getCode();
	}

	@Override
	protected ZycResourceCategory convert(ResourceCategory resourceCategory) {
		ZycResourceCategory v = new ZycResourceCategory();
		v.setId(resourceCategory.getCode());
		v.setParent(resourceCategory.getPcode());
		v.setName(resourceCategory.getName());
		v.setSequence(resourceCategory.getSequence());
		return v;
	}
}

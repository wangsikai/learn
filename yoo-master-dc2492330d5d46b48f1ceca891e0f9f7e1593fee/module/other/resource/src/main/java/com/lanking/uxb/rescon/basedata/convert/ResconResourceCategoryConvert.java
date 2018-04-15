package com.lanking.uxb.rescon.basedata.convert;

import com.lanking.cloud.domain.common.baseData.ResourceCategory;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.basedata.value.VResconResourceCategory;

import org.springframework.stereotype.Component;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@Component
public class ResconResourceCategoryConvert extends Converter<VResconResourceCategory, ResourceCategory, Integer> {
	@Override
	protected Integer getId(ResourceCategory resourceCategory) {
		return resourceCategory.getCode();
	}

	@Override
	protected VResconResourceCategory convert(ResourceCategory resourceCategory) {
		VResconResourceCategory v = new VResconResourceCategory();
		v.setId(resourceCategory.getCode());
		v.setParent(resourceCategory.getPcode());
		v.setName(resourceCategory.getName());
		v.setSequence(resourceCategory.getSequence());
		return v;
	}
}

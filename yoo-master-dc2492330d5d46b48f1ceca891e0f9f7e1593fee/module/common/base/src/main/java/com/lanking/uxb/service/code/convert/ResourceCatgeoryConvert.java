package com.lanking.uxb.service.code.convert;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.ResourceCategory;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.code.api.ResourceCategoryService;
import com.lanking.uxb.service.code.value.VResourceCategory;

@Component
public class ResourceCatgeoryConvert extends Converter<VResourceCategory, ResourceCategory, Integer> {

	@Autowired
	private ResourceCategoryService resourceCategoryService;

	@Override
	protected Integer getId(ResourceCategory s) {
		return s.getCode();
	}

	@Override
	protected VResourceCategory convert(ResourceCategory s) {
		VResourceCategory v = new VResourceCategory();
		v.setCode(s.getCode());
		v.setName(s.getName());
		v.setpCode(s.getPcode());
		return v;
	}

	@Override
	protected ResourceCategory internalGet(Integer id) {
		return resourceCategoryService.getResCategory(id);
	}

	@Override
	protected Map<Integer, ResourceCategory> internalMGet(Collection<Integer> ids) {
		return resourceCategoryService.getcategories(ids);
	}

}

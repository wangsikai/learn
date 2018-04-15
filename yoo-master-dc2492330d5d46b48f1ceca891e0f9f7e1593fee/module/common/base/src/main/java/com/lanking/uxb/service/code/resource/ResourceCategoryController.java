package com.lanking.uxb.service.code.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.code.api.ResourceCategoryService;
import com.lanking.uxb.service.code.convert.ResourceCatgeoryConvert;

/**
 * 资源标签controller
 *
 * @author xinyu.zhou
 * @since V2.1
 */
@RestController
@RequestMapping(value = "common/resourceCategory")
public class ResourceCategoryController {

	@Autowired
	private ResourceCategoryService resourceCategoryService;
	@Autowired
	private ResourceCatgeoryConvert resourceCatgeoryConvert;

	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public Value query(@RequestParam(value = "category") Integer category) {
		return new Value(resourceCatgeoryConvert.to(resourceCategoryService.getResCategory(category)));
	}

	@RequestMapping(value = "query_by_parent", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryByParent(@RequestParam(value = "pcode", defaultValue = "-1") Integer pcode) {
		return new Value(resourceCatgeoryConvert.to(resourceCategoryService.findCategoryByParent(pcode)));
	}

}

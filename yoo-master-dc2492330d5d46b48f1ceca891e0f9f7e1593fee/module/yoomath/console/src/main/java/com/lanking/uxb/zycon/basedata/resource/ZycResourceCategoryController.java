package com.lanking.uxb.zycon.basedata.resource;

import java.util.List;

import com.lanking.cloud.domain.common.baseData.ResourceCategory;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.adminSecurity.support.ConsoleRolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.uxb.zycon.basedata.api.ZycResourceCategoryService;
import com.lanking.uxb.zycon.basedata.convert.ZycResourceCategoryConvert;
import com.lanking.uxb.zycon.basedata.value.ZycResourceCategory;

/**
 * 精品试卷管理
 * 
 * @since V2.0.7
 * @author zemin.song
 *
 */
@RestController
@RequestMapping("zyc/goods/ep")
public class ZycResourceCategoryController {
	@Autowired
	private ZycResourceCategoryService ZycResourceCategoryService;

	@Autowired
	private ZycResourceCategoryConvert convert;

	/**
	 * 查询所有教材
	 *
	 * @return {@link Value}
	 */
	@ConsoleRolesAllowed(roleCodes = { "ZYPG", "ZYADMIN" })
	@RequestMapping(value = "findAll", method = { RequestMethod.GET, RequestMethod.POST })
	public Value findAll() {
		List<ResourceCategory> categoryList = ZycResourceCategoryService.findAll();
		List<ZycResourceCategory> vResourceCategories = convert.to(categoryList);
		return new Value(vResourceCategories);
	}

}

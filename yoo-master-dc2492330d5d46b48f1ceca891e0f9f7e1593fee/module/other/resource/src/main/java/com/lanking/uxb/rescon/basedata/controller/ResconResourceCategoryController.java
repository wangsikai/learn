package com.lanking.uxb.rescon.basedata.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.common.baseData.ResourceCategory;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.rescon.basedata.api.ResconResourceCategoryService;
import com.lanking.uxb.rescon.basedata.convert.ResconResourceCategoryConvert;
import com.lanking.uxb.rescon.basedata.form.ResconResourceCategoryForm;
import com.lanking.uxb.rescon.basedata.value.VResconResourceCategory;

/**
 * ResourceCategory Controller
 *
 * @author xinyu.zhou
 * @since V2.1
 */
@RestController
@RequestMapping(value = "rescon/rs")
public class ResconResourceCategoryController {

	@Autowired
	private ResconResourceCategoryService resconResourceCategoryService;
	@Autowired
	private ResconResourceCategoryConvert convert;

	/**
	 * 保存Resource Category
	 *
	 * @param formStr
	 *            JSON序列化后的{@link ResconResourceCategoryForm}
	 * @return {@link Value}
	 */
	@RequestMapping(value = "save", method = { RequestMethod.GET, RequestMethod.POST })
	public Value save(@RequestParam(value = "formStr") String formStr) {
		if (StringUtils.isBlank(formStr)) {
			return new Value(new IllegalArgException());
		}

		ResconResourceCategoryForm form = JSONObject.parseObject(formStr, ResconResourceCategoryForm.class);
		resconResourceCategoryService.save(form);
		return new Value();
	}

	/**
	 * 查询所有教材
	 *
	 * @return {@link Value}
	 */
	@RequestMapping(value = "findAll", method = { RequestMethod.GET, RequestMethod.POST })
	public Value findAll() {
		List<ResourceCategory> categoryList = resconResourceCategoryService.findAll();
		List<VResconResourceCategory> vResourceCategories = convert.to(categoryList);
		return new Value(vResourceCategories);
	}

	/**
	 * 同步
	 */
	@RequestMapping(value = "syncData", method = { RequestMethod.GET, RequestMethod.POST })
	public Value syncData() {
		resconResourceCategoryService.syncData();
		return new Value();
	}

	/**
	 * 保存排序值
	 *
	 * @param code
	 *            资源类别code
	 * @param sequence
	 *            新的排序值
	 * @return {@link Value}
	 */
	@RequestMapping(value = "saveSequence", method = { RequestMethod.GET, RequestMethod.POST })
	public Value sequence(int code, int sequence) {
		resconResourceCategoryService.updateSequence(code, sequence);

		return new Value();
	}

	/**
	 * 批量更新排序值
	 *
	 * @param formStr
	 *            需要更新的排序对象
	 * @return {@link Value}
	 */
	@RequestMapping(value = "update_sequences", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updateSequences(@RequestParam(value = "formStr") String formStr) {
		List<ResconResourceCategoryForm> forms = JSONObject.parseArray(formStr, ResconResourceCategoryForm.class);

		resconResourceCategoryService.updateSequence(forms);

		return new Value();
	}
}

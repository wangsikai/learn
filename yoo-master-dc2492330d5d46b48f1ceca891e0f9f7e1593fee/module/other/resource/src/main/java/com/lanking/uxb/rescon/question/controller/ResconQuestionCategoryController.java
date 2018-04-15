package com.lanking.uxb.rescon.question.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.baseData.QuestionCategory;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.rescon.question.api.ResconQuestionCategoryManage;
import com.lanking.uxb.rescon.question.convert.ResconQuestionCategoryConvert;

/**
 * 习题类别（场景）管理.
 * 
 * @author wlche
 *
 */
@RestController
@RequestMapping("rescon/que/category")
@RolesAllowed(userTypes = { "VENDOR_ADMIN", "VENDOR_HEAD", "VENDOR_BUILD", "VENDOR_CHECK" })
public class ResconQuestionCategoryController {

	@Autowired
	private ResconQuestionCategoryManage resconQuestionCategoryManage;
	@Autowired
	private ResconQuestionCategoryConvert questionCategoryConvert;

	/**
	 * 获取习题类别场景列表.
	 * 
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public Value list() {
		List<QuestionCategory> list = resconQuestionCategoryManage.listAll(null);
		Map<String, Object> map = new HashMap<String, Object>(1);
		map.put("questionCategorys", questionCategoryConvert.to(list));
		return new Value(map);
	}

	/**
	 * 创建或者更新习题类别.
	 * 
	 * @param code
	 *            类别编码
	 * @param name
	 *            类别名称
	 * @param status
	 *            状态
	 * @return
	 */
	@RequestMapping(value = "saveOrUpdateCategory", method = RequestMethod.POST)
	public Value saveOrUpdateCategory(Long code, String name, Status status) {
		if (StringUtils.isEmpty(name) || status == null) {
			return new Value(new MissingArgumentException());
		}
		try {
			resconQuestionCategoryManage.saveOrUpdateCategory(code, name, status);
			return this.list();
		} catch (AbstractException e) {
			return new Value(e);
		}
	}

}

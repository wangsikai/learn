package com.lanking.uxb.rescon.question.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.baseData.QuestionTag;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.rescon.question.api.ResconQuestionTagManage;
import com.lanking.uxb.rescon.question.convert.ResconQuestionTagConvert;

/**
 * 习题标签管理.
 * 
 * @author wlche
 *
 */
@RestController
@RequestMapping("rescon/que/tag")
@RolesAllowed(userTypes = { "VENDOR_ADMIN", "VENDOR_HEAD", "VENDOR_BUILD", "VENDOR_CHECK" })
public class ResconQuestionTagController {

	@Autowired
	private ResconQuestionTagManage questionTagManage;

	@Autowired
	private ResconQuestionTagConvert questionTagConvert;

	/**
	 * 获取习题标签列表.
	 * 
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public Value list() {
		List<QuestionTag> questionTags = questionTagManage.listAll(null, null);
		Map<String, Object> map = new HashMap<String, Object>(1);
		map.put("questionTags", questionTagConvert.to(questionTags));
		return new Value(map);
	}

	/**
	 * 创建更新习题标签.
	 * 
	 * @param code
	 * @param name
	 * @param shortName
	 * @param cfg
	 * @param icon
	 * @param squence
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "saveOrUpdateTag", method = RequestMethod.POST)
	public Value saveOrUpdateTag(Long code, String name, String shortName, String cfg, Long icon, Integer squence,
			Status status) {
		if (StringUtils.isEmpty(name) || icon == null || status == null) {
			return new Value(new MissingArgumentException());
		} else if (code == null && squence == null) {
			return new Value(new MissingArgumentException());
		}
		try {
			questionTagManage.saveOrUpdateTag(code, name, shortName, cfg, icon, squence, status);
			return this.list();
		} catch (AbstractException e) {
			return new Value(e);
		}
	}

	/**
	 * 移动标签.
	 * 
	 * @param code
	 *            编码
	 * @param op
	 *            操作
	 * @return
	 */
	@RequestMapping(value = "moveTag", method = RequestMethod.POST)
	public Value moveTag(Long code, String op) {
		if (StringUtils.isEmpty(op) || code == null) {
			return new Value(new MissingArgumentException());
		}
		try {
			QuestionTag questionTag = questionTagManage.moveTag(code, op);

			List<QuestionTag> questionTags = questionTagManage.listAll(null, null);
			Map<String, Object> map = new HashMap<String, Object>(2);
			map.put("questionTags", questionTagConvert.to(questionTags));
			map.put("sequence", questionTag.getSequence());
			return new Value(map);
		} catch (AbstractException e) {
			return new Value(e);
		}
	}
}

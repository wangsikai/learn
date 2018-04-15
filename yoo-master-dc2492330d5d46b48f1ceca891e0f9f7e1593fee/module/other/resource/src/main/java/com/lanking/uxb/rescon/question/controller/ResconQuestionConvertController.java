package com.lanking.uxb.rescon.question.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.AsciiStatus;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.rescon.account.cache.ResconQuestionConvertCacheService;
import com.lanking.uxb.rescon.question.api.ResconAnswerManage;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;
import com.lanking.uxb.rescon.question.form.AnswerCheckForm;
import com.lanking.uxb.service.session.api.impl.Security;

@RestController
@RequestMapping("rescon/convert")
@RolesAllowed(userTypes = { "VENDOR_ADMIN", "VENDOR_HEAD", "VENDOR_CHECK", "VENDOR_BUILD" })
public class ResconQuestionConvertController {

	@Autowired
	private ResconQuestionManage questionManage;
	@Autowired
	private ResconAnswerManage answerService;
	@Autowired
	private ResconQuestionConvertCacheService cacheService;

	@RequestMapping(value = "list")
	public Value getConvertQuestion(AsciiStatus status) {
		if (status == null) {
			return new Value(new MissingArgumentException());
		}

		Long userId = Security.getUserId();

		// cacheService.invalidAll();
		Map<String, Object> data = Maps.newHashMap();
		Set<String> cacheQuestions = Sets.newHashSet();
		List<Long> hasQuestions = new ArrayList<Long>();

		if (status == AsciiStatus.NOCHANGE) {
			// 获取需要转换的题目
			cacheQuestions = cacheService.getConvertAsciiQuestion();
		} else if (status == AsciiStatus.NOCHECK) {
			// 获取需要校验的题目
			cacheQuestions = cacheService.getCheckAsciiQuestion();
		}

		Question question = null;
		for (String cache : cacheQuestions) {
			String[] cacheArray = cache.split("_");
			long questionId = Long.parseLong(cacheArray[1]);
			if (userId.toString().equals(cacheArray[0])) {
				question = questionManage.get(questionId);
				break;
			}
			hasQuestions.add(questionId);
		}

		if (question == null) {
			// 无该用户题目缓存
			question = questionManage.getConvertQuestion(status, hasQuestions);
		}

		if (question == null) {
			return new Value();
		}

		String code = question.getCode();
		if (question.getParentId() != null) {
			code = questionManage.get(question.getParentId()).getCode();
		}

		// 记录缓存
		if (status == AsciiStatus.NOCHANGE) {
			cacheService.setConvertQuestion(userId, question.getId());
		} else if (status == AsciiStatus.NOCHECK) {
			cacheService.setCheckQuestion(userId, question.getId());
		}

		data.put("questionId", question.getId());
		data.put("code", code);
		data.put("subFlag", question.isSubFlag());
		data.put("answer", answerService.getQuestionAnswers(question.getId()));
		return new Value(data);
	}

	@RequestMapping(value = "count")
	public Value getConvertQuestionCount() {
		Map<String, Long> countMap = questionManage.getConvertQuestionCount();
		return new Value(countMap);
	}

	/**
	 * 转换习题/校验习题
	 * 
	 * @param questionId
	 *            习题ID
	 * @param answers
	 *            答案数据
	 * @return
	 */
	@RequestMapping(value = "checkQuestion")
	public Value checkQuestion(String json) {
		AnswerCheckForm form = JSON.parseObject(json, AnswerCheckForm.class);
		if (null == form || null == form.getQuestionId() || null == form.getCheckFlag()) {
			return new Value(new MissingArgumentException());
		}
		boolean checkFlag = form.getCheckFlag() == null ? false : form.getCheckFlag();

		if (!checkFlag && (null == form.getAnswers() || form.getAnswers().size() == 0)) {
			return new Value(new MissingArgumentException());
		}
		try {
			if (checkFlag) {
				// 校验
				questionManage.checkAnswer(form.getQuestionId(), form.getAnswers(), checkFlag);
				cacheService.removeCheckQuestion(Security.getUserId(), form.getQuestionId());
			} else {
				// 修正
				questionManage.checkAnswer(form.getQuestionId(), form.getAnswers(), checkFlag);
				cacheService.removeConvertQuestion(Security.getUserId(), form.getQuestionId());
			}
		} catch (AbstractException e) {
			return new Value(e);
		}

		return new Value();
	}

	/**
	 * 清除习题缓存.
	 * 
	 * @param questionId
	 *            习题ID
	 * @return
	 */
	@RequestMapping(value = "removeQuestionCache")
	public Value removeQuestionCache(Long questionId, AsciiStatus status) {
		if (null == questionId || null == status) {
			return new Value(new MissingArgumentException());
		}

		if (status == AsciiStatus.NOCHANGE) {
			cacheService.removeConvertQuestion(Security.getUserId(), questionId);
		} else if (status == AsciiStatus.NOCHECK) {
			cacheService.removeCheckQuestion(Security.getUserId(), questionId);
		}
		return new Value();
	}
}

package com.lanking.uxb.operation.questionSection.controller;

import java.util.List;

import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.lanking.uxb.operation.questionSection.api.OpQuestionService;
import com.lanking.uxb.operation.questionSection.api.impl.OpQuestionSectionConvertService;
import com.lanking.uxb.operation.questionSection.cache.OpQuestionSectionConvertCacheService;
import com.lanking.uxb.operation.questionSection.convert.OpQuestionSectionTextbookConvert;
import com.lanking.uxb.service.code.api.TextbookService;

@RestController
@RequestMapping(value = "op/questionSection")
public class OpQuestionSectionController {

	private static final Integer MAX_CONVERTING = 20;

	@Autowired
	private TextbookService textbookService;
	@Autowired
	private OpQuestionSectionTextbookConvert questionSectionTextbookConvert;
	@Autowired
	private OpQuestionSectionConvertService convertService;
	@Autowired
	private OpQuestionService questionService;
	@Autowired
	private OpQuestionSectionConvertCacheService questionSectionConvertCacheService;

	@RequestMapping(value = "index", method = { RequestMethod.GET, RequestMethod.POST })
	public Value index(@RequestParam(value = "subjectCode", defaultValue = "2") int subjectCode) {
		List<Textbook> textbookList = textbookService.getAll();
		List<Textbook> tbs = Lists.newArrayList();
		for (Textbook textbook : textbookList) {
			if (textbook.getSubjectCode() % 100 == subjectCode) {
				tbs.add(textbook);
			}
		}
		return new Value(questionSectionTextbookConvert.to(tbs));
	}

	/**
	 * 转换知识点
	 *
	 * @param code
	 *            教材编码
	 * @param all
	 *            是否转换全部
	 * @return {@link Value}
	 */
	@RequestMapping(value = "convert", method = { RequestMethod.GET, RequestMethod.POST })
	public Value convert(int code, @RequestParam(value = "all", defaultValue = "true") boolean all) {
		String questionId = questionSectionConvertCacheService.get(code);

		Integer convertingCount = questionSectionConvertCacheService.getCount();
		if (convertingCount >= MAX_CONVERTING) {
			return new Value(convertingCount);
		}

		if (StringUtils.isBlank(questionId) || Long.valueOf(questionId).equals(-1)) {
			questionSectionConvertCacheService.incrConvertingCount(1);
			convertService.convert(code, true);
		} else {
			questionSectionConvertCacheService.incrConvertingCount(1);
			convertService.convert(code, all);
		}
		return new Value();
	}

	@RequestMapping(value = "remaining", method = { RequestMethod.GET, RequestMethod.POST })
	public Value remaining(int code) {
		String id = questionSectionConvertCacheService.get(code);
		if (id != null) {
			return new Value(questionService.remaining(code, Long.parseLong(id)));
		}
		return new Value(-1);
	}

}

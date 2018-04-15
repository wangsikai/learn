package com.lanking.uxb.operation.questionSection.convert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.common.baseData.TextbookCategory;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.operation.questionSection.cache.OpQuestionSectionConvertCacheService;
import com.lanking.uxb.operation.questionSection.value.VOpQuestionSectionTextbook;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;

@Component
public class OpQuestionSectionTextbookConvert extends Converter<VOpQuestionSectionTextbook, Textbook, Integer> {
	@Autowired
	private OpQuestionSectionConvertCacheService questionSectionConvertCacheService;

	@Autowired
	private PhaseService phaseService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private TextbookCategoryService tbcService;

	@Override
	protected Integer getId(Textbook textbook) {
		return textbook.getCode();
	}

	@Override
	protected VOpQuestionSectionTextbook convert(Textbook textbook) {
		VOpQuestionSectionTextbook v = new VOpQuestionSectionTextbook();
		v.setCode(textbook.getCode());
		TextbookCategory textbookCategory = tbcService.get(textbook.getCategoryCode());
		if (textbookCategory == null) {
			v.setName("版本缺失 " + textbook.getCategoryCode() + "|" + phaseService.get(textbook.getPhaseCode()).getName()
					+ "|" + subjectService.get(textbook.getSubjectCode()).getName() + "|" + textbook.getName());
		} else {
			v.setName(textbookCategory.getName() + "|" + phaseService.get(textbook.getPhaseCode()).getName() + "|"
					+ subjectService.get(textbook.getSubjectCode()).getName() + "|" + textbook.getName());
		}
		String convertingCode = questionSectionConvertCacheService.get(textbook.getCode());
		v.setConverting(StringUtils.isNotBlank(convertingCode) && !convertingCode.equals("-1"));
		return v;
	}

}

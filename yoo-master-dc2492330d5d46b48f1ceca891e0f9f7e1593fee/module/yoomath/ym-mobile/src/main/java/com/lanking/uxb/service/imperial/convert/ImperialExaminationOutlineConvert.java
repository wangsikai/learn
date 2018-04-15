package com.lanking.uxb.service.imperial.convert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.convert.KnowledgePointConvert;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityService;
import com.lanking.uxb.service.imperial.value.VImperialExaminationOutline;

@Component
public class ImperialExaminationOutlineConvert extends Converter<VImperialExaminationOutline, Homework, Long> {

	@Autowired
	private KnowledgePointService knowledgePointService;
	@Autowired
	private KnowledgePointConvert knowledgePointConvert;
	@Autowired
	private ImperialExaminationActivityService imperialService;

	@Override
	protected Long getId(Homework s) {
		return s.getId();
	}

	@Override
	protected VImperialExaminationOutline convert(Homework s) {
		VImperialExaminationOutline v = new VImperialExaminationOutline();
		v.setDifficulty(s.getDifficulty());
		v.setQuestionCount(s.getQuestionCount());
		v.setHomeworkId(s.getId());
		v.setKnowledgePoints(knowledgePointConvert.to(knowledgePointService.mgetList(s.getKnowledgePoints())));
		return v;
	}

}

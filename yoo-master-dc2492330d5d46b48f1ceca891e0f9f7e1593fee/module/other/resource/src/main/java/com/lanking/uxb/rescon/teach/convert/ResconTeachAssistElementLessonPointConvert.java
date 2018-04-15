package com.lanking.uxb.rescon.teach.convert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementLessonPoint;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgePointService;
import com.lanking.uxb.rescon.basedata.convert.ResconKnowledgePointConvert;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;
import com.lanking.uxb.rescon.question.convert.ResconQuestionConvert;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementLessonPoint;

/**
 * TeachAssistElementLessonPoint -> VTeachAssistElementLessonPoint
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Component
public class ResconTeachAssistElementLessonPointConvert extends
		Converter<VTeachAssistElementLessonPoint, TeachAssistElementLessonPoint, Long> {

	@Autowired
	private ResconQuestionManage questionManage;
	@Autowired
	private ResconQuestionConvert questionConvert;
	@Autowired
	private ResconKnowledgePointService knowledgePointService;
	@Autowired
	private ResconKnowledgePointConvert knowledgePointConvert;

	@Override
	protected Long getId(TeachAssistElementLessonPoint teachAssistElementLessonPoint) {
		return teachAssistElementLessonPoint.getId();
	}

	@Override
	protected VTeachAssistElementLessonPoint convert(TeachAssistElementLessonPoint teachAssistElementLessonPoint) {
		VTeachAssistElementLessonPoint v = new VTeachAssistElementLessonPoint();
		v.setId(teachAssistElementLessonPoint.getId());
		v.setLessonId(teachAssistElementLessonPoint.getLessonId());
		v.setName(teachAssistElementLessonPoint.getName());
		v.setSequence(teachAssistElementLessonPoint.getSequence());

		v.setExampleQuestions(questionConvert.to(questionManage.mgetList(teachAssistElementLessonPoint
				.getExampleQuestions())));
		v.setExampleQuestionIds(teachAssistElementLessonPoint.getExampleQuestions());
		v.setExpandQuestions(questionConvert.to(questionManage.mgetList(teachAssistElementLessonPoint
				.getExpandQuestions())));
		v.setExpandQuestionIds(teachAssistElementLessonPoint.getExpandQuestions());
		v.setKnowledgePoints(knowledgePointConvert.to(knowledgePointService.mgetList(teachAssistElementLessonPoint
				.getKnowpoints())));
		return v;
	}

}

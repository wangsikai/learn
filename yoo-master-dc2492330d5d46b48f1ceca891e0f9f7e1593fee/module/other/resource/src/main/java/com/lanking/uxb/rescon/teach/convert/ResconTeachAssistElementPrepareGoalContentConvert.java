package com.lanking.uxb.rescon.teach.convert;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPrepareGoalContent;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgePointService;
import com.lanking.uxb.rescon.basedata.convert.ResconKnowledgePointConvert;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;
import com.lanking.uxb.rescon.question.convert.ResconQuestionConvert;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementPrepareGoalContent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Component
public class ResconTeachAssistElementPrepareGoalContentConvert extends
		Converter<VTeachAssistElementPrepareGoalContent, TeachAssistElementPrepareGoalContent, Long> {
	@Autowired
	private ResconQuestionManage questionManage;
	@Autowired
	private ResconQuestionConvert questionConvert;
	@Autowired
	private ResconKnowledgePointService knowledgePointService;
	@Autowired
	private ResconKnowledgePointConvert knowledgePointConvert;

	@Override
	protected Long getId(TeachAssistElementPrepareGoalContent content) {
		return content.getId();
	}

	@Override
	protected VTeachAssistElementPrepareGoalContent convert(TeachAssistElementPrepareGoalContent content) {
		VTeachAssistElementPrepareGoalContent v = new VTeachAssistElementPrepareGoalContent();
		v.setId(content.getId());
		v.setName(content.getName());
		v.setSequence(content.getSequence());

		v.setPreviewQuestions(questionConvert.to(questionManage.mgetList(content.getPreviewQuestions())));
		v.setPreviewQuestionIds(content.getPreviewQuestions());
		v.setSelfTestQuestions(questionConvert.to(questionManage.mgetList(content.getSelfTestQuestions())));
		v.setSelfTestQuestionIds(content.getSelfTestQuestions());
		v.setKnowledgePoints(knowledgePointConvert.to(knowledgePointService.mgetList(content.getKnowpoints())));

		return v;
	}
}

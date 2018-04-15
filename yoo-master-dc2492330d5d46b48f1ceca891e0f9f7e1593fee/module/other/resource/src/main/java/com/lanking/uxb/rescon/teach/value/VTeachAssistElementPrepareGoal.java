package com.lanking.uxb.rescon.teach.value;

import com.lanking.uxb.rescon.basedata.value.VKnowledgePoint;
import com.lanking.uxb.rescon.question.value.VQuestion;

import java.util.List;

/**
 * @see com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPrepareGoal
 * @author xinyu.zhou
 * @since 2.2.0
 */
public class VTeachAssistElementPrepareGoal extends VTeachAssistElement {
	private static final long serialVersionUID = -8921999602530151644L;

	private List<VTeachAssistElementPrepareGoalContent> contents;

	public List<VTeachAssistElementPrepareGoalContent> getContents() {
		return contents;
	}

	public void setContents(List<VTeachAssistElementPrepareGoalContent> contents) {
		this.contents = contents;
	}
}

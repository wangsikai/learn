package com.lanking.uxb.rescon.teach.value;

import java.util.List;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementTopicType;

/**
 * @see com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementTopic
 * @author xinyu.zhou
 * @since 2.2.0
 */
public class VTeachAssistElementTopic extends VTeachAssistElement {
	private static final long serialVersionUID = -2863388322327010943L;

	private TeachAssistElementTopicType topicType;
	private List<VTeachAssistElementTopicContent> contents;

	public List<VTeachAssistElementTopicContent> getContents() {
		return contents;
	}

	public void setContents(List<VTeachAssistElementTopicContent> contents) {
		this.contents = contents;
	}

	public TeachAssistElementTopicType getTopicType() {
		return topicType;
	}

	public void setTopicType(TeachAssistElementTopicType topicType) {
		this.topicType = topicType;
	}
}

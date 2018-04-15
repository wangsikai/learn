package com.lanking.uxb.rescon.teach.value;

import java.util.List;

/**
 * @see com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPractice
 * @author xinyu.zhou
 * @since 2.2.0
 */
public class VTeachAssistElementPractice extends VTeachAssistElement {
	private static final long serialVersionUID = 5997430436326232184L;

	private List<VTeachAssistElementPracticeContent> contents;

	public List<VTeachAssistElementPracticeContent> getContents() {
		return contents;
	}

	public void setContents(List<VTeachAssistElementPracticeContent> contents) {
		this.contents = contents;
	}
}

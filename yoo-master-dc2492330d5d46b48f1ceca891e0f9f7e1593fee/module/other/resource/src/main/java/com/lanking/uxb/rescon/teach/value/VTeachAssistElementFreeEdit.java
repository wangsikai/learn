package com.lanking.uxb.rescon.teach.value;

import java.util.List;

/**
 * 自由编辑Value
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
public class VTeachAssistElementFreeEdit extends VTeachAssistElement {
	private static final long serialVersionUID = -9082445418638165951L;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private List<VTeachAssistElementFreeEditContent> contents;

	public List<VTeachAssistElementFreeEditContent> getContents() {
		return contents;
	}

	public void setContents(List<VTeachAssistElementFreeEditContent> contents) {
		this.contents = contents;
	}
}

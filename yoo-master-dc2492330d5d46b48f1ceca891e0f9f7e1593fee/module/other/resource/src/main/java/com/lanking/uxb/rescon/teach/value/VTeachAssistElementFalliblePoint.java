package com.lanking.uxb.rescon.teach.value;

import java.util.List;

/**
 * @author xinyu.zhou
 * @since 2.2.0
 */
public class VTeachAssistElementFalliblePoint extends VTeachAssistElement {
	private static final long serialVersionUID = 6648003808031306552L;

	List<VTeachAssistElementFalliblePointContent> contents;

	public List<VTeachAssistElementFalliblePointContent> getContents() {
		return contents;
	}

	public void setContents(List<VTeachAssistElementFalliblePointContent> contents) {
		this.contents = contents;
	}
}

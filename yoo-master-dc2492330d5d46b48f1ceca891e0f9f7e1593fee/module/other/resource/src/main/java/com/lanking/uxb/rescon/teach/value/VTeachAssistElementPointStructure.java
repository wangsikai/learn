package com.lanking.uxb.rescon.teach.value;

import com.lanking.uxb.rescon.basedata.value.VKnowledgeSystem;

/**
 * @see com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPointStructure
 * @author xinyu.zhou
 * @since 2.2.0
 */
public class VTeachAssistElementPointStructure extends VTeachAssistElement {
	private static final long serialVersionUID = 3253299290886229978L;

	private VKnowledgeSystem knowledgeSystem;

	public VKnowledgeSystem getKnowledgeSystem() {
		return knowledgeSystem;
	}

	public void setKnowledgeSystem(VKnowledgeSystem knowledgeSystem) {
		this.knowledgeSystem = knowledgeSystem;
	}
}

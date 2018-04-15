package com.lanking.uxb.rescon.teach.value;

import com.lanking.uxb.rescon.basedata.value.VKnowledgeSystem;

import java.io.Serializable;

/**
 * 知识拓朴图 模块
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
public class VTeachAssistElementPointMap extends VTeachAssistElement {
	private static final long serialVersionUID = -1196962416010027223L;

	private VKnowledgeSystem knowledgeSystem;

	public VKnowledgeSystem getKnowledgeSystem() {
		return knowledgeSystem;
	}

	public void setKnowledgeSystem(VKnowledgeSystem knowledgeSystem) {
		this.knowledgeSystem = knowledgeSystem;
	}
}

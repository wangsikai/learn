package com.lanking.uxb.rescon.teach.value;

import com.lanking.uxb.rescon.basedata.value.VKnowledgePoint;

import java.io.Serializable;

/**
 * @see com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementKnowledgeSpecKp
 * @author xinyu.zhou
 * @since 2.2.0
 */
public class VTeachAssistElementKnowledgeSpecKp implements Serializable {
	private static final long serialVersionUID = 3477984944422267724L;

	private long id;
	private long knowledgeSpecId;
	private VKnowledgePoint knowledgePoint;
	private String content;
	private Integer sequence;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getKnowledgeSpecId() {
		return knowledgeSpecId;
	}

	public void setKnowledgeSpecId(long knowledgeSpecId) {
		this.knowledgeSpecId = knowledgeSpecId;
	}

	public VKnowledgePoint getKnowledgePoint() {
		return knowledgePoint;
	}

	public void setKnowledgePoint(VKnowledgePoint knowledgePoint) {
		this.knowledgePoint = knowledgePoint;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
}

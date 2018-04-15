package com.lanking.uxb.service.code.value;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 知识卡片
 * 
 * @since 2.3.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年8月22日
 */
public class VKnowledgePointCard implements Serializable {

	private static final long serialVersionUID = 3968475099239100444L;

	private long id;
	private String name;
	@JSONField(serialize = true)
	private long knowpointCode;
	private VKnowledgePoint knowledgePoint;
	private String description;
	private String detailDescription;
	private List<String> hints;
	@JSONField(serialize = false)
	private List<Long> questionIds;
	private List<Object> questions;
	// 相关的考点
	@JSONField(serialize = false)
	private List<Long> examinationPointIds;
	private List<VExaminationPoint> examinationPoint;
	// 相关的知识卡片
	private List<VKnowledgePointCard> related;

	private String h5PageUrl;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getKnowpointCode() {
		return knowpointCode;
	}

	public void setKnowpointCode(long knowpointCode) {
		this.knowpointCode = knowpointCode;
	}

	public VKnowledgePoint getKnowledgePoint() {
		return knowledgePoint;
	}

	public void setKnowledgePoint(VKnowledgePoint knowledgePoint) {
		this.knowledgePoint = knowledgePoint;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDetailDescription() {
		return detailDescription;
	}

	public void setDetailDescription(String detailDescription) {
		this.detailDescription = detailDescription;
	}

	public List<String> getHints() {
		return hints;
	}

	public void setHints(List<String> hints) {
		this.hints = hints;
	}

	public List<Long> getQuestionIds() {
		return questionIds;
	}

	public void setQuestionIds(List<Long> questionIds) {
		this.questionIds = questionIds;
	}

	public List<Object> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Object> questions) {
		this.questions = questions;
	}

	public List<Long> getExaminationPointIds() {
		return examinationPointIds;
	}

	public void setExaminationPointIds(List<Long> examinationPointIds) {
		this.examinationPointIds = examinationPointIds;
	}

	public List<VExaminationPoint> getExaminationPoint() {
		return examinationPoint;
	}

	public void setExaminationPoint(List<VExaminationPoint> examinationPoint) {
		this.examinationPoint = examinationPoint;
	}

	public List<VKnowledgePointCard> getRelated() {
		return related;
	}

	public void setRelated(List<VKnowledgePointCard> related) {
		this.related = related;
	}

	public String getH5PageUrl() {
		return h5PageUrl;
	}

	public void setH5PageUrl(String h5PageUrl) {
		this.h5PageUrl = h5PageUrl;
	}
}

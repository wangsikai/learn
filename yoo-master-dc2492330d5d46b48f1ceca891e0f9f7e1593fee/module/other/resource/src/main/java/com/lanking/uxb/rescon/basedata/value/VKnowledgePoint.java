package com.lanking.uxb.rescon.basedata.value;

import java.io.Serializable;

import com.lanking.cloud.domain.common.baseData.FocalDifficult;
import com.lanking.cloud.domain.common.baseData.KnowledgePointDifficulty;
import com.lanking.cloud.domain.common.baseData.StudyDifficulty;

/**
 * 知识点
 * 
 * @since 2.0.1
 * @author wangsenhao
 *
 */
public class VKnowledgePoint implements Serializable {

	private static final long serialVersionUID = 6629674190452545847L;

	private Long id;

	private Long pcode;

	private String name;

	private KnowledgePointDifficulty difficulty;

	// 学习要求
	private StudyDifficulty studyDifficulty;

	// 重点&难点
	private FocalDifficult focalDifficult;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPcode() {
		return pcode;
	}

	public void setPcode(Long pcode) {
		this.pcode = pcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public KnowledgePointDifficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(KnowledgePointDifficulty difficulty) {
		this.difficulty = difficulty;
	}

	public StudyDifficulty getStudyDifficulty() {
		return studyDifficulty;
	}

	public void setStudyDifficulty(StudyDifficulty studyDifficulty) {
		this.studyDifficulty = studyDifficulty;
	}

	public FocalDifficult getFocalDifficult() {
		return focalDifficult;
	}

	public void setFocalDifficult(FocalDifficult focalDifficult) {
		this.focalDifficult = focalDifficult;
	}

}

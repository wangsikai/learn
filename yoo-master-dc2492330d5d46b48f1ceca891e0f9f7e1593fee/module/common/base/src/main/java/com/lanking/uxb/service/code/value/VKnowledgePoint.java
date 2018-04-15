package com.lanking.uxb.service.code.value;

import java.util.List;

import com.lanking.cloud.domain.common.baseData.FocalDifficult;
import com.lanking.cloud.domain.common.baseData.KnowledgePointDifficulty;
import com.lanking.cloud.domain.common.baseData.StudyDifficulty;
import com.lanking.cloud.sdk.bean.Status;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
public class VKnowledgePoint extends VKnowledgeSystem {
	private static final long serialVersionUID = -2681694906807537889L;

	private KnowledgePointDifficulty difficulty;
	private StudyDifficulty studyDifficulty;
	private FocalDifficult focalDifficult;
	private Status status;
	private List<VKnowledgePointCard> cards;
	// 是否有知识卡片
	private boolean hasCard = false;

	// h5 url => 只有客户端会返回此参数
	private String h5PageUrl;

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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<VKnowledgePointCard> getCards() {
		return cards;
	}

	public void setCards(List<VKnowledgePointCard> cards) {
		this.cards = cards;
	}

	public boolean isHasCard() {
		return hasCard;
	}

	public void setHasCard(boolean hasCard) {
		this.hasCard = hasCard;
	}

	public String getH5PageUrl() {
		return h5PageUrl;
	}

	public void setH5PageUrl(String h5PageUrl) {
		this.h5PageUrl = h5PageUrl;
	}
}

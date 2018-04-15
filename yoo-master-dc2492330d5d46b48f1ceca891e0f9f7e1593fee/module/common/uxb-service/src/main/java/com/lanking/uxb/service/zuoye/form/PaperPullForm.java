package com.lanking.uxb.service.zuoye.form;

import java.util.List;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.yoomath.smartExamPaper.SmartExamPaperDifficulty;

/**
 * 智能出卷拉取条件
 * 
 * @author wangsenhao
 *
 */
public class PaperPullForm extends PullQuestionForm {

	private SmartExamPaperDifficulty smartExamPaperDifficulty;

	private Long userId;

	// 薄弱知识点
	private List<Long> weakKnowpoints = Lists.newArrayList();

	private Integer textBookCode;

	private Long paperId;

	public SmartExamPaperDifficulty getSmartExamPaperDifficulty() {
		return smartExamPaperDifficulty;
	}

	public void setSmartExamPaperDifficulty(SmartExamPaperDifficulty smartExamPaperDifficulty) {
		this.smartExamPaperDifficulty = smartExamPaperDifficulty;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<Long> getWeakKnowpoints() {
		return weakKnowpoints;
	}

	public void setWeakKnowpoints(List<Long> weakKnowpoints) {
		this.weakKnowpoints = weakKnowpoints;
	}

	public Integer getTextBookCode() {
		return textBookCode;
	}

	public void setTextBookCode(Integer textBookCode) {
		this.textBookCode = textBookCode;
	}

	public Long getPaperId() {
		return paperId;
	}

	public void setPaperId(Long paperId) {
		this.paperId = paperId;
	}

}

package com.lanking.uxb.rescon.question.form;

import java.util.List;

import com.lanking.cloud.domain.common.resource.question.Question;

/**
 * 校验条件表单.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年11月23日
 */
public class CheckForm {

	private Integer phaseCode;

	private Integer subjectCode;

	private Long vendorUserId;

	private Question.Type questionType;

	private Long createBt;

	private Long createEt;

	private List<Long> knowpoints;

	private Long bookVersionId;

	private Long paperId;

	private Long trainId;

	private Long questionId;

	private boolean checkRefund = false;

	public boolean hasParam() {
		return (phaseCode != null && phaseCode > 0) || (subjectCode != null && subjectCode > 0)
				|| (vendorUserId != null && vendorUserId > 0) || questionType != null || createBt != null
				|| createEt != null || (knowpoints != null && knowpoints.size() > 0) || bookVersionId != null
				|| paperId != null || trainId != null;
	}

	public Integer getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(Integer phaseCode) {
		this.phaseCode = phaseCode;
	}

	public Integer getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(Integer subjectCode) {
		this.subjectCode = subjectCode;
	}

	public Long getVendorUserId() {
		return vendorUserId;
	}

	public void setVendorUserId(Long vendorUserId) {
		this.vendorUserId = vendorUserId;
	}

	public Question.Type getQuestionType() {
		return questionType;
	}

	public void setQuestionType(Question.Type questionType) {
		this.questionType = questionType;
	}

	public Long getCreateBt() {
		return createBt;
	}

	public void setCreateBt(Long createBt) {
		this.createBt = createBt;
	}

	public Long getCreateEt() {
		return createEt;
	}

	public void setCreateEt(Long createEt) {
		this.createEt = createEt;
	}

	public List<Long> getKnowpoints() {
		return knowpoints;
	}

	public void setKnowpoints(List<Long> knowpoints) {
		this.knowpoints = knowpoints;
	}

	public Long getBookVersionId() {
		return bookVersionId;
	}

	public void setBookVersionId(Long bookVersionId) {
		this.bookVersionId = bookVersionId;
	}

	public Long getPaperId() {
		return paperId;
	}

	public void setPaperId(Long paperId) {
		this.paperId = paperId;
	}

	public Long getTrainId() {
		return trainId;
	}

	public void setTrainId(Long trainId) {
		this.trainId = trainId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public boolean isCheckRefund() {
		return checkRefund;
	}

	public void setCheckRefund(boolean checkRefund) {
		this.checkRefund = checkRefund;
	}

}

package com.lanking.uxb.service.user.value;

import java.io.Serializable;

import com.lanking.uxb.service.code.value.VPasswordQuestion;

/**
 * 密码保护VO
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年2月6日
 */
public class VAccountPasswordQuestion implements Serializable {

	private static final long serialVersionUID = 8041953204240924142L;

	private long id;
	private long accountId;
	private int passwordQuestionCode;
	private String answer;
	private VPasswordQuestion passwordQuestion;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getPasswordQuestionCode() {
		return passwordQuestionCode;
	}

	public void setPasswordQuestionCode(int passwordQuestionCode) {
		this.passwordQuestionCode = passwordQuestionCode;
	}

	public VPasswordQuestion getPasswordQuestion() {
		return passwordQuestion;
	}

	public void setPasswordQuestion(VPasswordQuestion passwordQuestion) {
		this.passwordQuestion = passwordQuestion;
	}

}

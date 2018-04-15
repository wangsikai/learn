package com.lanking.cloud.domain.common.resource.question;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.lanking.cloud.sdk.util.StringUtils;

/**
 * 题目&章节关系表
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@MappedSuperclass
public class QuestionSectionKey implements Serializable {

	private static final long serialVersionUID = -5340566057737059019L;

	/**
	 * 题目ID
	 */
	@Id
	@Column(name = "question_id", nullable = false)
	private long questionId;

	/**
	 * 章节代码
	 */
	@Id
	@Column(name = "section_code", nullable = false)
	private long sectionCode;

	public QuestionSectionKey(long questionId, long sectionCode) {
		this.questionId = questionId;
		this.sectionCode = sectionCode;
	}

	public QuestionSectionKey() {
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(long sectionCode) {
		this.sectionCode = sectionCode;
	}

	@Override
	public int hashCode() {
		return (int) (questionId * 37 + sectionCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof QuestionSectionKey) {
			QuestionSectionKey other = (QuestionSectionKey) obj;
			return questionId == other.questionId && sectionCode == other.sectionCode;
		}
		return false;
	}

	@Override
	public String toString() {
		return StringUtils.join(new Object[] { questionId, sectionCode }, '-');
	}
}

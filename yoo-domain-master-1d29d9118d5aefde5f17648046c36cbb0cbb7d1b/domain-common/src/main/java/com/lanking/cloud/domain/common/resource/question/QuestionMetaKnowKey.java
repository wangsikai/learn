package com.lanking.cloud.domain.common.resource.question;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.lanking.cloud.sdk.util.StringUtils;

/**
 * 题目&元知识点关系表
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@MappedSuperclass
public class QuestionMetaKnowKey implements Serializable {

	private static final long serialVersionUID = 1477052148187927469L;

	/**
	 * 题目ID
	 */
	@Id
	@Column(name = "question_id", nullable = false)
	private long questionId;

	/**
	 * 元知识点代码
	 */
	@Id
	@Column(name = "meta_code", nullable = false)
	private int metaCode;

	public QuestionMetaKnowKey(long questionId, int metaCode) {
		this.questionId = questionId;
		this.metaCode = metaCode;
	}

	public QuestionMetaKnowKey() {
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public int getMetaCode() {
		return metaCode;
	}

	public void setMetaCode(int metaCode) {
		this.metaCode = metaCode;
	}

	@Override
	public int hashCode() {
		return (int) (questionId * 37 + metaCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof QuestionMetaKnowKey) {
			QuestionMetaKnowKey other = (QuestionMetaKnowKey) obj;
			return questionId == other.questionId && metaCode == other.metaCode;
		}
		return false;
	}

	@Override
	public String toString() {
		return StringUtils.join(new Object[] { questionId, metaCode }, '-');
	}
}

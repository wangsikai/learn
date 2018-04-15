package com.lanking.uxb.rescon.question.form;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年10月20日
 */
public class SimilarQuestionsForm implements Serializable {
	private static final long serialVersionUID = 7846132412707851135L;

	/**
	 * 题组MD5值.
	 */
	private String md5;

	/**
	 * 题组基准题目.
	 */
	private Long baseQuestionId;

	/**
	 * 原始题组集合.
	 */
	private List<Long> questionIds;

	/**
	 * 删除题组.
	 */
	private List<Long> deleteIds;

	/**
	 * 相似题组.
	 */
	private List<Long> likeIds;

	/**
	 * 重复题组集合.
	 */
	private List<SimilarSameQuestionForm> similarSameQuestionForms;

	public List<Long> getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(List<Long> deleteIds) {
		this.deleteIds = deleteIds;
	}

	public List<SimilarSameQuestionForm> getSimilarSameQuestionForms() {
		return similarSameQuestionForms;
	}

	public void setSimilarSameQuestionForms(List<SimilarSameQuestionForm> similarSameQuestionForms) {
		this.similarSameQuestionForms = similarSameQuestionForms;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public List<Long> getQuestionIds() {
		return questionIds;
	}

	public void setQuestionIds(List<Long> questionIds) {
		this.questionIds = questionIds;
	}

	public List<Long> getLikeIds() {
		return likeIds;
	}

	public void setLikeIds(List<Long> likeIds) {
		this.likeIds = likeIds;
	}

	public Long getBaseQuestionId() {
		return baseQuestionId;
	}

	public void setBaseQuestionId(Long baseQuestionId) {
		this.baseQuestionId = baseQuestionId;
	}

}

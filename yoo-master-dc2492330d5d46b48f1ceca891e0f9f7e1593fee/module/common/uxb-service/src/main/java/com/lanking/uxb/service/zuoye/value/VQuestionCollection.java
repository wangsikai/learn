package com.lanking.uxb.service.zuoye.value;

import java.io.Serializable;
import java.util.Date;

import com.lanking.uxb.service.resources.value.VQuestion;

/**
 * 题目收藏的VO
 * 
 * @since yoomath V1.3
 * @author wangsenhao
 *
 */
public class VQuestionCollection implements Serializable {

	private static final long serialVersionUID = -3821135933206480466L;
	/**
	 * 收藏时间
	 */
	private Date collectTime;
	/**
	 * 收藏id
	 */
	private Long collectId;

	private VQuestion vQuestion;

	public VQuestion getvQuestion() {
		return vQuestion;
	}

	public void setvQuestion(VQuestion vQuestion) {
		this.vQuestion = vQuestion;
	}

	public Date getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}

	public Long getCollectId() {
		return collectId;
	}

	public void setCollectId(Long collectId) {
		this.collectId = collectId;
	}

}

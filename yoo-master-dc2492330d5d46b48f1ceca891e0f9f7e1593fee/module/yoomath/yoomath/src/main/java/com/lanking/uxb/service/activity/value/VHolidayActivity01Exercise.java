package com.lanking.uxb.service.activity.value;

import java.io.Serializable;

/**
 * 活动练习VO
 * 
 * @author zemin.song
 *
 */
public class VHolidayActivity01Exercise implements Serializable {

	private static final long serialVersionUID = -5133767731085782178L;

	private long id;

	private String name;

	private long questionCount;

	private Integer sequence;

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

	public long getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(long questionCount) {
		this.questionCount = questionCount;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

}

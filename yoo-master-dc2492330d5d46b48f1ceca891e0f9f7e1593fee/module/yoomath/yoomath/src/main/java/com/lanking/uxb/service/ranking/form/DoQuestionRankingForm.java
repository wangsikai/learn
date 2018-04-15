package com.lanking.uxb.service.ranking.form;

/**
 * 答题排名查询条件
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月24日
 */
public class DoQuestionRankingForm {

	// 7,30,365
	private Integer day = 7;
	// 类型[class|school]
	private String type;
	// 班级ID
	private Long classId;

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

}

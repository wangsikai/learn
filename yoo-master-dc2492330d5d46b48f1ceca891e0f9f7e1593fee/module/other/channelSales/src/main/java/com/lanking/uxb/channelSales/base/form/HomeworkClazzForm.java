package com.lanking.uxb.channelSales.base.form;

/**
 * 班级查询form
 *
 * @author xinyu.zhou
 * @since 3.9.2
 */
public class HomeworkClazzForm {
	// 渠道商名(若有code查询条件在，此条不起作用)
	private String channelName;
	// 渠道商code
	private Integer channelCode;
	// 学校名
	private String schoolName;
	// 班级名
	private String className;
	// 学校id
	private Long schoolId;

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public Integer getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(Integer channelCode) {
		this.channelCode = channelCode;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}
}

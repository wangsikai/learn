package com.lanking.uxb.channelSales.base.value;

import java.io.Serializable;

import com.lanking.cloud.sdk.bean.Status;

/**
 * @author xinyu.zhou
 * @since 3.9.2
 */
public class VCsHomeworkClass implements Serializable {
	private static final long serialVersionUID = 6191729502438382856L;

	// 数据id
	private long id;
	// 班级码
	private String code;
	// 班级名
	private String name;
	// 学校名
	private String schoolName;
	// 渠道商名
	private String channelName;
	// 老师名
	private String teacherName;
	// 班级人数
	private Integer clazzNum;
	// 会员人数
	private Integer memberNum;
	// 状态
	private Status status;
	// 班级所属教师->渠道商code
	private Integer channelCode;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public Integer getClazzNum() {
		return clazzNum;
	}

	public void setClazzNum(Integer clazzNum) {
		this.clazzNum = clazzNum;
	}

	public Integer getMemberNum() {
		return memberNum;
	}

	public void setMemberNum(Integer memberNum) {
		this.memberNum = memberNum;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Integer getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(Integer channelCode) {
		this.channelCode = channelCode;
	}
}

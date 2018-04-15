package com.lanking.uxb.channelSales.channel.value;

import java.io.Serializable;

import com.lanking.cloud.domain.common.baseData.SchoolType;
import com.lanking.uxb.service.code.value.VPhase;

/**
 * 学校VO
 *
 * @author xinyu.zhou
 * @since 2.5.0
 */
public class VSchool implements Serializable {
	// id
	private long id;
	// 学校名称
	private String name;
	// 阶段
	private VPhase phase;
	// 渠道商名
	private String channelName;
	// 地址
	private String districtName;
	// 学校类型
	private SchoolType schoolType;
	// 渠道code 2017.2.27新增
	private Integer channelCode;

	private boolean clickDisabled = false;
	// 套餐组已邦定的套餐名
	private String memberPackageGroupName;
	// 学生数量
	private Long studentNum;
	// 教师数量
	private Long teacherNum;
	// 学生会员数量
	private Long studentMemberNum;
	// 教师会员数量
	private Long teacherMemberNum;

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

	public VPhase getPhase() {
		return phase;
	}

	public void setPhase(VPhase phase) {
		this.phase = phase;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public SchoolType getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(SchoolType schoolType) {
		this.schoolType = schoolType;
	}

	public Integer getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(Integer channelCode) {
		this.channelCode = channelCode;
	}

	public boolean isClickDisabled() {
		return clickDisabled;
	}

	public void setClickDisabled(boolean clickDisabled) {
		this.clickDisabled = clickDisabled;
	}

	public String getMemberPackageGroupName() {
		return memberPackageGroupName;
	}

	public void setMemberPackageGroupName(String memberPackageGroupName) {
		this.memberPackageGroupName = memberPackageGroupName;
	}

	public Long getStudentNum() {
		return studentNum;
	}

	public void setStudentNum(Long studentNum) {
		this.studentNum = studentNum;
	}

	public Long getTeacherNum() {
		return teacherNum;
	}

	public void setTeacherNum(Long teacherNum) {
		this.teacherNum = teacherNum;
	}

	public Long getStudentMemberNum() {
		return studentMemberNum;
	}

	public void setStudentMemberNum(Long studentMemberNum) {
		this.studentMemberNum = studentMemberNum;
	}

	public Long getTeacherMemberNum() {
		return teacherMemberNum;
	}

	public void setTeacherMemberNum(Long teacherMemberNum) {
		this.teacherMemberNum = teacherMemberNum;
	}
}

package com.lanking.uxb.channelSales.channel.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 渠道商用户VO
 *
 * @author xinyu.zhou
 * @since 2.5.0
 */
public class VUserChannel implements Serializable {
	private int code;
	private String originalName;
	private String name;
	private int schoolCount;
	private long teacherVipCount;
	private long teacherCount;
	private long teacherSchoolVipCount;
	private long studentCount;
	private long studentVipCount;
	private String conUserName;
	private BigDecimal openMemberLimit;
	private BigDecimal openedMember;
	// tree相关
	private List<VSchool> children;
	private boolean clickDisabled = false;
	private String memberPackageGroupName;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSchoolCount() {
		return schoolCount;
	}

	public void setSchoolCount(int schoolCount) {
		this.schoolCount = schoolCount;
	}

	public long getTeacherVipCount() {
		return teacherVipCount;
	}

	public void setTeacherVipCount(long teacherVipCount) {
		this.teacherVipCount = teacherVipCount;
	}

	public long getTeacherCount() {
		return teacherCount;
	}

	public void setTeacherCount(long teacherCount) {
		this.teacherCount = teacherCount;
	}

	public long getTeacherSchoolVipCount() {
		return teacherSchoolVipCount;
	}

	public void setTeacherSchoolVipCount(long teacherSchoolVipCount) {
		this.teacherSchoolVipCount = teacherSchoolVipCount;
	}

	public long getStudentCount() {
		return studentCount;
	}

	public void setStudentCount(long studentCount) {
		this.studentCount = studentCount;
	}

	public long getStudentVipCount() {
		return studentVipCount;
	}

	public void setStudentVipCount(long studentVipCount) {
		this.studentVipCount = studentVipCount;
	}

	public String getConUserName() {
		return conUserName;
	}

	public void setConUserName(String conUserName) {
		this.conUserName = conUserName;
	}

	public BigDecimal getOpenMemberLimit() {
		return openMemberLimit;
	}

	public void setOpenMemberLimit(BigDecimal openMemberLimit) {
		this.openMemberLimit = openMemberLimit;
	}

	public BigDecimal getOpenedMember() {
		return openedMember;
	}

	public void setOpenedMember(BigDecimal openedMember) {
		this.openedMember = openedMember;
	}

	public List<VSchool> getChildren() {
		return children;
	}

	public void setChildren(List<VSchool> children) {
		this.children = children;
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

}

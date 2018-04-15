package com.lanking.cloud.domain.yoo.channel;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 用户渠道
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "user_channel")
public class UserChannel implements Serializable {

	private static final long serialVersionUID = -1225482443494125073L;

	/**
	 * 自主注册渠道编码
	 */
	@Transient
	public static final int YOOMATH = 10000;

	/**
	 * 融捷渠道名称
	 */
	@Transient
	public static final String YOUNGEDU = "融捷";

	/**
	 * 渠道代码
	 */
	@Id
	@Column(precision = 5)
	private int code;

	/**
	 * 第一次添加的时候名字(原始名称)
	 */
	@Column(length = 64, name = "original_name")
	private String originalName;

	/**
	 * 名称
	 */
	@Column(length = 64)
	private String name;

	/**
	 * 学校数量
	 */
	@Column(name = "school_count", columnDefinition = "bigint default 0")
	private int schoolCount = 0;

	/**
	 * 教师数量
	 */
	@Column(name = "teacher_count", columnDefinition = "bigint default 0")
	private long teacherCount = 0;

	/**
	 * 教师会员数量
	 */
	@Column(name = "teacher_vip_count", columnDefinition = "bigint default 0")
	private long teacherVipCount = 0;

	/**
	 * 教师校级会员数量
	 */
	@Column(name = "teacher_svip_count", columnDefinition = "bigint default 0")
	private long teacherSchoolVipCount = 0;

	/**
	 * 学生数量
	 */
	@Column(name = "student_count", columnDefinition = "bigint default 0")
	private long studentCount = 0;

	/**
	 * 学生会员数量
	 */
	@Column(name = "student_vip_count", columnDefinition = "bigint default 0")
	private long studentVipCount = 0;

	/**
	 * 可以自行开通会员的额度限制
	 */
	@Column(name = "open_member_limit", scale = 2)
	private BigDecimal openMemberLimit;

	/**
	 * 当前自行开通会员透支额度
	 */
	@Column(name = "opened_member", scale = 2)
	private BigDecimal openedMember;

	/**
	 * 对应的后台管理员
	 */
	@Column(name = "con_user_id")
	private Long conUserId;

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

	public long getTeacherCount() {
		return teacherCount;
	}

	public void setTeacherCount(long teacherCount) {
		this.teacherCount = teacherCount;
	}

	public long getTeacherVipCount() {
		return teacherVipCount;
	}

	public void setTeacherVipCount(long teacherVipCount) {
		this.teacherVipCount = teacherVipCount;
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

	public Long getConUserId() {
		return conUserId;
	}

	public void setConUserId(Long conUserId) {
		this.conUserId = conUserId;
	}

}

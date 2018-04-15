package com.lanking.uxb.service.imperial.value;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lanking.cloud.domain.yoo.member.MemberType;

/**
 * 本场成绩 VO <br>
 * 公布成绩--作业已下发的情况下
 * 
 * @author wangsenhao
 *
 */
public class VImperialExaminationScore implements Serializable {

	private static final long serialVersionUID = 2449168560969684910L;
	/**
	 * 作业平均用时
	 */
	private Integer homeworkTime = 0;
	/**
	 * 平均正确率
	 */
	private BigDecimal rightRate;
	/**
	 * 平均提交率
	 */
	private BigDecimal commitRate;
	/**
	 * 最优班级id
	 */
	private long bestClassId;
	/**
	 * 最优班级名称
	 */
	private String bestClassName;

	private Integer score;
	/**
	 * 老师姓名为X老师（X为老师姓名的第一个字）
	 */
	private String teacherName;
	/**
	 * 老师头像URL
	 */
	private String imageUrl;

	private Long userId;
	/**
	 * 当前排名
	 */
	private Long rank;

	/**
	 * 冲刺题
	 */
	private Integer tag;

	/**
	 * 用户会员类型
	 */
	private MemberType memberType;

	/**
	 * 学生姓名
	 */
	private String studentName;

	/**
	 * 学校名称
	 */
	private String schoolName;

	public Integer getHomeworkTime() {
		return homeworkTime;
	}

	public void setHomeworkTime(Integer homeworkTime) {
		this.homeworkTime = homeworkTime;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public BigDecimal getCommitRate() {
		return commitRate;
	}

	public void setCommitRate(BigDecimal commitRate) {
		this.commitRate = commitRate;
	}

	public long getBestClassId() {
		return bestClassId;
	}

	public void setBestClassId(long bestClassId) {
		this.bestClassId = bestClassId;
	}

	public String getBestClassName() {
		return bestClassName;
	}

	public void setBestClassName(String bestClassName) {
		this.bestClassName = bestClassName;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRank() {
		return rank;
	}

	public void setRank(Long rank) {
		this.rank = rank;
	}

	public Integer getTag() {
		return tag;
	}

	public void setTag(Integer tag) {
		this.tag = tag;
	}

	public MemberType getMemberType() {
		return memberType;
	}

	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

}

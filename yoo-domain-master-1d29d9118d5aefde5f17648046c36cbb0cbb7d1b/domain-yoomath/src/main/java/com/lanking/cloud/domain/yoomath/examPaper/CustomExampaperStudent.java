package com.lanking.cloud.domain.yoomath.examPaper;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 组卷&学生关系
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "custom_exampaper_student")
public class CustomExampaperStudent implements Serializable {

	private static final long serialVersionUID = -5514600456792875693L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 学生ID
	 */
	@Column(name = "student_id")
	private Long studentId;

	/**
	 * 组卷ID {@link CustomExampaper}.id
	 */
	@Column(name = "custom_exampaper_id")
	private long customExampaperId;

	/**
	 * 状态
	 */
	@Column(name = "status", precision = 3)
	private CustomExampaperStudentStatus status;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 统计时间（学生上报结果的时间）
	 */
	@Column(name = "statistics_at", columnDefinition = "datetime(3)")
	private Date statisticsAt;

	/**
	 * 班级ID
	 */
	@Column(name = "class_id")
	private long classId;

	/**
	 * 是否转换组卷信息
	 */
	@Transient
	private boolean hasCustomExampaper = false;
	/**
	 * 是否转换班级信息
	 */
	@Transient
	private boolean hasClazz = false;
	/**
	 * 是否转换习题信息
	 */
	@Transient
	private boolean hasQuestion = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public long getCustomExampaperId() {
		return customExampaperId;
	}

	public void setCustomExampaperId(long customExampaperId) {
		this.customExampaperId = customExampaperId;
	}

	public CustomExampaperStudentStatus getStatus() {
		return status;
	}

	public void setStatus(CustomExampaperStudentStatus status) {
		this.status = status;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getStatisticsAt() {
		return statisticsAt;
	}

	public void setStatisticsAt(Date statisticsAt) {
		this.statisticsAt = statisticsAt;
	}

	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	public boolean isHasCustomExampaper() {
		return hasCustomExampaper;
	}

	public void setHasCustomExampaper(boolean hasCustomExampaper) {
		this.hasCustomExampaper = hasCustomExampaper;
	}

	public boolean isHasClazz() {
		return hasClazz;
	}

	public void setHasClazz(boolean hasClazz) {
		this.hasClazz = hasClazz;
	}

	public boolean isHasQuestion() {
		return hasQuestion;
	}

	public void setHasQuestion(boolean hasQuestion) {
		this.hasQuestion = hasQuestion;
	}

}

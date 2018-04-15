package com.lanking.cloud.domain.yoomath.diagnostic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 诊断-学生-学生在某个班级内最近n次作业的统计
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "diagno_stu_class_latest_hk")
public class DiagnosticStudentClassLatestHomework implements Serializable {

	private static final long serialVersionUID = -7255255144734561370L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private long id;

	/**
	 * 班级ID
	 */
	@Column(name = "class_id")
	private long classId;

	/**
	 * 学生ID
	 */
	@Column(name = "student_id")
	private long studentId;

	/**
	 * 作业ID
	 */
	@Column(name = "homework_id")
	private long homeworkId;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 40)
	private String name;

	/**
	 * 开始时间
	 */
	@Column(name = "start_time", columnDefinition = "datetime(3)")
	private Date startTime;

	/**
	 * 难度
	 */
	@Column(name = "difficulty")
	private BigDecimal difficulty;

	/**
	 * 正确率
	 */
	@Column(name = "right_rate", scale = 2)
	private BigDecimal rightRate;

	/**
	 * 对应作业的班级正确率
	 */
	@Column(name = "class_right_rate", scale = 2)
	private BigDecimal classRightRate;

	/**
	 * 学生在班级内的排名
	 */
	@Column(name = "class_rank", precision = 5)
	private int rank;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public long getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(long homeworkId) {
		this.homeworkId = homeworkId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public BigDecimal getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public BigDecimal getClassRightRate() {
		return classRightRate;
	}

	public void setClassRightRate(BigDecimal classRightRate) {
		this.classRightRate = classRightRate;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

}

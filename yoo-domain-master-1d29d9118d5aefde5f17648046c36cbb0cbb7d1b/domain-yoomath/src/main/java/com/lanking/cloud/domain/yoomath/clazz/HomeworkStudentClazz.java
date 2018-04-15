package com.lanking.cloud.domain.yoomath.clazz;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 班级&学生关系
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "homework_student_class")
public class HomeworkStudentClazz implements Serializable {

	private static final long serialVersionUID = -7617591347747935916L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

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
	 * 备注
	 */
	@Column(name = "mark", length = 40)
	private String mark;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 加入时间
	 */
	@Column(name = "join_at", columnDefinition = "datetime(3)")
	private Date joinAt;

	/**
	 * 退出时间
	 */
	@Column(name = "exit_at", columnDefinition = "datetime(3)")
	private Date exitAt;

	/**
	 * 状态
	 */
	@Column(name = "status", precision = 3)
	private Status status = Status.ENABLED;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getJoinAt() {
		return joinAt;
	}

	public void setJoinAt(Date joinAt) {
		this.joinAt = joinAt;
	}

	public Date getExitAt() {
		return exitAt;
	}

	public void setExitAt(Date exitAt) {
		this.exitAt = exitAt;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}

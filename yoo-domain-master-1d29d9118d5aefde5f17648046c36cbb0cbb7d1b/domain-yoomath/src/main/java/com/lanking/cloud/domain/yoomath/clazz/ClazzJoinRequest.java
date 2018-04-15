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
 * 学生请求加入班级请求
 * 
 * <pre>
 * 1.一个学生针对一个班级只能有一条待处理的请求。
 * </pre>
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "clazz_join_request")
public class ClazzJoinRequest implements Serializable {
	private static final long serialVersionUID = -5751080789026328562L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private long id;

	/**
	 * 学生ID
	 */
	@Column(name = "student_id")
	private long studentId;

	/**
	 * 当时请求时学生所填的真实姓名
	 */
	@Column(name = "real_name", length = 40)
	private String realName;

	/**
	 * 教师ID
	 */
	@Column(name = "teacher_id")
	private long teacherId;

	/**
	 * 班级ID
	 */
	@Column(name = "homework_class_id")
	private long homeworkClassId;

	/**
	 * 创建时间（申请时间）
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	/**
	 * 请求处理状态
	 */
	@Column(name = "request_status", precision = 3)
	private ClazzJoinRequestStatus requestStatus;

	/**
	 * 删除状态
	 */
	@Column(name = "delete_status", precision = 3, columnDefinition = "bigint default 0")
	private Status deleteStatus;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(long teacherId) {
		this.teacherId = teacherId;
	}

	public long getHomeworkClassId() {
		return homeworkClassId;
	}

	public void setHomeworkClassId(long homeworkClassId) {
		this.homeworkClassId = homeworkClassId;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public ClazzJoinRequestStatus getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(ClazzJoinRequestStatus requestStatus) {
		this.requestStatus = requestStatus;
	}

	public Status getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(Status deleteStatus) {
		this.deleteStatus = deleteStatus;
	}
}

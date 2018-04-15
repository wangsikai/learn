package com.lanking.cloud.domain.yoomath.interaction;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 师生互动
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "interaction")
public class Interaction implements Serializable {

	private static final long serialVersionUID = -6849159297013704429L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 教师用户ID
	 */
	@Column(name = "teacher_id")
	private Long teacherId;

	/**
	 * 学生用户ID
	 */
	@Column(name = "student_id")
	private Long studentId;

	/**
	 * 班级ID
	 */
	@Column(name = "class_id")
	private Long classId;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	/**
	 * 状态
	 */
	@Column(precision = 3, nullable = false)
	private InteractionStatus status;

	/**
	 * 互动类型(原因)
	 */
	@Column(precision = 3, nullable = false)
	private InteractionType type;

	/**
	 * 首页是否显示
	 */
	@Column(name = "homepage_show", columnDefinition = "bit default 1")
	private boolean homePageShow = true;

	/**
	 * 附加参数1
	 * 
	 * <pre>
	 * 1.学生排名字段
	 * </pre>
	 */
	@Column(name = "p1", length = 32)
	private String p1;

	/**
	 * 附加参数2
	 * 
	 * <pre>
	 * 1.学生进步几名/退步几名/有多少次没有提交作业
	 * </pre>
	 */
	@Column(name = "p2", length = 32)
	private String p2;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public InteractionStatus getStatus() {
		return status;
	}

	public void setStatus(InteractionStatus status) {
		this.status = status;
	}

	public InteractionType getType() {
		return type;
	}

	public void setType(InteractionType type) {
		this.type = type;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public boolean isHomePageShow() {
		return homePageShow;
	}

	public void setHomePageShow(boolean homePageShow) {
		this.homePageShow = homePageShow;
	}

	public String getP1() {
		return p1;
	}

	public void setP1(String p1) {
		this.p1 = p1;
	}

	public String getP2() {
		return p2;
	}

	public void setP2(String p2) {
		this.p2 = p2;
	}

}

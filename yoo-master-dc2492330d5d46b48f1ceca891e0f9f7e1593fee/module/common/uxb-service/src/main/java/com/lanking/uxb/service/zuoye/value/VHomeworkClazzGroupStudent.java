package com.lanking.uxb.service.zuoye.value;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生组信息.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2017年3月20日
 */
public class VHomeworkClazzGroupStudent implements Serializable {
	private static final long serialVersionUID = -224079492188035657L;

	private Long id;

	/**
	 * 班级ID
	 */
	private long classId;

	/**
	 * 分组ID
	 */
	private long groupId;

	/**
	 * 学生ID
	 */
	private long studentId;

	/**
	 * 创建时间
	 */
	private Date createAt;

	/**
	 * 更新时间
	 */
	private Date updateAt;

	// 所属分组.
	private VHomeworkClazzGroup group;

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

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
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

	public VHomeworkClazzGroup getGroup() {
		return group;
	}

	public void setGroup(VHomeworkClazzGroup group) {
		this.group = group;
	}
}

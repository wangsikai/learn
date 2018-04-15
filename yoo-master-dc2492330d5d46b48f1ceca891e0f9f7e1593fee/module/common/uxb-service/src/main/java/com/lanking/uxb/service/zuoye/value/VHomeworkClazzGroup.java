package com.lanking.uxb.service.zuoye.value;

import java.io.Serializable;
import java.util.Date;

import com.lanking.cloud.sdk.bean.Status;

/**
 * 班级组信息.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2017年3月17日
 */
public class VHomeworkClazzGroup implements Serializable {
	private static final long serialVersionUID = 2083782181218543653L;

	private Long id;

	/**
	 * 班级ID
	 */
	private long classId;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 学生数量
	 */
	private int studentCount = 0;

	/**
	 * 创建时间
	 */
	private Date createAt;

	/**
	 * 更新时间
	 */
	private Date updateAt;

	/**
	 * 状态
	 */
	private Status status;
	/**
	 * 是否选中组，默认为false
	 */
	private boolean selected = false;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStudentCount() {
		return studentCount;
	}

	public void setStudentCount(int studentCount) {
		this.studentCount = studentCount;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}

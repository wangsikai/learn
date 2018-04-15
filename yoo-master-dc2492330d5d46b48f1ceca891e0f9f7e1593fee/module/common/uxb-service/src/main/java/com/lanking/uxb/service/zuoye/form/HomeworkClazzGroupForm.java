package com.lanking.uxb.service.zuoye.form;

import java.util.List;

/**
 * 创建班级组form
 *
 * @author xinyu.zhou
 * @since 3.9.3
 */
public class HomeworkClazzGroupForm {
	// 学生id列表
	private List<Long> studentIds;
	// 名称
	private String name;
	// 班级id
	private Long classId;
	// 教师id
	private Long teacherId;

	public List<Long> getStudentIds() {
		return studentIds;
	}

	public void setStudentIds(List<Long> studentIds) {
		this.studentIds = studentIds;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public Long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}
}

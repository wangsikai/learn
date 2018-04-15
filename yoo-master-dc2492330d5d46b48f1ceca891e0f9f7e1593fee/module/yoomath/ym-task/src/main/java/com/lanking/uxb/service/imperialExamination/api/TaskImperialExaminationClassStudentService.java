package com.lanking.uxb.service.imperialExamination.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.user.Student;

public interface TaskImperialExaminationClassStudentService {
	/**
	 * 根据班级id查询该班级的所有学生
	 * 
	 * @param classId
	 */
	List<Student> query(long classId);

}

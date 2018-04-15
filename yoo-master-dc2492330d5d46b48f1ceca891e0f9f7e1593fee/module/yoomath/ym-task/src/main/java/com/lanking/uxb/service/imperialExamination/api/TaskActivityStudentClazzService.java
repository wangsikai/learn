package com.lanking.uxb.service.imperialExamination.api;

import java.util.List;

/**
 * 学生班级相关接口
 * 
 * @since 2.1
 * @author zemin.song
 * @version 2017年4月12日
 */
public interface TaskActivityStudentClazzService {

	List<Long> listClassStudents(long classId);
}

package com.lanking.uxb.zycon.homework.api;

import java.util.List;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;

/**
 * @author xinyu.zhou
 * @since yoomath V1.5
 */
public interface ZycHomeworkStudentClazzService {

	List<HomeworkStudentClazz> list(long classId);
}

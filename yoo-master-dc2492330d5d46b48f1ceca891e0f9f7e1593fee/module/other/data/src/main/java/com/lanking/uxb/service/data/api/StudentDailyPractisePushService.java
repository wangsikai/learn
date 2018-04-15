package com.lanking.uxb.service.data.api;

import java.util.Collection;

import com.lanking.cloud.domain.yoo.user.Student;

/**
 * 每日作业推送服务
 *
 * @author xinyu.zhou
 * @since yoomath(mobile) V1.0.0
 */
public interface StudentDailyPractisePushService {
	/**
	 * 推送题目
	 *
	 * @param students
	 *            学生列表
	 */
	void push(Collection<Student> students);
}

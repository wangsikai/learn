package com.lanking.uxb.service.web.api;

import java.util.Map;

import com.lanking.cloud.domain.yoo.user.Student;

/**
 * 假期作业进度及生成相关
 *
 * @author xinyu.zhou
 * @since 2.0.3
 */
public interface DailyPractiseGenerateService {

	/**
	 * 生成学生每日练习数据<br/>
	 *
	 * @param student
	 *            学生
	 * @param count
	 *            拉取题目数量
	 * @return {@link Map}
	 */
	Map<String, Object> generate(Student student, int count);
}

package com.lanking.uxb.service.examactivity001.api;

import com.lanking.cloud.domain.yoo.activity.exam001.ExamActivity001User;

/**
 * 期末考试活动用户相关接口
 * 
 * @author qiuxue.jiang
 *
 * @version 2017年12月27日
 */
public interface ExamActivity01UserService {
	
	/**
	 * 获取当前学生的资料信息
	 * 
	 * @param code
	 * @param userId
	 */
	ExamActivity001User getUser(Long code,Long userId);
	
	/**
	 * 添加用户
	 * 
	 * @param code
	 * @param userId
	 */
	void addUser(ExamActivity001User user);
	
	/**
	 * 修改用户的资料
	 * 
	 * @param code
	 * @param userId
	 */
	void updateUser(ExamActivity001User user);
	
}

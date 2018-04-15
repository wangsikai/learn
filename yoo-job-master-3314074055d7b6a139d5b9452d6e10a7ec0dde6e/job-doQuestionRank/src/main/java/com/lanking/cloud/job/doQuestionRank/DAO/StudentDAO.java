package com.lanking.cloud.job.doQuestionRank.DAO;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.user.Student;

/**
 * Student 相关数据库操作接口
 * 
 * @since 1.4.7
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年9月19日
 */
public interface StudentDAO {

	/**
	 * 统计学生在一段时间内做题信息
	 * 
	 * @param userIds
	 * @return
	 */
	Map<Long, Student> taskListStudent(List<Long> userIds);
}

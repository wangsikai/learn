package com.lanking.uxb.service.imperialExamination.api;

import java.util.List;

import com.lanking.uxb.service.resources.ex.ExerciseException;
import com.lanking.uxb.service.resources.ex.HomeworkException;

/**
 * 提供作业习题相关操作接口
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月22日
 */
public interface TaskActivityHomeworkQuestionService {

	/**
	 * 获取一次作业中的习题集合
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @return 习题集合
	 * @throws ExerciseException
	 */
	List<Long> getQuestion(long homeworkId) throws HomeworkException;

}

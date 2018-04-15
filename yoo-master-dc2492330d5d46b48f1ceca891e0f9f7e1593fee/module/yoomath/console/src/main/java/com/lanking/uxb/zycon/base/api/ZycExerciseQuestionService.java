package com.lanking.uxb.zycon.base.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.homework.ExerciseQuestion;
import com.lanking.uxb.zycon.common.ex.YoomathConsoleException;

/**
 * 提供练习题相关操作接口
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年8月31日 上午11:12:15
 */
public interface ZycExerciseQuestionService {

	/**
	 * 获取一个练习题
	 * 
	 * @param id
	 *            练习题ID
	 * @return 练习题
	 * @throws YoomathConsoleException
	 */
	ExerciseQuestion get(long id) throws YoomathConsoleException;

	/**
	 * 获取多个练习题
	 * 
	 * @param ids
	 *            练习题IDs
	 * @return 练习题集合
	 * @throws YoomathConsoleException
	 */
	Map<Long, ExerciseQuestion> mget(Collection<Long> ids) throws YoomathConsoleException;

	/**
	 * 获取多个练习题
	 * 
	 * @param ids
	 *            练习题IDs
	 * @return 练习题集合
	 * @throws YoomathConsoleException
	 */
	List<ExerciseQuestion> mgetList(Collection<Long> ids) throws YoomathConsoleException;

	/**
	 * 根据练习ID获取练习题集合
	 * 
	 * @param exerciseId
	 *            练习ID
	 * @return 练习题集合
	 * @throws YoomathConsoleException
	 */
	List<ExerciseQuestion> getExerciseQuestion(long exerciseId) throws YoomathConsoleException;

	/**
	 * 根据练习ID获取习题ID集合
	 * 
	 * @param exerciseId
	 * @return 习题ID集合
	 * @throws YoomathConsoleException
	 */
	List<Long> getQuestion(long exerciseId) throws YoomathConsoleException;

	/**
	 * 插入 一条习题
	 * 
	 * @param exerciseId
	 *            练习ID
	 * @param questionId
	 *            习题ID
	 * @param initSequence
	 *            指定练习序号(为空时自动追加在练习最后)
	 * @return 练习题
	 * @throws YoomathConsoleException
	 */
	ExerciseQuestion appendQuestion(long exerciseId, long questionId, Integer initSequence) throws YoomathConsoleException;

	/**
	 * 从练习题中移除一条习题
	 * 
	 * @param exerciseId
	 *            练习ID
	 * @param questionId
	 *            习题ID
	 * @throws YoomathConsoleException
	 */
	void removeQuestion(long exerciseId, long questionId) throws YoomathConsoleException;

	/**
	 * 上移习题
	 * 
	 * @param exerciseId
	 *            练习ID
	 * @param questionId
	 *            习题ID
	 * @throws YoomathConsoleException
	 */
	void upQuestion(long exerciseId, long questionId) throws YoomathConsoleException;

	/**
	 * 下移习题
	 * 
	 * @param exerciseId
	 *            练习ID
	 * @param questionId
	 *            习题ID
	 * @throws YoomathConsoleException
	 */
	void downQuestion(long exerciseId, long questionId) throws YoomathConsoleException;

	/**
	 * 获取一条练习题记录
	 * 
	 * @param exerciseId
	 *            习题ID
	 * @param questionId
	 *            习题ID
	 * @return 练习题
	 * @throws YoomathConsoleException
	 */
	ExerciseQuestion findOne(long exerciseId, long questionId) throws YoomathConsoleException;

}

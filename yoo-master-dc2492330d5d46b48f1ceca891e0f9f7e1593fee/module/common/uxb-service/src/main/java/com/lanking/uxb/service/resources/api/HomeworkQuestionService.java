package com.lanking.uxb.service.resources.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.yoomath.homework.HomeworkQuestion;
import com.lanking.uxb.service.resources.ex.ExerciseException;
import com.lanking.uxb.service.resources.ex.HomeworkException;

/**
 * 提供作业习题相关操作接口
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月22日
 */
public interface HomeworkQuestionService {

	/**
	 * 获取单个作业习题
	 * 
	 * @param id
	 *            作业习题ID
	 * @return 作业习题
	 * @throws ExerciseException
	 */
	HomeworkQuestion get(long id) throws HomeworkException;

	/**
	 * 批量获取作业习题
	 * 
	 * @param ids
	 *            作业习题IDs
	 * @return 作业习题集合
	 * @throws ExerciseException
	 */
	Map<Long, HomeworkQuestion> mget(Collection<Long> ids) throws HomeworkException;

	/**
	 * 批量获取作业习题
	 * 
	 * @param ids
	 *            作业习题IDs
	 * @return 作业习题集合
	 * @throws ExerciseException
	 */
	List<HomeworkQuestion> mgetList(Collection<Long> ids) throws HomeworkException;

	/**
	 * 获取一次作业中的作业习题集合
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @return 作业习题集合
	 * @throws ExerciseException
	 */
	List<HomeworkQuestion> getHomeworkQuestion(long homeworkId) throws HomeworkException;

	/**
	 * 获取一次作业中的习题集合
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @return 习题集合
	 * @throws ExerciseException
	 */
	List<Long> getQuestion(long homeworkId) throws HomeworkException;

	/**
	 * 追加或者插入一条习题到作业中
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @param questionId
	 *            习题ID
	 * @param initSequence
	 *            序号
	 * @return 作业习题
	 * @throws ExerciseException
	 */
	HomeworkQuestion appendQuestion(long homeworkId, long questionId, Integer initSequence) throws HomeworkException;

	/**
	 * 从作业中删除一条习题
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @param questionId
	 *            习题ID
	 * @throws ExerciseException
	 */
	void removeQuestion(long homeworkId, long questionId) throws HomeworkException;

	/**
	 * 上移一条习题
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @param questionId
	 *            习题ID
	 * @throws ExerciseException
	 */
	void upQuestion(long homeworkId, long questionId) throws HomeworkException;

	/**
	 * 下移一条习题
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @param questionId
	 *            习题ID
	 * @throws ExerciseException
	 */
	void downQuestion(long homeworkId, long questionId) throws HomeworkException;

	/**
	 * 复用习题页
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @param exerciseId
	 *            习题ID
	 * @throws HomeworkException
	 */
	void createByExercise(long homeworkId, long exerciseId) throws HomeworkException;

	/**
	 * 根据作业ID和习题ID获取作业习题
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @param exerciseId
	 *            习题ID
	 * @return 作业习题
	 * @throws HomeworkException
	 */
	HomeworkQuestion findOne(long homeworkId, long questionId) throws HomeworkException;

	/**
	 * count作业里面的习题数量
	 * 
	 * @param homeworkId
	 * @return
	 */
	long countQuestion(long homeworkId);

	/**
	 * 根据作业Id删除作业习题
	 * 
	 * @param homeworkId
	 *            作业ID
	 */
	void deleteByHomework(long homeworkId);
	
	/**
	 * 根据题目类型获取某份作业下的题目
	 * @param hkid
	 * @param type
	 * @return
	 */
	List<Long> findHomeworkQuestionsByType(long hkid,int type);
}

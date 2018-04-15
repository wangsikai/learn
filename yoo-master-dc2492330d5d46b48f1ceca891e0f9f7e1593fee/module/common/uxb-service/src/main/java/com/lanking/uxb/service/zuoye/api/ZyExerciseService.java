package com.lanking.uxb.service.zuoye.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.yoomath.homework.Exercise;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.service.resources.ex.ExerciseException;

/**
 * 练习题相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月15日
 */
public interface ZyExerciseService {

	/**
	 * 查找某个老师在某个教材下最新的练习
	 * 
	 * @since 2.1
	 * @param teacherId
	 *            教师ID
	 * @param textbookCode
	 *            教材代码
	 * @return 最新的练习
	 */
	Exercise findLatestOne(long teacherId, Integer textbookCode);

	/**
	 * 查找某个老师在某个教材下最新的练习(智能)
	 * 
	 * @since 2.1
	 * @param teacherId
	 *            教师ID
	 * @param textbookCode
	 *            教材代码
	 * @return 最新的练习
	 */
	Exercise findNoBooksIdLatestOne(long teacherId, Integer textbookCode);

	/**
	 * 查找某个老师在某个预置习题下的最新练习
	 * 
	 * @since 2.1
	 * @param teacherId
	 *            教师ID
	 * @param textbookExerciseId
	 *            预置习题ID
	 * @return 最新的练习
	 */
	Exercise findLatestOne(long teacherId, Long textbookExerciseId, Long sectionCode, Long bookId);

	/**
	 * 通过教师ID和预置练习ID拉取题目列表
	 * 
	 * @since 2.1
	 * @param teacherId
	 *            教师ID
	 * @param textbookExerciseId
	 *            预置练习ID
	 * @param exerciseId
	 *            习题ID如果为空则根据textbookExerciseId生成
	 * @return 习题ID列表
	 */
	List<Long> listQuestions(long teacherId, long textbookExerciseId, Long exerciseId);

	/**
	 * 随机获取某个章节下的题目
	 * 
	 * @since 2.1
	 * @param textbookExerciseId
	 *            预置练习ID
	 * @param sectionCode
	 *            章节ID
	 * @param types
	 *            题目类型
	 * @param count
	 *            获取数量
	 * @param exQIds
	 *            排除的习题IDs
	 * @param minDifficulty
	 *            最小难度,增加难度使用
	 * @param maxDifficulty
	 *            最大难度,降低难度使用
	 * @return 题目列表
	 */
	List<Question> pullQuestions(Long textbookExerciseId, Long sectionCode, Set<Type> types, int count,
			List<Long> exQIds, BigDecimal minDifficulty, BigDecimal maxDifficulty);

	/**
	 * 计算某个老师使用此预置习题页当天已经布置过几次
	 * 
	 * @since 2.1
	 * @param teacherId
	 *            教师ID
	 * @param textbookExerciseId
	 *            预置练习ID
	 * @return 数量
	 */
	Long countTodayExercise(long teacherId, long textbookExerciseId, Long sectionCode);

	/**
	 * 创建一个空的练习
	 * 
	 * @since 2.1
	 * @param exercise
	 *            Exercise
	 * @return Exercise
	 */
	Exercise createBlankExercise(Exercise exercise) throws ExerciseException;

	/**
	 * 创建一个练习,包含习题
	 * 
	 * @since 2.1
	 * @param exercise
	 *            Exercise
	 * @param questionIds
	 *            习题IDs
	 * @return Exercise
	 */
	Exercise createExercise(Exercise exercise, List<Long> questionIds) throws ExerciseException;

	/**
	 * 查询历史作业的对应习题页(作业状态为下发状态)
	 * 
	 * @since 2.1
	 * @param teacherId
	 *            教师ID
	 * @param sectionCode
	 *            章节代码
	 * @param pageable
	 *            分页条件
	 * @return 分页数据
	 */
	Page<Exercise> query(long teacherId, long sectionCode, Pageable pageable);

}

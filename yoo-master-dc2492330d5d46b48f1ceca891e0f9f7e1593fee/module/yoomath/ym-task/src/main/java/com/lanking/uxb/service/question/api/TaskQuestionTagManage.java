package com.lanking.uxb.service.question.api;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.lanking.cloud.domain.common.baseData.ExaminationPoint;
import com.lanking.cloud.domain.common.baseData.QuestionTag;
import com.lanking.cloud.domain.common.baseData.QuestionTagType;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

/**
 * 习题标签相关.
 * 
 * @author wlche
 *
 */
public interface TaskQuestionTagManage {

	/**
	 * 获取所有的标签.
	 * 
	 * @return
	 */
	List<QuestionTag> listAll(Status status, QuestionTagType questionTagType);

	/**
	 * 分页查询习题.
	 * 
	 * @param pageable
	 * @return
	 */
	Page<Question> findQuestionsForInitTag(Date now, Pageable pageable);

	/**
	 * 分页查询带典型题的考点.
	 * 
	 * @param pageable
	 * @return
	 */
	Page<ExaminationPoint> findExaminationPointForInitTag(Pageable pageable);

	/**
	 * 处理初始化习题标签.
	 * 
	 * @param questions
	 *            习题集合
	 */
	void handleInitQuestions(List<Question> questions);

	/**
	 * 处理初始化习题标签（典型题）.
	 * 
	 * @param questions
	 *            习题集合
	 */
	void handleInitExaminationPointQuestions(Set<Long> questions);

	/**
	 * 删除系统标签对应的习题.
	 */
	void deleteSystemQuestions();

	/**
	 * 分页查询热门习题ID.
	 * 
	 * @return
	 */
	Page<Long> findHotQuestionsForInitTag(int minPulishCount, Pageable pageable);

	/**
	 * 分页查询易错习题ID.
	 * 
	 * @return
	 */
	Page<Long> findFallQuestionsForInitTag(int maxRightRate, int minDoNum, Pageable pageable);

	/**
	 * 分页查询好题ID.
	 * 
	 * @return
	 */
	Page<Long> findGoodQuestionsForInitTag(int minCollectCount, Pageable pageable);

	/**
	 * 处理初始化习题标签.
	 * 
	 * @param questionIds
	 *            习题集合
	 */
	void handleInitTagQuestions(List<Long> questionIds, long tagCode);

	/**
	 * TASK-分页查询热门习题ID（需要删除的部分）.
	 * 
	 * @return
	 */
	Page<Long> findHotQuestionsForDEL(int minPulishCount, Pageable pageable);

	/**
	 * TASK-分页查询热门习题ID（需要增加的部分）.
	 * 
	 * @return
	 */
	Page<Long> findHotQuestionsForADD(int minPulishCount, Pageable pageable);

	/**
	 * TASK-分页查询易错习题ID（需要删除的部分）.
	 * 
	 * @return
	 */
	Page<Long> findFallQuestionsForDEL(int maxRightRate, int minDoNum, Pageable pageable);

	/**
	 * TASK-分页查询易错习题ID（需要增加的部分）.
	 * 
	 * @return
	 */
	Page<Long> findFallQuestionsForADD(int maxRightRate, int minDoNum, Pageable pageable);

	/**
	 * TASK-分页查询好题ID（需要删除的部分）.
	 * 
	 * @return
	 */
	Page<Long> findGoodQuestionsForDEL(int minCollectCount, Pageable pageable);

	/**
	 * TASK-分页查询好题ID（需要增加的部分）.
	 * 
	 * @return
	 */
	Page<Long> findGoodQuestionsForADD(int minCollectCount, Pageable pageable);

	/**
	 * TASK-处理标签任务这边删除或添加习题.
	 * 
	 * @param delQuestionIds
	 *            删除的习题
	 * @param addQuestionIds
	 *            增加的习题
	 * @param tagCode
	 *            标签CODE
	 */
	void handleTaskTagQuestions(List<Long> delQuestionIds, List<Long> addQuestionIds, long tagCode);
}

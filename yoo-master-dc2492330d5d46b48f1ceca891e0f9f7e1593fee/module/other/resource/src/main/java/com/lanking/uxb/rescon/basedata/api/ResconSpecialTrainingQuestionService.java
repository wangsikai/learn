package com.lanking.uxb.rescon.basedata.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.specialTraining.SpecialTrainingQuestion;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

/**
 * 针对性训练题目接口
 * 
 * @since 2.0.1
 * @author wangsenhao
 *
 */
public interface ResconSpecialTrainingQuestionService {
	/**
	 * 查询针对训练里题目ID的集合
	 * 
	 * @param specialTrainingId
	 * @return
	 */
	Page<SpecialTrainingQuestion> queryQuestionList(Pageable p, long specialTrainingId);

	/**
	 * 针对性训练题目保存
	 * 
	 * @param specialTrainingId
	 * @param questionIds
	 * @param createId
	 */
	void saveQuestion(long specialTrainingId, List<Long> questionIds, Long createId);

	/**
	 * 删除针对性训练题目
	 * 
	 * @param specialTrainingId
	 */
	void deleteQuestions(long specialTrainingId);

	/**
	 * 删除针对性训练题目
	 * 
	 * @param specialTrainingId
	 */
	void deleteQuestion(long specialTrainingId, Long questionId);

	/**
	 * 查询训练的所有题目,不翻页
	 * 
	 * @param specialTrainingId
	 * @return
	 */
	List<SpecialTrainingQuestion> questionList(long specialTrainingId);

	/**
	 * 获取训练题目的统计
	 * 
	 * @param trainId
	 * @return
	 */
	Map<Integer, Long> getQuestionStat(Long trainId);

	/**
	 * 替换习题.
	 * 
	 * @since rescon v1.3.3
	 * @param trainId
	 *            针对性训练ID
	 * @param oldQuestionId
	 *            旧习题ID
	 * @param newQuestionId
	 *            新习题ID
	 */
	void changeQuestion(long trainId, long oldQuestionId, long newQuestionId);
}

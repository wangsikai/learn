package com.lanking.uxb.rescon.exam.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperQuestion;
import com.lanking.uxb.rescon.exam.value.VExamPaperTopic;

/**
 * 提供试卷习题相关接口
 * 
 * @since 2.5
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年10月21日 下午3:56:14
 */
public interface ResconExamPaperQuestionManage {

	/**
	 * 更新试卷中的题目
	 * 
	 * @param examId
	 *            试卷ID
	 * @param qIds
	 *            题目ID
	 * @param createId
	 *            创建者Id
	 * @param vmap
	 *            构造好的含有题目类型的map
	 * @param examTopicScoreMap
	 *            已存在的题型 分数MAP
	 */
	void updateExamQuesions(Long examId, List<Long> qIds, Long createId, Map<Long, VExamPaperTopic> vmap,
			Map<String, Integer> examTopicScoreMap);

	/**
	 * 由试卷ID 获取试卷下所有题目
	 * 
	 * @param examId
	 * @return 试卷题目列表
	 */
	List<ExamPaperQuestion> getExamQuestionByExam(Long examId);

	/**
	 * 设置该题型下 题目分数
	 * 
	 * @param examTopicId
	 * @param score
	 */
	void updateScoreByTopic(Long examTopicId, Integer score);

	/**
	 * 保存试卷中的题目顺序
	 * 
	 * @param topicList
	 * @param id
	 * @param userId
	 */
	void save(List<VExamPaperTopic> topicList, Long examId, long userId);

	/**
	 * 编辑试卷中单体分数
	 * 
	 * @param examId
	 *            试卷ID
	 * @param questionId
	 *            题目ID
	 * @param score
	 *            分数
	 */
	void editScore(Long examId, Long questionId, Integer score);

	/**
	 * 删除试卷与习题之间的关联.
	 * 
	 * @param questionId
	 */
	void deleteQuestionFromExam(Long questionId);
}

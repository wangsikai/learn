package com.lanking.uxb.rescon.exam.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperTopic;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperTopicType;
import com.lanking.uxb.rescon.exam.value.VExamPaperTopic;

/**
 * 提供试卷题型相关接口
 * 
 * @since 2.5
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年10月21日 下午3:56:14
 */
public interface ResconExamTopicManage {

	/**
	 * 由试卷ID获取 题型列表
	 * 
	 * @param examId
	 * @return
	 */
	List<ExamPaperTopic> getTopicsByExam(Long examId);

	/**
	 * 构造一个在单独的exam 中 status 和topic 的map
	 * 
	 * @param examId
	 * @return
	 */
	Map<ExamPaperTopicType, ExamPaperTopic> getTopicsMap(Long examId);

	/**
	 * 由试卷ID获取 题型列表
	 * 
	 * @param keys
	 * @return
	 */
	Map<Long, List<ExamPaperTopic>> mgetTopicsByExam(Collection<Long> keys);

	/**
	 * 根据试卷题型ids 排序， examTopicIds 按顺序
	 * 
	 * @param examTopicIds
	 */
	void sortTopic(List<Long> examTopicIds);

	/**
	 * 编辑试卷题型，包含设置该题型下每题分数
	 * 
	 * @param examTopicId
	 *            试卷题型ID
	 * @param examTopicName
	 *            试卷题型名称
	 * @param score
	 *            题型 下 每题分数
	 * @return
	 */
	ExamPaperTopic edit(Long examTopicId, String examTopicName, Integer score);

	/**
	 * 更新试卷中尚还没有的题目类型
	 * 
	 * @param examId
	 * @param topics
	 */
	void updateByTopic(Long examId, List<VExamPaperTopic> topics);

	/**
	 * 保存试卷题目类型
	 * 
	 * @param topicList
	 *            题目类型
	 * @param examId
	 *            试卷ID
	 * @return 试卷类型列表
	 */
	List<ExamPaperTopic> save(List<VExamPaperTopic> topicList, Long examId);

}

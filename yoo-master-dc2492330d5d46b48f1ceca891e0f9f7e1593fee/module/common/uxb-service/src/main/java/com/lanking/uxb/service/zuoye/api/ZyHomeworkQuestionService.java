package com.lanking.uxb.service.zuoye.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.yoomath.homework.HomeworkQuestion;

/**
 * 悠作业:作业题目接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月14日
 */
public interface ZyHomeworkQuestionService {

	/**
	 * 根据作业id获取作业题目列表
	 * 
	 * @since 2.1
	 * @param homeworkIds
	 *            作业IDs
	 * @return 作业题目集合
	 */
	List<HomeworkQuestion> findByHomework(Collection<Long> homeworkIds);

	/**
	 * 根据作业id获取题目
	 * 
	 * @param homeworkId
	 * @return
	 */
	List<Double> getHkQuestion(Long homeworkId);

	/**
	 * 作业中某题 做错的学生 id和 studentHomeworkQuestion的Id map
	 * 
	 * @param homeworkId
	 * @param questionId
	 * @return
	 */
	List<Map> listWrongStu(long homeworkId, long questionId);

	/**
	 * 得到一份作业中待批改的题目
	 *
	 * @param homeworkId
	 *            作业id
	 * @return {@link List}
	 */
	List<HomeworkQuestion> findCorrectQuestions(long homeworkId);

	/**
	 * 获得作业中的所有题目.
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @return
	 */
	List<Question> findQuestionByHomework(long homeworkId);
}

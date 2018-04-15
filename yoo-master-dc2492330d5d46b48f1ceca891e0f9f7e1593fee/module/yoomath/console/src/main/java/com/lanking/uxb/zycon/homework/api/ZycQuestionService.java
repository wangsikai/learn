package com.lanking.uxb.zycon.homework.api;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 题目处理Service
 * 
 * @author xinyu.zhou
 * @since yoomath V1.4
 */
public interface ZycQuestionService {
	/**
	 * 根据题目id查询题目
	 *
	 * @param id
	 *            查询的大小
	 * @return {@link Question}
	 */
	Question zycFindQuestion(long id);
	
	/**
	 * 查询待批改的题目
	 *
	 * @param size
	 *            查询的大小
	 * @return {@link Question}
	 */
	List<Question> zycFindStuHkQuestions(int size);

	/**
	 * 查询作业下的所有题目
	 *
	 * @param homeworkId
	 *            作业id
	 * @return 作业下面的所有题目
	 */
	List<Question> zycFindHKQuestions(long homeworkId);

	/**
	 * 得到待批改题目的数量
	 *
	 * @param lastCommitMinute
	 *            最少多长时间提交
	 * @return 数量
	 */
	Long countUnCommitQuestions(int lastCommitMinute);

	/**
	 * mget Question
	 *
	 * @param qids
	 *            Question的id
	 * @return {@link Map}
	 */
	Map<Long, Question> mget(Collection<Long> qids);

	/**
	 * 获得题目的编号
	 *
	 * @param id
	 *            Question id
	 * @return 题目编号
	 */
	String getQuestionCode(long id);

	/**
	 * 查询待确认的题目
	 *
	 * @param pageable
	 *            {@link Pageable}
	 * @return {@link Page}
	 */
	Map<String, Object> zycFindConfirmQuestions(Pageable pageable);
	
	/**
	 * 通过题目编号查询待确认的题目
	 *
	 * @param pageable
	 * @param questionCode      
	 * @return
	 */
	Map<String, Object> zycFindConfirmQuestionsByCode(Pageable pageable, String questionCode);
}

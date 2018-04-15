package com.lanking.uxb.service.question.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.book.BookQuestion;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

/**
 * 题目操作接口
 * 
 * @since 2.3.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年8月18日
 */
public interface QuestionService {

	Question get(long id);

	Map<Long, Question> mget(Collection<Long> ids);

	List<Question> mgetList(Collection<Long> ids);

	List<Question> getSubQuestions(long id);

	Map<Long, List<Question>> mgetSubQuestions(Collection<Long> ids);

	Question findByCode(String code);

	/**
	 * 得到题目预估时间
	 *
	 * @since 3.9.0
	 *
	 * @param type
	 *            题目类型
	 * @param difficulty
	 *            难度
	 * @param subjectCode
	 *            学科
	 * @return 预估做题时间
	 */
	int calPredictTime(Question.Type type, double difficulty, int subjectCode);

	/**
	 * 得到题目预估时间
	 *
	 * @since 3.9.0
	 *
	 * @param q
	 *            {@link Question}
	 * @return 预估做题时间
	 */
	int calPredictTime(Question q);

	/**
	 * 查询题目列表
	 * 
	 * @param bookCatalogId
	 * @param pageable
	 * @param questionType
	 *            题目类型
	 * @param diff1
	 *            题目难度范围
	 * @param diff2
	 * @return
	 */
	CursorPage<Long, BookQuestion> queryQuestionByCatalog(Long bookCatalogId, CursorPageable<Long> pageable,
			Type questionType, Double diff1, Double diff2);
	
}

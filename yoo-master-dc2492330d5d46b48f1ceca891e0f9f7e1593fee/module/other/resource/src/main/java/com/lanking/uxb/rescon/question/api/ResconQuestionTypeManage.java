package com.lanking.uxb.rescon.question.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.QuestionType;
import com.lanking.cloud.domain.common.resource.question.Question;

/**
 * 习题题型服务.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年3月6日
 */
public interface ResconQuestionTypeManage {

	QuestionType get(Integer code);

	Map<Integer, QuestionType> mget(Collection<Integer> codes);

	List<QuestionType> mgetList(Collection<Integer> codes);

	/**
	 * 根据科目获得题型列表.
	 * 
	 * @param subjectCode
	 * @param typeCode
	 *            基本题型
	 * @return
	 */
	List<QuestionType> findBySubject(Integer subjectCode, Question.Type typeCode);
}

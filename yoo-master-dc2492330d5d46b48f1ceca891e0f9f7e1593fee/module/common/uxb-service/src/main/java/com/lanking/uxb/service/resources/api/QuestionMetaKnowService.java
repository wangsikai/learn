package com.lanking.uxb.service.resources.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;

/**
 * 提供元知识点和习题关系的相关接口
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月15日
 */
public interface QuestionMetaKnowService {

	List<MetaKnowpoint> listByQuestion(long questionId);

	Map<Long, List<MetaKnowpoint>> mListByQuestions(Collection<Long> questionIds);

	/**
	 * 根据学科获得元知识点对应的没有新知识点习题个数
	 * 
	 * @param subjectCode
	 * @return
	 */
	Map<Integer, Integer> mGetQuestionCountsByNoNewKnowledge(Integer subjectCode);
}

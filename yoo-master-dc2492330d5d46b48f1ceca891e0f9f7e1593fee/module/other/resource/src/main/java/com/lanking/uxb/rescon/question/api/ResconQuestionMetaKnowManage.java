package com.lanking.uxb.rescon.question.api;

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
public interface ResconQuestionMetaKnowManage {

	List<MetaKnowpoint> listByQuestion(long questionId);

	Map<Long, List<MetaKnowpoint>> mListByQuestions(Collection<Long> questionIds);
}

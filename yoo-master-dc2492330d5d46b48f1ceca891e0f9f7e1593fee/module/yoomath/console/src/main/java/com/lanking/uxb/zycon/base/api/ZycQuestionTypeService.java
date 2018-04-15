package com.lanking.uxb.zycon.base.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.QuestionType;

/**
 * 提供习题题型相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年8月31日 上午11:12:48
 */
public interface ZycQuestionTypeService {

	QuestionType get(Integer code);

	Map<Integer, QuestionType> mget(Collection<Integer> codes);

	List<QuestionType> mgetList(Collection<Integer> codes);

	List<QuestionType> findBySubject(Integer subjectCode);
}

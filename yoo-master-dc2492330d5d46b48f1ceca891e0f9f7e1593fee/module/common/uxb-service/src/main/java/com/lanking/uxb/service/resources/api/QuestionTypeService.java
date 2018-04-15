package com.lanking.uxb.service.resources.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.QuestionType;

/**
 * 提供习题题型相关接口
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月14日
 */
public interface QuestionTypeService {

	QuestionType get(Integer code);

	Map<Integer, QuestionType> mget(Collection<Integer> codes);

	List<QuestionType> mgetList(Collection<Integer> codes);

	List<QuestionType> findBySubject(Integer subjectCode);
}

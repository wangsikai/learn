package com.lanking.uxb.service.zuoye.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.question.Question.Type;

public interface ZyBookQuestionService {

	/**
	 * 获取书本分类下的题目
	 * 
	 * @param bookCategoryId
	 * @return
	 */
	List<Long> findQuestionByBookCategoryId(Long bookCategoryId);

	/**
	 * 批量获取分类下的题目
	 * 
	 * @param bookCategoryIds
	 * @return
	 */
	Map<Long, List<Long>> findQuestionByBookCategroyIds(Collection<Long> bookCategoryIds, Type questionType,
			Double diff1, Double diff2);
}

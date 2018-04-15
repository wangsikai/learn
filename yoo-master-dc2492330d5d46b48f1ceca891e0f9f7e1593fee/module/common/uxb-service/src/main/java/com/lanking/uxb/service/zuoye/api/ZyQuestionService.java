package com.lanking.uxb.service.zuoye.api;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;

/**
 * 题目相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月16日
 */
public interface ZyQuestionService {

	/**
	 * 按条件从数据库里面随机获取一些题目
	 * 
	 * @since 2.1
	 * @param sectionCode
	 *            章节代码
	 * @param types
	 *            题目类型
	 * @param count
	 *            获取条数
	 * @param exQIds
	 *            排除的题目
	 * @param minDifficulty
	 *            最小难度,增加难度使用
	 * @param maxDifficulty
	 *            最大难度,降低难度使用
	 * @return 题目列表
	 */
	List<Question> pullQuestions(Long sectionCode, Set<Type> types, int count, List<Long> exQIds,
			BigDecimal minDifficulty, BigDecimal maxDifficulty);

	/**
	 * 是否有简答题
	 * 
	 * @since yoomath V1.9.1
	 * @param ids
	 *            题目ID
	 * @return 是否有简答题
	 */
	boolean hasQuestionAnswering(Collection<Long> ids);

	/**
	 * 通过codes集合查询题目列表
	 * 
	 * @param codes
	 * @return
	 */
	List<Question> findByCodes(List<String> codes);

}

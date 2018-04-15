package com.lanking.uxb.service.zuoye.api;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.collection.QuestionCollection;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.service.zuoye.form.QuestionQueryForm;

/**
 * 题目收藏相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年9月6日
 */
public interface ZyQuestionCollectionService {
	/**
	 * 获取单个收藏
	 * 
	 * @param questionId
	 * @return
	 */
	QuestionCollection get(Long questionId, Long userId);

	/**
	 * 获取多个收藏
	 * 
	 * @param questionIds
	 * @return
	 */
	Map<Long, QuestionCollection> mget(Collection<Long> questionIds, Long userId);

	/**
	 * 收藏题目
	 * 
	 * @param questionId
	 * @param userId
	 */
	void collect(Long questionId, Long userId);

	/**
	 * 取消收藏
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param questionId
	 *            题目ID
	 * @param userId
	 *            用户ID
	 */
	void cancelCollect(long questionId, long userId);

	/**
	 * 取消收藏
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param id
	 *            收藏记录ID
	 * @param userId
	 *            用户ID
	 */
	void cancel(long id, long userId);

	/**
	 * 统计每个知识点对应的收藏数量
	 * 
	 * @param subjectCode
	 *            阶段
	 * @param userId
	 *            用户id
	 * @return
	 */
	Map<Integer, Integer> statisKnowPointCollect(Integer subjectCode, Long userId, List<Integer> qtCodes);

	/**
	 * 统计每个知识点对应的收藏数量(包含解答题)
	 * 
	 * @since yoomath V1.9.1
	 * @param subjectCode
	 *            阶段
	 * @param userId
	 *            用户id
	 * @return
	 */
	Map<Integer, Integer> statisKnowPointCollect2(Integer subjectCode, Long userId);

	Map<Long, Long> statisNewKnowPointCollect(Integer subjectCode, Long userId);

	/**
	 * 统计每个章节对应的收藏数量
	 * 
	 * @param textbookCode
	 *            教材code
	 * @param userId
	 *            用户id
	 * @return
	 */
	Map<Long, Long> statisSectionCollect(Integer textbookCode, Long userId, List<Integer> qtCodes);

	/**
	 * 统计每个章节对应的收藏数量(包含解答题)
	 * 
	 * @since yoomath V1.9.1
	 * @param textbookCode
	 *            教材code
	 * @param userId
	 *            用户id
	 * @return
	 */
	Map<Long, Long> statisSectionCollect2(Integer textbookCode, Long userId);

	/**
	 * 查询我的收藏
	 * 
	 * @param query
	 * @return
	 */
	Page<QuestionCollection> queryCollection(QuestionQueryForm query, Pageable p);

	/**
	 * 查询收藏的题目(DB)
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param query
	 *            查询条件
	 * @param pageable
	 *            分页条件
	 * @return 分页数据
	 */
	CursorPage<Long, QuestionCollection> queryCollection(QuestionQueryForm query, CursorPageable<Long> pageable);

	/**
	 * 索引查询我的收藏
	 * 
	 * @param query
	 * @param p
	 * @return
	 */
	Page<QuestionCollection> queryCollectionByIndex(QuestionQueryForm query, Pageable p);

	/**
	 * 查询我的收藏(包含解答题)
	 * 
	 * @since yoomath V1.9.1
	 * @param query
	 * @param p
	 * @return
	 */
	Page<QuestionCollection> queryCollectionByIndex2(QuestionQueryForm query, Pageable p);

	/**
	 * 统计教材是否存在收藏(更新缓存)
	 * 
	 * @since yoomath V1.3
	 * @param textbookCodes
	 *            教材代码
	 * @param userId
	 *            用户ID
	 * @return 统计数据
	 */
	Map<Integer, Boolean> statisTextbookExistCollectWithCache(List<Integer> textbookCodes, Long userId,
			List<Integer> qtCodes);

	/**
	 * 统计教材是否存在收藏(包含解答题)
	 * 
	 * @since yoomath V1.9.1
	 * @param textbookCodes
	 *            教材代码
	 * @param userId
	 *            用户ID
	 * @return 统计数据
	 */
	Map<Integer, Boolean> statisTextbookExistCollectWithCache2(List<Integer> textbookCodes, Long userId);

	void updateTextbookCollectCache(long questionId, long userId);

	/**
	 * 获取单个收藏
	 * 
	 * @param id
	 * @return
	 */
	QuestionCollection get(Long id);

	/**
	 * 批量获取收藏
	 * 
	 * @param ids
	 * @return
	 */
	Map<Long, QuestionCollection> mget(Collection<Long> ids);

	/**
	 * 计算用户收藏数量
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param userId
	 *            用户ID
	 * @return 收藏数量
	 */
	long count(long userId);

	/**
	 * 统计教材对应的收藏数量
	 * 
	 * @param subjectCode
	 *            阶段
	 * @param userId
	 *            用户id
	 * @return
	 */
	Map<Integer, Integer> statisTextbookCollect(long teacherId, int categoryCode);

	/**
	 * 查询收藏的题目(DB)2
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param query
	 *            查询条件
	 * @param pageable
	 *            分页条件
	 * @return 分页数据
	 */
	CursorPage<Long, QuestionCollection> queryCollection2(QuestionQueryForm query, CursorPageable<Long> pageable);

	/**
	 * 取最后一条收藏题目的textbookCode
	 * 
	 * @since V1.3.1
	 * @param userId
	 *            教材code
	 * @param categoryCode
	 *            用户id
	 * @return
	 */
	Map<String, BigInteger> getLastTextbookCode(Long userId, Integer categoryCode);

	/**
	 * 根据userId和教材查询章节
	 * 
	 * @since yoomath V1.3.0
	 * @param teacherId
	 * @param textbookCode
	 * @return
	 */
	Map<Long, Long> querySectionCode(Long teacherId, Integer textbookCode);

	/**
	 * 查询收藏的题目数量
	 * 
	 * @since yoomath(mobile) V1.3.0
	 * @param query
	 *            查询条件
	 * @return 题目总数量
	 */
	Long queryCollectionCount(QuestionQueryForm query);
}

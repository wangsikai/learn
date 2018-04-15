package com.lanking.uxb.service.zuoye.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.fallible.TeacherFallibleQuestion;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

/**
 * 教师错题相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月16日
 */
public interface ZyTeacherFallibleQuestionService {

	List<TeacherFallibleQuestion> mgetList(List<Long> ids);

	/**
	 * 查询老师错题
	 * 
	 * @since 2.1
	 * @param query
	 *            查询条件
	 * @param pageable
	 *            分页条件
	 * @return 分页数据
	 */
	@Deprecated
	Page<TeacherFallibleQuestion> query(ZyTeacherFallibleQuestionQuery query, Pageable pageable);

	/**
	 * 查询老师错题 题库综合知识点用
	 * 
	 * @param subjectCode
	 * 
	 * @since 2.1
	 * @param query
	 *            查询条件
	 * @param pageable
	 *            分页条件
	 * @return 分页数据
	 */
	Page<TeacherFallibleQuestion> queryFaliableQuestion(Integer subjectCode, ZyTeacherFallibleQuestionQuery query,
			Pageable index);

	/**
	 * 查询老师错题 题库综合知识点用 1.4.2新增搜索错题
	 * 
	 * @param subjectCode
	 * 
	 * @since 1.4.2
	 * @param query
	 *            查询条件
	 * @param page
	 *            页数
	 * @param pageSize
	 *            每页大小
	 * @return 分页数据
	 */
	Page<TeacherFallibleQuestion> queryFaliableQuestion2(Integer subjectCode, ZyTeacherFallibleQuestionQuery query,
			Integer page, Integer pageSize);

	/**
	 * 查询老师错题 题库综合知识点用(包含解答题)
	 * 
	 * @since yoomath V1.9.1
	 * @param subjectCode
	 * 
	 * @param query
	 *            查询条件
	 * @param page
	 *            页数
	 * @param pageSize
	 *            每页大小
	 * @return 分页数据
	 */
	Page<TeacherFallibleQuestion> queryFaliableQuestion3(Integer subjectCode, ZyTeacherFallibleQuestionQuery query,
			Integer page, Integer pageSize);

	/**
	 * 统计某个教师某个教材的错题
	 * 
	 * @since 2.1
	 * @param teacherId
	 *            教师ID
	 * @param textbookCode
	 *            教材代码
	 * @return KEY:sectionCode VALUE:错题数量
	 */
	@Deprecated
	Map<Long, Long> staticFallibleCount(long teacherId, int textbookCode);

	/**
	 * 根据老师科目获取该知识点下的习题数
	 * 
	 * @param qtCodes
	 *            新增 排除 解答，证明，计算
	 * 
	 * @param 科目
	 *            subjectCode
	 * @return
	 */
	Map<Integer, Integer> getKnowpointFailCount(Long uid, Integer subjectCode, List<Integer> qtCodes);

	/**
	 * 根据老师科目获取该知识点下的习题数(包含解答题)
	 * 
	 * @since yoomath V1.9.1
	 * @param qtCodes
	 *            新增 排除 解答，证明，计算
	 * 
	 * @param 科目
	 *            subjectCode
	 * @return
	 */
	Map<Integer, Integer> getKnowpointFailCount2(Long uid, Integer subjectCode);

	/**
	 * 根据老师科目获取该新知识点下的错习题数
	 * 
	 * @version 2016.11.23
	 * @param uid
	 * @param subjectCode
	 * @return
	 */
	Map<Integer, Integer> getNewKnowpointFailCount(Long uid, Integer subjectCode);

	/**
	 * 根据教材获取该教材下的错题书数
	 * 
	 * @param textbookCode
	 *            教材代码
	 * @param qtCodes
	 *            排除的题目类型codes
	 * @return
	 */
	Map<Long, Integer> staticQuestionFallibleCount(long userId, Integer textbookCode, Integer subjectCode,
			List<Integer> qtCodes);

	/**
	 * 根据教材获取该教材下的错题书数(包含解答题)
	 * 
	 * @since yoomath V1.9.1
	 * @param textbookCode
	 *            教材代码
	 * @param qtCodes
	 *            排除的题目类型codes
	 * @return
	 */
	Map<Long, Integer> staticQuestionFallibleCount2(long userId, Integer textbookCode, Integer subjectCode);

	/**
	 * 统计教材是否存在错题(更新缓存)
	 * 
	 * @since yoomath V1.3
	 * @since 教师端 v1.3.0 保持与客户端数据获取一致，废弃使用
	 * 
	 * @param textbookCodes
	 *            教材代码
	 * @param userId
	 *            用户ID
	 * @return 统计数据
	 */
	@Deprecated
	Map<Integer, Boolean> statisTextbookExistFallibleWithCache(List<Integer> textbookCodes, Long userId);

	Map<Long, TeacherFallibleQuestion> mget(List<Long> ids);

	Map<Integer, Integer> statisTextbookFallible(long teacherId, int categoryCode);

	/**
	 * 删除教师错题
	 *
	 * @param id
	 * @param teacherId
	 *            当前教师ID
	 */
	void deleteFailQuestion(Long id, Long teacherId);

	/**
	 * 查询老师错题 题库综合知识点用(游标查询)
	 * 
	 * @since yoomath V1.3.0
	 * @param subjectCode
	 * 
	 * @param query
	 *            查询条件
	 * @param pageSize
	 *            每页大小
	 * @return 游标查询数据
	 */
	Page<TeacherFallibleQuestion> queryFaliableQuestion4(Integer subjectCode, ZyTeacherFallibleQuestionQuery query,
			Integer pageSize);

	/**
	 * 根据userId和教材查询章节
	 * 
	 * @since yoomath V1.3.0
	 * @param teacherId
	 * @param textbookCode
	 * @return
	 */
	Map<Long, Long> querySectionCode(Long teacherId, Integer textbookCode);
}

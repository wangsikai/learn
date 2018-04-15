package com.lanking.uxb.service.zuoye.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.school.QuestionSchool;
import com.lanking.cloud.domain.yoomath.school.SchoolQuestion;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.service.zuoye.form.QuestionQueryForm;

/**
 * 
 * @author wangsenhao
 *
 */
public interface ZySchoolQuestionService {
	/**
	 * 获取单个学校题目
	 * 
	 * @param questionId
	 * @return
	 */
	SchoolQuestion get(Long schoolQuestionIds, Long userId);

	/**
	 * 获取多个学校题目
	 * 
	 * @param schoolQuestionIds
	 * @return
	 */
	Map<Long, SchoolQuestion> mget(Collection<Long> schoolQuestionIds, Long userId);

	/**
	 * 统计每个知识点对应的学校题目数量
	 * 
	 * @param subjectCode
	 *            阶段
	 * @param schoolId
	 *            学校id
	 * @return
	 */
	Map<Integer, Integer> statisKnowPointSchool(Integer subjectCode, Long schoolId, List<Integer> qtCodes);

	/**
	 * 统计每个知识点对应的学校题目数量(包含解答题)
	 * 
	 * @since yoomath V1.9.1
	 * @param subjectCode
	 *            阶段
	 * @param schoolId
	 *            学校id
	 * @return
	 */
	Map<Integer, Integer> statisKnowPointSchool2(Integer subjectCode, Long schoolId);

	/**
	 * 统计每个章节对应的学校题目数量
	 * 
	 * @param textbookCode
	 *            教材code
	 * @param schoolId
	 *            学校id
	 * @return
	 */
	Map<Long, Long> statisSectionSchool(Integer textbookCode, Long schoolId, List<Integer> qtCodes);

	/**
	 * 统计每个章节对应的学校题目数量(新知识点)
	 * 
	 * @param textbookCode
	 *            教材code
	 * @param schoolId
	 *            学校id
	 * @return
	 */
	Map<Long, Long> statisSectionSchool2(Integer textbookCode, Long schoolId, List<Integer> qtCodes);

	/**
	 * 统计每个章节对应的学校题目数量(包含解答题)
	 * 
	 * @since yoomath V1.9.1
	 * @param textbookCode
	 *            教材code
	 * @param schoolId
	 *            学校id
	 * @return
	 */
	Map<Long, Long> statisSectionSchool2(Integer textbookCode, Long schoolId);

	/**
	 * 索引查询学校题目(不含解答题)
	 * 
	 * @param query
	 * @param p
	 * @return
	 */
	Page<SchoolQuestion> querySchoolQuestionByIndex(QuestionQueryForm query, Pageable p);

	/**
	 * 索引查询学校题目(包含解答题)
	 * 
	 * @since yoomath V1.9.1
	 * @param query
	 * @param p
	 * @return
	 */
	Page<SchoolQuestion> querySchoolQuestionByIndex2(QuestionQueryForm query, Pageable p);

	/**
	 * 获取单个学校题目
	 * 
	 * @param id
	 * @return
	 */
	SchoolQuestion get(Long id);

	/**
	 * 批量获取学校题目
	 * 
	 * @param ids
	 * @return
	 */
	Map<Long, SchoolQuestion> mget(Collection<Long> ids);


	/**
	 * 查询题库学校
	 * 
	 * @param schoolId
	 * @return
	 */
	QuestionSchool getQuestionSchool(Long schoolId);

	/**
	 * 统计教材是否存在学校题目
	 * 
	 * @param textbookCodes
	 * @param schoolId
	 * @return
	 */
	Map<Integer, Boolean> statisTextbookExistSchoolWithCache(List<Integer> textbookCodes, Long schoolId,
			List<Integer> qtCodes);

	/**
	 * 统计教材是否存在学校题目(包括解答题)
	 * 
	 * @since yoomath V1.9.1
	 * @param textbookCodes
	 * @param schoolId
	 * @return
	 */
	Map<Integer, Boolean> statisTextbookExistSchoolWithCache2(List<Integer> textbookCodes, Long schoolId);

	/**
	 * 新知识相关的校本题库统计
	 * 
	 * @param schoolId
	 * @param subjectCode
	 * @return
	 */
	Map<Long, Long> getNewKnowpointSchoolQCount(Long schoolId, Integer subjectCode);
}

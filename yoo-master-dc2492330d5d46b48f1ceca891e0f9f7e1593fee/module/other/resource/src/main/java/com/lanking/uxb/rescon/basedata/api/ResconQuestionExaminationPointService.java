package com.lanking.uxb.rescon.basedata.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.ExaminationPoint;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

/**
 * 题目和新考点关联接口
 * 
 * @since 2.0.1
 * @author wangsenhao
 *
 */
public interface ResconQuestionExaminationPointService {
	/**
	 * 获取考点集合对应的id集合
	 * 
	 * @param examIds
	 * @return
	 */
	List<Long> findQuestionIds(List<Long> examIds, Long vendorId);

	/**
	 * 根据习题获取考点集合.
	 * 
	 * @param questionId
	 *            习题ID
	 * @return
	 */
	List<ExaminationPoint> listByQuestion(Long questionId);

	/**
	 * 根据习题获取考点集合.
	 * 
	 * @param questionIds
	 *            习题ID集合
	 * @return
	 */
	Map<Long, List<ExaminationPoint>> mListByQuestions(Collection<Long> questionIds);

	/**
	 * 根据考点获取习题.
	 * 
	 * @param examinationPointCode
	 *            考点
	 * @param pageable
	 * @return
	 */
	Page<Long> queryQuestionByExaminationPointCode(long examinationPointCode, Pageable pageable);
}

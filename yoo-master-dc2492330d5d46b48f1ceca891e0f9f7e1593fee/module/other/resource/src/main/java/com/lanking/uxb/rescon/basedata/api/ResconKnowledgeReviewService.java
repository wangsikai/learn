package com.lanking.uxb.rescon.basedata.api;

import java.util.List;

import com.lanking.cloud.domain.common.baseData.KnowledgeReview;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 复习知识点相关接口.
 * 
 * @author wlche
 *
 */
public interface ResconKnowledgeReviewService {

	/**
	 * 根据阶段学科获取所有复习知识点.
	 * 
	 * @param phaseCode
	 *            阶段
	 * @param subjectCode
	 *            学科
	 * @param status
	 *            状态
	 * @return
	 */
	List<KnowledgeReview> findAll(int phaseCode, int subjectCode, Status status);
}

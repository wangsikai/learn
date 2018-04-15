package com.lanking.uxb.service.code.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.KnowledgeReview;

/**
 * 复习知识点
 * 
 * @author wlche
 *
 */
public interface KnowledgeReviewService {

	/**
	 * 批量获取
	 * 
	 * @param codes
	 * @return
	 */
	Map<Long, KnowledgeReview> mget(Collection<Long> codes);

	/**
	 * 根据知识点id列表查询知识点列表
	 *
	 * @param codes
	 *            知识点id列表
	 * @return 知识点列表
	 */
	List<KnowledgeReview> mgetList(Collection<Long> codes);
}

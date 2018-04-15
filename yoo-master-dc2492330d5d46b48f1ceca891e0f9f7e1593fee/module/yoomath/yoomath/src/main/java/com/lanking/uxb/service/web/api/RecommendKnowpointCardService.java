package com.lanking.uxb.service.web.api;

import com.lanking.cloud.domain.yoo.recommend.RecommendKnowpointCard;

/**
 * 学生首页知识卡片推送接口
 *
 * @author xinyu.zhou
 * @since 2.0.3
 */
public interface RecommendKnowpointCardService {
	/**
	 * 根据知识点user查询推送知识点卡片
	 * 
	 * @param studentId
	 * @return List<RecommendKnowpointCard>
	 */
	RecommendKnowpointCard getRecommendKnowpointCard(Long studentId);

	/**
	 * 获取最后一期推送
	 * 
	 * @param studentId
	 * @return RecommendKnowpointCard
	 */
	RecommendKnowpointCard getLastRecommendKnowpointCard(Long studentId);

}

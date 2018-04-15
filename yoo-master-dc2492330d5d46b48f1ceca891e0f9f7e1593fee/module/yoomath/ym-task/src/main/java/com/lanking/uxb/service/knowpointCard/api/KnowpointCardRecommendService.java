package com.lanking.uxb.service.knowpointCard.api;

import com.lanking.cloud.sdk.data.Page;

/**
 * 知识点卡片
 *
 * @author zemin.song
 * @since 2.1.1
 */
public interface KnowpointCardRecommendService {

	/**
	 * 知识点卡片推送
	 */
	Page<Long> recommendKnowpointCard(int page, int size);

}

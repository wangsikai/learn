package com.lanking.uxb.service.zuoye.api;

import com.lanking.cloud.domain.common.resource.card.KnowpointCard;

/**
 * 知识点学习相关.
 * 
 * @since 2.0.0
 * @author wlche
 *
 */
public interface ZyKnowpointLearnService {

	/**
	 * 根据知识点code获取 知识卡片
	 * 
	 * @param knowpointCode
	 * @return
	 */
	KnowpointCard getByCode(long knowpointCode);
}

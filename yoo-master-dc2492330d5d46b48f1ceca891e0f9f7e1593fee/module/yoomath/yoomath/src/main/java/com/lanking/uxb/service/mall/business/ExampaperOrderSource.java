package com.lanking.uxb.service.mall.business;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 精品试卷订单来源.<br>
 * 用于支付成功后的来源跳转等动作
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月5日
 */
public enum ExampaperOrderSource implements Valueable {
	DEFAULT(0),

	/**
	 * 精品试卷列表下载.
	 */
	EXAM_LIST_DOWNLOAD(1),

	/**
	 * 收藏试卷列表下载.
	 */
	FAVORITE_LIST_DOWNLOAD(2),

	/**
	 * 精品试卷列表组卷.
	 */
	EXAM_LIST_ASSEMBLE(3),

	/**
	 * 收藏试卷列表组卷.
	 */
	FAVORITE_LIST_ASSEMBLE(4),

	/**
	 * 精品试卷手工组卷.
	 */
	EXAM_DETAIL_ASSEMBLE(5);

	private int value;

	ExampaperOrderSource(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return this.value;
	}
}

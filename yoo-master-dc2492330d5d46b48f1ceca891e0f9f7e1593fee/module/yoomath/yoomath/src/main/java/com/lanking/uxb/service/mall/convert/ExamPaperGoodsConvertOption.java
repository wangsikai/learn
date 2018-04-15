package com.lanking.uxb.service.mall.convert;

/**
 * 题目VO转换选项.
 * 
 * @since zemin.song
 * @version 2016年8月19日
 */
public class ExamPaperGoodsConvertOption {

	// 控制商品试卷
	private boolean initGoodsInfo = false;
	// 是否收藏信息
	private boolean initCollect = false;

	public boolean isInitGoodsInfo() {
		return initGoodsInfo;
	}

	public void setInitGoodsInfo(boolean initGoodsInfo) {
		this.initGoodsInfo = initGoodsInfo;
	}

	public boolean isInitCollect() {
		return initCollect;
	}

	public void setInitCollect(boolean initCollect) {
		this.initCollect = initCollect;
	}

}

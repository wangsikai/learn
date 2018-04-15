package com.lanking.uxb.service.mall.value;

import com.lanking.uxb.service.examPaper.value.VExamPaper;

/**
 * 试卷商品查询 vo
 * 
 * @author zemin.song
 */
public class VExamPaperGoods extends VResourcesGoods {
	private static final long serialVersionUID = -8037350386859255427L;

	private VExamPaper paper;
	// 是否收藏
	private boolean isCollect = false;

	public VExamPaper getPaper() {
		return paper;
	}

	public void setPaper(VExamPaper paper) {
		this.paper = paper;
	}

	public boolean isCollect() {
		return isCollect;
	}

	public void setCollect(boolean isCollect) {
		this.isCollect = isCollect;
	}

}

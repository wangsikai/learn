package com.lanking.uxb.service.zuoye.api;

import java.util.List;

/**
 * 生成的试卷数据结构
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月16日
 */
public class Paper {

	private List<Long> ids;
	// 当没有符合条件的题目的时候,根据具体业务场景进行题目推荐
	private boolean recommend = false;

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public boolean isRecommend() {
		return recommend;
	}

	public void setRecommend(boolean recommend) {
		this.recommend = recommend;
	}

}

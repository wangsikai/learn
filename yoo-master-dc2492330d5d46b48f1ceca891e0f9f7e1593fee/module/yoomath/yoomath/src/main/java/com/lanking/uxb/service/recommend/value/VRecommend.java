package com.lanking.uxb.service.recommend.value;

import java.io.Serializable;

/**
 * 推荐类型
 * 
 * @since 2.0.3
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年8月4日
 */
public class VRecommend implements Serializable {

	private static final long serialVersionUID = 7360266923423384317L;

	private String recommendType;
	private boolean selected = false;

	public String getRecommendType() {
		return recommendType;
	}

	public void setRecommendType(String recommendType) {
		this.recommendType = recommendType;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}

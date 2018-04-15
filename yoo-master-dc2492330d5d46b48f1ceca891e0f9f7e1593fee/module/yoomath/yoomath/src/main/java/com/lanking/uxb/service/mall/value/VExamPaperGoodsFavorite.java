package com.lanking.uxb.service.mall.value;

import java.util.Date;

/**
 * 试卷收藏
 *
 * @author zemin.song
 */
public class VExamPaperGoodsFavorite {
	// 收藏ID
	private Long id;
	// 试卷商品
	private VExamPaperGoods goods;
	// 收藏时间
	private Date favoriteAt;

	public VExamPaperGoods getGoods() {
		return goods;
	}

	public void setGoods(VExamPaperGoods goods) {
		this.goods = goods;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFavoriteAt() {
		return favoriteAt;
	}

	public void setFavoriteAt(Date favoriteAt) {
		this.favoriteAt = favoriteAt;
	}

}

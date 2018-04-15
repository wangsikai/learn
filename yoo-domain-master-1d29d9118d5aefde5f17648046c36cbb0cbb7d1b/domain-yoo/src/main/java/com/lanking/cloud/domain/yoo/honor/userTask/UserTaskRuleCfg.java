package com.lanking.cloud.domain.yoo.honor.userTask;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * 用户任务配置
 * 
 * @since 3.9.7
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年5月2日
 */
public class UserTaskRuleCfg implements Serializable {

	private static final long serialVersionUID = 6614421718291421753L;

	/**
	 * 成长值金币值设定,不能为null,无效值请设置为-1
	 */
	private int growthValue;
	private int lGrowthValue;
	private int rGrowthValue;

	private int coinsValue;
	private int lCoinsValue;
	private int rCoinsValue;

	private int activeStar;
	private int lActiveStar;
	private int rActiveStar;

	/**
	 * 任务项（不具备通用型，根据具体任务类型使用），对应项的指标阀值、金币值、成长值如果没有则设置为null
	 */
	private List<String> items;
	private List<String> itemsThreshold;
	private List<Integer> itemCoins;
	private List<Integer> itemStar;
	private List<Integer> itemGrowth;

	/**
	 * 跳转url
	 */
	private String url;

	public int getGrowthValue() {
		return growthValue;
	}

	public void setGrowthValue(int growthValue) {
		this.growthValue = growthValue;
	}

	public int getCoinsValue() {
		return coinsValue;
	}

	public void setCoinsValue(int coinsValue) {
		this.coinsValue = coinsValue;
	}

	public int getlCoinsValue() {
		return lCoinsValue;
	}

	public void setlCoinsValue(int lCoinsValue) {
		this.lCoinsValue = lCoinsValue;
	}

	public int getrCoinsValue() {
		return rCoinsValue;
	}

	public void setrCoinsValue(int rCoinsValue) {
		this.rCoinsValue = rCoinsValue;
	}

	public int getActiveStar() {
		return activeStar;
	}

	public void setActiveStar(int activeStar) {
		this.activeStar = activeStar;
	}

	public int getlActiveStar() {
		return lActiveStar;
	}

	public void setlActiveStar(int lActiveStar) {
		this.lActiveStar = lActiveStar;
	}

	public int getrActiveStar() {
		return rActiveStar;
	}

	public void setrActiveStar(int rActiveStar) {
		this.rActiveStar = rActiveStar;
	}

	public List<String> getItemsThreshold() {
		return itemsThreshold;
	}

	public void setItemsThreshold(List<String> itemsThreshold) {
		this.itemsThreshold = itemsThreshold;
	}

	public List<String> getItems() {
		return items;
	}

	public void setItems(List<String> items) {
		this.items = items;
	}

	public List<Integer> getItemCoins() {
		return itemCoins;
	}

	public void setItemCoins(List<Integer> itemCoins) {
		this.itemCoins = itemCoins;
	}

	public List<Integer> getItemStar() {
		return itemStar;
	}

	public void setItemStar(List<Integer> itemStar) {
		this.itemStar = itemStar;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		return obj.toString().equals(this.toString());
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

	public int getlGrowthValue() {
		return lGrowthValue;
	}

	public void setlGrowthValue(int lGrowthValue) {
		this.lGrowthValue = lGrowthValue;
	}

	public int getrGrowthValue() {
		return rGrowthValue;
	}

	public void setrGrowthValue(int rGrowthValue) {
		this.rGrowthValue = rGrowthValue;
	}

	public List<Integer> getItemGrowth() {
		return itemGrowth;
	}

	public void setItemGrowth(List<Integer> itemGrowth) {
		this.itemGrowth = itemGrowth;
	}

}
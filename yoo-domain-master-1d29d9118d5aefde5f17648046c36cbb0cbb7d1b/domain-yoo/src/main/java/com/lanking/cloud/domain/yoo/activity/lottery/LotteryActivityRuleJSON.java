package com.lanking.cloud.domain.yoo.activity.lottery;

import java.util.List;

/**
 * 抽奖活动规则JSON数据定义
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public class LotteryActivityRuleJSON {

	/**
	 * 单次获得的虚拟币个数（与multi互斥）
	 */
	private Integer one;

	/**
	 * 多个阶段获得的虚拟币个数（与one互斥）
	 */
	private List<Integer> multi;

	/**
	 * 活动最多获得的虚拟币总数（为空表示不限）
	 */
	private Integer totalmax;

	/**
	 * 每日最多获得的虚拟币总数（为空表示不限）
	 */
	private Integer daymax;

	public Integer getOne() {
		return one;
	}

	public void setOne(Integer one) {
		this.one = one;
	}

	public List<Integer> getMulti() {
		return multi;
	}

	public void setMulti(List<Integer> multi) {
		this.multi = multi;
	}

	public Integer getTotalmax() {
		return totalmax;
	}

	public void setTotalmax(Integer totalmax) {
		this.totalmax = totalmax;
	}

	public Integer getDaymax() {
		return daymax;
	}

	public void setDaymax(Integer daymax) {
		this.daymax = daymax;
	}
}

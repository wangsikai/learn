package com.lanking.cloud.domain.yoo.activity.lottery;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.lanking.cloud.domain.yoo.goods.activity.lottery.LotteryActivityVirtualCoinsType;

/**
 * 抽奖活动用户相关数据
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "lottery_activity_user")
@IdClass(LotteryActivityUserKey.class)
public class LotteryActivityUser extends LotteryActivityUserKey {

	private static final long serialVersionUID = -1306222377629814477L;

	/**
	 * 虚拟货币类型
	 */
	@Column(name = "virtual_coins_type", precision = 3, columnDefinition = "tinyint default 0")
	private LotteryActivityVirtualCoinsType virtualCoinsType = LotteryActivityVirtualCoinsType.DEFUALT;

	/**
	 * 用户当前活动的虚拟货币总数
	 */
	@Column(name = "total_coins")
	private int totalCoins = 0;

	/**
	 * 记录上次查询新增虚拟币的截止时间（用于提醒等功能使用）
	 */
	@Column(name = "last_increase_record_at", columnDefinition = "datetime(3)")
	private Date lastIncrRecordAt;

	public LotteryActivityVirtualCoinsType getVirtualCoinsType() {
		return virtualCoinsType;
	}

	public void setVirtualCoinsType(LotteryActivityVirtualCoinsType virtualCoinsType) {
		this.virtualCoinsType = virtualCoinsType;
	}

	public int getTotalCoins() {
		return totalCoins;
	}

	public void setTotalCoins(int totalCoins) {
		this.totalCoins = totalCoins;
	}

	public Date getLastIncrRecordAt() {
		return lastIncrRecordAt;
	}

	public void setLastIncrRecordAt(Date lastIncrRecordAt) {
		this.lastIncrRecordAt = lastIncrRecordAt;
	}
}

package com.lanking.cloud.domain.yoo.honor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 用户荣誉相关记录值
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "user_honor")
public class UserHonor implements Serializable {

	private static final long serialVersionUID = 4678633792939013070L;

	/**
	 * 用户ID
	 */
	@Id
	@Column(name = "user_id")
	private long userId;

	/**
	 * 积分值
	 */
	@Column(name = "point", precision = 10)
	private int point = 0;

	/**
	 * 成长值
	 */
	@Column(name = "growth", precision = 10)
	private int growth = 0;

	/**
	 * 金币值
	 */
	@Column(name = "coins", precision = 10)
	private int coins = 0;

	/**
	 * 当前等级
	 */
	@Column(name = "level")
	private int level = 1;

	/**
	 * 用户是否已经获取过升级提示
	 */
	@Column(name = "upgrade")
	private boolean upgrade = true;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	@Transient
	private Integer upRewardCoins;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getGrowth() {
		return growth;
	}

	public void setGrowth(int growth) {
		this.growth = growth;
	}

	public int getCoins() {
		return coins;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isUpgrade() {
		return upgrade;
	}

	public void setUpgrade(boolean upgrade) {
		this.upgrade = upgrade;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public Integer getUpRewardCoins() {
		return upRewardCoins;
	}

	public void setUpRewardCoins(Integer upRewardCoins) {
		this.upRewardCoins = upRewardCoins;
	}

}
package com.lanking.cloud.domain.yoo.goods.activity.lottery;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.UserType;

/**
 * 抽奖活动商品信息基类
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@MappedSuperclass
public abstract class LotteryActivityGoodsBaseInfo implements Serializable {
	private static final long serialVersionUID = 5272764813359232102L;

	/**
	 * 对应的抽奖活动code
	 */
	@Column(name = "activity_code")
	private Long activityCode;

	/**
	 * 活动商品类型
	 */
	@Column(name = "activity_goods_type", precision = 3)
	private CoinsGoodsType type;

	/**
	 * 抽奖活动商品分类
	 */
	@Column(name = "category_id")
	private Long categoryId;

	/**
	 * 初始化商品数量，为0时表示无限
	 */
	@Column(name = "init_count")
	private Integer initCount;

	/**
	 * 已消耗商品数量
	 */
	@Column(name = "sell_count")
	private Integer sellCount = 0;

	/**
	 * 商品限定的用户类型，默认不限制
	 */
	@Column(name = "user_type", precision = 3)
	private UserType userType = UserType.NULL;

	/**
	 * 商品限定的会员类型，默认不限制
	 */
	@Column(name = "member_type", precision = 3)
	private MemberType memberType = MemberType.NONE;

	public Long getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(Long activityCode) {
		this.activityCode = activityCode;
	}

	public CoinsGoodsType getType() {
		return type;
	}

	public void setType(CoinsGoodsType type) {
		this.type = type;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getInitCount() {
		return initCount;
	}

	public void setInitCount(Integer initCount) {
		this.initCount = initCount;
	}

	public Integer getSellCount() {
		return sellCount;
	}

	public void setSellCount(Integer sellCount) {
		this.sellCount = sellCount;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public MemberType getMemberType() {
		return memberType;
	}

	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}

}

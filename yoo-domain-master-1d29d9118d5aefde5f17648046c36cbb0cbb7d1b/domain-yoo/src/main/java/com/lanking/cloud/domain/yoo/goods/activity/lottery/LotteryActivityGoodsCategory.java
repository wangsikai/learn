package com.lanking.cloud.domain.yoo.goods.activity.lottery;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 抽奖商品分类.
 * 
 * <pre>
 * 例如砸蛋类型、宝箱类型等
 * </pre>
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "lottery_activity_goods_category")
public class LotteryActivityGoodsCategory implements Serializable {
	private static final long serialVersionUID = -5482479891755234052L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 对应的抽奖活动code.
	 */
	@Column(name = "activity_code")
	private Long activityCode;

	/**
	 * 分类名称.
	 */
	@Column(name = "name", length = 100)
	private String name;

	/**
	 * 等级（依据活动类型需求，此处数值越高等级越高）.
	 */
	@Column(name = "level")
	private Integer level = 0;

	/**
	 * 抽奖所需虚拟币类型.
	 */
	@Column(name = "virtual_coins_type", precision = 3, columnDefinition = "tinyint default 0")
	private LotteryActivityVirtualCoinsType virtualCoinsType = LotteryActivityVirtualCoinsType.DEFUALT;

	/**
	 * 本类抽奖所需虚拟币个数.
	 */
	@Column(name = "cost")
	private Integer cost;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(Long activityCode) {
		this.activityCode = activityCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public LotteryActivityVirtualCoinsType getVirtualCoinsType() {
		return virtualCoinsType;
	}

	public void setVirtualCoinsType(LotteryActivityVirtualCoinsType virtualCoinsType) {
		this.virtualCoinsType = virtualCoinsType;
	}

	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}
}

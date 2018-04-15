package com.lanking.cloud.domain.yoo.honor.coins;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 金币值详细记录
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "coins_log")
public class CoinsLog implements Serializable {

	private static final long serialVersionUID = -6894936516656897451L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * log类型
	 */
	@Column(name = "type", precision = 3)
	private CoinsLogType type = CoinsLogType.COINS_RULE;

	/**
	 * 规则代码
	 */
	@Column(name = "rule_code", precision = 10)
	private int ruleCode;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 金币值（可正可负）
	 */
	@Column(name = "coins_value", precision = 5)
	private int coinsValue;

	/**
	 * 关联业务对象(如果没有则设置成Biz.NULL)
	 */
	@Column(name = "biz", precision = 3)
	private Biz biz = Biz.NULL;

	/**
	 * 关联业务对象ID(如果没有则设置成0)
	 */
	@Column(name = "biz_id")
	private Long bizId = 0L;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 状态
	 */
	@Column(name = "status", precision = 3, columnDefinition = "tinyint default 0")
	private Status status = Status.ENABLED;

	/**
	 * 附加参数1
	 */
	@Column(name = "p1", length = 100)
	private String p1;

	/**
	 * 附加参数2
	 */
	@Column(name = "p2", length = 100)
	private String p2;

	/**
	 * 附加参数3
	 */
	@Column(name = "p3", length = 100)
	private String p3;

	@Transient
	private int nextCoins = 0;
	@Transient
	private Integer upRewardCoins;

	public CoinsLog() {
		super();
	}

	public CoinsLog(Long userId) {
		super();
		this.userId = userId;
	}

	public CoinsLog(Long userId, Integer colinsValue, Biz biz, Long bizId) {
		super();
		this.userId = userId;
		this.coinsValue = colinsValue;
		this.biz = biz;
		this.bizId = bizId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CoinsLogType getType() {
		return type;
	}

	public void setType(CoinsLogType type) {
		this.type = type;
	}

	public int getRuleCode() {
		return ruleCode;
	}

	public void setRuleCode(int ruleCode) {
		this.ruleCode = ruleCode;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public int getCoinsValue() {
		return coinsValue;
	}

	public void setCoinsValue(int coinsValue) {
		this.coinsValue = coinsValue;
	}

	public Biz getBiz() {
		return biz;
	}

	public void setBiz(Biz biz) {
		this.biz = biz;
	}

	public Long getBizId() {
		return bizId;
	}

	public void setBizId(Long bizId) {
		this.bizId = bizId;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getP1() {
		return p1;
	}

	public void setP1(String p1) {
		this.p1 = p1;
	}

	public String getP2() {
		return p2;
	}

	public void setP2(String p2) {
		this.p2 = p2;
	}

	public String getP3() {
		return p3;
	}

	public void setP3(String p3) {
		this.p3 = p3;
	}

	public int getNextCoins() {
		return nextCoins;
	}

	public void setNextCoins(int nextCoins) {
		this.nextCoins = nextCoins;
	}

	public Integer getUpRewardCoins() {
		return upRewardCoins;
	}

	public void setUpRewardCoins(Integer upRewardCoins) {
		this.upRewardCoins = upRewardCoins;
	}

}
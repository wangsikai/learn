package com.lanking.cloud.domain.yoo.honor.growth;

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
import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 成长值详细记录
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "growth_log")
public class GrowthLog implements Serializable {

	private static final long serialVersionUID = 8121985014802648150L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * log类型
	 */
	@Column(name = "type", precision = 3)
	private GrowthLogType type = GrowthLogType.GROWTH_RULE;

	/**
	 * 规则代码
	 */
	@Column(name = "rule_code", precision = 10)
	private int ruleCode;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private long userId;

	/**
	 * 对应的成长值（可正可负）
	 */
	@Column(name = "growth_value", precision = 3)
	private int growthValue;

	/**
	 * 关联业务对象(如果没有则设置成Biz.NULL)
	 */
	@Column(name = "biz", precision = 3)
	private Biz biz = Biz.NULL;

	/**
	 * 关联业务对象ID(如果没有则设置成0)
	 */
	@Column(name = "biz_id")
	private long bizId = 0L;

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
	private UserHonor honor;

	@Transient
	private int nextGrowth = 0;

	public GrowthLog() {
		super();
	}

	public GrowthLog(long userId) {
		super();
		this.userId = userId;
	}

	public GrowthLog(long userId, Integer groupValue, Biz biz, long bizId) {
		super();
		this.userId = userId;
		this.growthValue = groupValue;
		this.biz = biz;
		this.bizId = bizId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public GrowthLogType getType() {
		return type;
	}

	public void setType(GrowthLogType type) {
		this.type = type;
	}

	public int getRuleCode() {
		return ruleCode;
	}

	public void setRuleCode(int ruleCode) {
		this.ruleCode = ruleCode;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getGrowthValue() {
		return growthValue;
	}

	public void setGrowthValue(int growthValue) {
		this.growthValue = growthValue;
	}

	public Biz getBiz() {
		return biz;
	}

	public void setBiz(Biz biz) {
		this.biz = biz;
	}

	public long getBizId() {
		return bizId;
	}

	public void setBizId(long bizId) {
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

	public UserHonor getHonor() {
		return honor;
	}

	public void setHonor(UserHonor honor) {
		this.honor = honor;
	}

	public int getNextGrowth() {
		return nextGrowth;
	}

	public void setNextGrowth(int nextGrowth) {
		this.nextGrowth = nextGrowth;
	}

}
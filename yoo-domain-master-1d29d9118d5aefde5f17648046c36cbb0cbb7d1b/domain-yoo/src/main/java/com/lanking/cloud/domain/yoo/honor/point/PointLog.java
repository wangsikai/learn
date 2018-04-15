package com.lanking.cloud.domain.yoo.honor.point;

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
 * 用户积分记录
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "point_log")
public class PointLog implements Serializable {

	private static final long serialVersionUID = 2126283550406064796L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 规则代码
	 */
	@Column(name = "rule_code", precision = 5)
	private int ruleCode;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private long userId;

	/**
	 * 对应积分值（可正可负）
	 */
	@Column(name = "point", precision = 3)
	private Integer point;

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

	@Transient
	private int nextPoint = 0;
	@Transient
	private int userPoint = 0;

	public PointLog() {
		super();
	}

	public PointLog(long userId) {
		super();
		this.userId = userId;
	}

	public PointLog(long userId, Biz biz, long bizId) {
		super();
		this.userId = userId;
		this.biz = biz;
		this.bizId = bizId;
	}

	public PointLog(long userId, Integer point, Biz biz, long bizId) {
		super();
		this.userId = userId;
		this.point = point;
		this.biz = biz;
		this.bizId = bizId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
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

	public int getNextPoint() {
		return nextPoint;
	}

	public void setNextPoint(int nextPoint) {
		this.nextPoint = nextPoint;
	}

	public int getUserPoint() {
		return userPoint;
	}

	public void setUserPoint(int userPoint) {
		this.userPoint = userPoint;
	}

}
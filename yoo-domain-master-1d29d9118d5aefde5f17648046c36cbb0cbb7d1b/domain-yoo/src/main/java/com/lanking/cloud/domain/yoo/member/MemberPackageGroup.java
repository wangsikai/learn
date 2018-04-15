package com.lanking.cloud.domain.yoo.member;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 会员套餐组
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "member_package_group")
public class MemberPackageGroup implements Serializable {

	private static final long serialVersionUID = 874718753454713097L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 套餐组名称
	 */
	@Column(name = "name", length = 128)
	private String name;

	/**
	 * 用户类型
	 */
	@Column(name = "user_type", precision = 3)
	private UserType userType;

	/**
	 * 会员类型
	 */
	@Column(name = "member_type", precision = 3)
	private MemberType memberType;

	/**
	 * 对应阀值的利润1
	 */
	@Column(name = "profits1", scale = 2, nullable = false)
	private BigDecimal profits1;

	/**
	 * 对应阀值的利润2
	 */
	@Column(name = "profits2", scale = 2, nullable = false)
	private BigDecimal profits2;

	/**
	 * 会员套餐组适用用户群类型
	 * 
	 * <pre>
	 * 用户群类型： {@link MemberPackageGroupType}
	 * 1.ALL_CHANNEL_USER:如果套餐组对应所有渠道用户时,表  {@link MemberPackageGroupChannel} 没有对应数据
	 * 2.CUSTOM_CHANNEL_USER:套餐组指定适用的渠道，也可以限定渠道关联的某个学校,如果不限制学校,表  {@link MemberPackageGroupChannel}的school_id设置为0
	 * 3.REGISTER_USER:user.user_channel_code = 10000的用户,如果套餐组对应自主注册用户时,表  {@link MemberPackageGroupChannel} 没有对应数据
	 * </pre>
	 */
	@Column(name = "type", precision = 3)
	private MemberPackageGroupType type;

	/**
	 * 状态
	 */
	@Column(name = "status", precision = 3)
	private Status status = Status.ENABLED;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public BigDecimal getProfits1() {
		return profits1;
	}

	public void setProfits1(BigDecimal profits1) {
		this.profits1 = profits1;
	}

	public BigDecimal getProfits2() {
		return profits2;
	}

	public void setProfits2(BigDecimal profits2) {
		this.profits2 = profits2;
	}

	public MemberPackageGroupType getType() {
		return type;
	}

	public void setType(MemberPackageGroupType type) {
		this.type = type;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}

package com.lanking.cloud.domain.yoo.order.member;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.order.TradingOrderBaseInfo;

/**
 * 会员套餐订单表信息基类
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@MappedSuperclass
public abstract class MemberPackageOrderBaseInfo extends TradingOrderBaseInfo {

	private static final long serialVersionUID = 8598855042709110458L;

	/**
	 * 会员套餐ID,此字段可以为0,即不对应任何一个套餐,此时source必定为channel(满足后台设置会员指定时间)
	 */
	@Column(name = "member_package_id")
	private long memberPackageId;

	/**
	 * 会员类型
	 */
	@Column(name = "member_type", precision = 3)
	private MemberType memberType;

	/**
	 * 订单所属类型<br>
	 * 根据当前订单关联一个或者多个用户的渠道属性来设置 <br>
	 * 用户渠道代码=10000时，此字段设置为MemberPackageOrderSource.USER<br>
	 * 用户渠道代码>10000时，此字段设置为MemberPackageOrderSource.CHANNEL<br>
	 */
	@Column(name = "source", precision = 3)
	private MemberPackageOrderSource source;

	/**
	 * 订单开通人类型<br>
	 * 
	 * <pre>
	 * 用户自行开通时，此字段设置为MemberPackageOrderType.USER
	 * 渠道销售系统中开通时，根据管理员类型设置，此字段设置为MemberPackageOrderType.ADMIN|MemberPackageOrderType.CHANNEL_ADMIN
	 * </pre>
	 */
	@Column(name = "type", precision = 3)
	private MemberPackageOrderType type;

	/**
	 * 会员套餐订单状态.
	 */
	@Column(name = "status", precision = 3)
	private MemberPackageOrderStatus status = MemberPackageOrderStatus.NOT_PAY;

	/**
	 * 附加数据，一般用来存储跟业务相关的标识数据等.
	 */
	@Column(name = "attach_data", length = 500)
	private String attachData;

	/**
	 * 渠道，下单时用户所属的渠道(如果此单包含多个用户,则用户必须同属一个渠道下)
	 */
	@Column(name = "user_channel_code", precision = 5)
	private Integer userChannelCode;

	/**
	 * 对应套餐会员组ID.
	 */
	@Column(name = "member_package_group_id")
	private Long memberPackageGroupId;

	public long getMemberPackageId() {
		return memberPackageId;
	}

	public void setMemberPackageId(long memberPackageId) {
		this.memberPackageId = memberPackageId;
	}

	public MemberType getMemberType() {
		return memberType;
	}

	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}

	public MemberPackageOrderSource getSource() {
		return source;
	}

	public void setSource(MemberPackageOrderSource source) {
		this.source = source;
	}

	public MemberPackageOrderType getType() {
		return type;
	}

	public void setType(MemberPackageOrderType type) {
		this.type = type;
	}

	public MemberPackageOrderStatus getStatus() {
		return status;
	}

	public void setStatus(MemberPackageOrderStatus status) {
		this.status = status;
	}

	public String getAttachData() {
		return attachData;
	}

	public void setAttachData(String attachData) {
		this.attachData = attachData;
	}

	public Integer getUserChannelCode() {
		return userChannelCode;
	}

	public void setUserChannelCode(Integer userChannelCode) {
		this.userChannelCode = userChannelCode;
	}

	public Long getMemberPackageGroupId() {
		return memberPackageGroupId;
	}

	public void setMemberPackageGroupId(Long memberPackageGroupId) {
		this.memberPackageGroupId = memberPackageGroupId;
	}
}

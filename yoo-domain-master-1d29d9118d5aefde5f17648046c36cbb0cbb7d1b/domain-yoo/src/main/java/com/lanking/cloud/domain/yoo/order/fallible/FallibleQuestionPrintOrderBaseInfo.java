package com.lanking.cloud.domain.yoo.order.fallible;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.lanking.cloud.domain.common.baseData.Express;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.order.TradingOrderBaseInfo;

/**
 * 错题打印订单信息基类
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@MappedSuperclass
public class FallibleQuestionPrintOrderBaseInfo extends TradingOrderBaseInfo {

	private static final long serialVersionUID = -8572474167836145560L;

	/**
	 * 状态
	 */
	@Column(name = "status", precision = 3)
	private FallibleQuestionPrintOrderStatus status = FallibleQuestionPrintOrderStatus.NOT_PAY;

	/**
	 * 快递
	 */
	@Column(name = "express", precision = 3)
	private Express express;

	/**
	 * 快递号
	 */
	@Column(name = "express_code", length = 64)
	private String expressCode;

	/**
	 * 收货人
	 */
	@Column(name = "contact_name", length = 64)
	private String contactName;

	/**
	 * 收货人电话
	 */
	@Column(name = "contact_phone", length = 32)
	private String contactPhone;

	/**
	 * 收货地区
	 */
	@Column(name = "district_code")
	private long districtCode;

	/**
	 * 收货详细地址
	 */
	@Column(name = "contact_address", length = 512)
	private String contactAddress;

	/**
	 * 附加数据，一般用来存储跟业务相关的标识数据等
	 */
	@Column(name = "attach_data", length = 4000)
	private String attachData;

	/**
	 * 下单时用户的会员状态
	 */
	@Column(name = "member_type", precision = 3, nullable = false)
	private MemberType memberType = MemberType.NONE;

	public FallibleQuestionPrintOrderStatus getStatus() {
		return status;
	}

	public void setStatus(FallibleQuestionPrintOrderStatus status) {
		this.status = status;
	}

	public Express getExpress() {
		return express;
	}

	public void setExpress(Express express) {
		this.express = express;
	}

	public String getExpressCode() {
		return expressCode;
	}

	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public long getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(long districtCode) {
		this.districtCode = districtCode;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	public String getAttachData() {
		return attachData;
	}

	public void setAttachData(String attachData) {
		this.attachData = attachData;
	}

	public MemberType getMemberType() {
		return memberType;
	}

	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}
}

package com.lanking.uxb.service.fallible.form;

import java.io.Serializable;
import java.util.List;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.yoo.order.PayMode;

/**
 * 订单表单数据.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年10月8日
 */
public class ZyStuFalliblePrintOrderForm implements Serializable {
	private static final long serialVersionUID = 7713372232904448209L;

	/**
	 * 支付模式.
	 */
	private PayMode payMod = PayMode.ONLINE;

	/**
	 * 支付平台代码.
	 */
	private Integer paymentPlatformCode;

	/**
	 * 章节.
	 */
	private List<Long> sectionCodes;

	/**
	 * 时间范围（月）.
	 */
	private int timeScope = 0;

	/**
	 * 错误次数.
	 */
	private int errorTimes = 0;

	/**
	 * 错题类型.
	 */
	private List<Question.Type> types;

	/**
	 * 收货人.
	 */
	private String contactName;

	/**
	 * 收货人电话.
	 */
	private String contactPhone;

	/**
	 * 收货地区.
	 */
	private long districtCode;

	/**
	 * 收货详细地址.
	 */
	private String contactAddress;

	public PayMode getPayMod() {
		return payMod;
	}

	public void setPayMod(PayMode payMod) {
		this.payMod = payMod;
	}

	public Integer getPaymentPlatformCode() {
		return paymentPlatformCode;
	}

	public void setPaymentPlatformCode(Integer paymentPlatformCode) {
		this.paymentPlatformCode = paymentPlatformCode;
	}

	public List<Long> getSectionCodes() {
		return sectionCodes;
	}

	public void setSectionCodes(List<Long> sectionCodes) {
		this.sectionCodes = sectionCodes;
	}

	public int getTimeScope() {
		return timeScope;
	}

	public void setTimeScope(int timeScope) {
		this.timeScope = timeScope;
	}

	public int getErrorTimes() {
		return errorTimes;
	}

	public void setErrorTimes(int errorTimes) {
		this.errorTimes = errorTimes;
	}

	public List<Question.Type> getTypes() {
		return types;
	}

	public void setTypes(List<Question.Type> types) {
		this.types = types;
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
}

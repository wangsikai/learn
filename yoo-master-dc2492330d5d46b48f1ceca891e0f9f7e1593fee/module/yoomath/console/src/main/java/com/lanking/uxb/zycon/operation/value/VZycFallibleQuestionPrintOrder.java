package com.lanking.uxb.zycon.operation.value;

import java.io.Serializable;
import java.util.Date;

import com.lanking.cloud.domain.common.baseData.Express;
import com.lanking.cloud.domain.yoo.order.fallible.FallibleQuestionPrintOrderStatus;

/**
 * 代打印管理VO
 * 
 * @author wangsenhao
 * @since 2.5.0
 */
public class VZycFallibleQuestionPrintOrder implements Serializable {

	private static final long serialVersionUID = -748125980056437440L;

	private Long id;
	/**
	 * 提交时间(支付时间)
	 */
	private Date payTime;
	/**
	 * 提交账号
	 */
	private String accountName;

	private String userName;
	/**
	 * 代打印内容
	 */
	private String printName;

	/**
	 * 收货人.
	 */
	private String contactName;

	/**
	 * 收货人电话.
	 */
	private String contactPhone;

	/**
	 * 地域名称(全称)
	 */
	private String districtName;

	/**
	 * 收货详细地址.
	 */
	private String contactAddress;

	private FallibleQuestionPrintOrderStatus status;

	private Express express;

	private String expressCode;

	/**
	 * 错题导出Id(用于下载调接口用)
	 */
	private Long exportRecordId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPrintName() {
		return printName;
	}

	public void setPrintName(String printName) {
		this.printName = printName;
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

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

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

	public Long getExportRecordId() {
		return exportRecordId;
	}

	public void setExportRecordId(Long exportRecordId) {
		this.exportRecordId = exportRecordId;
	}

}

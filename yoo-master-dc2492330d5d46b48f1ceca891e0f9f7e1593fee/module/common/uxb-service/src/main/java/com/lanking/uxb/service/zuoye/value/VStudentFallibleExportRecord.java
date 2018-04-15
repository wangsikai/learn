package com.lanking.uxb.service.zuoye.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.lanking.cloud.domain.common.baseData.Express;
import com.lanking.cloud.domain.yoo.order.fallible.FallibleQuestionPrintOrderStatus;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 学生错题下载记录.
 * 
 * @author wlche
 * @since web 2.0.3
 */
public class VStudentFallibleExportRecord implements Serializable {
	private static final long serialVersionUID = -4832740806124644193L;

	private long id;
	private int hash;
	private long studentId;
	private String name;
	private Double size;
	private String extend;
	private Date creatAt;
	private int count;
	private int sellPrice;
	private BigDecimal sellPriceRMB;
	private boolean buy = false;
	private Status status = Status.ENABLED;

	/**
	 * 关联的待打印订单ID.
	 */
	private Long fallibleQuestionPrintOrderId;
	private FallibleQuestionPrintOrderStatus orderStatus;
	private Express express;
	private String expressName;
	private String expressCode;
	private String contactName;
	private String contactPhone;
	private long districtCode;
	private String contactAddress;
	private String districtName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getHash() {
		return hash;
	}

	public void setHash(int hash) {
		this.hash = hash;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getSize() {
		return size;
	}

	public void setSize(Double size) {
		this.size = size;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	public Date getCreatAt() {
		return creatAt;
	}

	public void setCreatAt(Date creatAt) {
		this.creatAt = creatAt;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(int sellPrice) {
		this.sellPrice = sellPrice;
	}

	public boolean isBuy() {
		return buy;
	}

	public void setBuy(boolean buy) {
		this.buy = buy;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Long getFallibleQuestionPrintOrderId() {
		return fallibleQuestionPrintOrderId;
	}

	public void setFallibleQuestionPrintOrderId(Long fallibleQuestionPrintOrderId) {
		this.fallibleQuestionPrintOrderId = fallibleQuestionPrintOrderId;
	}

	public BigDecimal getSellPriceRMB() {
		return sellPriceRMB;
	}

	public void setSellPriceRMB(BigDecimal sellPriceRMB) {
		this.sellPriceRMB = sellPriceRMB;
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

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public FallibleQuestionPrintOrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(FallibleQuestionPrintOrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
}

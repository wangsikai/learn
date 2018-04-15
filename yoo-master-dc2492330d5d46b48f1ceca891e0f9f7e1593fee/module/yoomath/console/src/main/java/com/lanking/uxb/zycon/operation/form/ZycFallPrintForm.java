package com.lanking.uxb.zycon.operation.form;

import com.lanking.cloud.domain.common.baseData.Express;
import com.lanking.cloud.domain.yoo.order.fallible.FallibleQuestionPrintOrderStatus;

/**
 * 待打印管理相关数据提交
 * 
 * @author wangsenhao
 * @since 2.5.0
 */
public class ZycFallPrintForm {

	private FallibleQuestionPrintOrderStatus status;
	// 快递
	private Express express;
	// 快递单号
	private String expressCode;

	private Long orderId;

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

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

}

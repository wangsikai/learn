package com.lanking.uxb.service.payment.weixin.request;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 支付结果响应数据（用于反馈给微信）.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月1日
 */
@XmlRootElement(name = "xml")
public class RespondData implements Serializable {
	private static final long serialVersionUID = -1468923021898237622L;

	/**
	 * 返回状态码.
	 */
	private String returnCode;

	/**
	 * 返回信息.
	 */
	private String returnMsg;

	@XmlElement(name = "return_code")
	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	@XmlElement(name = "return_msg")
	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
}

package com.lanking.cloud.domain.base.message.api;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.lanking.cloud.domain.base.message.api.MessageType;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.frame.system.YooApp;

/**
 * 消息包基类
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
@MappedSuperclass
public abstract class MessagePacket implements Serializable {

	private static final long serialVersionUID = 2675149942925197856L;

	/**
	 * 消息类型
	 * 
	 * @see MessageType
	 */
	private MessageType messageType;

	/**
	 * 所属产品
	 * 
	 * @see Product
	 */
	private Product product;

	/**
	 * 所属APP
	 * 
	 * @see YooApp
	 */
	private YooApp app;

	/**
	 * 消息体
	 */
	private String body;

	/**
	 * 消息模板代码
	 */
	private Integer messageTemplateCode;

	/**
	 * 消息模板title参数
	 */
	private Map<String, Object> messageTemplateTitleParams;

	/**
	 * 消息模板body参数
	 */
	private Map<String, Object> messageTemplateBodyParams;

	/**
	 * 创建时间
	 */
	private Date createAt;

	/**
	 * 发送时间
	 */
	private Date sendAt;

	/**
	 * 发送结果
	 */
	private Integer ret;

	/**
	 * 发送结果
	 */
	private Map<String, Integer> rets;

	/**
	 * 发送结果消息
	 */
	private String retMsg;

	/**
	 * 发送结果消息
	 */
	private Map<String, String> retMsgs;

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public YooApp getApp() {
		return app;
	}

	public void setApp(YooApp app) {
		this.app = app;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Integer getMessageTemplateCode() {
		return messageTemplateCode;
	}

	public void setMessageTemplateCode(Integer messageTemplateCode) {
		this.messageTemplateCode = messageTemplateCode;
	}

	public Map<String, Object> getMessageTemplateTitleParams() {
		return messageTemplateTitleParams;
	}

	public void setMessageTemplateTitleParams(Map<String, Object> messageTemplateTitleParams) {
		this.messageTemplateTitleParams = messageTemplateTitleParams;
	}

	public Map<String, Object> getMessageTemplateBodyParams() {
		return messageTemplateBodyParams;
	}

	public void setMessageTemplateBodyParams(Map<String, Object> messageTemplateBodyParams) {
		this.messageTemplateBodyParams = messageTemplateBodyParams;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getSendAt() {
		return sendAt;
	}

	public void setSendAt(Date sendAt) {
		this.sendAt = sendAt;
	}

	public Integer getRet() {
		return ret;
	}

	public void setRet(Integer ret) {
		this.ret = ret;
	}

	public Map<String, Integer> getRets() {
		return rets;
	}

	public void setRets(Map<String, Integer> rets) {
		this.rets = rets;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public Map<String, String> getRetMsgs() {
		return retMsgs;
	}

	public void setRetMsgs(Map<String, String> retMsgs) {
		this.retMsgs = retMsgs;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}

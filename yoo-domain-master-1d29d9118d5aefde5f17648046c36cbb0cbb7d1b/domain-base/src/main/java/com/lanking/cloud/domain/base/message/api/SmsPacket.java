package com.lanking.cloud.domain.base.message.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.lanking.cloud.domain.base.message.api.MessageType;

/**
 * 短信消息包
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
public class SmsPacket extends MessagePacket {

	private static final long serialVersionUID = 233651675068280802L;

	/**
	 * 接受者
	 */
	private String target;

	/**
	 * 接受者
	 */
	private Collection<String> targets;

	/**
	 * 调用结果
	 */
	private String callRet;

	SmsPacket() {
		super();
		setMessageType(MessageType.SMS);
	}

	public SmsPacket(String target) {
		this.target = target;
		setMessageType(MessageType.SMS);
	}

	public SmsPacket(String target, String body) {
		this.target = target;
		setMessageType(MessageType.SMS);
		setBody(body);
	}

	public SmsPacket(String target, int messageTemplateCode) {
		this.target = target;
		setMessageType(MessageType.SMS);
		setMessageTemplateCode(messageTemplateCode);
		setMessageTemplateBodyParams(new HashMap<String, Object>(0));
	}

	public SmsPacket(String target, int messageTemplateCode, Map<String, Object> messageTemplateBodyParams) {
		this.target = target;
		setMessageType(MessageType.SMS);
		setMessageTemplateCode(messageTemplateCode);
		setMessageTemplateBodyParams(messageTemplateBodyParams);
	}

	public SmsPacket(Collection<String> targets, String body) {
		this.targets = targets;
		setMessageType(MessageType.SMS);
		setBody(body);
	}

	public SmsPacket(Collection<String> targets, int messageTemplateCode) {
		this.targets = targets;
		setMessageType(MessageType.SMS);
		setMessageTemplateCode(messageTemplateCode);
		setMessageTemplateBodyParams(new HashMap<String, Object>(0));
	}

	public SmsPacket(Collection<String> targets, int messageTemplateCode, Map<String, Object> messageTemplateBodyParams) {
		this.targets = targets;
		setMessageType(MessageType.SMS);
		setMessageTemplateCode(messageTemplateCode);
		setMessageTemplateBodyParams(messageTemplateBodyParams);
	}

	public Collection<String> getTargets() {
		return targets;
	}

	public void setTargets(Collection<String> targets) {
		this.targets = targets;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getCallRet() {
		return callRet;
	}

	public void setCallRet(String callRet) {
		this.callRet = callRet;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}

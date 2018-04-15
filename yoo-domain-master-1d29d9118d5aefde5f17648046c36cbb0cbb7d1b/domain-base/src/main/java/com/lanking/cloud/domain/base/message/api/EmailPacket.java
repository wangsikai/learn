package com.lanking.cloud.domain.base.message.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.lanking.cloud.domain.base.message.api.MessageType;

/**
 * 邮件消息包
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
public class EmailPacket extends MessagePacket {

	private static final long serialVersionUID = 2337323981272654173L;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 消息接受对象
	 */
	private Collection<String> targets;

	/**
	 * 消息接受对象
	 */
	private String target;

	public EmailPacket() {
		super();
		setMessageType(MessageType.EMAIL);
	}

	public EmailPacket(String target, int messageTemplateCode) {
		this.target = target;
		setMessageType(MessageType.EMAIL);
		setMessageTemplateCode(messageTemplateCode);
		setMessageTemplateTitleParams(new HashMap<String, Object>(0));
		setMessageTemplateBodyParams(new HashMap<String, Object>(0));
	}

	public EmailPacket(String target, int messageTemplateCode, Map<String, Object> messageTemplateBodyParams) {
		this.target = target;
		setMessageType(MessageType.EMAIL);
		setMessageTemplateCode(messageTemplateCode);
		setMessageTemplateTitleParams(new HashMap<String, Object>(0));
		setMessageTemplateBodyParams(messageTemplateBodyParams);
	}

	public EmailPacket(Collection<String> targets, int messageTemplateCode) {
		this.targets = targets;
		setMessageType(MessageType.EMAIL);
		setMessageTemplateCode(messageTemplateCode);
		setMessageTemplateTitleParams(new HashMap<String, Object>(0));
		setMessageTemplateBodyParams(new HashMap<String, Object>(0));
	}

	public EmailPacket(Collection<String> targets, int messageTemplateCode,
			Map<String, Object> messageTemplateBodyParams) {
		this.targets = targets;
		setMessageType(MessageType.EMAIL);
		setMessageTemplateCode(messageTemplateCode);
		setMessageTemplateTitleParams(new HashMap<String, Object>(0));
		setMessageTemplateBodyParams(messageTemplateBodyParams);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}

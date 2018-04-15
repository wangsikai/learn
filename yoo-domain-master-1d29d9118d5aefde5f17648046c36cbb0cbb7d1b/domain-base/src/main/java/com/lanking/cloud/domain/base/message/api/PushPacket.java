package com.lanking.cloud.domain.base.message.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.base.message.api.MessageType;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.frame.system.YooApp;

/**
 * 推送消息包
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
public class PushPacket extends MessagePacket {

	private static final long serialVersionUID = -7304060845115064538L;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 参数
	 */
	private Map<String, Object> params = Maps.newHashMap();

	/**
	 * 接受者
	 */
	private String target;

	/**
	 * 接受者
	 */
	private Collection<String> targets;

	public PushPacket() {
		super();
		setMessageType(MessageType.PUSH);
	}

	public PushPacket(Product product, YooApp app, String target, int messageTemplateCode, Map<String, Object> params,
			String url) {
		this.target = target;
		setProduct(product);
		setApp(app);
		setMessageType(MessageType.PUSH);
		setMessageTemplateCode(messageTemplateCode);
		if (params == null) {
			setParams(new HashMap<String, Object>(0));
		} else {
			setParams(params);
		}
		if (url != null) {
			getParams().put("url", url);
		}
		setMessageTemplateTitleParams(new HashMap<String, Object>(0));
		setMessageTemplateBodyParams(new HashMap<String, Object>(0));
	}

	public PushPacket(Product product, YooApp app, String target, int messageTemplateCode, Map<String, Object> params,
			String url, Map<String, Object> messageTemplateBodyParams) {
		this.target = target;
		setProduct(product);
		setApp(app);
		setMessageType(MessageType.PUSH);
		setMessageTemplateCode(messageTemplateCode);
		if (params == null) {
			setParams(new HashMap<String, Object>(0));
		} else {
			setParams(params);
		}
		if (url != null) {
			getParams().put("url", url);
		}
		setMessageTemplateTitleParams(new HashMap<String, Object>(0));
		setMessageTemplateBodyParams(messageTemplateBodyParams);
	}

	public PushPacket(Product product, YooApp app, Collection<String> targets, int messageTemplateCode,
			Map<String, Object> params, String url) {
		this.targets = targets;
		setProduct(product);
		setApp(app);
		setMessageType(MessageType.PUSH);
		setMessageTemplateCode(messageTemplateCode);
		if (params == null) {
			setParams(new HashMap<String, Object>(0));
		} else {
			setParams(params);
		}
		if (url != null) {
			getParams().put("url", url);
		}
		setMessageTemplateTitleParams(new HashMap<String, Object>(0));
		setMessageTemplateBodyParams(new HashMap<String, Object>(0));
	}

	public PushPacket(Product product, YooApp app, Collection<String> targets, int messageTemplateCode,
			Map<String, Object> params, String url, Map<String, Object> messageTemplateBodyParams) {
		this.targets = targets;
		setProduct(product);
		setApp(app);
		setMessageType(MessageType.PUSH);
		setMessageTemplateCode(messageTemplateCode);
		if (params == null) {
			setParams(new HashMap<String, Object>(0));
		} else {
			setParams(params);
		}
		if (url != null) {
			getParams().put("url", url);
		}
		setMessageTemplateTitleParams(new HashMap<String, Object>(0));
		setMessageTemplateBodyParams(messageTemplateBodyParams);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
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

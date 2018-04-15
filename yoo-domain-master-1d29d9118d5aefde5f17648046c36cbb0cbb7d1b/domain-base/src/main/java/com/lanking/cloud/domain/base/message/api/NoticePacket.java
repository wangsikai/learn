package com.lanking.cloud.domain.base.message.api;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.base.message.api.MessageType;
import com.lanking.cloud.domain.type.Biz;

/**
 * 提醒消息包
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
public class NoticePacket extends MessagePacket {

	private static final long serialVersionUID = -3681748999064416214L;

	/**
	 * 参数
	 */
	private Map<String, Object> params = Maps.newHashMap();

	/**
	 * 发送者
	 */
	private long from;

	/**
	 * 接受者
	 */
	private long to;

	/**
	 * 接受者
	 */
	private Collection<Long> tos;

	/**
	 * 类别
	 */
	private int catgory;

	/**
	 * 类型
	 */
	private int type;

	/**
	 * 关联业务对象类型
	 * 
	 * @see Biz
	 */
	private int biz;

	/**
	 * 关联业务对象ID
	 */
	private long bizId;

	public NoticePacket() {
		super();
		setMessageType(MessageType.NOTICE);
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public long getFrom() {
		return from;
	}

	public void setFrom(long from) {
		this.from = from;
	}

	public long getTo() {
		return to;
	}

	public void setTo(long to) {
		this.to = to;
	}

	public Collection<Long> getTos() {
		return tos;
	}

	public void setTos(Collection<Long> tos) {
		this.tos = tos;
	}

	public int getCatgory() {
		return catgory;
	}

	public void setCatgory(int catgory) {
		this.catgory = catgory;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getBiz() {
		return biz;
	}

	public void setBiz(int biz) {
		this.biz = biz;
	}

	public long getBizId() {
		return bizId;
	}

	public void setBizId(long bizId) {
		this.bizId = bizId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}

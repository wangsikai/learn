package com.lanking.cloud.sdk.event;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.context.ApplicationEvent;

/**
 * 集群处理事件(主要用来处理集群环境下的数据同步等问题)
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月4日
 * @param <T>
 */
public abstract class ClusterEvent<T extends Serializable> extends ApplicationEvent {

	private static final long serialVersionUID = 8318957153039353205L;

	private String action;
	private T source;

	public ClusterEvent(String action, T source) {
		super(source == null ? new Object() : source);
		this.action = action;
		this.source = source;
	}

	public T getSource() {
		return source;
	}

	public String getAction() {
		return action;
	}

	public boolean action(String action) {
		return this.action.equals(action);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}

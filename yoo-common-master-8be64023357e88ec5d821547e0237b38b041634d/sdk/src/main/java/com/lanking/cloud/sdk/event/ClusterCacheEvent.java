package com.lanking.cloud.sdk.event;

import java.io.Serializable;

/**
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月17日
 * @param <T>
 */
public class ClusterCacheEvent<T extends Serializable> extends ClusterEvent<T> {

	private static final long serialVersionUID = -982306220333804566L;

	public ClusterCacheEvent(String action, T source) {
		super(action, source);
	}

}

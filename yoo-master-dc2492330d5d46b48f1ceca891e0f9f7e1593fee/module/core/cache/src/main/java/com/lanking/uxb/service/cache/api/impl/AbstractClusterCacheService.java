package com.lanking.uxb.service.cache.api.impl;

import javax.persistence.MappedSuperclass;

import org.springframework.context.ApplicationListener;

import com.lanking.cloud.sdk.event.ClusterCacheEvent;

@SuppressWarnings("rawtypes")
@MappedSuperclass
public abstract class AbstractClusterCacheService extends AbstractCacheService implements
		ApplicationListener<ClusterCacheEvent> {

}

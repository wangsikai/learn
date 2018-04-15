package com.lanking.uxb.service.code.api.impl.cache.local;

import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.sdk.util.MemoryUtil;
import com.lanking.uxb.service.code.api.BaseDataHandle;

public abstract class AbstractBaseDataHandle implements BaseDataHandle {

	protected Long getObjectDeepSize(Object obj) {
		return MemoryUtil.deepSizeOf(obj);
	}

	protected Long getObjectShallowSize(Object obj) {
		return MemoryUtil.shallowSizeOf(obj);
	}

	@Transactional(readOnly = true)
	@Override
	public void init() {
		reload();
	}
}

package com.lanking.cloud.ex.core;

import com.lanking.cloud.ex.AbstractCoreException;

/**
 * @since 2.5.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年11月17日
 */
@SuppressWarnings("rawtypes")
public class EntityNotFoundException extends AbstractCoreException {

	private static final long serialVersionUID = 1991351947014631615L;

	public EntityNotFoundException() {
		super(ENTITY_NOT_FOUND_EX);
	}

	public EntityNotFoundException(Class clazz, String key) {
		super(ENTITY_NOT_FOUND_EX, clazz.getName(), key);
	}

	public EntityNotFoundException(Class clazz, String key, Throwable cause) {
		super(cause, ENTITY_NOT_FOUND_EX, clazz.getName(), key);
	}
}

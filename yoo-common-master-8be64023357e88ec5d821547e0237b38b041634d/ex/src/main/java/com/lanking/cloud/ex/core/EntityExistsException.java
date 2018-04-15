package com.lanking.cloud.ex.core;

import com.lanking.cloud.ex.AbstractCoreException;

/**
 * @since 2.5.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年11月17日
 */
@SuppressWarnings("rawtypes")
public class EntityExistsException extends AbstractCoreException {

	private static final long serialVersionUID = 5697327010486471757L;

	public EntityExistsException() {
		super(ENTITY_EXISTS_EX);
	}

	public EntityExistsException(Class clazz) {
		super(ENTITY_EXISTS_EX, clazz.getName());
	}

	public EntityExistsException(Class clazz, Throwable cause) {
		super(cause, ENTITY_EXISTS_EX, clazz.getName());
	}
}

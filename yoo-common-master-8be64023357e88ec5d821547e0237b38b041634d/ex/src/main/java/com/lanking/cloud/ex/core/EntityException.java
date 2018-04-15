package com.lanking.cloud.ex.core;

import com.lanking.cloud.ex.AbstractCoreException;

/**
 * @since 2.5.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年11月17日
 */
@SuppressWarnings("rawtypes")
public class EntityException extends AbstractCoreException {

	private static final long serialVersionUID = 1991278454562045435L;

	public EntityException() {
		super(ENTITY_EX);
	}

	public EntityException(Class clazz) {
		super(ENTITY_EX, clazz.getName());
	}

	public EntityException(Class clazz, Throwable cause) {
		super(cause, ENTITY_EX, clazz.getName());
	}

}

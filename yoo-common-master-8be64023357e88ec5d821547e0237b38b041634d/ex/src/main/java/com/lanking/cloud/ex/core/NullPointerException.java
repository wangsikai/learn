package com.lanking.cloud.ex.core;

import com.lanking.cloud.ex.AbstractCoreException;

/**
 * @since 2.5.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年11月17日
 */
public class NullPointerException extends AbstractCoreException {

	private static final long serialVersionUID = 3867025435090132815L;

	public NullPointerException() {
		super(NPE_EX);
	}
}

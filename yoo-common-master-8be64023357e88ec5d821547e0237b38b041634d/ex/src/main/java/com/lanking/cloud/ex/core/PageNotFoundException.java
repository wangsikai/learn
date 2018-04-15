package com.lanking.cloud.ex.core;

import com.lanking.cloud.ex.AbstractCoreException;

/**
 * @since 2.5.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年11月17日
 */
public class PageNotFoundException extends AbstractCoreException {

	private static final long serialVersionUID = 689550079123870435L;

	public PageNotFoundException() {
		super(PAGE_NOT_FOUND_EX);
	}
}

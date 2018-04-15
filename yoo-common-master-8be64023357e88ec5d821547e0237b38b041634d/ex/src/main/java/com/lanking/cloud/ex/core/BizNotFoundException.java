package com.lanking.cloud.ex.core;

import com.lanking.cloud.ex.AbstractCoreException;

/**
 * @since 2.5.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年11月17日
 */
public class BizNotFoundException extends AbstractCoreException {

	private static final long serialVersionUID = -2986440203237848074L;

	public BizNotFoundException() {
		super(BIZ_NOT_FOUND_EX);
	}
}

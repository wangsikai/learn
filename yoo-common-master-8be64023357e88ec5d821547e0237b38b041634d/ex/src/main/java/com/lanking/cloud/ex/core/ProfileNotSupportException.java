package com.lanking.cloud.ex.core;

import com.lanking.cloud.ex.AbstractCoreException;

/**
 * @since 2.5.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年11月17日
 */
public class ProfileNotSupportException extends AbstractCoreException {

	private static final long serialVersionUID = 307867270446220922L;

	public ProfileNotSupportException() {
		super(PROFILE_NOT_SUPPORT_EX);
	}
}

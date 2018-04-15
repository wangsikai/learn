package com.lanking.uxb.service.message.util;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月18日
 */
public final class VerifyCodes {

	public static String smsCode(int length) {
		return RandomStringUtils.randomNumeric(length);
	}

	public static String emailCode(int length) {
		return RandomStringUtils.randomNumeric(length);
	}
}

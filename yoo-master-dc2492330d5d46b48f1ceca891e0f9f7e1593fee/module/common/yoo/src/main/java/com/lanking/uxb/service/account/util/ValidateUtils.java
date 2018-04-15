package com.lanking.uxb.service.account.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.account.ex.AccountException;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月11日
 *
 */
public class ValidateUtils {

	public static void validateName(String value) throws AccountException {
		boolean validate = true;
		validate = StringUtils.isNotBlank(value);
		if (validate) {
			char[] cs = value.toCharArray();
			int len = 0;
			for (char c : cs) {
				String n = String.valueOf(c);
				if (Pattern.compile("([a-z]|[A-Z]|[0-9]|[_])+").matcher(n).matches()) {
					len += 1;
				} else if (Pattern.compile("([\\u4e00-\\u9fa5])+").matcher(n).matches()) {
					len += 2;
				}
			}
			if (len < 4 || len > 16) {
				validate = false;
			}
		}
		if (validate) {
			validate = !Pattern.compile("([0-9])+").matcher(value).matches();
		}
		if (validate) {
			Pattern p = Pattern.compile("([a-z]|[A-Z]|[0-9]|[_]|[\\u4e00-\\u9fa5])+");
			Matcher m = p.matcher(value);
			validate = m.matches();
		}
		if (!validate) {
			throw new AccountException(AccountException.ACCOUNT_NAME_INVALID);
		}
	}

	public static void validateEmail(String value) throws AccountException {
		boolean validate = true;
		validate = StringUtils.isNotBlank(value);
		if (validate) {
			Pattern p = Pattern
					.compile("\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*");
			Matcher m = p.matcher(value);
			validate = m.matches();
		}
		if (!validate) {
			throw new AccountException(AccountException.ACCOUNT_EMAIL_INVALID);
		}
	}

	public static void validateMobile(String value) throws AccountException {
		boolean validate = true;
		validate = StringUtils.isNotBlank(value);
		if (validate) {
			Pattern p = Pattern.compile("(1)\\d{10}");
			Matcher m = p.matcher(value);
			validate = m.matches();
		}
		if (!validate) {
			throw new AccountException(AccountException.ACCOUNT_MOBILE_INVALID);
		}
	}

	public static void validatePassword(String value) throws AccountException {
		boolean validate = true;
		validate = StringUtils.isNotBlank(value);
		if (validate) {
			Pattern p = Pattern.compile("[^\\s\u4e00-\u9fa5]{6,16}");
			Matcher m = p.matcher(value);
			validate = m.matches();
		}
		if (validate) {
			char[] pc = value.toCharArray();
			boolean repeat = true;
			String p = String.valueOf(pc[0]);
			for (int i = 1; i < pc.length; i++) {
				if (!p.equals(String.valueOf(pc[i]))) {
					repeat = false;
					break;
				}
			}
			if (repeat) {
				validate = false;
			}
		}
		if (!validate) {
			throw new AccountException(AccountException.ACCOUNT_PASSWORD_INVALID);
		}
	}
}

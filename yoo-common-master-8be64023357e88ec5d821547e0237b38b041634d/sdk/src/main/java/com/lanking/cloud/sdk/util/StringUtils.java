package com.lanking.cloud.sdk.util;

public class StringUtils extends org.apache.commons.lang3.StringUtils {
	public StringUtils() {

	}

	/**
	 * 获得与JS前端一致的计算字符长度.
	 * 
	 * <p>
	 * 几乎所有电脑系统都支持基本拉丁字母，并各自支持不同的其他编码方式。Unicode为了和它们相互兼容， 其首256字符保留给ISO
	 * 8859-1所定义的字符，使既有的西欧语系文字的转换不需特别考量；
	 * 并且把大量相同的字符重复编到不同的字符码中去，使得旧有纷杂的编码方式得以和Unicode编码间互相直接转换， 而不会丢失任何信息. <br>
	 * {@link http://baike.baidu.com/link?url=
	 * P7lFoOwrHwaIBYVBwTte2E7d6kDeMZiAkCdpXXPr5xCG0CegSlxgfaNOHuiAFyicQ3qoE
	 * -xuo-_O7ehXpj6IO_}
	 * 
	 * @param str
	 *            输入字串
	 * @return
	 */
	public static int getJsUnicodeLength(String str) {
		if (isBlank(str)) {
			return 0;
		} else {
			int byteCount = 0;
			for (int i = 0; i < str.length(); i++) {
				byteCount = ((int) str.charAt(i)) <= 256 ? byteCount + 1 : byteCount + 2;
			}
			return byteCount;
		}
	}

	/**
	 * 通过JsUnicode长度截取字符.
	 * 
	 * @param str
	 *            原始字符串
	 * @param length
	 *            JsUnicode 长度
	 * @return
	 */
	public static String cutByJsUnicodeLength(String str, int length) {
		if (isBlank(str) || length < 1) {
			return str;
		}

		int len = StringUtils.getJsUnicodeLength(str);
		if (len > length) {
			String temp = "";
			int byteCount = 0;
			for (int i = 0; i < str.length(); i++) {
				temp += str.charAt(i);
				byteCount = ((int) str.charAt(i)) <= 256 ? byteCount + 1 : byteCount + 2;
				if (byteCount > length) {
					return temp.substring(0, temp.length() - 1);
				}
			}
		}
		return str;
	}

	public static String defaultIfBlank(String str) {
		return defaultIfBlank(str, "");
	}

	public static String safeSubString(String content, int len) {
		if (StringUtils.isBlank(content)) {
			return EMPTY;
		} else {
			if (content.length() <= len) {
				return content;
			} else {
				return content.substring(0, len);
			}
		}
	}

	public static String getMaskMobile(String mobile) {
		return mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
	}

	public static String getMaskEmail(String email) {
		String[] emailArr = email.split("@");
		if (emailArr[0].length() <= 2) {
			return emailArr[0].substring(0, 1) + "****" + "@" + emailArr[1];
		}
		return emailArr[0].substring(0, 2) + "****" + emailArr[0].substring(emailArr[0].length() - 1) + "@"
				+ emailArr[1];
	}

	public static String getMaskName(String name) {
		return name.substring(0, 1) + "****" + name.substring(name.length() - 1, name.length());
	}
}

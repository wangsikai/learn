package com.lanking.uxb.zycon.user.util;

/**
 * 用户导入验证类
 * 
 * @author wangsenhao
 *
 */
public class ZycUserExcelCheck {

	public static String ERROR_LLEGAL = "格式有误";
	public static String ERROR_LENGTH = "不符合长度";
	public static String ERROR_EXIST = "已存在";

	/**
	 * 返回当前值的长度,中文算两个字符
	 * 
	 * @param name
	 * @return
	 */
	public static Integer getLength(String name) {
		if (name == null) {
			return 0;
		}
		// 匹配中文字符
		String regular = "[\u2E80-\uFE4F]";
		int length = name.length();
		for (int i = 0; i < name.length(); i++) {
			Character temp = name.charAt(i);
			if (temp.toString().matches(regular)) {
				length += 1;
			}
		}
		return length;
	}

	/**
	 * 是否合法用户名<br>
	 * 1.支持汉字、数字、字母和“_" 的组合 <br>
	 * 2.不能是纯数字
	 * 
	 * @param name
	 * @return
	 */
	public static Boolean isLegalUser(String name) {
		String regular1 = "[\\w\u2E80-\uFE4F]+";
		if (name == null) {
			return false;
		}
		if (!name.matches(regular1)) {
			return false;
		}
		String regular2 = "[0-9]*";
		if (name.matches(regular2)) {
			return false;
		}
		return true;
	}

	/**
	 * 验证是否是数字
	 * 
	 * @param val
	 * @return
	 */
	public static Boolean isNumber(String val) {
		String regular2 = "[0-9]*";
		if (val == null) {
			return false;
		}
		if (!val.matches(regular2)) {
			return false;
		}
		return true;
	}

	/**
	 * 验证当前手机号是否合法
	 * 
	 * @param mobile
	 * @return
	 */
	public static Boolean isMobile(String mobile) {
		String regular = "1\\d{10}";
		if (mobile == null) {
			return false;
		}
		if (!mobile.matches(regular)) {
			return false;
		}
		return true;
	}

	/**
	 * 密码是否合法<br>
	 * 1.可由字母、数字和符号组成，区分大小写<br>
	 * 2.不能使用连续重复的数字或字母<br>
	 * 3.密码不能跟用户名一样
	 * 
	 * @param name
	 * @return
	 */
	public static Boolean isLegalPwd(String name, String userName) {
		String regular = "\\S+";
		String regular1 = ".*[\\u4e00-\\u9faf].*";
		if (name == null) {
			return false;
		}
		// 密码不能为中文
		if (name.matches(regular1)) {
			return false;
		}
		if (!name.matches(regular)) {
			return false;
		}
		Boolean simple = true;
		Character first = name.charAt(0);
		for (int i = 0; i < name.length(); i++) {
			if (first != name.charAt(i)) {
				simple = false;
				break;
			}
		}
		if (simple) {
			return false;
		}
		if (name.equals(userName)) {
			return false;
		}
		return true;
	}

	/**
	 * 验证是否是合法的真实姓名
	 * 
	 * @param name
	 * @return
	 */
	public static Boolean isLegalRealName(String name) {
		String regular = "[\u2E80-\uFE4Fa-zA-Z|\\s]+";
		if (name == null) {
			return false;
		}
		return name.matches(regular);
	}
}

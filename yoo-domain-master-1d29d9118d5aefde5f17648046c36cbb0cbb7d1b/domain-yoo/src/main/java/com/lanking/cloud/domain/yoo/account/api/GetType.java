package com.lanking.cloud.domain.yoo.account.api;

/**
 * 获取类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum GetType {
	/**
	 * 用户名
	 */
	NAME("name"),
	/**
	 * 手机
	 */
	MOBILE("mobile"),
	/**
	 * 邮箱
	 */
	EMAIL("email"),
	/**
	 * 密码
	 */
	PASSWORD("password");

	private String name;

	GetType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}

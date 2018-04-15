package com.lanking.uxb.service.holiday.convert;

/**
 * 假期作业vo转换条件
 * 
 * @author wangsenhao
 *
 */
public class HolidayStuHomeworkConvertOption {

	private boolean initUser = true;// 初始化用户信息（兼容设置，默认true）

	public boolean isInitUser() {
		return initUser;
	}

	public void setInitUser(boolean initUser) {
		this.initUser = initUser;
	}

}

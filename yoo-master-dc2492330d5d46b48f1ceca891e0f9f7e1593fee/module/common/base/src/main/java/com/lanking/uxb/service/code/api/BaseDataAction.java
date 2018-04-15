package com.lanking.uxb.service.code.api;

import com.lanking.cloud.sdk.util.StringUtils;

/**
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月6日
 */
public enum BaseDataAction {
	RELOAD;

	public static BaseDataAction findByName(String action) {
		if (StringUtils.isBlank(action)) {
			return null;
		}
		try {
			return Enum.valueOf(BaseDataAction.class, action);
		} catch (Exception e) {
			return null;
		}
	}
}

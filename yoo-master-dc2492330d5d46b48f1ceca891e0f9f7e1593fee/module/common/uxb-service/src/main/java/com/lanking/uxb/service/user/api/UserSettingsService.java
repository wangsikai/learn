package com.lanking.uxb.service.user.api;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.yoo.user.UserSettings;

/**
 * 用户设置相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月4日
 */
public interface UserSettingsService {

	UserSettings get(long userId);

	UserSettings safeGet(long userId);

	/**
	 * 批量获取设置
	 * 
	 * @param userIds
	 * @return
	 */
	Map<Long, UserSettings> safeGets(Collection<Long> userIds);
}

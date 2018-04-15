package com.lanking.uxb.service.thirdparty.youngy.api;

import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.uxb.service.thirdparty.youngy.form.YoungyeduUser;

/**
 * @author xinyu.zhou
 * @since 3.0.3
 */
public interface YoungyeduDataService {

	/**
	 * 创建用户
	 *
	 * @param user
	 *            {@link YoungyeduUser}
	 */
	User createUser(YoungyeduUser user, int code);

	/**
	 * 查询融捷channelCode
	 *
	 * @return 渠道商code
	 */
	Integer findYoungyChannelCode();
}

package com.lanking.uxb.service.user.api;

import com.lanking.cloud.domain.yoo.channel.UserChannel;

/**
 * 用户渠道接口
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月24日
 */
public interface UserChannelService {
	UserChannel findByCode(int code);

	UserChannel findByName(String name);
}

package com.lanking.uxb.service.session.api;

import com.lanking.cloud.domain.base.session.DeviceType;

/**
 * @since yoomath V1.9
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月30日
 */
public interface SessionActiveLogService {

	void log(String token, long userId, DeviceType deviceType, long startActiveAt, long endActiveAt);
}

package com.lanking.uxb.service.base.api;

import com.lanking.cloud.domain.base.session.DeviceType;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.version.YoomathClientVersion;

import java.util.List;

/**
 * 悠数学客户端版本升级相关接口
 * 
 * @since yoomath(mobile) V1.0.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月10日
 */
public interface YoomathClientVersionService {
	/**
	 * 查找此版本以上最新版本列表
	 *
	 * @param version
	 *            当前版本
	 * @param app
	 *            {@link YooApp}
	 * @param deviceType
	 *            {@link DeviceType}
	 * @return 版本列表
	 */
	List<YoomathClientVersion> findUpVersions(int version, YooApp app, DeviceType deviceType);
}

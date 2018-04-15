package com.lanking.uxb.zycon.client.api;

import java.util.List;

import com.lanking.cloud.domain.base.session.DeviceType;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.version.YoomathClientVersion;
import com.lanking.uxb.zycon.client.form.CVersionForm;

public interface ZycClientVersionService {
	/**
	 * 查询版本信息
	 * 
	 * @return
	 */
	List<YoomathClientVersion> queryVersion(YooApp app, DeviceType deviceType);

	/**
	 * 增加或更新版本
	 * 
	 * @param form
	 */
	void updateVersion(CVersionForm form);

	YoomathClientVersion get(long id);

	/**
	 * 版本名称是否存在
	 * 
	 * @param version
	 * @return
	 */
	boolean versionCount(YooApp app, String version, DeviceType deviceType);

	/**
	 * 是否是最大的版本号
	 * 
	 * @param versionNum
	 * @return
	 */
	boolean isMaxVersionNum(YooApp app, int versionNum, DeviceType deviceType);

	/**
	 * 根据YooApp设备类型查找最新的一期版本信息
	 *
	 * @param app
	 *            {@link YooApp}
	 * @param deviceType
	 *            {@link DeviceType}
	 * @return {@link YoomathClientVersion}
	 */
	YoomathClientVersion findOpenLatestVersion(YooApp app, DeviceType deviceType);

}

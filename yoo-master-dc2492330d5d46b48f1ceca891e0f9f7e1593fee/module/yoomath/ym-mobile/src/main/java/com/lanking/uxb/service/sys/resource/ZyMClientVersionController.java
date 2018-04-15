package com.lanking.uxb.service.sys.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.base.session.DeviceType;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.type.UpgradeType;
import com.lanking.cloud.domain.yoo.version.YoomathClientVersion;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.util.UUID;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.base.api.YoomathClientVersionService;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.sys.cache.ZyMClientVersionCacheService;

/**
 * 悠数学客户端版本升级相关接口
 * 
 * @since yoomath(mobile) V1.0.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月10日
 */
@RestController
@RequestMapping("zy/m/v")
public class ZyMClientVersionController {
	@Autowired
	private YoomathClientVersionService clientVersionService;
	@Autowired
	private ZyMClientVersionCacheService clientVersionCacheService;
	@Autowired
	private ParameterService parameterService;

	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "check" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value check(@RequestParam(required = true) String version,
			@RequestHeader(value = "APP", required = false) YooApp app,
			@RequestParam(value = "deviceId", required = false) String deviceId,
			@RequestParam(value = "manualCheckVersion", defaultValue = "false") boolean manualCheckVersion) {
		if (StringUtils.isBlank(deviceId)) {
			deviceId = String.valueOf(UUID.uuid());
		}

		Map<String, Object> data = new HashMap<String, Object>(2);
		app = app == null ? YooApp.MATH_STUDENT : app;

		List<YoomathClientVersion> versions = clientVersionService.findUpVersions(
				Integer.valueOf(version.replaceAll("\\.", "")), app, Security.getDeviceType());

		if (CollectionUtils.isNotEmpty(versions)) {
			boolean hasForceUpdateVersion = false;
			for (YoomathClientVersion v : versions) {
				if (v.getType() == UpgradeType.FORCE) {
					hasForceUpdateVersion = true;
				}
			}

			// 最新版本
			YoomathClientVersion latestVersion = versions.get(versions.size() - 1);

			Long noticeTime = clientVersionCacheService.getNoticeTime(deviceId, app, Security.getDeviceType(),
					latestVersion.getVersion());

			Parameter noticeRate = parameterService.get(Product.YOOMATH, "app.notice.rate");
			Long noticeRateMills = TimeUnit.DAYS.toMillis(Integer.valueOf(noticeRate.getValue()));

			if (Security.getDeviceType() == DeviceType.IOS) {
				Parameter noticeOpenParameter = parameterService.get(Product.YOOMATH, "app.ios.notice.open");
				if (noticeOpenParameter.getValue().equals("true")) {
					if (hasForceUpdateVersion || System.currentTimeMillis() - noticeTime >= noticeRateMills) {
						latestVersion.setType(hasForceUpdateVersion ? UpgradeType.FORCE : UpgradeType.OPTIONAL);
						data.put("upgrade", true);
						data.put("version", latestVersion);

						clientVersionCacheService.setNoticeTime(deviceId, app, Security.getDeviceType(),
								latestVersion.getVersion());
					}
				}
			} else {
				if (hasForceUpdateVersion || System.currentTimeMillis() - noticeTime >= noticeRateMills
						|| manualCheckVersion) {
					latestVersion.setType(hasForceUpdateVersion ? UpgradeType.FORCE : UpgradeType.OPTIONAL);
					data.put("upgrade", true);
					data.put("version", latestVersion);

					clientVersionCacheService.setNoticeTime(deviceId, app, Security.getDeviceType(),
							latestVersion.getVersion());
				}
			}

		} else {
			data.put("upgrade", false);
		}

		data.put("deviceId", deviceId);

		return new Value(data);
	}
}

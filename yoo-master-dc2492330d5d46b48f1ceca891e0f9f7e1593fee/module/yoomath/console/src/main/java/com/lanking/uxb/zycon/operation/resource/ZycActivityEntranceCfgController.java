package com.lanking.uxb.zycon.operation.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.zycon.operation.api.ZycActivityEntranceCfgService;
import com.lanking.uxb.zycon.operation.convert.ZycActivityEntranceCfgConvert;

/**
 * 活动入口配置接口
 * 
 * @since 2.1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年7月25日
 */
@RestController
@RequestMapping(value = "zyc/aec")
public class ZycActivityEntranceCfgController {

	@Autowired
	private ZycActivityEntranceCfgService aecService;
	@Autowired
	private ZycActivityEntranceCfgConvert aecConvert;

	@RequestMapping(value = "init", method = { RequestMethod.POST })
	public Value init() {
		YooApp[] apps = YooApp.values();
		return new Value(ValueMap.value("config", aecConvert.to(aecService.findByApp(apps[0]))).put("apps", apps));
	}

	@RequestMapping(value = "getConfig", method = { RequestMethod.POST })
	public Value getConfig(YooApp app) {
		return new Value(ValueMap.value("config", aecConvert.to(aecService.findByApp(app))));
	}

	@RequestMapping(value = "updateStatus", method = { RequestMethod.POST })
	public Value updateStatus(YooApp app, Status status) {
		return new Value(ValueMap.value("config",
				aecConvert.to(aecService.updateStatus(app, status, Security.getUserId()))));
	}

	@RequestMapping(value = "update", method = { RequestMethod.POST })
	public Value update(YooApp app, Status status, Long icon, String uri) {
		return new Value(ValueMap.value("config",
				aecConvert.to(aecService.update(app, status, icon, uri, Security.getUserId()))));
	}
}

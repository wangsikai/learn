package com.lanking.uxb.zycon.operation.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.common.EmbeddedAppLocation;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.zycon.operation.api.ZycEmbeddedAppService;
import com.lanking.uxb.zycon.operation.convert.ZycEmbeddedAppConvert;

@RestController
@RequestMapping(value = "zyc/embeddedApp")
public class ZycEmbeddedAppController {

	@Autowired
	private ZycEmbeddedAppService embeddedAppService;
	@Autowired
	private ZycEmbeddedAppConvert embeddedAppConvert;

	@RequestMapping(value = "init", method = { RequestMethod.POST })
	public Value init(YooApp app, EmbeddedAppLocation location) {
		if (location == null) {
			YooApp[] apps = YooApp.values();
			EmbeddedAppLocation[] locations = EmbeddedAppLocation.values();
			return new Value(ValueMap
					.value("embeddedApps", embeddedAppConvert.to(embeddedAppService.list(apps[0], locations[0])))
					.put("apps", apps).put("locations", locations));
		} else {
			return new Value(ValueMap.value("embeddedApps",
					embeddedAppConvert.to(embeddedAppService.list(app, location))));
		}
	}

	@RequestMapping(value = "order", method = { RequestMethod.POST })
	public Value order(YooApp app, EmbeddedAppLocation location, Long[] ids) {
		return new Value(ValueMap.value("embeddedApps",
				embeddedAppConvert.to(embeddedAppService.order(app, location, ids))));
	}

	@RequestMapping(value = "del", method = { RequestMethod.POST })
	public Value del(Long id) {
		return new Value(ValueMap.value("embeddedApps", embeddedAppConvert.to(embeddedAppService.del(id))));
	}

	@RequestMapping(value = "saveUpdate", method = { RequestMethod.POST })
	public Value saveUpdate(Long id, YooApp app, EmbeddedAppLocation location, String name, Long imageId, String url) {
		embeddedAppService.saveUpdate(id, app, location, name, imageId, url);
		return new Value(ValueMap.value("embeddedApps", embeddedAppConvert.to(embeddedAppService.list(app, location))));
	}
}

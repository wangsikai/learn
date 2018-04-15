package com.lanking.uxb.zycon.operation.api;

import java.util.List;

import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.common.EmbeddedApp;
import com.lanking.cloud.domain.yoo.common.EmbeddedAppLocation;

public interface ZycEmbeddedAppService {

	List<EmbeddedApp> list(YooApp app, EmbeddedAppLocation location);

	List<EmbeddedApp> order(YooApp app, EmbeddedAppLocation location, Long[] ids);

	List<EmbeddedApp> del(Long id);

	EmbeddedApp saveUpdate(Long id, YooApp app, EmbeddedAppLocation location, String name, Long imageId, String url);
}

package com.lanking.uxb.service.sys.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.common.EmbeddedApp;

public interface EmbeddedAppService {

	List<EmbeddedApp> list(EmbeddedAppQuery query);

}

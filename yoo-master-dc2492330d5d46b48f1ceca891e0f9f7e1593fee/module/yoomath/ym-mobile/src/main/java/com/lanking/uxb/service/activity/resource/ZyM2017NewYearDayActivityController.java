package com.lanking.uxb.service.activity.resource;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;

@ApiAllowed
@RestController
@RequestMapping("zy/m/activity/nyd2017")
public class ZyM2017NewYearDayActivityController {

	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "getIncrCoins", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getIncrCoins() {
		Map<String, Object> map = new HashMap<String, Object>(3);
		map.put("preheat", false);
		map.put("incr", 0);
		map.put("total", 0);
		return new Value(map);
	}

}

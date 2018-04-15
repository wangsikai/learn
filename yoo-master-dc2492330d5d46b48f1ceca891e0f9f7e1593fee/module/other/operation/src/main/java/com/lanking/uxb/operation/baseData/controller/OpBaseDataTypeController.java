package com.lanking.uxb.operation.baseData.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.sdk.event.ClusterEvent;
import com.lanking.cloud.sdk.event.ClusterEventSender;
import com.lanking.cloud.sdk.event.LocalCacheEvent;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.code.api.BaseDataAction;
import com.lanking.uxb.service.code.api.BaseDataType;

/**
 * baseDataType
 * 
 * @author wangsenhao
 *
 */
@RestController
@RequestMapping(value = "op/baseData")
public class OpBaseDataTypeController {

	@Autowired
	@Qualifier(value = "clusterDataSender")
	private ClusterEventSender sender;

	@RequestMapping(value = "list", method = { RequestMethod.GET, RequestMethod.POST })
	public Value list() {
		return new Value(BaseDataType.values());
	}

	@RequestMapping(value = "reload", method = { RequestMethod.GET, RequestMethod.POST })
	public Value reload(BaseDataType type) {
		ClusterEvent<String> e = new LocalCacheEvent<String>(BaseDataAction.RELOAD.name(), type.name());
		sender.send(e);
		return new Value();
	}

}

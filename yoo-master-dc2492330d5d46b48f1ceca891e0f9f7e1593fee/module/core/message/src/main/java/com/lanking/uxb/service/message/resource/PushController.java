package com.lanking.uxb.service.message.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.message.cache.PushCacheService;

/**
 * push的相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月30日
 */
@RestController
@RequestMapping("msg/push")
public class PushController {

	@Autowired
	private PushCacheService pushCacheService;

	@RolesAllowed(anyone = true)
	@RequestMapping(value = "clear", method = { RequestMethod.POST, RequestMethod.GET })
	public Value clear(Product product, String token) {
		pushCacheService.clearUnReadPushCount(product, token);
		return new Value();
	}
}

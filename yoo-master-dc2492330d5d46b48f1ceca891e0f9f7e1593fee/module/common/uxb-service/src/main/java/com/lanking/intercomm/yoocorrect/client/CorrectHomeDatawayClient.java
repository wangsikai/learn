package com.lanking.intercomm.yoocorrect.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lanking.cloud.sdk.value.Value;

/**
 * 小悠快批-首页服务客户端.
 * 
 * @author wanlong.che
 *
 */
@FeignClient("${correct-server.name}")
public interface CorrectHomeDatawayClient {

	/**
	 * 获取快批首页数据.
	 * 
	 * @param uxbUserId
	 *            UXB用户ID
	 * @return
	 */
	@RequestMapping(value = "/home/getHomeData", method = { RequestMethod.POST })
	Value getHomeData(@RequestParam("userId") Long userId);

	/**
	 * 获取快批配置.
	 * 
	 * @param uxbUserId
	 *            UXB用户ID
	 * @return
	 */
	@RequestMapping(value = "/home/getConfig", method = { RequestMethod.POST })
	Value getConfig();
}

/**
 * 
 */
package com.lanking.intercomm.yoocorrect;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lanking.intercomm.yoocorrect.dto.HwRequest;
import com.lanking.intercomm.yoocorrect.dto.HwResponse;

/**
 * @author michael see yoo-correct-service for api definition
 *
 */
@FeignClient("yoo-correct")
public interface DemoServiceClient {

	@RequestMapping(value = "/demo", method = { RequestMethod.POST })
	HwResponse demo(HwRequest hwRequest);

}

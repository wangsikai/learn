package com.lanking.intercomm.yoocorrect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.intercomm.yoocorrect.dto.HwRequest;
import com.lanking.intercomm.yoocorrect.dto.HwResponse;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author michael controller for a rest call to yoo-correct jvm
 */
@Slf4j
@RestController
public class DemoServiceController {

	@Autowired
	private DemoServiceClient demoClient;

	@RolesAllowed(anyone = true)
	@RequestMapping(value = "/demo-feign", method = { RequestMethod.POST })
	@MemberAllowed
	public HwResponse demo(@RequestBody HwRequest hwRequest) {

		log.info("uxb hw request: {}", hwRequest.getInfo());
		Long start = System.currentTimeMillis();
		HwResponse resp = demoClient.demo(hwRequest);
		log.info("rest feign client call takes {} ms", System.currentTimeMillis() - start);

		return resp;

	}

}

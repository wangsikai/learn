package com.lanking.uxb.service.sys.resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;

@RestController
@RequestMapping("zy/m/api")
public class ZyMAPIController {

	/**
	 * 记录api反馈
	 * 
	 * @param statusCode
	 *            请求状态码
	 * @param api
	 *            rest api
	 * @param requestAt
	 *            请求时间(毫秒)
	 * @param cost
	 *            响应时间(毫秒)
	 * @param response
	 *            响应内容
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@RequestMapping(value = { "fb" }, method = { RequestMethod.POST })
	public Value feedback(Integer statusCode, String api, Long requestAt, Long cost, String response) {
		return new Value();
	}
}

package com.lanking.cloud.handwriting.resource;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.handwriting.form.HwRequest;
import com.lanking.cloud.handwriting.form.HwResponse;
import com.lanking.cloud.handwriting.service.HwProxyService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping({ "hw", "proxy/hw" })
@Slf4j
public class HandwritingProxyController {

	@Autowired
	private HwProxyService hwProxyService;

	@RequestMapping(value = "mathreco", method = { RequestMethod.POST, RequestMethod.GET })
	public HwResponse mathreco(@RequestBody HwRequest hwRequest) {
		hwRequest.setId(System.currentTimeMillis());

		hwProxyService.record(hwRequest);
		HwResponse hwResponse = null;
		try {
			hwResponse = hwProxyService.proxy(hwRequest);
		} catch (IOException e) {
			log.error("proxy error:", e);
		} finally {
			if (hwResponse != null) {
				hwProxyService.response(hwResponse);
			}
		}
		return hwResponse;
	}
}

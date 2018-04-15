package com.lanking.cloud.handwriting.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.lanking.cloud.handwriting.form.HwRequest;
import com.lanking.cloud.handwriting.form.HwResponse;

public interface HwProxyService {

	void record(HwRequest hwRequest);

	HwResponse proxy(HwRequest hwRequest) throws ClientProtocolException, IOException;

	void response(HwResponse hwResponse);
}

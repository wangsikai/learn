package com.tencent.service;

import com.tencent.common.APIConfigure;
import com.tencent.common.Configure;
import com.tencent.protocol.pay_protocol.ScanPayReqData;

/**
 * User: rizenguo Date: 2014/10/29 Time: 16:03
 */
public class ScanPayService extends BaseService {

	public ScanPayService(Configure configure) throws IllegalAccessException, InstantiationException,
			ClassNotFoundException {
		super(APIConfigure.PAY_API, configure);
	}

	/**
	 * 请求支付服务
	 * 
	 * @param scanPayReqData
	 *            这个数据对象里面包含了API要求提交的各种数据字段
	 * @return API返回的数据
	 * @throws Exception
	 */
	public String request(ScanPayReqData scanPayReqData, Configure configure) throws Exception {

		// --------------------------------------------------------------------
		// 发送HTTPS的Post请求到API地址
		// --------------------------------------------------------------------
		String responseString = sendPost(scanPayReqData, configure);

		return responseString;
	}
}

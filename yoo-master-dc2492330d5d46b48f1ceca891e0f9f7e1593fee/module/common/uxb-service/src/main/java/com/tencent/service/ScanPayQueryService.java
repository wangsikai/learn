package com.tencent.service;

import com.tencent.common.APIConfigure;
import com.tencent.common.Configure;
import com.tencent.protocol.pay_query_protocol.ScanPayQueryReqData;

/**
 * User: rizenguo Date: 2014/10/29 Time: 16:04
 */
public class ScanPayQueryService extends BaseService {

	public ScanPayQueryService(Configure configure) throws IllegalAccessException, InstantiationException,
			ClassNotFoundException {
		super(APIConfigure.PAY_QUERY_API, configure);
	}

	/**
	 * 请求支付查询服务
	 * 
	 * @param scanPayQueryReqData
	 *            这个数据对象里面包含了API要求提交的各种数据字段
	 * @return API返回的XML数据
	 * @throws Exception
	 */
	public String request(ScanPayQueryReqData scanPayQueryReqData, Configure configure) throws Exception {

		// --------------------------------------------------------------------
		// 发送HTTPS的Post请求到API地址
		// --------------------------------------------------------------------
		String responseString = sendPost(scanPayQueryReqData, configure);

		return responseString;
	}

}

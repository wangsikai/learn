package com.tencent;

import com.tencent.business.DownloadBillBusiness;
import com.tencent.business.RefundBusiness;
import com.tencent.business.RefundQueryBusiness;
import com.tencent.business.ScanPayBusiness;
import com.tencent.common.Configure;
import com.tencent.protocol.downloadbill_protocol.DownloadBillReqData;
import com.tencent.protocol.pay_protocol.ScanPayReqData;
import com.tencent.protocol.pay_query_protocol.ScanPayQueryReqData;
import com.tencent.protocol.refund_protocol.RefundReqData;
import com.tencent.protocol.refund_query_protocol.RefundQueryReqData;
import com.tencent.protocol.reverse_protocol.ReverseReqData;
import com.tencent.service.DownloadBillService;
import com.tencent.service.RefundQueryService;
import com.tencent.service.RefundService;
import com.tencent.service.ReverseService;
import com.tencent.service.ScanPayQueryService;
import com.tencent.service.ScanPayService;

/**
 * SDK总入口
 */
public class WXPay {

	/**
	 * 初始化SDK依赖的几个关键配置
	 * 
	 * @param key
	 *            签名算法需要用到的秘钥
	 * @param appID
	 *            公众账号ID
	 * @param mchID
	 *            商户ID
	 * @param sdbMchID
	 *            子商户ID，受理模式必填
	 * @param certLocalPath
	 *            HTTP证书在服务器中的路径，用来加载证书用
	 * @param certPassword
	 *            HTTP证书的密码，默认等于MCHID
	 */
	// public static void initSDKConfiguration(String key, String appID, String
	// mchID, String sdbMchID,
	// String certLocalPath, String certPassword) {
	// configure.setKey(key);
	// configure.setAppID(appID);
	// configure.setMchID(mchID);
	// configure.setSubMchID(sdbMchID);
	// configure.setCertLocalPath(certLocalPath);
	// configure.setCertPassword(certPassword);
	// }

	/**
	 * 请求支付服务
	 * 
	 * @param scanPayReqData
	 *            这个数据对象里面包含了API要求提交的各种数据字段
	 * @return API返回的数据
	 * @throws Exception
	 */
	public static String requestScanPayService(Configure configure, ScanPayReqData scanPayReqData) throws Exception {
		return new ScanPayService(configure).request(scanPayReqData, configure);
	}

	/**
	 * 请求支付查询服务
	 * 
	 * @param scanPayQueryReqData
	 *            这个数据对象里面包含了API要求提交的各种数据字段
	 * @return API返回的XML数据
	 * @throws Exception
	 */
	public static String requestScanPayQueryService(Configure configure, ScanPayQueryReqData scanPayQueryReqData)
			throws Exception {
		return new ScanPayQueryService(configure).request(scanPayQueryReqData, configure);
	}

	/**
	 * 请求退款服务
	 * 
	 * @param refundReqData
	 *            这个数据对象里面包含了API要求提交的各种数据字段
	 * @return API返回的XML数据
	 * @throws Exception
	 */
	public static String requestRefundService(Configure configure, RefundReqData refundReqData) throws Exception {
		return new RefundService(configure).request(refundReqData, configure);
	}

	/**
	 * 请求退款查询服务
	 * 
	 * @param refundQueryReqData
	 *            这个数据对象里面包含了API要求提交的各种数据字段
	 * @return API返回的XML数据
	 * @throws Exception
	 */
	public static String requestRefundQueryService(Configure configure, RefundQueryReqData refundQueryReqData)
			throws Exception {
		return new RefundQueryService(configure).request(refundQueryReqData, configure);
	}

	/**
	 * 请求撤销服务
	 * 
	 * @param reverseReqData
	 *            这个数据对象里面包含了API要求提交的各种数据字段
	 * @return API返回的XML数据
	 * @throws Exception
	 */
	public static String requestReverseService(Configure configure, ReverseReqData reverseReqData) throws Exception {
		return new ReverseService(configure).request(reverseReqData, configure);
	}

	/**
	 * 请求对账单下载服务
	 * 
	 * @param downloadBillReqData
	 *            这个数据对象里面包含了API要求提交的各种数据字段
	 * @return API返回的XML数据
	 * @throws Exception
	 */
	public static String requestDownloadBillService(Configure configure, DownloadBillReqData downloadBillReqData)
			throws Exception {
		return new DownloadBillService(configure).request(downloadBillReqData, configure);
	}

	/**
	 * 直接执行被扫支付业务逻辑（包含最佳实践流程）
	 * 
	 * @param scanPayReqData
	 *            这个数据对象里面包含了API要求提交的各种数据字段
	 * @param resultListener
	 *            商户需要自己监听被扫支付业务逻辑可能触发的各种分支事件，并做好合理的响应处理
	 * @throws Exception
	 */
	public static void doScanPayBusiness(Configure configure, ScanPayReqData scanPayReqData,
			ScanPayBusiness.ResultListener resultListener) throws Exception {
		new ScanPayBusiness(configure).run(scanPayReqData, resultListener);
	}

	/**
	 * 调用退款业务逻辑
	 * 
	 * @param refundReqData
	 *            这个数据对象里面包含了API要求提交的各种数据字段
	 * @param resultListener
	 *            业务逻辑可能走到的结果分支，需要商户处理
	 * @throws Exception
	 */
	public static void doRefundBusiness(Configure configure, RefundReqData refundReqData,
			RefundBusiness.ResultListener resultListener) throws Exception {
		new RefundBusiness(configure).run(refundReqData, resultListener);
	}

	/**
	 * 运行退款查询的业务逻辑
	 * 
	 * @param refundQueryReqData
	 *            这个数据对象里面包含了API要求提交的各种数据字段
	 * @param resultListener
	 *            商户需要自己监听被扫支付业务逻辑可能触发的各种分支事件，并做好合理的响应处理
	 * @throws Exception
	 */
	public static void doRefundQueryBusiness(Configure configure, RefundQueryReqData refundQueryReqData,
			RefundQueryBusiness.ResultListener resultListener) throws Exception {
		new RefundQueryBusiness(configure).run(refundQueryReqData, resultListener);
	}

	/**
	 * 请求对账单下载服务
	 * 
	 * @param downloadBillReqData
	 *            这个数据对象里面包含了API要求提交的各种数据字段
	 * @param resultListener
	 *            商户需要自己监听被扫支付业务逻辑可能触发的各种分支事件，并做好合理的响应处理
	 * @return API返回的XML数据
	 * @throws Exception
	 */
	public static void doDownloadBillBusiness(Configure configure, DownloadBillReqData downloadBillReqData,
			DownloadBillBusiness.ResultListener resultListener) throws Exception {
		new DownloadBillBusiness(configure).run(downloadBillReqData, resultListener);
	}

}

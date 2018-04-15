package com.tencent.common;

// 以下是几个API的路径：
public class APIConfigure {

	// 1）被扫支付API
	public static String PAY_API = "https://api.mch.weixin.qq.com/pay/micropay";

	// 2）被扫支付查询API
	public static String PAY_QUERY_API = "https://api.mch.weixin.qq.com/pay/orderquery";

	// 3）退款API
	public static String REFUND_API = "https://api.mch.weixin.qq.com/secapi/pay/refund";

	// 4）退款查询API
	public static String REFUND_QUERY_API = "https://api.mch.weixin.qq.com/pay/refundquery";

	// 5）撤销API
	public static String REVERSE_API = "https://api.mch.weixin.qq.com/secapi/pay/reverse";

	// 6）下载对账单API
	public static String DOWNLOAD_BILL_API = "https://api.mch.weixin.qq.com/pay/downloadbill";

	// 7) 统计上报API
	public static String REPORT_API = "https://api.mch.weixin.qq.com/payitil/report";

	// 统一支付URL
	public static String URL_UNIFIED_PAY = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	// 是否使用异步线程的方式来上报API测速，默认为异步模式
	private static boolean useThreadToDoReport = true;

	public static String HttpsRequestClassName = "com.tencent.common.HttpsRequest";

	public static boolean isUseThreadToDoReport() {
		return useThreadToDoReport;
	}

	public static void setUseThreadToDoReport(boolean useThreadToDoReport) {
		APIConfigure.useThreadToDoReport = useThreadToDoReport;
	}

	public static void setHttpsRequestClassName(String name) {
		HttpsRequestClassName = name;
	}
}

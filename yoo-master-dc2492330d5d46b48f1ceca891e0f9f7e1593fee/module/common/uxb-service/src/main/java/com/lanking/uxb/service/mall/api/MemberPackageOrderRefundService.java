package com.lanking.uxb.service.mall.api;

/**
 * 订单返款接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2017年3月2日
 */
public interface MemberPackageOrderRefundService {

	/**
	 * 返款操作，对已有的单子数据做更新.
	 * 
	 * @param divideNum
	 *            阀值人数
	 * @param userChannelCode
	 *            渠道
	 */
	public void refund(int divideNum, int userChannelCode);

	/**
	 * 返款操作，对已有的单子数据做更新.
	 * 
	 * @param divideNum
	 *            阀值人数
	 */
	public void refund(int divideNum);
}
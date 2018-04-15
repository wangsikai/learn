/**
 * 
 */
package com.lanking.uxb.service.syncOrder.api;

import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrder;

/**
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 * @author zemin.song
 * @since 2.1.1
 */
public interface TaskSyncOrderOpenVipService {

	/**
	 * 通过支付单号核对支付结果
	 * 
	 * @param payCode
	 *            支付单号
	 * @param paymentPlatformCode
	 *            支付平台
	 * @return
	 */
	public void checkPayCode(MemberPackageOrder order);

}

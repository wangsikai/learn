package com.lanking.uxb.zycon.operation.api;

import com.lanking.cloud.domain.yoo.order.fallible.FallibleQuestionPrintOrder;
import com.lanking.cloud.domain.yoo.order.fallible.FallibleQuestionPrintOrderStatus;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.zycon.operation.form.ZycFallPrintForm;

/**
 * 错题代打印订单接口
 * 
 * @author wangsenhao
 * @since 2.5.0
 */
public interface ZycFallibleQuestionPrintOrderService {
	/**
	 * 查询打印列表
	 * 
	 * @param query
	 * @param p
	 * @return
	 */
	Page<FallibleQuestionPrintOrder> queryPrintList(ZycFallPrintQuery query, Pageable p);

	/**
	 * 修改打印相关信息
	 * 
	 * @param form
	 */
	void savePrint(ZycFallPrintForm form);

	/**
	 * 
	 * @param status
	 * @return
	 */
	Long countPrintList(FallibleQuestionPrintOrderStatus status);
}

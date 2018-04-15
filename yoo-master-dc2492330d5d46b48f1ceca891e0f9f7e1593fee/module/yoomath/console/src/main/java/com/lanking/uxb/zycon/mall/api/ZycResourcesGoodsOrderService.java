package com.lanking.uxb.zycon.mall.api;

import java.util.Map;

import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.zycon.mall.form.ExamOrdersQueryForm;
import com.lanking.uxb.zycon.mall.value.VZycTotalOrdersData;

/**
 * 资源商品订单接口
 * 
 * @author zemin.song
 * @version 2016年9月11日
 */
public interface ZycResourcesGoodsOrderService {

	/**
	 * 统计订单数量
	 * 
	 * @param goodsId
	 *            商品ID
	 * @param type
	 *            订单类型
	 * @param status
	 *            订单状态
	 * @return
	 */
	VZycTotalOrdersData totalOrders(ExamOrdersQueryForm form);

	/**
	 * 批量统计订单数量
	 * 
	 * @param form
	 * @return
	 */
	Page<Map> mGetResourcesGoodsOrders(ExamOrdersQueryForm form, Pageable pageable);

}

package com.lanking.uxb.service.mall.api.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.yoo.goods.Goods;
import com.lanking.cloud.domain.yoo.order.resources.ResourcesGoodsOrder;
import com.lanking.cloud.sdk.web.WebUtils;
import com.lanking.uxb.service.mall.api.GoodsService;
import com.lanking.uxb.service.mall.api.ResourcesGoodsOrderService;
import com.lanking.uxb.service.mall.api.ResourcesGoodsPaymentService;
import com.lanking.uxb.service.payment.OrderPayBusinessSpace;
import com.lanking.uxb.service.payment.alipay.api.AlipayService;
import com.lanking.uxb.service.payment.alipay.request.DirectPayData;
import com.lanking.uxb.service.payment.weixin.api.WXPaymentService;
import com.lanking.uxb.service.payment.weixin.response.UnifiedPayResult;

/**
 * 资源商品支付接口实现.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月2日
 */
@Service
public class ResourcesGoodsPaymentServiceImpl implements ResourcesGoodsPaymentService {

	@Autowired
	private WXPaymentService wxPaymentService;
	@Autowired
	private AlipayService alipayService;
	@Autowired
	private ResourcesGoodsOrderService resourcesGoodsOrderService;
	@Autowired
	private GoodsService goodsService;

	@Override
	public UnifiedPayResult getWXQRCodeImage(String appid, long resourcesGoodsID, long orderID,
			OrderPayBusinessSpace space, HttpServletRequest request, HttpServletResponse response) {

		// 查询订单
		ResourcesGoodsOrder order = resourcesGoodsOrderService.get(orderID);
		Goods goods = goodsService.get(resourcesGoodsID);

		String attach = "";
		String businessID = String.valueOf(orderID);
		String spbill_create_ip = WebUtils.getRealIP(request); // 用户客户端IP地址
		int total_fee = (int) (order.getTotalPrice().doubleValue() * 100); // 订单金额分值
		try {
			return wxPaymentService.unifiedPayOrder(appid, space, goods.getName(), attach, businessID,
					spbill_create_ip, total_fee, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void jumpToAlipay(long resourcesGoodsID, long orderID, OrderPayBusinessSpace space,
			HttpServletRequest request, HttpServletResponse response) {

		// 查询订单
		ResourcesGoodsOrder order = resourcesGoodsOrderService.get(orderID);
		Goods goods = goodsService.get(resourcesGoodsID);
		String price = String.format("%.2f", goods.getPriceRMB().doubleValue());
		String totalPrice = String.format("%.2f", order.getTotalPrice().doubleValue());

		DirectPayData directPayData = new DirectPayData();
		directPayData.setExtra_common_param("");
		directPayData.setGoods_type("0");
		directPayData.setOut_trade_no(String.valueOf(orderID));
		directPayData.setPrice(price);
		directPayData.setQuantity(1);
		directPayData.setSubject(goods.getName());
		directPayData.setTotal_fee(totalPrice);

		try {
			alipayService.jumpToAlipay(space, directPayData, request, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

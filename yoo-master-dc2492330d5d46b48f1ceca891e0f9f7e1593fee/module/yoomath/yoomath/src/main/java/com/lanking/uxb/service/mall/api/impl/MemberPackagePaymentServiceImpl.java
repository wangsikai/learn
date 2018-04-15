package com.lanking.uxb.service.mall.api.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.yoo.member.MemberPackage;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrder;
import com.lanking.cloud.sdk.web.WebUtils;
import com.lanking.uxb.service.mall.api.MemberPackageOrderService;
import com.lanking.uxb.service.mall.api.MemberPackagePaymentService;
import com.lanking.uxb.service.mall.api.MemberPackageService;
import com.lanking.uxb.service.payment.OrderPayBusinessSpace;
import com.lanking.uxb.service.payment.alipay.api.AlipayService;
import com.lanking.uxb.service.payment.alipay.request.DirectPayData;
import com.lanking.uxb.service.payment.weixin.api.WXPaymentService;
import com.lanking.uxb.service.payment.weixin.response.UnifiedPayResult;

@Service
public class MemberPackagePaymentServiceImpl implements MemberPackagePaymentService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MemberPackageOrderService memberPackageOrderService;
	@Autowired
	private MemberPackageService memberPackageService;
	@Autowired
	private WXPaymentService wxPaymentService;
	@Autowired
	private AlipayService alipayService;

	@Override
	public UnifiedPayResult getWXQRCodeImage(String appid, long memberPackageID, long orderID,
			OrderPayBusinessSpace space, String trade_typ, String open_id, HttpServletRequest request,
			HttpServletResponse response) {

		// 查询订单
		MemberPackageOrder order = memberPackageOrderService.get(orderID);
		MemberPackage memberPackage = memberPackageService.get(memberPackageID);

		String name = "会员套餐：";
		if (memberPackage.getMonth() % 12 == 0) {
			name += memberPackage.getMonth() / 12 + "年";
		} else {
			name += memberPackage.getMonth() + "个月";
		}
		String attach = "";
		String businessID = String.valueOf(orderID);
		String spbill_create_ip = WebUtils.getRealIP(request); // 用户客户端IP地址
		int total_fee = (int) (order.getTotalPrice().doubleValue() * 100); // 订单金额分值
		try {
			return wxPaymentService.unifiedPayOrder(appid, space, name, attach, businessID, spbill_create_ip,
					total_fee, trade_typ, open_id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public void jumpToAlipay(long memberPackageID, long orderID, OrderPayBusinessSpace space,
			HttpServletRequest request, HttpServletResponse response) {

		// 查询订单
		MemberPackageOrder order = memberPackageOrderService.get(orderID);
		MemberPackage memberPackage = memberPackageService.get(memberPackageID);

		String name = "会员套餐：";
		if (memberPackage.getMonth() % 12 == 0) {
			name += memberPackage.getMonth() / 12 + "年";
		} else {
			name += memberPackage.getMonth() + "个月";
		}

		String price = String.format("%.2f", memberPackage.getPresentPrice().doubleValue());
		String totalPrice = String.format("%.2f", order.getTotalPrice().doubleValue());

		DirectPayData directPayData = new DirectPayData();
		directPayData.setExtra_common_param("");
		directPayData.setGoods_type("0");
		directPayData.setOut_trade_no(String.valueOf(orderID));
		directPayData.setPrice(price);
		directPayData.setQuantity(1);
		directPayData.setSubject(name);
		directPayData.setTotal_fee(totalPrice);

		try {
			alipayService.jumpToAlipay(space, directPayData, request, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void jumpToAlipayWap(long memberPackageID, long orderID, OrderPayBusinessSpace space, String return_url,
			HttpServletRequest request, HttpServletResponse response) {

		// 查询订单
		MemberPackageOrder order = memberPackageOrderService.get(orderID);
		MemberPackage memberPackage = memberPackageService.get(memberPackageID);

		String name = "会员套餐：";
		if (memberPackage.getMonth() % 12 == 0) {
			name += memberPackage.getMonth() / 12 + "年";
		} else {
			name += memberPackage.getMonth() + "个月";
		}

		String price = String.format("%.2f", memberPackage.getPresentPrice().doubleValue());
		String totalPrice = String.format("%.2f", order.getTotalPrice().doubleValue());

		DirectPayData directPayData = new DirectPayData();
		directPayData.setExtra_common_param("");
		directPayData.setGoods_type("0");
		directPayData.setOut_trade_no(String.valueOf(orderID));
		directPayData.setPrice(price);
		directPayData.setQuantity(1);
		directPayData.setSubject(name);
		directPayData.setTotal_fee(totalPrice);
		directPayData.setApp_pay("Y");

		try {
			alipayService.jumpToAlipayWap(space, directPayData, return_url, request, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

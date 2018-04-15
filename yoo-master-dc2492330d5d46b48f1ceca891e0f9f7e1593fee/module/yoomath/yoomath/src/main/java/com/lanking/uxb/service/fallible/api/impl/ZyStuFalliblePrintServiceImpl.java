package com.lanking.uxb.service.fallible.api.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.order.PayMode;
import com.lanking.cloud.domain.yoo.order.fallible.FallibleQuestionPrintOrder;
import com.lanking.cloud.domain.yoo.order.fallible.FallibleQuestionPrintOrderSnapshot;
import com.lanking.cloud.domain.yoo.order.fallible.FallibleQuestionPrintOrderStatus;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.web.WebUtils;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.fallible.api.ZyStuFalliblePrintService;
import com.lanking.uxb.service.mall.api.GoodsOrderService;
import com.lanking.uxb.service.payment.OrderPayBusinessSpace;
import com.lanking.uxb.service.payment.alipay.api.AlipayService;
import com.lanking.uxb.service.payment.alipay.request.DirectPayData;
import com.lanking.uxb.service.payment.weixin.api.WXPaymentService;
import com.lanking.uxb.service.payment.weixin.response.UnifiedPayResult;

/**
 * 错题待打印服务接口实现.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年10月8日
 */
@Service
@Transactional(readOnly = true)
public class ZyStuFalliblePrintServiceImpl implements ZyStuFalliblePrintService {
	@Autowired
	@Qualifier("FallibleQuestionPrintOrderRepo")
	private Repo<FallibleQuestionPrintOrder, Long> repo;
	@Autowired
	@Qualifier("FallibleQuestionPrintOrderSnapshotRepo")
	private Repo<FallibleQuestionPrintOrderSnapshot, Long> snapshotRepo;

	@Autowired
	private GoodsOrderService goodsOrderService;
	@Autowired
	private WXPaymentService wxPaymentService;
	@Autowired
	private AlipayService alipayService;

	@Override
	@Transactional
	public FallibleQuestionPrintOrder createOrder(long userID, PayMode payMod, Integer paymentPlatformCode,
			BigDecimal totalPrice, String attachData, String contactName, String contactPhone, long districtCode,
			String contactAddress) {
		Date date = new Date();
		FallibleQuestionPrintOrder order = new FallibleQuestionPrintOrder();
		order.setAttachData(attachData);
		order.setCode(goodsOrderService.generateCode()); // 订单编号
		order.setContactAddress(contactAddress);
		order.setContactName(contactName);
		order.setContactPhone(contactPhone);
		order.setDistrictCode(districtCode);
		order.setOrderAt(date);
		order.setPaymentPlatformCode(paymentPlatformCode);
		order.setPayMod(payMod);
		order.setTotalPrice(totalPrice);
		order.setUpdateAt(date);
		order.setUpdateId(userID);
		order.setUserId(userID);
		order.setMemberType(SecurityContext.getMemberType());
		repo.save(order);

		// 订单快照
		FallibleQuestionPrintOrderSnapshot snapshot = this.createOrderSnapshot(order, date, userID);
		order.setFallibleQuestionPrintOrderSnapshotId(snapshot.getId());
		repo.save(order);

		return order;
	}

	@Override
	@Transactional
	public FallibleQuestionPrintOrderSnapshot createOrderSnapshot(FallibleQuestionPrintOrder order, Date date,
			long userID) {
		FallibleQuestionPrintOrderSnapshot snapshot = new FallibleQuestionPrintOrderSnapshot();
		snapshot.setAttachData(order.getAttachData());
		snapshot.setCode(order.getCode());
		snapshot.setContactAddress(order.getContactAddress());
		snapshot.setContactName(order.getContactName());
		snapshot.setContactPhone(order.getContactPhone());
		snapshot.setDelStatus(order.getDelStatus());
		snapshot.setDistrictCode(order.getDistrictCode());
		snapshot.setExpress(order.getExpress());
		snapshot.setExpressCode(order.getExpressCode());
		snapshot.setFallibleQuestionPrintOrderId(order.getId());
		snapshot.setOrderAt(order.getOrderAt());
		snapshot.setPaymentCode(order.getPaymentCode());
		snapshot.setPaymentPlatformCode(order.getPaymentPlatformCode());
		snapshot.setPaymentPlatformOrderCode(order.getPaymentPlatformOrderCode());
		snapshot.setPayMod(order.getPayMod());
		snapshot.setPayTime(order.getPayTime());
		snapshot.setSellerNotes(order.getSellerNotes());
		snapshot.setStatus(order.getStatus());
		snapshot.setTotalPrice(order.getTotalPrice());
		snapshot.setUpdateAt(order.getUpdateAt());
		snapshot.setUpdateId(order.getUpdateId());
		snapshot.setUserId(order.getUserId());
		snapshot.setMemberType(order.getMemberType());
		snapshotRepo.save(snapshot);
		return snapshot;
	}

	@Override
	public UnifiedPayResult getWXQRCodeImage(String appid, long orderID, OrderPayBusinessSpace space,
			HttpServletRequest request, HttpServletResponse response) {

		// 查询订单
		FallibleQuestionPrintOrder order = repo.get(orderID);

		String attach = "";
		String businessID = String.valueOf(orderID);
		String spbill_create_ip = WebUtils.getRealIP(request); // 用户客户端IP地址
		int total_fee = (int) (order.getTotalPrice().doubleValue() * 100); // 订单金额分值
		try {
			return wxPaymentService.unifiedPayOrder(appid, space, "错题本代打印", attach, businessID, spbill_create_ip,
					total_fee, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void jumpToAlipay(long orderID, OrderPayBusinessSpace space, HttpServletRequest request,
			HttpServletResponse response) {

		// 查询订单
		FallibleQuestionPrintOrder order = repo.get(orderID);
		String totalPrice = String.format("%.2f", order.getTotalPrice().doubleValue());

		DirectPayData directPayData = new DirectPayData();
		directPayData.setExtra_common_param("");
		directPayData.setGoods_type("0");
		directPayData.setOut_trade_no(String.valueOf(orderID));
		directPayData.setPrice(totalPrice);
		directPayData.setQuantity(1);
		directPayData.setSubject("错题本代打印");
		directPayData.setTotal_fee(totalPrice);

		try {
			alipayService.jumpToAlipay(space, directPayData, request, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	@Transactional
	public FallibleQuestionPrintOrder updatePaymentCallback(long orderID, String paymentPlatformOrderCode,
			String paymentCode, Date payTime) {
		FallibleQuestionPrintOrder order = repo.get(orderID);
		if (order.getPayTime() == null) {
			Date date = new Date();
			order.setPaymentPlatformOrderCode(paymentPlatformOrderCode);
			order.setPaymentCode(paymentCode);
			order.setPayTime(payTime);
			order.setStatus(FallibleQuestionPrintOrderStatus.PAY);
			order.setUpdateAt(date);

			FallibleQuestionPrintOrderSnapshot snapshot = this.createOrderSnapshot(order, date, order.getUserId());
			order.setFallibleQuestionPrintOrderSnapshotId(snapshot.getId());
			repo.save(order);
		}
		return order;
	}

	@Override
	@Transactional
	public FallibleQuestionPrintOrder updateOrderStatus(long orderID, Long updateID,
			FallibleQuestionPrintOrderStatus status) {
		FallibleQuestionPrintOrder order = repo.get(orderID);
		if (order.getStatus() != status) {
			Date date = new Date();
			order.setStatus(status);
			order.setUpdateId(updateID);
			order.setUpdateAt(date);

			FallibleQuestionPrintOrderSnapshot snapshot = this.createOrderSnapshot(order, date, order.getUserId());
			order.setFallibleQuestionPrintOrderSnapshotId(snapshot.getId());
			repo.save(order);
		}
		return order;
	}

	@Override
	public FallibleQuestionPrintOrder get(long orderID) {
		return repo.get(orderID);
	}

	@Override
	public FallibleQuestionPrintOrder getLast(long userID) {
		return repo.find("$zyGetLast", Params.param("userId", userID)).get();
	}

	@Override
	public Map<Long, FallibleQuestionPrintOrder> mget(Collection<Long> ids) {
		return repo.mget(ids);
	}
}

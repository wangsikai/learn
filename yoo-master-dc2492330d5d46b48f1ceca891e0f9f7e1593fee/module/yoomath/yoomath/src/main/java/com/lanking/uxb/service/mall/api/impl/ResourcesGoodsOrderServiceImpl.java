package com.lanking.uxb.service.mall.api.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.goods.Goods;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoods;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsType;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.order.GoodsOrderStatus;
import com.lanking.cloud.domain.yoo.order.PayMode;
import com.lanking.cloud.domain.yoo.order.resources.ResourcesGoodsOrder;
import com.lanking.cloud.domain.yoo.order.resources.ResourcesGoodsOrderSnapshot;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.honor.api.CoinsService;
import com.lanking.uxb.service.mall.api.GoodsOrderService;
import com.lanking.uxb.service.mall.api.GoodsService;
import com.lanking.uxb.service.mall.api.ResourcesGoodsOrderService;
import com.lanking.uxb.service.mall.api.ResourcesGoodsService;

@Transactional(readOnly = true)
@Service
public class ResourcesGoodsOrderServiceImpl implements ResourcesGoodsOrderService {

	@Autowired
	@Qualifier("ResourcesGoodsOrderRepo")
	private Repo<ResourcesGoodsOrder, Long> repo;
	@Autowired
	@Qualifier("ResourcesGoodsOrderSnapshotRepo")
	private Repo<ResourcesGoodsOrderSnapshot, Long> snapshotRepo;

	@Autowired
	private ResourcesGoodsService resourcesGoodsService;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private GoodsOrderService goodsOrderService;
	@Autowired
	private CoinsService coinsService;

	@Override
	public ResourcesGoodsOrder get(long id) {
		return repo.get(id);
	}

	@Override
	public Map<Long, ResourcesGoodsOrder> mget(Collection<Long> ids) {
		return repo.mget(ids);
	}

	@Override
	public Page<ResourcesGoodsOrder> getOrdersByUser(Long userId, int type, Pageable pageable) {
		// 订单用户
		Params params = Params.param("userId", userId);
		// 订单类型
		params.put("type", type);
		// 订单状态
		params.put("status", GoodsOrderStatus.COMPLETE.getValue());
		// 删除状态
		params.put("delStatus", Status.ENABLED.getValue());
		return repo.find("$getResourcesGoodsOrders", params).fetch(pageable);
	}

	@Override
	public List<ResourcesGoodsOrder> findCompleteOrderByUserAndGoods(long userID, long goodsID) {
		return repo.find("$findCompleteOrderByUserAndGoods", Params.param("userID", userID).put("goodsID", goodsID))
				.list();
	}

	@Override
	@Transactional
	public ResourcesGoodsOrder createOrder(long userID, long goodsID, PayMode payMod, Integer paymentPlatformCode,
			String attachData) {
		ResourcesGoods resourcesGoods = resourcesGoodsService.get(goodsID);
		Goods goods = goodsService.get(goodsID);

		// 生成资源商品订单
		Date date = new Date();
		ResourcesGoodsOrder resourcesGoodsOrder = new ResourcesGoodsOrder();
		resourcesGoodsOrder.setAmount(1);
		resourcesGoodsOrder.setCode(goodsOrderService.generateCode()); // 订单编号
		resourcesGoodsOrder.setResourcesGoodsId(goodsID);
		resourcesGoodsOrder.setResourcesGoodsSnapshotId(resourcesGoods.getResourcesGoodsSnapshotId());
		resourcesGoodsOrder.setGoodsId(goodsID);
		resourcesGoodsOrder.setGoodsSnapshotId(goods.getGoodsSnapshotId());
		resourcesGoodsOrder.setOrderAt(date);
		resourcesGoodsOrder.setPayMod(payMod);
		resourcesGoodsOrder.setTotalPrice(payMod == PayMode.COINS ? goods.getPrice() : goods.getPriceRMB());
		if (payMod == PayMode.COINS
				|| (payMod == PayMode.ONLINE && resourcesGoodsOrder.getTotalPrice().doubleValue() == 0)) {
			// 金币支付，在线免费支付，订单直接完成
			resourcesGoodsOrder.setStatus(GoodsOrderStatus.COMPLETE);
			resourcesGoodsOrder.setPayTime(date);
		} else {
			resourcesGoodsOrder.setStatus(GoodsOrderStatus.NOT_PAY);
		}
		resourcesGoodsOrder.setUserId(userID);
		resourcesGoodsOrder.setDelStatus(Status.ENABLED);
		resourcesGoodsOrder.setPaymentPlatformCode(paymentPlatformCode); // 支付平台
		resourcesGoodsOrder.setType(ResourcesGoodsType.EXAM_PAPER); // 资源商品类型
		resourcesGoodsOrder.setUpdateAt(date);
		resourcesGoodsOrder.setUpdateId(userID);
		resourcesGoodsOrder.setAttachData(attachData);
		repo.save(resourcesGoodsOrder);

		// 订单快照
		ResourcesGoodsOrderSnapshot snapshot = createOrderSnapshot(resourcesGoodsOrder);
		resourcesGoodsOrder.setResourcesGoodsOrderSnapshotId(snapshot.getId());
		repo.save(resourcesGoodsOrder);

		// 若金币支付订单，直接扣除金币
		if (payMod == PayMode.COINS) {
			coinsService.earn(CoinsAction.BUY_COINS_GOODS, userID, -goods.getPrice().intValue(), Biz.NULL, 0);
		}

		return resourcesGoodsOrder;
	}

	@Override
	@Transactional
	public ResourcesGoodsOrderSnapshot createOrderSnapshot(ResourcesGoodsOrder resourcesGoodsOrder) {
		Date date = new Date();
		ResourcesGoodsOrderSnapshot snapshot = new ResourcesGoodsOrderSnapshot();
		snapshot.setResourcesGoodsOrderId(resourcesGoodsOrder.getId());
		snapshot.setAmount(resourcesGoodsOrder.getAmount());
		snapshot.setCode(resourcesGoodsOrder.getCode()); // 订单编号
		snapshot.setResourcesGoodsId(resourcesGoodsOrder.getResourcesGoodsId());
		snapshot.setResourcesGoodsSnapshotId(resourcesGoodsOrder.getResourcesGoodsSnapshotId());
		snapshot.setGoodsId(resourcesGoodsOrder.getGoodsId());
		snapshot.setGoodsSnapshotId(resourcesGoodsOrder.getGoodsSnapshotId());
		snapshot.setOrderAt(date);
		snapshot.setPayMod(resourcesGoodsOrder.getPayMod());
		snapshot.setTotalPrice(resourcesGoodsOrder.getTotalPrice());
		snapshot.setStatus(resourcesGoodsOrder.getStatus());
		snapshot.setUserId(resourcesGoodsOrder.getUserId());
		snapshot.setDelStatus(resourcesGoodsOrder.getDelStatus());
		snapshot.setType(resourcesGoodsOrder.getType()); // 资源商品类型
		snapshot.setUpdateAt(date);
		snapshot.setUpdateId(resourcesGoodsOrder.getUpdateId());
		snapshot.setBuyerNotes(resourcesGoodsOrder.getBuyerNotes());
		snapshot.setSellerNotes(resourcesGoodsOrder.getSellerNotes());
		snapshot.setPaymentPlatformCode(resourcesGoodsOrder.getPaymentPlatformCode()); // 支付平台
		snapshot.setPaymentCode(resourcesGoodsOrder.getPaymentCode());
		snapshot.setPaymentPlatformOrderCode(resourcesGoodsOrder.getPaymentPlatformOrderCode());
		snapshot.setPayTime(resourcesGoodsOrder.getPayTime());
		snapshotRepo.save(snapshot);
		return snapshot;
	}

	@Override
	@Transactional
	public ResourcesGoodsOrder updatePaymentCallback(long resourcesGoodsOrderID, String paymentPlatformOrderCode,
			String paymentCode, Date payTime) {
		ResourcesGoodsOrder order = repo.get(resourcesGoodsOrderID);
		if (order.getPayTime() == null) {
			order.setPaymentPlatformOrderCode(paymentPlatformOrderCode);
			order.setPaymentCode(paymentCode);
			order.setPayTime(payTime);
			order.setStatus(GoodsOrderStatus.PAY);
			order.setUpdateAt(new Date());

			ResourcesGoodsOrderSnapshot snapshot = this.createOrderSnapshot(order);
			order.setResourcesGoodsOrderSnapshotId(snapshot.getId());
			repo.save(order);
		}
		return order;
	}

	@Override
	@Transactional
	public ResourcesGoodsOrder updateOrderStatus(long resourcesGoodsOrderID, Long updateID, GoodsOrderStatus status) {
		ResourcesGoodsOrder order = repo.get(resourcesGoodsOrderID);
		if (order.getStatus() != status) {
			order.setStatus(status);
			order.setUpdateId(updateID);
			order.setUpdateAt(new Date());

			ResourcesGoodsOrderSnapshot snapshot = this.createOrderSnapshot(order);
			order.setResourcesGoodsOrderSnapshotId(snapshot.getId());
			repo.save(order);
		}
		return order;
	}

	@Transactional
	@Override
	public Value delOrders(Long createId, Long id) {
		ResourcesGoodsOrder rgo = repo.get(id);
		if (rgo.getUserId() == createId.longValue()) {
			rgo.setDelStatus(Status.DELETED);
			repo.save(rgo);
		}
		return new Value();
	}

}

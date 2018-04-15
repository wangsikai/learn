package com.lanking.uxb.zycon.mall.api.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.base.message.api.EmailPacket;
import com.lanking.cloud.domain.base.message.api.SmsPacket;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.goods.Goods;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsDaySellCount;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.order.GoodsOrderStatus;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrder;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrderSnapshot;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrderSource;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.service.message.api.MessageSender;
import com.lanking.uxb.zycon.mall.api.ZycCoinsGoodsOrderService;
import com.lanking.uxb.zycon.mall.api.ZycLotteryRecordQuery;
import com.lanking.uxb.zycon.mall.form.OrderForm;
import com.lanking.uxb.zycon.operation.api.ZycAccountService;

@Service
@Transactional(readOnly = true)
public class ZycCoinsGoodsOrderServiceImpl implements ZycCoinsGoodsOrderService {

	@Autowired
	@Qualifier("CoinsGoodsOrderRepo")
	private Repo<CoinsGoodsOrder, Long> orderRepo;

	@Autowired
	@Qualifier("GoodsRepo")
	private Repo<Goods, Long> repo;

	@Autowired
	@Qualifier("CoinsGoodsOrderSnapshotRepo")
	private Repo<CoinsGoodsOrderSnapshot, Long> snapshotRepo;

	@Autowired
	@Qualifier("CoinsGoodsDaySellCountRepo")
	private Repo<CoinsGoodsDaySellCount, Long> sellCountRepo;
	@Autowired
	private MessageSender messageSender;
	@Autowired
	private MqSender mqSender;
	@Autowired
	private ZycAccountService zycAccountService;

	@Override
	public Page<CoinsGoodsOrder> queryOrderList(OrderForm form, Pageable p) {
		Params params = Params.param();
		if (form.getAccountName() != null) {
			params.put("accountName", "%" + form.getAccountName() + "%");
		}
		if (form.getStatus() != null) {
			params.put("status", form.getStatus().getValue());
		}
		if (form.getStartTime() != null) {
			params.put("startTime", form.getStartTime());
		}
		if (form.getEndTime() != null) {
			params.put("endTime", form.getEndTime());
		}
		if (form.getOrderType() != null) {
			params.put("orderType", form.getOrderType());
		}
		if (form.getUserType() != null) {
			params.put("userType", form.getUserType().getValue());
		}
		if (form.getSource() != null) {
			params.put("source", form.getSource().getValue());
		}
		return orderRepo.find("$queryOrderList", params).fetch(p);
	}

	@Transactional
	@Override
	public void updateStatus(Long orderId, GoodsOrderStatus status, String sellerNotes, Long userId) {
		CoinsGoodsOrder order = orderRepo.get(orderId);
		// 状态是否修改,flag等于true表示没有修改
		Boolean flag = order.getStatus() == status;
		order.setStatus(status);
		order.setSellerNotes(sellerNotes);
		order.setUpdateAt(new Date());
		order.setUpdateId(userId);
		orderRepo.save(order);
		// 创建快照
		CoinsGoodsOrderSnapshot snapshot = createOrderSnapshot(order);
		order.setCoinsGoodsOrderSnapshotId(snapshot.getId());
		orderRepo.save(order);
		Params params = Params.param();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date0 = sdf.format(order.getOrderAt());
		params.put("goodsId", order.getGoodsId());
		params.put("date0", date0);
		// 兑换用户的id
		Long orderUserId = order.getUserId();
		Account account = zycAccountService.getAccountByUserId(orderUserId);
		String noticeObject = StringUtils.isBlank(account.getMobile()) ? account.getEmail() : account.getMobile();
		// 如果同于之前的，则不操作
		if (!flag) {
			// 取消的时候，操作CoinsGoods--DaySellCount -1 若是抽奖失败也不退回金币
			if (status == GoodsOrderStatus.FAIL && order.getSource() == CoinsGoodsOrderSource.EXCHANGE) {
				params.put("count1", -1);
				// 通过时间和商品id更新每天的数量
				sellCountRepo.execute("$updateDaySellCount", params);
				// 发送Mq消息通知
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("action", CoinsAction.BUY_COINS_GOODS_FAIL);
				jsonObject.put("userId", order.getUserId());
				jsonObject.put("biz", Biz.COINS_GOODS_ORDER);
				jsonObject.put("bizId", order.getId());
				jsonObject.put("coinsValue", order.getTotalPrice().intValue());
				mqSender.send(MqHonorRegistryConstants.EX_COINS, MqHonorRegistryConstants.RK_COINS_LOG,
						MQ.builder().data(jsonObject).build());
			}

		}
		// 已兑换,根据用户情况发送短信或者邮件
		if (status == GoodsOrderStatus.COMPLETE && !StringUtils.isBlank(noticeObject)) {
			Goods goods = repo.get(order.getGoodsId());
			if (noticeObject.indexOf("@") != -1) {
				// 说明兑换号码是邮箱号,发邮件
				messageSender
						.send(new EmailPacket(noticeObject, 11000003, ValueMap.value("goodsname", goods.getName())));
			} else {
				// 说明兑换号码是手机号,发短信
				messageSender.send(new SmsPacket(noticeObject, 10000014, ValueMap.value("goodsName", goods.getName())));
			}
		}
	}

	@Transactional
	@Override
	public CoinsGoodsOrderSnapshot createOrderSnapshot(CoinsGoodsOrder order) {
		CoinsGoodsOrderSnapshot snapshot = new CoinsGoodsOrderSnapshot();
		snapshot.setAmount(order.getAmount());
		snapshot.setBuyerNotes(order.getBuyerNotes());
		snapshot.setCode(order.getCode());
		snapshot.setCoinsGoodsOrderId(order.getId());
		snapshot.setGoodsId(order.getGoodsId());
		snapshot.setGoodsSnapshotId(order.getGoodsSnapshotId());
		snapshot.setCoinsGoodsId(order.getCoinsGoodsId());
		snapshot.setCoinsGoodsSnapshotId(order.getCoinsGoodsSnapshotId());
		snapshot.setOrderAt(order.getOrderAt());
		snapshot.setP0(order.getP0());
		snapshot.setSellerNotes(order.getSellerNotes());
		snapshot.setStatus(order.getStatus());
		snapshot.setTotalPrice(order.getTotalPrice());
		snapshot.setUpdateAt(order.getUpdateAt());
		snapshot.setUpdateId(order.getUpdateId());
		snapshot.setUserId(order.getUserId());
		snapshot.setDelStatus(order.getDelStatus());
		snapshot.setSource(order.getSource());
		snapshot.setPaymentCode(order.getPaymentCode());
		snapshot.setPaymentPlatformCode(order.getPaymentPlatformCode());
		snapshot.setPaymentPlatformOrderCode(order.getPaymentPlatformOrderCode());
		snapshot.setPayMod(order.getPayMod());
		snapshot.setPayTime(order.getPayTime());
		return snapshotRepo.save(snapshot);
	}

	@Override
	public List<Map> statisticTypeData(String beginDate, String endDate, Integer type) {
		Params params = Params.param();
		if (StringUtils.isNotBlank(beginDate)) {
			params.put("beginDate", beginDate);
		}
		if (StringUtils.isNotBlank(endDate)) {
			params.put("endDate", endDate);
		}
		if (type != null) {
			if (type != -1) {
				params.put("virtualType", type);
			} else {
				params.put("notVirtualType", true);
			}
		}
		return orderRepo.find("$zycStatisticByType", params).list(Map.class);
	}

	@Override
	public Page<Map> statisticByName(String beginDate, String endDate, Integer type, Pageable pageable) {
		Params params = Params.param();
		if (StringUtils.isNotBlank(beginDate)) {
			params.put("beginDate", beginDate);
		}
		if (StringUtils.isNotBlank(endDate)) {
			params.put("endDate", endDate);
		}
		if (type != null) {
			if (type != -1) {
				params.put("virtualType", type);
			} else if (type != null) {
				params.put("notVirtualType", true);
			}
		}

		return orderRepo.find("$zycStatisticByName", params).fetch(pageable, Map.class);
	}

	@Override
	public CoinsGoodsOrder get(Long id) {
		return orderRepo.get(id);
	}

	@Override
	public Long tobeExchageCount() {
		return orderRepo.find("$tobeExchageCount").get(Long.class);
	}

	@Override
	public List<Map> queryLotteryOrderStatis(ZycLotteryRecordQuery query) {
		Params params = Params.param();
		if (query.getCode() != null) {
			params.put("code", query.getCode());
		}
		if (query.getType() != null) {
			params.put("type", query.getType().getValue());
		}
		// 目前定的期别Id和时间范围只能同时查询一个
		if (query.getSeasonId() != null) {
			params.put("seasonId", query.getSeasonId());
		} else {
			if (query.getStartAt() != null) {
				params.put("startAt", query.getStartAt());
			}
			if (query.getEndAt() != null) {
				params.put("endAt", query.getEndAt());
			}
		}
		return orderRepo.find("$queryLotteryOrderStatis", params).list(Map.class);
	}

	@Override
	public Page<Map> lotteryUserRecordList(ZycLotteryRecordQuery query, Pageable pageable) {
		Params params = Params.param();
		if (query.getCode() != null) {
			params.put("code", query.getCode());
		}
		if (query.getType() != null) {
			params.put("type", query.getType().getValue());
		}
		// 目前定的期别Id和时间范围只能同时查询一个
		if (query.getSeasonId() != null) {
			params.put("seasonId", query.getSeasonId());
		} else {
			if (query.getStartAt() != null) {
				params.put("startAt", query.getStartAt());
			}
			if (query.getEndAt() != null) {
				params.put("endAt", query.getEndAt());
			}
		}
		if (query.getAccountName() != null) {
			params.put("accountName", "%" + query.getAccountName() + "%");
		}
		if (query.getJustLookWin()) {
			params.put("justLookWin", 1);
		}
		if (query.getUserType() != null) {
			params.put("userType", query.getUserType().getValue());
		}
		return orderRepo.find("$lotteryUserRecordList", params).fetch(pageable, Map.class);
	}

	@Override
	public Map lotteryTotalStatis(ZycLotteryRecordQuery query) {
		Params params = Params.param();
		if (query.getCode() != null) {
			params.put("code", query.getCode());
		}
		if (query.getType() != null) {
			params.put("type", query.getType().getValue());
		}
		// 目前定的期别Id和时间范围只能同时查询一个
		if (query.getSeasonId() != null) {
			params.put("seasonId", query.getSeasonId());
		} else {
			if (query.getStartAt() != null) {
				params.put("startAt", query.getStartAt());
			}
			if (query.getEndAt() != null) {
				params.put("endAt", query.getEndAt());
			}
		}
		return orderRepo.find("$lotteryTotalStatis", params).get(Map.class);
	}
}

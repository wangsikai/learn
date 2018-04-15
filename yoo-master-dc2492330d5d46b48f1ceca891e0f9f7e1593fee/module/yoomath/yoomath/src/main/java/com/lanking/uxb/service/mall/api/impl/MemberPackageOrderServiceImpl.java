package com.lanking.uxb.service.mall.api.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.domain.yoo.member.MemberPackage;
import com.lanking.cloud.domain.yoo.member.MemberPackageCard;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.order.PayMode;
import com.lanking.cloud.domain.yoo.order.ThirdPaymentMethod;
import com.lanking.cloud.domain.yoo.order.VirtualCardType;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrder;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderSnapshot;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderSource;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderStatus;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderType;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserAction;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.mall.api.GoodsOrderService;
import com.lanking.uxb.service.mall.api.MemberPackageCardService;
import com.lanking.uxb.service.mall.api.MemberPackageOrderService;
import com.lanking.uxb.service.mall.api.MemberPackageService;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.UserActionService;
import com.lanking.uxb.service.user.api.UserMemberService;

@Service
@Transactional(readOnly = true)
public class MemberPackageOrderServiceImpl implements MemberPackageOrderService {
	@Autowired
	@Qualifier("MemberPackageOrderRepo")
	private Repo<MemberPackageOrder, Long> repo;
	@Autowired
	@Qualifier("MemberPackageOrderSnapshotRepo")
	private Repo<MemberPackageOrderSnapshot, Long> snapshotRepo;

	@Autowired
	private MemberPackageService memberPackageService;
	@Autowired
	private GoodsOrderService goodsOrderService;
	@Qualifier("accountService")
	@Autowired
	private AccountService accountService;
	@Autowired
	private UserMemberService userMemberService;
	@Autowired
	private MemberPackageCardService memberPackageCardService;
	@Autowired
	private UserActionService userActionService;

	@Override
	@Transactional
	public MemberPackageOrder createOrder(long userID, Long memberPackageID, MemberType memberType, PayMode payMode,
			Integer paymentPlatformCode, ThirdPaymentMethod thirdPaymentMethod, String attachData,
			MemberPackageCard card) {
		User user = accountService.getUserByUserId(userID);
		MemberPackageOrder order = new MemberPackageOrder();

		if (memberPackageID != null && card == null) {
			MemberPackage memberPackage = memberPackageService.get(memberPackageID);
			order.setMemberPackageId(memberPackage.getId());
			order.setTotalPrice(memberPackage.getPresentPrice());
			order.setMemberPackageGroupId(memberPackage.getMemberPackageGroupId()); // 创建订单时加入套餐组Id(学生有这个概念)
		}

		Date date = new Date();

		order.setAmount(1);
		order.setCode(goodsOrderService.generateCode()); // 订单编号
		order.setMemberType(memberType);
		order.setOrderAt(date);
		order.setPaymentPlatformCode(paymentPlatformCode);
		order.setThirdPaymentMethod(thirdPaymentMethod);
		order.setPayMod(payMode);
		order.setSource(user.getUserChannelCode() == UserChannel.YOOMATH ? MemberPackageOrderSource.USER
				: MemberPackageOrderSource.CHANNEL);
		order.setType(MemberPackageOrderType.USER);
		order.setUpdateAt(date);
		order.setUpdateId(userID);
		order.setUserId(userID);
		order.setDelStatus(Status.ENABLED);
		order.setAttachData(attachData);
		order.setUserChannelCode(user.getUserChannelCode());

		// 会员卡支付
		if (card != null) {
			order.setVirtualCardCode(card.getCode());
			order.setVirtualCardType(VirtualCardType.MEMBER_PACKAGE);
			order.setStatus(MemberPackageOrderStatus.COMPLETE);
			order.setAmount(1);
			order.setPayTime(date);
			order.setTotalPrice(card.getPrice());
		}
		repo.save(order);

		// 更新会员卡信息
		if (card != null) {
			memberPackageCardService.used(card.getCode(), userID, order.getId());

			// 学生用户会员卡激活动作行为
			if (user.getUserType() == UserType.STUDENT) {
				userActionService.asyncAction(UserAction.OPEN_VIP, order.getUserId(), null);
			}
		}

		// 订单快照
		MemberPackageOrderSnapshot snapshot = this.createOrderSnapshot(order, date, userID);
		order.setMemberPackageOrderSnapshotId(snapshot.getId());
		repo.save(order);

		// 处理会员数据
		if (card != null) {
			userMemberService.createOrRenew(order.getUserId(), date, null, order.getId(), card);
		}

		return order;
	}

	/**
	 * 创建订单快照.
	 * 
	 * @param order
	 *            订单
	 * @return
	 */
	@Override
	@Transactional
	public MemberPackageOrderSnapshot createOrderSnapshot(MemberPackageOrder order, Date updateAt, Long updateID) {
		MemberPackageOrderSnapshot snapshot = new MemberPackageOrderSnapshot();
		snapshot.setAmount(order.getAmount());
		snapshot.setBuyerNotes(order.getBuyerNotes());
		snapshot.setCode(order.getCode());
		snapshot.setDelStatus(order.getDelStatus());
		snapshot.setMemberPackageId(order.getMemberPackageId());
		snapshot.setMemberPackageOrderId(order.getId());
		snapshot.setMemberType(order.getMemberType());
		snapshot.setOrderAt(order.getOrderAt());
		snapshot.setPayMod(order.getPayMod());
		snapshot.setPaymentPlatformCode(order.getPaymentPlatformCode());
		snapshot.setPaymentCode(order.getPaymentCode());
		snapshot.setPaymentPlatformOrderCode(order.getPaymentPlatformOrderCode());
		snapshot.setThirdPaymentMethod(order.getThirdPaymentMethod());
		snapshot.setPayTime(order.getPayTime());
		snapshot.setSellerNotes(order.getSellerNotes());
		snapshot.setSource(order.getSource());
		snapshot.setType(order.getType());
		snapshot.setTotalPrice(order.getTotalPrice());
		snapshot.setUpdateAt(updateAt);
		snapshot.setUpdateId(updateID);
		snapshot.setUserId(order.getUserId());
		snapshot.setVirtualCardType(order.getVirtualCardType());
		snapshot.setVirtualCardCode(order.getVirtualCardCode());
		snapshot.setStatus(order.getStatus());
		snapshot.setUserChannelCode(order.getUserChannelCode());
		snapshot.setMemberPackageGroupId(order.getMemberPackageGroupId());
		snapshotRepo.save(snapshot);
		return snapshot;
	}

	@Override
	public MemberPackageOrder get(long orderID) {
		return repo.get(orderID);
	}

	@Override
	public MemberPackageOrderSnapshot getSnapshot(long snapshotOrderID) {
		return snapshotRepo.get(snapshotOrderID);
	}

	@Override
	@Transactional
	public MemberPackageOrder updateOrderStatus(long memberPackageOrderID, Long updateID,
			MemberPackageOrderStatus status) {
		MemberPackageOrder order = repo.get(memberPackageOrderID);
		if (order.getStatus() != status) {
			Date date = new Date();
			order.setStatus(status);
			order.setUpdateId(updateID);
			order.setUpdateAt(date);

			// 订单快照
			MemberPackageOrderSnapshot snapshot = this.createOrderSnapshot(order, date, updateID);
			order.setMemberPackageOrderSnapshotId(snapshot.getId());
			repo.save(order);
		}
		return order;
	}

	@Override
	@Transactional
	public MemberPackageOrder updatePaymentCallback(long memberPackageOrderID, String paymentPlatformOrderCode,
			String paymentCode, Date payTime) {
		MemberPackageOrder order = repo.get(memberPackageOrderID);
		if (order.getPayTime() == null) {
			Date date = new Date();
			order.setPaymentPlatformOrderCode(paymentPlatformOrderCode);
			order.setPaymentCode(paymentCode);
			order.setPayTime(payTime);
			order.setStatus(MemberPackageOrderStatus.PAY);
			order.setUpdateAt(date);

			// 订单快照
			MemberPackageOrderSnapshot snapshot = this.createOrderSnapshot(order, date, null);
			order.setMemberPackageOrderSnapshotId(snapshot.getId());
			repo.save(order);
		}
		return order;
	}

	@Override
	@Transactional
	public void deleteOrder(long memberPackageOrderID) {
		MemberPackageOrder order = repo.get(memberPackageOrderID);
		if (order != null) {
			long snapshotId = order.getMemberPackageOrderSnapshotId();
			MemberPackageOrderSnapshot snapshot = snapshotRepo.get(snapshotId);
			if (snapshot != null) {
				snapshotRepo.delete(snapshot);
			}
			repo.delete(order);
		}
	}

	@Override
	public MemberPackageOrder getWXLastNotpayOrder(long userId, long memberPackageID) {
		return repo
				.find("$getWXLastNotpayOrder", Params.param("userId", userId).put("memberPackageID", memberPackageID))
				.get();
	}

	@Override
	@Transactional
	public MemberPackageOrder updatePayOrderInfos(long orderId, int paymentPlatformCode,
			ThirdPaymentMethod thirdPaymentMethod) {
		MemberPackageOrder order = repo.get(orderId);
		if (order != null) {
			Date date = new Date();
			order.setPaymentPlatformCode(paymentPlatformCode);
			if (null != thirdPaymentMethod) {
				order.setThirdPaymentMethod(thirdPaymentMethod);
			}
			order.setUpdateAt(date);

			// 订单快照
			MemberPackageOrderSnapshot snapshot = this.createOrderSnapshot(order, date, null);
			order.setMemberPackageOrderSnapshotId(snapshot.getId());
			repo.save(order);
		}
		return order;
	}

	@Override
	@Transactional
	public MemberPackageOrder refreshOrder(long orderId) {
		MemberPackageOrder order = repo.get(orderId);
		if (order != null && order.getStatus() == MemberPackageOrderStatus.NOT_PAY) {
			Date date = new Date();
			order.setOrderAt(date);
			order.setUpdateAt(date);
			MemberPackageOrderSnapshot snapshot = snapshotRepo.get(order.getMemberPackageOrderSnapshotId());
			snapshot.setOrderAt(date);
			snapshot.setUpdateAt(date);
			repo.save(order);
			snapshotRepo.save(snapshot);
		}
		return order;
	}
}

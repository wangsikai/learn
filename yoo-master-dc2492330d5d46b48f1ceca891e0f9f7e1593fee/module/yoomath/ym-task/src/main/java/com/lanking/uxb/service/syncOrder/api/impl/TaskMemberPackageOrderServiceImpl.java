/**
 * 
 */
package com.lanking.uxb.service.syncOrder.api.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrder;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderSnapshot;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderStatus;
import com.lanking.cloud.sdk.data.CursorGetter;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.syncOrder.api.TaskMemberPackageOrderService;

/**
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 *
 */
@Service
@Transactional(readOnly = true)
public class TaskMemberPackageOrderServiceImpl implements TaskMemberPackageOrderService {

	@Autowired
	@Qualifier("MemberPackageOrderRepo")
	private Repo<MemberPackageOrder, Long> repo;

	@Autowired
	@Qualifier("MemberPackageOrderSnapshotRepo")
	private Repo<MemberPackageOrderSnapshot, Long> snapshotRepo;

	@Override
	public CursorPage<Long, MemberPackageOrder> findMemberPackageOrderByNotPay(CursorPageable<Long> cursorPageable,
			Date nowTime) {
		Params params = Params.param();
		if (nowTime == null) {
			return null;
		}
		params.put("nowTime", nowTime);
		return repo.find("$findMemberPackageOrderByNotPay", params).fetch(cursorPageable, MemberPackageOrder.class,
				new CursorGetter<Long, MemberPackageOrder>() {
					@Override
					public Long getCursor(MemberPackageOrder bean) {
						return Long.valueOf(bean.getId());
					}
				});
	}

	@Transactional
	@Override
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

	/**
	 * 创建订单快照.
	 * 
	 * @param order
	 *            订单
	 * @return
	 */
	@Transactional
	@Override
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

	@Transactional
	@Override
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
	public MemberPackageOrder get(long orderID) {
		return repo.get(orderID);
	}

}

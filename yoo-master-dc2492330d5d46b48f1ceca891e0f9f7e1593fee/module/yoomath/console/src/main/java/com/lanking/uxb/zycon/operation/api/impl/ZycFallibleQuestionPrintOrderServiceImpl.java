package com.lanking.uxb.zycon.operation.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.order.fallible.FallibleQuestionPrintOrder;
import com.lanking.cloud.domain.yoo.order.fallible.FallibleQuestionPrintOrderSnapshot;
import com.lanking.cloud.domain.yoo.order.fallible.FallibleQuestionPrintOrderStatus;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.operation.api.ZycFallPrintQuery;
import com.lanking.uxb.zycon.operation.api.ZycFallibleQuestionPrintOrderService;
import com.lanking.uxb.zycon.operation.form.ZycFallPrintForm;

@Transactional(readOnly = true)
@Service
public class ZycFallibleQuestionPrintOrderServiceImpl implements ZycFallibleQuestionPrintOrderService {

	@Autowired
	@Qualifier("FallibleQuestionPrintOrderRepo")
	private Repo<FallibleQuestionPrintOrder, Long> orderRepo;

	@Autowired
	@Qualifier("FallibleQuestionPrintOrderSnapshotRepo")
	private Repo<FallibleQuestionPrintOrderSnapshot, Long> shotRepo;

	@Override
	public Page<FallibleQuestionPrintOrder> queryPrintList(ZycFallPrintQuery query, Pageable p) {
		Params params = Params.param();
		if (query.getStartAt() != null) {
			params.put("startAt", query.getStartAt());
		}
		if (query.getEndAt() != null) {
			params.put("endAt", query.getEndAt());
		}
		if (query.getStatus() != null) {
			params.put("status", query.getStatus().getValue());
		}
		if (query.getAccountName() != null) {
			params.put("accountName", "%" + query.getAccountName() + "%");
		}
		return orderRepo.find("$queryPrintList", params).fetch(p);
	}

	@Transactional
	@Override
	public void savePrint(ZycFallPrintForm form) {
		// 修改
		FallibleQuestionPrintOrder order = orderRepo.get(form.getOrderId());
		if (form.getStatus() != null) {
			order.setStatus(form.getStatus());
		}
		if (form.getExpress() != null) {
			order.setExpress(form.getExpress());
		}
		if (form.getExpressCode() != null) {
			order.setExpressCode(form.getExpressCode());
		}
		FallibleQuestionPrintOrderSnapshot shot = this.createShot(order);
		order.setFallibleQuestionPrintOrderSnapshotId(shot.getId());
		orderRepo.save(order);
	}

	/**
	 * 产生快照
	 * 
	 * @param order
	 * @return
	 */
	@Transactional
	public FallibleQuestionPrintOrderSnapshot createShot(FallibleQuestionPrintOrder order) {
		FallibleQuestionPrintOrderSnapshot shot = new FallibleQuestionPrintOrderSnapshot();
		shot.setAmount(order.getAmount());
		shot.setAttachData(order.getAttachData());
		shot.setBuyerNotes(order.getBuyerNotes());
		shot.setCode(order.getCode());
		shot.setContactAddress(order.getContactAddress());
		shot.setContactName(order.getContactName());
		shot.setContactPhone(order.getContactPhone());
		shot.setDelStatus(order.getDelStatus());
		shot.setDistrictCode(order.getDistrictCode());
		shot.setExpress(order.getExpress());
		shot.setExpressCode(order.getExpressCode());
		shot.setFallibleQuestionPrintOrderId(order.getId());
		shot.setOrderAt(order.getOrderAt());
		shot.setPaymentCode(order.getPaymentCode());
		shot.setPaymentPlatformCode(order.getPaymentPlatformCode());
		shot.setPaymentPlatformOrderCode(order.getPaymentPlatformOrderCode());
		shot.setPayMod(order.getPayMod());
		shot.setPayTime(order.getPayTime());
		shot.setSellerNotes(order.getSellerNotes());
		shot.setStatus(order.getStatus());
		shot.setTotalPrice(order.getTotalPrice());
		shot.setUpdateAt(order.getUpdateAt());
		shot.setUpdateId(order.getUpdateId());
		shot.setUserId(order.getUserId());
		shot.setMemberType(order.getMemberType());
		shotRepo.save(shot);
		return shot;
	}

	@Override
	public Long countPrintList(FallibleQuestionPrintOrderStatus status) {
		Params params = Params.param();
		if (status != null) {
			params.put("status", status.getValue());
		}
		return orderRepo.find("$countPrintList", params).get(Long.class);
	}
}

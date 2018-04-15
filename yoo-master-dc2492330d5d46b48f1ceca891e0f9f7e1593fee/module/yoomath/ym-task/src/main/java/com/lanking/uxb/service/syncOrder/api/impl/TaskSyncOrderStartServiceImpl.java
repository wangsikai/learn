/**
 * 
 */
package com.lanking.uxb.service.syncOrder.api.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrder;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.syncOrder.api.TaskMemberPackageOrderService;
import com.lanking.uxb.service.syncOrder.api.TaskSyncOrderOpenVipService;
import com.lanking.uxb.service.syncOrder.api.TaskSyncOrderStartService;

/**
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 *
 */
@Service
public class TaskSyncOrderStartServiceImpl implements TaskSyncOrderStartService {

	private static final int ORDER_SIZE = 50;

	@Autowired
	private TaskMemberPackageOrderService memberPackageOrderService;
	@Autowired
	private TaskSyncOrderOpenVipService syncOrderOpenVipService;

	@Override
	public void initSyncOrder(Date nowTime) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(nowTime);
		cal.add(Calendar.HOUR_OF_DAY, -3);
		Date date = cal.getTime();
		CursorPageable<Long> cursorPageable = CP.cursor(Long.MIN_VALUE, ORDER_SIZE);
		CursorPage<Long, MemberPackageOrder> cursorPage = memberPackageOrderService
				.findMemberPackageOrderByNotPay(cursorPageable, date);
		while (cursorPage != null && CollectionUtils.isNotEmpty(cursorPage.getItems())) {
			List<MemberPackageOrder> orders = cursorPage.getItems();
			for (MemberPackageOrder order : orders) {
				syncOrderOpenVipService.checkPayCode(order);
			}
			Long nextCursor = cursorPage.getNextCursor();
			cursorPageable = CP.cursor(nextCursor, ORDER_SIZE);
			cursorPage = memberPackageOrderService.findMemberPackageOrderByNotPay(cursorPageable, date);
		}

	}
}

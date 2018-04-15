package com.lanking.uxb.service.syncOrder.api.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.member.MemberPackage;
import com.lanking.cloud.domain.yoo.member.MemberPackageCard;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.member.UserMember;
import com.lanking.cloud.domain.yoo.member.UserMemberHistory;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.syncOrder.api.TaskUserMemberService;

@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
@Service
public class TaskUserMemberServiceImpl implements TaskUserMemberService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier("UserMemberRepo")
	private Repo<UserMember, Long> userMemberRepo;

	@Autowired
	@Qualifier("UserMemberHistoryRepo")
	private Repo<UserMemberHistory, Long> userMemberHistoryRepo;

	@Override
	public UserMember findByUserId(long userId) {
		return userMemberRepo.find("$taskFindByUserId", Params.param("userId", userId)).get();
	}

	@Override
	@Transactional
	public void createOrRenew(long userId, Date startDate, MemberPackage memberPackage, Long orderID,
			MemberPackageCard card) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		try {
			UserMember userMember = this.findByUserId(userId);
			Date start = format.parse(format.format(startDate.getTime()));
			Calendar cal = Calendar.getInstance();

			if (userMember != null) {
				if (orderID != null && userMember.getOrderID() != null
						&& userMember.getOrderID().longValue() == orderID) {
					// 避免支付平台重复通知
					return;
				}

				Date userMemberEndAt = format.parse(format.format(userMember.getEndAt()));
				if (userMemberEndAt.compareTo(start) >= 0) {
					// 当前会员还未结束，正常续期
					cal.setTime(userMember.getEndAt());
					if (card != null) {
						cal.add(Calendar.MONTH, card.getMonth());
					} else {
						cal.add(Calendar.MONTH, memberPackage.getMonth());
					}
					userMember.setEndAt(cal.getTime());
					userMemberRepo.save(userMember);
					return;
				} else {
					// 超期重新生成

					// 转移至历史记录中
					UserMemberHistory history = new UserMemberHistory();
					history.setEndAt(userMember.getEndAt());
					history.setMemberType(userMember.getMemberType());
					history.setStartAt(userMember.getStartAt());
					history.setUserId(userMember.getUserId());
					history.setOrderID(userMember.getOrderID());
					userMemberHistoryRepo.save(history);
					userMemberRepo.execute("$taskDeleteById", Params.param("id", userMember.getId()));
				}
			}

			cal.setTime(start);
			if (card != null) {
				cal.add(Calendar.MONTH, card.getMonth());
			} else {
				cal.add(Calendar.MONTH, memberPackage.getMonth());
			}
			userMember = new UserMember();
			userMember.setEndAt(cal.getTime());
			userMember.setMemberType(MemberType.VIP);
			userMember.setStartAt(start);
			userMember.setUserId(userId);
			userMember.setOrderID(orderID);
			userMemberRepo.save(userMember);
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
	}

}

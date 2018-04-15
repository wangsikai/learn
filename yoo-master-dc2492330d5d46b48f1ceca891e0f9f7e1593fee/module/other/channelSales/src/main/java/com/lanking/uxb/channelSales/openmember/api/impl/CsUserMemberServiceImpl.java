package com.lanking.uxb.channelSales.openmember.api.impl;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.member.MemberPackage;
import com.lanking.cloud.domain.yoo.member.MemberPackageCard;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.member.UserMember;
import com.lanking.cloud.domain.yoo.member.UserMemberHistory;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.channelSales.openmember.api.CsUserMemberService;
import com.lanking.uxb.channelSales.openmember.form.UserMemberCreateForm;

/**
 * @see CsUserMemberService
 * @author xinyu.zhou
 * @since 2.5.0
 */
@Service
@Transactional(readOnly = true)
public class CsUserMemberServiceImpl implements CsUserMemberService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier("UserMemberRepo")
	private Repo<UserMember, Long> userMemberRepo;

	@Autowired
	@Qualifier("UserMemberHistoryRepo")
	private Repo<UserMemberHistory, Long> userMemberHistoryRepo;

	@Override
	public UserMember findByUser(long userId) {
		return userMemberRepo.find("$csFindByUserId", Params.param("userId", userId)).get();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<Long, UserMember> findByUsers(Collection<Long> userIds) {
		if (CollectionUtils.isEmpty(userIds)) {
			return Collections.EMPTY_MAP;
		}
		List<UserMember> userMemberList = userMemberRepo.find("$csFindByUserIds", Params.param("userIds", userIds))
				.list();

		Map<Long, UserMember> userMemberMap = new HashMap<Long, UserMember>(userMemberList.size());
		for (UserMember u : userMemberList) {
			userMemberMap.put(u.getUserId(), u);
		}
		return userMemberMap;
	}

	@Override
	@Transactional
	public void create(UserMemberCreateForm form) {
		UserMember userMember = findByUser(form.getUserId());
		if (userMember == null) {
			UserMember newUserMember = new UserMember();
			newUserMember.setEndAt(form.getEndAt());
			newUserMember.setMemberType(form.getMemberType());
			newUserMember.setStartAt(form.getStartAt());
			newUserMember.setUserId(form.getUserId());
			newUserMember.setOrderID(form.getOrderId());

			userMemberRepo.save(newUserMember);
		} else {
			if (userMember.getMemberType() == MemberType.SCHOOL_VIP && form.getMemberType() == MemberType.VIP) {
				throw new NoPermissionException();
			}
			if (userMember.getMemberType() == MemberType.VIP && form.getMemberType() == MemberType.SCHOOL_VIP) {
				archive(userMember);

				// 删除当前会员信息
				userMemberRepo.delete(userMember);
				userMemberRepo.flush();

				save(form);
			} else if ((userMember.getMemberType() == MemberType.VIP && form.getMemberType() == MemberType.VIP)
					|| (userMember.getMemberType() == MemberType.SCHOOL_VIP
							&& form.getMemberType() == MemberType.SCHOOL_VIP)) {
				// 当前会员时间已经结束，精确至天
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
				Long endDateVal = Long.valueOf(dateFormat.format(userMember.getEndAt()));
				Long nowVal = Long.valueOf(dateFormat.format(new Date()));
				if (endDateVal < nowVal) {
					archive(userMember);

					userMemberRepo.delete(userMember);
					userMemberRepo.flush();

					save(form);
				} else {
					// 当前时间未结束，则在续期原来的会员
					Long days = (form.getEndAt().getTime() - form.getStartAt().getTime()) / (1000 * 60 * 60 * 24);
					Date endAt = DateUtils.addDays(userMember.getEndAt(), days.intValue());
					userMember.setEndAt(endAt);
					userMember.setOrderID(form.getOrderId());

					userMemberRepo.save(userMember);
				}
			}
		}
	}

	/**
	 * 归档，将现有会员数据移至history表中
	 *
	 * @param userMember
	 *            {@link UserMember}
	 */
	@Transactional
	private void archive(UserMember userMember) {
		UserMemberHistory history = new UserMemberHistory();
		history.setEndAt(userMember.getEndAt());
		history.setStartAt(userMember.getStartAt());
		history.setMemberType(userMember.getMemberType());
		history.setUserId(userMember.getUserId());
		history.setOrderID(userMember.getOrderID());

		userMemberHistoryRepo.save(history);
	}

	/**
	 * 创建新的会员数据
	 *
	 * @param form
	 *            {@link UserMemberCreateForm}
	 */
	@Transactional
	private void save(UserMemberCreateForm form) {
		UserMember newUserMember = new UserMember();
		newUserMember.setEndAt(form.getEndAt());
		newUserMember.setStartAt(form.getStartAt());
		newUserMember.setMemberType(form.getMemberType());
		newUserMember.setUserId(form.getUserId());
		newUserMember.setOrderID(form.getOrderId());

		userMemberRepo.save(newUserMember);
	}

	@Override
	@Transactional
	public void createOrRenew(long userId, Date startDate, MemberPackage memberPackage, Long orderID,
			MemberPackageCard card) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		try {
			UserMember userMember = this.findByUser(userId);
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
					userMemberRepo.execute("$deleteById", Params.param("id", userMember.getId()));
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

	@Override
	@SuppressWarnings("unchecked")
	public Map<Long, Integer> countMemberCountByClasses(Collection<Long> classIds) {
		if (CollectionUtils.isEmpty(classIds)) {
			return Collections.EMPTY_MAP;
		}

		Params params = Params.param();
		Date nowDate = new Date();

		params.put("endDate", nowDate);
		params.put("classIds", classIds);

		List<Map> results = userMemberRepo.find("$csCountByClass", params).list(Map.class);
		Map<Long, Integer> retMap = new HashMap<Long, Integer>(results.size());

		for (Map m : results) {
			Long classId = ((BigInteger) m.get("id")).longValue();
			Integer memberNum = ((BigInteger) m.get("member_num")).intValue();

			retMap.put(classId, memberNum);
		}

		return retMap;
	}

	@Override
	@Transactional
	public void closeMember(Long[] userIds) {
		Map<Long, UserMember> userMemberMap = this.findByUsers(Lists.newArrayList(userIds));

		if (userMemberMap != null && userMemberMap.size() > 0) {
			userMemberRepo.delete(userMemberMap.values());

			// 转移至历史记录中
			List<UserMemberHistory> historys = new ArrayList<UserMemberHistory>();
			for (UserMember userMember : userMemberMap.values()) {
				UserMemberHistory history = new UserMemberHistory();
				history.setEndAt(userMember.getEndAt());
				history.setMemberType(userMember.getMemberType());
				history.setStartAt(userMember.getStartAt());
				history.setUserId(userMember.getUserId());
				history.setOrderID(userMember.getOrderID());
				historys.add(history);
			}
			userMemberHistoryRepo.save(historys);
		}
	}
}

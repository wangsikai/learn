package com.lanking.uxb.service.mall.api.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.domain.yoo.member.MemberPackage;
import com.lanking.cloud.domain.yoo.member.MemberPackageGroup;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.order.PayMode;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrder;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderSettlement;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderSource;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderStatus;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderType;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.mall.api.MemberPackageOrderRefundService;
import com.lanking.uxb.service.mall.api.MemberPackageOrderSettlementService;
import com.lanking.uxb.service.user.api.UserService;

/**
 * 会员套餐订单结算表接口实现.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2017年2月27日
 */
@Service
@Transactional(readOnly = true)
public class MemberPackageOrderSettlementServiceImpl implements MemberPackageOrderSettlementService {
	@Autowired
	@Qualifier("MemberPackageOrderSettlementRepo")
	private Repo<MemberPackageOrderSettlement, Long> repo;
	@Autowired
	@Qualifier("MemberPackageRepo")
	private Repo<MemberPackage, Long> memberPackageRepo;
	@Autowired
	@Qualifier("MemberPackageGroupRepo")
	private Repo<MemberPackageGroup, Long> memberPackageGroupRepo;

	@Autowired
	@Qualifier("userService")
	private UserService userService;
	@Autowired
	private MemberPackageOrderRefundService memberPackageOrderRefundService;
	@Autowired
	private ParameterService parameterService;

	private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);

	@Override
	public MemberPackageOrderSettlement findByOrder(long orderId) {
		List<MemberPackageOrderSettlement> list = repo.find("findByOrder", Params.param("orderId", orderId)).list();
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	@Transactional
	public void createByOrder(MemberPackageOrder order, int memberCount) {
		if (order.getStatus() != MemberPackageOrderStatus.COMPLETE
				|| order.getSource() != MemberPackageOrderSource.CHANNEL
				|| (order.getPayMod() != null && order.getPayMod() != PayMode.ONLINE)
				|| order.getUserChannelCode() == UserChannel.YOOMATH
				|| order.getMemberType() == MemberType.SCHOOL_VIP) {
			// 只统计渠道相关的已完成订单，只统计线上支付或者后台开通的订单，只统计普通会员订单
			// 套餐组不得为空
			return;
		}
		MemberPackageOrderSettlement entity = this.findByOrder(order.getId());
		if (entity != null) {
			// 已经存在该订单统计
			return;
		}
		if (order.getType() == MemberPackageOrderType.USER) {
			User user = userService.get(order.getUserId());
			if (user.getUserType() != UserType.STUDENT) {
				// 只统计学生订单
				return;
			}
		}

		// 套餐
		MemberPackage memberPackage = memberPackageRepo.get(order.getMemberPackageId());
		Long memberPackageGroupId = order.getMemberPackageGroupId();
		if (memberPackageGroupId == null && memberPackage != null) {
			memberPackageGroupId = memberPackage.getMemberPackageGroupId();
		}
		if (memberPackageGroupId == null) {
			return;
		}

		// 套餐组
		MemberPackageGroup memberPackageGroup = memberPackageGroupRepo.get(memberPackageGroupId);

		final int userChannelCode = order.getUserChannelCode(); // 套餐组ID
		int yearCount = memberPackage.getMonth() / 12; // 套餐的年份数
		BigDecimal profits1 = memberPackageGroup.getProfits1(); // 公司利润1
		BigDecimal profits2 = memberPackageGroup.getProfits2(); // 公司利润2
		Parameter parameter = parameterService.get(Product.YOOMATH, "memberPackage.settlement"); // 结算返还截止人数
		final int divideNum = parameter == null ? 699 : Integer.parseInt(parameter.getValue()); // 固定阀值
		int thisCountPoint = memberCount * yearCount; // 本次订单的阀值数

		entity = new MemberPackageOrderSettlement();
		entity.setOrderId(order.getId());
		entity.setOrderCode(order.getCode());
		entity.setUserChannelCode(userChannelCode);
		entity.setType(order.getType());
		entity.setTransactionAmount(order.getTotalPrice());
		entity.setMemberCount(memberCount);
		entity.setProfits(profits2.multiply(new BigDecimal(thisCountPoint))); // 当前订单的公司利润
		entity.setProfitsGap(new BigDecimal(0));
		entity.setYearCount(yearCount);
		entity.setMemberPackageGroupId(memberPackageGroupId);

		// 获得已开通阀值数、是否已经做过利润返还
		final Date date = new Date();
		Map<String, Integer> countMap = this.countMember(userChannelCode);
		int countPoint = countMap.get("countPoint"); // 已产生的阀值数

		if (countMap.get("refund") == 0) {
			// 没有做过利润返还
			if (countPoint + thisCountPoint >= divideNum) {
				// 需要做利润返还
				fixedThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						memberPackageOrderRefundService.refund(divideNum, userChannelCode);
					}
				});
				if (countPoint <= divideNum) {
					int c1 = divideNum - countPoint; // 按原始利润计算的部分
					int c2 = thisCountPoint - c1; // 按新利润计算的部分
					entity.setProfits(new BigDecimal((c1 * profits1.doubleValue()) + (c2 * profits2.doubleValue())));
					if (countPoint < divideNum) {
						entity.setProfitsGap(new BigDecimal(c1).multiply(profits1.subtract(profits2))); // 返还利润
						entity.setProfitsGapAt(date);
					}
				}
			} else {
				entity.setProfits(profits1.multiply(new BigDecimal(thisCountPoint))); // 当前订单的公司利润
			}
		} else {
			if (countPoint + thisCountPoint > divideNum) {
				if (countPoint <= divideNum) {
					int c1 = divideNum - countPoint; // 按原始利润计算的部分
					int c2 = thisCountPoint - c1; // 按新利润计算的部分
					entity.setProfits(new BigDecimal((c1 * profits1.doubleValue()) + (c2 * profits2.doubleValue())));
				}
			} else {
				entity.setProfits(profits1.multiply(new BigDecimal(thisCountPoint)));
			}
		}

		entity.setChannelProfits(order.getTotalPrice().subtract(entity.getProfits())); // 销售渠道利润
		Calendar cal = Calendar.getInstance();
		cal.setTime(order.getOrderAt());
		entity.setSettlementYear(cal.get(Calendar.YEAR));
		entity.setSettlementMonth(cal.get(Calendar.MONTH) + 1);
		entity.setCreateAt(date);
		repo.save(entity);
	}

	/**
	 * 获得已产生的渠道阀值及是否已经做了返润.
	 * 
	 * @param userChannelCode
	 *            渠道
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Map<String, Integer> countMember(int userChannelCode) {
		List<Map> list = repo.find("$countMember", Params.param("userChannelCode", userChannelCode)).list(Map.class);
		Map<String, Integer> map = new HashMap<String, Integer>(2);
		int countPoint = 0;
		int refund = 0;
		if (list.size() > 0) {
			Map objMap = list.get(0);
			if (objMap.get("mc") != null) {
				countPoint = Integer.parseInt(objMap.get("mc").toString());
			}
			if (objMap.get("pg") != null) {
				double pg = Double.parseDouble(objMap.get("pg").toString());
				if (pg > 0) {
					refund = 1;
				}
			}
		}
		map.put("countPoint", countPoint); // 已产生的阀值
		map.put("refund", refund); // 是否已做了返润
		return map;
	}
}

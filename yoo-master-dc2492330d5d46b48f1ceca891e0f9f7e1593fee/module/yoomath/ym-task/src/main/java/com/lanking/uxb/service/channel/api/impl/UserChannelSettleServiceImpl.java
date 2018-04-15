package com.lanking.uxb.service.channel.api.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrder;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderChannelSettlement;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderChannelSettlementStatus;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderSettlement;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderType;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.channel.api.UserChannelSettleService;
import com.lanking.uxb.service.syncOrder.api.TaskMemberPackageOrderService;
import com.lanking.uxb.service.user.api.UserService;

@Service
@Transactional(readOnly = true)
public class UserChannelSettleServiceImpl implements UserChannelSettleService {

	@Autowired
	@Qualifier("MemberPackageOrderChannelSettlementRepo")
	Repo<MemberPackageOrderChannelSettlement, Long> repo;

	@Autowired
	@Qualifier("MemberPackageOrderRepo")
	Repo<MemberPackageOrder, Long> orderRepo;

	@Autowired
	@Qualifier("UserChannelRepo")
	Repo<UserChannel, Integer> channelRepo;

	@Autowired
	@Qualifier("MemberPackageOrderSettlementRepo")
	Repo<MemberPackageOrderSettlement, Long> settlementRepo;

	@Autowired
	private TaskMemberPackageOrderService memberPackageOrderService;
	@Autowired
	@Qualifier("userService")
	private UserService userService;

	/**
	 * 统计任务.
	 */
	@Override
	@Transactional
	public void staticChannelSettle(int year, int month) {
		List<UserChannel> channels = this.findAllChannel();
		for (UserChannel userChannel : channels) {
			if (userChannel.getCode() == UserChannel.YOOMATH) {
				continue;
			}
			this.updateBySingleMonth(userChannel.getCode(), year, month);
		}
	}

	/**
	 * 获得所有渠道.
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	List<UserChannel> findAllChannel() {
		return channelRepo.find("$taskAllUserChannel").list();
	}

	/**
	 * 分页获取指定年月的渠道相关已完成订单统计数据.
	 * 
	 * @param channelCode
	 * @param year
	 * @param month
	 * @param pageable
	 * @return
	 */
	@Transactional(readOnly = true)
	private Page<MemberPackageOrderSettlement> findOrderSettleByChannel(int channelCode, int year, int month,
			Pageable pageable) {
		return settlementRepo.find("$taskFindByChannel",
				Params.param("channelCode", channelCode).put("year", year).put("month", month)).fetch(pageable);
	}

	/**
	 * 获取会员套餐订单渠道结算数据.
	 * 
	 * @param channelCode
	 *            渠道商
	 * @param settlementYear
	 *            统计年份
	 * @param settlementMonth
	 *            统计月份
	 * @return MemberPackageOrderChannelSettlement
	 */
	@Transactional(readOnly = true)
	public MemberPackageOrderChannelSettlement get(int channelCode, int settlementYear, int settlementMonth) {
		return repo.find("getByDate",
				Params.param("channelCode", channelCode).put("year", settlementYear).put("month", settlementMonth))
				.get();
	}

	/**
	 * 获取某渠道返还利润的时间点及总数
	 * 
	 * @param channelCode
	 *            渠道号
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	private Map<String, Object> findRefundDatas(int channelCode) {
		List<Map> list = settlementRepo.find("$findRefundDatas", Params.param("channelCode", channelCode)).list(
				Map.class);
		Map<String, Object> map = new HashMap<String, Object>(2);
		Date profitsGapAt = null;
		Double profitsGap = 0.0;
		if (list.size() > 0) {
			Map objMap = list.get(0);
			if (objMap.get("pga") != null) {
				profitsGapAt = (Date) objMap.get("pga");
			}
			if (objMap.get("pg") != null) {
				profitsGap = Double.parseDouble(objMap.get("pg").toString());
			}
		}
		map.put("profitsGapAt", profitsGapAt);
		map.put("profitsGap", profitsGap);

		return map;
	}

	/**
	 * 获得已返还利润的统计.
	 * 
	 * @param channelCode
	 *            渠道号
	 * @return
	 */
	@Transactional(readOnly = true)
	MemberPackageOrderChannelSettlement getRefundSettle(int channelCode) {
		MemberPackageOrderChannelSettlement entity = repo.find("$getRefundSettle",
				Params.param("channelCode", channelCode)).get();
		return entity;
	}

	/**
	 * 统计一个月的渠道销售情况.
	 * 
	 * @param channelCode
	 *            渠道商
	 * @param settlementYear
	 *            统计年份
	 * @param settlementMonth
	 *            统计月份
	 */
	@Transactional
	private void updateBySingleMonth(int channelCode, int settlementYear, int settlementMonth) {
		Page<MemberPackageOrderSettlement> orderSettles = this.findOrderSettleByChannel(channelCode, settlementYear,
				settlementMonth, P.all());
		if (orderSettles.getItemSize() == 0) {
			return;
		}
		MemberPackageOrderChannelSettlement entity = this.get(channelCode, settlementYear, settlementMonth);
		if (entity == null) {
			entity = new MemberPackageOrderChannelSettlement();
		}
		entity.setChannelCode(channelCode);
		entity.setChannelProfits(new BigDecimal(0));
		entity.setProfits(new BigDecimal(0));
		entity.setSettlementYear(settlementYear);
		entity.setSettlementMonth(settlementMonth);
		entity.setStatus(MemberPackageOrderChannelSettlementStatus.INIT);
		entity.setCreateAt(new Date());
		entity.setTransactionAmount(new BigDecimal(0));
		entity.setOnlineTransactionAmount(new BigDecimal(0));
		entity.setChannelTransactionAmount(new BigDecimal(0));
		entity.setAdminTransactionAmount(new BigDecimal(0));
		entity.setChannelProfits(new BigDecimal(0));
		entity.setProfits(new BigDecimal(0));
		entity.setProfitsGap(new BigDecimal(0));

		// 获得返现信息
		MemberPackageOrderChannelSettlement refundSettle = this.getRefundSettle(channelCode);
		if (refundSettle == null) {
			Map<String, Object> refundDatas = this.findRefundDatas(channelCode);
			Date profitsGapAt = (Date) refundDatas.get("profitsGapAt");
			Double profitsGap = (Double) refundDatas.get("profitsGap");
			if (profitsGapAt != null) {
				Calendar cal = Calendar.getInstance();
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH);
				cal.setTime(profitsGapAt);
				if (year == cal.get(Calendar.YEAR) && month == cal.get(Calendar.MONTH)) {
					entity.setProfitsGap(new BigDecimal(profitsGap));
				}
			}
		}

		for (MemberPackageOrderSettlement orderSettle : orderSettles.getItems()) {
			BigDecimal totalPrice = orderSettle.getTransactionAmount();
			entity.setTransactionAmount(entity.getTransactionAmount().add(totalPrice)); // 交易总金额
			if (orderSettle.getType() == MemberPackageOrderType.USER) {
				// 线上交易总金额
				entity.setOnlineTransactionAmount(entity.getOnlineTransactionAmount().add(totalPrice));
			} else if (orderSettle.getType() == MemberPackageOrderType.CHANNEL_ADMIN) {
				// 开发商开通交易金额
				entity.setChannelTransactionAmount(entity.getChannelTransactionAmount().add(totalPrice));
			} else {
				// 管理员代开交易金额
				entity.setAdminTransactionAmount(entity.getAdminTransactionAmount().add(totalPrice));
			}
			entity.setChannelProfits(entity.getChannelProfits().add(orderSettle.getChannelProfits())); // 渠道利润
			entity.setProfits(entity.getProfits().add(orderSettle.getProfits())); // 公司利润
		}

		// 计算结算利润
		entity.setSettlementProfits(entity.getProfits().subtract(entity.getOnlineTransactionAmount())
				.subtract(entity.getProfitsGap()));

		repo.save(entity);
	}
}

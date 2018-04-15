package com.lanking.uxb.channelSales.finance.api.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderChannelSettlement;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderChannelSettlementStatus;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.channelSales.finance.api.CsMemberPackageOrderChannelSettlementService;

@Service
@Transactional(readOnly = true)
public class CsMemberPackageOrderChannelSettlementServiceImpl implements CsMemberPackageOrderChannelSettlementService {

	@Autowired
	@Qualifier("MemberPackageOrderChannelSettlementRepo")
	private Repo<MemberPackageOrderChannelSettlement, Long> memberPackageOrderChannelSettlementRepo;
	@Autowired
	@Qualifier("UserChannelRepo")
	private Repo<UserChannel, Long> userChannelRepo;

	@Override
	public List<MemberPackageOrderChannelSettlement> list(Integer year, Integer channelCode) {
		return memberPackageOrderChannelSettlementRepo.find("$csSettlementList",
				Params.param("year", year).put("channelCode", channelCode)).list();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> queryChannelSalesRank(Integer year, Integer month) {
		Params params = Params.param();
		if (year != null) {
			params.put("year", year);
		}
		if (month != null) {
			params.put("month", month);
		}
		if (month == null) {
			return memberPackageOrderChannelSettlementRepo.find("$csQueryChannelSalesAllYearRank", params).list(
					Map.class);
		} else {
			return memberPackageOrderChannelSettlementRepo.find("$csQueryChannelSalesRank", params).list(Map.class);
		}

	}

	@Transactional
	@Override
	public void settlement(long id) {
		MemberPackageOrderChannelSettlement s = memberPackageOrderChannelSettlementRepo.get(id);
		s.setStatus(MemberPackageOrderChannelSettlementStatus.SETTLED);
		s.setSettlementAt(new Date());
		memberPackageOrderChannelSettlementRepo.save(s);

		// 恢复渠道商额度
		userChannelRepo.execute(
				"$csUpdateUserChannelLimit",
				Params.param("channelCode", s.getChannelCode()).put("addLimit",
						s.getChannelTransactionAmount().doubleValue()));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> allYearStat(Integer year, Integer channelCode, Integer month) {
		Params params = Params.param();
		if (year != null) {
			params.put("year", year);
		}
		if (month != null) {
			params.put("month", month);
		}
		if (channelCode != null) {
			params.put("channelCode", channelCode);
		}
		return memberPackageOrderChannelSettlementRepo.find("$csAllYearStat", params).list(Map.class);
	}
}

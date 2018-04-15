package com.lanking.uxb.channelSales.finance.api.impl;

import httl.util.StringUtils;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrder;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.channelSales.finance.api.CsMemberPackageOrderQuery;
import com.lanking.uxb.channelSales.finance.api.CsMemberPackageOrderStatService;

@Service
@Transactional(readOnly = true)
public class CsMemberPackageOrderStatServiceImpl implements CsMemberPackageOrderStatService {

	@Autowired
	@Qualifier("MemberPackageOrderRepo")
	private Repo<MemberPackageOrder, Long> memberPackageOrderRepo;

	@Override
	public Page<Map> memberPackagerStat(CsMemberPackageOrderQuery query, Pageable p) {
		Params params = Params.param();
		if (null != query.getUserType()) {
			params.put("userType", query.getUserType().getValue());
		}
		if (null != query.getMemberType()) {
			params.put("memberType", query.getMemberType().getValue());
		}
		if (StringUtils.isNotBlank(query.getStartDate())) {
			params.put("startAt", query.getStartDate());
		}
		if (StringUtils.isNotBlank(query.getEndDate())) {
			params.put("endAt", query.getEndDate());
		}
		return memberPackageOrderRepo.find("$csQueryMemberPackageOrderStat", params).fetch(p, Map.class);

	}

	@Override
	public List<Map> memberPackagerSumStat(CsMemberPackageOrderQuery query) {

		Params params = Params.param();
		if (null != query.getUserType()) {
			params.put("userType", query.getUserType().getValue());
		}
		if (null != query.getMemberType()) {
			params.put("memberType", query.getMemberType().getValue());
		}
		if (StringUtils.isNotBlank(query.getStartDate())) {
			params.put("startAt", query.getStartDate());
		}
		if (StringUtils.isNotBlank(query.getEndDate())) {
			params.put("endAt", query.getEndDate());
		}

		return memberPackageOrderRepo.find("$csQueryMemberPackageOrderSumStat", params).list(Map.class);
	}

	@Override
	public List<Integer> getHasDataYearList() {
		return memberPackageOrderRepo.find("$cardYearList").list(Integer.class);
	}

	@Override
	public Page<Map> queryMemberPackageStatByChannel(CsMemberPackageOrderQuery query, Pageable pageable) {
		Params params = Params.param();
		if (StringUtils.isNotBlank(query.getChannelName())) {
			params.put("name", "%" + query.getChannelName() + "%");
		}
		if (StringUtils.isBlank(query.getStartDate())) {
			if (query.getYear() != null) {
				params.put("year", query.getYear());
			}
			if (query.getMonth() != null) {
				params.put("month", query.getMonth());
			}
		} else {
			params.put("startTime", query.getStartDate());
			if (StringUtils.isNotBlank(query.getEndDate())) {
				params.put("endTime", query.getEndDate());
			}
		}

		return memberPackageOrderRepo.find("$queryByChannel", params).fetch(pageable, Map.class);
	}
}

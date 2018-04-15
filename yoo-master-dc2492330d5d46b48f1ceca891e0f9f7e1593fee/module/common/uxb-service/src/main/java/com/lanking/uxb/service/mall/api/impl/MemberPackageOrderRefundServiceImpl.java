package com.lanking.uxb.service.mall.api.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.member.MemberPackageGroup;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderSettlement;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.mall.api.MemberPackageOrderRefundService;

@Service
@Transactional(readOnly = true)
public class MemberPackageOrderRefundServiceImpl implements MemberPackageOrderRefundService {
	@Autowired
	@Qualifier("MemberPackageOrderSettlementRepo")
	private Repo<MemberPackageOrderSettlement, Long> repo;

	/**
	 * 返款操作，对已有的单子数据做更新.
	 * 
	 * @param divideNum
	 *            阀值人数
	 * @param userChannelCode
	 *            渠道
	 */
	@Transactional
	@Override
	public void refund(int divideNum, int userChannelCode) {
		// 获取单子集合
		List<MemberPackageOrderSettlement> list = repo
				.find("queryRefunds", Params.param("userChannelCode", userChannelCode)).list();
		// 对应的套餐组集合
		List<MemberPackageGroup> memberPackageGroups = repo
				.find("$queryGroupsBySettles", Params.param("userChannelCode", userChannelCode))
				.list(MemberPackageGroup.class);
		Map<Long, MemberPackageGroup> groups = new HashMap<Long, MemberPackageGroup>();
		for (MemberPackageGroup group : memberPackageGroups) {
			groups.put(group.getId(), group);
		}

		int countPoint = 0; // 已产生的阀值数
		Date date = new Date();
		List<MemberPackageOrderSettlement> results = new ArrayList<MemberPackageOrderSettlement>();
		for (MemberPackageOrderSettlement settle : list) {
			MemberPackageGroup group = groups.get(settle.getMemberPackageGroupId());
			if (group == null) {
				continue;
			}
			double profits1 = group.getProfits1().doubleValue();
			double profits2 = group.getProfits2().doubleValue();
			int thisCountPoint = settle.getMemberCount() * settle.getYearCount(); // 本次订单的阀值数

			if (countPoint >= divideNum) {
				break;
			} else if (countPoint + thisCountPoint <= divideNum) {
				settle.setProfitsGap(new BigDecimal((profits1 - profits2) * thisCountPoint)); // 返还差额
				settle.setProfitsGapAt(date);
				results.add(settle);
			} else {
				settle.setProfitsGap(new BigDecimal((profits1 - profits2) * (divideNum - countPoint))); // 返还差额
				settle.setProfitsGapAt(date);
				results.add(settle);
				break;
			}
			countPoint += thisCountPoint;
		}
		repo.save(results);
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public void refund(int divideNum) {
		// 获取所有未做返还的，且总阈值超过或等于指定阈值的渠道
		List<Map> list = repo.find("$findAllNotRefundChannel", Params.param("divideNum", divideNum)).list(Map.class);
		if (list.size() > 0) {
			for (Map objMap : list) {
				if (objMap.get("code") != null) {
					this.refund(divideNum, Integer.parseInt(objMap.get("code").toString()));
				}
			}
		}
	}
}

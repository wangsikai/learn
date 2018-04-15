package com.lanking.uxb.channelSales.finance.api.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.order.VirtualCardType;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrder;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderSource;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderType;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.channelSales.finance.api.CsFinanceQuery;
import com.lanking.uxb.channelSales.finance.api.CsFinanceStatisticsService;
import com.lanking.uxb.channelSales.finance.api.CsMemberPackageOrderChannelSettlementService;

@Service
@Transactional(readOnly = true)
public class CsFinanceStatisticsServiceImpl implements CsFinanceStatisticsService {

	@Autowired
	@Qualifier("MemberPackageOrderRepo")
	private Repo<MemberPackageOrder, Long> memberPackageOrderRepo;

	@Autowired
	private CsMemberPackageOrderChannelSettlementService settlementService;

	/**
	 * 渠道商
	 */
	@Override
	public Map<String, Object> getChannelStat(CsFinanceQuery query) {
		query.setCardType(VirtualCardType.DEFAULT);
		query.setSource(MemberPackageOrderSource.CHANNEL);
		query.setOrderType(MemberPackageOrderType.ADMIN);
		query.setMemberType(MemberType.SCHOOL_VIP);
		// 管理员开通校级会员
		Double admin_schoolVip = this.queryBaseStatByCondition(query);
		// 管理员开通学生会员
		query.setMemberType(MemberType.VIP);
		query.setUserType(UserType.STUDENT);
		Double admin_stuVip = this.queryBaseStatByCondition(query);
		// 管理员开通老师会员
		query.setUserType(UserType.TEACHER);
		Double admin_teaVip = this.queryBaseStatByCondition(query);
		// 个人自主消费
		query.setOrderType(MemberPackageOrderType.USER);
		// 个人自主学生会员
		query.setUserType(UserType.STUDENT);
		Double personal_stuVip = this.queryBaseStatByCondition(query);
		// 个人自主老师会员
		query.setUserType(UserType.TEACHER);
		Double personal_teaVip = this.queryBaseStatByCondition(query);
		// 错题本代印价格
		Double fallPrint = this.queryFallPrintStat(query);
		query.setChannel(true);
		Double buyPaper = this.queryBuyPaperStat(query);
		Map<String, Object> statMap = new HashMap<String, Object>();
		statMap.put("admin_schoolVip", admin_schoolVip);
		statMap.put("admin_stuVip", admin_stuVip);
		statMap.put("admin_teaVip", admin_teaVip);
		statMap.put("personal_stuVip", personal_stuVip);
		statMap.put("personal_teaVip", personal_teaVip);
		statMap.put("fallPrint", fallPrint);
		statMap.put("buyPaper", buyPaper);
		// 管理员代开通总收入
		Double adminTotal = admin_schoolVip + admin_stuVip + admin_teaVip;
		BigDecimal a = new BigDecimal(adminTotal);
		statMap.put("adminTotal", a.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		// 个人自主消费总收入
		Double personalTotal = personal_stuVip + personal_teaVip + fallPrint + buyPaper;
		BigDecimal b = new BigDecimal(personalTotal);
		statMap.put("personalTotal", b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		// 渠道商总收入
		BigDecimal c = new BigDecimal(adminTotal + personalTotal);
		statMap.put("total", c.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		return statMap;
	}

	/**
	 * 会员卡 <br>
	 * 需求要求是一旦创建就计入统计，不是激活后才统计
	 * 
	 * @param query
	 * @return
	 */
	@Override
	public Map<String, Object> getMemberCardStat(CsFinanceQuery query) {
		Params params = Params.param();
		if (query.getStartTime() != null) {
			params.put("startTime", query.getStartTime());
		}
		if (query.getEndTime() != null) {
			params.put("endTime", query.getEndTime());
		}
		if (query.getYear() != null) {
			params.put("timeStr", this.getBaseTimeStr(query.getYear(), query.getMonth()) + "%");
		}
		// 会员卡--校级会员
		params.put("memberType", MemberType.SCHOOL_VIP.getValue());
		Double card_schoolVip = queryCardTotal(params);
		// 会员卡--学生会员
		params.put("memberType", MemberType.VIP.getValue());
		params.put("userType", UserType.STUDENT.getValue());
		Double card_stuVip = queryCardTotal(params);
		// 会员卡--老师会员
		params.put("memberType", MemberType.VIP.getValue());
		params.put("userType", UserType.TEACHER.getValue());
		Double card_teaVip = queryCardTotal(params);
		Map<String, Object> statMap = new HashMap<String, Object>();
		statMap.put("card_schoolVip", card_schoolVip);
		statMap.put("card_stuVip", card_stuVip);
		statMap.put("card_teaVip", card_teaVip);
		BigDecimal a = new BigDecimal(card_schoolVip + card_stuVip + card_teaVip);
		statMap.put("total", a.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		return statMap;
	}

	public Double queryCardTotal(Params params) {
		Double val = memberPackageOrderRepo.find("$queryCardTotal", params).get(Double.class);
		return val == null ? 0.0 : val;
	}

	@Override
	public Map<String, Object> getStuAutoStat(CsFinanceQuery query) {
		query.setCardType(VirtualCardType.DEFAULT);
		query.setChannel(false);
		query.setSource(MemberPackageOrderSource.USER);
		query.setUserType(UserType.STUDENT);
		query.setMemberType(MemberType.VIP);
		query.setOrderType(null);
		// 学生自主开通学生会员
		Double stuAuto_Vip = this.queryBaseStatByCondition(query);
		// 错题本代印价格
		query.setChannel(false);
		Double fallPrint = this.queryFallPrintStat(query);
		Map<String, Object> statMap = new HashMap<String, Object>();
		statMap.put("stuAuto_Vip", stuAuto_Vip);
		statMap.put("fallPrint", fallPrint);
		BigDecimal a = new BigDecimal(stuAuto_Vip + fallPrint);
		statMap.put("total", a.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		return statMap;
	}

	@Override
	public Map<String, Object> getTeaAutoStat(CsFinanceQuery query) {
		query.setCardType(VirtualCardType.DEFAULT);
		query.setChannel(false);
		query.setSource(MemberPackageOrderSource.USER);
		query.setUserType(UserType.TEACHER);
		query.setMemberType(MemberType.VIP);
		query.setOrderType(null);
		// 老师自主开通学生会员
		Double teaAuto_Vip = this.queryBaseStatByCondition(query);
		query.setChannel(false);
		Double buyPaper = this.queryBuyPaperStat(query);
		Map<String, Object> statMap = new HashMap<String, Object>();
		statMap.put("teaAuto_Vip", teaAuto_Vip);
		statMap.put("buyPaper", buyPaper);
		BigDecimal a = new BigDecimal(teaAuto_Vip + buyPaper);
		statMap.put("total", a.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		return statMap;
	}

	@Override
	public Double queryBaseStatByCondition(CsFinanceQuery query) {
		Params params = Params.param();
		params.put("isChannel", query.isChannel());
		if (query.getCardType() != null) {
			params.put("cardType", query.getCardType().getValue());
		}
		if (query.getSource() != null) {
			params.put("source", query.getSource().getValue());
		}
		if (query.getOrderType() != null) {
			params.put("orderType", query.getOrderType().getValue());
		}
		if (query.getUserType() != null) {
			params.put("userType", query.getUserType().getValue());
		}
		if (query.getMemberType() != null) {
			params.put("memberType", query.getMemberType().getValue());
		}
		if (query.getYear() != null) {
			params.put("timeStr", this.getBaseTimeStr(query.getYear(), query.getMonth()) + "%");
		}
		if (query.getStartTime() != null) {
			params.put("startTime", query.getStartTime());
		}
		if (query.getEndTime() != null) {
			params.put("endTime", query.getEndTime());
		}
		Double price = memberPackageOrderRepo.find("$queryBaseStatByCondition", params).get(Double.class);
		return price == null ? 0.0 : price;
	}

	@Override
	public Double queryFallPrintStat(CsFinanceQuery query) {
		Params params = Params.param("isChannel", query.isChannel());
		if (query.getYear() != null) {
			params.put("timeStr", this.getBaseTimeStr(query.getYear(), query.getMonth()) + "%");
		}
		if (query.getStartTime() != null) {
			params.put("startTime", query.getStartTime());
		}
		if (query.getEndTime() != null) {
			params.put("endTime", query.getEndTime());
		}
		Double price = memberPackageOrderRepo.find("$queryFallPrintStat", params).get(Double.class);
		return price == null ? 0.0 : price;
	}

	@Override
	public Double queryBuyPaperStat(CsFinanceQuery query) {
		Params params = Params.param("isChannel", query.isChannel());
		if (query.getYear() != null) {
			params.put("timeStr", this.getBaseTimeStr(query.getYear(), query.getMonth()) + "%");
		}
		if (query.getStartTime() != null) {
			params.put("startTime", query.getStartTime());
		}
		if (query.getEndTime() != null) {
			params.put("endTime", query.getEndTime());
		}
		Double price = memberPackageOrderRepo.find("$queryBuyPaperStat", params).get(Double.class);
		return price == null ? 0.0 : price;
	}

	public String getBaseTimeStr(String year, String month) {
		if (month != null) {
			if (Integer.parseInt(month) > 9) {
				return year + "-" + month;
			} else {
				return year + "-" + "0" + month;
			}
		} else {
			return year;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Object> getStatMap(CsFinanceQuery query) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (query.getTypeStr().equals("all")) {
			Map<String, Object> channelMap = getChannelStat(query);
			Map<String, Object> cardMap = getMemberCardStat(query);
			Map<String, Object> stuAutoMap = getStuAutoStat(query);
			Map<String, Object> teaAutoMap = getTeaAutoStat(query);
			data.put("channel", channelMap);
			data.put("card", cardMap);
			data.put("stuAuto", stuAutoMap);
			data.put("teaAuto", teaAutoMap);
			Double channelTotal = Double.parseDouble(String.valueOf(channelMap.get("total")));
			Double cardTotal = Double.parseDouble(String.valueOf(cardMap.get("total")));
			Double stuAutoTotal = Double.parseDouble(String.valueOf(stuAutoMap.get("total")));
			Double teaAutoTotal = Double.parseDouble(String.valueOf(teaAutoMap.get("total")));
			Double total = channelTotal + cardTotal + stuAutoTotal + teaAutoTotal;
			BigDecimal b = new BigDecimal(total);
			data.put("total", b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			if (query.getStartTime() == null && query.getEndTime() == null) {
				List<Map> list = settlementService.allYearStat(
						query.getYear() == null ? null : Integer.parseInt(query.getYear()), null,
						query.getMonth() == null ? null : Integer.parseInt(query.getMonth()));
				if (CollectionUtils.isNotEmpty(list)) {
					if (list.get(0).get("my_amount") != null) {
						Double profit = Double.parseDouble(list.get(0).get("my_amount").toString());
						data.put("profit", profit);
					} else {
						data.put("profit", "--");
					}
				} else {
					// 如果为空说明还没有结算
					data.put("profit", "--");
				}
			} else {
				data.put("profit", "--");
			}

		} else if (query.getTypeStr().equals("channel")) {
			data.put("channel", getChannelStat(query));
		} else if (query.getTypeStr().equals("card")) {
			data.put("card", getMemberCardStat(query));
		} else if (query.getTypeStr().equals("stuAuto")) {
			data.put("stuAuto", getStuAutoStat(query));
		} else if (query.getTypeStr().equals("teaAuto")) {
			data.put("teaAuto", getTeaAutoStat(query));
		}
		return data;
	}

	@Override
	public List<Integer> getHasDataYearList() {
		List<Integer> cardYearList = memberPackageOrderRepo.find("$cardYearList").list(Integer.class);
		List<Integer> fallPrintYearList = memberPackageOrderRepo.find("$fallPrintYearList").list(Integer.class);
		List<Integer> resourceGoodsYearList = memberPackageOrderRepo.find("$resourceGoodsYearList").list(Integer.class);
		Set<Integer> set = new HashSet<Integer>();
		set.addAll(cardYearList);
		set.addAll(fallPrintYearList);
		set.addAll(resourceGoodsYearList);
		List<Integer> list = new ArrayList<Integer>(set);
		return list;
	}

}

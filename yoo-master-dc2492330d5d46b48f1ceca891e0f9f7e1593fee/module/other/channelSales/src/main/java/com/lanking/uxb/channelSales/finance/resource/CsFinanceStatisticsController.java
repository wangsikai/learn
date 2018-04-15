package com.lanking.uxb.channelSales.finance.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderChannelSettlement;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.channelSales.channel.api.CsUserChannelService;
import com.lanking.uxb.channelSales.channel.convert.CsUserChannelConvert;
import com.lanking.uxb.channelSales.finance.api.CsFinanceQuery;
import com.lanking.uxb.channelSales.finance.api.CsFinanceStatisticsService;
import com.lanking.uxb.channelSales.finance.api.CsMemberPackageOrderChannelSettlementService;
import com.lanking.uxb.channelSales.finance.api.CsMemberPackageOrderQuery;
import com.lanking.uxb.channelSales.finance.api.CsMemberPackageOrderStatService;
import com.lanking.uxb.channelSales.finance.convert.MemberPackageOrderChannelSettlementConvert;
import com.lanking.uxb.channelSales.finance.value.VMemberPackageOrderChannelSettlement;
import com.lanking.uxb.channelSales.finance.value.VPageStat;

/**
 * 财务统计
 * 
 * @author wangsenhao
 *
 */
@RestController
@RequestMapping("channelSales/finance")
public class CsFinanceStatisticsController {
	@Autowired
	private CsMemberPackageOrderStatService memberPackageOrderStatService;
	@Autowired
	private CsFinanceStatisticsService csFinanceStatisticsService;
	@Autowired
	private CsMemberPackageOrderChannelSettlementService settlementService;
	@Autowired
	private MemberPackageOrderChannelSettlementConvert settlementConvert;
	@Autowired
	private CsUserChannelService csUserChannelService;
	@Autowired
	private CsUserChannelConvert csUserChannelConvert;

	/**
	 * 财务统计--整体统计
	 * 
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "allStat")
	public Value allStat(CsFinanceQuery query) {
		return new Value(csFinanceStatisticsService.getStatMap(query));
	}

	/**
	 * 返回柱状图数据
	 * 
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "histogramData")
	public Value histogramData(CsFinanceQuery query) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 1; i <= 12; i++) {
			query.setMonth(String.valueOf(i));
			list.add(csFinanceStatisticsService.getStatMap(query));
		}
		return new Value(list);
	}

	/**
	 * 获取有数据的年份集合
	 * 
	 * @return
	 */
	@RequestMapping(value = "getHasDataYearList")
	public Value getHasDataYearList() {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Integer> list = csFinanceStatisticsService.getHasDataYearList();
		List<Object> tempList = new ArrayList<Object>();
		tempList.addAll(list);
		tempList.add(0, "总收入");
		data.put("first", list);
		data.put("second", tempList);
		return new Value(data);
	}

	/**
	 * 会员套餐
	 * 
	 * @author zemin.song
	 */
	@RequestMapping(value = "queryMemberPackageOrderStat")
	public Value fallibleQuestionPrintOrderQuery(CsMemberPackageOrderQuery query) {
		Pageable pageable = P.index(query.getPage(), query.getSize());
		Page<Map> page = memberPackageOrderStatService.memberPackagerStat(query, pageable);
		VPageStat vpage = new VPageStat();
		if (query.getPage() == 1) {
			List<Map> map = memberPackageOrderStatService.memberPackagerSumStat(query);
			vpage.setTotalSum(map);
		}
		vpage.setItems(page.getItems());
		vpage.setPageSize(query.getSize());
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		vpage.setCurrentPage(query.getPage());
		return new Value(vpage);
	}

	/**
	 * 获取有数据的年份集合 （会员套餐订单）
	 * 
	 * @author zemin.song
	 */
	@RequestMapping(value = "getYearListByMemberPackageOrder")
	public Value getHasDataYearListByMemberPackageOrder() {
		return new Value(memberPackageOrderStatService.getHasDataYearList());
	}

	@RequestMapping(value = "queryByChannel", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryByChannel(CsMemberPackageOrderQuery query) {
		Pageable pageable = P.index(query.getPage(), query.getSize());
		Page<Map> page = memberPackageOrderStatService.queryMemberPackageStatByChannel(query, pageable);

		VPageStat v = new VPageStat();
		v.setItems(page.getItems());
		v.setTotal(page.getTotalCount());
		v.setCurrentPage(query.getPage());
		v.setPageSize(query.getSize());
		v.setTotalPage(page.getPageCount());

		return new Value(v);
	}

	/**
	 * 渠道商统计
	 * 
	 * @param year
	 * @param channelCode
	 * @return
	 */
	@RequestMapping(value = "queryChanelStat", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryChanelStat(Integer year, Integer channelCode) {
		List<MemberPackageOrderChannelSettlement> list = settlementService.list(year, channelCode);
		List<VMemberPackageOrderChannelSettlement> vlist = settlementConvert.to(list);
		List<VMemberPackageOrderChannelSettlement> tempList = new ArrayList<VMemberPackageOrderChannelSettlement>();
		List<Integer> monthList = new ArrayList<Integer>();
		Map<Integer, VMemberPackageOrderChannelSettlement> settlementMap = new HashMap<Integer, VMemberPackageOrderChannelSettlement>();
		for (VMemberPackageOrderChannelSettlement t : vlist) {
			monthList.add(t.getSettlementMonth());
			settlementMap.put(t.getSettlementMonth(), t);
		}
		// 没有数据的月份也需要显示
		for (int i = 1; i <= 12; i++) {
			if (monthList.contains(i)) {
				tempList.add(settlementMap.get(i));
			} else {
				VMemberPackageOrderChannelSettlement v = new VMemberPackageOrderChannelSettlement();
				v.setSettlementMonth(i);
				tempList.add(v);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		// 统计列表
		map.put("list", tempList);
		UserChannel s = csUserChannelService.get(channelCode);
		// 当前渠道下学生会员数
		map.put("stuVipNum", s.getStudentVipCount());
		// 全年统计数据
		map.put("allYearData", settlementService.allYearStat(year, channelCode, null));
		return new Value(map);
	}

	/**
	 * 查询渠道商销售额排名
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "queryChannelSalesRank", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryChannelSalesRank(Integer year, Integer month) {
		List<Map> list = settlementService.queryChannelSalesRank(year, month);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rankList", list);
		return new Value(map);
	}

	/**
	 * 结算,更新状态为已结算
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "settlement", method = { RequestMethod.GET, RequestMethod.POST })
	public Value settlement(long id) {
		settlementService.settlement(id);
		return new Value();
	}

	/**
	 * 获取渠道列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "queryChannelBase", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryChannelBase() {
		Map<String, Object> data = new HashMap<String, Object>();
		List<UserChannel> list = csUserChannelService.findAllChannelList();
		List<Integer> yearList = memberPackageOrderStatService.getHasDataYearList();
		data.put("channelList", list);
		data.put("yearList", yearList);
		return new Value(data);
	}
}

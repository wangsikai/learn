package com.lanking.uxb.zycon.mall.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotterySeason;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotterySeasonGoods;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.zycon.mall.api.ZycCoinsGoodsOrderService;
import com.lanking.uxb.zycon.mall.api.ZycCoinsLotteryGoodsService;
import com.lanking.uxb.zycon.mall.api.ZycCoinsLotterySeasonGoodsService;
import com.lanking.uxb.zycon.mall.api.ZycCoinsLotterySeasonService;
import com.lanking.uxb.zycon.mall.api.ZycLotteryRecordQuery;
import com.lanking.uxb.zycon.mall.convert.ZycCoinsLotteryGoodsConvert;
import com.lanking.uxb.zycon.mall.convert.ZycCoinsLotterySeasonConvert;
import com.lanking.uxb.zycon.mall.form.LotteryGoodsForm;
import com.lanking.uxb.zycon.mall.form.LotterySeasonForm;
import com.lanking.uxb.zycon.mall.value.VZycCoinsLotterySeason;

/**
 * 金币抽奖后台相关接口
 * 
 * @since V2.4.0
 * @author wangsenhao
 *
 */
@RestController
@RequestMapping(value = "zyc/lottery")
public class ZycCoinsLotteryController {
	@Autowired
	private ZycCoinsLotteryGoodsService lotteryGoodsService;
	@Autowired
	private ZycCoinsLotterySeasonService lotterySeasonService;
	@Autowired
	private ZycCoinsLotterySeasonGoodsService lotterySeasonGoodsService;
	@Autowired
	private ZycCoinsLotterySeasonConvert seasonConvert;
	@Autowired
	private ZycCoinsLotteryGoodsConvert lotteryGoodsConvert;
	@Autowired
	private ZycCoinsGoodsOrderService orderService;
	@Autowired
	private ParameterService pService;
	
	

	/**
	 * 获取抽奖列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "list", method = { RequestMethod.GET, RequestMethod.POST })
	public Value list(Long seasonId) {
		Map<String, Object> data = new HashMap<String, Object>();
		CoinsLotterySeason season = lotterySeasonService.get(seasonId);
		if (season != null) {
			data.put("season", seasonConvert.to(season));
			List<CoinsLotterySeasonGoods> lotteryGoodsList = lotterySeasonGoodsService.findBySeasonId(seasonId);
			data.put("lotteryGoodsList", lotteryGoodsConvert.to(lotteryGoodsList));
		}
		return new Value(data);
	}

	/**
	 * 保存
	 * 
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "save", method = { RequestMethod.GET, RequestMethod.POST })
	public Value save(LotterySeasonForm form) {
		form.setUserId(Security.getUserId());
		List<LotteryGoodsForm> jsonlist = JSON.parseArray(form.getList(), LotteryGoodsForm.class);
		form.setGoodsList(jsonlist);
		lotteryGoodsService.saveLotteryGoods(form);
		return new Value();
	}

	/**
	 * 抽奖总体统计
	 * 
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "lotteryTotalStatis", method = { RequestMethod.GET, RequestMethod.POST })
	public Value lotteryTotalStatis(ZycLotteryRecordQuery query) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map> list = orderService.queryLotteryOrderStatis(query);
		data.put("list", list);
		Map map = orderService.lotteryTotalStatis(query);
		data.put("total", map);
		return new Value(data);
	}

	/**
	 * 用户抽奖记录列表
	 * 
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "lotteryUserRecordList", method = { RequestMethod.GET, RequestMethod.POST })
	public Value lotteryUserRecordList(ZycLotteryRecordQuery query, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int pageSize) {
		Page<Map> cp = orderService.lotteryUserRecordList(query, P.index(page, pageSize));
		VPage<Map> vp = new VPage<Map>();
		int tPage = (int) (cp.getTotalCount() + pageSize - 1) / pageSize;
		vp.setPageSize(pageSize);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		vp.setItems(cp.getItems());
		return new Value(vp);
	}

	/**
	 * 获取期别列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "seasonList", method = { RequestMethod.GET, RequestMethod.POST })
	public Value seasonList() {
		Parameter p_mobile = pService.get(Product.YOOMATH, "draw.url.mobile");
		Parameter p_web = pService.get(Product.YOOMATH, "draw.url.web");
		List<CoinsLotterySeason> list = lotterySeasonService.list();
		List<VZycCoinsLotterySeason> vList = seasonConvert.to(list);
		for (VZycCoinsLotterySeason v : vList) {
			v.setMobileUrl(p_mobile.getValue().replace("{code}", v.getCode() + ""));
			v.setWebUrl(p_web.getValue().replace("{code}", v.getCode() + ""));
		}
		return new Value(vList);
	}

	/**
	 * 通过活动code查询该活动所有期别
	 * 
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "querySeasonsByCode", method = { RequestMethod.GET, RequestMethod.POST })
	public Value querySeasonsByCode(Integer code) {
		List<CoinsLotterySeason> list = lotterySeasonService.list(code);
		return new Value(seasonConvert.to(list));
	}

	/**
	 * 下架
	 * 
	 * @return
	 */
	@RequestMapping(value = "offShelf", method = { RequestMethod.GET, RequestMethod.POST })
	public Value offShelf(Long seasonId) {
		if (seasonId != null) {
			lotterySeasonService.updateSeasonStatus(Status.DISABLED, seasonId);
		}
		return new Value();
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequestMapping(value = "deleteActive", method = { RequestMethod.GET, RequestMethod.POST })
	public Value deleteActive(Long seasonId) {
		if (seasonId != null) {
			lotterySeasonService.updateSeasonStatus(Status.DELETED, seasonId);
		}
		return new Value();
	}
}

package com.lanking.uxb.zycon.mall.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;
import com.lanking.cloud.domain.yoo.order.GoodsOrderStatus;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrder;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.zycon.mall.api.ZycCoinsGoodsOrderService;
import com.lanking.uxb.zycon.mall.convert.ZycCoinsGoodsOrderConvert;
import com.lanking.uxb.zycon.mall.form.OrderForm;
import com.lanking.uxb.zycon.mall.value.VZycCoinsGoodsOrder;
import com.lanking.uxb.zycon.mall.value.VZycCoinsTypeData;

/**
 * 兑换管理
 * 
 * @since V2.0
 * @author wangsenhao
 *
 */
@RestController
@RequestMapping("zyc/mall")
public class ZycOrderManageController {
	@Autowired
	private ZycCoinsGoodsOrderService coinsGoodsOrderService;
	@Autowired
	private ZycCoinsGoodsOrderConvert coinsGoodsOrderConvert;

	/**
	 * 兑换管理列表
	 * 
	 * @param accountName
	 *            账号名称
	 * @param status
	 *            订单状态
	 * @return
	 */
	@RequestMapping(value = "findOrderList", method = { RequestMethod.GET, RequestMethod.POST })
	public Value findOrderList(OrderForm form, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int pageSize) {
		Page<CoinsGoodsOrder> cp = coinsGoodsOrderService.queryOrderList(form, P.index(page, pageSize));
		VPage<VZycCoinsGoodsOrder> vp = new VPage<VZycCoinsGoodsOrder>();
		int tPage = (int) (cp.getTotalCount() + pageSize - 1) / pageSize;
		vp.setPageSize(pageSize);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		vp.setItems(coinsGoodsOrderConvert.to(cp.getItems()));
		return new Value(vp);
	}

	/**
	 * 更新兑换状态
	 * 
	 * @param orderId
	 *            订单号
	 * @param status
	 *            兑换状态
	 * @return
	 */
	@RequestMapping(value = "updateStatus", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updateStatus(Long orderId, GoodsOrderStatus status, String sellerNotes) {
		coinsGoodsOrderService.updateStatus(orderId, status, sellerNotes, Security.getUserId());
		return new Value();

	}

	/**
	 * 获取兑换商品个数
	 * 
	 * @return
	 */
	@RequestMapping(value = "findTobeExchangeCount", method = { RequestMethod.GET, RequestMethod.POST })
	public Value findTobeExchangeCount() {
		return new Value(coinsGoodsOrderService.tobeExchageCount());
	}

	/**
	 * 统计兑换的记录
	 *
	 * @param beginDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @param type
	 *            {@link CoinsGoodsType}
	 * @return {@link Value}
	 */
	@RequestMapping(value = "statisticData", method = { RequestMethod.GET, RequestMethod.POST })
	public Value statisticData(String beginDate, String endDate, Integer type) {
		Map<String, Object> retValue = new HashMap<String, Object>(1);
		List<Map> list = coinsGoodsOrderService.statisticTypeData(beginDate, endDate, type);
		List<VZycCoinsTypeData> datas = new ArrayList<VZycCoinsTypeData>(list.size());
		for (Map m : list) {
			VZycCoinsTypeData v = new VZycCoinsTypeData();
			if (m.get("coins_goods_type") != null) {
				v.setType(CoinsGoodsType.findByValue(Integer.parseInt(m.get("coins_goods_type").toString())));
			}
			v.setTypeAmount(Long.valueOf(m.get("amount").toString()));
			v.setTypeTotalPrice((BigDecimal) m.get("total_price"));

			datas.add(v);
		}

		retValue.put("allDatas", datas);

		return new Value(retValue);
	}

	/**
	 * 根据商品进行分类统计
	 *
	 * @param beginDate
	 *            查询开始时间
	 * @param endDate
	 *            查询结束时间
	 * @param type
	 *            {@link CoinsGoodsType}
	 * @param page
	 *            当前页
	 * @param size
	 *            每页大小
	 * @return {@link Value}
	 */
	@RequestMapping(value = "statisticByName", method = { RequestMethod.GET, RequestMethod.POST })
	public Value statisticByName(String beginDate, String endDate, Integer type, int page, int size) {
		Pageable pageable = P.index(page, size);
		Page<Map> resultPage = coinsGoodsOrderService.statisticByName(beginDate, endDate, type, pageable);
		VPage<VZycCoinsTypeData> retPage = new VPage<VZycCoinsTypeData>();
		List<VZycCoinsTypeData> items = new ArrayList<VZycCoinsTypeData>(resultPage.getItems().size());

		for (Map m : resultPage.getItems()) {
			VZycCoinsTypeData v = new VZycCoinsTypeData();
			v.setName((String) m.get("good_name"));
			v.setTypeAmount(Long.parseLong(m.get("amount").toString()));
			v.setTypeTotalPrice((BigDecimal) m.get("total_price"));

			items.add(v);
		}

		retPage.setItems(items);
		retPage.setCurrentPage(page);
		retPage.setPageSize(size);
		retPage.setTotal(resultPage.getTotalCount());
		retPage.setTotalPage(resultPage.getPageCount());
		return new Value(retPage);
	}
}

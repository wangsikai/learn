package com.lanking.uxb.service.mall.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaper;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.goods.Goods;
import com.lanking.cloud.domain.yoo.goods.GoodsSnapshot;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoods;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsSnapshot;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsStatus;
import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.member.UserMember;
import com.lanking.cloud.domain.yoo.order.GoodsOrderStatus;
import com.lanking.cloud.domain.yoo.order.PayMode;
import com.lanking.cloud.domain.yoo.order.resources.ResourcesGoodsOrder;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.examPaper.api.ExamPaperService;
import com.lanking.uxb.service.honor.api.UserHonorService;
import com.lanking.uxb.service.mall.api.GoodsService;
import com.lanking.uxb.service.mall.api.GoodsSnapshotService;
import com.lanking.uxb.service.mall.api.ResourcesGoodsOrderService;
import com.lanking.uxb.service.mall.api.ResourcesGoodsService;
import com.lanking.uxb.service.mall.api.ResourcesGoodsSnapshotService;
import com.lanking.uxb.service.mall.business.ExampaperOrderSource;
import com.lanking.uxb.service.mall.ex.ResourcesGoodsPaymentException;
import com.lanking.uxb.service.payment.weixin.api.WXPaymentService;
import com.lanking.uxb.service.payment.weixin.response.OrderQueryResult;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.UserMemberService;

/**
 * 资源商品-精品组卷支付相关.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月2日
 */
@RestController
@RequestMapping("zy/mall/pay/exampaper")
@RolesAllowed(userTypes = { "TEACHER" })
public class ZyExampaperPaymentController extends AbstractResourcesGoodsPaymentController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ResourcesGoodsOrderService resourcesGoodsOrderService;
	@Autowired
	private WXPaymentService wxPaymentService;
	@Autowired
	private UserHonorService userHonorService;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private GoodsSnapshotService goodsSnapshotService;
	@Autowired
	private ResourcesGoodsService resourcesGoodsService;
	@Autowired
	private ResourcesGoodsSnapshotService resourcesGoodsSnapshotService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private ExamPaperService examPaperService;
	@Autowired
	private UserMemberService userMemberService;

	private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

	/**
	 * 创建订单.<br>
	 * 若该资源试卷已被购买过，则不再生成订单.
	 * 
	 * @param resourcesGoodsID
	 *            资源商品ID（同goodID）
	 * @param payMod
	 *            支付方式
	 * @param paymentPlatformCode
	 *            支付平台代码
	 * @param attachData
	 *            附加数据
	 */
	@RequestMapping("createOrader")
	public Value createOrader(Long resourcesGoodsID, PayMode payMod, Integer paymentPlatformCode,
			ExampaperOrderSource source, String attachData) {
		long userID = Security.getUserId();
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("err", 0);

		// 查找商品
		ResourcesGoods resourcesGoods = resourcesGoodsService.get(resourcesGoodsID);
		if (resourcesGoods.getStatus() != ResourcesGoodsStatus.PUBLISH) {
			// 该商品已下架或被删除
			map.put("err", ResourcesGoodsPaymentException.RESGODDS_UNPUBLISH);
			return new Value(map);
		}

		Goods goods = goodsService.get(resourcesGoodsID);

		// 查找订单
		List<ResourcesGoodsOrder> orders = resourcesGoodsOrderService.findCompleteOrderByUserAndGoods(userID,
				resourcesGoodsID);
		if (orders.size() > 0) {
			// 表示该订单已经支付完成
			map.put("err", ResourcesGoodsPaymentException.RESGODDS_PAY_COMPLETE);
			map.put("orderID", orders.get(0).getId());
			return new Value(map);
		}

		// 若金币购买，需要校验金币
		if (payMod == PayMode.COINS) {
			UserHonor honor = userHonorService.getUserHonor(userID);
			if (honor == null || honor.getCoins() < goods.getPrice().intValue()) {
				// 金币余额不足
				map.put("err", ResourcesGoodsPaymentException.RESGODDS_PAY_COINS_NOT_ENOUGH);
				map.put("coins", honor.getCoins());
				return new Value(map);
			}
		}

		// 创建订单
		JSONObject attachDataJSON = new JSONObject();
		attachDataJSON.put("attach", attachData);
		attachDataJSON.put("source", source);
		ResourcesGoodsOrder resourcesGoodsOrder = resourcesGoodsOrderService.createOrder(userID, resourcesGoodsID,
				payMod, paymentPlatformCode, attachDataJSON.toJSONString());

		// 在线支付免费订单
		if (resourcesGoodsOrder.getTotalPrice().doubleValue() == 0 && payMod == PayMode.ONLINE) {
			// 表示该订单已经支付完成
			map.put("err", ResourcesGoodsPaymentException.RESGODDS_PAY_COMPLETE);
			map.put("orderID", resourcesGoodsOrder.getId());
			return new Value(map);
		}

		map.put("orderID", resourcesGoodsOrder.getId());
		return new Value(map);
	}

	/**
	 * 查询微信订单状态.
	 * 
	 * @param resourcesGoodsOrderID
	 *            本地资源订单ID
	 * @return
	 */
	@RequestMapping("queryWXOrder")
	public Value queryWXOrder(Long resourcesGoodsOrderID) {
		if (null == resourcesGoodsOrderID) {
			return new Value(new MissingArgumentException());
		}
		try {
			final SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddHHmmss");
			String appid = Env.getString("weixin.pay.appid");
			OrderQueryResult result = wxPaymentService.orderQuery(appid, null, String.valueOf(resourcesGoodsOrderID));
			final String return_code = result.getReturnCode(); // 返回状态
			String trade_state = result.getTradeState(); // 交易状态
			final String outTradeNo = result.getOutTradeNo(); // 本地订单ID
			final String timeEnd = result.getTimeEnd(); // 交易付款时间
			final String transactionId = result.getTransactionId(); // 微信支付订单号

			if ("SUCCESS".equalsIgnoreCase(return_code) && ("SUCCESS".equalsIgnoreCase(trade_state))) {
				// 交易成功

				fixedThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						Date payTime = null;
						try {
							payTime = formate.parse(timeEnd);
						} catch (ParseException e) {
							payTime = new Date();
							logger.error(e.getMessage(), e);
						}

						// 支付订单
						resourcesGoodsOrderService.updatePaymentCallback(Long.parseLong(outTradeNo), transactionId,
								null, payTime);

						// 完成订单
						resourcesGoodsOrderService.updateOrderStatus(Long.parseLong(outTradeNo), null,
								GoodsOrderStatus.COMPLETE);
					}
				});
				return new Value(1);
			} else if ("REVOKED".equalsIgnoreCase(trade_state) || "PAYERROR".equalsIgnoreCase(trade_state)
					|| "CLOSED".equalsIgnoreCase(trade_state)) {
				// 交易失败
				fixedThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						resourcesGoodsOrderService.updateOrderStatus(Long.parseLong(outTradeNo), null,
								GoodsOrderStatus.FAIL);
					}
				});
			} else {
				return new Value(2);
			}
		} catch (Exception e) {
			logger.error("查询微信订单状态出错！", e);
			return new Value(new ServerException());
		}
		return new Value(0);
	}

	/**
	 * 校验试卷商品购买状态.
	 * 
	 * @param goodsID
	 * @return buy：0：未购买（商品不免费），1：已购买，2：未购买（商品免费）
	 */
	@RequestMapping("checkGoodsOrder")
	public Value checkGoodsOrder(Long goodsID) {
		if (goodsID == null) {
			return new Value(new MissingArgumentException());
		}
		long userID = Security.getUserId();
		int buy = 0;

		// 检查商品状态
		Goods goods = goodsService.get(goodsID);
		if (goods == null) {
			return new Value(new MissingArgumentException());
		}

		// 查找订单
		List<ResourcesGoodsOrder> orders = resourcesGoodsOrderService.findCompleteOrderByUserAndGoods(userID, goodsID);
		if (orders.size() > 0) {
			ResourcesGoodsOrder order = orders.get(0);
			if (order.getStatus() == GoodsOrderStatus.COMPLETE) {
				// 已完成购买
				buy = 1;
			}
		}

		if (buy == 0 && (goods.getPrice().intValue() == 0 || goods.getPriceRMB().doubleValue() == 0)) {
			// 商品未购买但是免费
			buy = 2;
		}

		// 会员免费使用
		UserMember userMember = userMemberService.findByUserId(Security.getUserId());
		if (userMember != null) {
			try {
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
				Date today = format.parse(format.format(new Date()));
				Date start = format.parse(format.format(userMember.getStartAt()));
				Date end = format.parse(format.format(userMember.getEndAt()));
				if (start.compareTo(today) <= 0 && end.compareTo(today) >= 0) {
					buy = 2;
				}
			} catch (ParseException e) {
				logger.error(e.getMessage(), e);
			}
		}

		return new Value(buy);
	}

	/**
	 * 购买页数据.
	 * 
	 * @param goodsID
	 *            商品ID
	 * @return
	 */
	@RequestMapping("buyIndex")
	public Value buyIndex(Long goodsID) {
		if (null == goodsID || goodsID < 1) {
			return new Value(new MissingArgumentException());
		}

		Goods goods = goodsService.get(goodsID);
		ResourcesGoods resourcesGoods = resourcesGoodsService.get(goodsID);
		if (goods == null) {
			return new Value(new EntityNotFoundException());
		}
		ExamPaper examPaper = examPaperService.get(resourcesGoods.getResourcesId());

		Map<String, Object> map = new HashMap<String, Object>(6);
		map.put("id", goodsID);
		map.put("paperId", resourcesGoods.getResourcesId()); // 试卷ID
		map.put("name", examPaper.getName()); // 试卷名称
		map.put("category", resourcesGoods.getCategory()); // 试卷类型
		map.put("recommend", resourcesGoods.getRecommendReason()); // 推荐理由
		map.put("price", goods.getPrice() == null ? 0 : goods.getPrice().intValue()); // 金币
		map.put("priceRMB", goods.getPriceRMB() == null ? 0 : goods.getPriceRMB().doubleValue()); // 现金

		// 用户信息
		Account account = accountService.getAccount(Security.getAccountId());
		UserHonor honor = userHonorService.getUserHonor(Security.getUserId());
		if (StringUtils.isBlank(account.getEmail()) && StringUtils.isBlank(account.getMobile())) {
			map.put("needImprove", true);
		} else {
			map.put("needImprove", false);
		}
		map.put("userCoins", honor == null ? 0 : honor.getCoins());

		return new Value(map);
	}

	/**
	 * 订单支付完成.
	 * 
	 * @param orderID
	 *            订单ID
	 * @return
	 */
	@RequestMapping("complete")
	public Value complete(Long orderID) {
		if (null == orderID || orderID < 1) {
			return new Value(new MissingArgumentException());
		}
		ResourcesGoodsOrder resourcesGoodsOrder = resourcesGoodsOrderService.get(orderID);
		if (resourcesGoodsOrder == null) {
			return new Value(new EntityNotFoundException());
		}
		if (resourcesGoodsOrder.getUserId() != Security.getUserId()) {
			return new Value(new EntityNotFoundException());
		}

		long goodsSnapshotId = resourcesGoodsOrder.getGoodsSnapshotId();
		long resourcesGoodsSnapshotId = resourcesGoodsOrder.getResourcesGoodsSnapshotId();
		GoodsSnapshot goodsSnapshot = goodsSnapshotService.get(goodsSnapshotId);
		ResourcesGoodsSnapshot resourcesGoodsSnapshot = resourcesGoodsSnapshotService.get(resourcesGoodsSnapshotId);
		ExamPaper examPaper = examPaperService.get(resourcesGoodsSnapshot.getResourcesId());
		goodsSnapshot.setName(examPaper.getName());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", resourcesGoodsOrder.getStatus());
		map.put("totalPrice", resourcesGoodsOrder.getPayMod() == PayMode.ONLINE ? resourcesGoodsOrder.getTotalPrice()
				.doubleValue() : resourcesGoodsOrder.getTotalPrice().intValue());
		map.put("attach", resourcesGoodsOrder.getAttachData());
		map.put("payMod", resourcesGoodsOrder.getPayMod());
		map.put("goodsSnapshot", goodsSnapshot);
		map.put("resourcesGoods", resourcesGoodsSnapshot);

		if (StringUtils.isNotBlank(resourcesGoodsOrder.getAttachData())) {
			JSONObject attachDataJSON = JSONObject.parseObject(resourcesGoodsOrder.getAttachData());
			map.put("attach", attachDataJSON.getString("attach"));
			map.put("source", attachDataJSON.getString("source"));
		}
		return new Value(map);
	}
}

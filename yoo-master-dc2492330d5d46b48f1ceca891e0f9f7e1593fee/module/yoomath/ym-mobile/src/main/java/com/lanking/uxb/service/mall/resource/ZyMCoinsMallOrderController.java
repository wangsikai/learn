package com.lanking.uxb.service.mall.resource;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.base.message.api.SmsPacket;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoods;
import com.lanking.cloud.domain.yoo.order.GoodsOrderStatus;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrder;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrderSource;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.VCursorPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.account.util.ValidateUtils;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.mall.api.CoinsGoodsOrderQuery;
import com.lanking.uxb.service.mall.api.CoinsGoodsOrderService;
import com.lanking.uxb.service.mall.api.CoinsGoodsService;
import com.lanking.uxb.service.mall.cache.GoodsValidCodeCacheService;
import com.lanking.uxb.service.mall.convert.CoinsGoodsConvert;
import com.lanking.uxb.service.mall.convert.CoinsGoodsConvertOption;
import com.lanking.uxb.service.mall.convert.CoinsGoodsOrderConvert;
import com.lanking.uxb.service.mall.convert.CoinsGoodsOrderConvertOption;
import com.lanking.uxb.service.mall.form.CoinsGoodsOrderForm;
import com.lanking.uxb.service.mall.value.VCoinsGoodsOrder;
import com.lanking.uxb.service.message.api.MessageSender;
import com.lanking.uxb.service.message.util.VerifyCodes;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;

/**
 * 金币商城订单相关接口
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月15日
 */
@RestController
@RequestMapping("zy/m/mall/coins/order")
public class ZyMCoinsMallOrderController {

	private Logger logger = LoggerFactory.getLogger(ZyMCoinsMallOrderController.class);

	@Autowired
	private CoinsGoodsService coinsGoodsService;
	@Autowired
	private CoinsGoodsConvert coinsGoodsConvert;
	@Autowired
	private CoinsGoodsOrderService coinsGoodsOrderService;
	@Autowired
	private CoinsGoodsOrderConvert coinsGoodsOrderConvert;
	@Autowired
	@Qualifier("accountService")
	private AccountService accountService;
	@Autowired
	private GoodsValidCodeCacheService goodsValidCodeCacheService;
	@Autowired
	private MessageSender messageSender;

	@SuppressWarnings("unchecked")
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "query", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryCoinsOrder(Long cursor, CoinsGoodsOrderSource orderSource) {
		CoinsGoodsOrderQuery query = new CoinsGoodsOrderQuery();
		query.setUserId(Security.getUserId());
		query.setOrderSource(orderSource);
		CursorPage<Long, CoinsGoodsOrder> cpage = coinsGoodsOrderService.query(query,
				CP.cursor(cursor == null ? Long.MAX_VALUE : cursor, 20));
		VCursorPage<VCoinsGoodsOrder> vpage = new VCursorPage<VCoinsGoodsOrder>();
		if (cpage.isEmpty()) {
			vpage.setCursor(cursor == null ? Long.MAX_VALUE : cursor);
			vpage.setItems(Collections.EMPTY_LIST);
		} else {
			vpage.setCursor(cpage.getNextCursor());
			List<VCoinsGoodsOrder> orders = coinsGoodsOrderConvert.to(cpage.getItems(),
					new CoinsGoodsOrderConvertOption(true));
			vpage.setItems(orders);
		}
		return new Value(vpage);
	}

	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Value deleteCoinsOrder(long id) {
		try {
			coinsGoodsOrderService.delete(id);
			return new Value();
		} catch (ZuoyeException e) {
			return new Value(new ServerException());
		}
	}

	private void orderSms(String mobile) {
		String authCode = goodsValidCodeCacheService.getMobileCode(Security.getToken(), Security.getToken());
		if (StringUtils.isBlank(authCode)) {
			authCode = VerifyCodes.smsCode(6);
			messageSender.send(new SmsPacket(mobile, 10000021, ValueMap.value("authCode", authCode)));
			goodsValidCodeCacheService.setMobileCode(Security.getToken(), Security.getToken(), authCode, 1,
					TimeUnit.MINUTES);
			goodsValidCodeCacheService.setMobileCode(Security.getToken(), mobile, authCode, 5, TimeUnit.MINUTES);
			logger.info("sms code is:{}", authCode);
		} else {
			logger.info("last sms code is:{}", authCode);
		}
	}

	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "toCreate", method = { RequestMethod.POST, RequestMethod.GET })
	public Value toCreate(long coinsGoodsId) {
		CoinsGoods coinsGoods = coinsGoodsService.get(coinsGoodsId);
		if (coinsGoods.getDayBuyCount() > -1 && coinsGoodsOrderService.countTodayCoinsGoodsBuyCount(coinsGoodsId,
				Security.getUserId()) >= coinsGoods.getDayBuyCount()) {// 每人每天购买限制
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_GOODS_BUY_LIMIT,
					coinsGoods.getDayBuyCount()));
		}

		Map<String, Object> map = new HashMap<String, Object>(2);
		Account account = accountService.getAccount(Security.getAccountId());
		if (StringUtils.isNotBlank(account.getMobile())) {
			map.put("mobile", account.getMobile());
			orderSms(account.getMobile());
		}
		map.put("coinsGoods",
				coinsGoodsConvert.to(coinsGoodsService.get(coinsGoodsId), new CoinsGoodsConvertOption(true)));
		return new Value(map);
	}

	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "sendSms", method = { RequestMethod.POST, RequestMethod.GET })
	public Value sendSms(String mobile) {
		try {
			ValidateUtils.validateMobile(mobile);
		} catch (AccountException ex) {
			return new Value(new IllegalArgException());
		}
		Account bindMobileAccount = accountService.getAccount(GetType.MOBILE, mobile);
		if (bindMobileAccount == null) {
			orderSms(mobile);
			return new Value();
		} else {
			if (bindMobileAccount.getId().longValue() != Security.getAccountId()) {
				return new Value(
						new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_MOBILE_HAS_BIND_ACCOUNT));
			} else {
				orderSms(mobile);
				return new Value();
			}
		}
	}

	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "create", method = { RequestMethod.POST, RequestMethod.GET })
	public Value create(CoinsGoodsOrderForm form) {
		if (form == null || StringUtils.isBlank(form.getBindMobile())) {
			return new Value(new IllegalArgException());
		}
		if (StringUtils.isBlank(form.getMobile()) && StringUtils.isBlank(form.getQq())
				&& StringUtils.isBlank(form.getMemo())) {
			return new Value(new IllegalArgException());
		}
		String bindMobile = form.getBindMobile();
		boolean check = true;
		String cacheAuthCode = goodsValidCodeCacheService.getMobileCode(Security.getToken(), bindMobile);
		if (StringUtils.isBlank(cacheAuthCode)) {
			check = false;
		} else {
			check = cacheAuthCode.equals(form.getValidCode());
		}
		if (check) {
			Account bindMobileAccount = accountService.getAccount(GetType.MOBILE, bindMobile);
			if (bindMobileAccount == null) {
				Account account = accountService.getAccount(Security.getAccountId());
				if (StringUtils.isNotBlank(account.getMobile())) {
					form.setBindMobile(null);
				}
			} else {
				form.setBindMobile(null);
			}
			form.setBindEmail(null);
			// 兑换
			form.setAmount(1);
			form.setMobile(form.getMobile());
			form.setQq(form.getQq());
			form.setUserId(Security.getUserId());
			form.setUserType(Security.getUserType());
			form.setStatus(GoodsOrderStatus.PAY);
			// 创建订单
			try {
				coinsGoodsOrderService.createOrder(form);
				goodsValidCodeCacheService.invalidMobileCode(Security.getToken(), bindMobile);
				// 清理缓存
				goodsValidCodeCacheService.invalidMobileCode(Security.getToken(), Security.getToken());
				goodsValidCodeCacheService.invalidMobileCode(Security.getToken(), form.getBindMobile());
				return new Value();
			} catch (ZuoyeException | NoPermissionException e) {
				if (e.getCode() == ZuoyeException.ZUOYE_COINS_NOT_ENOUGH) {
					return new Value(
							new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_COINS_NOT_ENOUGH));
				} else if (e.getCode() == ZuoyeException.ZUOYE_GOODS_SELL_OUT) {
					return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_GOODS_SELL_OUT));
				} else if (e.getCode() == ZuoyeException.ZUOYE_GOODS_NOTIN_SALESTIME) {
					return new Value(
							new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_GOODS_NOTIN_SALESTIME));
				} else if (e.getCode() == ZuoyeException.ZUOYE_GOODS_SOLDOUT) {
					return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_GOODS_SOLDOUT));
				} else if (e.getCode() == ZuoyeException.ZUOYE_GOODS_BUY_LIMIT) {
					CoinsGoods coinsGoods = coinsGoodsService.get(form.getCoinsGoodsId());
					return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_GOODS_BUY_LIMIT,
							coinsGoods.getDayBuyCount()));
				} else if (e.getCode() == NoPermissionException.NO_PERMISSION_EX) {
					return new Value(e);
				} else {
					return new Value(new ServerException());
				}
			}
		} else {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_WRONG_AUTHCODE));
		}
	}
}

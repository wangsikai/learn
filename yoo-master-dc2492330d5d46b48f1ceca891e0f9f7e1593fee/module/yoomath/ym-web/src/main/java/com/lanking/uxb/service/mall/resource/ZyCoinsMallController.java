package com.lanking.uxb.service.mall.resource;

import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.uxb.service.mall.api.CoinsGoodsService;
import com.lanking.uxb.service.mall.convert.CoinsGoodsConvert;
import com.lanking.uxb.service.mall.convert.CoinsGoodsConvertOption;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.convert.UserConvert;

import java.util.HashMap;
import java.util.Map;

/**
 * 金币商城相关接口
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月15日
 */
@RestController
@RequestMapping("zy/mall")
public class ZyCoinsMallController {
	@Autowired
	private CoinsGoodsService coinsGoodsService;
	@Autowired
	private CoinsGoodsConvert coinsGoodsConvert;
	@Autowired
	private UserConvert userConvert;

	/**
	 * 查看商品详情
	 *
	 * @param id
	 *            商品id
	 * @return {@link Value}
	 */
	@RequestMapping(value = "view", method = { RequestMethod.GET, RequestMethod.POST })
	public Value view(long id) {
		return new Value(coinsGoodsConvert.to(coinsGoodsService.get(id), new CoinsGoodsConvertOption(true)));
	}

	/**
	 * 新版查看商品详情接口<br/>
	 *
	 * redirectUrl -> 跳转至悠数学绑定页面
	 *
	 * @since 2.0.3
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "2/view", method = { RequestMethod.GET, RequestMethod.POST })
	public Value view2(long id) {
		Map<String, Object> retMap = new HashMap<String, Object>(2);
		retMap.put("goods", coinsGoodsConvert.to(coinsGoodsService.get(id), new CoinsGoodsConvertOption(true)));
		retMap.put("redirectUrl", Env.getString("mall.redirect.yoomath.mobile", new Object[] { Security.getUserId() }));

		return new Value(retMap);
	}

	/**
	 * 当用户金币不足时,查看"如何赚取金币" 链接
	 *
	 * @return {@link Value}
	 */
	@RequestMapping(value = "getYoomathRuleCoinsUrl", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getYoomathRuleCoinsUrl() {
		String redirectUrl;
		if (Security.getUserType() == UserType.TEACHER) {
			redirectUrl = Env.getString("mall.tea.redirect.yoomath.coinsRule");
		} else {
			redirectUrl = Env.getString("mall.stu.redirect.yoomath.coinsRule");
		}
		return new Value(redirectUrl);
	}

	/**
	 * 悠数学首页链接
	 *
	 * @return {@link Value}
	 */
	@RequestMapping(value = "getYoomathUrl", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getYoomathUrl() {
		String redirectUrl;
		if (Security.getUserType() == UserType.TEACHER) {
			redirectUrl = Env.getString("mall.tea.redirect.yoomath.home");
		} else {
			redirectUrl = Env.getString("mall.stu.redirect.yoomath.home");
		}
		return new Value(redirectUrl);
	}
}

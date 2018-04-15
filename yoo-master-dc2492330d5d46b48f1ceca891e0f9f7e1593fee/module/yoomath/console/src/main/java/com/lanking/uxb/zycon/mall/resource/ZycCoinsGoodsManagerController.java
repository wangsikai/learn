package com.lanking.uxb.zycon.mall.resource;

import httl.util.StringUtils;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoods;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsStatus;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.zycon.mall.api.ZycCoinsGoodsService;
import com.lanking.uxb.zycon.mall.convert.ZycCoinsGoodsConvert;
import com.lanking.uxb.zycon.mall.form.CoinsGoodsForm;
import com.lanking.uxb.zycon.mall.value.VZycGoods;

/**
 * 后台金币兑换商品管理Controller
 * 
 * @author zdy
 *
 */
@RestController
@RequestMapping(value = "zyc/goods/coins/")
public class ZycCoinsGoodsManagerController {

	@Autowired
	private ZycCoinsGoodsService zycCoinGoodsService;

	@Autowired
	private ZycCoinsGoodsConvert cgConvert;

	private static Logger logger = Logger.getLogger(ZycCoinsGoodsManagerController.class);

	private Object lock = new Object();

	/**
	 * 商品列表查询
	 * 
	 * @return
	 *
	 * @author zdy
	 */
	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public Value query(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "20") int size,
			@RequestParam(value = "virtualGoodsType") CoinsGoodsType coinsGoodsType,
			@RequestParam(value = "userType", defaultValue = "111") String useTypeStr) {

		// 先处理自动上架和自动上架的商品排序
		if (page == 1) {
			reviewSequence(coinsGoodsType);
		}
		int userType = Integer.parseInt(useTypeStr, 2);
		Page<CoinsGoods> cp = zycCoinGoodsService.queryCoinsGoods(coinsGoodsType, userType, P.index(page, size));
		VPage<VZycGoods> vp = new VPage<VZycGoods>();
		int tPage = (int) (cp.getTotalCount() + size - 1) / size;
		vp.setPageSize(size);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		vp.setItems(cgConvert.to(cp.getItems()));
		return new Value(vp);
	}

	/**
	 * 对于自动下架的产品进行重新排序，在刷新页面时候
	 *
	 * @author zdy
	 */
	private synchronized void reviewSequence(CoinsGoodsType coinsGoodsType) {
		try {
			// 把自动上架的商品的seqence填充
			doDealSequenceOfNull(0, coinsGoodsType);
			doDealSequenceOfNull(1, coinsGoodsType);
			doDealSequenceOfNull(2, coinsGoodsType);

			// 处理自动下架的商品
			dealSequenceOffGoods(0, coinsGoodsType);
			dealSequenceOffGoods(1, coinsGoodsType);
			dealSequenceOffGoods(2, coinsGoodsType);
		} catch (Exception e) {
			logger.error(getClass().getSimpleName() + ".query reviewSequence", e);
		}

	}

	private void dealSequenceOffGoods(int _index, CoinsGoodsType coinsGoodsType) {
		List<CoinsGoods> offGoods = zycCoinGoodsService.getSequenceOffGoods(_index, coinsGoodsType);

		if (offGoods == null || offGoods.size() == 0) {
			return;
		}
		for (CoinsGoods goods : offGoods) {
			zycCoinGoodsService.dealBatchMoveSequence(goods, _index);
		}
	}

	private void doDealSequenceOfNull(int _index, CoinsGoodsType coinsGoodsType) {
		// 查询出所有上架商品 ，但是seq为空的商品
		List<CoinsGoods> nullSeqlist = zycCoinGoodsService.getSalingGoodsOfNullSequence(_index, coinsGoodsType);

		if (nullSeqlist == null || nullSeqlist.size() == 0) {
			return;
		}
		for (CoinsGoods goods : nullSeqlist) {
			zycCoinGoodsService.dealSequenceOfNull(goods, _index);
		}
	}

	/**
	 * 发布商品
	 * 
	 * @param goodsJson
	 * @return
	 *
	 * @author zdy
	 */
	@RequestMapping(value = "publish", method = { RequestMethod.GET, RequestMethod.POST })
	public Value publishGoods(String goodsJson) {
		try {
			CoinsGoodsForm goods = JSON.parseObject(goodsJson, CoinsGoodsForm.class);
			goods.setStatus(CoinsGoodsStatus.PUBLISH);
			if (goods.getSalesTime().before(new Date()) && goods.getSoldOutTime().after(new Date())) {
				synchronized (lock) {// 如果是在发布，直接上架的，需要补充坑位，这里需要同步
					zycCoinGoodsService.saveCoinsGoods(goods);
				}
			} else {
				zycCoinGoodsService.saveCoinsGoods(goods);
			}

		} catch (Exception e) {
			logger.error(getClass().getSimpleName() + ".publishGoods exception", e);
			return new Value(new ServerException(e));
		}

		return new Value();
	}

	/**
	 * 保存商品
	 * 
	 * @return
	 *
	 * @author zdy
	 */
	@RequestMapping(value = "save", method = { RequestMethod.GET, RequestMethod.POST })
	public Value saveGoods(String goodsJson) {
		try {
			CoinsGoodsForm goods = JSON.parseObject(goodsJson, CoinsGoodsForm.class);
			goods.setStatus(CoinsGoodsStatus.DRAFT);
			zycCoinGoodsService.saveCoinsGoods(goods);
		} catch (Exception e) {
			logger.error(getClass().getSimpleName() + ".saveGoods exception", e);
			return new Value(new ServerException(e));
		}

		return new Value();
	}

	/**
	 * 删除商品
	 * 
	 * @return
	 *
	 * @author zdy
	 */
	@RequestMapping(value = "deleteGoods", method = { RequestMethod.GET, RequestMethod.POST })
	public Value deleteGoods(Long goodsId) {
		try {
			zycCoinGoodsService.updateCoinsGoodsStatus(goodsId, CoinsGoodsStatus.DELETE);
		} catch (Exception e) {
			logger.error(getClass().getSimpleName() + ".deleteGoods exception", e);
			return new Value(new ServerException(e));
		}

		return new Value();
	}

	/**
	 * 下架操作
	 * 
	 * @return
	 *
	 * @author zdy
	 */
	@RequestMapping(value = "offGoods", method = { RequestMethod.GET, RequestMethod.POST })
	public Value offGoods(Long goodsId) {
		try {
			zycCoinGoodsService.updateCoinsGoodsStatus(goodsId, CoinsGoodsStatus.UN_PUBLISH);
		} catch (Exception e) {
			logger.error(getClass().getSimpleName() + ".offGoods exception", e);
			return new Value(new ServerException(e));
		}

		return new Value();
	}

	/**
	 * 商品上移,下移
	 * 
	 * @return
	 *
	 * @author zdy
	 */
	@RequestMapping(value = "moveSequence", method = { RequestMethod.GET, RequestMethod.POST })
	public Value moveSequence(Long goodsId, Integer dic, String userTypeStr) {
		try {
			if (StringUtils.isNotBlank(userTypeStr)) {
				int userType = Integer.parseInt(userTypeStr, 2);
				zycCoinGoodsService.exchangeSequenceOfCoinsGoods(goodsId, dic, userType);
			}
		} catch (Exception e) {
			logger.error(getClass().getSimpleName() + ".offGoods exception", e);
			return new Value(new ServerException(e));
		}

		return new Value();
	}

	/**
	 * 商品预览
	 * 
	 * @return
	 *
	 * @author zdy
	 */
	@RequestMapping(value = "detail", method = { RequestMethod.GET, RequestMethod.POST })
	public Value detail(Long goodsId) {
		CoinsGoods cg = zycCoinGoodsService.get(goodsId);
		VZycGoods goods = cgConvert.to(cg);
		return new Value(goods);
	}

	/**
	 * 查询上架再售商品
	 * 
	 * @param coinsGoodsType
	 * @param goodsType
	 * @param useTypeStr
	 * @return
	 *
	 * @author zdy
	 */
	@RequestMapping(value = "getSalingGoodsCount", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getSalingGoodsCount(@RequestParam(value = "virtualGoodsType") CoinsGoodsType coinsGoodsType,
			@RequestParam(value = "userType", defaultValue = "111") String useTypeStr) {

		Integer count = zycCoinGoodsService.getSalingGoodsCount(coinsGoodsType, useTypeStr);
		return new Value(count);
	}
}

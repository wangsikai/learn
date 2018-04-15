package com.lanking.uxb.service.mall.convert;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.goods.Goods;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;
import com.lanking.cloud.domain.yoo.order.GoodsOrderStatus;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrder;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrderSource;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.service.mall.api.CoinsGoodsService;
import com.lanking.uxb.service.mall.api.CoinsLotteryGoodsService;
import com.lanking.uxb.service.mall.api.GoodsService;
import com.lanking.uxb.service.mall.value.VCoinsGoods;
import com.lanking.uxb.service.mall.value.VCoinsGoodsOrder;
import com.lanking.uxb.service.mall.value.VCoinsLotteryGoods;

/**
 * 金币商品订单convert
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月14日
 */
@Component
public class CoinsGoodsOrderConvert extends Converter<VCoinsGoodsOrder, CoinsGoodsOrder, Long> {

	@Autowired
	private GoodsService goodsService;
	@Autowired
	private GoodsConvert goodsConvert;
	@Autowired
	private CoinsGoodsConvert coinsGoodsConvert;
	@Autowired
	private CoinsGoodsService coinsGoodsService;
	@Autowired
	private CoinsLotteryGoodsService coinsLotteryGoodsService;
	@Autowired
	private CoinsLotteryGoodsConvert coinsLotteryGoodsConvert;

	public VCoinsGoodsOrder to(CoinsGoodsOrder s, CoinsGoodsOrderConvertOption option) {
		s.setInitCoinsGoods(option.isInitCoinsGoods());
		return super.to(s);
	}

	public List<VCoinsGoodsOrder> to(List<CoinsGoodsOrder> ss, CoinsGoodsOrderConvertOption option) {
		for (CoinsGoodsOrder s : ss) {
			s.setInitCoinsGoods(option.isInitCoinsGoods());
		}
		return super.to(ss);
	}

	@Override
	protected Long getId(CoinsGoodsOrder s) {
		return s.getId();
	}

	@Override
	protected VCoinsGoodsOrder convert(CoinsGoodsOrder s) {
		VCoinsGoodsOrder v = new VCoinsGoodsOrder();
		v.setId(s.getId());
		v.setSellerNotes(validBlank(s.getSellerNotes()));
		v.setCoinsGoodsId(s.getCoinsGoodsId());
		v.setCoinsGoodsSnapshotId(s.getCoinsGoodsSnapshotId());
		v.setGoodsId(s.getGoodsId());
		v.setGoodsSnapshotId(s.getGoodsSnapshotId());
		v.setOrderAt(s.getOrderAt());
		v.setStatus(s.getStatus());
		v.setSource(s.getSource());
		if (s.getSource() == CoinsGoodsOrderSource.LUCKY_DRAW) {
			v.setSourceTitle(s.getSource().getTitle());
		} else if (s.getSource() == CoinsGoodsOrderSource.LOTTERY_ACTIVITY
				|| s.getSource() == CoinsGoodsOrderSource.HOLIDAY_ACTIVITY_01) {
			v.setSourceTitle("活动");
		}
		v.setTotalPrice(s.getTotalPrice());
		v.setTotalCoinsPrice(s.getTotalPrice().intValue());
		if (v.getStatus() == GoodsOrderStatus.FAIL) {
			v.setStatusTitle("兑换失败");
		} else if (v.getStatus() == GoodsOrderStatus.COMPLETE) {
			v.setStatusTitle("兑换成功");
		} else if (v.getStatus() == GoodsOrderStatus.PAY || v.getStatus() == GoodsOrderStatus.PROCESSING) {
			v.setStatusTitle("兑换中");
		} else {
			v.setStatusTitle("未知");
		}
		v.setP0(validBlank(s.getP0()));
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 转化商品信息
		assemblers.add(new ConverterAssembler<VCoinsGoodsOrder, CoinsGoodsOrder, Long, Goods>() {

			@Override
			public boolean accept(CoinsGoodsOrder s) {
				return s.isInitCoinsGoods() && s.getSource() != CoinsGoodsOrderSource.EXCHANGE
						&& s.getSource() != CoinsGoodsOrderSource.LUCKY_DRAW;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(CoinsGoodsOrder s, VCoinsGoodsOrder d) {
				return s.getGoodsId();
			}

			@Override
			public void setValue(CoinsGoodsOrder s, VCoinsGoodsOrder d, Goods value) {
				d.setGoods(goodsConvert.to(value));
				if (StringUtils.isBlank(d.getGoods().getImageUrl())
						&& s.getSource() == CoinsGoodsOrderSource.LOTTERY_ACTIVITY) {
					d.getGoods().setImageUrl(FileUtil.getUrl(Env.getDynamicLong("lottery.goods.img")));
					d.getGoods().setImageMidUrl(FileUtil.getUrl(Env.getDynamicLong("lottery.goods.img")));
					d.getGoods().setImageMinUrl(FileUtil.getUrl(Env.getDynamicLong("lottery.goods.img")));
				}
			}

			@Override
			public Goods getValue(Long key) {
				return goodsService.get(key);
			}

			@Override
			public Map<Long, Goods> mgetValue(Collection<Long> keys) {
				return goodsService.mget(keys);
			}

		});

		// 转化金币商品
		assemblers.add(new ConverterAssembler<VCoinsGoodsOrder, CoinsGoodsOrder, Long, VCoinsGoods>() {
			@Override
			public boolean accept(CoinsGoodsOrder coinsGoodsOrder) {
				return coinsGoodsOrder.isInitCoinsGoods()
						&& coinsGoodsOrder.getSource() == CoinsGoodsOrderSource.EXCHANGE;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(CoinsGoodsOrder coinsGoodsOrder, VCoinsGoodsOrder vCoinsGoodsOrder) {
				return coinsGoodsOrder.getCoinsGoodsId();
			}

			@Override
			public void setValue(CoinsGoodsOrder coinsGoodsOrder, VCoinsGoodsOrder d, VCoinsGoods value) {
				if (value != null && value.getCoinsGoodsType() == CoinsGoodsType.PHYSICAL_COMMODITY) {
					// 处理实物类商品
					if (StringUtils.isBlank(coinsGoodsOrder.getP0())
							&& StringUtils.isNotBlank(coinsGoodsOrder.getContactName())) {
						d.setP0(coinsGoodsOrder.getContactName() + "/" + coinsGoodsOrder.getContactPhone() + "/"
								+ coinsGoodsOrder.getContactAddress());
					}
				}
				d.setGoods(value);
			}

			@Override
			public VCoinsGoods getValue(Long key) {
				return coinsGoodsConvert.to(coinsGoodsService.get(key));
			}

			@Override
			@SuppressWarnings("unchecked")
			public Map<Long, VCoinsGoods> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}

				return coinsGoodsConvert.to(coinsGoodsService.mget(keys));
			}
		});

		// 转换抽奖商品信息
		assemblers.add(new ConverterAssembler<VCoinsGoodsOrder, CoinsGoodsOrder, Long, VCoinsLotteryGoods>() {

			@Override
			public boolean accept(CoinsGoodsOrder coinsGoodsOrder) {
				return coinsGoodsOrder.isInitCoinsGoods()
						&& coinsGoodsOrder.getSource() == CoinsGoodsOrderSource.LUCKY_DRAW;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(CoinsGoodsOrder coinsGoodsOrder, VCoinsGoodsOrder vCoinsGoodsOrder) {
				return coinsGoodsOrder.getGoodsId();
			}

			@Override
			public void setValue(CoinsGoodsOrder coinsGoodsOrder, VCoinsGoodsOrder d, VCoinsLotteryGoods value) {
				if (value != null && value.getCoinsGoodsType() == CoinsGoodsType.PHYSICAL_COMMODITY) {
					// 处理实物类商品
					if (StringUtils.isBlank(coinsGoodsOrder.getP0())
							&& StringUtils.isNotBlank(coinsGoodsOrder.getContactName())) {
						d.setP0(coinsGoodsOrder.getContactName() + "/" + coinsGoodsOrder.getContactPhone() + "/"
								+ coinsGoodsOrder.getContactAddress());
					}
				}
				d.setGoods(value);
			}

			@Override
			public VCoinsLotteryGoods getValue(Long key) {
				return coinsLotteryGoodsConvert.to(coinsLotteryGoodsService.get(key));
			}

			@Override
			@SuppressWarnings("unchecked")
			public Map<Long, VCoinsLotteryGoods> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}

				return coinsLotteryGoodsConvert.to(coinsLotteryGoodsService.mget(keys));
			}
		});

		// 转换假期活动抽奖商品信息
		assemblers.add(new ConverterAssembler<VCoinsGoodsOrder, CoinsGoodsOrder, Long, VCoinsLotteryGoods>() {

			@Override
			public boolean accept(CoinsGoodsOrder coinsGoodsOrder) {
				return coinsGoodsOrder.getSource() == CoinsGoodsOrderSource.HOLIDAY_ACTIVITY_01;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(CoinsGoodsOrder coinsGoodsOrder, VCoinsGoodsOrder vCoinsGoodsOrder) {
				return coinsGoodsOrder.getGoodsId();
			}

			@Override
			public void setValue(CoinsGoodsOrder coinsGoodsOrder, VCoinsGoodsOrder d, VCoinsLotteryGoods value) {
				if (value != null && value.getCoinsGoodsType() == CoinsGoodsType.PHYSICAL_COMMODITY) {
					// 处理实物类商品
					if (StringUtils.isBlank(coinsGoodsOrder.getP0())
							&& StringUtils.isNotBlank(coinsGoodsOrder.getContactName())) {
						d.setP0(coinsGoodsOrder.getContactName() + "/" + coinsGoodsOrder.getContactPhone() + "/"
								+ coinsGoodsOrder.getContactAddress());
					}
				}
				d.setGoods(value);
				d.setCoinsGoodsType(value.getCoinsGoodsType());
			}

			@Override
			public VCoinsLotteryGoods getValue(Long key) {
				return coinsLotteryGoodsConvert.to(coinsLotteryGoodsService.get(key));
			}

			@Override
			@SuppressWarnings("unchecked")
			public Map<Long, VCoinsLotteryGoods> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}

				return coinsLotteryGoodsConvert.to(coinsLotteryGoodsService.mget(keys));
			}
		});

	}

}

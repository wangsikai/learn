package com.lanking.uxb.service.mall.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;
import com.lanking.cloud.domain.yoo.goods.GoodsSnapshot;
import com.lanking.cloud.domain.yoo.goods.activity.lottery.LotteryActivityGoodsSnapshot;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrder;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.mall.api.GoodsSnapshotService;
import com.lanking.uxb.service.mall.api.LotteryActivityGoodsSnapshotService;
import com.lanking.uxb.service.mall.value.VLotteryActivityGoodsOrder;
import com.lanking.uxb.service.user.api.UserService;

/**
 * 抽奖活动订单转换.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年12月24日
 */
@Component
public class LotteryActivityGoodsOrderConvert extends Converter<VLotteryActivityGoodsOrder, CoinsGoodsOrder, Long> {

	@Autowired
	private GoodsSnapshotService goodsSnapshotService;
	@Autowired
	private LotteryActivityGoodsSnapshotService lotteryActivityGoodsSnapshotService;
	@Autowired
	private UserService userService;

	@Override
	protected Long getId(CoinsGoodsOrder s) {
		return s.getId();
	}

	@Override
	protected VLotteryActivityGoodsOrder convert(CoinsGoodsOrder s) {
		if (s == null) {
			return null;
		}
		VLotteryActivityGoodsOrder order = new VLotteryActivityGoodsOrder();
		order.setOrderid(s.getId());
		order.setContact(s.getP0());
		order.setGoodsId(s.getGoodsId());
		order.setGoodsSnapshotId(s.getGoodsSnapshotId());
		order.setStatus(s.getStatus());
		order.setMemo(s.getSellerNotes());
		return order;
	}

	public List<VLotteryActivityGoodsOrder> to(List<CoinsGoodsOrder> orders,
			LotteryActivityGoodsOrderConvertOption option) {
		for (CoinsGoodsOrder order : orders) {
			order.setInitActivityGoods(option.isInitActivityGoods());
			order.setInitGoods(option.isInitGoods());
			order.setInitUser(option.isInitUser());
		}
		return super.to(orders);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		/**
		 * 转换商品的快照信息.
		 */
		assemblers.add(new ConverterAssembler<VLotteryActivityGoodsOrder, CoinsGoodsOrder, Long, GoodsSnapshot>() {

			@Override
			public boolean accept(CoinsGoodsOrder s) {
				return s.isInitGoods();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(CoinsGoodsOrder s, VLotteryActivityGoodsOrder d) {
				return s.getGoodsSnapshotId();
			}

			@Override
			public void setValue(CoinsGoodsOrder s, VLotteryActivityGoodsOrder d, GoodsSnapshot goodsSnapshot) {
				if (goodsSnapshot != null) {
					d.setName(goodsSnapshot.getName());
					d.setImgid(goodsSnapshot.getImage());
				}
			}

			@Override
			public GoodsSnapshot getValue(Long key) {
				return goodsSnapshotService.get(key);
			}

			@Override
			public Map<Long, GoodsSnapshot> mgetValue(Collection<Long> keys) {
				return goodsSnapshotService.mget(keys);
			}
		});

		/**
		 * 转换奖品的快照信息.
		 */
		assemblers
				.add(new ConverterAssembler<VLotteryActivityGoodsOrder, CoinsGoodsOrder, Long, LotteryActivityGoodsSnapshot>() {

					@Override
					public boolean accept(CoinsGoodsOrder s) {
						return s.isInitActivityGoods();
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(CoinsGoodsOrder s, VLotteryActivityGoodsOrder d) {
						return s.getCoinsGoodsSnapshotId();
					}

					@Override
					public void setValue(CoinsGoodsOrder s, VLotteryActivityGoodsOrder d,
							LotteryActivityGoodsSnapshot lotteryActivityGoodsSnapshot) {
						if (lotteryActivityGoodsSnapshot != null) {
							d.setGoodsType(lotteryActivityGoodsSnapshot.getType());
						}
					}

					@Override
					public LotteryActivityGoodsSnapshot getValue(Long key) {
						return lotteryActivityGoodsSnapshotService.get(key);
					}

					@Override
					public Map<Long, LotteryActivityGoodsSnapshot> mgetValue(Collection<Long> keys) {
						return lotteryActivityGoodsSnapshotService.mget(keys);
					}
				});

		/**
		 * 转换订单用户信息.
		 */
		assemblers.add(new ConverterAssembler<VLotteryActivityGoodsOrder, CoinsGoodsOrder, Long, User>() {

			@Override
			public boolean accept(CoinsGoodsOrder s) {
				return s.isInitUser();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(CoinsGoodsOrder s, VLotteryActivityGoodsOrder d) {
				return s.getUserId();
			}

			@Override
			public void setValue(CoinsGoodsOrder s, VLotteryActivityGoodsOrder d, User user) {
				if (user != null) {
					d.setUserId(user.getId());
					d.setUserName(user.getName());
				}
			}

			@Override
			public User getValue(Long key) {
				return userService.get(key);
			}

			@Override
			public Map<Long, User> mgetValue(Collection<Long> keys) {
				return userService.getUsers(Sets.newHashSet(keys));
			}
		});
	}
}

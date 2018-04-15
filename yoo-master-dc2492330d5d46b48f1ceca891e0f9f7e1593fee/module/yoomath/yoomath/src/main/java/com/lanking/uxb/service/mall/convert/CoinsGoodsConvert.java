package com.lanking.uxb.service.mall.convert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.goods.Goods;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoods;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsDaySellCount;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.mall.api.CoinsGoodsDaySellCountService;
import com.lanking.uxb.service.mall.api.GoodsService;
import com.lanking.uxb.service.mall.value.VCoinsGoods;

/**
 * 金币商品convert
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月14日
 */
@Component
public class CoinsGoodsConvert extends Converter<VCoinsGoods, CoinsGoods, Long> {

	@Autowired
	private GoodsService goodsService;
	@Autowired
	private GoodsConvert goodsConvert;
	@Autowired
	private CoinsGoodsDaySellCountService countService;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	public VCoinsGoods to(CoinsGoods s, CoinsGoodsConvertOption option) {
		s.setInitDaySelledCount(option.isInitDaySelledCount());
		return super.to(s);
	}

	public List<VCoinsGoods> to(List<CoinsGoods> ss, CoinsGoodsConvertOption option) {
		for (CoinsGoods s : ss) {
			s.setInitDaySelledCount(option.isInitDaySelledCount());
		}
		return super.to(ss);
	}

	@Override
	protected Long getId(CoinsGoods s) {
		return s.getId();
	}

	@Override
	protected VCoinsGoods convert(CoinsGoods s) {
		VCoinsGoods v = new VCoinsGoods();
		v.setId(s.getId());
		v.setCoinsGoodsSnapshotId(s.getCoinsGoodsSnapshotId());
		v.setCoinsGoodsType(s.getCoinsGoodsType() == null ? CoinsGoodsType.NULL : s.getCoinsGoodsType());
		v.setVirtualGoodsType(s.getCoinsGoodsType());
		v.setDaySellCount(s.getDaySellShowCount());
		v.setDayBuyCount(s.getDayBuyCount());
		v.setDayStartHour(s.getDayStartHour());
		v.setDayStartMin(s.getDayStartMin());
		v.setDayEndHour(s.getDayEndHour());
		v.setDayEndMin(s.getDayEndMin());
		// 限制字符串描述
		List<Integer> weekOfDays = new ArrayList<Integer>(7);
		StringBuilder weekdayLimitSb = new StringBuilder();
		if (s.getWeekdayLimit() != null) {
			weekdayLimitSb.append("每周");
			if (s.getWeekdayLimit().intValue() % 10 == 1) {
				weekdayLimitSb.append("一、");
				weekOfDays.add(0);
			}
			if (s.getWeekdayLimit().intValue() / 10 % 10 == 1) {
				weekdayLimitSb.append("二、");
				weekOfDays.add(1);
			}
			if (s.getWeekdayLimit().intValue() / 100 % 10 == 1) {
				weekdayLimitSb.append("三、");
				weekOfDays.add(2);
			}
			if (s.getWeekdayLimit().intValue() / 1000 % 10 == 1) {
				weekdayLimitSb.append("四、");
				weekOfDays.add(3);
			}
			if (s.getWeekdayLimit().intValue() / 10000 % 10 == 1) {
				weekdayLimitSb.append("五、");
				weekOfDays.add(4);
			}
			if (s.getWeekdayLimit().intValue() / 100000 % 10 == 1) {
				weekdayLimitSb.append("六、");
				weekOfDays.add(5);
			}
			if (s.getWeekdayLimit().intValue() / 1000000 % 10 == 1) {
				weekdayLimitSb.append("日、");
				weekOfDays.add(6);
			}
			weekdayLimitSb = new StringBuilder(weekdayLimitSb.substring(0, weekdayLimitSb.length() - 1));
		} else {
			weekdayLimitSb.append("每天");
			for (int i = 0; i <= 6; i++) {
				weekOfDays.add(i);
			}
		}
		if (weekOfDays.size() == 7 && s.getDayStartHour() == 0 && s.getDayStartMin() == 0 && s.getDayEndHour() == 24
				&& s.getDayEndMin() == 0) {
			weekdayLimitSb = new StringBuilder();
		} else {
			if (s.getDayStartHour() / 10 == 0) {
				weekdayLimitSb.append("0");
			}
			weekdayLimitSb.append(s.getDayStartHour());
			weekdayLimitSb.append(":");
			if (s.getDayStartMin() / 10 == 0) {
				weekdayLimitSb.append("0");
			}
			weekdayLimitSb.append(s.getDayStartMin());
			weekdayLimitSb.append("开始兑换;");
		}
		if (s.getDayBuyCount() >= 0) {
			weekdayLimitSb.append("每人每天限兑换");
			weekdayLimitSb.append(s.getDayBuyCount());
			weekdayLimitSb.append("个");
		}
		v.setWeekdayLimit(weekdayLimitSb.toString());
		// 距离开始时间&结束时间
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.SECOND, 0);
		if (s.getDayEndHour() == 24) {
			cal.add(Calendar.DAY_OF_YEAR, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
		} else {
			cal.set(Calendar.HOUR_OF_DAY, s.getDayEndHour());
		}
		cal.set(Calendar.MINUTE, s.getDayEndMin());
		v.setToEndTime(System.currentTimeMillis() - cal.getTimeInMillis());

		cal = Calendar.getInstance();
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.HOUR_OF_DAY, s.getDayStartHour());
		cal.set(Calendar.MINUTE, s.getDayStartMin());
		v.setToStartTime(System.currentTimeMillis() - cal.getTimeInMillis());
		if (s.getWeekdayLimit() != null) {
			int curDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
			curDayOfWeek = curDayOfWeek == 1 ? 6 : curDayOfWeek - 2;
			if (!weekOfDays.contains(curDayOfWeek)) {
				int index = 0;
				for (Integer weekOfDay : weekOfDays) {
					if (weekOfDay > curDayOfWeek) {
						break;
					}
					index++;
				}
				int deltaDay = 0;
				if (weekOfDays.size() == index || !(weekOfDays.get(index) > curDayOfWeek)) {
					index = 0;
				}
				int nextDayOfWeek = weekOfDays.get(index);
				if (nextDayOfWeek > curDayOfWeek) {
					deltaDay = nextDayOfWeek - curDayOfWeek;
				} else {
					deltaDay = 6 - curDayOfWeek + nextDayOfWeek + 1;
				}
				if (deltaDay > 0) {
					v.setToStartTime(v.getToStartTime() - deltaDay * 24 * 60 * 60 * 1000);
					v.setToEndTime(v.getToEndTime() - deltaDay * 24 * 60 * 60 * 1000);
				}
			}
		} else if (s.getDateStart() != null) {
			v.setToStartTime(System.currentTimeMillis() - s.getDateStart().getTime());
		}
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VCoinsGoods, CoinsGoods, Long, Goods>() {

			@Override
			public boolean accept(CoinsGoods s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(CoinsGoods s, VCoinsGoods d) {
				return s.getId();
			}

			@Override
			public void setValue(CoinsGoods s, VCoinsGoods d, Goods value) {
				goodsConvert.assemblerVO(value, d);
				d.setCoinsPrice(value.getPrice().intValue());
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

		assemblers.add(new ConverterAssembler<VCoinsGoods, CoinsGoods, Long, Integer>() {

			@Override
			public boolean accept(CoinsGoods s) {
				return s.isInitDaySelledCount();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(CoinsGoods s, VCoinsGoods d) {
				return s.getId();
			}

			@Override
			public void setValue(CoinsGoods s, VCoinsGoods d, Integer value) {
				d.setDaySelledCount(value);
				int dayRemainingCount = s.getDaySellCount() - value;
				if (dayRemainingCount <= 0) {
					d.setDayRemainingCount(0);
				} else {
					d.setDayRemainingCount(s.getDaySellShowCount() - value);
				}
			}

			@Override
			public Integer getValue(Long key) {
				return countService.get(Long.parseLong(sdf.format(new Date())), key).getCount0();
			}

			@Override
			public Map<Long, Integer> mgetValue(Collection<Long> keys) {
				long dayLong = Long.parseLong(sdf.format(new Date()));
				Map<Long, CoinsGoodsDaySellCount> counts = countService.mget(dayLong, keys);
				Map<Long, Integer> map = new HashMap<Long, Integer>(keys.size());
				for (Long key : keys) {
					map.put(key, counts.get(key).getCount0());
				}
				return map;
			}

		});
	}
}

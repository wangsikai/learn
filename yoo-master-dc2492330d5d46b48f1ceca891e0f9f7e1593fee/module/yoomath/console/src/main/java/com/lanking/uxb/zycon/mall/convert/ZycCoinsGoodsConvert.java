package com.lanking.uxb.zycon.mall.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.goods.Goods;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoods;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsStatus;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.zycon.mall.api.ZycCoinsGoodsGroupGoodsService;
import com.lanking.uxb.zycon.mall.api.ZycGoodsService;
import com.lanking.uxb.zycon.mall.value.VZycGoods;

@Component
public class ZycCoinsGoodsConvert extends Converter<VZycGoods, CoinsGoods, Long> {

	@Autowired
	private ZycGoodsService zycGoodsService;

	@Autowired
	private ZycCoinsGoodsGroupGoodsService groupGoodsService;

	@Override
	protected Long getId(CoinsGoods s) {
		return s.getId();
	}

	@Override
	protected VZycGoods convert(CoinsGoods s) {
		VZycGoods vzygoods = new VZycGoods();
		vzygoods.setGoodsId(s.getId());
		vzygoods.setSequence0(s.getSequence0());
		vzygoods.setSequence1(s.getSequence1());
		vzygoods.setSequence2(s.getSequence2());
		if (s.getCoinsGoodsType() != CoinsGoodsType.TELEPHONE_CHARGE && s.getCoinsGoodsType() != CoinsGoodsType.QQ_VIP
				&& s.getCoinsGoodsType() != CoinsGoodsType.COUPONS) {
			vzygoods.setVirtualGoodsType(null);
		} else {
			vzygoods.setVirtualGoodsType(s.getCoinsGoodsType());
		}
		vzygoods.setGoodsStatus(s.getStatus());
		vzygoods.setDaySellCount(s.getDaySellCount());
		vzygoods.setDayBuyCount(s.getDayBuyCount());
		vzygoods.setDayStartHour(s.getDayStartHour());
		vzygoods.setDayEndHour(s.getDayEndHour());
		vzygoods.setDayStartMin(s.getDayStartMin());
		vzygoods.setDayEndMin(s.getDayEndMin());
		vzygoods.setUserTypeStr(StringUtils.leftPad(Integer.toString(s.getUserType(), 2), 3, '0'));
		vzygoods.setUserType(s.getUserType());
		vzygoods.setDayStartHourStr(StringUtils.leftPad(s.getDayStartHour() + "", 2, '0'));
		vzygoods.setDayStartMinStr(StringUtils.leftPad(s.getDayStartMin() + "", 2, '0'));
		if (s.getDateStart() != null) {
			vzygoods.setDateStart(s.getDateStart());
			vzygoods.setBuyTimeType(1);
		} else {
			if (s.getWeekdayLimit() != null) {
				vzygoods.setWeekdayLimit(s.getWeekdayLimit());
				vzygoods.setBuyTimeType(2);
				StringBuffer b = new StringBuffer();
				b.append("每周");
				List<Integer> indexList = new ArrayList<Integer>();
				if (s.getWeekdayLimit().intValue() % 10 == 1) {
					b.append("一、");
					indexList.add(0);
				}
				if (s.getWeekdayLimit().intValue() / 10 % 10 == 1) {
					b.append("二、");
					indexList.add(1);
				}
				if (s.getWeekdayLimit().intValue() / 100 % 10 == 1) {
					b.append("三、");
					indexList.add(2);
				}
				if (s.getWeekdayLimit().intValue() / 1000 % 10 == 1) {
					b.append("四、");
					indexList.add(3);
				}
				if (s.getWeekdayLimit().intValue() / 10000 % 10 == 1) {
					b.append("五、");
					indexList.add(4);
				}
				if (s.getWeekdayLimit().intValue() / 100000 % 10 == 1) {
					b.append("六、");
					indexList.add(5);
				}
				if (s.getWeekdayLimit().intValue() / 1000000 % 10 == 1) {
					b.append("日、");
					indexList.add(6);
				}
				vzygoods.setDes(b.toString().substring(0, b.length() - 1));
				vzygoods.setWeekIndexList(indexList);
			} else {
				vzygoods.setBuyTimeType(0);
			}
		}
		vzygoods.setDaySellShowCount(s.getDaySellShowCount() == 0 ? s.getDaySellCount() : s.getDaySellShowCount());
		return vzygoods;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VZycGoods, CoinsGoods, Long, Goods>() {

			@Override
			public boolean accept(CoinsGoods s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(CoinsGoods s, VZycGoods d) {
				return s.getId();
			}

			@Override
			public void setValue(CoinsGoods s, VZycGoods d, Goods value) {
				d.setName(value.getName());
				d.setContent(value.getContent());
				d.setIntroduction(value.getIntroduction());
				d.setImageId(value.getImage());
				d.setPrice(value.getPrice());
				d.setSalesTime(value.getSalesTime());
				d.setSoldOutTime(value.getSoldOutTime());

				Date now = new Date();
				if (s.getStatus().getValue() == CoinsGoodsStatus.PUBLISH.getValue()) {
					// 在有效期类
					if (value.getSalesTime().before(now) && value.getSoldOutTime().after(now)) {
						d.setCanOffGoods(1);
					} else if (value.getSalesTime().after(now)) {
						d.setCanOffGoods(0);
					} else if (value.getSoldOutTime().before(now)) {
						d.setCanOffGoods(2);
					}
				}
			}

			@Override
			public Goods getValue(Long key) {
				return zycGoodsService.get(key);
			}

			@Override
			public Map<Long, Goods> mgetValue(Collection<Long> keys) {
				return zycGoodsService.mget(keys);
			}

		});

		assemblers.add(new ConverterAssembler<VZycGoods, CoinsGoods, Long, Long>() {

			@Override
			public boolean accept(CoinsGoods s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(CoinsGoods s, VZycGoods d) {
				return s.getId();
			}

			@Override
			public void setValue(CoinsGoods s, VZycGoods d, Long value) {
				d.setGroupId(value);
			}

			@Override
			public Long getValue(Long key) {
				return groupGoodsService.getGroupIdByGoodsId(key);
			}

			@Override
			public Map<Long, Long> mgetValue(Collection<Long> keys) {
				return groupGoodsService.getGroupIdsByGoodsIds(keys);
			}

		});
	}
}

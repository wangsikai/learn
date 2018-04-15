package com.lanking.uxb.zycon.mall.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotterySeason;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.zycon.mall.value.VZycCoinsLotterySeason;

@Component
public class ZycCoinsLotterySeasonConvert extends Converter<VZycCoinsLotterySeason, CoinsLotterySeason, Long> {

	@Override
	protected Long getId(CoinsLotterySeason s) {
		return s.getId();
	}

	@Override
	protected VZycCoinsLotterySeason convert(CoinsLotterySeason s) {
		VZycCoinsLotterySeason v = new VZycCoinsLotterySeason();
		v.setId(s.getId());
		v.setTitle(s.getTitle());
		v.setStartTime(s.getStartTime());
		v.setEndTime(s.getEndTime());
		v.setUserJoinTimes(s.getUserJoinTimes());
		v.setStatus(s.getStatus());
		v.setEveryWeek(s.getEveryWeek());
		v.setName(s.getName());
		v.setUserType(s.getUserType());
		v.setCode(s.getCode());
		v.setType(s.getType());
		v.setAwardsTimes(s.getAwardsTimes());
		v.setUserAwardsTimes(s.getUserAwardsTimes());
		v.setMustAwardsGoods(s.getMustAwardsGoods());
		v.setMustAwardsTimes(s.getMustAwardsTimes());
		return v;
	}

}

package com.lanking.uxb.service.mall.convert;

import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotterySeason;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.service.mall.value.VCoinsLotterySeason;

import org.springframework.stereotype.Component;

/**
 * CoinsLotterySeason -> VCoinsLotterySeason
 *
 * @author xinyu.zhou
 * @since 2.4.0
 */
@Component
public class CoinsLotterySeasonConvert extends Converter<VCoinsLotterySeason, CoinsLotterySeason, Long> {

	@Override
	protected Long getId(CoinsLotterySeason coinsLotterySeason) {
		return coinsLotterySeason.getId();
	}

	@Override
	protected VCoinsLotterySeason convert(CoinsLotterySeason coinsLotterySeason) {

		VCoinsLotterySeason v = new VCoinsLotterySeason();

		v.setEndTime(coinsLotterySeason.getEndTime());
		v.setStartTime(coinsLotterySeason.getStartTime());
		v.setRemainTime(v.getStartTime().getTime() - System.currentTimeMillis());
		if (coinsLotterySeason.getEndTime().getTime() < System.currentTimeMillis()) {
			v.setStatus(Status.DISABLED);
		} else {
			v.setStatus(coinsLotterySeason.getStatus());
		}
		v.setTitle(coinsLotterySeason.getTitle());
		v.setUserJoinTimes(coinsLotterySeason.getUserJoinTimes());
		v.setId(coinsLotterySeason.getId());

		return v;
	}
}

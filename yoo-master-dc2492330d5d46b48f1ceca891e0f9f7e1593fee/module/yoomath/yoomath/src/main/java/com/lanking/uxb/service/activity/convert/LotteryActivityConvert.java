package com.lanking.uxb.service.activity.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.activity.lottery.LotteryActivity;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.activity.value.VLotteryActivity;

@Component
public class LotteryActivityConvert extends Converter<VLotteryActivity, LotteryActivity, Long> {

	@Override
	protected Long getId(LotteryActivity s) {
		return s.getCode();
	}

	@Override
	protected VLotteryActivity convert(LotteryActivity s) {
		VLotteryActivity v = new VLotteryActivity();
		v.setCode(s.getCode());
		v.setName(s.getName());
		v.setIntroduction(s.getIntroduction());
		v.setStartTime(s.getStartTime());
		v.setEndTime(s.getEndTime());
		v.setCreateAt(s.getCreateAt());
		v.setStatus(s.getStatus());

		v.setRemainTime(v.getStartTime().getTime() - System.currentTimeMillis());

		return v;
	}

}

package com.lanking.uxb.service.user.convert;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.common.PracticeHistory;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.user.value.VPracticeHistory;

@Component
public class PracticeHistoryConvert extends Converter<VPracticeHistory, PracticeHistory, Long> {

	@Override
	protected Long getId(PracticeHistory s) {
		return s.getId();
	}

	@Override
	protected VPracticeHistory convert(PracticeHistory s) {
		VPracticeHistory v = new VPracticeHistory();
		v.setBiz(s.getBiz());
		v.setBizId(s.getBizId());
		v.setCompletionRate(s.getCompletionRate());
		v.setName(s.getName());
		v.setRightRate(s.getRightRate() == null ? null : s.getRightRate().setScale(0, BigDecimal.ROUND_HALF_UP));
		v.setCommitAt(s.getCreateAt());
		return v;
	}
}

package com.lanking.uxb.service.zuoye.convert;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.stat.HomeworkStat;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.value.VHomeworkStat;

@Component
public class ZyHomeworkStatConvert extends Converter<VHomeworkStat, HomeworkStat, Long> {

	@Override
	protected Long getId(HomeworkStat s) {
		return s.getId();
	}

	@Override
	protected VHomeworkStat convert(HomeworkStat s) {
		VHomeworkStat v = new VHomeworkStat();
		v.setId(s.getId());
		v.setHomeworkClassId(s.getHomeworkClassId() == null ? 0 : s.getHomeworkClassId());
		v.setDoingNum(s.getDoingNum());
		v.setHomeWorkNum(s.getHomeWorkNum());
		v.setHomeworkTime(s.getHomeworkTime());
		v.setRightRate(s.getRightRate() == null ? BigDecimal.valueOf(0) : s.getRightRate());
		if (s.getRightRate() == null) {
			v.setRightRateTitle(StringUtils.EMPTY);
		}
		if (Security.isClient()) {
			if (s.getRightRate() == null) {
				v.setCompletionRate(null);
			} else {
				v.setCompletionRate(s.getCompletionRate());
			}
		} else {
			v.setCompletionRate(s.getCompletionRate() == null ? BigDecimal.valueOf(0) : s.getCompletionRate());
		}
		if (s.getCompletionRate() == null) {
			v.setCompletionRateTitle(StringUtils.EMPTY);
		}
		v.setUserId(s.getUserId());
		return v;
	}

}

package com.lanking.uxb.service.zuoye.convert;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPracticeDifficulty;
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPractise;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.zuoye.value.VDailyPractise;

/**
 * DailyPractise -> VDailyPractise
 *
 * @author xinyu.zhou
 * @since yoomath(mobile) V1.0.0
 */
@Component
public class ZyDailyPractiseConvert extends Converter<VDailyPractise, DailyPractise, Long> {

	@Override
	protected Long getId(DailyPractise dailyPractise) {
		return dailyPractise.getId();
	}

	@Override
	protected VDailyPractise convert(DailyPractise dailyPractise) {
		VDailyPractise v = new VDailyPractise();
		v.setId(dailyPractise.getId());
		v.setCreateAt(dailyPractise.getCreateAt());
		v.setUpdateAt(dailyPractise.getUpdateAt());
		v.setCommitAt(dailyPractise.getCommitAt());
		v.setQuestionCount(dailyPractise.getQuestionCount());
		v.setDoCount(dailyPractise.getDoCount());
		v.setRightCount(dailyPractise.getRightCount());
		v.setWrongCount(dailyPractise.getWrongCount());
		v.setDifficulty(dailyPractise.getDifficulty());
		v.setName(dailyPractise.getName());
		if (v.getDifficulty() > 0.8 && v.getDifficulty() <= 1.0) {
			v.setDifficultyStar(DailyPracticeDifficulty.LEVEL_1.getStar());
		} else if (v.getDifficulty() > 0.6) {
			v.setDifficultyStar(DailyPracticeDifficulty.LEVEL_2.getStar());
		} else if (v.getDifficulty() > 0.3) {
			v.setDifficultyStar(DailyPracticeDifficulty.LEVEL_3.getStar());
		} else if (v.getDifficulty() > 0) {
			v.setDifficultyStar(DailyPracticeDifficulty.LEVEL_4.getStar());
		}

		v.setCompleteRate(BigDecimal.valueOf((dailyPractise.getDoCount() * 100) / dailyPractise.getQuestionCount()));
		v.setCompleteRateTitle(v.getCompleteRate() + "%");
		if (v.getDoCount() == 0) {
			v.setRightRate(null);
		} else {
			v.setRightRate(dailyPractise.getRightRate());
			v.setRightRateTitle(v.getRightRate() + "%");
		}
		if (DateUtils.isSameDay(new Date(), v.getCreateAt())) {
			v.setNowDay(true);
		} else {
			v.setNowDay(false);
		}
		v.setHomeworkTime(dailyPractise.getHomeworkTime());

		return v;
	}

}

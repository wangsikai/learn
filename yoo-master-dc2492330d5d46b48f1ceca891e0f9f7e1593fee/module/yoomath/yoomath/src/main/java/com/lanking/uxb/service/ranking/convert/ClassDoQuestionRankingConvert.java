package com.lanking.uxb.service.ranking.convert;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.stat.DoQuestionClassStat;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.ranking.value.VClassDoQuestionRanking;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.convert.UserConvertOption;
import com.lanking.uxb.service.user.value.VUser;

@Component
public class ClassDoQuestionRankingConvert extends Converter<VClassDoQuestionRanking, DoQuestionClassStat, Long> {

	@Autowired
	private UserConvert userConvert;

	@Override
	protected Long getId(DoQuestionClassStat s) {
		return s.getId();
	}

	@Override
	protected VClassDoQuestionRanking convert(DoQuestionClassStat s) {
		VClassDoQuestionRanking v = new VClassDoQuestionRanking();
		v.setId(s.getId());
		v.setQuestionCount(s.getDoCount());
		v.setRightRate(s.getRightRate());
		if (s.getRightRate() != null) {
			v.setRightRateTitle(String.valueOf(s.getRightRate().setScale(0, BigDecimal.ROUND_HALF_UP).intValue()) + "%");
		}
		v.setRank(s.getRank());
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 学生信息
		assemblers.add(new ConverterAssembler<VClassDoQuestionRanking, DoQuestionClassStat, Long, VUser>() {

			@Override
			public boolean accept(DoQuestionClassStat s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(DoQuestionClassStat s, VClassDoQuestionRanking d) {
				return s.getUserId();
			}

			@Override
			public void setValue(DoQuestionClassStat s, VClassDoQuestionRanking d, VUser value) {
				d.setUser(value);
			}

			@Override
			public VUser getValue(Long key) {
				UserConvertOption option = new UserConvertOption();
				option.setInitMemberType(true);
				option.setInitPhase(false);
				option.setInitTeaDuty(false);
				option.setInitTeaSubject(false);
				option.setInitTeaTitle(false);
				option.setInitTextbook(false);
				option.setInitTextbookCategory(false);
				option.setInitUserState(false);
				return userConvert.get(key, option);
			}

			@Override
			public Map<Long, VUser> mgetValue(Collection<Long> keys) {
				UserConvertOption option = new UserConvertOption();
				option.setInitMemberType(true);
				option.setInitPhase(false);
				option.setInitTeaDuty(false);
				option.setInitTeaSubject(false);
				option.setInitTeaTitle(false);
				option.setInitTextbook(false);
				option.setInitTextbookCategory(false);
				option.setInitUserState(false);
				return userConvert.mget(keys, option);
			}

		});

	}

}

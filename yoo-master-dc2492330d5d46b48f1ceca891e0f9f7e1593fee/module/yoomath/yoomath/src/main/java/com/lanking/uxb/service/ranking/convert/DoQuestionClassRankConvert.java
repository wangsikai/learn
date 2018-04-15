package com.lanking.uxb.service.ranking.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.stat.DoQuestionClassRank;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.ranking.value.VDoQuestionRank;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.convert.UserConvertOption;
import com.lanking.uxb.service.user.value.VUser;

@Component
public class DoQuestionClassRankConvert extends Converter<VDoQuestionRank, DoQuestionClassRank, Long> {

	@Autowired
	private UserConvert userConvert;

	@Override
	protected Long getId(DoQuestionClassRank s) {
		return s.getId();
	}

	@Override
	protected VDoQuestionRank convert(DoQuestionClassRank s) {
		VDoQuestionRank v = new VDoQuestionRank();
		v.setId(s.getId());
		v.setPraiseCount(s.getPraiseCount() == null ? 0 : s.getPraiseCount());
		v.setRank(s.getRank() == null ? 0 : s.getRank());
		v.setRightCount(s.getRightCount() == null ? 0 : s.getRightCount());

		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 学生信息
		assemblers.add(new ConverterAssembler<VDoQuestionRank, DoQuestionClassRank, Long, VUser>() {

			@Override
			public boolean accept(DoQuestionClassRank s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(DoQuestionClassRank s, VDoQuestionRank d) {
				return s.getUserId();
			}

			@Override
			public void setValue(DoQuestionClassRank s, VDoQuestionRank d, VUser value) {
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

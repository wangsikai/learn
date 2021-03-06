package com.lanking.uxb.service.nationalDayActivity01.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01Tea;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.nationalDayActivity01.value.VNationalDayActivity01Rank;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.convert.UserConvertOption;
import com.lanking.uxb.service.user.value.VUser;

@Component
public class NationalDayActivity01TeaConvert
		extends Converter<VNationalDayActivity01Rank, NationalDayActivity01Tea, Long> {

	@Autowired
	private UserConvert userConvert;

	@Override
	protected Long getId(NationalDayActivity01Tea s) {
		return s.getUserId();
	}

	@Override
	protected VNationalDayActivity01Rank convert(NationalDayActivity01Tea s) {
		VNationalDayActivity01Rank v = new VNationalDayActivity01Rank();
		v.setHomeworkCount(s.getHomeworkCount() == null ? 0 : s.getHomeworkCount());
		v.setScore(s.getScore() == null ? 0 : s.getScore());

		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 用户信息
		assemblers.add(new ConverterAssembler<VNationalDayActivity01Rank, NationalDayActivity01Tea, Long, VUser>() {

			@Override
			public boolean accept(NationalDayActivity01Tea s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(NationalDayActivity01Tea s, VNationalDayActivity01Rank d) {
				return s.getUserId();
			}

			@Override
			public void setValue(NationalDayActivity01Tea s, VNationalDayActivity01Rank d, VUser value) {
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

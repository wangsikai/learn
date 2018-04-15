package com.lanking.uxb.service.imperial.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityAwardStudent;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.imperial.value.VImperialExaminationActivityAward;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.value.VUser;

/**
 * 获奖排名转换.
 * 
 * @author peng.zhao
 *
 * @version 2017年11月27日
 */
@Component
public class ImperialExaminationActivityAwardStudentConvert
		extends Converter<VImperialExaminationActivityAward, ImperialExaminationActivityAwardStudent, Long> {

	@Autowired
	private UserConvert userConvert;

	@Override
	protected Long getId(ImperialExaminationActivityAwardStudent s) {
		return s.getId();
	}

	@Override
	protected VImperialExaminationActivityAward convert(ImperialExaminationActivityAwardStudent s) {
		if (s != null) {
			VImperialExaminationActivityAward v = new VImperialExaminationActivityAward();
			v.setAwardContact(StringUtils.defaultString(s.getAwardContact(), ""));
			v.setAwardContactNumber(StringUtils.defaultString(s.getAwardContactNumber(), ""));
			v.setAwardDeliveryAddress(StringUtils.defaultString(s.getAwardDeliveryAddress(), ""));
			v.setAwardLevel(s.getAwardLevel());

			v.setDoTime(s.getDoTime());
			v.setId(s.getId());
			v.setScore(s.getScore() == null ? 0 : s.getScore());
			v.setRank(s.getRank() == null ? 0 : s.getRank());
			v.setUserId(s.getUserId());
			v.setClazzId(s.getClazzId());
			v.setStatus(s.getStatus());
			return v;
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {

		/**
		 * 用户.
		 */
		assemblers.add(
				new ConverterAssembler<VImperialExaminationActivityAward, ImperialExaminationActivityAwardStudent, Long, VUser>() {

					@Override
					public boolean accept(ImperialExaminationActivityAwardStudent s) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(ImperialExaminationActivityAwardStudent s, VImperialExaminationActivityAward d) {
						return s.getUserId();
					}

					@Override
					public void setValue(ImperialExaminationActivityAwardStudent s, VImperialExaminationActivityAward d,
							VUser value) {
						d.setUserName(value.getName());
					}

					@Override
					public VUser getValue(Long key) {
						return userConvert.get(key);
					}

					@Override
					public Map<Long, VUser> mgetValue(Collection<Long> keys) {
						return userConvert.mget(keys);
					}

				});
	}

}

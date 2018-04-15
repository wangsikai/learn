package com.lanking.uxb.service.imperial.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityAward;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.imperial.value.VImperialExaminationActivityAward;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;

/**
 * 获奖排名转换.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2017年4月5日
 */
@Component
public class ImperialExaminationActivityAwardConvert extends
		Converter<VImperialExaminationActivityAward, ImperialExaminationActivityAward, Long> {

	@Autowired
	private UserConvert userConvert;
	@Autowired
	private ZyHomeworkClassService homeworkClassService;

	@Override
	protected Long getId(ImperialExaminationActivityAward s) {
		return s.getId();
	}

	@Override
	protected VImperialExaminationActivityAward convert(ImperialExaminationActivityAward s) {
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
		assemblers
				.add(new ConverterAssembler<VImperialExaminationActivityAward, ImperialExaminationActivityAward, Long, VUser>() {

					@Override
					public boolean accept(ImperialExaminationActivityAward s) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(ImperialExaminationActivityAward s, VImperialExaminationActivityAward d) {
						return s.getUserId();
					}

					@Override
					public void setValue(ImperialExaminationActivityAward s, VImperialExaminationActivityAward d,
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

		/**
		 * 班级.
		 */
		assemblers
				.add(new ConverterAssembler<VImperialExaminationActivityAward, ImperialExaminationActivityAward, Long, HomeworkClazz>() {

					@Override
					public boolean accept(ImperialExaminationActivityAward s) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(ImperialExaminationActivityAward s, VImperialExaminationActivityAward d) {
						return s.getClazzId();
					}

					@Override
					public void setValue(ImperialExaminationActivityAward s, VImperialExaminationActivityAward d,
							HomeworkClazz value) {
						d.setClazzName(value.getName());
					}

					@Override
					public HomeworkClazz getValue(Long key) {
						return homeworkClassService.get(key);
					}

					@Override
					public Map<Long, HomeworkClazz> mgetValue(Collection<Long> keys) {
						return homeworkClassService.mget(keys);
					}
				});
	}
}

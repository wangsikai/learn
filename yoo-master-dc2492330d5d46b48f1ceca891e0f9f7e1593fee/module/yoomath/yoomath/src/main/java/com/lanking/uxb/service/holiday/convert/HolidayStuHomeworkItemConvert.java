package com.lanking.uxb.service.holiday.convert;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkItem;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItem;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkItemService;
import com.lanking.uxb.service.holiday.value.VHolidayStuHomeworkItem;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.value.VUser;

/**
 * 学生假日作业项 convert
 * 
 * @since yoomath V1.9
 * @author wangsenhao
 *
 */
@Component
public class HolidayStuHomeworkItemConvert extends Converter<VHolidayStuHomeworkItem, HolidayStuHomeworkItem, Long> {

	@Autowired
	private UserConvert userConvert;
	@Autowired
	private HolidayHomeworkItemService holidayHomeworkItemService;
	@Autowired
	private HolidayHomeworkItemConvert holidayHomeworkItemConvert;

	@Override
	protected Long getId(HolidayStuHomeworkItem s) {
		return s.getId();
	}

	public VHolidayStuHomeworkItem to(HolidayStuHomeworkItem s, HolidayStuHomeworkItemConvertOption option) {
		s.setInitUser(option.isInitUser());
		return super.to(s);
	}

	public List<VHolidayStuHomeworkItem> to(List<HolidayStuHomeworkItem> ss, HolidayStuHomeworkItemConvertOption option) {
		for (HolidayStuHomeworkItem s : ss) {
			s.setInitUser(option.isInitUser());
		}
		return super.to(ss);
	}

	@Override
	protected VHolidayStuHomeworkItem convert(HolidayStuHomeworkItem s) {
		VHolidayStuHomeworkItem view = new VHolidayStuHomeworkItem();
		view.setHolidayStuHomeworkItemId(s.getId());
		view.setHolidayHomeworkId(s.getHolidayHomeworkId());
		view.setCompleteRate(s.getCompletionRate() == null ? BigDecimal.valueOf(0) : s.getCompletionRate().setScale(0,
				BigDecimal.ROUND_HALF_UP));
		view.setCompletionRate(view.getCompleteRate());
		view.setRightRate(s.getRightRate() == null ? BigDecimal.valueOf(-1) : s.getRightRate());
		view.setRank(s.getRank());
		view.setStatus(s.getStatus());
		view.setStudentId(s.getStudentId());
		view.setHomeworkTime(s.getHomeworkTime());
		view.setRightCount(s.getRightCount());
		view.setWrongCount(s.getWrongCount());
		view.setSubmitAt(s.getSubmitAt() == null ? (s.getStatus() == StudentHomeworkStatus.SUBMITED ? s.getCreateAt()
				: null) : s.getSubmitAt());
		view.setUpdateAt(s.getUpdateAt());
		return view;
	}

	@SuppressWarnings("rawtypes")
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers
				.add(new ConverterAssembler<VHolidayStuHomeworkItem, HolidayStuHomeworkItem, Long, HolidayHomeworkItem>() {

					@Override
					public boolean accept(HolidayStuHomeworkItem s) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(HolidayStuHomeworkItem s, VHolidayStuHomeworkItem d) {
						return s.getHolidayHomeworkItemId();
					}

					@Override
					public void setValue(HolidayStuHomeworkItem s, VHolidayStuHomeworkItem d, HolidayHomeworkItem value) {
						d.setHolidayHomeworkItem(holidayHomeworkItemConvert.to(value));
						if (s.getCompletionRate() != null) {
							int completionCount = BigDecimal
									.valueOf(
											value.getQuestionCount().longValue()
													* d.getCompletionRate().setScale(2, BigDecimal.ROUND_HALF_UP)
															.doubleValue() / 100).setScale(0, BigDecimal.ROUND_HALF_UP)
									.intValue();
							d.setCompletionCount(completionCount);
						}
					}

					@Override
					public HolidayHomeworkItem getValue(Long key) {
						return holidayHomeworkItemService.get(key);
					}

					@Override
					public Map<Long, HolidayHomeworkItem> mgetValue(Collection<Long> keys) {
						return holidayHomeworkItemService.mget(keys);
					}

				});
		// 设置用户信息
		assemblers.add(new ConverterAssembler<VHolidayStuHomeworkItem, HolidayStuHomeworkItem, Long, VUser>() {
			@Override
			public boolean accept(HolidayStuHomeworkItem s) {
				return s.isInitUser();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(HolidayStuHomeworkItem s, VHolidayStuHomeworkItem d) {
				return s.getStudentId();
			}

			@Override
			public void setValue(HolidayStuHomeworkItem s, VHolidayStuHomeworkItem d, VUser value) {
				d.setUser(value);
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

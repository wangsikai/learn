package com.lanking.uxb.service.holiday.convert;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomework;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomework;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkService;
import com.lanking.uxb.service.holiday.value.VHolidayStuHomework;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.value.VUser;

/**
 * 学生假日作业convert
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月25日 上午10:25:05
 */
@Component
public class HolidayStuHomeworkConvert extends Converter<VHolidayStuHomework, HolidayStuHomework, Long> {

	@Autowired
	private UserConvert userConvert;
	@Autowired
	private HolidayHomeworkConvert holidayHomeworkConvert;
	@Autowired
	private HolidayHomeworkService holidayHomeworkService;

	@Override
	protected Long getId(HolidayStuHomework s) {
		return s.getId();
	}

	public VHolidayStuHomework to(HolidayStuHomework s, HolidayStuHomeworkConvertOption option) {
		s.setInitUser(option.isInitUser());
		return super.to(s);
	}

	public List<VHolidayStuHomework> to(List<HolidayStuHomework> ss, HolidayStuHomeworkConvertOption option) {
		for (HolidayStuHomework s : ss) {
			s.setInitUser(option.isInitUser());
		}
		return super.to(ss);
	}

	public Map<Long, VHolidayStuHomework> to(Map<Long, HolidayStuHomework> map,
			HolidayStuHomeworkConvertOption option) {
		for (HolidayStuHomework s : map.values()) {
			s.setInitUser(option.isInitUser());
		}
		return super.to(map);
	}

	@Override
	protected VHolidayStuHomework convert(HolidayStuHomework s) {
		VHolidayStuHomework v = new VHolidayStuHomework();
		v.setAllItemCount(s.getAllItemCount() == null ? 0 : s.getAllItemCount());
		v.setCommitItemCount(s.getCommitItemCount() == null ? 0 : s.getCommitItemCount());
		v.setCompletionRate(s.getCompletionRate() == null ? BigDecimal.valueOf(0)
				: s.getCompletionRate().setScale(0, BigDecimal.ROUND_HALF_UP));
		v.setCompletionCount(s.getCommitItemCount() == null ? 0 : s.getCommitItemCount());
		v.setCreateAt(s.getCreateAt());
		v.setDelStatus(s.getDelStatus());
		v.setHolidayHomeworkId(s.getHolidayHomeworkId());
		v.setHomeworkTime(s.getHomeworkTime());
		v.setId(s.getId());
		v.setRank(s.getRank());
		v.setRightCount(s.getRightCount());
		v.setRightRate(s.getRightRate() == null ? BigDecimal.valueOf(-1) : s.getRightRate());
		v.setStatus(s.getStatus());
		v.setStudentId(s.getStudentId());
		v.setType(s.getType());
		v.setWrongCount(s.getWrongCount());
		return v;
	}

	@SuppressWarnings("rawtypes")
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 对应的假期作业信息
		assemblers.add(new ConverterAssembler<VHolidayStuHomework, HolidayStuHomework, Long, HolidayHomework>() {
			@Override
			public boolean accept(HolidayStuHomework s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(HolidayStuHomework s, VHolidayStuHomework d) {
				return s.getHolidayHomeworkId();
			}

			@Override
			public void setValue(HolidayStuHomework s, VHolidayStuHomework d, HolidayHomework value) {
				d.setHolidayHomework(holidayHomeworkConvert.to(value));
			}

			@Override
			public HolidayHomework getValue(Long key) {
				return holidayHomeworkService.get(key);
			}

			@Override
			public Map<Long, HolidayHomework> mgetValue(Collection<Long> keys) {
				return holidayHomeworkService.mget(keys);
			}

		});
		// 设置用户信息
		assemblers.add(new ConverterAssembler<VHolidayStuHomework, HolidayStuHomework, Long, VUser>() {
			@Override
			public boolean accept(HolidayStuHomework s) {
				return s.isInitUser();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(HolidayStuHomework s, VHolidayStuHomework d) {
				return s.getStudentId();
			}

			@Override
			public void setValue(HolidayStuHomework s, VHolidayStuHomework d, VUser value) {
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

package com.lanking.uxb.zycon.holiday.convert;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomework;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.zycon.holiday.value.VZycHolidayHomework;
import com.lanking.uxb.zycon.homework.convert.ZycHomeworkClazzConvert;
import com.lanking.uxb.zycon.homework.value.VZycHomeworkClazz;

/**
 * @author zemin.song
 */
@Component
public class ZycHolidayHomeworkConvert extends Converter<VZycHolidayHomework, HolidayHomework, Long> {
	@Autowired
	private ZycHomeworkClazzConvert zycHomeworkClazzConvert;

	@Override
	protected Long getId(HolidayHomework homework) {
		return homework.getId();
	}

	@Override
	protected VZycHolidayHomework convert(HolidayHomework homework) {
		VZycHolidayHomework v = new VZycHolidayHomework();
		v.setId(homework.getId());
		v.setName(homework.getName());
		v.setStartTime(homework.getStartTime());
		v.setDeadline(homework.getDeadline());
		v.setDelStatus(homework.getDelStatus());
		v.setCompletionRate(homework.getCompletionRate());
		if (v.getDeadline().before(new Date())) {
			v.setHomeworkStatus(HomeworkStatus.ISSUED);
		} else {
			v.setHomeworkStatus(homework.getStatus());
		}
		return v;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VZycHolidayHomework, HolidayHomework, Long, VZycHomeworkClazz>() {

			@Override
			public boolean accept(HolidayHomework homework) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(HolidayHomework homework, VZycHolidayHomework vZycHomework) {
				return homework.getHomeworkClassId();
			}

			@Override
			public void setValue(HolidayHomework homework, VZycHolidayHomework vZycHomework, VZycHomeworkClazz value) {
				if (value != null) {
					vZycHomework.setClazz(value);
				}
			}

			@Override
			public VZycHomeworkClazz getValue(Long key) {
				if (null == key)
					return null;
				return zycHomeworkClazzConvert.get(key);
			}

			@Override
			public Map<Long, VZycHomeworkClazz> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys))
					return Maps.newHashMap();
				return zycHomeworkClazzConvert.mget(keys);
			}
		});
	}
}

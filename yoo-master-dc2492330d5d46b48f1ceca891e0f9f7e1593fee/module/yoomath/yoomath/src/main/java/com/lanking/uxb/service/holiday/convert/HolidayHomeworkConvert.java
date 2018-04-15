package com.lanking.uxb.service.holiday.convert;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomework;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.holiday.value.VHolidayHomework;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

/**
 * 作业专项转换
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月22日 下午4:50:38
 */
@Component
public class HolidayHomeworkConvert extends Converter<VHolidayHomework, HolidayHomework, Long> {

	@Autowired
	private ZyHomeworkClazzConvert homeworkClazzConvert;
	@Autowired
	private ZyHomeworkClassService zyHomeworkClassService;

	@Override
	protected Long getId(HolidayHomework s) {
		return s.getId();
	}

	@Override
	protected VHolidayHomework convert(HolidayHomework s) {
		VHolidayHomework holidayHomework = new VHolidayHomework();
		holidayHomework
				.setCompletionRate(s.getCompletionRate() == null ? BigDecimal.valueOf(0) : s.getCompletionRate());
		holidayHomework.setCreateAt(s.getCreateAt());
		holidayHomework.setCreateId(s.getCreateId());
		holidayHomework.setDeadline(s.getDeadline());
		holidayHomework.setDelStatus(s.getDelStatus());
		holidayHomework.setDifficulty(s.getDifficulty());
		holidayHomework.setHomeworkClassId(s.getHomeworkClassId());
		holidayHomework.setHomeworkTime(s.getHomeworkTime());
		holidayHomework.setId(s.getId());
		holidayHomework.setMetaKnowpoints(s.getMetaKnowpoints());
		holidayHomework.setName(s.getName());
		holidayHomework.setQuestionCount(s.getQuestionCount());
		holidayHomework.setRightCount(s.getRightCount());
		holidayHomework.setRightRate(s.getRightRate());
		holidayHomework.setStartTime(s.getStartTime());
		holidayHomework.setStatus(s.getStatus());
		holidayHomework.setType(s.getType());
		holidayHomework.setWrongCount(s.getWrongCount());
		holidayHomework.setKnowledgePoints(s.getKnowledgePoints());
		return holidayHomework;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 班级信息
		assemblers.add(new ConverterAssembler<VHolidayHomework, HolidayHomework, Long, VHomeworkClazz>() {

			@Override
			public boolean accept(HolidayHomework s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(HolidayHomework s, VHolidayHomework d) {
				return s.getHomeworkClassId();
			}

			@Override
			public void setValue(HolidayHomework s, VHolidayHomework d, VHomeworkClazz value) {
				if (value != null) {
					d.setClazz(value);
				}
			}

			@Override
			public VHomeworkClazz getValue(Long key) {
				if (key == null) {
					return null;
				}
				return homeworkClazzConvert.to(zyHomeworkClassService.get(key));
			}

			@Override
			public Map<Long, VHomeworkClazz> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}
				return homeworkClazzConvert.to(zyHomeworkClassService.mget(keys));
			}
		});
	}

}

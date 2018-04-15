package com.lanking.uxb.service.common.convert;

import java.math.BigDecimal;

import com.lanking.uxb.service.session.api.impl.Security;
import org.springframework.stereotype.Component;

import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.holiday.value.VHolidayStuHomework;
import com.lanking.uxb.service.resources.value.VHomework;
import com.lanking.uxb.service.resources.value.VStudentHomework;

/**
 * 学生假期作业vo转学生作业vo
 * 
 * @since yoomath V1.9
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年1月21日
 */
@Component
public class VStudentHomeworkConvert extends Converter<VStudentHomework, VHolidayStuHomework, Long> {

	@Override
	protected Long getId(VHolidayStuHomework s) {
		return s.getId();
	}

	@Override
	protected VStudentHomework convert(VHolidayStuHomework s) {
		VStudentHomework v = new VStudentHomework();
		v.setId(s.getId());
		VHomework vh = new VHomework();
		vh.setId(s.getHolidayHomework().getId());
		vh.setDeadline(s.getHolidayHomework().getDeadline());
		vh.setName(s.getHolidayHomework().getName());
		vh.setStartTime(s.getHolidayHomework().getStartTime());
		vh.setCompletionRate(s.getHolidayHomework().getCompletionRate());
		vh.setType(2);
		vh.setHomeworkClazz(s.getHolidayHomework().getClazz());
		vh.setCreateAt(s.getCreateAt());
		vh.setDifficulty(s.getHolidayHomework().getDifficulty());
		vh.setStatus(s.getHolidayHomework().getStatus());
		if (Security.isClient()) {
			vh.setQuestionCount(s.getAllItemCount());
		} else {
			if (s.getHolidayHomework() != null && s.getHolidayHomework().getQuestionCount() != null) {
				vh.setQuestionCount(s.getHolidayHomework().getQuestionCount());
			}
		}
		v.setHomework(vh);
		v.setHomeworkTime(s.getHomeworkTime());
		v.setStatus(s.getStatus());
		v.setRightRate(s.getRightRate() == BigDecimal.valueOf(-1) ? null : s.getRightRate());
		if (s.getRank() != null) {
			v.setRank(s.getRank());
		}
		v.setCompletionRate(s.getCompletionRate());
		v.setCompletionRateTitle(s.getCompletionRateTitle());
		v.setCompletionCount(s.getCompletionCount());
		return v;
	}

}

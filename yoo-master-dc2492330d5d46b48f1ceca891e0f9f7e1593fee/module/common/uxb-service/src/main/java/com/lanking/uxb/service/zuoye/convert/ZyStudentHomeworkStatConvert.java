package com.lanking.uxb.service.zuoye.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.stat.StudentHomeworkStat;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.zuoye.value.VStudentHomeworkStat;

@Component
public class ZyStudentHomeworkStatConvert extends Converter<VStudentHomeworkStat, StudentHomeworkStat, Long> {

	@Override
	protected Long getId(StudentHomeworkStat s) {
		return s.getId();
	}

	@Override
	protected VStudentHomeworkStat convert(StudentHomeworkStat s) {
		VStudentHomeworkStat v = new VStudentHomeworkStat();
		v.setId(s.getId());
		v.setHomeworkClassId(s.getHomeworkClassId() == null ? 0 : s.getHomeworkClassId());
		v.setTodoNum(s.getTodoNum());
		v.setOverdueNum(s.getOverdueNum());
		v.setHomeWorkNum(s.getHomeWorkNum());
		v.setHomeworkTime(s.getHomeworkTime());
		v.setRightRate(s.getRightRate());
		v.setUserId(s.getUserId());
		v.setRank(s.getRank());
		return v;
	}

}

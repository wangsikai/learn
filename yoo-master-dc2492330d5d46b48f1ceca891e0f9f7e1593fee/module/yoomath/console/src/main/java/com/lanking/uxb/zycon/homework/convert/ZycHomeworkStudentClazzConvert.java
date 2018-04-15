package com.lanking.uxb.zycon.homework.convert;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.zycon.homework.value.VZycHomeworkStudentClazz;

import org.springframework.stereotype.Component;

@Component
public class ZycHomeworkStudentClazzConvert extends Converter<VZycHomeworkStudentClazz, HomeworkStudentClazz, Long> {

	@Override
	protected Long getId(HomeworkStudentClazz s) {
		return s.getId();
	}

	@Override
	protected VZycHomeworkStudentClazz convert(HomeworkStudentClazz s) {
		VZycHomeworkStudentClazz v = new VZycHomeworkStudentClazz();
		v.setClassId(s.getClassId());
		v.setCreateAt(s.getCreateAt());
		v.setExitAt(s.getExitAt());
		v.setId(s.getId());
		v.setJoinAt(s.getJoinAt());
		v.setStatus(s.getStatus());
		v.setStudentId(s.getStudentId());
		v.setMark(validBlank(s.getMark()));
		return v;
	}

}

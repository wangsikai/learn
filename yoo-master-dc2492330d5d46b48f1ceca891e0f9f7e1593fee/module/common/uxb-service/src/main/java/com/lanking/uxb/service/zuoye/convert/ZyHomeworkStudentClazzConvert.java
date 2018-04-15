package com.lanking.uxb.service.zuoye.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.zuoye.value.VHomeworkStudentClazz;

@Component
public class ZyHomeworkStudentClazzConvert extends Converter<VHomeworkStudentClazz, HomeworkStudentClazz, Long> {

	@Override
	protected Long getId(HomeworkStudentClazz s) {
		return s.getId();
	}

	@Override
	protected VHomeworkStudentClazz convert(HomeworkStudentClazz s) {
		VHomeworkStudentClazz v = new VHomeworkStudentClazz();
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

package com.lanking.uxb.service.teachersDay01.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.activity.teachersDay01.TeachersDayActiviy01Tag;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.teachersDay01.value.VTeachersDayActiviy01Tag;

@Component
public class TeachersDayActiviy01TagConvert extends Converter<VTeachersDayActiviy01Tag, TeachersDayActiviy01Tag, Long> {

	@Override
	protected Long getId(TeachersDayActiviy01Tag s) {
		return s.getCode();
	}

	@Override
	protected VTeachersDayActiviy01Tag convert(TeachersDayActiviy01Tag s) {
		VTeachersDayActiviy01Tag v = new VTeachersDayActiviy01Tag();
		v.setCode(s.getCode());
		v.setName(s.getName());
		v.setSex(s.getSex());
		return v;
	}

}

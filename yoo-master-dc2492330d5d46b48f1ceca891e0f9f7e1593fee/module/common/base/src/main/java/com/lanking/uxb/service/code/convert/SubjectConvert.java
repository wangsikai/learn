package com.lanking.uxb.service.code.convert;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.Subject;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.value.VSubject;

@Component
public class SubjectConvert extends Converter<VSubject, Subject, Integer> {

	@Autowired
	private SubjectService subjectService;

	@Override
	protected Integer getId(Subject s) {
		return s.getCode();
	}

	@Override
	protected VSubject convert(Subject s) {
		VSubject v = new VSubject();
		v.setCode(s.getCode());
		v.setName(s.getName());
		v.setSequence(s.getSequence());
		v.setAcronym(s.getAcronym());
		v.setPhaseCode(s.getPhaseCode());
		return v;
	}

	@Override
	protected Subject internalGet(Integer id) {
		return subjectService.get(id);
	}

	@Override
	protected Map<Integer, Subject> internalMGet(Collection<Integer> ids) {
		return subjectService.mget(ids);
	}

}

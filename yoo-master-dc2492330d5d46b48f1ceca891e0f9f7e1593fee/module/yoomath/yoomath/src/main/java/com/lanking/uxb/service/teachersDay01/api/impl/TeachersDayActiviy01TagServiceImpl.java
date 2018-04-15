package com.lanking.uxb.service.teachersDay01.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.teachersDay01.TeachersDayActiviy01Tag;
import com.lanking.cloud.domain.yoo.user.Sex;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.teachersDay01.api.TeachersDayActiviy01TagService;

@Transactional(readOnly = true)
@Service
public class TeachersDayActiviy01TagServiceImpl implements TeachersDayActiviy01TagService {

	@Autowired
	@Qualifier("TeachersDayActiviy01TagRepo")
	private Repo<TeachersDayActiviy01Tag, Long> repo;

	@Override
	public TeachersDayActiviy01Tag get(long code) {
		return repo.get(code);
	}

	@Override
	public Map<Long, TeachersDayActiviy01Tag> mget(Collection<Long> codes) {
		return repo.mget(codes);
	}

	@Override
	public List<TeachersDayActiviy01Tag> findList(Sex sex) {
		Params params = Params.param();
		if (sex == null || sex == Sex.UNKNOWN) {
			params.put("sex", Sex.MALE.getValue());
		} else {
			params.put("sex", sex.getValue());
		}
		return repo.find("$findList", params).list();
	}

	@Override
	public List<TeachersDayActiviy01Tag> mgetList(Collection<Long> codes) {
		return repo.mgetList(codes);
	}

}

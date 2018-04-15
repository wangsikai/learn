package com.lanking.uxb.zycon.homework.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.homework.api.ZycStudentService;

/**
 * @author xinyu.zhou
 * @since yoomath V1.5
 */
@Transactional(readOnly = true)
@Service
public class ZycStudentServiceImpl implements ZycStudentService {

	@Autowired
	@Qualifier("StudentRepo")
	private Repo<Student, Long> repo;

	@Override
	public Student get(long id) {
		return repo.get(id);
	}

	@Override
	public List<Student> mgetList(Collection<Long> ids) {
		return repo.mgetList(ids);
	}

	@Override
	public Map<Long, Student> mget(Collection<Long> ids) {
		return repo.mget(ids);
	}

	@Override
	public CursorPage<Long, Student> query(CursorPageable<Long> pageable) {
		return repo.find("$zycQuery", Params.param()).fetch(pageable);
	}
}

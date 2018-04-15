package com.lanking.uxb.zycon.qs.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.qs.api.ZycTeacherService;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4.2
 */
@Service
@Transactional(readOnly = true)
public class ZycTeacherServiceImpl implements ZycTeacherService {

	@Autowired
	@Qualifier("TeacherRepo")
	private Repo<Teacher, Long> repo;

	@Override
	public List<Teacher> mgetList(Collection<Long> ids) {
		return repo.mgetList(ids);
	}

	@Override
	public Map<Long, Teacher> mget(Collection<Long> ids) {
		return repo.mget(ids);
	}

	@Override
	public Teacher get(long id) {
		return repo.get(id);
	}

	@Override
	public Teacher findByAccountNameOrMobileOrEmail(String loginValue) {
		return repo.find("$zycFindByAccountNameOrMobileOrEmail", Params.param("loginValue", loginValue)).get();
	}

	@Override
	public CursorPage<Long, Teacher> getAll(CursorPageable<Long> cursorPageable) {
		return repo.find("$zycGetAllByPage").fetch(cursorPageable);
	}

}

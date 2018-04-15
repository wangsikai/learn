package com.lanking.uxb.zycon.homework.api.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.homework.api.ZycHomeworkClazzService;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4
 */
@Service
@Transactional(readOnly = true)
public class ZycHomeworkClazzServiceImpl implements ZycHomeworkClazzService {
	@Autowired
	@Qualifier("HomeworkClazzRepo")
	private Repo<HomeworkClazz, Long> repo;

	@Override
	public HomeworkClazz get(Long id) {
		return repo.get(id);
	}

	@Override
	public List<HomeworkClazz> mgetList(Collection<Long> ids) {
		return repo.mgetList(ids);
	}

	@Override
	public List<HomeworkClazz> listCurrentClazzs(long teacherId) {
		return repo.find("$zycQuery", Params.param("teacherId", teacherId).put("status", Status.ENABLED.getValue()))
				.list();
	}

	@Override
	public CursorPage<Long, HomeworkClazz> getAll(CursorPageable<Long> cursorPageable) {
		return repo.find("$zycGetAllByPage").fetch(cursorPageable);
	}
}

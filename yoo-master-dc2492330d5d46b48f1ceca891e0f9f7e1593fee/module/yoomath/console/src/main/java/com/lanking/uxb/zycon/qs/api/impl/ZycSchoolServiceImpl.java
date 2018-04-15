package com.lanking.uxb.zycon.qs.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.uxb.zycon.qs.api.ZycSchoolService;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4.2
 */
@Service
@Transactional(readOnly = true)
public class ZycSchoolServiceImpl implements ZycSchoolService {

	@Autowired
	@Qualifier("SchoolRepo")
	private Repo<School, Long> repo;

	@Override
	public School get(long id) {
		return repo.get(id);
	}

	@Override
	public List<School> mgetList(Collection<Long> ids) {
		return repo.mgetList(ids);
	}

	@Override
	public Map<Long, School> mget(Collection<Long> ids) {
		return repo.mget(ids);
	}
}

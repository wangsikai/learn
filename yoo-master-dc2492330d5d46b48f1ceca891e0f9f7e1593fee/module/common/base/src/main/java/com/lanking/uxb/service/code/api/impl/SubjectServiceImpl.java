package com.lanking.uxb.service.code.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.Subject;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.code.api.SubjectService;

@Service
@Transactional(readOnly = true)
@ConditionalOnExpression("!${common.code.cache}")
public class SubjectServiceImpl implements SubjectService {

	@Autowired
	@Qualifier("SubjectRepo")
	private Repo<Subject, Integer> subjectRepo;

	@Override
	public Subject get(Integer code) {
		return subjectRepo.get(code);
	}

	@Override
	public List<Subject> getAll() {
		List<Subject> subjectList = subjectRepo.find("$findAllSubject").list();
		return subjectList;
	}

	@Override
	public Map<Integer, Subject> mget(Collection<Integer> codes) {
		return subjectRepo.mget(codes);
	}

	@Override
	public Map<Integer, Subject> mgetAll() {
		List<Subject> subjectList = subjectRepo.find("$findAllSubject").list();
		Map<Integer, Subject> subjectMap = Maps.newHashMap();
		for (Subject suject : subjectList) {
			subjectMap.put(suject.getCode(), suject);
		}
		return subjectMap;
	}

	@Override
	public List<Subject> findByPhaseCode(Integer phaseCode) {
		Params params = Params.param("phaseCode", phaseCode);
		List<Subject> subjectList = subjectRepo.find("$getSubjectsByPhaseCode", params).list();
		return subjectList;
	}

}

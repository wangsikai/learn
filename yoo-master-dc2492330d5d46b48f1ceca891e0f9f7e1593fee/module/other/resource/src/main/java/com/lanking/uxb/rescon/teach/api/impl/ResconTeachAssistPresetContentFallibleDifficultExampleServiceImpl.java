package com.lanking.uxb.rescon.teach.api.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistPresetContentFallibleDifficultExample;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistPresetContentFallibleDifficultExampleService;

@Transactional(readOnly = true)
@Service
public class ResconTeachAssistPresetContentFallibleDifficultExampleServiceImpl implements
		ResconTeachAssistPresetContentFallibleDifficultExampleService {
	@Autowired
	@Qualifier("TeachAssistPresetContentFallibleDifficultExampleRepo")
	private Repo<TeachAssistPresetContentFallibleDifficultExample, Long> repo;

	@Override
	public List<TeachAssistPresetContentFallibleDifficultExample> findListByFallId(Long id) {
		return repo.find("$findListByFallId", Params.param("id", id)).list();
	}

	@Transactional
	@Override
	public void delByFallId(Long id) {
		repo.execute("$delByFallId", Params.param("id", id));
	}

	@Override
	public List<TeachAssistPresetContentFallibleDifficultExample> findListByFallIds(Collection<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return Collections.EMPTY_LIST;
		}
		return repo.find("$findListByFallIds", Params.param("ids", ids)).list();
	}
}

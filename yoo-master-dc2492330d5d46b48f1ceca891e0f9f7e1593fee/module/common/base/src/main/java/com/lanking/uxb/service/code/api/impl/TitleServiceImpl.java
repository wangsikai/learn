package com.lanking.uxb.service.code.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.Title;
import com.lanking.uxb.service.code.api.TitleService;

@Service
@Transactional(readOnly = true)
@ConditionalOnExpression("!${common.code.cache}")
public class TitleServiceImpl implements TitleService {

	@Autowired
	@Qualifier("TitleRepo")
	private Repo<Title, Integer> titleRepo;

	@Override
	public Title getTitle(Integer code) {
		return titleRepo.get(code);
	}

	@Override
	public List<Title> getAll() {
		return titleRepo.find("$findAllTitle").list();
	}

	@Override
	public Map<Integer, Title> mget(Collection<Integer> codes) {
		return titleRepo.mget(codes);
	}

}

package com.lanking.uxb.service.file.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.file.Space;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.file.api.SpaceService;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月28日
 *
 */
@Service
@Transactional(readOnly = true)
public class SpaceServiceImpl implements SpaceService {

	@Autowired
	@Qualifier("SpaceRepo")
	private Repo<Space, Long> spaceRepo;

	@Transactional(readOnly = true)
	@Override
	public Space getSpace(String name) {
		return spaceRepo.find("$getSpace", Params.param("name", name)).get();
	}

	@Transactional(readOnly = true)
	@Override
	public Space getSpace(long spaceId) {
		return spaceRepo.get(spaceId);
	}

	@Transactional
	@Override
	public Space saveSpace(Space space) {
		return spaceRepo.save(space);
	}

	@Transactional
	@Override
	public void updateSpace(long id, int incrNum, long incrSize) {
		spaceRepo.execute("$updateSpace", Params.param("id", id).put("incrNum", incrNum).put("incrSize", incrSize));
	}
}

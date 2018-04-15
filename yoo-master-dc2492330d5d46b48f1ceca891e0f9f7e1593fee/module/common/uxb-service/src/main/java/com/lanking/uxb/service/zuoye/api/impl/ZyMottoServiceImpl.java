package com.lanking.uxb.service.zuoye.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.common.Motto;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.zuoye.api.ZyMottoService;
import com.lanking.uxb.service.zuoye.cache.MottoCacheService;

@Transactional(readOnly = true)
@Service
public class ZyMottoServiceImpl implements ZyMottoService {

	@Autowired
	@Qualifier("MottoRepo")
	Repo<Motto, Long> mottoRepo;

	@Autowired
	private MottoCacheService mottoCacheService;

	// 从db里面最多获取的条数
	private static int MOTTO_LIMIT = 50;

	@PostConstruct
	void init() {
		MOTTO_LIMIT = Env.getInt("motto.limit");
	}

	@Override
	public String getOne() {
		List<String> all = mottoCacheService.get();
		if (CollectionUtils.isEmpty(all)) {
			List<Motto> mottos = mottoRepo.find("$zyList", Params.param("limit", MOTTO_LIMIT)).list();
			all = new ArrayList<String>(mottos.size());
			if (CollectionUtils.isNotEmpty(mottos)) {
				for (Motto motto : mottos) {
					all.add(motto.getContent());
				}
				mottoCacheService.set(all);
			}
		}
		if (all.size() > 0) {
			return all.get(new Random().nextInt(all.size()));
		}
		return StringUtils.EMPTY;
	}

}

package com.lanking.uxb.service.honor.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.honor.UserLevels;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.honor.api.UserLevelsService;

@Transactional(readOnly = true)
@Service
public class UserLevelsServiceImpl implements UserLevelsService {
	@Autowired
	@Qualifier("UserLevelsRepo")
	private Repo<UserLevels, Long> userLevelsRepo;

	@Override
	public UserLevels getUserLevel(int level, Product product) {
		return userLevelsRepo.find("$getUserLevel", Params.param("level", level).put("product", product.getValue()))
				.get();
	}

	@Override
	public Integer getLevelByGrowthValue(int growth) {
		return userLevelsRepo.find("$getLevelByGrowth", Params.param("growth", growth)).get(Integer.class);
	}

	@Override
	public List<UserLevels> getUserLevel(int startLevel, int endLevel, Product product) {
		return userLevelsRepo.find("$getUserLevels",
				Params.param("startLevel", startLevel).put("endLevel", endLevel).put("product", product.getValue()))
				.list();
	}
}

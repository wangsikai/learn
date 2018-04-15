package com.lanking.uxb.service.user.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.user.UserParameter;
import com.lanking.cloud.domain.yoo.user.UserParameterType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.user.api.UserParameterService;

@Transactional(readOnly = true)
@Service
public class UserParameterServiceImpl implements UserParameterService {

	@Autowired
	@Qualifier("UserParameterRepo")
	private Repo<UserParameter, Long> upRepo;

	@Override
	public UserParameter findOne(Product product, String version, UserParameterType type, long userId) {
		Params param = Params.param("product", product.getValue()).put("type", type.getValue()).put("userId", userId);
		if (StringUtils.isNotBlank(version)) {
			param.put("version", version);
		}
		return upRepo.find("$find", param).get();
	}

	@Transactional
	@Override
	public UserParameter save(UserParameter parameter) {
		UserParameter p = null;
		if (parameter.getId() != null) {
			p = upRepo.get(parameter.getId());
		}
		if (p == null) {
			p = new UserParameter();
			p.setProduct(parameter.getProduct());
			p.setType(parameter.getType());
			p.setUserId(parameter.getUserId());
			p.setVersion(parameter.getVersion());
		}
		p.setP0(parameter.getP0());
		p.setP1(parameter.getP1());
		p.setP2(parameter.getP2());
		p.setP3(parameter.getP3());
		p.setP4(parameter.getP4());
		p.setP5(parameter.getP5());
		p.setP6(parameter.getP6());
		p.setP7(parameter.getP7());
		p.setP8(parameter.getP8());
		p.setP9(parameter.getP9());
		return upRepo.save(p);
	}

	@Async
	@Transactional
	@Override
	public void asyncSave(UserParameter parameter) {
		save(parameter);
	}

}

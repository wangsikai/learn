package com.lanking.uxb.service.sys.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.common.Banner;
import com.lanking.cloud.domain.yoo.common.BannerLocation;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.sys.api.BannerQuery;
import com.lanking.uxb.service.sys.api.BannerService;

@Transactional(readOnly = true)
@Service
public class BannerServiceImpl implements BannerService {
	@Autowired
	@Qualifier("BannerRepo")
	private Repo<Banner, Long> bannerRepo;

	@Override
	public List<Banner> listEnable(BannerQuery query) {
		Params params = Params.param();
		if (query.getLocation() != null) {
			params.put("location", query.getLocation().getValue());
		}
		if (CollectionUtils.isNotEmpty(query.getLocations())) {
			List<Integer> locations = new ArrayList<Integer>(query.getLocations().size());

			for (BannerLocation l : query.getLocations()) {
				locations.add(l.getValue());
			}

			params.put("locations", locations);
		}
		if (query.getApp() == null) {
			params.put("app", -1);
		} else {
			params.put("app", query.getApp().getValue());
		}
		params.put("nowDate", new Date());
		return bannerRepo.find("$listEnable", params).list();
	}
}

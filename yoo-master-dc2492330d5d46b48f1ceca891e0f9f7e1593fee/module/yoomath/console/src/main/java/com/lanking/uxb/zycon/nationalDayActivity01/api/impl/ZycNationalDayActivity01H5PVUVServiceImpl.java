package com.lanking.uxb.zycon.nationalDayActivity01.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01H5;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01H5PVUV;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.nationalDayActivity01.api.ZycNationalDayActivity01H5PVUVService;

@Transactional(readOnly = true)
@Service
public class ZycNationalDayActivity01H5PVUVServiceImpl implements ZycNationalDayActivity01H5PVUVService {

	@Autowired
	@Qualifier("NationalDayActivity01H5PVUVRepo")
	private Repo<NationalDayActivity01H5PVUV, Long> nationalDayActivity01H5PVUVRepo;

	@Override
	public NationalDayActivity01H5PVUV getH5PVUV(NationalDayActivity01H5 h5, long viewAt) {
		Params params = Params.param();
		params.put("h5", h5.ordinal());
		params.put("viewAt", viewAt);

		return nationalDayActivity01H5PVUVRepo.find("$queryH5PVUV", params).get();
	}
}

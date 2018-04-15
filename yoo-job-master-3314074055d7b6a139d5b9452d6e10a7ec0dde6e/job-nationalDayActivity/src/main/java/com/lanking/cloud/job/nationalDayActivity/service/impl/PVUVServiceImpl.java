package com.lanking.cloud.job.nationalDayActivity.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01H5;
import com.lanking.cloud.job.nationalDayActivity.DAO.NationalDayActivity01H5PVUVDAO;
import com.lanking.cloud.job.nationalDayActivity.DAO.NationalDayActivity01H5UVDAO;
import com.lanking.cloud.job.nationalDayActivity.service.PVUVService;

@Transactional(readOnly = true)
@Service("nda01PVUVService")
public class PVUVServiceImpl implements PVUVService {

	@Autowired
	@Qualifier("nda01NationalDayActivity01H5UVDAO")
	private NationalDayActivity01H5UVDAO uvDAO;
	@Autowired
	@Qualifier("nda01NationalDayActivity01H5PVUVDAO")
	private NationalDayActivity01H5PVUVDAO pvuvDAO;

	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

	@Transactional
	@Override
	public void viewPage(NationalDayActivity01H5 h5, long userId, Date viewAt) {
		long viewAtL = Long.parseLong(format.format(viewAt));
		int dayuv = 0;
		if (uvDAO.updateUV(h5, userId, 1, viewAt, viewAtL) == 0) {
			uvDAO.create(h5, userId, viewAt, viewAtL);
			if (userId > 0) {
				dayuv = 1;
			}
		}
		if (pvuvDAO.updateUVPV(h5, 1, dayuv, viewAtL) == 0) {
			pvuvDAO.create(h5, 1, dayuv, viewAtL);
		}

		int alluv = 0;
		if (uvDAO.updateUV(h5, userId, 1, viewAt, 0) == 0) {
			uvDAO.create(h5, userId, viewAt, 0);
			if (userId > 0) {
				alluv = 1;
			}
		}
		if (pvuvDAO.updateUVPV(h5, 1, alluv, 0) == 0) {
			pvuvDAO.create(h5, 1, alluv, 0);
		}

	}
}

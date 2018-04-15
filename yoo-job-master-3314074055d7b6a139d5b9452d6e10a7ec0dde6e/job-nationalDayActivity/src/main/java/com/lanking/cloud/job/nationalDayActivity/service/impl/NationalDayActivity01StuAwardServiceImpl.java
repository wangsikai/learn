package com.lanking.cloud.job.nationalDayActivity.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01Stu;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01StuAward;
import com.lanking.cloud.job.nationalDayActivity.DAO.NationalDayActivity01DAO;
import com.lanking.cloud.job.nationalDayActivity.DAO.NationalDayActivity01StuAwardDAO;
import com.lanking.cloud.job.nationalDayActivity.DAO.NationalDayActivity01StuDAO;
import com.lanking.cloud.job.nationalDayActivity.service.NationalDayActivity01Service;
import com.lanking.cloud.job.nationalDayActivity.service.NationalDayActivity01StuAwardService;
import com.lanking.cloud.sdk.util.CollectionUtils;

@Service("nda01StuAwardService")
@Transactional(readOnly = true)
public class NationalDayActivity01StuAwardServiceImpl implements NationalDayActivity01StuAwardService {

	@Autowired
	@Qualifier("nda01NationalDayActivity01StuDAO")
	private NationalDayActivity01StuDAO nda01StuDAO;
	@Autowired
	@Qualifier("nda01NationalDayActivity01StuAwardDAO")
	private NationalDayActivity01StuAwardDAO nda01StuAwardDAO;
	@Autowired
	@Qualifier("nda01NationalDayActivity01DAO")
	private NationalDayActivity01DAO nda01DAO;

	@Transactional
	@Override
	public void award() {
		List<NationalDayActivity01Stu> stus = nda01StuDAO.top50();
		if (CollectionUtils.isNotEmpty(stus)) {
			int rank = 1;
			for (NationalDayActivity01Stu stu : stus) {
				NationalDayActivity01StuAward one = new NationalDayActivity01StuAward();
				one.setUserId(stu.getUserId());
				if (rank <= 3) {
					one.setAward(1);
				} else if (rank <= 10) {
					one.setAward(2);
				} else if (rank <= 50) {
					one.setAward(3);
				}
				one.setRank(rank);
				rank++;
				nda01StuAwardDAO.save(one);
			}
		}
	}

	@Override
	public boolean needAward() {
		NationalDayActivity01 nda01 = nda01DAO.get(NationalDayActivity01Service.NATIONAL_DAY_ACTIVITY_ID);
		return System.currentTimeMillis() > nda01.getEndTime().getTime() && nda01StuAwardDAO.count() == 0;
	}
}

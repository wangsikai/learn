package com.lanking.cloud.job.nationalDayActivity.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01Tea;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01TeaAward;
import com.lanking.cloud.job.nationalDayActivity.DAO.NationalDayActivity01DAO;
import com.lanking.cloud.job.nationalDayActivity.DAO.NationalDayActivity01TeaAwardDAO;
import com.lanking.cloud.job.nationalDayActivity.DAO.NationalDayActivity01TeaDAO;
import com.lanking.cloud.job.nationalDayActivity.service.NationalDayActivity01Service;
import com.lanking.cloud.job.nationalDayActivity.service.TaskNationalDayActivity01TeaAwardService;

import httl.util.CollectionUtils;

@Transactional(readOnly = true)
@Service
public class TaskNationalDayActivity01TeaAwardServiceImpl implements TaskNationalDayActivity01TeaAwardService {

	@Autowired
	@Qualifier("nda01NationalDayActivity01TeaDAO")
	private NationalDayActivity01TeaDAO nationalDayActivity01TeaDAO;
	@Autowired
	@Qualifier("nda01NationalDayActivity01TeaAwardDAO")
	private NationalDayActivity01TeaAwardDAO nationalDayActivity01TeaAwardDAO;
	@Autowired
	@Qualifier("nda01NationalDayActivity01DAO")
	private NationalDayActivity01DAO nationalDayActivity01DAO;

	@Transactional(readOnly = false)
	@Override
	public void statTeaAward(int count) {
		List<NationalDayActivity01Tea> teas = nationalDayActivity01TeaDAO.getTopNTea(count);
		if (CollectionUtils.isEmpty(teas)) {
			return;
		}

		List<NationalDayActivity01TeaAward> awards = Lists.newArrayList();
		NationalDayActivity01TeaAward award = null;
		for (int i = 0; i < teas.size(); i++) {
			award = new NationalDayActivity01TeaAward();
			award.setUserId(teas.get(i).getUserId());
			if (i > 2) {
				// 第4名到第10名固定4等奖
				award.setAward(4);
			} else {
				award.setAward(i + 1);
			}
			award.setRank(i + 1);
			awards.add(award);
		}

		// 保存数据
		nationalDayActivity01TeaAwardDAO.save(awards);
	}

	@Override
	public boolean needAward() {
		// 控制任务开始时间，取活动结束后一小时
		NationalDayActivity01 activity = nationalDayActivity01DAO
				.get(NationalDayActivity01Service.NATIONAL_DAY_ACTIVITY_ID);

		return System.currentTimeMillis() > activity.getEndTime().getTime()
				&& nationalDayActivity01TeaAwardDAO.count() == 0;
	}

}

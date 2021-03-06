package com.lanking.cloud.job.correctUserDayStat.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.cloud.job.correctUserDayStat.service.TaskUserDayStatService;
import com.lanking.cloud.job.correctUserDayStat.service.TaskUserMonthStatService;
import com.lanking.cloud.job.correctUserDayStat.service.UserMonthStatService;
import com.lanking.cloud.sdk.data.CursorPage;

@Service
public class UserMonthStatServiceImpl implements UserMonthStatService {

	@Autowired
	private TaskUserDayStatService taskUserDayStatService;
	@Autowired
	private TaskUserMonthStatService taskUserMonthStatService;

	@SuppressWarnings("rawtypes")
	@Override
	public void doUserMonthStat() {
		int fetchCount = 200;

		CursorPage<Long, Map> ids = taskUserDayStatService.queryUserId(fetchCount, Long.MAX_VALUE);
		List<Long> userIds = new ArrayList<Long>(ids.getItemSize());
		while (ids.isNotEmpty()) {
			for (Map map : ids) {
				userIds.add(((BigInteger) map.get("id")).longValue());

				taskUserMonthStatService.statUserCorrect(userIds);
				userIds.clear();
			}

			// 获取下一页
			ids = taskUserDayStatService.queryUserId(fetchCount, ids.getNextCursor());
		}
	}

}

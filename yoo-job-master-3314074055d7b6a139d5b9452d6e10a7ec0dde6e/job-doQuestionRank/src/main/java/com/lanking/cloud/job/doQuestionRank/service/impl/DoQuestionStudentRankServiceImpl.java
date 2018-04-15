package com.lanking.cloud.job.doQuestionRank.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.yoomath.stat.DoQuestionClassRank;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionSchoolRank;
import com.lanking.cloud.job.doQuestionRank.service.DoQuestionStudentRankService;
import com.lanking.cloud.job.doQuestionRank.service.TaskDoQuestionRankService;
import com.lanking.cloud.sdk.data.CursorPage;

import httl.util.CollectionUtils;

@Service
public class DoQuestionStudentRankServiceImpl implements DoQuestionStudentRankService {

	@Autowired
	private TaskDoQuestionRankService taskDoQuestionRankService;

	@SuppressWarnings("rawtypes")
	@Override
	public void statStudentDoQuestion(int startInt, int endInt, Date startDate, Date endDate) {
		int fetchCount = 200;

		CursorPage<Long, Map> ids = taskDoQuestionRankService.queryUserId(fetchCount, Long.MAX_VALUE);
		List<Long> userIds = new ArrayList<Long>(ids.getItemSize());
		while (ids.isNotEmpty()) {
			for (Map map : ids) {
				userIds.add(((BigInteger) map.get("id")).longValue());

				taskDoQuestionRankService.doQuestionStudentRank(startInt, endInt, startDate, endDate, userIds);
				userIds.clear();
			}

			// 获取下一页
			ids = taskDoQuestionRankService.queryUserId(fetchCount, ids.getNextCursor());
		}
	}

	@Override
	public void refreshClassData(int startInt, int endInt) {
		int fetchCount = 200;
		CursorPage<Long, DoQuestionClassRank> rankPage = taskDoQuestionRankService.refreshClassData(startInt,
				endInt, Long.MAX_VALUE, fetchCount);
		while (rankPage.isNotEmpty()) {
			rankPage = taskDoQuestionRankService.refreshClassData(startInt, endInt, rankPage.getNextCursor(),
					fetchCount);
		}
	}

	@Override
	public void statisSchoolRank(int startInt, int endInt) {
		int start = 0;
		int size = 200;

		List<Long> schoolIds = taskDoQuestionRankService.statisSchoolRank(startInt, endInt, start, size);
		while (CollectionUtils.isNotEmpty(schoolIds)) {
			// 取下一部分数据
			start = start + size;
			schoolIds = taskDoQuestionRankService.statisSchoolRank(startInt, endInt, start, size);
		}

	}

	@Override
	public void refreshSchoolData(int startInt, int endInt) {
		int fetchCount = 200;
		CursorPage<Long, DoQuestionSchoolRank> rankPage = taskDoQuestionRankService
				.refreshSchoolData(startInt, endInt, fetchCount, Long.MAX_VALUE);
		while (rankPage.isNotEmpty()) {
			// 取下一页数据
			rankPage = taskDoQuestionRankService.refreshSchoolData(startInt, endInt, fetchCount,
					rankPage.getNextCursor());
		}
	}

}

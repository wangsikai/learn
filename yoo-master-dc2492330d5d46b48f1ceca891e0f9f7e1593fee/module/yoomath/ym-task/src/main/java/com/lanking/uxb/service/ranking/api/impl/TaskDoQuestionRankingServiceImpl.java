package com.lanking.uxb.service.ranking.api.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.yoomath.stat.DoQuestionClassStat;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.uxb.service.ranking.api.StaticDoQuestionClassStatService;
import com.lanking.uxb.service.ranking.api.StaticDoQuestionSchoolStatService;
import com.lanking.uxb.service.ranking.api.StaticDoQuestionStudentStatService;
import com.lanking.uxb.service.ranking.api.TaskDoQuestionRankingService;

@Service
public class TaskDoQuestionRankingServiceImpl implements TaskDoQuestionRankingService {

	@Autowired
	private StaticDoQuestionStudentStatService staticDoQuestionStudentStatService;
	@Autowired
	private StaticDoQuestionClassStatService staticDoQuestionClassStatService;
	@Autowired
	private StaticDoQuestionSchoolStatService staticDoQuestionSchoolStatService;

	@SuppressWarnings("rawtypes")
	@Override
	public void staticDoQuestionStudentStat() {
		int fetchCount = 200;
		CursorPage<Long, Map> ids = staticDoQuestionStudentStatService.queryUserId(CP
				.cursor(Long.MAX_VALUE, fetchCount));
		while (ids.isNotEmpty()) {
			// 统计每个用户
			List<Long> userIds = new ArrayList<Long>(ids.getItemSize());
			for (Map map : ids) {
				userIds.add(((BigInteger) map.get("id")).longValue());
			}
			staticDoQuestionStudentStatService.staticDoQuestionStudentStat(7, userIds);
			staticDoQuestionStudentStatService.staticDoQuestionStudentStat(30, userIds);
			staticDoQuestionStudentStatService.staticDoQuestionStudentStat(365, userIds);
			// 获取下一页
			ids = staticDoQuestionStudentStatService.queryUserId(CP.cursor(ids.getNextCursor(), fetchCount));
		}
		staticDoQuestionStudentStatService.refreshDoQuestionStudentStat();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void staticDoQuestionClassStat() {
		int fetchCount = 20;
		CursorPage<Long, Map> ids = staticDoQuestionClassStatService
				.queryClassId(CP.cursor(Long.MAX_VALUE, fetchCount));
		while (ids.isNotEmpty()) {
			// 统计每个班级
			List<Long> classIds = new ArrayList<Long>(ids.getItemSize());
			List<Long> teacherIds = new ArrayList<Long>(ids.getItemSize());
			for (Map map : ids) {
				classIds.add(((BigInteger) map.get("id")).longValue());
				if (map.get("teacher_id") == null) {
					teacherIds.add(0L);
				} else {
					teacherIds.add(((BigInteger) map.get("teacher_id")).longValue());
				}
			}
			staticDoQuestionClassStatService.staticDoQuestionClassStat(7, classIds, teacherIds);
			staticDoQuestionClassStatService.staticDoQuestionClassStat(30, classIds, teacherIds);
			staticDoQuestionClassStatService.staticDoQuestionClassStat(365, classIds, teacherIds);
			// 获取下一页
			ids = staticDoQuestionClassStatService.queryClassId(CP.cursor(ids.getNextCursor(), fetchCount));
		}
		staticDoQuestionClassStatService.refreshDoQuestionClassStat();
	}

	@Override
	public void staticDoQuestionSchoolStat() {
		int fetchCount = 500;
		CursorPage<Long, DoQuestionClassStat> cpage = staticDoQuestionSchoolStatService.queryDoQuestionClassStat(CP
				.cursor(Long.MAX_VALUE, fetchCount));
		while (cpage.isNotEmpty()) {
			staticDoQuestionSchoolStatService.staticDoQuestionClassStat(cpage.getItems());
			// 获取下一页
			cpage = staticDoQuestionSchoolStatService.queryDoQuestionClassStat(CP.cursor(cpage.getNextCursor(),
					fetchCount));
		}
		staticDoQuestionSchoolStatService.refreshDoQuestionSchoolStat();
	}

}

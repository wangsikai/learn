package com.lanking.cloud.job;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.cloud.job.doQuestionRank.service.DoQuestionStudentRankService;
import com.lanking.cloud.job.doQuestionRank.service.TaskDoQuestionRankService;
import com.lanking.cloud.job.doQuestionRank.util.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DoQuestionRankJob implements SimpleJob {

	@Autowired
	private TaskDoQuestionRankService service;
	@Autowired
	private DoQuestionStudentRankService doQuestionStudentRankService;

	@Override
	public void execute(final ShardingContext shardingContext) {
		log.info("DoQuestionRankJob start.");
		int day = Integer.parseInt(shardingContext.getShardingParameter());

		Map<String, Integer> timeIntMap = DateUtil.getNowTimeInteger(day);
		int startInt = timeIntMap.get("startDate");
		int endInt = timeIntMap.get("endDate");
		Map<String, Date> timeMap = DateUtil.getNowTime(day);
		Date startDate = timeMap.get("startDate");
		Date endDate = timeMap.get("endDate");

		// 统计
		doQuestionStudentRankService.statStudentDoQuestion(startInt, endInt, startDate, endDate);
		// 班级榜排序
		Map<Long, String> pushMap = service.statisClassRank(startInt, endInt);
		// 班级榜刷新
		doQuestionStudentRankService.refreshClassData(startInt, endInt);
		// 班级榜发送推送消息
		service.sendClassRankingPush(pushMap);
		// 校级榜排序
		doQuestionStudentRankService.statisSchoolRank(startInt, endInt);
		// 校级榜刷新
		doQuestionStudentRankService.refreshSchoolData(startInt, endInt);

		log.info("DoQuestionRankJob end.");
	}
}

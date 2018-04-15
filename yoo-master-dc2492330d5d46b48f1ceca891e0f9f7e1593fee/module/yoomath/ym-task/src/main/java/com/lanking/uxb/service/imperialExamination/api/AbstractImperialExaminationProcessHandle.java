package com.lanking.uxb.service.imperialExamination.api;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivity;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcessTime;

public abstract class AbstractImperialExaminationProcessHandle implements ImperialExaminationProcessHandle {
	
	@Autowired
	private TaskImperialExaminationActivityService activityService;
	
	//表示乡试题目已下发
	public static final String PROVINCIAL_PUBLISHED = "provincial_published";
	//表示会试题目已下发
	public static final String METROPOLITAN_PUBLISHED = "metropolitan_published";
	//表示殿试题目已下发
	public static final String FINAL_PUBLISHED = "final_published";
	
	//表示乡试题目状态已修改
	public static final String PROVINCIAL_STATUS_CHANGED = "provincial_status_changed";
	//表示会试题目状态已修改
	public static final String METROPOLITAN_STATUS_CHANGED= "metropolitan_status_changed";
	//表示殿试题目状态已修改
	public static final String FINAL_STATUS_CHANGED = "final_status_changed";
		
	//表示会试奖券已处理
	public static final String PROVINCIAL_LOTTERY_PROCESSED = "provincial_lottery_processed";
	//表示会试奖券已处理
	public static final String METROPOLITAN_LOTTERY_PROCESSED = "metropolitan_lottery_processed";
	//表示殿试奖券已处理
	public static final String FINAL_LOTTERY_PROCESSED = "final_lottery_processed";
	//表示当前正在处理分数的统计
	public static final String PROCESS_SCORE = "process score process ";
	//表示当前殿试完成，总成绩已处理
	public static final String FINAL_SCORE_PROCESSED = "final_score_processed";
	
	//表示当前排名完成，总排名已出来
	public static final String FINAL_RANK_PROCESSED = "final_rank_processed";

	//默认试卷都已下发，需要的子类自己实现
	@Override
	public boolean isPublished(long code) {
		return true;
	}
	
	//默认奖券都已处理，需要的子类自己实现
	@Override
	public boolean isLotteryProcessed(long code) {
		return true;
	}
	
	public boolean isCurrentProcess(long code) {
		// 获取活动
		ImperialExaminationActivity activity = activityService.get(code);
		List<ImperialExaminationProcessTime> timeList = activity.getCfg().getTimeList();

		Date startTime = null;
		Date endTime = null;
		for (ImperialExaminationProcessTime ptime : timeList) {
			// 获取当前阶段的开始时间和结束时间
			if (ptime.getProcess() == getProcess()) {
				startTime = ptime.getStartTime();
				endTime = ptime.getEndTime();
				break;
			}
		}

		// 判断当前时间是否在当前阶段
		long now = System.currentTimeMillis();
		if (now < startTime.getTime() || now > endTime.getTime()) {
			return false;
		}
		
		return true;
	}
	
}

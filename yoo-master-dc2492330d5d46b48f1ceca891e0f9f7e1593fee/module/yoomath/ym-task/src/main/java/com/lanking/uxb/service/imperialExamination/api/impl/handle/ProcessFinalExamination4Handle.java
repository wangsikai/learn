package com.lanking.uxb.service.imperialExamination.api.impl.handle;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivity;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityProcessLog;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationExamTag;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcess;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcessTime;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;
import com.lanking.uxb.service.imperialExamination.api.AbstractImperialExaminationProcessHandle;
import com.lanking.uxb.service.imperialExamination.api.TaskActivityHomeworkService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityLotteryService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityProcessLogService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityRankStatService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityService;

import httl.util.CollectionUtils;

/**
 * 殿试成绩调整阶段
 * */
@Component
public class ProcessFinalExamination4Handle extends AbstractImperialExaminationProcessHandle {
	@Autowired
	private TaskImperialExaminationActivityRankStatService imperialExaminationActivityRankStatService;
	@Autowired
	private TaskImperialExaminationActivityProcessLogService activityProcessLogService;
	@Autowired
	private TaskActivityHomeworkService homeworkService;
	@Autowired
	private TaskImperialExaminationActivityLotteryService lotteryService;
	@Autowired
	private TaskImperialExaminationActivityService activityService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public ImperialExaminationProcess getProcess() {
		return ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL4;
	}
	
	@Override
	public boolean isLotteryProcessed(long code) {
		ImperialExaminationActivityProcessLog log = activityProcessLogService.get(code, getProcess(),
				FINAL_LOTTERY_PROCESSED);
		if (log != null) {
			return true;
		}

		return false;
	}
	
	public boolean isProcessed(long code) {
		ImperialExaminationActivityProcessLog log = activityProcessLogService.get(code, getProcess(),FINAL_SCORE_PROCESSED);
		if (log != null && log.getSuccess() == true) {
			return true;
		} 
		
		return false;
	}

	@Override
	public synchronized void process(long code) {	
		//不是当前阶段，不处理
		if(!isCurrentProcess(code)){
			return;
		}

		if (!isLotteryProcessed(code)) {
			ImperialExaminationActivityProcessLog activityLog = new ImperialExaminationActivityProcessLog();
			activityLog.setActivityCode(code);
			activityLog.setProcess(getProcess());
			activityLog.setStartTime(new Date());
			// 获取活动
			ImperialExaminationActivity activity = activityService.get(code);
			List<ImperialExaminationProcessTime> timeList = activity.getCfg().getTimeList();

			// 1.统计所有imperial_exam_homework表中规定时间内已下发的作业
			Date issueStartTime = null;
			Date issueEndTime = null;
			for (ImperialExaminationProcessTime ptime : timeList) {
				// 答题开始时间
				if (ptime.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL2) {
					issueStartTime = ptime.getStartTime();
					break;
				}
				// 批改下发时间
				if (ptime.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL3) {
					issueEndTime = ptime.getEndTime();
					break;
				}
			}
			// 给老师发奖券
			List<Long> userIds = homeworkService.getIssueHomeworkUserId(code, issueStartTime, issueEndTime,
					ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL5);

			if (CollectionUtils.isNotEmpty(userIds)) {
				for (Long id : userIds) {
					lotteryService.addLottery(id, ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL5, code);
				}
			}

			// 给学生发奖券,条件是学生在规定的时间内下发作业并且正确率不为0
			List<Long> studentIds = homeworkService.getIssuedHomeworkStudentId(code, issueStartTime,
					issueEndTime);

			if (CollectionUtils.isNotEmpty(studentIds)) {
				for (Long id : studentIds) {
					lotteryService.addLottery(id, ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL5, code);
				}
			}

			activityLog.setProcessData(FINAL_LOTTERY_PROCESSED);
			activityLog.setSuccess(true);
			activityLog.setEndTime(new Date());
			// 更新日志
			activityProcessLogService.create(activityLog);
		}

		//如果已经处理过
		if(isProcessed(code)){
			return;
		}
		
		ImperialExaminationActivityProcessLog activityLog = new ImperialExaminationActivityProcessLog();
		activityLog.setActivityCode(code);
		activityLog.setProcess(getProcess());
		activityLog.setStartTime(new Date());
		try {
			//殿试只有一次考试，没有冲刺1，冲刺2
			imperialExaminationActivityRankStatService.countRank(code,
					ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION,ImperialExaminationExamTag.FIRST_EXAM.getValue(),ImperialExaminationExamTag.FIRST_EXAM.getValue());
			try {
				imperialExaminationActivityRankStatService.statActivityAward(code);
				activityLog.setProcessData(FINAL_SCORE_PROCESSED);
				activityLog.setSuccess(true);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("PROCESS_FINAL_EXAMINATION4 fail:",e);
				activityLog.setProcessData("综合成绩统计异常");
				activityLog.setSuccess(false);
			}
		} catch (Exception e) {
			logger.error("PROCESS_FINAL_EXAMINATION4 fail:",e);
			activityLog.setProcessData("殿试成绩处理异常");
			activityLog.setSuccess(false);
		}
		activityLog.setEndTime(new Date());
		// 更新日志
		activityProcessLogService.create(activityLog);

	}
}

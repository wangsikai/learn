package com.lanking.uxb.service.imperialExamination.api.impl.handle;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityAward;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityAwardStudent;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityProcessLog;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcess;
import com.lanking.uxb.service.imperialExamination.api.AbstractImperialExaminationProcessHandle;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityAwardService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityAwardStudentService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityProcessLogService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityRankStatService;

/**
 * 总成绩调整阶段
 * */
@Component
public class ProcessRankingHandle extends AbstractImperialExaminationProcessHandle {

	@Autowired
	private TaskImperialExaminationActivityAwardService awardService;
	
	@Autowired
	private TaskImperialExaminationActivityAwardStudentService awardStudentService;

	@Autowired
	private TaskImperialExaminationActivityRankStatService activityRankStatService;
	@Autowired
	private TaskImperialExaminationActivityProcessLogService activityProcessLogService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public ImperialExaminationProcess getProcess() {
		return ImperialExaminationProcess.PROCESS_TOTALRANKING;
	}
	
	public boolean isProcessed(long code) {
		ImperialExaminationActivityProcessLog log = activityProcessLogService.get(code, getProcess(),FINAL_RANK_PROCESSED);
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
		//如果已经处理过
		if(isProcessed(code)){
			return;
		}
				
		ImperialExaminationActivityProcessLog activityLog = new ImperialExaminationActivityProcessLog();
		activityLog.setActivityCode(code);
		activityLog.setProcess(getProcess());
		activityLog.setStartTime(new Date());
		List<ImperialExaminationActivityAward> awardsFristRoom = awardService.queryRank(code,1);
		List<ImperialExaminationActivityAward> awardsSecondRoom = awardService.queryRank(code,2);
		
		List<ImperialExaminationActivityAwardStudent> awardsStudent = awardStudentService.queryRank(code);
		try {
			activityRankStatService.setAwardRank(awardsFristRoom,1);
			activityRankStatService.setAwardRank(awardsSecondRoom,2);
			activityRankStatService.setAwardRankStudent(awardsStudent);
			activityLog.setProcessData(FINAL_RANK_PROCESSED);
			activityLog.setSuccess(true);
		} catch (Exception e) {
			logger.error("PROCESS_RANKS fail:", e);
			activityLog.setSuccess(false);
		}
		activityLog.setEndTime(new Date());
		// 更新日志
		activityProcessLogService.create(activityLog);
	}
}

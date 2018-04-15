package com.lanking.uxb.service.imperialExamination.api.impl.handle;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityProcessLog;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationExamTag;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcess;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;
import com.lanking.uxb.service.imperialExamination.api.AbstractImperialExaminationProcessHandle;
import com.lanking.uxb.service.imperialExamination.api.TaskActivityHomeworkService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityProcessLogService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityRankStatService;

/**
 * 会试答题阶段
 * 定时查询老师和学生的会试成绩并排名，这个阶段只会有首次考试的成绩
 * */
@Component
public class ProcessMetropolitan2Handle extends AbstractImperialExaminationProcessHandle {
	@Autowired
	private TaskImperialExaminationActivityProcessLogService activityProcessLogService;

	@Autowired
	private TaskImperialExaminationActivityRankStatService imperialExaminationActivityRankStatService;
	@Autowired
	private TaskActivityHomeworkService homeworkService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public ImperialExaminationProcess getProcess() {
		return ImperialExaminationProcess.PROCESS_METROPOLITAN2;
	}
	
	public boolean isStatusChanged(long code) {
		ImperialExaminationActivityProcessLog log = activityProcessLogService.get(code, getProcess(),METROPOLITAN_STATUS_CHANGED);
		if (log != null) {
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
		//判断是否已修改题目状态，如果没有修改题目状态，先修改题目状态，如果已修改，查询成绩并排名
		if(!isStatusChanged(code)){
			ImperialExaminationActivityProcessLog activityLog = new ImperialExaminationActivityProcessLog();
			activityLog.setActivityCode(code);
			activityLog.setProcess(getProcess());
			activityLog.setStartTime(new Date());
			//这里要修改已发布题目的删除状态
			homeworkService.modifyImperialHomeworkDelstatus(code);
			activityLog.setProcessData(METROPOLITAN_STATUS_CHANGED);
			activityLog.setSuccess(true);
			activityLog.setEndTime(new Date());
			// 更新日志
			activityProcessLogService.create(activityLog);
		} else {
			ImperialExaminationActivityProcessLog activityLog = new ImperialExaminationActivityProcessLog();
			activityLog.setActivityCode(code);
			activityLog.setProcess(getProcess());
			activityLog.setStartTime(new Date());
			//否则查询成绩并排名
			try {
				//这个阶段只需要生成首次考试的成绩
				imperialExaminationActivityRankStatService.countRank(code, ImperialExaminationType.METROPOLITAN_EXAMINATION,
								ImperialExaminationExamTag.FIRST_EXAM.getValue(),ImperialExaminationExamTag.FIRST_EXAM.getValue());
				activityLog.setSuccess(true);
			} catch (Exception e) {
				logger.error("PROCESS_METROPOLITAN2 fail:"+e);
				activityLog.setSuccess(false);
			}
			activityLog.setProcessData(PROCESS_SCORE + getProcess());
			activityLog.setEndTime(new Date());
			// 更新日志
			activityProcessLogService.create(activityLog);
		}

	}

}

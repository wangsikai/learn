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
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityProcessLogService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityRankStatService;

/**
 * 会试成绩公布
 * 定时查询老师和学生的会试成绩并排名，这个阶段可能会有首次考试、冲刺1、冲刺2的成绩
 * */
@Component
public class ProcessMetropolitan5Handle extends AbstractImperialExaminationProcessHandle {
	@Autowired
	private TaskImperialExaminationActivityRankStatService imperialExaminationActivityRankStatService;
	@Autowired
	private TaskImperialExaminationActivityProcessLogService activityProcessLogService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public ImperialExaminationProcess getProcess() {
		return ImperialExaminationProcess.PROCESS_METROPOLITAN5;
	}

	@Override
	public void process(long code) {
		// 不是当前阶段，不处理
		if (!isCurrentProcess(code)) {
			return;
		}

		ImperialExaminationActivityProcessLog activityLog = new ImperialExaminationActivityProcessLog();
		activityLog.setActivityCode(code);
		activityLog.setProcess(getProcess());
		activityLog.setStartTime(new Date());
		try {
			// 这个阶段需要生成冲刺1和冲刺2考试的成绩
			imperialExaminationActivityRankStatService.countRank(code, ImperialExaminationType.METROPOLITAN_EXAMINATION,
					ImperialExaminationExamTag.SECOND_EXAM.getValue(),
					ImperialExaminationExamTag.THIRD_EXAM.getValue());
			activityLog.setSuccess(true);
		} catch (Exception e) {
			logger.error("PROCESS_METROPOLITAN5 fail:"+e);
			activityLog.setSuccess(false);
		}
		activityLog.setProcessData(PROCESS_SCORE + getProcess());
		activityLog.setEndTime(new Date());
		// 更新日志
		activityProcessLogService.create(activityLog);
	}

}

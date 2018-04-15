package com.lanking.uxb.service.imperialExamination.api;

import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcess;
import com.lanking.cloud.domain.yoomath.homework.Homework;

/**
 * 作业分发
 * 
 * @author zemin.song
 */

public interface TaskActivityHomeworkService {
	/**
	 * 分发作业
	 * 
	 * @param createId
	 * @param clazzId
	 * @param startTime
	 * @param endTime
	 * @param homeWorkName
	 * @param questionIds
	 */
	Homework publish(long createId, long clazzId, long startTime, long endTime, List<Long> questionIds,
			String homeWorkName);

	/**
	 * 查询规定时间内下发作业的用户id
	 * 
	 * @param code
	 * @param startTime
	 * @param endTime
	 * @param process
	 */
	List<Long> getIssueHomeworkUserId(Long code, Date startTime, Date endTime,
			ImperialExaminationProcess process);
	
	/**
	 * 查询规定时间内下发作业且正确率不为0的学生id
	 * 
	 * @param code
	 * @param startTime
	 * @param endTime
	 */
	List<Long> getIssuedHomeworkStudentId(Long code, Date startTime, Date endTime);
	
	/**
	 * 修改提前下发的科举考试作业的删除状态
	 * 
	 * @param code
	 */
	void modifyImperialHomeworkDelstatus(Long code);
}

package com.lanking.uxb.service.imperial.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationHomeworkStudent;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;

/**
 * 科举活动考试作业(学生)相关接口
 * 
 * @author peng.zhao
 * @version 2017-11-16
 */
public interface ImperialExaminationHomeworkStudentService {

	void save(ImperialExaminationHomeworkStudent entity);

	void save(List<ImperialExaminationHomeworkStudent> entitys);

	/**
	 * 获取学生科举当前阶段对应的作业id集合,老师可能存在多个班级
	 * 
	 * @param code
	 *            活动code
	 * @param type
	 *            科举考试类型
	 * @param userId
	 *            当前老师用户id
	 * @param tag
	 *            1:原题目,2:冲刺题,3:冲刺题
	 * @param room
	 *            1:考场1,2:考场2
	 * @return
	 */
	List<ImperialExaminationHomeworkStudent> list(Long code, ImperialExaminationType type, Long userId, Integer tag,
			Integer room);

	/**
	 * 获取单个对象
	 * 
	 * @param id
	 * @return
	 */
	ImperialExaminationHomeworkStudent get(long id);
}

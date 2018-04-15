package com.lanking.uxb.service.imperial.api;

import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationHomework;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;

/**
 * 科举活动考试作业相关接口
 * 
 * @author wangsenhao
 *
 */
public interface ImperialExaminationHomeworkService {

	/**
	 * 获取老师科举当前阶段对应的作业id集合,老师可能存在多个班级
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
	List<ImperialExaminationHomework> list(Long code, ImperialExaminationType type, Long userId, Integer tag,
			Integer room);

	/**
	 * 获取单个对象
	 * 
	 * @param id
	 * @return
	 */
	ImperialExaminationHomework get(long id);

	/**
	 * 科举考试--当前老师，当前阶段试题是否全部已下发
	 * 
	 * @param code
	 * @param type
	 * @param userId
	 * @param flag
	 *            1查询是否有未下发的。2查询是否有已下发的
	 * @param tag
	 *            1:原题目,2:冲刺题,3:冲刺题
	 * @param room
	 *            1:考场1,2:考场2
	 * @return
	 */
	boolean isAllIssue(Long code, ImperialExaminationType type, Long userId, Integer tag, Integer room);

	/**
	 * 科举考试--当前老师，当前阶段试题是否全部已下发
	 * 
	 * @param code
	 * @param type
	 * @param userId
	 * @param flag
	 *            1查询是否有未下发的。2查询是否有已下发的
	 * @return
	 */
	boolean isExistIssue(Long code, ImperialExaminationType type, Long userId);

	/**
	 * 根据条件查询符合条件的试题个数
	 * 
	 * @param code
	 * @param type
	 * @param userId
	 * @param flag
	 *            1查询是否有未下发的。2查询是否有已下发的
	 * @return
	 */
	Integer countHk(Long code, ImperialExaminationType type, Long userId, Integer flag);

	/**
	 * 获取当前老师阶段任意一个homeworkId,不同homework作业是一样的，用于获取试题概览
	 * 
	 * @param code
	 * @param type
	 * @param userId
	 * @return
	 */
	long getHkId(Long code, ImperialExaminationType type, Long userId);

	/**
	 * 科举考试--当前老师，当前阶段试题是否全部已下发,关联tag
	 * 
	 * @param code
	 * @param type
	 * @param userId
	 * @param flag
	 *            1查询是否有未下发的。2查询是否有已下发的
	 * @param tag
	 *            1:原题目,2:冲刺题,3:冲刺题
	 * @param endTime 作业结束时间
	 * @return
	 */
	boolean isExistIssueByTag(Long code, ImperialExaminationType type, Long userId, Integer tag, Date endTime);

	/**
	 * 根据条件查询符合条件的试题个数
	 * 
	 * @param code
	 * @param type
	 * @param userId
	 * @param flag
	 *            1查询是否有未下发的。2查询是否有已下发的
	 * @param tag
	 *            1:原题目,2:冲刺题,3:冲刺题
	 * @return
	 */
	Integer countHkByTag(Long code, ImperialExaminationType type, Long userId, Integer flag, Integer tag, 
			Integer room);

	/**
	 * 创建一条记录
	 * 
	 * @param form
	 */
	void save(ImperialExaminationHomework homework);
}

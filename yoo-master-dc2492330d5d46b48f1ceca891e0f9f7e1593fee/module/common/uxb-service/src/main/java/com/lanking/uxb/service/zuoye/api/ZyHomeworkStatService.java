package com.lanking.uxb.service.zuoye.api;

import java.util.Collection;
import java.util.List;

import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoomath.stat.HomeworkStat;

/**
 * 悠作业:作业统计接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月14日
 */
public interface ZyHomeworkStatService {

	/**
	 * 查找作业统计数据
	 * 
	 * @since 2.1
	 * @param teacherId
	 *            教师ID
	 * @param homeworkClassId
	 *            作业班级ID
	 * 
	 * @return HomeworkStat
	 */
	HomeworkStat findOne(long teacherId, Long homeworkClassId);

	/**
	 * 发布作业后更新统计
	 * 
	 * @since 2.1
	 * @param teacherId
	 *            教师ID
	 * @param homeworkClassId
	 *            作业班级ID
	 * @param from
	 *            原始状态(定时分发作业)
	 * @param to
	 *            目标状态(初始布置作业或者定时分发作业)
	 */
	void updateAfterPublishHomework(long teacherId, Long homeworkClassId, HomeworkStatus from, HomeworkStatus to);

	/**
	 * @since 2.1
	 * @since 小悠快批，2018-2-26，方法名称暂不改，但新流程中已不在“下发”过程中调用
	 * 
	 * @param teacherId
	 *            教师ID
	 * @param homeworkClassId
	 *            作业班级ID
	 */
	void updateAfterIssueHomework(long teacherId, Long homeworkClassId);

	/**
	 * 根据作业班级ID获取作业统计
	 * 
	 * @since yoomath V1.2
	 * @param homeworkClassId
	 *            作业班级ID
	 * @return HomeworkStat
	 */
	HomeworkStat getByHomeworkClassId(long homeworkClassId);

	/**
	 * 根据作业班级IDs获取作业统计
	 * 
	 * @since yoomath V1.2
	 * @param homeworkClassIds
	 *            作业班级ID
	 * @return List
	 */
	List<HomeworkStat> getByHomeworkClassIds(Collection<Long> homeworkClassIds);

}

package com.lanking.uxb.service.zuoye.api;

import com.lanking.cloud.domain.yoomath.homework.Homework;

/**
 * 作业统计接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月21日
 */
public interface ZyHomeworkStatisticService extends ZyHomeworkStatistic2Service {

	void asyncStaticHomework(long homeworkId);

	/**
	 * 删除作业后调用
	 * 
	 * @since yoomath V1.4
	 * @param homeworkId
	 *            作业ID
	 */
	void asyncDeleteHomework(long homeworkId);

	/**
	 * 统计作业的正确率(最后一个人提交后)
	 * 
	 * @since 2.1
	 * @since 小优快批,2018-2-26，自动批改过程转移至新的统一流程中
	 * 
	 * @param homeworkId
	 *            作业ID
	 */
	@Deprecated
	void staticAfterLastCommit(long homeworkId);

	/**
	 * 一个学生作业自动批改后统计（废弃）
	 * 
	 * @since 2.1
	 * @since 小优快批,2018-2-26，自动批改过程转移至新的统一流程中
	 * 
	 * @param stuHkId
	 *            学生作业ID
	 */
	@Deprecated
	void staticAfterAutoCorrect(long stuHkId);

	/**
	 * 更新班级作业统计
	 * 
	 * @since 2.1
	 * @since 教师端 v1.3.0 2017-7-5 学生退出班级不再参与班级的整体统计，班级统计来源于学生统计
	 * 
	 * @param homeworkId
	 *            作业ID
	 */
	void updateTeacherHomeworkStat(long homeworkId);

	/**
	 * 更新班级作业统计（直接统计班级，学生加入或者离开班级时，需要重新统计）
	 * 
	 * @since 教师端 v1.3.0 2017-7-5 学生退出班级不再参与班级的整体统计，班级统计来源于学生统计
	 * @param clazzId
	 *            班级ID
	 */
	void updateTeacherHomeworkStatByClass(long clazzId);

	/**
	 * 更新学生作业统计
	 * 
	 * @since 2.1
	 * @param homeworkId
	 *            作业ID
	 */
	void updateStudentHomeworkStat(long homeworkId);

	/**
	 * 为此次作业的学生作业排名
	 * 
	 * @since 2.1
	 * @param homeworkId
	 *            作业ID
	 */
	void rankingStudentHomework(long homeworkId);

	/**
	 * 为此次作业所属课程或者班级里面的学生排名
	 * 
	 * @since 2.1
	 * @param homework
	 *            作业ID
	 */
	void rankingStudent(Homework homework);
}

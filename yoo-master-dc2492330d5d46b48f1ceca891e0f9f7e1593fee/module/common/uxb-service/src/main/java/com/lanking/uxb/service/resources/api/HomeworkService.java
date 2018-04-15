package com.lanking.uxb.service.resources.api;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.uxb.service.resources.ex.HomeworkException;
import com.lanking.uxb.service.resources.form.HomeworkForm;

/**
 * 提供作业相关接口
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月14日
 */
public interface HomeworkService {

	/**
	 * 获取作业
	 * 
	 * @param id
	 *            作业ID
	 * @return {@link Homework}
	 */
	Homework get(long id);

	/**
	 * 批量获取作业.
	 * 
	 * @param ids
	 *            作业ID集合
	 * @return {@link Homework}
	 */
	Map<Long, Homework> mget(Collection<Long> ids);

	/**
	 * 批量获取作业.
	 * 
	 * @param ids
	 *            作业ID集合
	 * @return
	 */
	List<Homework> mgetList(Collection<Long> ids);

	/**
	 * 获取作业(不存在时会报异常)
	 * 
	 * @param id
	 *            作业ID
	 * @return {@link Homework}
	 */
	Homework load(long id) throws HomeworkException;

	/**
	 * 创建作业
	 * 
	 * @param form
	 *            参数
	 * @return Homework
	 * @throws HomeworkException
	 */
	Homework create(HomeworkForm form) throws HomeworkException;

	/**
	 * 设置作业时间
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @param startTime
	 *            开始时间
	 * @param deadline
	 *            终止时间
	 * @return {@link Homework}
	 * @throws HomeworkException
	 */
	Homework setTime(long homeworkId, long startTime, long deadline) throws HomeworkException;

	/**
	 * 更新作业状态
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @param status
	 *            状态
	 * @return {@link Homework}
	 * @throws HomeworkException
	 */
	void updateStatus(long homeworkId, HomeworkStatus status) throws HomeworkException;

	/**
	 * 查询应该发布的作业
	 * 
	 * @param now
	 *            时间
	 * @param cpr
	 *            分页条件
	 * @return CursorPage
	 */
	CursorPage<Long, Homework> queryShouldPublish(Date now, CursorPageable<Long> cpr);

	/**
	 * 查询应该提交的作业
	 * 
	 * @param now
	 *            时间
	 * @param cpr
	 *            分页条件
	 * @return CursorPage
	 */
	CursorPage<Long, Homework> queryShouldCommit(Date now, CursorPageable<Long> cpr);

	/**
	 * 查询应该下发的作业
	 * 
	 * @since 小优快批，2018-2-26，新批改流程没有下发过程
	 * 
	 * @param now
	 *            时间
	 * @param issueHour
	 *            下发小时数
	 * @param cpr
	 *            分页条件
	 * @return CursorPage
	 */
	@Deprecated
	CursorPage<Long, Homework> queryShouldIssue(Date now, int issueHour, CursorPageable<Long> cpr);

	/**
	 * 查询
	 * 
	 * @param cpr
	 * @return
	 */
	CursorPage<Long, Homework> queryShouldCalRightRate(CursorPageable<Long> cpr);

	/**
	 * 根据课程ID获取作业列表
	 * 
	 * @param courseId
	 *            课程ID
	 * @param status
	 *            作业状态
	 * @return 作业列表
	 * @throws HomeworkException
	 */
	List<Homework> findByCourseId(long courseId, HomeworkStatus status) throws HomeworkException;

	/**
	 * 下发作业
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @throws HomeworkException
	 */
	@Deprecated
	void issue(long homeworkId) throws HomeworkException;

	/**
	 * 分页获取课程作业
	 * 
	 * @param courseId
	 *            课程ID
	 * @param keyword
	 *            名称模糊搜索
	 * @param cpr
	 *            分页条件
	 * @return page
	 */
	CursorPage<Long, Homework> findByCourseId(long courseId, String keyword, CursorPageable<Long> cpr);

	/**
	 * 发作作业
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @throws HomeworkException
	 */
	void publishHomework(long homeworkId) throws HomeworkException;

	/**
	 * 保存作业正确率
	 * 
	 * @param id
	 *            作业ID
	 * @param rightRate
	 *            正确率
	 */
	void saveRightRate(long id, double rightRate);

	/**
	 * 更新作业名称
	 * 
	 * @param id
	 *            作业ID
	 * @param name
	 *            作业名称
	 * @return 更新后的作业
	 */
	Homework updateName(long id, String name);

	/**
	 * 删除作业
	 * 
	 * @param id
	 *            作业ID
	 */
	void delete(long id) throws HomeworkException;

	/**
	 * 增加作业的提交数量
	 * 
	 * @since 2.1
	 * @param id
	 *            作业ID
	 */
	void incrCommitCount(long id);

	/**
	 * 获取教师最早的作业、假期作业创建时间
	 * 
	 * @param teacherId
	 * @return
	 */
	Date getFirstCreateAt(Long teacherId);

	/**
	 * 关闭自动下发
	 * 
	 * @param hkId
	 */
	void closeAutoIssued(long hkId);

	/**
	 * 获取自动下发时间<br>
	 * 1.如果全部都是选择题,则自动下发为截止日期<br>
	 * 2.如果有一道解答题，则不显示自动下发时间<br>
	 * 3.没有解答题，有填空题，此填空题没有答题&&有解题过程上传，则显示已关闭自动下发<br>
	 * 4.没有解答题，有填空题，有答题||没有上传解题过程，则显示截至日期+24h
	 * 
	 * 2016.12.19 wangsenhao新加
	 * 
	 * @param hkId
	 * @param issueHour
	 * @return
	 */
	Date getAutoIssuedTime(long hkId, int issueHour);

	/**
	 * 更新作业截止时间
	 *
	 * @since 3.9.2
	 * @param hkId
	 *            作业id
	 * @param deadline
	 *            结束时间
	 */
	void setDeadline(long hkId, Date deadline);

	/**
	 * 设置自动下发开关
	 *
	 * @param hkId
	 *            作业id
	 * @param autoIssue
	 *            自动下发开关 true -> 开启 false -> 关闭
	 */
	void setAutoIssued(long hkId, boolean autoIssue);

	/**
	 * 游标查询需要推送是否更改截止时间的作业
	 *
	 * @param now
	 *            当前时间
	 * @param cursor
	 *            游标
	 * @return {@link CursorPage}
	 */
	CursorPage<Long, Homework> queryShouldDelay(Date now, CursorPageable<Long> cursor);

	/**
	 * 设置作业时间
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @param startTime
	 *            开始时间
	 * @param deadline
	 *            终止时间
	 * @param timeLimit
	 *            限时设置,null说明没有限时
	 * @return {@link Homework}
	 * @throws HomeworkException
	 */
	Homework setTimeAndTimelimit(long homeworkId, long startTime, long deadline, Integer timeLimit)
			throws HomeworkException;
}

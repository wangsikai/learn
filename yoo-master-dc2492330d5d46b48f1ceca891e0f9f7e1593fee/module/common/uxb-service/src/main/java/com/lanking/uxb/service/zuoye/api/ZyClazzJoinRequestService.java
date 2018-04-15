package com.lanking.uxb.service.zuoye.api;

import java.util.Date;

import com.lanking.cloud.domain.yoomath.clazz.ClazzJoinRequest;
import com.lanking.cloud.domain.yoomath.clazz.ClazzJoinRequestStatus;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

/**
 * 学生加入班级请求Service
 *
 * @author xinyu.zhou
 * @since 3.0.2
 */
public interface ZyClazzJoinRequestService {
	/**
	 * 用户加入班级确认请求, 若学生请求的数据有待老师确认的则直接更新update_at时间。 若学生请求的数据中无待确认的，则插入一条新的数据。
	 *
	 * @param studentId
	 *            学生id
	 * @param homeworkClassId
	 *            班级id
	 * @param teacherId
	 *            教师id
	 * @param realName
	 *            学生当时的真实姓名
	 */
	void request(long studentId, long homeworkClassId, long teacherId, String realName);

	/**
	 * 更新请求状态
	 *
	 * @param id
	 *            用户id
	 * @param status
	 *            状态值
	 */
	void updateRequestStatus(long id, ClazzJoinRequestStatus status);

	/**
	 * 查询老师对应的学生加入班级申请
	 * 
	 * @param teacherId
	 * @param pageable
	 * @return
	 */
	Page<ClazzJoinRequest> list(long teacherId, Pageable pageable);

	/**
	 * 游标查询老师对应的学生加入班级申请
	 *
	 * @param teacherId
	 *            老师id
	 * @param cursorPageable
	 *            游标分页参数
	 * @return {@link CursorPage}
	 */
	CursorPage<Long, ClazzJoinRequest> list(long teacherId, CursorPageable<Long> cursorPageable);

	/**
	 * 从某一个时间点开始，有多少申请加入的学生
	 * 
	 * @param teacherId
	 * @param startTime
	 * @return
	 */
	Long requestCount(long teacherId, Date startTime);

	ClazzJoinRequest get(long id);

	/**
	 * 删除请求
	 *
	 * @param id
	 *            请求数据id
	 */
	void delete(long id);

}

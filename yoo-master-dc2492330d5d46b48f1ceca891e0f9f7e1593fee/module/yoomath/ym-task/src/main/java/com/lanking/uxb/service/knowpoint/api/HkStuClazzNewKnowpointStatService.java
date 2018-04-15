package com.lanking.uxb.service.knowpoint.api;

import java.util.Date;

import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

/**
 * 普通作业，学生知识点统计(新知识点)
 * 
 * @since 2.6.0
 * @author wangsenhao
 *
 */
public interface HkStuClazzNewKnowpointStatService {

	/**
	 * 下发作业后统计此次作业的知识点数据
	 * 
	 * @since 2.6.0
	 * @param homeworkId
	 */
	void statisticAfterHomework(long homeworkId);

	/**
	 * 分页查询已下发的作业
	 *
	 * @param cursorPageable
	 *            分页条件
	 * @return 分页查询结果
	 */
	CursorPage<Long, Long> findIssueHkIds(CursorPageable<Long> cursorPageable, Date nowTime);

	/**
	 * 删除
	 */
	void deleteNewAll();

}

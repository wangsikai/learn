package com.lanking.cloud.job.doQuestionRank.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.stat.DoQuestionClassRank;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionSchoolRank;
import com.lanking.cloud.sdk.data.CursorPage;

/**
 * 答题数量排行榜数据接口
 * 
 * @since 1.4.7
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年9月8日
 */
public interface TaskDoQuestionRankService {

	/**
	 * 统计班级和校级做题榜
	 * 
	 * @param startInt
	 * @param endInt
	 * @param startDate
	 * @param endDate
	 */
	void doQuestionStudentRank(int startInt, int endInt, Date startDate, Date endDate, List<Long> userIds);

	/**
	 * 统计完成后刷新班级榜数据
	 * 
	 * @param startInt
	 * @param endInt
	 */
	CursorPage<Long, DoQuestionClassRank> refreshClassData(int startInt, int endInt, long cursor, int fetchCount);

	/**
	 * 统计班级榜排名
	 * 
	 * @param startInt
	 * @param endInt
	 * 
	 * @return 推送队列
	 */
	Map<Long, String> statisClassRank(int startInt, int endInt);

	/**
	 * 统计校级榜排名
	 * 
	 * @param startInt
	 * @param endInt
	 */
	List<Long> statisSchoolRank(int startInt, int endInt, int start, int size);

	/**
	 * 统计完成后刷新校级榜数据
	 * 
	 * @param startInt
	 * @param endInt
	 */
	CursorPage<Long, DoQuestionSchoolRank> refreshSchoolData(int startDate, int endDate, int fetchCount, long cursor);

	/**
	 * 班级排行榜发送推送
	 * 
	 * @param map key:班级id value:学生id列表
	 */
	void sendClassRankingPush(Map<Long, String> pushMap);

	CursorPage<Long, Map> queryUserId(int fetchCount, long cursor);

	CursorPage<Long, DoQuestionClassRank> getAllRankPraiseByCursor(int startDate, int endDate,
			int fetchCount, long cursor);

	/**
	 * 取指定时间范围内的所有学校列表
	 */
	List<Long> getSchoolIds(int startDate, int endDate, int start, int size);

	/**
	 * 查询指定时间的校级榜数据
	 * 
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @param pageable
	 */
	CursorPage<Long, DoQuestionSchoolRank> getSchoolRankPraiseBySchoolId(int startDate, int endDate, Long schoolId,
			int fetchCount, long cursor);
}

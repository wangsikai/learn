package com.lanking.cloud.job.doQuestionRank.DAO;

import java.util.List;

import com.lanking.cloud.domain.yoomath.stat.DoQuestionClassRank;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

/**
 * DoQuestionClassRank 相关数据库操作接口
 * 
 * @since 1.4.7
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年9月12日
 */
public interface DoQuestionClassRankDAO {

	/**
	 * 查询个人的排行信息
	 * 
	 * @param classId
	 * @param startDate
	 * @param endDate
	 * @param userId
	 * @return
	 */
	DoQuestionClassRank findStudentInClassRank(long classId, int startDate, int endDate, long userId);

	/**
	 * 保存数据
	 * 
	 * @param doQuestionClassRank
	 */
	DoQuestionClassRank save(DoQuestionClassRank doQuestionClassRank);

	/**
	 * 取指定时间范围内的所有班级列表
	 */
	List<Long> getClassIds(int startDate, int endDate, int start, int size);

	/**
	 * 批量查询查询个人的排行信息
	 * 
	 * @param classId
	 * @param startDate
	 * @param endDate
	 * @param userId
	 * @return
	 */
	List<DoQuestionClassRank> findStudentInClassRanks(List<Long> classIds, int startDate, int endDate);

	/**
	 * 保存数据
	 * 
	 * @param doQuestionClassRanks
	 */
	List<DoQuestionClassRank> saves(List<DoQuestionClassRank> doQuestionClassRanks);

	/**
	 * 游标查询所有指定时间段的数据数据
	 * 
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @param pageable
	 */
	CursorPage<Long, DoQuestionClassRank> getAllRankPraiseByCursor(int startDate, int endDate,
			CursorPageable<Long> pageable);
}

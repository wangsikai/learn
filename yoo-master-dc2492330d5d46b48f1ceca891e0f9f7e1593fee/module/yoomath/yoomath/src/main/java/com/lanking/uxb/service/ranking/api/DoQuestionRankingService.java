package com.lanking.uxb.service.ranking.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.stat.DoQuestionClassStat;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionSchoolStat;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

/**
 * 答题排名接口
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月24日
 */
public interface DoQuestionRankingService {

	List<DoQuestionClassStat> listDoQuestionClassStatTopN(DoQuestionRankingQuery query, int topN);

	List<DoQuestionSchoolStat> listDoQuestionSchoolStatTopN(DoQuestionRankingQuery query, int topN);

	DoQuestionClassStat findStudentInClassStat(DoQuestionRankingQuery query);

	/**
	 * 排行榜学校排行分页
	 * 
	 * @param query
	 * @param p
	 * @return
	 */
	Page<DoQuestionSchoolStat> query(DoQuestionRankingQuery query, Pageable p);

	/**
	 * 批量查询学生所在班的
	 * 
	 * @param classIds
	 *            班级ID集合
	 * @param studentId
	 * @param day
	 *            查询的是多少天的题目统计
	 * @return
	 */
	Map<Long, DoQuestionClassStat> mgetDoQuestionClassStat(List<Long> classIds, Long studentId, int day);

}

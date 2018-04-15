package com.lanking.uxb.service.ranking.api;

/**
 * 答题数量排行榜数据接口
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月23日
 */
public interface TaskDoQuestionRankingService {

	void staticDoQuestionStudentStat();

	void staticDoQuestionClassStat();

	void staticDoQuestionSchoolStat();

}

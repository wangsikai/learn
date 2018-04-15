package com.lanking.uxb.service.imperial.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityRank;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;

/**
 * 科举活动--科举活动排名表
 * 
 * @author wangsenhao
 * @version 2017年4月6日
 */
public interface ImperialExaminationActivityRankService {
	/**
	 * 获取老师最优排名数据
	 * 
	 * @param code
	 * @param type
	 * @param userId
	 * @return
	 */
	ImperialExaminationActivityRank queryBest(long code, ImperialExaminationType type, long userId);

	/**
	 * 查询当前阶段下前limit的数据<br>
	 * 1.老师如果有多个班级，只算一个最佳的 <br>
	 * 2.如果存在分数相同的，比较答题用时<br>
	 * 3.如果答题时间和分数都相同(这种情况可能性很小)，默认取第一个(UE柴林森定的)
	 * 
	 * @param code
	 * @param type
	 * @param limit
	 * @return
	 */
	List<Map> queryTopList(long code, ImperialExaminationType type, int limit);

	/**
	 * 获取当前老师最佳班级在当前阶段的排名
	 * 
	 * @param code
	 * @param type
	 * @param score
	 *            最佳班级的分数
	 * @param doTime
	 *            最佳班级的时间
	 * @return
	 */
	Long getRank(long code, ImperialExaminationType type, Integer score, Integer doTime);

	/**
	 * 查询当前阶段下前limit的数据<br>
	 * 1.老师如果有多个班级，只算一个最佳的 <br>
	 * 2.如果存在分数相同的，比较答题用时<br>
	 * 3.如果答题时间和分数都相同(这种情况可能性很小)，默认取第一个(UE柴林森定的)
	 * 
	 * @param code
	 * @param type
	 * @param limit
	 * @return
	 */
	List<Map> queryTopList2(long code, ImperialExaminationType type, int limit, int room);

	/**
	 * 获取当前老师最佳班级在当前阶段的排名
	 * 
	 * @param code
	 * @param type
	 * @param score
	 *            最佳班级的分数
	 * @param doTime
	 *            最佳班级的时间
	 * @return
	 */
	Long getRank2(long code, ImperialExaminationType type, Integer score, Integer doTime, Integer room);
}

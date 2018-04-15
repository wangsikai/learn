package com.lanking.uxb.service.imperial.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityRankStudent;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;

/**
 * 科举活动--学生排名
 * 
 * @author peng.zhao
 * @version 2017年11月24日
 */
public interface ImperialExaminationActivityRankServiceStudent {

	/**
	 * 获取学生最优排名数据
	 * 
	 * @param code
	 * @param type
	 * @param userId
	 * @return
	 */
	ImperialExaminationActivityRankStudent queryBest(long code, ImperialExaminationType type, long userId);

	/**
	 * 查询当前阶段下前limit的数据<br>
	 * 1.老师如果有多个班级，只算一个最佳的 <br>
	 * 2.如果存在分数相同的，比较答题用时<br>
	 * 3.如果答题时间和分数都相同(这种情况可能性很小)，默认取第一个
	 * 
	 * @param code
	 * @param type
	 * @param limit
	 * @return
	 */
	List<Map> queryTopList(long code, ImperialExaminationType type, int limit);

	/**
	 * 获取当前用户最佳班级在当前阶段的排名
	 * 
	 * @param code
	 * @param type
	 * @param score
	 *            最佳班级的分数
	 * @param doTime
	 *            最佳班级的时间
	 * @param submitAt
	 *            提交作业时间
	 * @return
	 */
	Long getRank(long code, ImperialExaminationType type, Integer score, Integer doTime, Date submitAt);
}

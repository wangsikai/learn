package com.lanking.uxb.service.recommend.api;

import java.util.List;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.uxb.service.recommend.form.ResourceRecommendForm;

/**
 * 教师习题推荐服务.
 * <p>
 * 习题缓存仅保留一天
 * </p>
 * 
 * @author wlche
 *
 */
public interface QuestionTeaRecommendService {

	/**
	 * 强制刷新习题（产生的习题会存入缓存中）.
	 * 
	 * @param teacherId
	 *            教师ID
	 */
	List<Question> refreshNewQuestions(long teacherId, ResourceRecommendForm form);

	/**
	 * 直接获取缓存中的习题（若缓存习题超过24点，则会自动强制刷新习题）
	 * 
	 * @param teacherId
	 * @return
	 */
	List<Question> getQuestionsFromCache(long teacherId);
}

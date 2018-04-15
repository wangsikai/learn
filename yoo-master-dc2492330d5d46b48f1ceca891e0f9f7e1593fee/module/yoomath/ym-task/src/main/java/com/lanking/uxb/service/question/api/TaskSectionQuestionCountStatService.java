package com.lanking.uxb.service.question.api;

/**
 * 章节题目统计数据调用接口
 * 
 * @since 2.6.0
 * @author wangsenhao
 *
 */
public interface TaskSectionQuestionCountStatService {

	/**
	 * 通过版本号统计章节题目数
	 * 
	 * @param version
	 */
	void chapterStat(int version);
}

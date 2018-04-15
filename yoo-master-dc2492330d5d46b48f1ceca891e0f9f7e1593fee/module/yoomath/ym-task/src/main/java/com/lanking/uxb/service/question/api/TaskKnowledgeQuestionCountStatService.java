package com.lanking.uxb.service.question.api;

/**
 * 知识点统计
 * 
 * @since 2.6.0
 * @author zemin.song
 *
 */
public interface TaskKnowledgeQuestionCountStatService {

	/**
	 * 通过版本号统计知识点
	 * 
	 * @param version
	 */
	void countKnowledge(int version);

}

package com.lanking.uxb.service.examPaper.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperTopic;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperTopicType;

/**
 * 组卷题型相关接口
 *
 * @author zemin.song
 */
public interface CustomExamTopicService {
	/**
	 * 通过组卷ID 获取题型,分类Map
	 * 
	 * @param examPaperId
	 *            组卷ID
	 * @return
	 */
	Map<CustomExampaperTopicType, CustomExampaperTopic> getTopicsMap(Long examPaperId);

	/**
	 * 由组卷ID获取 题型列表
	 * 
	 * @param examPaperId
	 * @return
	 */
	List<CustomExampaperTopic> getTopicsByExamPaperId(Long examPaperId);

	/**
	 * 创建题类
	 * 
	 * @param CustomExampaperTopic
	 * @return
	 */
	CustomExampaperTopic createCustomExampaperTopic(CustomExampaperTopic cet);

}

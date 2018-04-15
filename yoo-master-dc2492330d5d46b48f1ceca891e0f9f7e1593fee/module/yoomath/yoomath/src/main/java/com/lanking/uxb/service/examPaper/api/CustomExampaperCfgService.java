package com.lanking.uxb.service.examPaper.api;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperCfg;

/**
 * 组卷配置接口
 * 
 * @author xinyu.zhou
 */
public interface CustomExampaperCfgService {
	/**
	 * 查询试卷配置
	 *
	 * @param examPaperId
	 *            组卷ID
	 */
	CustomExampaperCfg get(long examPaperId);

	/**
	 * 批量询试卷配置
	 *
	 * @param list
	 *            <Long>
	 */
	Map<Long, CustomExampaperCfg> mget(Collection<Long> examPaperIds);

	/**
	 * 存储保存分值
	 *
	 */
	void saveCustomExampaperCfg(CustomExampaperCfg cfg);

}

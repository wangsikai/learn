package com.lanking.uxb.rescon.question.api;

import java.util.List;

/**
 * 知识点标签接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年12月19日
 */
public interface ResconTaggingManage {

	/**
	 * 提取知识点标签.
	 * 
	 * @param questionId
	 *            题目ID
	 * @return
	 */
	List<Long> extractKnowledges(long questionId);
}

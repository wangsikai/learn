package com.lanking.uxb.operation.wordml.api;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

public interface OpWordMLQuestionManage {

	/**
	 * 获得word缓存控制使用的习题.
	 * 
	 * @param type
	 *            0：直接从question表取，1：过滤questionWordMl表中已有的习题
	 * @param pageable
	 * @return
	 */
	Page<Question> wordMLQueryByPage(int type, Long minId, Pageable pageable);
}

package com.lanking.uxb.rescon.question.api;

import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.sdk.value.VPage;

/**
 * 相似题组基础题库服务接口.
 * 
 * @author wlche
 *
 */
public interface ResconQuestionSimilarBaseManage {

	/**
	 * 分页查询指定题目的相似题集合.
	 * 
	 * @param page
	 *            页码
	 * @param size
	 *            取题个数
	 * @param question
	 *            指定题目
	 * 
	 * @return
	 */
	VPage<Document> queryAllQuestionBase(int page, int size, Question question);
}

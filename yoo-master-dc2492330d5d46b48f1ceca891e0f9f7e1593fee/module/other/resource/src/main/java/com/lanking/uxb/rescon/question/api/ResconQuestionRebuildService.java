package com.lanking.uxb.rescon.question.api;

import java.util.Collection;
import java.util.List;

import com.lanking.cloud.component.searcher.api.Document;

public interface ResconQuestionRebuildService {

	/**
	 * 搜索标记习题相关的所有Latex输入数据.
	 * 
	 * @param questionIds
	 *            需要处理的习题ID集合
	 * @param rebuildQuestionIndexIds
	 *            需要重建索引的习题ID集合
	 */
	void handleKatexInputQuestions(Collection<Long> questionIds);

	/**
	 * 处理存在v3知识点的展示题、重复题相关习题数据.
	 * 
	 * @param questionIndexDocs
	 *            习题索引数据集合
	 */
	List<Long> handleV3SameQuestions(Collection<Document> questionIndexDocs);
}

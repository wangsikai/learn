package com.lanking.uxb.service.knowledge.api;

import java.util.List;

public interface KnowledgeHandleService {

	List<Long> findKpIsNullList();

	/**
	 * 通过作业id查询对应的题目新知识点
	 * 
	 * @param homeworkId
	 * @return
	 */
	List<Long> findNewKps(long homeworkId);

	/**
	 * 
	 * @param homeworkId
	 * @param kps
	 */
	void updateKp(long homeworkId, List<Long> kps);

	List<Long> findKpIsWrongList();

}

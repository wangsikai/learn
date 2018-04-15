package com.lanking.uxb.rescon.question.api;

import java.util.Collection;

/**
 * 习题标签对应关系.
 * 
 * @author wlche
 *
 */
public interface ResconQuestion2TagManage {

	/**
	 * 系统自动打标签.
	 * 
	 * @param questionIds
	 *            习题ID集合
	 * @param tagCode
	 *            标签CODE
	 */
	void systemAdd(Collection<Long> questionIds, long tagCode);

	/**
	 * 系统自动打标签.
	 * 
	 * @param questionIds
	 *            习题ID集合
	 * @param tagCode
	 *            标签CODE
	 */
	void systemDel(Collection<Long> questionIds, long tagCode);
}

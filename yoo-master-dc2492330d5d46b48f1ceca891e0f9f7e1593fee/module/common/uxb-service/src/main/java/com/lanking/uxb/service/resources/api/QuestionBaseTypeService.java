package com.lanking.uxb.service.resources.api;

import java.util.List;

/**
 * 题目题型与科目题型对应关系相关接口
 * 
 * @since yoomath V1.9.1
 * @author wangsenhao
 *
 */
public interface QuestionBaseTypeService {
	/**
	 * 通过题目题型获取科目题型code
	 * 
	 * @param questionCode
	 * @return
	 */
	List<Integer> findBaseCodeList(long questionCode);

}

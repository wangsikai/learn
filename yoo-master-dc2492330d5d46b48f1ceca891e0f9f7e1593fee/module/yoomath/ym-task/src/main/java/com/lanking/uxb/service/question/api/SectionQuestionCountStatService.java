package com.lanking.uxb.service.question.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.uxb.service.question.form.ChapterStatForm;

/**
 * 章节题目统计
 * 
 * @since 2.6.0
 * @author wangsenhao
 *
 */
public interface SectionQuestionCountStatService {

	/**
	 * 通过章节查询对应的题目数量(不同题目状态)<br>
	 * 2016.10.20跟陈霄确认，不需要通过供应商Id过滤题目
	 * 
	 * @param textbookCode
	 * @param version
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<Map> findQuestionStatBySections(Integer textbookCode, Integer version);

	/**
	 * 保存
	 * 
	 * @param list
	 * @param form
	 */
	@SuppressWarnings("rawtypes")
	void save(List<Map> list, ChapterStatForm form);

	/**
	 * 清空章节统计表
	 * 
	 * @param version
	 */
	void deleteStat(int version);

	/**
	 * 分页游标查询教材
	 * 
	 * @param pageable
	 * @return
	 */
	CursorPage<Long, Textbook> queryTextbookList(CursorPageable<Long> pageable);

}

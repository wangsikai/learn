package com.lanking.uxb.service.examPaper.api;

import java.util.List;

import com.lanking.uxb.service.examPaper.form.SmartExamPaperForm;

/**
 * 智能组卷相关接口(此类名跟表名没有关系)
 * 
 * @author wangsenhao
 *
 */
public interface SmartPaperService {
	/**
	 * 智能组卷拉取题目数据条件组装
	 * 
	 * @param form
	 * @return
	 */
	List<Long> queryQuestionsByIndex(SmartExamPaperForm form);

	/**
	 * 具体到索引里查询的接口
	 * 
	 * @param form
	 * @return
	 */
	List<Long> query(SmartExamPaperForm form);
}

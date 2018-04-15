package com.lanking.uxb.service.web.api;

import java.math.BigDecimal;
import java.util.List;

import com.lanking.uxb.service.web.form.PertinenceHomeworkForm;

/**
 * 针对性训练作业相关接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年12月8日
 */
public interface ZyTeaPertinenceHomeworkService {

	/**
	 * 根据规则获取针对性训练作业题目集合.
	 * 
	 * @param form
	 * @return
	 */
	List<Long> queryPertinenceHomeworkQuestions(PertinenceHomeworkForm form);

	/**
	 * 平均难度.
	 * 
	 * @param questionIds
	 *            题目ID集合.
	 * @return
	 */
	public BigDecimal getAvgDifficulty(List<Long> questionIds);
}

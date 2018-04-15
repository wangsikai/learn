package com.lanking.uxb.service.zuoye.api;

import com.lanking.cloud.sdk.value.VPage;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.zuoye.form.QuestionQueryForm;

/**
 * 公共题库接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年9月15日
 */
public interface ZyQuestionBankService {
	/**
	 * 查询公共题库的题目(不包含解答题)
	 * 
	 * @param form
	 * @return
	 */
	VPage<VQuestion> queryQuestionBankByIndex(QuestionQueryForm form);

	/**
	 * 查询公共题库的题目(包含解答题)
	 * 
	 * @since yoomath V1.9.1
	 * @param form
	 * @return
	 */
	VPage<VQuestion> queryQuestionBankByIndex2(QuestionQueryForm form);
}

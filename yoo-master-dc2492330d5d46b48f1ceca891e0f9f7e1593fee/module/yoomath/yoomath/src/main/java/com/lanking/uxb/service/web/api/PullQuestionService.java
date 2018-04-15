package com.lanking.uxb.service.web.api;

import java.util.List;

import com.lanking.uxb.service.zuoye.form.PullQuestionForm;

/**
 * 拉取题目接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月22日
 */
public interface PullQuestionService {

	/**
	 * 根据条件拉取题目
	 * 
	 * @since 2.1
	 * @param form
	 *            拉取题目相关参数
	 * @return 题目ID集合
	 */
	List<Long> pull(PullQuestionForm form);

}

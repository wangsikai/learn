package com.lanking.uxb.zycon.homework.api;

import java.util.Map;

import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.zycon.homework.form.QuestionCorrectLogForm;

/**
 * 作业批改记录
 *
 * @author xinyu.zhou
 * @since yoomath V1.7
 */
public interface ZycQuestionCorrectLogService {
	/**
	 * 分页查询当前的作业批改记录
	 *
	 * @param pageable
	 *            分页条件
	 * @param form
	 *            {@link QuestionCorrectLogForm}
	 * @return 分页数据
	 */
	Page<Map> page(Pageable pageable, QuestionCorrectLogForm form);

}

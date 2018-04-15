package com.lanking.uxb.zycon.homework.api;

import java.util.Map;

import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.zycon.homework.form.QuestionAppealRecordForm;

/**
 * 作业申述记录查询
 *
 * @author xinyu.zhou
 * @since yoomath V1.7
 */
public interface ZycQuestionAppealRecordService {
	/**
	 * 分页查询当前的题目申述记录
	 *
	 * @param pageable
	 *            分页条件
	 * @param form
	 *            {@link QuestionAppealRecordForm}
	 * @return 分页数据
	 */
	Page<Map> page(Pageable pageable, QuestionAppealRecordForm form);

}

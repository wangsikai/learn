package com.lanking.uxb.zycon.qs.api;

import com.lanking.cloud.domain.yoo.order.questionRecord.QuestionRecordOrder;
import com.lanking.cloud.domain.yoo.order.questionRecord.QuestionRecordOrderStatus;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.zycon.qs.form.QuestionRecordOrderQueryForm;

/**
 * 教师请求录题相关service
 *
 * @author xinyu.zhou
 * @since 2.6.0
 */
public interface ZycQuestionRecordOrderService {
	/**
	 * 分页查询教师请求数据
	 *
	 * @param form
	 *            {@link QuestionRecordOrderQueryForm}
	 * @return {Page}
	 */
	Page<QuestionRecordOrder> query(Pageable pageable, QuestionRecordOrderQueryForm form);

	/**
	 * 后台运营备注信息
	 *
	 * @param id
	 *            请求录题记录id
	 * @param message
	 *            备注信息
	 * @param status
	 *            {@link QuestionRecordOrderStatus}
	 * @param userId
	 *            后台管理员id
	 */
	void memo(long id, String message, QuestionRecordOrderStatus status, long userId);

	/**
	 * 查询待处理的数据量
	 *
	 * @return 待处理条数
	 */
	Long countDo();
}

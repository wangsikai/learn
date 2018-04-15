package com.lanking.uxb.service.school.api;

import com.lanking.cloud.domain.yoo.order.questionRecord.QuestionRecordOrder;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.service.school.form.QuestionRecordOrderCreateForm;

/**
 * 教师请求录题、录教辅Service
 *
 * @author xinyu.zhou
 * @since 2.6.0
 */
public interface QuestionRecordOrderService {
	/**
	 * 分页查询教师用户提交的请求
	 *
	 * @param pageable
	 *            {@link Pageable}
	 * @param userId
	 *            用户id
	 * @return {@link Page}
	 */
	Page<QuestionRecordOrder> page(Pageable pageable, long userId);

	/**
	 * 删除用户的请求数据
	 *
	 * @param id
	 *            记录id
	 */
	void delete(long id);

	/**
	 * 创建用户提交订单
	 *
	 * @param form
	 *            {@link QuestionRecordOrderCreateForm}
	 * @return {@link QuestionRecordOrder}
	 */
	QuestionRecordOrder create(QuestionRecordOrderCreateForm form, long userId);

	/**
	 * 关闭需求
	 *
	 * @param id
	 *            请求记录id
	 */
	void close(long id);

	/**
	 * 短信提醒运营人员
	 */
	void asyncNoticeUsers();
}

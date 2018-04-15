package com.lanking.uxb.zycon.qs.api.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.order.questionRecord.QuestionRecordOrder;
import com.lanking.cloud.domain.yoo.order.questionRecord.QuestionRecordOrderStatus;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.zycon.qs.api.ZycQuestionRecordOrderService;
import com.lanking.uxb.zycon.qs.form.QuestionRecordOrderQueryForm;

/**
 * @author xinyu.zhou
 * @since 2.6.0
 */
@Service
@Transactional
public class ZycQuestionRecordOrderServiceImpl implements ZycQuestionRecordOrderService {

	@Autowired
	@Qualifier("QuestionRecordOrderRepo")
	private Repo<QuestionRecordOrder, Long> repo;

	@Override
	public Page<QuestionRecordOrder> query(Pageable pageable, QuestionRecordOrderQueryForm form) {
		Params params = Params.param();
		if (StringUtils.isNotBlank(form.getAccountName())) {
			params.put("accountName", "%" + form.getAccountName() + "%");
		}

		if (StringUtils.isNotBlank(form.getSchoolName())) {
			params.put("schoolName", "%" + form.getSchoolName() + "%");
		}
		if (form.getStatus() != null) {
			params.put("status", form.getStatus().getValue());
		}
		if (StringUtils.isNotBlank(form.getStartTime())) {
			params.put("startTime", form.getStartTime());
		}
		if (StringUtils.isNotBlank(form.getEndTime())) {
			params.put("endTime", form.getEndTime());
		}

		return repo.find("$zycQuery", params).fetch(pageable);
	}

	@Override
	@Transactional
	public void memo(long id, String message, QuestionRecordOrderStatus status, long userId) {
		QuestionRecordOrder order = repo.get(id);
		if (order == null || order.getDelStatus() != Status.ENABLED) {
			throw new IllegalArgException();
		}

		if (status == QuestionRecordOrderStatus.NOT_CONTACT) {
			order.setMessage(message);
			order.setUpdateMessageAt(new Date());
		} else if (status == QuestionRecordOrderStatus.TERMINATE) {
			order.setCloseMessage(message);
			order.setCloseAt(new Date());
		}
		order.setOrderStatus(status);

		order.setUpdateAt(new Date());
		order.setUpdateId(userId);

		repo.save(order);
	}

	@Override
	public Long countDo() {
		return repo.find("$zycCountDo", Params.param()).count();
	}

}

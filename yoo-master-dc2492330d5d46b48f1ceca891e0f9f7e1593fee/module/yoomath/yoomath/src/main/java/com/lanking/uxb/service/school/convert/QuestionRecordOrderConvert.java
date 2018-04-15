package com.lanking.uxb.service.school.convert;

import com.lanking.cloud.domain.yoo.order.questionRecord.QuestionRecordOrder;
import com.lanking.cloud.domain.yoo.order.questionRecord.QuestionRecordOrderStatus;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.file.api.FileService;
import com.lanking.uxb.service.file.convert.FileConverter;
import com.lanking.uxb.service.school.value.VQuestionRecordOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * QuestionRecordOrder -> VQuestionRecordOrder
 *
 * @author xinyu.zhou
 * @since 2.6.0
 */
@Component
public class QuestionRecordOrderConvert extends Converter<VQuestionRecordOrder, QuestionRecordOrder, Long> {
	@Autowired
	private FileService fileService;
	@Autowired
	private FileConverter fileConverter;

	@Override
	protected Long getId(QuestionRecordOrder questionRecordOrder) {
		return questionRecordOrder.getId();
	}

	@Override
	protected VQuestionRecordOrder convert(QuestionRecordOrder questionRecordOrder) {
		VQuestionRecordOrder v = new VQuestionRecordOrder();
		v.setId(questionRecordOrder.getId());
		v.setDescription(questionRecordOrder.getDescription());
		v.setMessage(questionRecordOrder.getMessage());
		v.setMobile(questionRecordOrder.getMobile());
		v.setOrderAt(questionRecordOrder.getOrderAt());
		if (questionRecordOrder.getOrderStatus() == QuestionRecordOrderStatus.TERMINATE) {
			v.setStatus(QuestionRecordOrderStatus.CLOSE);
		} else {
			v.setStatus(questionRecordOrder.getOrderStatus());
		}
		v.setType(questionRecordOrder.getType());
		v.setAttachFiles(fileConverter.mgetList(questionRecordOrder.getAttachFiles()));
		v.setUpdateAt(questionRecordOrder.getUpdateAt());
		v.setUpdateMessageAt(questionRecordOrder.getUpdateMessageAt());
		v.setCloseAt(questionRecordOrder.getCloseAt());
		v.setCloseMessage(questionRecordOrder.getCloseMessage());

		return v;
	}

}

package com.lanking.uxb.service.school.api.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.message.api.SmsPacket;
import com.lanking.cloud.domain.yoo.order.questionRecord.QuestionRecordOrder;
import com.lanking.cloud.domain.yoo.order.questionRecord.QuestionRecordOrderStatus;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoomath.school.QuestionSchool;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.service.mall.api.GoodsOrderService;
import com.lanking.uxb.service.message.api.MessageSender;
import com.lanking.uxb.service.school.api.QuestionRecordOrderService;
import com.lanking.uxb.service.school.api.SchoolQuestionService;
import com.lanking.uxb.service.school.form.QuestionRecordOrderCreateForm;
import com.lanking.uxb.service.user.api.QuestionSchoolService;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.zuoye.api.ZyCorrectUserService;

/**
 * @author xinyu.zhou
 * @since 2.6.0
 */
@Service
@Transactional(readOnly = true)
public class QuestionRecordOrderServiceImpl implements QuestionRecordOrderService {
	@Autowired
	@Qualifier("QuestionRecordOrderRepo")
	private Repo<QuestionRecordOrder, Long> repo;

	@Autowired
	private MessageSender messageSender;
	@Autowired
	private ZyCorrectUserService correctUserService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private SchoolQuestionService schoolQuestionService;
	@Autowired
	private QuestionSchoolService questionSchoolService;
	@Autowired
	private GoodsOrderService goodsOrderService;

	@Override
	public Page<QuestionRecordOrder> page(Pageable pageable, long userId) {
		Params params = Params.param("userId", userId);
		return repo.find("$findByUser", params).fetch(pageable);
	}

	@Override
	@Transactional
	public void delete(long id) {
		QuestionRecordOrder order = repo.get(id);
		if (order == null) {
			throw new IllegalArgException();
		}

		if (!(order.getOrderStatus() == QuestionRecordOrderStatus.COMPLETE
				|| order.getOrderStatus() == QuestionRecordOrderStatus.CLOSE || order.getOrderStatus() == QuestionRecordOrderStatus.TERMINATE)) {
			throw new IllegalArgException();
		}

		order.setDelStatus(Status.DISABLED);
		repo.save(order);
	}

	@Override
	@Transactional
	public QuestionRecordOrder create(QuestionRecordOrderCreateForm form, long userId) {
		QuestionRecordOrder order = new QuestionRecordOrder();
		order.setOrderStatus(QuestionRecordOrderStatus.INIT);
		order.setType(form.getType());
		order.setAttachFiles(form.getFiles());
		order.setDescription(form.getDescription());
		order.setMobile(form.getMobile());
		order.setUserId(userId);
		order.setOrderAt(new Date());
		order.setUpdateAt(new Date());
		order.setUpdateId(userId);
		order.setCategoryCode(form.getCategoryCode());
		order.setCode(goodsOrderService.generateCode());

		Teacher teacher = (Teacher) teacherService.getUser(userId);
		QuestionSchool questionSchool = questionSchoolService.getBySchool(teacher.getSchoolId());
		order.setRecordCount(questionSchool == null ? 0 : questionSchool.getRecordQuestionCount());
		order.setQuestionCount(schoolQuestionService.countBySchool(teacher.getSchoolId()));

		return repo.save(order);
	}

	@Override
	@Transactional
	public void close(long id) {
		QuestionRecordOrder order = repo.get(id);
		if (!(order.getOrderStatus() == QuestionRecordOrderStatus.INIT || order.getOrderStatus() == QuestionRecordOrderStatus.NOT_CONTACT)) {
			throw new IllegalArgException();
		}

		order.setOrderStatus(QuestionRecordOrderStatus.CLOSE);
		order.setUpdateAt(new Date());
		order.setUpdateId(order.getUserId());

		repo.save(order);
	}

	@Async
	@Override
	public void asyncNoticeUsers() {
		messageSender.send(new SmsPacket(correctUserService.getAllMobile(), 10000024, ValueMap.value()));
	}
}

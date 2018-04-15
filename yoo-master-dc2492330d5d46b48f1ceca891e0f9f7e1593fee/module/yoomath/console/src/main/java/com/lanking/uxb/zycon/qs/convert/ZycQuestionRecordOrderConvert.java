package com.lanking.uxb.zycon.qs.convert;

import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.common.baseData.TextbookCategory;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.order.questionRecord.QuestionRecordOrder;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.file.convert.FileConverter;
import com.lanking.uxb.zycon.operation.api.ZycAccountService;
import com.lanking.uxb.zycon.qs.api.ZycTeacherService;
import com.lanking.uxb.zycon.qs.value.VZycQuestionRecordOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author xinyu.zhou
 * @since 2.6.0
 */
@Component
public class ZycQuestionRecordOrderConvert extends Converter<VZycQuestionRecordOrder, QuestionRecordOrder, Long> {
	@Autowired
	private FileConverter fileConverter;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private ZycTeacherService teacherService;
	@Autowired
	private ZycAccountService accountService;
	@Autowired
	private TextbookCategoryService categoryService;

	@Override
	protected Long getId(QuestionRecordOrder questionRecordOrder) {
		return questionRecordOrder.getId();
	}

	@Override
	protected VZycQuestionRecordOrder convert(QuestionRecordOrder questionRecordOrder) {
		VZycQuestionRecordOrder v = new VZycQuestionRecordOrder();
		v.setAttachFiles(fileConverter.mgetList(questionRecordOrder.getAttachFiles()));
		v.setDescription(questionRecordOrder.getDescription());
		v.setId(questionRecordOrder.getId());
		v.setMessage(questionRecordOrder.getMessage());
		v.setMobile(questionRecordOrder.getMobile());
		v.setOrderAt(questionRecordOrder.getOrderAt());
		v.setQuestionCount(questionRecordOrder.getQuestionCount());
		v.setRecordCount(questionRecordOrder.getRecordCount());
		v.setStatus(questionRecordOrder.getOrderStatus());
		v.setType(questionRecordOrder.getType());
		v.setUpdateAt(questionRecordOrder.getUpdateAt());
		v.setUpdateMessageAt(questionRecordOrder.getUpdateMessageAt());
		v.setCloseMessage(questionRecordOrder.getCloseMessage());
		v.setCloseAt(questionRecordOrder.getCloseAt());

		return v;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VZycQuestionRecordOrder, QuestionRecordOrder, Integer, TextbookCategory>() {

			@Override
			public boolean accept(QuestionRecordOrder questionRecordOrder) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(QuestionRecordOrder questionRecordOrder, VZycQuestionRecordOrder vZycQuestionRecordOrder) {
				return questionRecordOrder.getCategoryCode();
			}

			@Override
			public void setValue(QuestionRecordOrder questionRecordOrder, VZycQuestionRecordOrder vZycQuestionRecordOrder, TextbookCategory value) {
				if (value != null) {
					vZycQuestionRecordOrder.setCategoryName(value.getName());
				}
			}

			@Override
			public TextbookCategory getValue(Integer key) {
				return categoryService.get(key);
			}

			@Override
			@SuppressWarnings("unchecked")
			public Map<Integer, TextbookCategory> mgetValue(Collection<Integer> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}
				return categoryService.mget(keys);
			}
		});

		assemblers.add(new ConverterAssembler<VZycQuestionRecordOrder, QuestionRecordOrder, Long, Account>() {

			@Override
			public boolean accept(QuestionRecordOrder questionRecordOrder) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(QuestionRecordOrder questionRecordOrder, VZycQuestionRecordOrder vZycQuestionRecordOrder) {
				return questionRecordOrder.getUserId();
			}

			@Override
			public void setValue(QuestionRecordOrder questionRecordOrder, VZycQuestionRecordOrder vZycQuestionRecordOrder, Account value) {
				if (value != null) {
					vZycQuestionRecordOrder.setAccountName(value.getName());
				}
			}

			@Override
			public Account getValue(Long key) {
				return accountService.getAccountByUserId(key);
			}

			@Override
			@SuppressWarnings("unchecked")
			public Map<Long, Account> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}
				return accountService.mgetByUserId(keys);
			}
		});

		assemblers.add(new ConverterAssembler<VZycQuestionRecordOrder, QuestionRecordOrder, Long, Teacher>() {

			@Override
			public boolean accept(QuestionRecordOrder questionRecordOrder) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(QuestionRecordOrder questionRecordOrder, VZycQuestionRecordOrder vZycQuestionRecordOrder) {
				return questionRecordOrder.getUserId();
			}

			@Override
			public void setValue(QuestionRecordOrder questionRecordOrder, VZycQuestionRecordOrder vZycQuestionRecordOrder, Teacher value) {
				if (value != null) {
					vZycQuestionRecordOrder.setUserName(value.getName());
					vZycQuestionRecordOrder.setSchoolId(value.getSchoolId());
				}
			}

			@Override
			public Teacher getValue(Long key) {
				return teacherService.get(key);
			}

			@Override
			@SuppressWarnings("unchecked")
			public Map<Long, Teacher> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}

				return teacherService.mget(keys);
			}
		});

		assemblers.add(new ConverterAssembler<VZycQuestionRecordOrder, QuestionRecordOrder, Long, School>() {
			@Override
			public boolean accept(QuestionRecordOrder questionRecordOrder) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(QuestionRecordOrder questionRecordOrder, VZycQuestionRecordOrder vZycQuestionRecordOrder) {
				return vZycQuestionRecordOrder.getSchoolId();
			}

			@Override
			public void setValue(QuestionRecordOrder questionRecordOrder, VZycQuestionRecordOrder vZycQuestionRecordOrder, School value) {
				if (value != null) {
					vZycQuestionRecordOrder.setSchoolName(value.getName());
				}
			}

			@Override
			public School getValue(Long key) {
				return schoolService.get(key);
			}

			@Override
			@SuppressWarnings("unchecked")
			public Map<Long, School> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}
				return schoolService.mget(keys);
			}
		});
	}
}

package com.lanking.uxb.zycon.operation.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.order.fallible.FallibleQuestionPrintOrder;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoomath.fallible.StudentFallibleExportRecord;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.code.api.DistrictService;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.zycon.operation.api.ZycStudentFallibleExportRecordService;
import com.lanking.uxb.zycon.operation.value.VZycFallibleQuestionPrintOrder;

@Component
public class ZycFallibleQuestionPrintOrderConvert extends
		Converter<VZycFallibleQuestionPrintOrder, FallibleQuestionPrintOrder, Long> {

	@Autowired
	private ZycStudentFallibleExportRecordService exportRecordService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private UserService userService;

	@Autowired
	private DistrictService districtService;

	@Override
	protected Long getId(FallibleQuestionPrintOrder s) {
		return s.getId();
	}

	@Override
	protected VZycFallibleQuestionPrintOrder convert(FallibleQuestionPrintOrder s) {
		VZycFallibleQuestionPrintOrder v = new VZycFallibleQuestionPrintOrder();
		v.setContactAddress(s.getContactAddress());
		v.setContactName(s.getContactName());
		v.setContactPhone(s.getContactPhone());
		v.setExpress(s.getExpress());
		v.setExpressCode(s.getExpressCode());
		v.setPayTime(s.getPayTime());
		v.setStatus(s.getStatus());
		v.setId(s.getId());
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers
				.add(new ConverterAssembler<VZycFallibleQuestionPrintOrder, FallibleQuestionPrintOrder, Long, StudentFallibleExportRecord>() {

					@Override
					public boolean accept(FallibleQuestionPrintOrder s) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(FallibleQuestionPrintOrder s, VZycFallibleQuestionPrintOrder d) {
						return s.getId();
					}

					@Override
					public void setValue(FallibleQuestionPrintOrder s, VZycFallibleQuestionPrintOrder d,
							StudentFallibleExportRecord value) {
						if (value != null) {
							d.setPrintName(value.getName());
							d.setExportRecordId(value.getId());
						}
					}

					@Override
					public StudentFallibleExportRecord getValue(Long key) {
						return exportRecordService.getByOrderId(key);
					}

					@Override
					public Map<Long, StudentFallibleExportRecord> mgetValue(Collection<Long> keys) {
						return exportRecordService.mgetByOrderIds(keys);
					}

				});

		assemblers
				.add(new ConverterAssembler<VZycFallibleQuestionPrintOrder, FallibleQuestionPrintOrder, Long, Account>() {

					@Override
					public boolean accept(FallibleQuestionPrintOrder s) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(FallibleQuestionPrintOrder s, VZycFallibleQuestionPrintOrder d) {
						return s.getUserId();
					}

					@Override
					public void setValue(FallibleQuestionPrintOrder s, VZycFallibleQuestionPrintOrder d, Account value) {
						d.setAccountName(value.getName());
					}

					@Override
					public Account getValue(Long key) {
						return accountService.getAccountByUserId(key);
					}

					@Override
					public Map<Long, Account> mgetValue(Collection<Long> keys) {
						Set<Long> userIds = Sets.newHashSet();
						userIds.addAll(keys);
						return accountService.getAccountByUserIds(userIds);
					}

				});

		assemblers
				.add(new ConverterAssembler<VZycFallibleQuestionPrintOrder, FallibleQuestionPrintOrder, Long, User>() {

					@Override
					public boolean accept(FallibleQuestionPrintOrder s) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(FallibleQuestionPrintOrder s, VZycFallibleQuestionPrintOrder d) {
						return s.getUserId();
					}

					@Override
					public void setValue(FallibleQuestionPrintOrder s, VZycFallibleQuestionPrintOrder d, User value) {
						d.setUserName(value.getName());
					}

					@Override
					public User getValue(Long key) {
						return accountService.getUserByUserId(key);
					}

					@Override
					public Map<Long, User> mgetValue(Collection<Long> keys) {
						Set<Long> userIds = Sets.newHashSet();
						userIds.addAll(keys);
						return userService.getUsers(userIds);
					}

				});

		assemblers
				.add(new ConverterAssembler<VZycFallibleQuestionPrintOrder, FallibleQuestionPrintOrder, Long, String>() {

					@Override
					public boolean accept(FallibleQuestionPrintOrder s) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(FallibleQuestionPrintOrder s, VZycFallibleQuestionPrintOrder d) {
						return s.getDistrictCode();
					}

					@Override
					public void setValue(FallibleQuestionPrintOrder s, VZycFallibleQuestionPrintOrder d, String value) {
						if (value != null) {
							d.setDistrictName(value);
						}
					}

					@Override
					public String getValue(Long key) {
						return districtService.getDistrictName(key);
					}

					@Override
					public Map<Long, String> mgetValue(Collection<Long> keys) {
						return districtService.mgetDistrictName(keys);
					}

				});
	}

}

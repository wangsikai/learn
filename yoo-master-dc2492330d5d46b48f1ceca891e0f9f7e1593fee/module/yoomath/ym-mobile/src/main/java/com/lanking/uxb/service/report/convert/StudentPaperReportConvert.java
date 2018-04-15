package com.lanking.uxb.service.report.convert;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.user.UserInfo;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.stat.StudentPaperReport;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.report.value.VStudentPaperReport;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;

@Component
public class StudentPaperReportConvert extends Converter<VStudentPaperReport, StudentPaperReport, Long> {

	@Autowired
	@Qualifier("accountService")
	private AccountService accountService;
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	@Autowired
	private ZyHomeworkClassService homeworkClassService;

	@Override
	protected Long getId(StudentPaperReport s) {
		return s.getId();
	}

	@Override
	protected VStudentPaperReport convert(StudentPaperReport s) {
		VStudentPaperReport v = new VStudentPaperReport();
		v.setClassCompletionRate(s.getClassCompletionRate());
		v.setClassRightRate(s.getClassRightRate());
		v.setComment(s.getComment());
		v.setCompletionRate(s.getCompletionRate());
		v.setEndDate(s.getEndDate().getTime());
		v.setRightRate(s.getRightRate());
		v.setRightRateRank(s.getRightRateRank());
		v.setSectionMap(s.getSectionMap());
		v.setStartDate(s.getStartDate().getTime());
		v.setStudentId(s.getStudentId());

		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 账户名
		assemblers.add(new ConverterAssembler<VStudentPaperReport, StudentPaperReport, Long, String>() {

			@Override
			public boolean accept(StudentPaperReport s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(StudentPaperReport s, VStudentPaperReport d) {
				return s.getStudentId();
			}

			@Override
			public void setValue(StudentPaperReport s, VStudentPaperReport d, String value) {
				d.setAccountName(value);
			}

			@Override
			public String getValue(Long key) {
				Account account = accountService.getAccountByUserId(key);
				if (account != null) {
					return account.getName();
				}
				return null;
			}

			@Override
			public Map<Long, String> mgetValue(Collection<Long> keys) {
				Set<Long> userIds = Sets.newHashSet();
				for (Long key : keys) {
					userIds.add(key);
				}
				Map<Long, Account> accounts = accountService.getAccountByUserIds(userIds);
				Map<Long, String> nameMap = new HashMap<>();
				if (accounts == null) {
					return nameMap;
				}

				for (Map.Entry<Long, Account> value : accounts.entrySet()) {
					nameMap.put(value.getKey(), value.getValue().getName());
				}
				return nameMap;
			}
		});

		// 用户名
		assemblers.add(new ConverterAssembler<VStudentPaperReport, StudentPaperReport, Long, String>() {

			@Override
			public boolean accept(StudentPaperReport s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(StudentPaperReport s, VStudentPaperReport d) {
				return s.getStudentId();
			}

			@Override
			public void setValue(StudentPaperReport s, VStudentPaperReport d, String value) {
				d.setUserName(value);
			}

			@Override
			public String getValue(Long key) {
				UserInfo userInfo = userService.getUser(key);
				if (userInfo != null) {
					return userInfo.getName();
				}
				return null;
			}

			@Override
			public Map<Long, String> mgetValue(Collection<Long> keys) {
				Set<Long> userIds = Sets.newHashSet();
				for (Long key : keys) {
					userIds.add(key);
				}
				Map<Long, UserInfo> userMap = userService.getUserInfos(UserType.STUDENT, userIds);
				Map<Long, String> nameMap = new HashMap<>();
				if (userMap == null) {
					return nameMap;
				}

				for (Map.Entry<Long, UserInfo> value : userMap.entrySet()) {
					nameMap.put(value.getKey(), value.getValue().getName());
				}
				return nameMap;
			}
		});

		// 班级名
		assemblers.add(new ConverterAssembler<VStudentPaperReport, StudentPaperReport, Long, String>() {

			@Override
			public boolean accept(StudentPaperReport s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(StudentPaperReport s, VStudentPaperReport d) {
				return s.getClassId();
			}

			@Override
			public void setValue(StudentPaperReport s, VStudentPaperReport d, String value) {
				d.setClazzName(value);
			}

			@Override
			public String getValue(Long key) {
				HomeworkClazz clazz = homeworkClassService.get(key);
				if (clazz != null) {
					return clazz.getName();
				}

				return null;
			}

			@Override
			public Map<Long, String> mgetValue(Collection<Long> keys) {
				Map<Long, HomeworkClazz> clazzMap = homeworkClassService.mget(keys);
				Map<Long, String> nameMap = new HashMap<>();
				if (clazzMap == null) {
					return nameMap;
				}

				for (Map.Entry<Long, HomeworkClazz> value : clazzMap.entrySet()) {
					nameMap.put(value.getKey(), value.getValue().getName());
				}
				return nameMap;
			}
		});
	}
}

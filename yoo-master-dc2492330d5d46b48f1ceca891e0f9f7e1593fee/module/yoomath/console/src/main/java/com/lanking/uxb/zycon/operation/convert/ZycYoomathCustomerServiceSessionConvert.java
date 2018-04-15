package com.lanking.uxb.zycon.operation.convert;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.customerService.YoomathCustomerServiceSession;
import com.lanking.cloud.domain.yoo.user.Sex;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.zycon.homework.api.ZycStudentService;
import com.lanking.uxb.zycon.homework.api.ZycUserService;
import com.lanking.uxb.zycon.operation.api.ZycAccountService;
import com.lanking.uxb.zycon.operation.api.ZycYoomathCustomerServiceLogService;
import com.lanking.uxb.zycon.operation.value.VZycAccount;
import com.lanking.uxb.zycon.operation.value.VZycYoomathCustomerServiceSession;
import com.lanking.uxb.zycon.qs.api.ZycSchoolService;
import com.lanking.uxb.zycon.qs.api.ZycTeacherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author xinyu.zhou
 * @since yoomath V1.7
 */
@Component
public class ZycYoomathCustomerServiceSessionConvert extends
		Converter<VZycYoomathCustomerServiceSession, YoomathCustomerServiceSession, Long> {

	@Autowired
	private ZycYoomathCustomerServiceLogService yoomathCustomerServiceLogService;
	@Autowired
	private ZycUserService userService;
	@Autowired
	private ZycAccountService accountService;
	@Autowired
	private ZycTeacherService teacherService;
	@Autowired
	private ZycStudentService studentService;
	@Autowired
	private ZycSchoolService schoolService;

	@Override
	protected Long getId(YoomathCustomerServiceSession yoomathCustomerServiceSession) {
		return yoomathCustomerServiceSession.getId();
	}

	@Override
	protected VZycYoomathCustomerServiceSession convert(YoomathCustomerServiceSession yoomathCustomerServiceSession) {
		VZycYoomathCustomerServiceSession v = new VZycYoomathCustomerServiceSession();
		v.setId(yoomathCustomerServiceSession.getId());
		v.setUserId(yoomathCustomerServiceSession.getUserId());
		return v;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers
				.add(new ConverterAssembler<VZycYoomathCustomerServiceSession, YoomathCustomerServiceSession, Long, User>() {

					@Override
					public boolean accept(YoomathCustomerServiceSession yoomathCustomerServiceSession) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(YoomathCustomerServiceSession yoomathCustomerServiceSession,
							VZycYoomathCustomerServiceSession vZycYoomathCustomerServiceSession) {
						return yoomathCustomerServiceSession.getUserId();
					}

					@Override
					public void setValue(YoomathCustomerServiceSession yoomathCustomerServiceSession,
							VZycYoomathCustomerServiceSession vZycYoomathCustomerServiceSession, User value) {
						if (value != null) {
							if (value.getName() != null) {
								vZycYoomathCustomerServiceSession.setUserName(value.getName());
							} else if (value.getNickname() != null) {
								vZycYoomathCustomerServiceSession.setUserName(value.getNickname());
							}
							yoomathCustomerServiceSession.setUserType(value.getUserType());
						}
					}

					@Override
					public User getValue(Long key) {
						if (key == null) {
							return null;
						}
						return userService.get(key);
					}

					@Override
					public Map<Long, User> mgetValue(Collection<Long> keys) {
						if (CollectionUtils.isEmpty(keys)) {
							return Maps.newHashMap();
						}
						return userService.mget(keys);
					}
				});

		assemblers
				.add(new ConverterAssembler<VZycYoomathCustomerServiceSession, YoomathCustomerServiceSession, Long, Long>() {

					@Override
					public boolean accept(YoomathCustomerServiceSession yoomathCustomerServiceSession) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(YoomathCustomerServiceSession yoomathCustomerServiceSession,
							VZycYoomathCustomerServiceSession vZycYoomathCustomerServiceSession) {
						return yoomathCustomerServiceSession.getUserId();
					}

					@Override
					public void setValue(YoomathCustomerServiceSession yoomathCustomerServiceSession,
							VZycYoomathCustomerServiceSession vZycYoomathCustomerServiceSession, Long value) {
						vZycYoomathCustomerServiceSession.setUnreadCount(value);
					}

					@Override
					public Long getValue(Long key) {
						if (key == null) {
							return 0L;
						}
						return yoomathCustomerServiceLogService.getUserUnreadCount(key);
					}

					@Override
					public Map<Long, Long> mgetValue(Collection<Long> keys) {
						if (CollectionUtils.isEmpty(keys)) {
							return Maps.newHashMap();
						}
						return yoomathCustomerServiceLogService.mgetUserUnreadCount(keys);
					}
				});

		// 账户信息
		assemblers
				.add(new ConverterAssembler<VZycYoomathCustomerServiceSession, YoomathCustomerServiceSession, Long, Account>() {

					@Override
					public boolean accept(YoomathCustomerServiceSession serviceSession) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(YoomathCustomerServiceSession serviceSession,
							VZycYoomathCustomerServiceSession vZycYoomathCustomerServiceSession) {
						return serviceSession.getUserId();
					}

					@Override
					public void setValue(YoomathCustomerServiceSession serviceSession,
							VZycYoomathCustomerServiceSession vZycYoomathCustomerServiceSession, Account value) {
						if (value != null) {
							VZycAccount v = new VZycAccount();
							v.setEmail(value.getEmail());
							v.setMobile(value.getMobile());
							v.setName(value.getName());

							vZycYoomathCustomerServiceSession.setAccount(v);
						}
					}

					@Override
					public Account getValue(Long key) {
						return accountService.getAccountByUserId(key);
					}

					@Override
					public Map<Long, Account> mgetValue(Collection<Long> keys) {
						if (CollectionUtils.isEmpty(keys)) {
							return Maps.newHashMap();
						}
						return accountService.mgetByUserId(keys);
					}
				});

		// 转换老师
		assemblers
				.add(new ConverterAssembler<VZycYoomathCustomerServiceSession, YoomathCustomerServiceSession, Long, Teacher>() {
					@Override
					public boolean accept(YoomathCustomerServiceSession serviceSession) {
						return serviceSession.getUserType() == UserType.TEACHER;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(YoomathCustomerServiceSession serviceSession,
							VZycYoomathCustomerServiceSession vZycYoomathCustomerServiceSession) {
						return serviceSession.getUserId();
					}

					@Override
					public void setValue(YoomathCustomerServiceSession serviceSession,
							VZycYoomathCustomerServiceSession vZycYoomathCustomerServiceSession, Teacher value) {
						if (value != null) {
							vZycYoomathCustomerServiceSession.setSchoolId(value.getSchoolId());
							vZycYoomathCustomerServiceSession.setPhaseCode(value.getPhaseCode());
							vZycYoomathCustomerServiceSession.setSex(Sex.getCnName(value.getSex()));
						}
					}

					@Override
					public Teacher getValue(Long key) {
						if (null == key) {
							return null;
						}
						return teacherService.get(key);
					}

					@Override
					public Map<Long, Teacher> mgetValue(Collection<Long> keys) {
						if (CollectionUtils.isEmpty(keys)) {
							return Maps.newHashMap();
						}
						return teacherService.mget(keys);
					}
				});

		// 转换学生
		assemblers
				.add(new ConverterAssembler<VZycYoomathCustomerServiceSession, YoomathCustomerServiceSession, Long, Student>() {
					@Override
					public boolean accept(YoomathCustomerServiceSession serviceSession) {
						return serviceSession.getUserType() == UserType.STUDENT;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(YoomathCustomerServiceSession serviceSession,
							VZycYoomathCustomerServiceSession vZycYoomathCustomerServiceSession) {
						return serviceSession.getUserId();
					}

					@Override
					public void setValue(YoomathCustomerServiceSession serviceSession,
							VZycYoomathCustomerServiceSession vZycYoomathCustomerServiceSession, Student value) {
						if (value != null) {
							vZycYoomathCustomerServiceSession.setSchoolId(value.getSchoolId());
							vZycYoomathCustomerServiceSession.setSex(Sex.getCnName(value.getSex()));
						}
					}

					@Override
					public Student getValue(Long key) {
						if (null == key) {
							return null;
						}
						return studentService.get(key);
					}

					@Override
					public Map<Long, Student> mgetValue(Collection<Long> keys) {
						if (CollectionUtils.isEmpty(keys)) {
							return Maps.newHashMap();
						}
						return studentService.mget(keys);
					}
				});

		// 转换学校
		assemblers
				.add(new ConverterAssembler<VZycYoomathCustomerServiceSession, YoomathCustomerServiceSession, Long, String>() {
					@Override
					public boolean accept(YoomathCustomerServiceSession serviceSession) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(YoomathCustomerServiceSession serviceSession,
							VZycYoomathCustomerServiceSession vZycYoomathCustomerServiceSession) {
						return vZycYoomathCustomerServiceSession.getSchoolId();
					}

					@Override
					public void setValue(YoomathCustomerServiceSession serviceSession,
							VZycYoomathCustomerServiceSession vZycYoomathCustomerServiceSession, String value) {
						if (value != null) {
							vZycYoomathCustomerServiceSession.setSchoolName(value);
						}
					}

					@Override
					public String getValue(Long key) {
						if (null == key) {
							return null;
						}
						School school = schoolService.get(key);
						return school == null ? null : school.getName();
					}

					@Override
					public Map<Long, String> mgetValue(Collection<Long> keys) {
						if (CollectionUtils.isEmpty(keys)) {
							return Maps.newHashMap();
						}
						Map<Long, String> map = Maps.newHashMap();
						List<School> schools = schoolService.mgetList(keys);
						for (School s : schools) {
							map.put(s.getId(), s.getName());
						}
						return map;
					}
				});
	}
}

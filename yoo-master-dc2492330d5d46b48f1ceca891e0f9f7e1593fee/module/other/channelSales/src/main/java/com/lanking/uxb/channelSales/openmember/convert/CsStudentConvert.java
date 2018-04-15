package com.lanking.uxb.channelSales.openmember.convert;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.domain.yoo.member.UserMember;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.channelSales.channel.api.CsUserChannelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.uxb.channelSales.channel.api.CsUserService;
import com.lanking.uxb.channelSales.openmember.api.CsAccountService;
import com.lanking.uxb.channelSales.openmember.api.CsUserMemberService;
import com.lanking.uxb.channelSales.openmember.value.VCsUser;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.service.code.convert.PhaseConvert;
import com.lanking.uxb.service.code.value.VPhase;

/**
 * @author xinyu.zhou
 * @since 2.5.0
 */
@Component
public class CsStudentConvert extends Converter<VCsUser, Student, Long> {
	@Autowired
	private CsUserService userService;
	@Autowired
	private CsAccountService accountService;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private PhaseConvert phaseConvert;
	@Autowired
	private CsUserMemberService userMemberService;
	@Autowired
	private CsUserChannelService userChannelService;

	private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

	@Override
	protected Long getId(Student student) {
		return student.getId();
	}

	@Override
	protected VCsUser convert(Student student) {
		VCsUser v = new VCsUser();
		v.setId(student.getId());
		v.setName(student.getName());
		v.setSex(student.getSex());
		v.setUserType(UserType.STUDENT);
		return v;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 转换User
		assemblers.add(new ConverterAssembler<VCsUser, Student, Long, User>() {

			@Override
			public boolean accept(Student student) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Student student, VCsUser vCsUser) {
				return student.getId();
			}

			@Override
			public void setValue(Student student, VCsUser vCsUser, User value) {
				if (value != null) {
					vCsUser.setAccountId(value.getAccountId());
					vCsUser.setChannelCode(value.getUserChannelCode());
				}
			}

			@Override
			public User getValue(Long key) {
				return userService.get(key);
			}

			@Override
			public Map<Long, User> mgetValue(Collection<Long> keys) {
				return userService.mget(keys);
			}
		});

		// 转换Account
		assemblers.add(new ConverterAssembler<VCsUser, Student, Long, Account>() {

			@Override
			public boolean accept(Student student) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Student student, VCsUser vCsUser) {
				return vCsUser.getAccountId();
			}

			@Override
			public void setValue(Student student, VCsUser vCsUser, Account value) {
				if (value != null) {
					vCsUser.setAccountName(value.getName());
				}
			}

			@Override
			public Account getValue(Long key) {
				return accountService.get(key);
			}

			@Override
			public Map<Long, Account> mgetValue(Collection<Long> keys) {
				return accountService.mget(keys);
			}
		});

		// 转换学校相关
		assemblers.add(new ConverterAssembler<VCsUser, Student, Long, School>() {
			@Override
			public boolean accept(Student student) {
				return student.getSchoolId() != null && student.getSchoolId() > 0;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Student student, VCsUser vCsUser) {
				return student.getSchoolId();
			}

			@Override
			public void setValue(Student student, VCsUser vCsUser, School value) {
				if (value != null) {
					vCsUser.setSchoolName(value.getName());
					vCsUser.setSchoolId(value.getId());
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

		// 转换阶段
		assemblers.add(new ConverterAssembler<VCsUser, Student, Integer, VPhase>() {

			@Override
			public boolean accept(Student student) {
				return student.getPhaseCode() != null && student.getPhaseCode() > 0;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(Student student, VCsUser vCsUser) {
				return student.getPhaseCode();
			}

			@Override
			public void setValue(Student student, VCsUser vCsUser, VPhase value) {
				vCsUser.setPhase(value);
			}

			@Override
			public VPhase getValue(Integer key) {
				return phaseConvert.to(phaseService.get(key));
			}

			@Override
			@SuppressWarnings("unchecked")
			public Map<Integer, VPhase> mgetValue(Collection<Integer> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}
				return phaseConvert.to(phaseService.mget(keys));
			}
		});

		// 判断用户是否是会员
		assemblers.add(new ConverterAssembler<VCsUser, Student, Long, UserMember>() {

			@Override
			public boolean accept(Student student) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Student student, VCsUser vCsUser) {
				return student.getId();
			}

			@Override
			public void setValue(Student student, VCsUser vCsUser, UserMember value) {
				if (value != null) {
					int now = Integer.valueOf(format.format(new Date()));
					int end = Integer.valueOf(format.format(value.getEndAt()));
					if (end >= now) {
						vCsUser.setMemberType(value.getMemberType());
						vCsUser.setMemberBeginDate(value.getStartAt());
						vCsUser.setMemberEndDate(value.getEndAt());
					}
				}
			}

			@Override
			public UserMember getValue(Long key) {
				return userMemberService.findByUser(key);
			}

			@Override
			@SuppressWarnings("unchecked")
			public Map<Long, UserMember> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}
				return userMemberService.findByUsers(keys);
			}
		});

		// 转换渠道名
		assemblers.add(new ConverterAssembler<VCsUser, Student, Integer, UserChannel>() {

			@Override
			public boolean accept(Student student) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(Student student, VCsUser vCsUser) {
				return vCsUser.getChannelCode();
			}

			@Override
			public void setValue(Student student, VCsUser vCsUser, UserChannel value) {
				if (value != null) {
					vCsUser.setChannelName(value.getName());
				}
			}

			@Override
			public UserChannel getValue(Integer key) {
				return userChannelService.get(key);
			}

			@Override
			@SuppressWarnings("unchecked")
			public Map<Integer, UserChannel> mgetValue(Collection<Integer> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}
				return userChannelService.mget(keys);
			}
		});
	}
}

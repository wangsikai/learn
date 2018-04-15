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
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.channelSales.channel.api.CsUserChannelService;
import com.lanking.uxb.channelSales.openmember.api.CsUserMemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.uxb.channelSales.channel.api.CsUserService;
import com.lanking.uxb.channelSales.openmember.api.CsAccountService;
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
public class CsTeacherConvert extends Converter<VCsUser, Teacher, Long> {
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
	protected Long getId(Teacher teacher) {
		return teacher.getId();
	}

	@Override
	protected VCsUser convert(Teacher teacher) {
		VCsUser v = new VCsUser();
		v.setId(teacher.getId());
		v.setName(teacher.getName());
		v.setSex(teacher.getSex());
		v.setUserType(UserType.TEACHER);
		return v;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 转换User
		assemblers.add(new ConverterAssembler<VCsUser, Teacher, Long, User>() {

			@Override
			public boolean accept(Teacher teacher) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Teacher teacher, VCsUser vCsUser) {
				return teacher.getId();
			}

			@Override
			public void setValue(Teacher teacher, VCsUser vCsUser, User value) {
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
		assemblers.add(new ConverterAssembler<VCsUser, Teacher, Long, Account>() {

			@Override
			public boolean accept(Teacher teacher) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Teacher teacher, VCsUser vCsUser) {
				return vCsUser.getAccountId();
			}

			@Override
			public void setValue(Teacher teacher, VCsUser vCsUser, Account value) {
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
		assemblers.add(new ConverterAssembler<VCsUser, Teacher, Long, School>() {
			@Override
			public boolean accept(Teacher teacher) {
				return teacher.getSchoolId() != null && teacher.getSchoolId() > 0;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Teacher teacher, VCsUser vCsUser) {
				return teacher.getSchoolId();
			}

			@Override
			public void setValue(Teacher teacher, VCsUser vCsUser, School value) {
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
		assemblers.add(new ConverterAssembler<VCsUser, Teacher, Integer, VPhase>() {

			@Override
			public boolean accept(Teacher teacher) {
				return teacher.getPhaseCode() != null && teacher.getPhaseCode() > 0;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(Teacher teacher, VCsUser vCsUser) {
				return teacher.getPhaseCode();
			}

			@Override
			public void setValue(Teacher teacher, VCsUser vCsUser, VPhase value) {
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
		assemblers.add(new ConverterAssembler<VCsUser, Teacher, Long, UserMember>() {

			@Override
			public boolean accept(Teacher teacher) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Teacher teacher, VCsUser vCsUser) {
				return teacher.getId();
			}

			@Override
			public void setValue(Teacher teacher, VCsUser vCsUser, UserMember value) {
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

		// 转换渠道商名
		assemblers.add(new ConverterAssembler<VCsUser, Teacher, Integer, UserChannel>() {

			@Override
			public boolean accept(Teacher teacher) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(Teacher teacher, VCsUser vCsUser) {
				return vCsUser.getChannelCode();
			}

			@Override
			public void setValue(Teacher teacher, VCsUser vCsUser, UserChannel value) {
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

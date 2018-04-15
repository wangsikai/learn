package com.lanking.uxb.service.user.convert;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.lanking.cloud.domain.base.counter.Counter;
import com.lanking.cloud.domain.common.baseData.Duty;
import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.common.baseData.Title;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.type.FileStyle;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.member.UserMember;
import com.lanking.cloud.domain.yoo.user.Sex;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserInfo;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.code.api.DutyService;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.api.TitleService;
import com.lanking.uxb.service.code.convert.PhaseConvert;
import com.lanking.uxb.service.code.convert.SchoolConvert;
import com.lanking.uxb.service.code.convert.SubjectConvert;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.util.TeachUtil;
import com.lanking.uxb.service.code.value.VPhase;
import com.lanking.uxb.service.code.value.VSchool;
import com.lanking.uxb.service.code.value.VSubject;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.code.value.VTextbookCategory;
import com.lanking.uxb.service.counter.api.impl.UserCounterProvider;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.UserMemberService;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.user.value.VParent;
import com.lanking.uxb.service.user.value.VStudent;
import com.lanking.uxb.service.user.value.VTeacher;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.user.value.VUserState;

@Component
public class UserConvert extends Converter<VUser, User, Long> {

	@Autowired
	@Qualifier("userService")
	private UserService userService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private SubjectConvert subjectConvert;
	@Autowired
	private UserStateConvert userStateConvert;
	@Autowired
	private UserCounterProvider userCounterProvider;
	@Autowired
	private SchoolConvert schoolConvert;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private PhaseConvert phaseConvert;
	@Autowired
	private TitleService titleService;
	@Autowired
	private DutyService dutyService;
	@Autowired
	@Qualifier("accountService")
	private AccountService accountService;
	@Autowired
	private TextbookCategoryService tbCateService;
	@Autowired
	private TextbookCategoryConvert tbCateConvert;
	@Autowired
	private TextbookService tbService;
	@Autowired
	private TextbookConvert tbConvert;
	@Autowired
	private UserMemberService userMemberService;

	@Override
	protected Long getId(User s) {
		return s.getId();
	}

	@Override
	protected VUser convert(User s) {
		VUser v = new VUser();
		v.setId(s.getId());
		v.setType(s.getUserType());
		if (s.getUserChannelCode() != null) {
			v.setChannelUser(s.getUserChannelCode() != UserChannel.YOOMATH);
		}
		return v;
	}

	@Override
	public VUser get(Long id) {
		User user = new User();
		user.setId(id);
		return this.to(user);
	}

	public VUser get(Long id, UserConvertOption option) {
		User user = new User();
		user.setInitMemberType(option.isInitMemberType());
		user.setInitPhase(option.isInitPhase());
		user.setInitTeaSubject(option.isInitTeaSubject());
		user.setInitTeaTitle(option.isInitTeaTitle());
		user.setInitTeaDuty(option.isInitTeaDuty());
		user.setInitTextbook(option.isInitTextbook());
		user.setInitTextbookCategory(option.isInitTextbookCategory());
		user.setInitUserState(option.isInitUserState());
		user.setId(id);

		return this.to(user);
	}

	@Override
	public Map<Long, VUser> mget(Collection<Long> ids) {
		Map<Long, User> sMap = Maps.newHashMap();
		for (Long id : ids) {
			User user = new User();
			user.setId(id);
			sMap.put(id, user);
		}
		return this.to(sMap);
	}

	public Map<Long, VUser> mget(Collection<Long> ids, UserConvertOption option) {
		Map<Long, User> sMap = Maps.newHashMap();
		for (Long id : ids) {
			User user = new User();
			user.setId(id);
			user.setInitMemberType(option.isInitMemberType());
			user.setInitPhase(option.isInitPhase());
			user.setInitTeaSubject(option.isInitTeaSubject());
			user.setInitTeaTitle(option.isInitTeaTitle());
			user.setInitTeaDuty(option.isInitTeaDuty());
			user.setInitTextbook(option.isInitTextbook());
			user.setInitTextbookCategory(option.isInitTextbookCategory());
			user.setInitUserState(option.isInitUserState());
			sMap.put(id, user);
		}
		return this.to(sMap);
	}

	@Override
	public List<VUser> mgetList(Collection<Long> ids) {
		List<User> ss = Lists.newArrayList();
		for (Long id : ids) {
			User user = new User();
			user.setId(id);
			ss.add(user);
		}
		return super.to(ss);
	}

	public List<VUser> mgetList(Collection<Long> ids, UserConvertOption option) {
		List<User> ss = Lists.newArrayList();
		for (Long id : ids) {
			User user = new User();
			user.setId(id);
			user.setInitMemberType(option.isInitMemberType());
			user.setInitPhase(option.isInitPhase());
			user.setInitTeaSubject(option.isInitTeaSubject());
			user.setInitTeaTitle(option.isInitTeaTitle());
			user.setInitTeaDuty(option.isInitTeaDuty());
			user.setInitTextbook(option.isInitTextbook());
			user.setInitTextbookCategory(option.isInitTextbookCategory());
			user.setInitUserState(option.isInitUserState());
			ss.add(user);
		}
		return super.to(ss);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 用户的总体信息
		assemblers.add(new ConverterAssembler<VUser, User, Long, UserInfo>() {

			@Override
			public boolean accept(User s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(User s, VUser d) {
				return s.getId();
			}

			@Override
			public void setValue(User s, VUser d, UserInfo value) {
				// 基础公共信息
				d.setMe(Security.getUserId() == s.getId().longValue());
				d.setName(validBlank(value.getName()));
				d.setNickname(validBlank(value.getNickname()));
				d.setIntroduce(validBlank(value.getIntroduce()));
				d.setSex(value.getSex() == null ? Sex.MALE : value.getSex());
				d.setBirthday(value.getBirthday() == null ? new Date(0L) : value.getBirthday());
				d.setAvatarId(value.getAvatarId() == null ? 0 : value.getAvatarId());
				if (value.getAvatarId() != null && value.getAvatarId() != 0) {
					d.setAvatarUrl(FileUtil.getUrl(value.getAvatarId()));
					d.setMinAvatarUrl(FileUtil.getUrl(FileStyle.AVATAR_MID, value.getAvatarId()));
				}
				d.setType(value.getUserType());
				d.setQq(value.getQq());
				// 个性化信息
				if (value.getUserType() == UserType.TEACHER) {// 老师
					Teacher teacher = (Teacher) value;

					VTeacher vt = new VTeacher();
					vt.setSchoolName(validBlank(teacher.getSchoolName()));
					if (teacher.getWorkAt() != null) {
						vt.setTeachYear(TeachUtil.getTeachYear(teacher.getWorkAt()));
						vt.setWorkAt(teacher.getWorkAt());
					}
					d.setT(vt);

					// 将学校ID放入vo里面,后面的assember使用
					if (teacher.getSchoolId() != null) {
						VSchool vs = new VSchool();
						vs.setId(teacher.getSchoolId());
						d.setSchool(vs);
					}
				} else if (value.getUserType() == UserType.STUDENT) {
					Student student = (Student) value;
					VStudent vstu = new VStudent();
					vstu.setYear(student.getYear());
					d.setS(vstu);

					// 将学校ID放入vo里面,后面的assember使用
					if (student.getSchoolId() != null) {
						VSchool vs = new VSchool();
						vs.setId(student.getSchoolId());
						d.setSchool(vs);
					}
				} else if (value.getUserType() == UserType.PARENT) {
					VParent vp = new VParent();
					d.setP(vp);
				}
				d.setChannelUser(value.getUserChannelCode() != UserChannel.YOOMATH);
				s.setUserInfo(value);
			}

			@Override
			public UserInfo getValue(Long key) {
				User user = accountService.getUserByUserId(key);
				UserInfo userInfo = userService.getUser(key);
				userInfo.setUserType(user.getUserType());
				if (StringUtils.isBlank(userInfo.getName())) {
					userInfo.setName(user.getName());
				}
				userInfo.setUserChannelCode(user.getUserChannelCode() == null ? UserChannel.YOOMATH : user
						.getUserChannelCode());
				return userInfo;
			}

			@Override
			public Map<Long, UserInfo> mgetValue(Collection<Long> keys) {
				Map<Long, UserInfo> us = Maps.newHashMap();
				Set<Long> ids = Sets.newHashSet();
				ids.addAll(keys);
				Map<Long, User> users = userService.getUsers(ids);
				Set<Long> tIds = Sets.newHashSet();
				Set<Long> sIds = Sets.newHashSet();
				Set<Long> pIds = Sets.newHashSet();
				for (Long userId : keys) {
					User user = users.get(userId);
					UserType type = user.getUserType();
					if (type == UserType.TEACHER) {
						tIds.add(userId);
					} else if (type == UserType.STUDENT) {
						sIds.add(userId);
					} else if (type == UserType.PARENT) {
						pIds.add(userId);
					}
				}
				Map<Long, UserInfo> ts = userService.getUserInfos(UserType.TEACHER, tIds);
				Map<Long, UserInfo> ss = userService.getUserInfos(UserType.STUDENT, sIds);
				Map<Long, UserInfo> ps = userService.getUserInfos(UserType.PARENT, pIds);
				us.putAll(ts);
				us.putAll(ss);
				us.putAll(ps);
				for (long id : users.keySet()) {
					User u = users.get(id);
					UserInfo ui = us.get(id);
					ui.setUserChannelCode(u.getUserChannelCode() == null ? UserChannel.YOOMATH : u.getUserChannelCode());
					us.put(id, ui);
				}
				return us;
			}

		});
		// 教师的学科信息
		assemblers.add(new ConverterAssembler<VUser, User, Integer, VSubject>() {
			@Override
			public boolean accept(User s) {
				return s.isInitTeaSubject();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(User s, VUser d) {
				if (s.getUserInfo().getUserType() == UserType.TEACHER) {
					return ((Teacher) s.getUserInfo()).getSubjectCode();
				}
				return null;
			}

			@Override
			public void setValue(User s, VUser d, VSubject value) {
				if (value != null && s.getUserInfo().getUserType() == UserType.TEACHER) {
					VTeacher vt = d.getT();
					vt.setSubject(value);
					d.setT(vt);
				}
			}

			@Override
			public VSubject getValue(Integer key) {
				if (key != null) {
					return subjectConvert.to(subjectService.get(key));
				}
				return null;
			}

			@Override
			public Map<Integer, VSubject> mgetValue(Collection<Integer> keys) {
				Set<Integer> subjectCodes = Sets.newHashSet();
				subjectCodes.addAll(keys);
				subjectCodes.remove(null);
				return subjectConvert.to(subjectService.mget(subjectCodes));
			}

		});
		// 用户的统计信息
		assemblers.add(new ConverterAssembler<VUser, User, Long, VUserState>() {
			@Override
			public boolean accept(User s) {
				return s.isInitUserState();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(User s, VUser d) {
				return s.getId();
			}

			@Override
			public void setValue(User s, VUser d, VUserState value) {
				d.setUserState(value);
			}

			@Override
			public VUserState getValue(Long key) {
				return userStateConvert.convert(userCounterProvider.getCounter(key));
			}

			@Override
			public Map<Long, VUserState> mgetValue(Collection<Long> keys) {
				Set<Long> bizIds = Sets.newHashSet();
				bizIds.addAll(keys);
				Map<Long, Counter> counters = userCounterProvider.getCounters(bizIds);
				Map<Long, Counter> cs = Maps.newHashMap();
				for (Long bizId : bizIds) {
					Counter c = counters.get(bizId);
					if (c == null) {
						c = new Counter();
						c.setBiz(Biz.USER.getValue());
						c.setBizId(bizId);
					}
					cs.put(bizId, c);
				}
				return userStateConvert.to(cs);
			}

		});
		// 用户的学校信息
		assemblers.add(new ConverterAssembler<VUser, User, Long, VSchool>() {
			@Override
			public boolean accept(User s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(User s, VUser d) {
				if (d.getSchool() != null) {
					return d.getSchool().getId();
				}
				return null;
			}

			@Override
			public void setValue(User s, VUser d, VSchool value) {
				d.setSchool(value);
			}

			@Override
			public VSchool getValue(Long key) {
				if (key != null) {
					School school = schoolService.get(key);
					return schoolConvert.to(school);
				}
				return null;
			}

			@Override
			public Map<Long, VSchool> mgetValue(Collection<Long> keys) {
				Set<Long> ids = Sets.newHashSet();
				ids.addAll(keys);
				ids.remove(null);
				Map<Long, School> ps = schoolService.mget(ids);
				Map<Long, VSchool> vMaps = schoolConvert.to(ps);
				return vMaps;
			}
		});
		assemblers.add(new ConverterAssembler<VUser, User, Long, String>() {

			@Override
			public boolean accept(User s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(User s, VUser d) {
				return s.getId();
			}

			@Override
			public void setValue(User s, VUser d, String value) {
				if (d.getType() == UserType.TEACHER) {
					if (d.getSchool() != null) {
						d.getT().setSchoolName(d.getSchool().getName());
					} else if (d.getSchool() == null && StringUtils.isNotEmpty(d.getT().getSchoolName())) {
						VSchool school = new VSchool();
						school.setId(-1L);
						school.setName(d.getT().getSchoolName());
						d.setSchool(school);
					}
				}
			}

			@Override
			public String getValue(Long key) {
				return StringUtils.EMPTY;
			}

			@Override
			public Map<Long, String> mgetValue(Collection<Long> keys) {
				Map<Long, String> map = new HashMap<Long, String>(keys.size());
				for (Long key : keys) {
					map.put(key, StringUtils.EMPTY);
				}
				return map;
			}

		});
		// 用户阶段信息
		assemblers.add(new ConverterAssembler<VUser, User, Integer, VPhase>() {
			@Override
			public boolean accept(User s) {
				return s.isInitPhase();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(User s, VUser d) {
				if (s.getUserInfo().getUserType() == UserType.TEACHER) {
					return ((Teacher) s.getUserInfo()).getPhaseCode();
				} else if (s.getUserInfo().getUserType() == UserType.STUDENT) {
					return ((Student) s.getUserInfo()).getPhaseCode();
				}
				return null;
			}

			@Override
			public void setValue(User s, VUser d, VPhase value) {
				if (value != null) {
					if (s.getUserInfo().getUserType() == UserType.TEACHER) {
						VTeacher vt = d.getT();
						vt.setPhase(value);
						d.setT(vt);
					} else if (s.getUserInfo().getUserType() == UserType.STUDENT) {
						VStudent vs = d.getS();
						vs.setPhase(value);
						d.setS(vs);
					}
				}
			}

			@Override
			public VPhase getValue(Integer key) {
				if (key != null) {
					return phaseConvert.to(phaseService.get(key));
				}
				return null;
			}

			@Override
			public Map<Integer, VPhase> mgetValue(Collection<Integer> keys) {
				Set<Integer> phaseCodes = Sets.newHashSet();
				phaseCodes.addAll(keys);
				phaseCodes.remove(null);
				return phaseConvert.to(phaseService.mget(phaseCodes));
			}

		});
		// 教师职称信息
		assemblers.add(new ConverterAssembler<VUser, User, Integer, Title>() {
			@Override
			public boolean accept(User s) {
				return s.isInitTeaTitle();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(User s, VUser d) {
				if (s.getUserInfo().getUserType() == UserType.TEACHER) {
					return ((Teacher) s.getUserInfo()).getTitleCode();
				}
				return null;
			}

			@Override
			public void setValue(User s, VUser d, Title value) {
				if (value != null && s.getUserInfo().getUserType() == UserType.TEACHER && value != null) {
					VTeacher vt = d.getT();
					vt.setTitle(value.getName());
					vt.setTitleCode(value.getCode());
					d.setT(vt);
				}
			}

			@Override
			public Title getValue(Integer key) {
				if (key != null) {
					return titleService.getTitle(key);
				}
				return null;
			}

			@Override
			public Map<Integer, Title> mgetValue(Collection<Integer> keys) {
				Set<Integer> titleCodes = Sets.newHashSet();
				titleCodes.addAll(keys);
				titleCodes.remove(null);
				return titleService.mget(titleCodes);
			}
		});
		// 教师职务信息
		assemblers.add(new ConverterAssembler<VUser, User, Integer, Duty>() {
			@Override
			public boolean accept(User s) {
				return s.isInitTeaDuty();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(User s, VUser d) {
				if (s.getUserInfo().getUserType() == UserType.TEACHER) {
					return ((Teacher) s.getUserInfo()).getDutyCode();
				}
				return null;
			}

			@Override
			public void setValue(User s, VUser d, Duty value) {
				if (value != null && s.getUserInfo().getUserType() == UserType.TEACHER && value != null) {
					VTeacher vt = d.getT();
					vt.setDuty(value.getName());
					vt.setDutyCode(value.getCode());
					d.setT(vt);
				}
			}

			@Override
			public Duty getValue(Integer key) {
				if (key != null) {
					return dutyService.get(key);
				}
				return null;
			}

			@Override
			public Map<Integer, Duty> mgetValue(Collection<Integer> keys) {
				Set<Integer> dutyCodes = Sets.newHashSet();
				dutyCodes.addAll(keys);
				dutyCodes.remove(null);
				return dutyService.mget(dutyCodes);
			}
		});
		// 学生版本信息
		assemblers.add(new ConverterAssembler<VUser, User, Integer, VTextbookCategory>() {
			@Override
			public boolean accept(User s) {
				return s.isInitTextbookCategory();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(User s, VUser d) {
				if (s.getUserInfo().getUserType() == UserType.STUDENT) {
					return ((Student) s.getUserInfo()).getTextbookCategoryCode();
				} else if (s.getUserInfo().getUserType() == UserType.TEACHER) {
					return ((Teacher) s.getUserInfo()).getTextbookCategoryCode();
				}
				return null;
			}

			@Override
			public void setValue(User s, VUser d, VTextbookCategory value) {
				if (value != null) {
					if (s.getUserInfo().getUserType() == UserType.STUDENT) {
						VStudent vs = d.getS();
						vs.setTextbookCategory(value);
						d.setS(vs);
					} else if (s.getUserInfo().getUserType() == UserType.TEACHER) {
						VTeacher vt = d.getT();
						vt.setTextbookCategory(value);
						d.setT(vt);
					}
				}
			}

			@Override
			public VTextbookCategory getValue(Integer key) {
				if (key != null) {
					return tbCateConvert.to(tbCateService.get(key));
				}
				return null;
			}

			@Override
			public Map<Integer, VTextbookCategory> mgetValue(Collection<Integer> keys) {
				Set<Integer> tbCateCodes = Sets.newHashSet();
				tbCateCodes.addAll(keys);
				tbCateCodes.remove(null);
				return tbCateConvert.to(tbCateService.mget(tbCateCodes));
			}
		});
		// 学生教材信息
		assemblers.add(new ConverterAssembler<VUser, User, Integer, VTextbook>() {
			@Override
			public boolean accept(User s) {
				return s.isInitTextbook();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(User s, VUser d) {
				if (s.getUserInfo().getUserType() == UserType.STUDENT) {
					return ((Student) s.getUserInfo()).getTextbookCode();
				}
				return null;
			}

			@Override
			public void setValue(User s, VUser d, VTextbook value) {
				if (value != null) {
					if (s.getUserInfo().getUserType() == UserType.STUDENT) {
						VStudent vs = d.getS();
						vs.setTextbook(value);
						d.setS(vs);
					}
				}
			}

			@Override
			public VTextbook getValue(Integer key) {
				if (key != null) {
					return tbConvert.to(tbService.get(key));
				}
				return null;
			}

			@Override
			public Map<Integer, VTextbook> mgetValue(Collection<Integer> keys) {
				Set<Integer> tbCodes = Sets.newHashSet();
				tbCodes.addAll(keys);
				tbCodes.remove(null);
				return tbConvert.to(tbService.mget(tbCodes));
			}
		});

		// 转换会员状态
		assemblers.add(new ConverterAssembler<VUser, User, Long, UserMember>() {

			@Override
			public boolean accept(User user) {
				return user.isInitMemberType();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(User user, VUser vuser) {
				return user.getId();
			}

			@Override
			public void setValue(User user, VUser vuser, UserMember value) {
				if (value == null) {
					vuser.setMemberType(MemberType.NONE);
				} else {
					if (value.getEndTime().getTime() < System.currentTimeMillis()) {
						vuser.setMemberType(MemberType.NONE);
					} else {
						vuser.setMemberType(value.getMemberType());
					}
				}
			}

			@Override
			public UserMember getValue(Long key) {
				return userMemberService.findSafeByUserId(key);
			}

			@Override
			@SuppressWarnings("unchecked")
			public Map<Long, UserMember> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}

				return userMemberService.mgetByUserIds(keys);
			}
		});

	}
}

package com.lanking.uxb.service.user.api.impl;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.persistence.MappedSuperclass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.common.baseData.Phase;
import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.common.baseData.Subject;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserInfo;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.user.form.RegisterForm;

//@Service("tUserService")
@MappedSuperclass
@Transactional(readOnly = true)
public class TeacherUserService extends AbstractUserService {

	@Autowired
	private PhaseService phaseService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private SchoolService schoolService;

	@Override
	public UserType getType() {
		return UserType.TEACHER;
	}

	@Override
	public boolean accept(UserType userType) {
		return userType == UserType.TEACHER;
	}

	@Transactional
	@Override
	public User createUser(Account account, RegisterForm form) {
		User u = super.createUser(account, form);
		Teacher teacher = new Teacher();
		teacher.setName(account.getName());
		teacher.setCreateAt(new Date());
		teacher.setUpdateAt(teacher.getCreateAt());
		teacherRepo.save(teacher);
		u.setId(teacher.getId());
		return userRepo.save(u);
	}

	@Transactional
	@Override
	public User createUser2(Account account, RegisterForm form) {
		User u = super.createUser2(account, form);
		Teacher teacher = new Teacher();
		teacher.setName(u.getName());
		teacher.setNickname(u.getNickname());
		teacher.setSex(form.getSex());
		teacher.setQq(form.getQq());
		teacher.setCreateAt(new Date());
		teacher.setUpdateAt(teacher.getCreateAt());
		if (form.getPhaseCode() != null) {
			Phase phase = phaseService.get(form.getPhaseCode());
			if (phase == null) {
				throw new NullPointerException();
			}
		}
		if (form.getSubjectCode() != null) {
			Subject subject = subjectService.get(form.getSubjectCode());
			if (subject == null) {
				throw new NullPointerException();
			}
			if (subject.getPhaseCode() != form.getPhaseCode()) {
				throw new ServerException();
			}
		}
		if (form.getSchoolId() > 0) {
			School school = schoolService.get(form.getSchoolId());
			if (school != null) {
				teacher.setSchoolId(form.getSchoolId());
				teacher.setSchoolName(school.getName());
			}
		}
		teacher.setPhaseCode(form.getPhaseCode());
		teacher.setSubjectCode(form.getSubjectCode());
		teacher.setAvatarId(form.getAvatarId());
		teacherRepo.save(teacher);
		u.setId(teacher.getId());
		return userRepo.save(u);
	}

	@Override
	public UserInfo getUser(long userId) {
		Teacher teacher = teacherRepo.get(userId);
		if (teacher != null) {
			teacher.setUserType(UserType.TEACHER);
		}
		return teacher;
	}

	@Transactional
	@Override
	public UserInfo updateAvatar(long userId, long avatarId) {
		Teacher teacher = teacherRepo.get(userId);
		teacher.setAvatarId(avatarId);
		teacherRepo.save(teacher);
		return teacher;
	}

	@Transactional(readOnly = true)
	@Override
	public Map<Long, UserInfo> getUserInfos(UserType type, Set<Long> ids) {
		Map<Long, UserInfo> userInfos = Maps.newHashMap();
		Map<Long, Teacher> teachers = teacherRepo.mget(ids);
		for (Long key : teachers.keySet()) {
			Teacher t = teachers.get(key);
			t.setUserType(UserType.TEACHER);
			userInfos.put(key, t);
		}
		return userInfos;
	}

	@Transactional
	@Override
	public void updateIntroduce(UserType userType, long userId, String introduce) {
		teacherRepo.execute("updateIntroduce", Params.param("userId", userId).put("introduce", introduce));
	}

	@Transactional
	@Override
	public void updateSchool(UserType userType, long userId, long schoolId) {
		if (userType == UserType.TEACHER) {
			Teacher teacher = teacherRepo.get(userId);
			if (teacher != null) {
				teacher.setSchoolId(schoolId);
				teacherRepo.save(teacher);
			}
		}
	}
}

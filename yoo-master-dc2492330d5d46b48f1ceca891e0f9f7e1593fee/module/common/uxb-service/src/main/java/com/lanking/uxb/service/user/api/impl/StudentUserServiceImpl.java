package com.lanking.uxb.service.user.api.impl;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.persistence.MappedSuperclass;

import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserInfo;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.user.form.RegisterForm;

//@Service("sUserService")
@MappedSuperclass
@Transactional(readOnly = true)
public class StudentUserServiceImpl extends AbstractUserService {

	@Override
	public UserType getType() {
		return UserType.STUDENT;
	}

	@Override
	public boolean accept(UserType userType) {
		return userType == UserType.STUDENT;
	}

	@Transactional
	@Override
	public User createUser(Account account, RegisterForm form) {
		User u = super.createUser(account, form);
		Student student = new Student();
		student.setName(u.getName());
		student.setNickname(u.getNickname());
		student.setCreateAt(new Date());
		student.setUpdateAt(student.getUpdateAt());
		student.setSchoolId(form.getSchoolId());
		studentRepo.save(student);
		u.setId(student.getId());
		return userRepo.save(u);
	}

	/**
	 * @since 2.1 2015-08-24 学生注册不再需要打码赛克
	 */
	@Transactional
	@Override
	public User createUser2(Account account, RegisterForm form) {
		User u = super.createUser2(account, form);
		// 学生注册成功
		Student student = new Student();
		student.setNickname(u.getNickname());
		student.setName(u.getName());
		student.setSex(form.getSex());
		student.setQq(form.getQq());
		student.setCreateAt(new Date());
		student.setUpdateAt(student.getUpdateAt());
		// student.setClassId(form.getClassId());
		if (form.getSchoolId() > 0) {
			student.setSchoolId(form.getSchoolId());
		}
		if (form.getPhaseCode() != null && form.getPhaseCode() > 0) {
			student.setPhaseCode(form.getPhaseCode());
		}
		student.setAvatarId(form.getAvatarId());
		studentRepo.save(student);
		u.setId(student.getId());
		return userRepo.save(u);
	}

	@Override
	public UserInfo getUser(long userId) {
		Student student = studentRepo.get(userId);
		if (student != null) {
			student.setUserType(UserType.STUDENT);
		}
		return student;
	}

	@Transactional
	@Override
	public UserInfo updateAvatar(long userId, long avatarId) {
		Student student = studentRepo.get(userId);
		student.setAvatarId(avatarId);
		studentRepo.save(student);
		return student;
	}

	@Transactional(readOnly = true)
	@Override
	public Map<Long, UserInfo> getUserInfos(UserType type, Set<Long> ids) {
		Map<Long, UserInfo> userInfos = Maps.newHashMap();
		Map<Long, Student> students = studentRepo.mget(ids);
		for (Long key : students.keySet()) {
			Student s = students.get(key);
			s.setUserType(UserType.STUDENT);
			userInfos.put(key, s);
		}
		return userInfos;
	}

	@Transactional
	@Override
	public void updateIntroduce(UserType userType, long userId, String introduce) {
		studentRepo.execute("updateIntroduce", Params.param("userId", userId).put("introduce", introduce));
	}

	@Transactional
	@Override
	public void updateSchool(UserType userType, long userId, long schoolId) {
		if (userType == UserType.STUDENT) {
			Student student = studentRepo.get(userId);
			if (student != null) {
				student.setSchoolId(schoolId);
				studentRepo.save(student);
			}
		}
	}
}

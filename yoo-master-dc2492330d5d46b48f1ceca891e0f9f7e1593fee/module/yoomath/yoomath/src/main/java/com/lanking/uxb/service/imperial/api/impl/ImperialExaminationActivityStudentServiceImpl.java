package com.lanking.uxb.service.imperial.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityStudent;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityStudentService;

import httl.util.CollectionUtils;

@Service
@Transactional(readOnly = true)
public class ImperialExaminationActivityStudentServiceImpl implements ImperialExaminationActivityStudentService {

	@Autowired
	@Qualifier("ImperialExaminationActivityStudentRepo")
	private Repo<ImperialExaminationActivityStudent, Long> repo;

	@Override
	@Transactional
	public void signUp(long code, long userId) {
		ImperialExaminationActivityStudent domain = new ImperialExaminationActivityStudent();
		domain.setActivityCode(code);
		domain.setUserId(userId);
		repo.save(domain);
	}

	@Override
	public ImperialExaminationActivityStudent getUser(long code, long userId) {
		List<ImperialExaminationActivityStudent> list = repo
				.find("$getByUser", Params.param("userId", userId).put("code", code)).list();
		return list.size() > 0 ? list.get(0) : null;
	}

	@Override
	public List<Long> getUsers(long code, List<Long> userIds) {
		List<Long> studentIds = Lists.newArrayList();
		Params params = Params.param();
		params.put("userIds", userIds);
		params.put("code", code);
		List<ImperialExaminationActivityStudent> students = repo.find("$getByUsers", params).list();
		if (CollectionUtils.isNotEmpty(students)) {
			for (ImperialExaminationActivityStudent value : students) {
				studentIds.add(value.getUserId());
			}
		}

		return studentIds;
	}

	@Override
	@Transactional
	public void signUpByUsers(long code, List<Long> userIds) {
		List<ImperialExaminationActivityStudent> domains = Lists.newArrayList();
		for (Long userId : userIds) {
			ImperialExaminationActivityStudent domain = new ImperialExaminationActivityStudent();
			domain.setActivityCode(code);
			domain.setUserId(userId);
			domains.add(domain);
		}

		repo.save(domains);
	}

}

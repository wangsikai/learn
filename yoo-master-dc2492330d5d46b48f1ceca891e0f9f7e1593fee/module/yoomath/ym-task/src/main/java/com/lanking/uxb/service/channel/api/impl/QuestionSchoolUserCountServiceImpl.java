package com.lanking.uxb.service.channel.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.school.QuestionSchool;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.channel.api.QuestionSchoolUserCountService;

@Transactional(readOnly = true)
@Service
public class QuestionSchoolUserCountServiceImpl implements QuestionSchoolUserCountService {

	@Autowired
	@Qualifier("QuestionSchoolRepo")
	Repo<QuestionSchool, Integer> schoolRepo;

	@Transactional
	@Override
	public void staticSchoolUserCount() {
		List<QuestionSchool> schools = findAllQuestionSchool(Status.ENABLED);
		List<QuestionSchool> newSchools = new ArrayList<QuestionSchool>();
		for (QuestionSchool school : schools) {
			long count = schoolRepo.find("$taskGetStaticSchoolSvip", Params.param("schoolId", school.getSchoolId()))
					.count();
			school.setTeacherSchoolVipCount(count);
			newSchools.add(school);
		}
		schoolRepo.save(newSchools);

	}

	@Override
	public List<QuestionSchool> findAllQuestionSchool(Status status) {
		Params param = Params.param("", "");
		if (status != null) {
			param.put("status", status.getValue());
		}
		return schoolRepo.find("$taskFindAll", param).list();
	}

	public List<QuestionSchool> findAllQuestionSchool() {
		return this.findAllQuestionSchool(null);
	}
}

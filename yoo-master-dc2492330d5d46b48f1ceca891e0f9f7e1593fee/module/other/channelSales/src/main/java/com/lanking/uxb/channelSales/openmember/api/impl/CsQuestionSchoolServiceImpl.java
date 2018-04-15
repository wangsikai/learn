package com.lanking.uxb.channelSales.openmember.api.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.school.QuestionSchool;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.channelSales.openmember.api.CsQuestionSchoolService;

/**
 * @author xinyu.zhou
 * @since 2.6.0
 */
@Service
@Transactional(readOnly = true)
public class CsQuestionSchoolServiceImpl implements CsQuestionSchoolService {
	@Autowired
	@Qualifier("QuestionSchoolRepo")
	private Repo<QuestionSchool, Long> questionSchoolRepo;

	@Override
	@Transactional
	public void create(long schoolId, int users) {
		QuestionSchool questionSchool = questionSchoolRepo.find("$csFindBySchool", Params.param("schoolId", schoolId))
				.get();

		if (questionSchool == null) {
			questionSchool = new QuestionSchool();
			questionSchool.setCreateAt(new Date());
			questionSchool.setQuestionCount(0);
			questionSchool.setRecordQuestionCount(0L);
			questionSchool.setSchoolId(schoolId);
			questionSchool.setStatus(Status.ENABLED);
			questionSchool.setTeacherCount(users);

			questionSchoolRepo.save(questionSchool);
		}
	}
}

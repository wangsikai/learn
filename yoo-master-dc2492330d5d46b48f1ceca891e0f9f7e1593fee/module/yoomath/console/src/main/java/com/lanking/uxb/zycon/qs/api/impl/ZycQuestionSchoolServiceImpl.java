package com.lanking.uxb.zycon.qs.api.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.school.QuestionSchool;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.zycon.book.api.ZycSchoolBookService;
import com.lanking.uxb.zycon.qs.api.ZycQuestionSchoolService;

/**
 *
 * @see ZycQuestionSchoolService
 * @author xinyu.zhou
 * @since yoomath V1.4.2
 */
@Service
@Transactional(readOnly = true)
public class ZycQuestionSchoolServiceImpl implements ZycQuestionSchoolService {

	@Autowired
	@Qualifier("QuestionSchoolRepo")
	private Repo<QuestionSchool, Long> schoolRepo;
	@Autowired
	private ZycSchoolBookService zycSchoolBookService;

	@Override
	public QuestionSchool get(long id) {
		return schoolRepo.get(id);
	}

	@Override
	public Page<QuestionSchool> page(Pageable pageable, String schoolName) {
		Params params = Params.param();
		if (!StringUtils.isBlank(schoolName)) {
			schoolName = "%" + schoolName + "%";
			params.put("schoolName", schoolName);
		}
		return schoolRepo.find("$zycFindByName", params).fetch(pageable);
	}

	@Override
	@Transactional
	public QuestionSchool update(long schoolId, Status status) {
		QuestionSchool questionSchool = schoolRepo.get(schoolId);
		if (questionSchool == null)
			return null;

		questionSchool.setStatus(status);
		// 把对应的书本也做相应的操作
		zycSchoolBookService.updateSchoolBook(schoolId, status);
		return schoolRepo.save(questionSchool);
	}

	@Override
	@Transactional
	public QuestionSchool incrQuestionCount(long schoolId, long count) {
		QuestionSchool questionSchool = schoolRepo.get(schoolId);
		if (questionSchool == null)
			return null;

		Long nowCount = count + questionSchool.getQuestionCount();
		questionSchool.setQuestionCount(nowCount);
		return schoolRepo.save(questionSchool);
	}

	@Override
	@Transactional
	public QuestionSchool create(long schoolId) {
		QuestionSchool school = schoolRepo.get(schoolId);
		if (school != null && school.getStatus() == Status.DELETED) {
			school.setStatus(Status.DISABLED);
		} else if (school != null && (school.getStatus() == Status.ENABLED || school.getStatus() == Status.DISABLED)) {
			return null;
		} else {
			school = new QuestionSchool();
			school.setQuestionCount(0);
			school.setTeacherCount(0);
			school.setCreateAt(new Date());
			school.setStatus(Status.DISABLED);
			school.setSchoolId(schoolId);
			school.setRecordQuestionCount(0L);
		}
		return schoolRepo.save(school);
	}

	@Override
	@Transactional
	public void updateRecordQuestionCount(Long schoolId, Long count) {
		QuestionSchool school = schoolRepo.get(schoolId);
		if (school != null) {
			school.setRecordQuestionCount(count);
			schoolRepo.save(school);
		}
	}
}

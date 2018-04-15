package com.lanking.uxb.service.data.api.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.uxb.service.base.type.CommonSettings;
import com.lanking.uxb.service.data.api.StudentDailyPractisePushService;
import com.lanking.uxb.service.web.api.DailyPractiseGenerateService;

/**
 * @see StudentDailyPractisePushService
 * @author xinyu.zhou
 * @since yoomath(mobile) V1.0.0
 */
@Transactional(readOnly = true)
@Service
public class StudentDailyPractisePushServiceImpl implements StudentDailyPractisePushService {
	@Autowired
	private DailyPractiseGenerateService generateService;

	@Override
	@Transactional
	public void push(Collection<Student> students) {
		for (Student s : students) {
			generateService.generate(s, CommonSettings.QUESTION_PULL_COUNT);
		}
	}
}

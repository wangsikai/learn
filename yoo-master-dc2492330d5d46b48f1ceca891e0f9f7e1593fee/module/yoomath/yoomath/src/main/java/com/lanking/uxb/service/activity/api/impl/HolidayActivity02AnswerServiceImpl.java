package com.lanking.uxb.service.activity.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02Answer;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.activity.api.HolidayActivity02AnswerService;

/**
 * 假期活动02接口实现
 * 
 * @author qiuxue.jiang
 *
 */
@Service
@Transactional(readOnly = true)
public class HolidayActivity02AnswerServiceImpl implements HolidayActivity02AnswerService {
	@Autowired
	@Qualifier("HolidayActivity02AnswerRepo")
	private Repo<HolidayActivity02Answer, Long> repo;

	@Override
	@Transactional
	public void save(HolidayActivity02Answer answer) {
		repo.save(answer);
	}

	@Override
	public HolidayActivity02Answer getPastExamAnswer(Long questionsId) {
		Params params = Params.param("questionsId", questionsId);

		HolidayActivity02Answer answer = repo.find("$getAnswer", params).get();
		
		return answer;
	}


	
}

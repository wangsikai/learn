package com.lanking.uxb.service.activity.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02Question;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.activity.api.HolidayActivity02QuestionService;

/**
 * 假期活动02接口实现
 * 
 * @author qiuxue.jiang
 *
 */
@Service
@Transactional(readOnly = true)
public class HolidayActivity02QuestionServiceImpl implements HolidayActivity02QuestionService {
	@Autowired
	@Qualifier("HolidayActivity02QuestionRepo")
	private Repo<HolidayActivity02Question, Long> repo;

	@Override
	@Transactional
	public void save(HolidayActivity02Question holidayActivity02Question) {
		repo.save(holidayActivity02Question);
	}

	@Override
	public List<Long> getQuestionIds(Long activityCode, Long pkRecordId) {
		HolidayActivity02Question question = getQuestion(activityCode,pkRecordId);
		
		if(question != null) {
			return question.getQuestionList();
		}
		
		return null;
	}

	@Override
	public HolidayActivity02Question getQuestion(Long activityCode, Long pkRecordId) {
		Params params = Params.param("pkRecordId", pkRecordId);
		params.put("code", activityCode);
		HolidayActivity02Question question = repo.find("$getQuestion", params).get();
		
		return question;
	}



}

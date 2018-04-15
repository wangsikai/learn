package com.lanking.uxb.zycon.holiday.api.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemAnswer;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.holiday.api.ZycHolidayStuHomeworkItemAnswerService;

/**
 * @author xinyu.zhou
 * @since yoomath V1.9
 */
@Service
@Transactional(readOnly = true)
public class ZycHolidayStuHomeworkItemAnswerServiceImpl implements ZycHolidayStuHomeworkItemAnswerService {

	@Autowired
	@Qualifier("HolidayStuHomeworkItemAnswerRepo")
	private Repo<HolidayStuHomeworkItemAnswer, Long> repo;

	@Override
	public List<HolidayStuHomeworkItemAnswer> query(long stuHomeworkItemQuestionId) {
		return repo.find("$zycQueryByItemQuestion", Params.param("itemQuestionId", stuHomeworkItemQuestionId)).list();
	}

	@Override
	public List<HolidayStuHomeworkItemAnswer> mgetListByQuestion(Collection<Long> stuHomeworkItemQuestionIds) {
		return repo.find("$zycQuestionByItemQuestions", Params.param("itemQuestionIds", stuHomeworkItemQuestionIds))
				.list();
	}
}

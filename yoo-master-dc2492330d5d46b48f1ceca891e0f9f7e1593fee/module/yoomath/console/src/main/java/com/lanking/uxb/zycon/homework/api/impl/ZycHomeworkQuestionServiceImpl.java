package com.lanking.uxb.zycon.homework.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.homework.HomeworkQuestion;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.homework.api.ZycHomeworkQuestionService;

/**
 * @author xinyu.zhou
 * @since yoomath V1.5
 */
@Transactional
@Service
public class ZycHomeworkQuestionServiceImpl implements ZycHomeworkQuestionService {
	@Autowired
	@Qualifier("HomeworkQuestionRepo")
	private Repo<HomeworkQuestion, Long> repo;

	@Override
	public List<Long> getQuestion(long hkId) {
		return repo.find("$zycGetQuestion", Params.param("homeworkId", hkId)).list(Long.class);
	}
}

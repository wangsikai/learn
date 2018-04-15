package com.lanking.uxb.zycon.homework.api.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.homework.QuestionCorrectLog;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.homework.api.ZycHomeworkCorrectLogService;
import com.lanking.uxb.zycon.homework.api.ZycQuestionCorrectLogService;
import com.lanking.uxb.zycon.homework.form.QuestionCorrectLogForm;

/**
 * @author xinyu.zhou
 * @see ZycHomeworkCorrectLogService
 * @since yoomath V1.7
 */
@Service
@Transactional(readOnly = true)
public class ZycQuestionCorrectLogServiceImpl implements ZycQuestionCorrectLogService {

	@Autowired
	@Qualifier("QuestionCorrectLogRepo")
	private Repo<QuestionCorrectLog, Long> repo;

	@Override
	public Page<Map> page(Pageable pageable, QuestionCorrectLogForm form) {
		Params param = Params.param();
		
		param.put("homeworkId", form.getHomeworkId());
		param.put("sequence", form.getSequence());
		param.put("accountName", form.getAccountName());
		if(form.getCorrect() != null) {
			param.put("newCorrect", form.getCorrect());
		}

		Page<Map> page = repo.find("$zycFindQuestionCorrectLog", param).fetch(pageable, Map.class);
		
		return page;
	}

}

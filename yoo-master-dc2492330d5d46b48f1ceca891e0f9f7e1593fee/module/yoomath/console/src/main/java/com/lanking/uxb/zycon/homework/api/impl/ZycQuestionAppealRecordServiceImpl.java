package com.lanking.uxb.zycon.homework.api.impl;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.homework.QuestionAppeal;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.homework.api.ZycHomeworkCorrectLogService;
import com.lanking.uxb.zycon.homework.api.ZycQuestionAppealRecordService;
import com.lanking.uxb.zycon.homework.form.QuestionAppealRecordForm;


/**
 * @author xinyu.zhou
 * @see ZycHomeworkCorrectLogService
 * @since yoomath V1.7
 */
@Service
@Transactional(readOnly = true)
public class ZycQuestionAppealRecordServiceImpl implements ZycQuestionAppealRecordService {

	@Autowired
	@Qualifier("QuestionAppealRepo")
	private Repo<QuestionAppeal, Long> repo;

	@Override
	public Page<Map> page(Pageable pageable, QuestionAppealRecordForm form) {
		Params param = Params.param();
		
		if(StringUtils.isNotBlank(form.getStartTime())){
			param.put("startTime", form.getStartTime());
		}
		
		if(StringUtils.isNotBlank(form.getEndTime())){
			param.put("endTime", form.getEndTime());
		}
		
		if(StringUtils.isNotBlank(form.getAccountName())){
			param.put("accountName", form.getAccountName());
		}
		
		if(StringUtils.isNotBlank(form.getSchoolName())){
			param.put("schoolName", form.getSchoolName());
		}
		
		if(StringUtils.isNotBlank(form.getCorrectName())){
			param.put("correctName", form.getCorrectName());
		}
		
		if(form.getStatus() != null) {
			param.put("status", form.getStatus().getValue());
		}

		Page<Map> page = repo.find("$zycFindQuestionAppeal", param).fetch(pageable, Map.class);
		
		return page;
	}

}

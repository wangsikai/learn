package com.lanking.uxb.zycon.homework.api.impl;

import httl.util.StringUtils;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.zycon.homework.api.ZycCorrectingService;
import com.lanking.uxb.zycon.homework.form.HomeworkQueryForm;
import com.lanking.uxb.zycon.homework.form.HomeworkQueryType;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4
 */
@Service
@Transactional(readOnly = true)
public class ZycCorrectionServiceImpl implements ZycCorrectingService {
	@Autowired
	@Qualifier("HomeworkRepo")
	private Repo<Homework, Long> homeworkRepo;
	
	@Override
	public Page<Homework> page(Pageable pageable, HomeworkQueryType type) {
		Date nowDate = new Date();
		Params params = Params.param();
		params.put("type", type);
		Page<Homework> page = homeworkRepo.find("$zycGetTodo", params).fetch(pageable);
		return page;
	}

	@Override
	public List<Homework> list(int lastCommitMinute) {
		Date nowDate = new Date();
		Date lastDate = DateUtils.addMinutes(nowDate, -lastCommitMinute);
		Params params = Params.param();
		params.put("nowDate", nowDate);
		params.put("lastDate", lastDate);
		return homeworkRepo.find("$zycGetTodo", params).list();
	}

	@Override
	public Page<Homework> page(HomeworkQueryForm form) {
		int offset = (form.getPage() - 1) * form.getSize();
		Date flagDate = DateUtils.addMinutes(new Date(), -Env.getInt("homework.allcommit.then"));

		Pageable pageable = P.offset(offset, form.getSize());
		Params params = Params.param();
		
		int finish = 0, working = 0, init = 0;

		for (HomeworkQueryType type : form.getTypes()) {
			switch (type) {
			case FINISH:
				finish = 1;
				break;
			case WORKING:
				working = 1;
				break;
			case INIT:
				init = 1;
				break;
			}
		}

		params.put("finish", finish);
		params.put("working", working);
		params.put("init", init);
		params.put("flagDate", flagDate);
		if (StringUtils.isNotEmpty(form.getStartTime())) {
			params.put("startTime", form.getStartTime());
		}
		if (StringUtils.isNotEmpty(form.getEndTime())) {
			params.put("endTime", form.getEndTime());
		}
		if (StringUtils.isNotEmpty(form.getSchoolName())) {
			params.put("schoolName", "%" + form.getSchoolName() + "%");
		}

		Page<Homework> page = homeworkRepo.find("$zycGetTodo", params).fetch(pageable);
		return page;
	}

}

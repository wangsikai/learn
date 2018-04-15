package com.lanking.uxb.service.diagnostic.api.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomework;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkItemQuestion;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.diagnostic.api.StaticHolidayHomeworkService;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
@Service
@Transactional(readOnly = true)
public class StaticHolidayHomeworkServiceImpl implements StaticHolidayHomeworkService {

	@Autowired
	@Qualifier("HolidayHomeworkRepo")
	private Repo<HolidayHomework, Long> holidayHomeworkRepo;

	@Autowired
	@Qualifier("HolidayHomeworkItemQuestionRepo")
	private Repo<HolidayHomeworkItemQuestion, Long> holidayHomeworkItemRepo;

	@Override
	public HolidayHomework get(long id) {
		return holidayHomeworkRepo.get(id);
	}

	@Override
	public List<Map> findHolidayHkItemQuestion(long id) {
		return holidayHomeworkItemRepo.find("$ymFindHolidayHkItemQuestion", Params.param("hkId", id)).list(Map.class);
	}

	@Override
	public List<HolidayHomework> getHks(long classId) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		Date now = new Date();
		Date yesterday = DateUtils.addDays(now, -1);
		return holidayHomeworkRepo.find(
				"$ymFindHolidayHk",
				Params.param("classId", classId).put("beginDate", simpleDateFormat.format(now))
						.put("endDate", simpleDateFormat.format(yesterday))).list();
	}

	@Override
	public List<HolidayHomework> findAll(long classId) {
		return holidayHomeworkRepo.find("$ymFindHolidayHk", Params.param("classId", classId)).list();
	}
}

package com.lanking.uxb.service.data.api.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.stat.HomeworkStudentClazzStat;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.data.api.HomeworkStudentClazzStatService;

@Service
@Transactional(readOnly = true)
public class HomeworkStudentClazzStatServiceImpl implements HomeworkStudentClazzStatService {

	@Autowired
	@Qualifier("HomeworkStudentClazzStatRepo")
	private Repo<HomeworkStudentClazzStat, Long> repo;
	@Autowired
	@Qualifier("HomeworkRepo")
	private Repo<Homework, Long> homeworkRepo;

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void statisticDay30(long classId) {
		Homework homework = homeworkRepo.find("$getLatestHomeworkDate", Params.param("classId", classId)).get();
		repo.execute("$deleteByClassId", Params.param("classId", classId));
		Date endDate = null;
		if (homework != null) {
			endDate = homework.getCreateAt();
		} else {
			endDate = new Date();
		}

		Date beginDate = DateUtils.addDays(endDate, -30);
		Params params = Params.param();
		params.put("classId", classId);
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		List<Map> listMap = repo.find("$getAvgData", params).list(Map.class);

		for (Map m : listMap) {
			HomeworkStudentClazzStat homeworkStudentClazzStat = new HomeworkStudentClazzStat();
			homeworkStudentClazzStat.setClassId(classId);
			homeworkStudentClazzStat.setCreateAt(new Date());
			homeworkStudentClazzStat.setDays30RightRate((BigDecimal) m.get("avgright"));
			homeworkStudentClazzStat.setStudentId(Long.valueOf(m.get("studentid").toString()));
			repo.save(homeworkStudentClazzStat);
		}
	}
}

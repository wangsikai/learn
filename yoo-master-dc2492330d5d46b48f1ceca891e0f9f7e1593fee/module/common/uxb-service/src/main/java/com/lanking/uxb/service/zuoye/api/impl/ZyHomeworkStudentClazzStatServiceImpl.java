package com.lanking.uxb.service.zuoye.api.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.stat.HomeworkStudentClazzStat;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzStatService;

@Service
@Transactional(readOnly = true)
public class ZyHomeworkStudentClazzStatServiceImpl implements ZyHomeworkStudentClazzStatService {

	@Autowired
	@Qualifier("HomeworkStudentClazzStatRepo")
	Repo<HomeworkStudentClazzStat, Long> hkStuClazzRepo;

	@Override
	public List<HomeworkStudentClazzStat> findTopStudent(long classId, long size) {
		Params params = Params.param("classId", classId);
		params.put("size", size);

		return hkStuClazzRepo.find("$getTopStudent", params).list();
	}

	@Override
	@Transactional
	public void removeByStudentId(long studentId, long classId) {
		HomeworkStudentClazzStat stat = hkStuClazzRepo
				.find("$getByStudentId", Params.param("studentId", studentId).put("classId", classId)).get();
		if (stat != null) {
			stat.setStatus(Status.DELETED);
			hkStuClazzRepo.save(stat);
		}
	}

	@Override
	@Transactional
	public void recoverByStudentId(long studentId, long classId) {
		HomeworkStudentClazzStat stat = hkStuClazzRepo
				.find("$getByStudentId", Params.param("studentId", studentId).put("classId", classId)).get();
		if (stat != null) {
			stat.setStatus(Status.ENABLED);
			hkStuClazzRepo.save(stat);
		}
	}

	@Override
	public List<HomeworkStudentClazzStat> findStudentByRightRate(long classId, BigDecimal days30RightRate) {
		Params params = Params.param("classId", classId);
		params.put("days30RightRate", days30RightRate);
		return hkStuClazzRepo.find("$findStudentByRightRate", params).list();
	}

}

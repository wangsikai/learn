package com.lanking.uxb.service.zuoye.api.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.stat.HomeworkStudentClazzKnowpointStat;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.zuoye.api.ZyHkStuClazzKnowpointStatService;

@Service
@Transactional(readOnly = true)
public class ZyHkStuClazzKnowpointStatServiceImpl implements ZyHkStuClazzKnowpointStatService {

	@Autowired
	@Qualifier("HomeworkStudentClazzKnowpointStatRepo")
	Repo<HomeworkStudentClazzKnowpointStat, Long> hkStuClazzKpRepo;

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> getStuKnowpointStat(Long studentId, Long classId) {
		return hkStuClazzKpRepo
				.find("$getStuKnowpointStat", Params.param("studentId", studentId).put("classId", classId))
				.list(Map.class);
	}

	@Override
	public List<Map> getStuLowKnowpointStat(Long studentId, Long classId) {
		return hkStuClazzKpRepo
				.find("$getStuLowKnowpointStat", Params.param("studentId", studentId).put("classId", classId))
				.list(Map.class);
	}
}

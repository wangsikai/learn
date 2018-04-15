package com.lanking.uxb.service.code.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.Subject;

public interface SubjectService {

	int PHASE_2_MATH = 202;// 初中数学
	int PHASE_3_MATH = 302;// 高中数学

	Subject get(Integer code);

	List<Subject> getAll();

	Map<Integer, Subject> mgetAll();

	Map<Integer, Subject> mget(Collection<Integer> codes);

	List<Subject> findByPhaseCode(Integer phaseCode);

}

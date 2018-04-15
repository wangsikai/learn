package com.lanking.uxb.service.zuoye.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.stat.StudentExerciseKnowpoint;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.zuoye.api.ZyStudentExerciseKnowpointService;

/**
 * @see ZyStudentExerciseKnowpointService
 * @author xinyu.zhou
 * @since yoomath V1.6
 * @since yoomath v2.1.2 新知识点
 */
@Service
@Transactional(readOnly = true)
public class ZyStudentExerciseKnowpointServiceImpl implements ZyStudentExerciseKnowpointService {
	@Autowired
	@Qualifier("StudentExerciseKnowpointRepo")
	private Repo<StudentExerciseKnowpoint, Long> repo;

	@Override
	public Map<Integer, StudentExerciseKnowpoint> mgetByCodes(Collection<Integer> codes, long studentId) {
		Map<Integer, StudentExerciseKnowpoint> map = Maps.newHashMap();
		if (CollectionUtils.isNotEmpty(codes)) {
			List<StudentExerciseKnowpoint> knowpointList = repo
					.find("$zyGetByCodes", Params.param("codes", codes).put("studentId", studentId)).list();
			for (StudentExerciseKnowpoint k : knowpointList) {
				map.put((int) k.getKnowpointCode(), k);
			}
		}
		return map;
	}

	@Override
	public Map<Long, StudentExerciseKnowpoint> mgetNewByCodes(Collection<Long> codes, long studentId) {
		Map<Long, StudentExerciseKnowpoint> map = Maps.newHashMap();
		if (CollectionUtils.isNotEmpty(codes)) {
			List<StudentExerciseKnowpoint> knowpointList = repo
					.find("$zyGetNewByCodes", Params.param("codes", codes).put("studentId", studentId)).list();
			for (StudentExerciseKnowpoint k : knowpointList) {
				map.put(k.getKnowpointCode(), k);
			}
		}
		return map;
	}

	@Override
	public StudentExerciseKnowpoint getByCode(int code, long studentId) {
		return repo.find("$zyGetByCode", Params.param("code", code).put("studentId", studentId)).get();
	}

	@Override
	public Map<Integer, List<StudentExerciseKnowpoint>> getByCodeAndClass(List<Integer> codes, long classId) {
		Params params = Params.param("classId", classId);
		if (codes != null && codes.size() > 0) {
			params.put("codes", codes);
		}
		List<StudentExerciseKnowpoint> knowpoints = repo.find("$zyGetByClass", params).list();
		if (CollectionUtils.isEmpty(knowpoints)) {
			return Maps.newHashMap();
		}

		Map<Integer, List<StudentExerciseKnowpoint>> tmpMap = Maps.newHashMap();
		for (StudentExerciseKnowpoint k : knowpoints) {
			List<StudentExerciseKnowpoint> points;
			if (CollectionUtils.isEmpty(tmpMap.get((int) k.getKnowpointCode()))) {
				points = Lists.newArrayList();
			} else {
				points = tmpMap.get((int) k.getKnowpointCode());
			}

			points.add(k);

			tmpMap.put((int) k.getKnowpointCode(), points);
		}

		return tmpMap;
	}
}

package com.lanking.uxb.zycon.activity.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Exercise;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01ExerciseType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.activity.api.ZycHolidayActivity01ExerciseService;
import com.lanking.uxb.zycon.activity.cache.ZycHolidayActivity01CacheService;

/**
 * 暑期作业活动-练习相关接口实现.
 * 
 * @since 教师端 v1.2.0
 *
 */
@Transactional(readOnly = true)
@Service
public class ZycHolidayActivity01ExerciseServiceImpl implements ZycHolidayActivity01ExerciseService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier("HolidayActivity01ExerciseRepo")
	private Repo<HolidayActivity01Exercise, Long> holidayActivity01ExerciseRepo;

	@Autowired
	private ZycHolidayActivity01CacheService holidayActivity01CacheService;

	@Override
	@Transactional
	public HolidayActivity01Exercise getLastTeacherExercise(long activityCode, long teacherId,
			HolidayActivity01ExerciseType type) {
		HolidayActivity01Exercise exercise = holidayActivity01ExerciseRepo.find("$getLastTeacherExercises",
				Params.param("teacherId", teacherId).put("type", type.getValue()).put("activityCode", activityCode))
				.get();
		return exercise;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> listTeacherWeakpointCount() {
		return holidayActivity01ExerciseRepo.find("$listTeacherWeakpointCount").list(Map.class);
	}

	@Override
	public void handleAllKnowpointQuestions() {
		logger.info("[2018寒假作业活动] 开始初始化知识点习题");
		long t1 = System.currentTimeMillis();
		List<Long> allKnowpointCodes = holidayActivity01ExerciseRepo.find("$listAllKnowpoint").list(Long.class);
		for (Long code : allKnowpointCodes) {
			List<Long> qids = holidayActivity01ExerciseRepo
					.find("$listQuestionByKnowpoint", Params.param("knowledgeCode", code).put("num", 30))
					.list(Long.class);
			holidayActivity01CacheService.setKnowpointQuestions(code, qids);
		}
		long t2 = System.currentTimeMillis();
		logger.info("[2018寒假作业活动] 知识点习题初始化完成，共耗时" + (t2 - t1) / 1000 + "秒");
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Long, Map<Long, List<Long>>> listTeacherWeakpoint(Collection<Long> teacherIds) {
		List<Map> list = holidayActivity01ExerciseRepo
				.find("$listTeacherWeakpoint", Params.param("teacherIds", teacherIds)).list(Map.class);

		Map<Long, Map<Long, List<Long>>> returnDatas = new HashMap<Long, Map<Long, List<Long>>>();
		for (Map map : list) {
			long teacherId = Long.parseLong(map.get("teacher_id").toString());
			long knowpointCode = Long.parseLong(map.get("knowpoint_code").toString());
			long l2KnowpointCode = knowpointCode / 1000; // 知识点小专题代码
			Map<Long, List<Long>> knowpointCodeMap = returnDatas.get(teacherId);
			if (knowpointCodeMap == null) {
				knowpointCodeMap = new HashMap<Long, List<Long>>();
				returnDatas.put(teacherId, knowpointCodeMap);
			}
			List<Long> knowpointCodes = knowpointCodeMap.get(l2KnowpointCode);
			if (knowpointCodes == null) {
				knowpointCodes = new ArrayList<Long>();
				knowpointCodeMap.put(l2KnowpointCode, knowpointCodes);
			}
			knowpointCodes.add(knowpointCode);
		}

		// 小专题内知识点超过15个，则随机15个知识点
		for (Entry<Long, Map<Long, List<Long>>> entry : returnDatas.entrySet()) {
			Map<Long, List<Long>> knowpointCodeMap = entry.getValue();
			for (Entry<Long, List<Long>> entry2 : knowpointCodeMap.entrySet()) {
				List<Long> knowpointCodes = entry2.getValue();
				if (knowpointCodes.size() > 15) {
					Collections.shuffle(knowpointCodes);
					knowpointCodes = knowpointCodes.subList(0, 14);
					Collections.sort(knowpointCodes);
				}
			}
		}

		return returnDatas;
	}
}

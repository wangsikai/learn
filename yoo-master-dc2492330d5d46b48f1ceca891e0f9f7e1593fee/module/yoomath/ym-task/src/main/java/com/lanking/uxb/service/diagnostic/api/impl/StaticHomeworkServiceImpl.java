package com.lanking.uxb.service.diagnostic.api.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.HomeworkQuestion;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.diagnostic.api.StaticHomeworkService;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
@Service
@Transactional
public class StaticHomeworkServiceImpl implements StaticHomeworkService {

	@Autowired
	@Qualifier("HomeworkRepo")
	private Repo<Homework, Long> hkRepo;

	@Autowired
	@Qualifier("HomeworkQuestionRepo")
	private Repo<HomeworkQuestion, Long> hkqRepo;

	@Autowired
	@Qualifier("StudentHomeworkRepo")
	private Repo<StudentHomework, Long> shRepo;

	@Autowired
	@Qualifier("StudentHomeworkQuestionRepo")
	private Repo<StudentHomeworkQuestion, Long> shqRepo;

	@Override
	public Homework get(long id) {
		return hkRepo.get(id);
	}

	@Override
	public Page<Homework> findTodayIssuedHk(Pageable pageable) {
		Date nowDate = new Date();
		Date yesterday = DateUtils.addDays(nowDate, -1);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String startAt = format.format(yesterday);
		String endAt = format.format(nowDate);
		return hkRepo.find("$ymGetNowDayIssuedHk", Params.param("startAt", startAt).put("endAt", endAt))
				.fetch(pageable);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> findHomeworkQuestion(long id) {
		return findHomeworkQuestion(Lists.newArrayList(id), null);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> findHomeworkQuestion(Collection<Long> ids, Collection<Long> studentIds) {
		Params params = Params.param("homeworkIds", ids);
		if (CollectionUtils.isNotEmpty(studentIds)) {
			params.put("studentIds", studentIds);
		}
		return shqRepo.find("$ymFindAllQuestionResultFromHomework", params).list(Map.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Page<Map> findHomeworkQuestionByStudent(Long classId, long studentId, Pageable pageable) {
		Params params = Params.param("studentId", studentId);
		if (classId != null) {
			params.put("classId", classId);
		}
		return shqRepo.find("$ymFindAllQuestionResultFromStudent", params).fetch(pageable, Map.class);
	}

	@Override
	public List<Homework> getLatestHks(long classId) {
		return hkRepo.find("$ymGetLatestHomework", Params.param("classId", classId)).list();
	}

	@Override
	public List<Homework> getByExerciseId(long exerciseId) {
		return hkRepo.find("$ymFindByExercise", Params.param("exerciseId", exerciseId)).list();
	}

	@Override
	public Map<Long, List<Homework>> mgetByExerciseIds(Collection<Long> exerciseIds) {
		Map<Long, List<Homework>> map = new HashMap<Long, List<Homework>>();
		if (CollectionUtils.isEmpty(exerciseIds)) {
			return map;
		}
		List<Homework> homeworks = hkRepo.find("$ymFindByExercises", Params.param("exerciseIds", exerciseIds)).list();
		for (Homework homework : homeworks) {
			List<Homework> list = map.get(homework.getExerciseId());
			if (list == null) {
				list = new ArrayList<Homework>();
				map.put(homework.getExerciseId(), list);
			}
			list.add(homework);
		}
		return map;
	}

	@Override
	public List<Homework> findAllByClassId(long classId) {
		return hkRepo.find("$ymFindAllByClass", Params.param("classId", classId)).list();
	}

	@Override
	public boolean hasStudentHomework(long classId, long studentId, Date curDate) {
		Params params = Params.param("classId", classId).put("studentId", studentId);
		if (curDate != null) {
			params.put("curDate", curDate);
		}
		long count = shRepo.find("$ymCountStudentHomeworkForDiagnostic", params).count();
		return count > 0 ? true : false;
	}
}

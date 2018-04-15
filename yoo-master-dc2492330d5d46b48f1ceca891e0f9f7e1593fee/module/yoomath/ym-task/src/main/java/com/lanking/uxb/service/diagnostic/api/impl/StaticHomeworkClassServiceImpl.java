package com.lanking.uxb.service.diagnostic.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.diagnostic.api.StaticHomeworkClassService;

/**
 * @see StaticHomeworkClassService
 * @author xinyu.zhou
 * @since 2.1.0
 */
@Service
@Transactional(readOnly = true)
public class StaticHomeworkClassServiceImpl implements StaticHomeworkClassService {
	@Autowired
	@Qualifier("HomeworkClazzRepo")
	private Repo<HomeworkClazz, Long> repo;

	@Autowired
	@Qualifier("HomeworkStudentClazzRepo")
	private Repo<HomeworkStudentClazz, Long> hscRepo;

	@Override
	public HomeworkClazz get(long id) {
		return repo.get(id);
	}

	@Override
	public Map<Long, HomeworkClazz> mget(Collection<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return new HashMap<Long, HomeworkClazz>(0);
		}
		return repo.mget(ids);
	}

	@Override
	public CursorPage<Long, HomeworkClazz> findEnableClass(CursorPageable<Long> cursorPageable) {
		return repo.find("$ymFindEnableClass", Params.param()).fetch(cursorPageable);
	}

	@Override
	public List<Long> findStudentIds(long classId) {
		return hscRepo.find("$taskListStudent", Params.param("classId", classId)).list(Long.class);
	}

	@Override
	public CursorPage<Long, Long> curDayIssuedClass(CursorPageable<Long> cursorPageable, Date startTime, Date endTime) {
		Params params = Params.param();
		if (startTime != null) {
			params.put("startTime", startTime);
		}
		if (endTime != null) {
			params.put("endTime", endTime);
		}
		return repo.find("$curDayIssuedClass", params).fetch(cursorPageable, Long.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Long, List<Long>> findAllByOneClassStudent(long homeworkClassId) {
		List<Map> list = repo.find("$findAllByOneClassStudent", Params.param("homeworkClassId", homeworkClassId))
				.list(Map.class);
		Map<Long, List<Long>> map = new HashMap<Long, List<Long>>();
		for (Map data : list) {
			long classId = Long.parseLong(data.get("class_id").toString());
			long studentId = Long.parseLong(data.get("student_id").toString());
			List<Long> studentIds = map.get(classId);
			if (studentIds == null) {
				studentIds = new ArrayList<Long>();
				map.put(classId, studentIds);
			}
			if (!studentIds.contains(studentIds)) {
				studentIds.add(studentId);
			}
		}
		return map;
	}
}

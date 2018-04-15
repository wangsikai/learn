package com.lanking.uxb.service.zuoye.api.impl;

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
import com.lanking.cloud.domain.yoomath.stat.StudentHomeworkStat;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkStatService;

@Transactional(readOnly = true)
@Service
public class ZyStudentHomeworkStatServiceImpl implements ZyStudentHomeworkStatService {

	@Autowired
	@Qualifier("StudentHomeworkStatRepo")
	Repo<StudentHomeworkStat, Long> stuHkStatRepo;

	@Override
	public Map<Long, StudentHomeworkStat> findByClazzId(long clazzId) {
		List<StudentHomeworkStat> stats = stuHkStatRepo.find("$zyFindByClazzId", Params.param("clazzId", clazzId))
				.list();
		Map<Long, StudentHomeworkStat> map = null;
		if (CollectionUtils.isNotEmpty(stats)) {
			map = new HashMap<Long, StudentHomeworkStat>(stats.size());
			for (StudentHomeworkStat s : stats) {
				map.put(s.getUserId(), s);
			}
		}
		return map == null ? new HashMap<Long, StudentHomeworkStat>(0) : map;
	}

	@Override
	public StudentHomeworkStat findOne(long studentId, Long homeworkClassId) {
		Params params = Params.param("studentId", studentId);
		if (homeworkClassId != null) {
			params.put("homeworkClassId", homeworkClassId);
		}
		return stuHkStatRepo.find("$zyFindOne", params).get();
	}

	@Transactional
	@Override
	public void updateAfterPublishHomework(long studentId, Long homeworkClassId) {
		StudentHomeworkStat hs = findOne(studentId, homeworkClassId);
		if (hs == null) {
			hs = new StudentHomeworkStat();
			hs.setHomeworkClassId(homeworkClassId);
			hs.setCreateAt(new Date());
			hs.setHomeWorkNum(1L);
			hs.setTodoNum(1L);
			hs.setHomeworkTime(0);
			hs.setRank(0);
			hs.setRightRate(null);
			hs.setUserId(studentId);
		} else {
			hs.setHomeWorkNum(hs.getHomeWorkNum() + 1);
			hs.setTodoNum(hs.getTodoNum() + 1);
		}
		hs.setUpdateAt(new Date());
		stuHkStatRepo.save(hs);
	}

	@Transactional
	@Override
	public void updateAfterCommitHomework(long studentId, Long homeworkClassId) {
		StudentHomeworkStat hs = findOne(studentId, homeworkClassId);
		if (hs == null) {
			hs = new StudentHomeworkStat();
			hs.setHomeworkClassId(homeworkClassId);
			hs.setCreateAt(new Date());
			hs.setHomeWorkNum(1L);
			hs.setTodoNum(0L);
			hs.setHomeworkTime(0);
			hs.setRank(0);
			hs.setRightRate(null);
			hs.setUserId(studentId);
		} else {
			hs.setTodoNum(hs.getTodoNum() - 1);
		}
		hs.setUpdateAt(new Date());
		stuHkStatRepo.save(hs);
	}

	@Override
	public StudentHomeworkStat getByHomeworkClassId(long studentId, long homeworkClassId) {
		return stuHkStatRepo.find("$zyGetByHomeworkClassId",
				Params.param("studentId", studentId).put("homeworkClassId", homeworkClassId)).get();
	}

	@Override
	public List<StudentHomeworkStat> getByHomeworkClassIds(long studentId, Collection<Long> homeworkClassIds) {
		return stuHkStatRepo.find("$zyGetByHomeworkClassIds",
				Params.param("studentId", studentId).put("homeworkClassIds", homeworkClassIds)).list();
	}

	@Override
	public List<StudentHomeworkStat> find(long homeworkClassId, Collection<Long> studentIds) {
		return stuHkStatRepo
				.find("$zyFind", Params.param("homeworkClassId", homeworkClassId).put("studentIds", studentIds)).list();
	}

	@Override
	public Map<Long, Integer> getSubmitRateByStuId(Long studentId, List<Long> classIds) {
		List<Map> list = stuHkStatRepo
				.find("$getSubmitRateByStuId", Params.param("studentId", studentId).put("classIds", classIds))
				.list(Map.class);
		Map<Long, Integer> map = new HashMap<Long, Integer>();
		for (Map pa : list) {
			Long classId = Long.parseLong(pa.get("homework_class_id").toString());
			if (pa.get("rate") != null) {
				Integer rate = Integer.parseInt(pa.get("rate").toString());
				map.put(classId, rate);
			} else {
				map.put(classId, null);
			}
		}
		return map;
	}

}

package com.lanking.uxb.service.zuoye.api.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazzGroupStudent;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassGroupStudentService;

/**
 * @author xinyu.zhou
 * @since 3.9.3
 */
@Service
@Transactional(readOnly = true)
public class ZyHomeworkClassGroupStudentServiceImpl implements ZyHomeworkClassGroupStudentService {
	@Autowired
	@Qualifier("HomeworkClazzGroupStudentRepo")
	Repo<HomeworkClazzGroupStudent, Long> repo;

	@Override
	public List<HomeworkClazzGroupStudent> findByStusAndClass(Collection<Long> studentIds, long classId) {
		return repo.find("$zyFindByStusAndClass", Params.param("studentIds", studentIds).put("classId", classId))
				.list();
	}

	@Override
	@Transactional
	public int removeStudents(Collection<Long> studentIds, long classId) {
		return repo.execute("$zyRemoveStudents", Params.param("studentIds", studentIds).put("classId", classId));
	}

	@Override
	@Transactional
	public void addStudents(Collection<Long> studentIds, long groupId, long classId) {
		for (Long id : studentIds) {
			HomeworkClazzGroupStudent g = new HomeworkClazzGroupStudent();
			g.setCreateAt(new Date());
			g.setUpdateAt(new Date());
			g.setGroupId(groupId);
			g.setClassId(classId);
			g.setStudentId(id);

			repo.save(g);
		}
	}

	@Override
	@Transactional
	public void removeStudents(long groupId) {
		repo.execute("$zyDeleteGroup", Params.param("groupId", groupId));
	}

	@Override
	public List<HomeworkClazzGroupStudent> findAll(long clazzId) {
		return repo.find("zyFindAll", Params.param("clazzId", clazzId)).list();
	}

	@Override
	@Transactional
	public void changeGroup(long clazzId, long studentId, long groupId) {
		HomeworkClazzGroupStudent homeworkClazzGroupStudent = repo.find("zyFindByClazzAndStudent",
				Params.param("clazzId", clazzId).put("studentId", studentId)).get();
		Date date = new Date();
		if (homeworkClazzGroupStudent == null) {
			homeworkClazzGroupStudent = new HomeworkClazzGroupStudent();
			homeworkClazzGroupStudent.setClassId(clazzId);
			homeworkClazzGroupStudent.setCreateAt(date);
			homeworkClazzGroupStudent.setStudentId(studentId);
		}
		homeworkClazzGroupStudent.setGroupId(groupId);
		homeworkClazzGroupStudent.setUpdateAt(date);
		repo.save(homeworkClazzGroupStudent);
	}

	@Override
	public int removeGroupStudents(Collection<Long> studentIds, long groupId) {
		if (CollectionUtils.isEmpty(studentIds) || groupId <= 0) {
			throw new IllegalArgException();
		}

		return repo.execute("$zyRemoveByGroup", Params.param("studentIds", studentIds).put("groupId", groupId));
	}

	@Override
	public List<Long> findGroupStudents(Long classId, Long groupId) {
		return repo.find("zyfindGroupStudents", Params.param("clazzId", classId).put("groupId", groupId)).list(
				Long.class);
	}
}

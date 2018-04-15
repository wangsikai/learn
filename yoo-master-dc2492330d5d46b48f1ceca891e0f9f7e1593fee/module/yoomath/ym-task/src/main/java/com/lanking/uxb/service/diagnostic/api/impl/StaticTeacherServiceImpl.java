package com.lanking.uxb.service.diagnostic.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.uxb.service.diagnostic.api.StaticTeacherService;

/**
 * 诊断统计服务教师接口实现.
 * 
 * @author wlche
 *
 */
@Service
@Transactional(readOnly = true)
public class StaticTeacherServiceImpl implements StaticTeacherService {

	@Autowired
	@Qualifier("TeacherRepo")
	private Repo<Teacher, Long> teacherRepo;

	@Override
	public Teacher get(long id) {
		return teacherRepo.get(id);
	}
}

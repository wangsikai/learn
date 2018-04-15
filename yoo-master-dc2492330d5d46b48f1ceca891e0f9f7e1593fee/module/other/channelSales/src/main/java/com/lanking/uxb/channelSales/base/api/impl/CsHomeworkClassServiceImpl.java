package com.lanking.uxb.channelSales.base.api.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.channelSales.base.api.CsHomeworkClassService;
import com.lanking.uxb.channelSales.base.form.HomeworkClazzForm;

import httl.util.StringUtils;

/**
 * @author xinyu.zhou
 * @since 3.9.2
 */
@Service
@Transactional(readOnly = true)
public class CsHomeworkClassServiceImpl implements CsHomeworkClassService {
	@Autowired
	@Qualifier("HomeworkClazzRepo")
	private Repo<HomeworkClazz, Long> homeworkClazzRepo;
	@Autowired
	@Qualifier("HomeworkStudentClazzRepo")
	private Repo<HomeworkStudentClazz, Long> homeworkStudentClazzRepo;

	@Override
	public Page<HomeworkClazz> query(HomeworkClazzForm queryForm, Pageable pageable) {
		Params params = Params.param();
		if (queryForm.getChannelCode() != null && queryForm.getChannelCode() > 0) {
			params.put("code", queryForm.getChannelCode());
		} else if (StringUtils.isNotBlank(queryForm.getChannelName())) {
			params.put("channelName", "%" + queryForm.getChannelName() + "%");
		}

		if (StringUtils.isNotBlank(queryForm.getClassName())) {
			params.put("className", "%" + queryForm.getClassName() + "%");
		}

		if (StringUtils.isNotBlank(queryForm.getSchoolName())) {
			params.put("schoolName", "%" + queryForm.getSchoolName() + "%");
		}
		if (queryForm.getSchoolId() != null) {
			params.put("schoolId", queryForm.getSchoolId());
		}

		return homeworkClazzRepo.find("$csFindClass", params).fetch(pageable);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map queryClassInfo(long classId) {
		List<Map> list = homeworkClazzRepo.find("$csQueryClassInfo", Params.param("classId", classId)).list(Map.class);
		return list.size() == 0 ? null : list.get(0);
	}

	@Override
	public Long countVip(long classId) {
		return homeworkClazzRepo.find("$csCountVip", Params.param("classId", classId).put("nowDate", new Date()))
				.get(Long.class);
	}

	@Override
	public Long countStu(long classId) {
		return homeworkClazzRepo.find("$csCountStu", Params.param("classId", classId)).get(Long.class);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> countStus(Collection<Long> classIds) {
		if (CollectionUtils.isEmpty(classIds)) {
			return Collections.EMPTY_LIST;
		}
		return homeworkClazzRepo.find("$csCountClassStus", Params.param("classIds", classIds)).list(Map.class);
	}

	@Override
	public List<Student> listAllStudents(long classId) {
		return homeworkStudentClazzRepo.find("$csListAllStudents", Params.param("classId", classId))
				.list(Student.class);
	}
}

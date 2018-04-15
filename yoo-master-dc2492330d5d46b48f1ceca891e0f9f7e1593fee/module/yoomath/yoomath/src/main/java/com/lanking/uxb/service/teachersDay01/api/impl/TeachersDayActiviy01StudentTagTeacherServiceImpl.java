package com.lanking.uxb.service.teachersDay01.api.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.teachersDay01.TeachersDayActiviy01StudentTagTeacher;
import com.lanking.cloud.ex.core.EntityException;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.teachersDay01.api.TeachersDayActiviy01StudentTagTeacherService;
import com.lanking.uxb.service.teachersDay01.api.TeachersDayActiviy01TeacherTagService;

@Service
@Transactional(readOnly = true)
public class TeachersDayActiviy01StudentTagTeacherServiceImpl implements TeachersDayActiviy01StudentTagTeacherService {

	@Autowired
	@Qualifier("TeachersDayActiviy01StudentTagTeacherRepo")
	private Repo<TeachersDayActiviy01StudentTagTeacher, Long> repo;

	@Autowired
	private TeachersDayActiviy01TeacherTagService tagService;

	@Transactional
	@Override
	public void setTag(List<Long> tagCodes, Long studentId, Long teacherId) {
		for (Long codeTag : tagCodes) {
			try {
				TeachersDayActiviy01StudentTagTeacher tag = new TeachersDayActiviy01StudentTagTeacher();
				tag.setStudentId(studentId);
				tag.setTagAt(new Date());
				tag.setTeacherId(teacherId);
				tag.setCodeTag(codeTag);
				repo.save(tag);
				tagService.save(teacherId, codeTag);
			} catch (EntityException e) {
			}
		}
	}

	@Override
	public List<Long> findTagList(Long studentId, Long teacherId) {
		return repo.find("$findTagList", Params.param("studentId", studentId).put("teacherId", teacherId))
				.list(Long.class);
	}

	@Override
	public long numberOfSetTag(Long teacherId) {
		return repo.find("$numberOfSetTag", Params.param("teacherId", teacherId)).count();
	}
}

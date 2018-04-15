package com.lanking.uxb.service.teachersDay01.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.teachersDay01.TeachersDayActiviy01TeacherTag;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.teachersDay01.api.TeachersDayActiviy01TeacherTagService;

@Transactional(readOnly = true)
@Service
public class TeachersDayActiviy01TeacherTagServiceImpl implements TeachersDayActiviy01TeacherTagService {

	@Autowired
	@Qualifier("TeachersDayActiviy01TeacherTagRepo")
	private Repo<TeachersDayActiviy01TeacherTag, Long> repo;

	@Override
	public List<TeachersDayActiviy01TeacherTag> getByTeacher(Long teacherId) {
		return repo.find("$findTagsByUserId", Params.param("teacherId", teacherId)).list();
	}

	@Transactional
	@Override
	public void save(Long teacherId, long tagCode) {
		TeachersDayActiviy01TeacherTag tag = repo.find("$getTagNum",
				Params.param("teacherId", teacherId).put("tagCode", tagCode)).get();
		if (tag == null) {
			tag = new TeachersDayActiviy01TeacherTag();
			tag.setNum(1);
		} else {
			tag.setNum(tag.getNum() + 1);
		}
		tag.setTeacherId(teacherId);
		tag.setCodeTag(tagCode);
		repo.save(tag);
	}
}

package com.lanking.cloud.job.nationalDayActivity.DAO.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01Homework;
import com.lanking.cloud.job.nationalDayActivity.DAO.NationalDayActivity01HomeworkDAO;
import com.lanking.cloud.sdk.data.Params;

import httl.util.CollectionUtils;

@Component("nda01NationalDayActivity01HomeworkDAO")
public class NationalDayActivity01HomeworkDAOImpl extends AbstractHibernateDAO<NationalDayActivity01Homework, Long>
		implements NationalDayActivity01HomeworkDAO {

	@Autowired
	@Qualifier("NationalDayActivity01HomeworkRepo")
	@Override
	public void setRepo(Repo<NationalDayActivity01Homework, Long> repo) {
		this.repo = repo;
	}

	@Override
	public void delete(long homeworkId) {
		repo.execute("$nda01Delete", Params.param("homeworkId", homeworkId));
	}

	@Override
	public NationalDayActivity01Homework create(long teacherId, long homeworkId) {

		NationalDayActivity01Homework activit01Homework = new NationalDayActivity01Homework();
		activit01Homework.setHomeworkId(homeworkId);
		activit01Homework.setTeacherId(teacherId);
		return repo.save(activit01Homework);
	}

	@Override
	public boolean hasNoHomework() {
		return repo.find("$nda01RandomGetOne").get() == null;
	}

	@Override
	public List<Long> getHomeworkTeacherIdByUsers(List<Long> userIds) {
		Params param = Params.param();
		param.put("userIds", userIds);

		return repo.find("$nda01findHomeworkTeacherIdByUsers", param).list(Long.class);
	}

	@Override
	public List<Long> getTeacherIdsSpecify(int startindex, int size) {
		Params param = Params.param();
		param.put("startindex", startindex);
		param.put("size", size);

		return repo.find("$nda01findTeacherIdsSpecify", param).list(Long.class);
	}

	@Override
	public Map<Long, List<NationalDayActivity01Homework>> getHomeworkByUsers(List<Long> userIds) {
		Params param = Params.param();
		param.put("teacherIds", userIds);
		
		Map<Long, List<NationalDayActivity01Homework>> data = new HashMap<>();
		List<NationalDayActivity01Homework> homeworkList = repo.find("$nda01findHomeworkByUsers", param).list();
		if (CollectionUtils.isEmpty(homeworkList)) {
			return data;
		}
		
		for (NationalDayActivity01Homework value : homeworkList) {
			if (data.containsKey(value.getTeacherId())) {
				List<NationalDayActivity01Homework> hs = data.get(value.getTeacherId());
				hs.add(value);
				data.put(value.getTeacherId(), hs);
			} else {
				List<NationalDayActivity01Homework> hs = Lists.newArrayList();
				hs.add(value);
				data.put(value.getTeacherId(), hs);
			}
		}

		return data;
	}

}

package com.lanking.uxb.service.zuoye.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.stat.StudentExerciseSection;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.zuoye.api.ZyStudentExerciseSectionService;

/**
 * 学生章节掌握情况service
 *
 * @author xinyu.zhou
 * @since yoomath V1.6
 */
@Service
@Transactional(readOnly = true)
public class ZyStudentExerciseSectionServiceImpl implements ZyStudentExerciseSectionService {
	@Autowired
	@Qualifier("StudentExerciseSectionRepo")
	private Repo<StudentExerciseSection, Long> repo;

	@Override
	public StudentExerciseSection get(long id) {
		return repo.get(id);
	}

	@Override
	public StudentExerciseSection getBySection(long userId, long sectionCode) {
		return repo.find("$getBySection", Params.param("userId", userId).put("sectionCode", sectionCode)).get();
	}

	@Override
	public Map<Long, StudentExerciseSection> mgetBySection(long userId, Collection<Long> sectionCodes, Long lastMonth) {
		Params p = Params.param();
		if (lastMonth != null) {
			p.put("lastMonth", lastMonth);
		}
		p.put("userId", userId);
		p.put("sectionCodes", sectionCodes);
		List<StudentExerciseSection> list = repo
				.find("$getBySections", Params.param("userId", userId).put("sectionCodes", sectionCodes)).list();
		Map<Long, StudentExerciseSection> map = Maps.newHashMap();
		for (StudentExerciseSection s : list) {
			map.put(s.getSectionCode(), s);
		}
		return map;
	}

	@Override
	public List<StudentExerciseSection> findByClassIdAndSectionCode(long classId, long sectionCode, Long lastMonth) {
		Params params = Params.param("sectionCode", sectionCode + "%");
		params.put("classId", classId);
		params.put("lastMonth", lastMonth);
		return repo.find("$findByClassIdAndSectionCode", params).list();
	}

}

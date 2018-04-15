package com.lanking.uxb.zycon.base.api.impl;

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
import com.lanking.uxb.zycon.base.api.ZycStudentExerciseSectionService;

/**
 * 学生章节掌握情况service
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2016年1月8日 下午3:25:26
 */
@Service
@Transactional(readOnly = true)
public class ZycStudentExerciseSectionServiceImpl implements ZycStudentExerciseSectionService {

	@Autowired
	@Qualifier("StudentExerciseSectionRepo")
	private Repo<StudentExerciseSection, Long> repo;

	@Override
	public StudentExerciseSection get(long id) {
		return repo.get(id);
	}

	@Override
	public StudentExerciseSection getBySection(long userId, long sectionCode) {
		return repo.find("$zycGetBySection", Params.param("userId", userId).put("sectionCode", sectionCode)).get();
	}

	@Override
	public Map<Long, StudentExerciseSection> mgetBySection(long userId, Collection<Long> sectionCodes, Long lastMonth) {
		Params p = Params.param();
		if (lastMonth != null) {
			p.put("lastMonth", lastMonth);
		}
		p.put("userId", userId);
		p.put("sectionCodes", sectionCodes);
		List<StudentExerciseSection> list = repo.find("$zycGetBySections",
				Params.param("userId", userId).put("sectionCodes", sectionCodes)).list();
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
		List<StudentExerciseSection> list = repo.find("$zycFindByClassIdAndSectionCode", params).list();
		if (list.size() == 0) {
			params.remove("lastMonth");
			params.put("curMonth", lastMonth);
			list = repo.find("$zycFindByClassIdAndSectionCode", params).list();
		}
		return list;
	}

}

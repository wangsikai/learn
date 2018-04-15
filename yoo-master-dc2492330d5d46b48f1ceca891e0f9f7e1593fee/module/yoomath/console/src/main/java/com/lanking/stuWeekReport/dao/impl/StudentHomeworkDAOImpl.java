package com.lanking.stuWeekReport.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;

@Component(value = "stuWeekReportStudentHomeworkDAO")
public class StudentHomeworkDAOImpl extends AbstractHibernateDAO<StudentHomework, Long> implements com.lanking.stuWeekReport.dao.StudentHomeworkDAO {

	@Autowired
	@Qualifier("StudentHomeworkRepo")
	@Override
	public void setRepo(Repo<StudentHomework, Long> repo) {
		this.repo = repo;
	}

	@Override
	public List<StudentHomework> findByUserId(Long userId, String startDate, String endDate) {
		Params params = Params.param("userId", userId);
		params.put("startDate", startDate);
		params.put("endDate", endDate + " 23:59:59");
		return repo.find("$findByUserId", params).list();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> getSectionAnalysis(Long studentId, Integer textbookCode, String startDate, String endDate) {
		Params params = Params.param("studentId", studentId);
		params.put("textbookCode", textbookCode + "%");
		params.put("startDate", startDate);
		params.put("endDate", endDate + " 23:59:59");
		List<Map> list = repo.find("$getSectionAnalysis", params).list(Map.class);
		Map<Long, Integer> rightMap = new HashMap<Long, Integer>();
		Map<Long, Integer> wrongMap = new HashMap<Long, Integer>();
		List<Long> sectionCodes = new ArrayList<Long>();
		for (Map map : list) {
			Integer result = Integer.parseInt(String.valueOf(map.get("result")));
			Long sectionCode = Long.parseLong(String.valueOf(map.get("section_code")));
			if (!sectionCodes.contains(sectionCode)) {
				sectionCodes.add(sectionCode);
			}
			if (result == HomeworkAnswerResult.RIGHT.getValue()) {
				if (rightMap.get(sectionCode) == null) {
					rightMap.put(sectionCode, 1);
				} else {
					rightMap.put(sectionCode, rightMap.get(sectionCode) + 1);
				}
			}
			if (result == HomeworkAnswerResult.WRONG.getValue()) {
				if (wrongMap.get(sectionCode) == null) {
					wrongMap.put(sectionCode, 1);
				} else {
					wrongMap.put(sectionCode, wrongMap.get(sectionCode) + 1);
				}
			}
		}
		List<Map> sList = new ArrayList<Map>();
		for (Long sectionCode : sectionCodes) {
			Map temp = new HashMap();
			temp.put("section_code", sectionCode);
			temp.put("wrong_count", wrongMap.get(sectionCode) == null ? 0 : wrongMap.get(sectionCode));
			temp.put("right_count", rightMap.get(sectionCode) == null ? 0 : rightMap.get(sectionCode));
			sList.add(temp);
		}
		return sList;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Object> getStatByUser(Long userId, String startDate, String endDate) {
		List<Map> list = repo.find("$getStatByUser",
				Params.param("userId", userId).put("startDate", startDate).put("endDate", endDate + " 23:59:59")).list(
				Map.class);
		if (CollectionUtils.isEmpty(list)) {
			return new HashMap<String, Object>();
		}
		return list.get(0);
	}

	@Override
	public List<Map> stuClassWeekReportList(Long classId, String startDate, String endDate) {
		return repo.find("$stuClassWeekReportList",
				Params.param("classId", classId).put("startDate", startDate).put("endDate", endDate + " 23:59:59"))
				.list(Map.class);
	}
}

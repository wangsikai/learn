package com.lanking.cloud.job.paperReport.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoomath.stat.StudentPaperReport;
import com.lanking.cloud.job.paperReport.dao.StudentPaperReportDAO;
import com.lanking.cloud.sdk.data.Params;

@Component
public class StudentPaperReportDAOImpl extends AbstractHibernateDAO<StudentPaperReport, Long> implements
		StudentPaperReportDAO {

	@Autowired
	@Qualifier("StudentPaperReportRepo")
	@Override
	public void setRepo(Repo<StudentPaperReport, Long> repo) {
		this.repo = repo;
	}

	@Override
	public List<Map> getSectionMapByClassId(Long classId, Date startDate, Date endDate, Integer textbookCode) {
		Params params = Params.param("classId", classId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("textbookCode", textbookCode + "%");
		return repo.find("$getSectionMapByClassId", params).list(Map.class);
	}

	@Override
	public List<Map> getSectionMapByStuId(Long studentId, Date startDate, Date endDate, Integer textbookCode) {
		Params params = Params.param("studentId", studentId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("textbookCode", textbookCode + "%");
		List<Map> list = repo.find("$getSectionMapByStuId", params).list(Map.class);
		return list;
	}

	@Override
	public List<Map> getStuStat(Long classId, Date startDate, Date endDate) {
		Params params = Params.param("classId", classId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return repo.find("$getStuStat", params).list(Map.class);
	}

	@Override
	public Map<String, Object> getClazzStat(Long classId, Date startDate, Date endDate) {
		Params params = Params.param("classId", classId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return repo.find("$getClazzStat", params).get(Map.class);
	}

	@Override
	public List<Map> getSmallSectionMapByStuId(Long studentId, Date startDate, Date endDate, Integer textbookCode) {
		Params params = Params.param("studentId", studentId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("textbookCode", textbookCode + "%");
		return repo.find("$getSmallSectionMapByStuId", params).list(Map.class);
	}

	@Override
	public List<StudentPaperReport> findByRecord(long recordId) {
		Params params = Params.param("recordId", recordId);
		return repo.find("$findByRecord", params).list();
	}

	@Override
	public void deleteReport(Long classId, Date startDate, Date endDate) {
		Params params = Params.param("classId", classId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		repo.execute("$deleteReport", params);
	}

	@Override
	public Map findClazzHkMap(Long classId, Date startDate, Date endDate) {
		Params params = Params.param("classId", classId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return repo.find("$findClazzHkList", params).get(Map.class);
	}

	@Override
	public Map findStuHkMap(Long classId, Long studentId, Date startDate, Date endDate) {
		Params params = Params.param("classId", classId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("studentId", studentId);
		return repo.find("$findStuHkMap", params).get(Map.class);
	}

	@Override
	public List<Map> findLastHkList(Long classId, Long studentId, Date startDate, Date endDate) {
		Params params = Params.param("classId", classId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("studentId", studentId);
		return repo.find("$findLastHkList", params).list(Map.class);
	}

	@Override
	public List<Map> getKpMapByClassId(Long classId, Date startDate, Date endDate, Integer textbookCode) {
		Params params = Params.param("classId", classId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("textbookCode", textbookCode + "%");
		return repo.find("$getKpMapByClassId", params).list(Map.class);
	}

	@Override
	public Long clazzSelfCount(Long classId, Date startDate, Date endDate) {
		Params params = Params.param("classId", classId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return repo.find("$clazzSelfCount", params).get(Long.class);
	}

	@Override
	public Long stuSelfCount(Long studentId, Date startDate, Date endDate) {
		Params params = Params.param("studentId", studentId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return repo.find("$stuSelfCount", params).get(Long.class);
	}

	@Override
	public Map findClazzHkMap2(Long classId, Date startDate, Date endDate) {
		Params params = Params.param("classId", classId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return repo.find("$findClazzHkMap2", params).get(Map.class);
	}

	@Override
	public Map findStuHkMap2(Long classId, Long studentId, Date startDate, Date endDate) {
		Params params = Params.param("classId", classId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("studentId", studentId);
		return repo.find("$findStuHkMap2", params).get(Map.class);
	}

}

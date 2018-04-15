package com.lanking.uxb.service.report.api.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.stat.StudentStatisticsReport;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.report.api.StudentStatisticsReportService;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.api.ZyStudentExerciseSectionService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkService;
import com.lanking.uxb.service.zuoye.convert.ZyStudentExerciseSectionConvert;
import com.lanking.uxb.service.zuoye.value.VStudentExerciseSection;

@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
@Service
public class StudentStatisticsReportServiceImpl implements StudentStatisticsReportService {

	@Autowired
	private ZyStudentHomeworkService studenthomeworkService;
	@Autowired
	private ZyHomeworkStudentClazzService stuClazzService;
	@Autowired
	private ZyStudentExerciseSectionService studentExerciseSectionService;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private ZyStudentExerciseSectionConvert studentExerciseSectionConvert;
	@Autowired
	private ZyHomeworkClassService homeworkClazzService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private HomeworkService homeworkService;

	@Autowired
	@Qualifier("StudentStatisticsReportRepo")
	private Repo<StudentStatisticsReport, Long> stuStatRepo;

	List<Map<String, Object>> getAnalysisMaps(List<VStudentExerciseSection> studentExerciseSections,
			List<Map<String, Object>> knowpointAnalysisMaps) {
		for (VStudentExerciseSection v : studentExerciseSections) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("complete", v.getComplete());
			map.put("sectionName", v.getSection().getName());
			map.put("level", v.getSection().getLevel());
			map.put("code", v.getSection().getCode());
			map.put("pcode", v.getSection().getPcode());
			knowpointAnalysisMaps.add(map);
			if (v.getChildren().size() > 0) {
				this.getAnalysisMaps(v.getChildren(), knowpointAnalysisMaps);
			}
		}
		return knowpointAnalysisMaps;
	}

	@Override
	public List<StudentStatisticsReport> getStuStatReports(int subjectCode, List<Long> stuIds, int year, int month) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M");
		try {
			return stuStatRepo.find(
					"$query",
					Params.param("subjectCode", subjectCode).put("stuIds", stuIds)
							.put("calDate", format.parseObject(year + "-" + month))).list();
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public StudentStatisticsReport getStudentReport(long studentId, long classId, int year, int month) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M");
		try {
			List<StudentStatisticsReport> list = stuStatRepo.find(
					"$query",
					Params.param("classId", classId).put("stuIds", Lists.newArrayList(studentId))
							.put("calDate", format.parseObject(year + "-" + month))).list();
			return list.size() == 0 ? null : list.get(0);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public StudentStatisticsReport get(Long id) {
		return stuStatRepo.get(id);
	}

	@Override
	@Transactional
	public void buReport(Long reportId) {
		StudentStatisticsReport studentStatisticsReport = this.get(reportId);
		if (null != studentStatisticsReport) {
			studentStatisticsReport.setBuy(true);
			stuStatRepo.save(studentStatisticsReport);
		}
	}

	@Override
	public List<HomeworkClazz> getClazzByMinDate(Integer subjectCode, long studentId, Date minDate, Date maxDate,
			Boolean buy) {
		Params params = Params.param("studentId", studentId);
		if (null != minDate) {
			params.put("minDate", minDate);
		}
		if (null != maxDate) {
			params.put("maxDate", maxDate);
		}
		if (null != subjectCode) {
			params.put("subjectCode", subjectCode);
		}
		if (null != buy) {
			params.put("buy", buy);
		}
		List<Long> clazzIds = stuStatRepo.find("$getClazzByMinDate", params).list(Long.class);
		if (clazzIds.size() > 0) {
			Map<Long, HomeworkClazz> clazzMap = homeworkClazzService.mget(clazzIds);
			List<HomeworkClazz> clazzs = new ArrayList<HomeworkClazz>(clazzIds.size());
			for (Long clazzId : clazzIds) {
				HomeworkClazz clazz = clazzMap.get(clazzId);
				if (clazz.getStatus() == Status.ENABLED) {
					clazzs.add(clazz);
				}
			}
			return clazzs;
		} else {
			return Lists.newArrayList();
		}
	}

	@Override
	public List<String> getDatesByMinDate(Integer subjectCode, long studentId, Long clazzId, Date minDate,
			Date maxDate, Boolean buy) {
		Params params = Params.param("studentId", studentId);
		if (null != minDate) {
			params.put("minDate", minDate);
		}
		if (null != maxDate) {
			params.put("maxDate", maxDate);
		}
		if (null != subjectCode) {
			params.put("subjectCode", subjectCode);
		}
		if (clazzId != null) {
			params.put("clazzId", clazzId);
		}
		if (buy != null) {
			params.put("buy", buy);
		}
		return stuStatRepo.find("$getDatesByMinDate", params).list(String.class);
	}

	@Override
	public boolean existReport(int year, int month, long studentId) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M");
		try {
			return stuStatRepo.find("$existReport",
					Params.param("studentId", studentId).put("calDate", format.parseObject(year + "-" + month)))
					.count() > 0;
		} catch (ParseException e) {
		}
		return false;
	}
}

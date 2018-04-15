package com.lanking.uxb.service.report.api.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.stat.StudentStatisticsReport;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.report.api.TaskHomeworkClassService;
import com.lanking.uxb.service.report.api.TaskHomeworkService;
import com.lanking.uxb.service.report.api.TaskHomeworkStudentClazzService;
import com.lanking.uxb.service.report.api.TaskStudentHomeworkService;
import com.lanking.uxb.service.report.api.TaskStudentStatisticsReportService;
import com.lanking.uxb.service.report.api.TaskTeacherService;
import com.lanking.uxb.service.report.convert.TaskStudentExerciseSectionConvert;
import com.lanking.uxb.service.report.value.VStudentExerciseSection;

@Transactional(readOnly = true)
@Service
public class TaskStudentStatisticsReportServiceImpl implements TaskStudentStatisticsReportService {

	@Autowired
	private TaskStudentHomeworkService studenthomeworkService;
	@Autowired
	private TaskHomeworkStudentClazzService stuClazzService;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private TaskStudentExerciseSectionConvert studentExerciseSectionConvert;
	@Autowired
	private TaskHomeworkClassService homeworkClazzService;
	@Autowired
	private TaskTeacherService teacherService;
	@Autowired
	private TaskHomeworkService homeworkService;

	@Autowired
	@Qualifier("StudentStatisticsReportRepo")
	private Repo<StudentStatisticsReport, Long> stuStatRepo;

	@Transactional
	@Override
	public void create(int year, int month, long clazzId) {
		List<HomeworkStudentClazz> stuclazzList = stuClazzService.list(clazzId);
		if (stuclazzList.isEmpty()) {
			return;
		}
		List<Long> stuIds = Lists.newArrayList();
		for (HomeworkStudentClazz homeworkStudentClazz : stuclazzList) {
			stuIds.add(homeworkStudentClazz.getStudentId());
		}
		Map<Long, Integer> rankMap = studenthomeworkService.getStuStatRankByClass(year, month, clazzId, stuIds);
		Date date = new Date();
		HomeworkClazz homeworkClazz = homeworkClazzService.get(clazzId);
		if (homeworkClazz.getTeacherId() != null) {
			Teacher teacher = teacherService.getId(homeworkClazz.getTeacherId());
			Map<String, Long> reportStuMap = this.getReportStuMap(stuIds, year, month, teacher.getSubjectCode());
			for (Long stuId : stuIds) {
				if (reportStuMap.get(((Integer) year).toString() + ((Integer) month).toString() + stuId.toString()
						+ ((Long) clazzId).toString()) == null) {
					this.createStudentStat(stuId, clazzId, year, month, stuclazzList.size(), date, rankMap.get(stuId),
							teacher.getTextbookCode(), teacher.getSubjectCode());
				}
			}
		}
	}

	@Override
	public Map<String, Long> getReportStuMap(List<Long> stuIds, int year, int month, Integer subjectCode) {
		// key为年月+classId，value为classId
		Map<String, Long> reportClassMap = Maps.newHashMap();
		List<StudentStatisticsReport> statReport = this.getStuStatReports(subjectCode, stuIds, year, month);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M");
		for (StudentStatisticsReport r : statReport) {
			reportClassMap.put(format.format(r.getCalDate()) + r.getStudentId().toString() + r.getClassId().toString(),
					r.getClassId());
		}
		return reportClassMap;
	}

	@Override
	public StudentStatisticsReport createStudentStat(long stuId, long clazzId, int year, int month, int stuNum,
			Date date, Integer rank, long textbookCode, int subjectCode) {
		StudentStatisticsReport report = new StudentStatisticsReport();
		List<StudentHomework> stuHomeworkList = studenthomeworkService.listByTime(year, month, stuId, clazzId);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M");
		try {
			report.setCalDate(format.parse(year + "-" + month));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		report.setRank(rank);
		report.setStudentNum(stuNum);
		report.setClassId(clazzId);
		report.setStudentId(stuId);
		report.setHomeworkNum(stuHomeworkList.size());
		report.setCreateAt(date);
		int questionCount = 0;
		int doQuestionCount = 0;
		int rightRateCount = 0;
		double rightRateAll = 0.0;
		List<Map<String, Object>> rightRateMap = Lists.newArrayList();
		int finishCount = 0;
		for (StudentHomework studentHomework : stuHomeworkList) {
			Homework homework = homeworkService.get(studentHomework.getHomeworkId());
			int wrongCount = studentHomework.getWrongCount() == null ? 0 : studentHomework.getWrongCount();
			int rightCount = studentHomework.getRightCount() == null ? 0 : studentHomework.getRightCount();
			questionCount = questionCount + Integer.valueOf(homework.getQuestionCount().toString());
			if (studentHomework.getStuSubmitAt() != null) {
				doQuestionCount = doQuestionCount + wrongCount + rightCount;
				finishCount++;
			}
			Map<String, Object> map = Maps.newHashMap();
			map.put("homeworkId", studentHomework.getHomeworkId());
			map.put("rightCount", studentHomework.getRightCount() == null ? 0 : studentHomework.getRightCount());
			map.put("wrongCount", studentHomework.getWrongCount() == null ? 0 : studentHomework.getWrongCount());
			map.put("rightRate", studentHomework.getRightRate() == null ? -1 : studentHomework.getRightRate());
			map.put("createAt", studentHomework.getCreateAt().getTime());
			if (studentHomework.getRightRate() != null) {
				rightRateAll = rightRateAll + studentHomework.getRightRate().doubleValue();
				rightRateCount++;
			}
			rightRateMap.add(map);
		}
		report.setRightRate(BigDecimal.valueOf(Math.round(rightRateAll / rightRateCount)));
		if (stuHomeworkList.size() > 0) {
			report.setCompleteRate(BigDecimal.valueOf(Math.round(finishCount * 100 / stuHomeworkList.size())));
		} else {
			report.setCompleteRate(BigDecimal.valueOf(-1));
		}
		report.setQuestionNum(questionCount);
		report.setQuestionDoNum(doQuestionCount);
		report.setRightRateMaps(rightRateMap);
		report.setSubjectCode(subjectCode);
		List<VSection> sections = sectionConvert.to(sectionService.findByTextbookCode(Long.valueOf(textbookCode)
				.intValue()));
		List<VStudentExerciseSection> vs = studentExerciseSectionConvert.to(sections);
		if (CollectionUtils.isEmpty(vs)) {
			return null;
		}
		String monthTemp = null;
		if (month < 10) {
			monthTemp = "0" + ((Integer) month).toString();
		} else {
			monthTemp = ((Integer) month).toString();
		}
		Long lastMonth = Long.valueOf(((Integer) year).toString() + monthTemp);
		studentExerciseSectionConvert.statisticsBeforeAssembleTree(vs, stuId, lastMonth);
		vs = studentExerciseSectionConvert.assembleTree(vs);
		List<Map<String, Object>> knowpointAnalysisMaps = Lists.newArrayList();
		report.setKnowpointAnalysisMaps(this.getAnalysisMaps(vs, knowpointAnalysisMaps));
		return stuStatRepo.save(report);
	}

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

}

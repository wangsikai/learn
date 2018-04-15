package com.lanking.uxb.zycon.base.api.impl;

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
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.stat.ClassStatisticsReport;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.zycon.base.api.ZycClassStatisticsReportService;
import com.lanking.uxb.zycon.base.convert.ZycStudentExerciseSectionConvert;
import com.lanking.uxb.zycon.base.value.CStudentExerciseSection;
import com.lanking.uxb.zycon.homework.api.ZycHomeworkClazzService;
import com.lanking.uxb.zycon.homework.api.ZycHomeworkService;

@Transactional(readOnly = true)
@Service
public class ZycClassStatisticsReportServiceImpl implements ZycClassStatisticsReportService {

	@Autowired
	private ZycHomeworkClazzService classService;
	@Autowired
	private ZycHomeworkService homeworkService;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private ZycStudentExerciseSectionConvert studentExerciseSectionConvert;

	@Autowired
	@Qualifier("ClassStatisticsReportRepo")
	private Repo<ClassStatisticsReport, Long> classStatRepo;

	@Transactional
	@Override
	public void create(int year, int month, Teacher teacher) {
		long userId = teacher.getId();
		Date date = new Date();
		List<HomeworkClazz> clazzList = classService.listCurrentClazzs(userId);
		if (clazzList.isEmpty()) {
			return;
		}
		List<Long> classIds = Lists.newArrayList();
		for (HomeworkClazz clazz : clazzList) {
			classIds.add(clazz.getId());
		}
		Map<String, Long> reportClassMap = this.getReportClassMap(classIds, year, month, teacher.getSubjectCode());
		for (HomeworkClazz homeworkClazz : clazzList) {
			Integer textbookCode = null;
			String key = ((Integer) year).toString() + ((Integer) month).toString() + homeworkClazz.getId().toString();
			Homework homework = homeworkService.getFirstIssuedHomeworkInMonth(year, month, homeworkClazz.getId());
			if (homework != null) {
				textbookCode = homework.getTextbookCode();
			} else {
				if (teacher.getTextbookCode() != null) {
					textbookCode = teacher.getTextbookCode();
				}
			}
			if (reportClassMap.get(key) == null && homework != null) {
				this.createClazzStat(userId, homeworkClazz.getId(), year, month, homeworkClazz.getStudentNum(), date,
						textbookCode, teacher.getSubjectCode());
			}
		}
	}

	@Override
	public ClassStatisticsReport createClazzStat(long userId, long clazzId, int year, int month, int stuNum, Date date,
			Integer textbookCode, int subjectCode) {
		ClassStatisticsReport report = new ClassStatisticsReport();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M");
		try {
			report.setCalDate(format.parse(year + "-" + month));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		report.setSubjectCode(subjectCode);
		report.setClassId(clazzId);
		report.setTeacherId(userId);
		report.setStudentNum(stuNum);
		List<Homework> hkList = homeworkService.listByTime(userId, clazzId, year, month);
		report.setHomeworkNum(hkList.size());
		int questionNum = 0;
		int questionDoNum = 0;
		double rightRateAll = 0.0;
		int commitCount = 0;
		int distributeCount = 0;
		int hasRightRateCount = 0;
		List<Map<String, Object>> rightRateMap = Lists.newArrayList();
		for (Homework homework : hkList) {
			questionNum = questionNum + homework.getQuestionCount().intValue();
			int wrongCount = homework.getWrongCount() == null ? 0 : homework.getWrongCount();
			int rightCount = homework.getRightCount() == null ? 0 : homework.getRightCount();
			questionDoNum = questionDoNum + wrongCount + rightCount;
			commitCount = commitCount + homework.getCommitCount().intValue();
			distributeCount = distributeCount + homework.getDistributeCount().intValue();
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", homework.getId());
			map.put("name", homework.getName());
			map.put("rightCount", homework.getRightCount());
			map.put("questionCount", homework.getQuestionCount());
			map.put("rightRate", homework.getRightRate());
			map.put("createAt", homework.getCreateAt().getTime());
			if (homework.getRightRate() != null) {
				rightRateAll = rightRateAll + homework.getRightRate().doubleValue();
				hasRightRateCount++;
			}
			rightRateMap.add(map);
		}
		if (rightRateAll > 0.0) {
			report.setRightRate(BigDecimal.valueOf(Math.round(rightRateAll / hasRightRateCount)));
		} else {
			report.setRightRate(BigDecimal.valueOf(-1));
		}
		if (distributeCount != 0) {
			report.setCompleteRate(BigDecimal.valueOf(Math.round(commitCount * 100 / distributeCount)));
		} else {
			report.setCompleteRate(BigDecimal.valueOf(-1));
		}
		report.setQuestionNum(questionNum);
		report.setQuestionDoNum(questionDoNum);
		report.setCreateAt(date);
		report.setRightRateMaps(rightRateMap);
		if (textbookCode != null) {
			// 班级章节掌握情况
			List<Section> sectionList = sectionService.findByTextbookCode(textbookCode);
			List<VSection> sections = sectionConvert.to(sectionList);
			List<CStudentExerciseSection> studentExerciseSections = studentExerciseSectionConvert.to(sections);
			String monthTemp = null;
			if (month < 10) {
				monthTemp = "0" + ((Integer) month).toString();
			} else {
				monthTemp = ((Integer) month).toString();
			}
			Long lastMonth = Long.valueOf(((Integer) year).toString() + monthTemp);
			studentExerciseSections = studentExerciseSectionConvert.statisticsByClassId(studentExerciseSections,
					clazzId, lastMonth);
			studentExerciseSections = studentExerciseSectionConvert.assembleTree(studentExerciseSections);
			List<Map<String, Object>> knowpointAnalysisMaps = Lists.newArrayList();
			report.setKnowpointAnalysisMaps(this.getAnalysisMaps(studentExerciseSections, knowpointAnalysisMaps));
		} else {
			report.setKnowpointAnalysisMaps(null);
		}
		return classStatRepo.save(report);
	}

	List<Map<String, Object>> getAnalysisMaps(List<CStudentExerciseSection> studentExerciseSections,
			List<Map<String, Object>> knowpointAnalysisMaps) {
		for (CStudentExerciseSection v : studentExerciseSections) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("complete", Math.round(v.getComplete()));
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
	public ClassStatisticsReport getClassReport(int subjectCode, long classId, int year, int month) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M");
		try {
			List<ClassStatisticsReport> list = classStatRepo.find(
					"$query",
					Params.param("subjectCode", subjectCode).put("classId", classId)
							.put("calDate", format.parseObject(year + "-" + month))).list();
			return list.size() == 0 ? null : list.get(0);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<ClassStatisticsReport> getClassReports(int subjectCode, List<Long> classIds, int year, int month) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M");
		try {
			return classStatRepo.find(
					"$query",
					Params.param("subjectCode", subjectCode).put("classIds", classIds)
							.put("calDate", format.parseObject(year + "-" + month))).list();
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Map<String, Long> getReportClassMap(List<Long> classIds, int year, int month, int subjectCode) {
		// key为年月+classId，value为classId
		Map<String, Long> reportClassMap = Maps.newHashMap();
		List<ClassStatisticsReport> statReport = this.getClassReports(subjectCode, classIds, year, month);
		SimpleDateFormat format = new SimpleDateFormat("yyyyM");
		for (ClassStatisticsReport r : statReport) {
			reportClassMap.put(format.format(r.getCalDate()) + r.getClassId().toString(), r.getClassId());
		}
		return reportClassMap;
	}
}

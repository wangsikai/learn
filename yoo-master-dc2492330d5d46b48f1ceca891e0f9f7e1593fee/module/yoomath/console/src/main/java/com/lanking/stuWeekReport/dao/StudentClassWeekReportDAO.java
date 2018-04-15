package com.lanking.stuWeekReport.dao;

import java.util.List;

import com.lanking.cloud.domain.yoomath.stat.StudentClassWeekReport;

public interface StudentClassWeekReportDAO {

	StudentClassWeekReport getByUser(Long userId, Long classId, String startDate, String endDate);

	StudentClassWeekReport save(StudentClassWeekReport report);

	List<StudentClassWeekReport> findList(List<Long> classIds, String startDate, String endDate);

}

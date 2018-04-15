package com.lanking.uxb.service.diagnostic.api.teacher.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassStudent;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.diagnostic.api.StaticHomeworkClassService;
import com.lanking.uxb.service.diagnostic.api.teacher.TaskDiagnosticClassStudentService;

/**
 * 班级-学生维度统计服务实现.
 * 
 * @author wlche
 *
 */
@Service
@Transactional(readOnly = true)
public class TaskDiagnosticClassStudentServiceImpl implements TaskDiagnosticClassStudentService {

	@Autowired
	@Qualifier("StudentHomeworkRepo")
	private Repo<StudentHomework, Long> stuHkRepo;

	@Autowired
	@Qualifier("DiagnosticClassStudentRepo")
	private Repo<DiagnosticClassStudent, Long> diaClassStuRepo;

	@Autowired
	private StaticHomeworkClassService homeworkClassService;

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public void doClassStudentRankStat(Long classId, Date date) throws ParseException {
		Date nowDate = date == null ? new Date() : date;

		Date lastWeek = DateUtils.addDays(nowDate, -7);
		Date lastMonth = DateUtils.addDays(nowDate, -30);
		Date last3Month = DateUtils.addDays(nowDate, -30 * 3);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

		// 获取班级内的所有学生作业
		List<Map> allHks = stuHkRepo
				.find("$ymFindAll",
						Params.param("classId", classId).put("now", format.format(DateUtils.addDays(nowDate, 1))))
				.list(Map.class);

		Map<Long, List<BigDecimal>> weekRightRate = Maps.newHashMap();
		Map<Long, List<BigDecimal>> monthRightRate = Maps.newHashMap();
		Map<Long, List<BigDecimal>> month3RightRate = Maps.newHashMap();
		Map<Long, List<BigDecimal>> allRightRate = Maps.newHashMap();

		for (Map stuHk : allHks) {
			BigDecimal rightRate = (BigDecimal) stuHk.get("right_rate");
			Date startTime = (Date) stuHk.get("start_time");
			Long studentId = Long.parseLong(stuHk.get("student_id").toString());
			if (rightRate != null && rightRate.doubleValue() >= 0) {
				// 是本周作业
				if (startTime.after(format.parse(format.format(lastWeek)))) {
					List<BigDecimal> weekRightRates = weekRightRate.get(studentId);
					if (weekRightRates == null) {
						weekRightRates = Lists.newArrayList();
					}
					weekRightRates.add(rightRate);
					weekRightRate.put(studentId, weekRightRates);
				}

				if (startTime.after(format.parse(format.format(lastMonth)))) {
					List<BigDecimal> monthRightRates = monthRightRate.get(studentId);
					if (monthRightRates == null) {
						monthRightRates = Lists.newArrayList();
					}

					monthRightRates.add(rightRate);
					monthRightRate.put(studentId, monthRightRates);
				}

				if (startTime.after(format.parse(format.format(last3Month)))) {

					List<BigDecimal> month3RightRates = month3RightRate.get(studentId);
					if (month3RightRates == null) {
						month3RightRates = Lists.newArrayList();
					}

					month3RightRates.add(rightRate);
					month3RightRate.put(studentId, month3RightRates);
				}

				List<BigDecimal> allRightRates = allRightRate.get(studentId);

				if (allRightRates == null) {
					allRightRates = Lists.newArrayList();
				}

				allRightRates.add(rightRate);
				allRightRate.put(studentId, allRightRates);
			}
		}

		// 统计一周的数据
		List<Map> weekRanks = stuHkRepo.find("$ymRankStudent", Params.param("classId", classId)
				.put("startAt", format.format(lastWeek)).put("endAt", format.format(nowDate))).list(Map.class);

		List<Map> monthRanks = stuHkRepo.find("$ymRankStudent", Params.param("classId", classId)
				.put("startAt", format.format(lastMonth)).put("endAt", format.format(nowDate))).list(Map.class);

		List<Map> month3Ranks = stuHkRepo.find("$ymRankStudent", Params.param("classId", classId)
				.put("startAt", format.format(last3Month)).put("endAt", format.format(nowDate))).list(Map.class);

		List<Map> all = stuHkRepo.find("$ymRankStudent", Params.param("classId", classId)).list(Map.class);

		doRank(7, weekRanks, classId, weekRightRate, nowDate);
		doRank(30, monthRanks, classId, monthRightRate, nowDate);
		doRank(0, all, classId, allRightRate, nowDate);
		doRank(90, month3Ranks, classId, month3RightRate, nowDate);
	}

	/**
	 * 排名处理.
	 * 
	 * @param day0
	 * @param datas
	 * @param classId
	 * @param rightRate
	 * @param date
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void doRank(int day0, List<Map> datas, long classId, Map<Long, List<BigDecimal>> rightRate, Date date) {
		int rank = 1;
		int sameRank = 1;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		Long yesterDayStat = Long.valueOf(simpleDateFormat.format(DateUtils.addDays(date, -2)));
		BigDecimal rankRightRate = null;
		List<Long> studentIds = homeworkClassService.findStudentIds(classId);

		// 统计日期
		long statisticAt = Long.valueOf(simpleDateFormat.format(DateUtils.addDays(date, -1)));

		diaClassStuRepo.execute("$ymDelete",
				Params.param("classId", classId).put("day0", day0).put("statisticAt", statisticAt));

		if (CollectionUtils.isEmpty(datas)) {
			for (Long s : studentIds) {
				DiagnosticClassStudent newData = new DiagnosticClassStudent();
				newData.setClassId(classId);
				newData.setCreateAt(date);
				newData.setDay0(day0);
				newData.setHomeworkCount(0);
				newData.setRightRate(null);
				newData.setStatisticAt(statisticAt);
				newData.setUserId(s);
				newData.setFloatRank(0);
				newData.setStatus(Status.ENABLED);

				diaClassStuRepo.save(newData);
			}
		} else {
			Long lastStatisticAt = Long.valueOf(simpleDateFormat.format(DateUtils.addDays(date, -(day0 + 1))));

			List<DiagnosticClassStudent> diagnosticClassStudents = diaClassStuRepo
					.find("$ymQueryByStudents", Params.param("classId", classId).put("day0", day0)
							.put("userIds", studentIds).put("statAt", lastStatisticAt))
					.list();
			Map<Long, DiagnosticClassStudent> dayDiaStuMap = null;
			if (CollectionUtils.isEmpty(diagnosticClassStudents)) {
				dayDiaStuMap = Collections.EMPTY_MAP;
			} else {
				dayDiaStuMap = new HashMap<Long, DiagnosticClassStudent>(diagnosticClassStudents.size());
				for (DiagnosticClassStudent s : diagnosticClassStudents) {
					dayDiaStuMap.put(s.getUserId(), s);
				}
			}
			for (Map m : datas) {
				Long studentId = Long.parseLong(m.get("student_id").toString());
				DiagnosticClassStudent classStu = dayDiaStuMap.get(studentId);

				BigDecimal halfWrongCount = (BigDecimal) m.get("half_wrong_count");
				BigDecimal rightCount = (BigDecimal) m.get("right_count");
				BigDecimal wrongCount = (BigDecimal) m.get("wrong_count");

				int homeworkCount = (halfWrongCount == null ? 0 : halfWrongCount.intValue())
						+ (rightCount == null ? 0 : rightCount.intValue())
						+ (wrongCount == null ? 0 : wrongCount.intValue());

				DiagnosticClassStudent newData = new DiagnosticClassStudent();
				newData.setClassId(classId);
				newData.setCreateAt(date);
				newData.setDay0(day0);
				newData.setHomeworkCount(homeworkCount);
				newData.setRightRate(m.get("right_rate") == null ? null
						: ((BigDecimal) m.get("right_rate")).setScale(0, RoundingMode.HALF_UP));
				newData.setStatisticAt(statisticAt);
				newData.setUserId(studentId);
				newData.setRightRates(rightRate.get(studentId));

				if (rankRightRate == null) {
					newData.setRank(rank);
					rankRightRate = newData.getRightRate();
					sameRank = rank;
				} else {
					if (newData.getRightRate() != null) {
						if (rankRightRate.compareTo(newData.getRightRate()) != 0) {
							rankRightRate = newData.getRightRate();
							rank++;
							sameRank = rank;
							newData.setRank(rank);
						} else if (rankRightRate.compareTo(newData.getRightRate()) == 0) {
							rank++;
							newData.setRank(sameRank);
						}
					}
				}

				if (classStu == null) {
					newData.setFloatRank(0);
				} else {
					if (classStu.getRank() == null) {
						newData.setFloatRank(0);
					} else if (newData.getRank() != null && classStu.getRank() != null) {
						newData.setFloatRank(classStu.getRank() - rank);
					} else {
						newData.setFloatRank(0);
					}

				}

				newData.setStatus(Status.ENABLED);
				diaClassStuRepo.save(newData);
			}
		}

		diaClassStuRepo.execute("$ymUpdate", Params.param("classId", classId).put("yesterday", yesterDayStat));

	}
}

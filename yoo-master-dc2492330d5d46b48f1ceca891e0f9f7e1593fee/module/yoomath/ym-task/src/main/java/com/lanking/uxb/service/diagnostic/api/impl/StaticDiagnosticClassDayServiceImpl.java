package com.lanking.uxb.service.diagnostic.api.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.diagnostic.Diagnostic;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClass;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassKnowpoint;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassStudent;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassTopnKnowpoint;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassKnowpoint;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.diagnostic.api.StaticDiagnosticClassDayService;
import com.lanking.uxb.service.diagnostic.api.StaticHomeworkClassService;
import com.lanking.uxb.service.diagnostic.form.StaticClassQuestionDifficultyForm;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
@Service
@Transactional
public class StaticDiagnosticClassDayServiceImpl implements StaticDiagnosticClassDayService {
	private static final Logger logger = LoggerFactory.getLogger(StaticDiagnosticClassDayServiceImpl.class);

	@Autowired
	@Qualifier("DiagnosticClassStudentRepo")
	private Repo<DiagnosticClassStudent, Long> diaClassStuRepo;

	@Autowired
	@Qualifier("DiagnosticStudentClassKnowpointRepo")
	private Repo<DiagnosticStudentClassKnowpoint, Long> stuClassKpRepo;

	@Autowired
	@Qualifier("DiagnosticClassTopnKnowpointRepo")
	private Repo<DiagnosticClassTopnKnowpoint, Long> stuTopnKpRepo;

	@Autowired
	@Qualifier("DiagnosticClassKnowpointRepo")
	private Repo<DiagnosticClassKnowpoint, Long> diaClassKpRepo;

	@Autowired
	@Qualifier("DiagnosticClassRepo")
	private Repo<DiagnosticClass, Long> diaClassRepo;

	@Autowired
	@Qualifier("DiagnosticRepo")
	private Repo<Diagnostic, Long> diaRepo;

	@Autowired
	@Qualifier("StudentHomeworkRepo")
	private Repo<StudentHomework, Long> stuHkRepo;

	@Autowired
	private StaticHomeworkClassService homeworkClassService;

	@Override
	public void staticTopnStu(Collection<Long> classIds) {
		for (Long c : classIds) {
			List<Long> topStudentIds = diaClassStuRepo.find("$ymFindTopnStudent", Params.param("classId", c))
					.list(Long.class);

			if (CollectionUtils.isEmpty(topStudentIds)) {
				continue;
			}

			List<Map> topnRightRates = stuClassKpRepo
					.find("$ymTopnStu", Params.param("classId", c).put("studentIds", topStudentIds)).list(Map.class);

			for (Map m : topnRightRates) {
				Long code = Long.parseLong(m.get("knowpoint_code").toString());
				DiagnosticClassTopnKnowpoint kp = stuTopnKpRepo
						.find("$ymFindByClassAndKp", Params.param("classId", c).put("code", code)).get();

				if (kp == null) {
					kp = new DiagnosticClassTopnKnowpoint();
					kp.setCreateAt(new Date());
					kp.setKnowpointCode(code);
					kp.setClassId(c);
				}

				Long doCount = ((BigDecimal) m.get("do_count")).longValue();
				Long rightCount = ((BigDecimal) m.get("right_count")).longValue();

				BigDecimal rightRate = new BigDecimal(rightCount * 100d / doCount).setScale(0,
						BigDecimal.ROUND_HALF_UP);

				kp.setDoCount(doCount.intValue());
				kp.setRightCount(rightCount.intValue());
				kp.setRightRate(rightRate);

				stuTopnKpRepo.save(kp);
			}

		}
	}

	/**
	 * 处理当前班级作业知识点难度等数据
	 *
	 * @param homeworkClassId
	 *            班级id
	 * @param difficultMap
	 *            题目对应的难度列表
	 */
	@Override
	@Transactional
	public void doClassKpStat(Long homeworkClassId, Map<Long, List<StaticClassQuestionDifficultyForm>> difficultMap) {
		for (Map.Entry<Long, List<StaticClassQuestionDifficultyForm>> entry : difficultMap.entrySet()) {
			Long code = entry.getKey();
			DiagnosticClassKnowpoint diagnosticClassKnowpoint = diaClassKpRepo
					.find("$ymGetByClassAndKnowpoint", Params.param("classId", homeworkClassId).put("code", code))
					.get();

			Double minDifficulty = diagnosticClassKnowpoint == null ? 0d
					: diagnosticClassKnowpoint.getMinDifficulty().doubleValue();
			Double maxDifficulty = diagnosticClassKnowpoint == null ? 1d
					: diagnosticClassKnowpoint.getMaxDifficulty().doubleValue();
			int doHard1Count = 0, rightHard1Count = 0, doHard2Count = 0, rightHard2Count = 0, doHard3Count = 0,
					rightHard3Count = 0;
			BigDecimal rightRate = null;
			for (StaticClassQuestionDifficultyForm f : entry.getValue()) {

				if (f.getDifficulty() > minDifficulty) {
					minDifficulty = f.getDifficulty();
				}
				if (f.getDifficulty() < maxDifficulty) {
					maxDifficulty = f.getDifficulty();
				}
				switch (f.getType()) {
				case SIMPLE:
					doHard1Count += f.getDoCount();
					rightHard1Count += f.getRightCount();
					break;
				case MIDDLE:
					doHard2Count += f.getDoCount();
					rightHard2Count += f.getRightCount();
					break;
				case DIFFICULT:
					doHard3Count += f.getDoCount();
					rightHard3Count += f.getRightCount();
					break;
				}
			}

			if (diagnosticClassKnowpoint == null) {
				diagnosticClassKnowpoint = new DiagnosticClassKnowpoint();
				diagnosticClassKnowpoint.setKnowpointCode(code);
				diagnosticClassKnowpoint.setCreateAt(new Date());
				diagnosticClassKnowpoint.setClassId(homeworkClassId);
			} else {
				doHard1Count += diagnosticClassKnowpoint.getDoHard1Count();
				rightHard1Count += diagnosticClassKnowpoint.getRightHard1Count();

				doHard2Count += diagnosticClassKnowpoint.getDoHard2Count();
				rightHard2Count += diagnosticClassKnowpoint.getRightHard2Count();

				doHard3Count += diagnosticClassKnowpoint.getDoHard3Count();
				rightHard3Count += diagnosticClassKnowpoint.getRightHard3Count();

			}
			diagnosticClassKnowpoint.setDoHard1Count(doHard1Count);
			diagnosticClassKnowpoint.setRightHard1Count(rightHard1Count);
			diagnosticClassKnowpoint.setDoHard2Count(doHard2Count);
			diagnosticClassKnowpoint.setRightHard2Count(rightHard2Count);
			diagnosticClassKnowpoint.setDoHard3Count(doHard3Count);
			diagnosticClassKnowpoint.setRightHard3Count(rightHard3Count);

			diagnosticClassKnowpoint.setDoCount(doHard1Count + doHard2Count + doHard3Count);
			diagnosticClassKnowpoint.setRightCount(rightHard1Count + rightHard2Count + rightHard3Count);

			if (diagnosticClassKnowpoint.getDoCount() > 0) {
				rightRate = new BigDecimal(
						diagnosticClassKnowpoint.getRightCount() * 100d / diagnosticClassKnowpoint.getDoCount())
								.setScale(0, BigDecimal.ROUND_HALF_UP);
			}

			diagnosticClassKnowpoint.setRightRate(rightRate);
			diagnosticClassKnowpoint.setMaxDifficulty(new BigDecimal(maxDifficulty));
			diagnosticClassKnowpoint.setMinDifficulty(new BigDecimal(minDifficulty));
			diagnosticClassKnowpoint.setUpdateAt(new Date());

			diaClassKpRepo.save(diagnosticClassKnowpoint);
		}
	}

	@Override
	public void doDiagnostic() {
		List<Map> datas = diaClassRepo.find("$ymAvg", Params.param()).list(Map.class);

		for (Map m : datas) {
			Integer textbookCode = ((BigInteger) m.get("textbook_code")).intValue();

			Long doCountMonth = ((BigDecimal) m.get("do_count_month")).longValue();
			Long rightCountMonth = ((BigDecimal) m.get("right_count_month")).longValue();

			Diagnostic diagnostic = diaRepo.find("$ymGet", Params.param("textbookCode", textbookCode)).get();
			if (diagnostic == null) {
				diagnostic = new Diagnostic();
				diagnostic.setCreateAt(new Date());
				diagnostic.setTextbookCode(textbookCode);
			}

			diagnostic.setClassMonthDoCount(doCountMonth.intValue());
			diagnostic.setClassMonthRightCount(rightCountMonth.intValue());
			diagnostic.setUpdateAt(new Date());

			diaRepo.save(diagnostic);
		}
	}

	/**
	 * 处理此次作业学生的排名数据
	 *
	 * @param classId
	 *            班级id
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public void doClassStudentRankStat(Long classId, Date date) throws ParseException {
		Date nowDate = date;
		if (nowDate == null) {
			nowDate = new Date();
		}
		Date lastWeek = DateUtils.addDays(nowDate, -7);
		Date lastMonth = DateUtils.addDays(nowDate, -30);
		Date last3Month = DateUtils.addDays(nowDate, -30 * 3);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

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
			Long studentId = ((BigInteger) stuHk.get("student_id")).longValue();
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

	@Override
	public boolean hasClassKpData() {
		Long num = diaClassKpRepo.find("$ymHasData", Params.param()).count();
		return num != null && num > 0;
	}

	@Transactional
	private void doRank(int day0, List<Map> datas, long classId, Map<Long, List<BigDecimal>> rightRate, Date date) {
		int rank = 1;
		int sameRank = 1;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		Long yesterDayStat = Long.valueOf(simpleDateFormat.format(DateUtils.addDays(date, -2)));
		BigDecimal rankRightRate = null;
		List<Long> studentIds = homeworkClassService.findStudentIds(classId);
		if (CollectionUtils.isEmpty(datas)) {
			for (Long s : studentIds) {
				DiagnosticClassStudent newData = new DiagnosticClassStudent();
				newData.setClassId(classId);
				newData.setCreateAt(date);
				newData.setDay0(day0);
				newData.setHomeworkCount(0);
				newData.setRightRate(null);
				newData.setStatisticAt(Long.valueOf(simpleDateFormat.format(DateUtils.addDays(date, -1))));
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
				Long studentId = ((BigInteger) m.get("student_id")).longValue();
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
				newData.setStatisticAt(Long.valueOf(simpleDateFormat.format(DateUtils.addDays(date, -1))));
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

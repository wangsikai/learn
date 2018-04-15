package com.lanking.uxb.service.report.api.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.stat.ClassStatisticsReport;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.convert.KnowledgePointConvert;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.code.value.VKnowledgePoint;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.report.api.TaskClassReportBaseService;
import com.lanking.uxb.service.report.api.TaskClassStatisticsReportService;

@Transactional(readOnly = true)
@Service
public class TaskClassStatisticsReportServiceImpl implements TaskClassStatisticsReportService {

	@Autowired
	private TaskClassReportBaseService baseService;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private KnowledgePointConvert kpConvert;

	@Autowired
	@Qualifier("ClassStatisticsReportRepo")
	private Repo<ClassStatisticsReport, Long> classStatRepo;

	@Autowired
	@Qualifier("TeacherRepo")
	private Repo<Teacher, Long> teacherRepo;

	private static final int VERSION = 2;

	@Transactional
	@Override
	public void create(int year, int month, Teacher teacher) {
		long userId = teacher.getId();
		Date date = new Date();
		// 获取老师对应的班级
		List<HomeworkClazz> clazzList = baseService.listCurrentClazzs(userId);
		if (clazzList.isEmpty()) {
			return;
		}
		List<Long> classIds = Lists.newArrayList();
		for (HomeworkClazz clazz : clazzList) {
			classIds.add(clazz.getId());
		}
		// 获取是否已经统计过该月数据
		Map<String, Long> reportClassMap = this.getReportClassMap(classIds, year, month, teacher.getSubjectCode());
		for (HomeworkClazz homeworkClazz : clazzList) {
			Integer textbookCode = null;
			String key = ((Integer) year).toString() + ((Integer) month).toString() + homeworkClazz.getId().toString();
			Homework homework = baseService.getFirstIssuedHomeworkInMonth(year, month, homeworkClazz.getId());
			if (homework != null) {
				textbookCode = homework.getTextbookCode();
			}
			if (textbookCode == null && teacher.getTextbookCode() != null) {
				textbookCode = teacher.getTextbookCode();
			}
			if (teacher.getSubjectCode() == null) {
				return;
			}
			// 如果该月该班级数据已经统计，不计算
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
		// 查询老师某个班级某一个月的作业情况
		List<Homework> hkList = baseService.listByTime(userId, clazzId, year, month);
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
			map.put("rightRate", homework.getRightRate() == null ? -1 : homework.getRightRate());
			map.put("startAt", homework.getStartTime().getTime());
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
		report.setVersion(VERSION);

		if (textbookCode != null) {
			// sectionAnalysis 章节学情分析集合
			report.setSectionAnalysis(JSON.toJSONString(this.getSectionAnalysis(clazzId, textbookCode)));
			// knowpointAnalysis 知识点学情分析集合
			report.setKnowpointAnalysis(JSON.toJSONString(this.getKnowpointAnalysis(clazzId, textbookCode)));
		} else {
			report.setSectionAnalysis(null);
			report.setKnowpointAnalysisMaps(null);
		}
		// studentRightRates 学生作业正确率的平均数据
		report.setStudentRightRates(JSON.toJSONString(this.getStudentRightRates(clazzId)));
		return classStatRepo.save(report);

	}

	/**
	 * 获取班级对应的学生平均正确率情况
	 * 
	 * @param clazzId
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> getStudentRightRates(Long clazzId) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		List<Map> stuRightMaps = baseService.findStuAvgRightRateList(clazzId, cal.getTime(), new Date());
		this.dealListRank(stuRightMaps);
		// 正确率高的在前面
		Calendar preCal = Calendar.getInstance();
		preCal.add(Calendar.MONTH, -2);
		List<Map> preStuRightMaps = baseService.findStuAvgRightRateList(clazzId, preCal.getTime(), cal.getTime());
		this.dealListRank(preStuRightMaps);
		Map<String, Object> studentRightRates = new HashMap<String, Object>();
		// 上一个月学生和排名对应关系
		Map<Long, Integer> preStuMap = new HashMap<Long, Integer>();
		for (Map map : preStuRightMaps) {
			Long studentId = Long.parseLong(String.valueOf(map.get("studentid")));
			Integer rank = Integer.parseInt(String.valueOf(map.get("rank")));
			preStuMap.put(studentId, rank);
		}
		if (CollectionUtils.isNotEmpty(stuRightMaps)) {
			// 获取平均正确率最大的
			Map<String, Object> maxStuMap = stuRightMaps.get(0);
			Map<String, Object> maxRightRate = new HashMap<String, Object>();
			maxRightRate.put("studentId", String.valueOf(maxStuMap.get("studentid")));
			maxRightRate.put("rightRate", Integer.parseInt(maxStuMap.get("avgright").toString()));
			Map<String, Object> minStuMap = stuRightMaps.get(stuRightMaps.size() - 1);
			Map<String, Object> minRightRate = new HashMap<String, Object>();
			minRightRate.put("studentId", String.valueOf(minStuMap.get("studentid")));
			minRightRate.put("rightRate", Integer.parseInt(minStuMap.get("avgright").toString()));
			// 不及格
			int fail = 0;
			// 及格
			int pass = 0;
			// 良好
			int good = 0;
			// 优秀
			int excellent = 0;
			// 正确率分布
			List<Map> distributedRightRate = new ArrayList<Map>();
			// 学生平均正确率
			List<Map> avgRightRate = new ArrayList<Map>();
			// 0,59
			Map<String, Object> temp1 = new HashMap<String, Object>();
			temp1.put("leRightRate", 0);
			temp1.put("reRightRate", 59);
			Map<String, Object> temp2 = new HashMap<String, Object>();
			temp2.put("leRightRate", 60);
			temp2.put("reRightRate", 69);
			Map<String, Object> temp3 = new HashMap<String, Object>();
			temp3.put("leRightRate", 70);
			temp3.put("reRightRate", 84);
			Map<String, Object> temp4 = new HashMap<String, Object>();
			temp4.put("leRightRate", 85);
			temp4.put("reRightRate", 100);
			for (Map map : stuRightMaps) {
				Integer rightRate = Integer.parseInt(map.get("avgright").toString());
				Long studentId = Long.parseLong(String.valueOf(map.get("studentid")));
				Integer rank = Integer.parseInt(String.valueOf(map.get("rank")));
				if (rightRate >= 0 && rightRate < 60) {
					fail++;
				} else if (rightRate >= 60 && rightRate < 70) {
					pass++;
				} else if (rightRate >= 70 && rightRate < 85) {
					good++;
				} else {
					excellent++;
				}
				Map<String, Object> rateMap = new HashMap<String, Object>();
				rateMap.put("studentId", studentId);
				rateMap.put("rightRate", rightRate);
				rateMap.put("rank", rank);
				if (preStuMap.get(studentId) != null) {
					rateMap.put("rankingFloating", preStuMap.get(studentId) - rank);
				} else {
					rateMap.put("rankingFloating", 0);
				}
				avgRightRate.add(rateMap);
			}
			temp1.put("studentCount", fail);
			temp2.put("studentCount", pass);
			temp3.put("studentCount", good);
			temp4.put("studentCount", excellent);
			distributedRightRate.add(temp1);
			distributedRightRate.add(temp2);
			distributedRightRate.add(temp3);
			distributedRightRate.add(temp4);
			studentRightRates.put("maxRightRate", maxRightRate);
			studentRightRates.put("minRightRate", minRightRate);
			studentRightRates.put("avgRightRate", avgRightRate);
			studentRightRates.put("distributedRightRate", distributedRightRate);
		}

		return studentRightRates;
	}

	/**
	 * 章节学情分析集合
	 * 
	 * @param clazzId
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String, Object>> getKnowpointAnalysis(Long clazzId, Integer textbookCode) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		Params params = Params.param("clazzId", clazzId).put("textbookCode", textbookCode + "%");
		params.put("startTime", cal.getTime());
		params.put("endTime", new Date());
		// 获取章节知识点做题情况
		List<Map> list = classStatRepo.find("$getKnowpointAnalysis", params).list(Map.class);
		if (CollectionUtils.isEmpty(list)) {
			return new ArrayList<Map<String, Object>>();
		}
		List<Long> sectionCodes = new ArrayList<Long>();
		List<Long> kpCodes = new ArrayList<Long>();
		for (Map map : list) {
			Long sectionCode = Long.parseLong(String.valueOf(map.get("section_code")));
			Long kpCode = Long.parseLong(String.valueOf(map.get("knowledge_code")));
			sectionCodes.add(sectionCode);
			kpCodes.add(kpCode);
		}
		Map<Long, VSection> sectionMap = sectionConvert.to(sectionService.mget(sectionCodes));
		Map<Long, VKnowledgePoint> kpMap = kpConvert.mget(kpCodes);
		// 临时用来存储sectionCode的set
		Set<Long> tempCodes = new HashSet<Long>();
		Map<String, Object> sectionKpMap = null;
		Map<Long, Map<String, Object>> skMap = new HashMap<Long, Map<String, Object>>();
		List<Map<String, Object>> knowpointAnalysis = new ArrayList<Map<String, Object>>();
		for (Map map : list) {
			Long sectionCode = Long.parseLong(String.valueOf(map.get("section_code")));
			Long kpCode = Long.parseLong(String.valueOf(map.get("knowledge_code")));
			Long wrongCount = Long.parseLong(String.valueOf(map.get("wrong_count")));
			Long rightCount = Long.parseLong(String.valueOf(map.get("right_count")));
			Map kpTemp = new HashMap();
			kpTemp.put("code", kpCode);
			kpTemp.put("name", kpMap.get(kpCode).getName());
			Integer score = new BigDecimal((rightCount + 1) * 100d / (rightCount + wrongCount + 2)).setScale(0,
					BigDecimal.ROUND_HALF_UP).intValue();
			kpTemp.put("score", score);
			if (score > 90 && score <= 100) {
				kpTemp.put("masterStatus", 0);
			} else if (score > 60 && score <= 90) {
				kpTemp.put("masterStatus", 1);
			} else if (score > 30 && score <= 60) {
				kpTemp.put("masterStatus", 2);
			} else if (score >= 0 && score <= 30) {
				kpTemp.put("masterStatus", 3);
			}
			if (tempCodes.contains(sectionCode)) {
				Map<String, Object> mm = skMap.get(sectionCode);
				List<Map> pp = (List<Map>) mm.get("knowledgePoints");
				pp.add(kpTemp);
			} else {
				// 每个sectionCode第一次的时候
				sectionKpMap = new HashMap<String, Object>();
				sectionKpMap.put("sectionCode", sectionCode);
				sectionKpMap.put("sectionName", sectionMap.get(sectionCode).getName());
				sectionKpMap.put("score", score);
				sectionKpMap.put("masterStatus", 1);
				List<Map> knowledgePoints = new ArrayList<Map>();
				knowledgePoints.add(kpTemp);
				sectionKpMap.put("knowledgePoints", knowledgePoints);
				skMap.put(sectionCode, sectionKpMap);
			}
			tempCodes.add(sectionCode);
		}
		for (Long sectionCode : skMap.keySet()) {
			knowpointAnalysis.add(skMap.get(sectionCode));
		}
		return knowpointAnalysis;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String, Object>> getSectionAnalysis(Long clazzId, Integer textbookCode) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		Params params = Params.param("clazzId", clazzId).put("textbookCode", textbookCode + "%");
		params.put("startTime", cal.getTime());
		params.put("endTime", new Date());
		// 获取章节知识点做题情况
		List<Map> list = classStatRepo.find("$getSectionAnalysis", params).list(Map.class);
		if (CollectionUtils.isEmpty(list)) {
			return new ArrayList<Map<String, Object>>();
		}
		List<Long> sectionCodes = new ArrayList<Long>();
		for (Map map : list) {
			Long sectionCode = Long.parseLong(String.valueOf(map.get("section_code")));
			sectionCodes.add(sectionCode);
			if (sectionCode.toString().length() > 12) {
				sectionCodes.add(Long.parseLong(sectionCode.toString().substring(0, 12)));
			}
		}
		Map<Long, VSection> sectionMap = sectionConvert.to(sectionService.mget(sectionCodes));
		// 临时用来存储sectionCode的set
		Map<Long, Map<String, Object>> skMap = new HashMap<Long, Map<String, Object>>();
		List<Map<String, Object>> sectionAnalysis = new ArrayList<Map<String, Object>>();
		for (Map map : list) {
			int level = 3;
			Long sectionCode = Long.parseLong(String.valueOf(map.get("section_code")));
			Long wrongCount = Long.parseLong(String.valueOf(map.get("wrong_count")));
			Long rightCount = Long.parseLong(String.valueOf(map.get("right_count")));
			Long parentCode = Long.parseLong(sectionCode.toString().substring(0, 12));
			Integer score = new BigDecimal((rightCount + 1) * 100d / (rightCount + wrongCount + 2)).setScale(0,
					BigDecimal.ROUND_HALF_UP).intValue();
			// 说明是第二层对应节
			if (sectionCode.toString().length() == 12) {
				level = 2;
			}
			if (level == 2) {
				// 每个sectionCode第一次的时候
				Map<String, Object> sectionKpMap = new HashMap<String, Object>();
				sectionKpMap.put("code", sectionCode);
				sectionKpMap.put("name", sectionMap.get(sectionCode).getName());
				sectionKpMap.put("score", score);
				sectionKpMap.put("wrongCount", wrongCount);
				sectionKpMap.put("rightCount", rightCount);
				if (score > 90 && score <= 100) {
					sectionKpMap.put("masterStatus", 0);
				} else if (score > 60 && score <= 90) {
					sectionKpMap.put("masterStatus", 1);
				} else if (score > 30 && score <= 60) {
					sectionKpMap.put("masterStatus", 2);
				} else if (score >= 0 && score <= 30) {
					sectionKpMap.put("masterStatus", 3);
				}
				skMap.put(sectionCode, sectionKpMap);
				sectionAnalysis.add(sectionKpMap);
			} else {
				if (skMap.get(parentCode) == null) {
					Map<String, Object> sectionKpMap = new HashMap<String, Object>();
					sectionKpMap.put("code", parentCode);
					sectionKpMap.put("name", sectionMap.get(parentCode).getName());
					sectionKpMap.put("score", score);
					sectionKpMap.put("wrongCount", wrongCount);
					sectionKpMap.put("rightCount", rightCount);
					List<Map> children = new ArrayList<Map>();
					Map kpTemp = new HashMap();
					kpTemp.put("code", sectionCode);
					kpTemp.put("name", sectionMap.get(sectionCode).getName());
					kpTemp.put("score", score);
					kpTemp.put("wrongCount", wrongCount);
					kpTemp.put("rightCount", rightCount);
					if (score > 90 && score <= 100) {
						sectionKpMap.put("masterStatus", 0);
						kpTemp.put("masterStatus", 0);
					} else if (score > 60 && score <= 90) {
						sectionKpMap.put("masterStatus", 1);
						kpTemp.put("masterStatus", 1);
					} else if (score > 30 && score <= 60) {
						sectionKpMap.put("masterStatus", 2);
						kpTemp.put("masterStatus", 2);
					} else if (score >= 0 && score <= 30) {
						sectionKpMap.put("masterStatus", 3);
						kpTemp.put("masterStatus", 3);
					}
					children.add(kpTemp);
					sectionKpMap.put("children", children);
					skMap.put(parentCode, sectionKpMap);
					sectionAnalysis.add(sectionKpMap);
				} else {
					Map<String, Object> pp = skMap.get(parentCode);
					Long wc = Long.parseLong(pp.get("wrongCount").toString()) + wrongCount;
					Long rc = Long.parseLong(pp.get("rightCount").toString()) + rightCount;
					pp.put("wrongCount", wc);
					pp.put("rightCount", rc);
					Integer total = new BigDecimal((rc + 1) * 100d / (rc + wc + 2)).setScale(0,
							BigDecimal.ROUND_HALF_UP).intValue();
					pp.put("score", total);
					if (total > 90 && total <= 100) {
						pp.put("masterStatus", 0);
					} else if (total > 60 && total <= 90) {
						pp.put("masterStatus", 1);
					} else if (total > 30 && total <= 60) {
						pp.put("masterStatus", 2);
					} else if (total >= 0 && total <= 30) {
						pp.put("masterStatus", 3);
					}
					List<Map> children = (List<Map>) pp.get("children");
					Map kpTemp = new HashMap();
					kpTemp.put("code", sectionCode);
					kpTemp.put("name", sectionMap.get(sectionCode).getName());
					kpTemp.put("score", score);
					kpTemp.put("wrongCount", wrongCount);
					kpTemp.put("rightCount", rightCount);
					if (score > 90 && score <= 100) {
						kpTemp.put("masterStatus", 0);
					} else if (score > 60 && score <= 90) {
						kpTemp.put("masterStatus", 1);
					} else if (score > 30 && score <= 60) {
						kpTemp.put("masterStatus", 2);
					} else if (score >= 0 && score <= 30) {
						kpTemp.put("masterStatus", 3);
					}
					children.add(kpTemp);
				}

			}
		}
		return sectionAnalysis;
	}

	/**
	 * 处理学生正确率，加上排名
	 * 
	 * @param list
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void dealListRank(List<Map> list) {
		int rank = 1;
		int sameRank = 1;
		Integer preRate = -1;
		for (int i = 0; i < list.size(); i++) {
			Map pa = list.get(i);
			Integer rate = Integer.parseInt(String.valueOf(pa.get("avgright")));
			if (preRate == -1) {
				pa.put("rank", rank);
				rank++;
			} else {
				if (rate == preRate) {
					pa.put("rank", sameRank);
				} else {
					pa.put("rank", rank);
					sameRank = rank;
				}
				rank++;
			}
			preRate = rate;
		}
	}

	@Override
	public Map<String, Long> getReportClassMap(List<Long> classIds, int year, int month, int subjectCode) {
		// key为年月+classId，value为classId
		Map<String, Long> reportClassMap = Maps.newHashMap();
		List<ClassStatisticsReport> statReport = this.getClassReports(subjectCode, classIds, year, month);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M");
		for (ClassStatisticsReport r : statReport) {
			reportClassMap.put(format.format(r.getCalDate()) + r.getClassId().toString(), r.getClassId());
		}
		return reportClassMap;
	}

	@Override
	public List<ClassStatisticsReport> getClassReports(int subjectCode, List<Long> classIds, int year, int month) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M");
		try {
			return classStatRepo.find(
					"$taskQuery",
					Params.param("subjectCode", subjectCode).put("classIds", classIds)
							.put("calDate", format.parseObject(year + "-" + month))).list();
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public CursorPage<Long, Teacher> getAll(CursorPageable<Long> cursorPageable) {
		return teacherRepo.find("$taskGetAllByPage").fetch(cursorPageable);
	}

}

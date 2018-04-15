package com.lanking.stuWeekReport.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.stat.StudentClassWeekReport;
import com.lanking.cloud.domain.yoomath.stat.StudentWeekReport;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.stuWeekReport.dao.HomeworkClazzDAO;
import com.lanking.stuWeekReport.dao.HomeworkDAO;
import com.lanking.stuWeekReport.dao.HomeworkStudentClazzDAO;
import com.lanking.stuWeekReport.dao.SectionDAO;
import com.lanking.stuWeekReport.dao.StudentClassWeekReportDAO;
import com.lanking.stuWeekReport.dao.StudentDAO;
import com.lanking.stuWeekReport.dao.StudentHomeworkDAO;
import com.lanking.stuWeekReport.dao.StudentWeekReportDAO;
import com.lanking.stuWeekReport.dao.UserDAO;

@Transactional(readOnly = true)
@Service
public class StuWeekReportStatServiceImpl implements com.lanking.stuWeekReport.service.StuWeekReportStatService {

	@Qualifier("stuWeekReportStudentHomeworkDAO")
	@Autowired
	private StudentHomeworkDAO studentHomeworkDAO;

	@Qualifier("stuWeekReportHomeworkDAO")
	@Autowired
	private HomeworkDAO homeworkDAO;

	@Qualifier("stuWeekReportSectionDAO")
	@Autowired
	private SectionDAO sectionDAO;

	@Qualifier("stuWeekReportStudentDAO")
	@Autowired
	private StudentDAO studentDAO;

	@Autowired
	private StudentWeekReportDAO stuWeekReportDAO;

	@Autowired
	private StudentClassWeekReportDAO studentClassWeekReportDAO;

	@Qualifier("stuWeekReportUserDAO")
	@Autowired
	private UserDAO userDAO;

	@Qualifier("stuWeekReportHomeworkClazzDAO")
	@Autowired
	private HomeworkClazzDAO homeworkClazzDAO;

	@Qualifier("stuWeekReportHomeworkStudentClazzDAO")
	@Autowired
	private HomeworkStudentClazzDAO homeworkStuClazzDAO;

	private Logger logger = LoggerFactory.getLogger(StuWeekReportStatServiceImpl.class);

	@Transactional
	@Override
	public void stat(Long userId, String createTime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			StudentWeekReport stuWeekReport = new StudentWeekReport();
			// 每周一统计，前一周的数据
			Calendar cal = Calendar.getInstance();
			
			Date fixDate = null;
			try {
				fixDate = format.parse(createTime);
			} catch (ParseException e) {
				logger.error("转换时间错误",e);
			}
			
			//统计时间设定为指定的时间
			cal.setTime(fixDate);
			
			// 暂时提供实时给测试,后续删除
			// cal.add(Calendar.DAY_OF_MONTH, 0);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			String endDate = format.format(cal.getTime());
			cal.add(Calendar.DAY_OF_MONTH, -6);
			String startDate = format.format(cal.getTime());
			stuWeekReport.setStartDate(format.parse(startDate));
			stuWeekReport.setEndDate(format.parse(endDate));
			stuWeekReport.setCreateAt(fixDate);
			stuWeekReport.setUserId(userId);
			// 查询homework,student_homework.保存对应homeworkAnalysis
			List<StudentHomework> stuHkList = studentHomeworkDAO.findByUserId(userId, startDate, endDate);
			List<Long> homeworkIds = new ArrayList<Long>();
			for (StudentHomework sh : stuHkList) {
				homeworkIds.add(sh.getHomeworkId());
			}
			Map<Long, Homework> homeworkMap = homeworkDAO.mget(homeworkIds);
			List<Map<String, Object>> homeworks = new ArrayList<Map<String, Object>>();
			Double maxRightRate = 0.0;
			Double minRightRate = 100.0;
			Map<String, Object> maxRightRateMap = new HashMap<String, Object>();
			Map<String, Object> minRightRateMap = new HashMap<String, Object>();
			for (StudentHomework sh : stuHkList) {
				Map<String, Object> hkMap = new HashMap<String, Object>();
				hkMap.put("id", sh.getHomeworkId());
				if (sh.getRightRate() != null) {
					hkMap.put("rightRate", sh.getRightRate());
				}
				Homework hh = homeworkMap.get(sh.getHomeworkId());
				if (hh != null) {
					hkMap.put("date", format.format(hh.getStartTime()));
					hkMap.put("name", hh.getName());
					if (sh.getRightRate().doubleValue() >= maxRightRate) {
						maxRightRate = sh.getRightRate().doubleValue();
						maxRightRateMap = hkMap;
					}
					if (sh.getRightRate().doubleValue() <= minRightRate) {
						minRightRate = sh.getRightRate().doubleValue();
						minRightRateMap = hkMap;
					}
					homeworks.add(hkMap);
				}
			}
			Map<String, Object> homeworkAnalysis = new HashMap<String, Object>();
			if (CollectionUtils.isNotEmpty(homeworks)) {
				homeworkAnalysis.put("homeworks", homeworks);
			}
			if (CollectionUtils.isNotEmpty(maxRightRateMap)) {
				homeworkAnalysis.put("maxRightRate", maxRightRateMap);
			}
			if (CollectionUtils.isNotEmpty(minRightRateMap)) {
				homeworkAnalysis.put("minRightRate", minRightRateMap);
			}
			if (CollectionUtils.isNotEmpty(homeworkAnalysis)) {
				stuWeekReport.setHomeworkAnalysis(JSON.toJSONString(homeworkAnalysis));
			}
			// 章节情况，若用户没有教材不统计为空。对应section_analysis
			Student student = studentDAO.get(userId);
			if (student.getTextbookCode() != null) {
				List<Map<String, Object>> analysisList = this.getSectionAnalysis(userId, student.getTextbookCode(),
						startDate, endDate);
				if (CollectionUtils.isNotEmpty(analysisList)) {
					stuWeekReport.setSectionAnalysis(JSON.toJSONString(analysisList));
				}
			}
			// 获取上一周的结束时间
			cal.add(Calendar.DAY_OF_MONTH, -7);
			String lastStartDate = format.format(cal.getTime());
			cal.add(Calendar.DAY_OF_MONTH, 6);
			String lastEndDate = format.format(cal.getTime());
			// 获取上一周的数据，若没有上一周数据完成率浮动和正确率浮动为null
			StudentWeekReport lastStuWeekReport = stuWeekReportDAO.getByUser(userId, lastStartDate, lastEndDate);
			// 查询student_homework 取最近一周的平均正确率和平均提交率
			Map<String, Object> dataMap = studentHomeworkDAO.getStatByUser(userId, startDate, endDate);
			int score = 0;
			if (CollectionUtils.isNotEmpty(dataMap)) {
				if (dataMap.get("completion_rate") != null) {
					Integer completionRate = Integer.parseInt(String.valueOf(dataMap.get("completion_rate")));
					stuWeekReport.setCompletionRate(BigDecimal.valueOf(completionRate));
					score = score + completionRate.intValue();
					if (lastStuWeekReport != null && lastStuWeekReport.getCompletionRate() != null) {
						stuWeekReport.setCompletionRateFloat(completionRate
								- lastStuWeekReport.getCompletionRate().intValue());
					}
				}
				if (dataMap.get("right_rate") != null) {
					Integer rightRate = Integer.parseInt(String.valueOf(dataMap.get("right_rate")));
					stuWeekReport.setRightRate(BigDecimal.valueOf(rightRate));
					score = score + rightRate.intValue();
					if (lastStuWeekReport != null && lastStuWeekReport.getRightRate() != null) {
						stuWeekReport.setRightRateFloat(rightRate - lastStuWeekReport.getRightRate().intValue());
					}
				}
			}
			if (CollectionUtils.isNotEmpty(stuHkList)) {
				stuWeekReport.setScore(score);
			}
			stuWeekReportDAO.save(stuWeekReport);
			// 更新正确率排名字段
			this.statRightRateClassRanks(userId, startDate, endDate);

		} catch (Exception e) {
			logger.error("studentWeekReport error", e);
		}
	}

	/**
	 * 获取学生当前教材下章节掌握情况
	 * 
	 * @param studentId
	 * @param textbookCode
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String, Object>> getSectionAnalysis(Long studentId, Integer textbookCode, String startDate,
			String endDate) {
		// 获取章节知识点做题情况
		List<Map> list = studentHomeworkDAO.getSectionAnalysis(studentId, textbookCode, startDate, endDate);
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
		Map<Long, Section> sectionMap = sectionDAO.mget(sectionCodes);
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

	@Transactional
	@Override
	public void statStuClassWeekReport(Long classId, String createTime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Calendar cal = Calendar.getInstance();
			
			Date fixDate = null;
			try {
				fixDate = format.parse(createTime);
			} catch (ParseException e) {
				logger.error("转换时间错误",e);
			}
			
			//统计时间设定为指定的时间
			cal.setTime(fixDate);
			
			// 临时处理给测试测
			// cal.add(Calendar.DAY_OF_MONTH, 0);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			String endDate = format.format(cal.getTime());
			cal.add(Calendar.DAY_OF_MONTH, -6);
			String startDate = format.format(cal.getTime());
			List<Map> list = studentHomeworkDAO.stuClassWeekReportList(classId, startDate, endDate);
			if (CollectionUtils.isNotEmpty(list)) {
				int rank = 1;
				// 获取上一周的结束时间
				cal.add(Calendar.DAY_OF_MONTH, -7);
				String lastStartDate = format.format(cal.getTime());
				cal.add(Calendar.DAY_OF_MONTH, 6);
				String lastEndDate = format.format(cal.getTime());
				for (Map pa : list) {
					StudentClassWeekReport stuClassWeekReport = new StudentClassWeekReport();
					stuClassWeekReport.setClazzId(classId);
					stuClassWeekReport.setCreateAt(fixDate);
					stuClassWeekReport.setRightRateRank(rank);
					if (pa.get("right_rate") != null) {
						Integer rightRate = Integer.parseInt(String.valueOf(pa.get("right_rate")));
						stuClassWeekReport.setRightRate(BigDecimal.valueOf(rightRate));
					}
					stuClassWeekReport.setStartDate(format.parse(startDate));
					stuClassWeekReport.setEndDate(format.parse(endDate));
					Long userId = Long.parseLong(String.valueOf(pa.get("student_id")));
					stuClassWeekReport.setUserId(userId);
					StudentClassWeekReport last = studentClassWeekReportDAO.getByUser(userId, classId, lastStartDate,
							lastEndDate);
					if (last != null && last.getRightRateRank() != null) {
						stuClassWeekReport.setRightRateRankFloat(last.getRightRateRank() - rank);
					}
					studentClassWeekReportDAO.save(stuClassWeekReport);
					rank++;
				}
			}

		} catch (Exception e) {
			logger.error("statStuClassWeekReport error", e);
		}

	}

	@Override
	public CursorPage<Long, Map> queryUserId(CursorPageable<Long> pageable) {
		return userDAO.queryUserId(pageable);
	}

	@Override
	public CursorPage<Long, Map> queryClassIds(CursorPageable<Long> pageable) {
		return homeworkClazzDAO.queryClassIds(pageable);
	}

	@Transactional
	@Override
	public void statRightRateClassRanks(Long userId, String startDate, String endDate) {
		// 获取当前学生对应的班级Id
		List<Long> classIds = homeworkStuClazzDAO.listCurrentClazzsHasTeacher(userId);
		if (CollectionUtils.isEmpty(classIds)) {
			return;
		}
		Map<Long, HomeworkClazz> clazzMap = homeworkClazzDAO.mget(classIds);
		// 按加入班级时间排序，再按排名排序
		List<StudentClassWeekReport> reportList = studentClassWeekReportDAO.findList(classIds, startDate, endDate);
		if (CollectionUtils.isEmpty(reportList)) {
			return;
		}
		Map<Long, List<StudentClassWeekReport>> map = new HashMap<Long, List<StudentClassWeekReport>>();
		for (StudentClassWeekReport report : reportList) {
			if (map.get(report.getClazzId()) == null) {
				List<StudentClassWeekReport> tempList = new ArrayList<StudentClassWeekReport>();
				tempList.add(report);
				map.put(report.getClazzId(), tempList);
			} else {
				map.get(report.getClazzId()).add(report);
			}
		}
		List<Map<String, Object>> classReportList = new ArrayList<Map<String, Object>>();
		for (Long classId : classIds) {
			Map<String, Object> classMap = new HashMap<String, Object>();
			classMap.put("id", classId);
			classMap.put("name", clazzMap.get(classId).getName());
			classMap.put("stuNum", clazzMap.get(classId).getStudentNum());
			List<StudentClassWeekReport> rList = map.get(classId);
			// 有些班级是空的
			Map<String, Object> temp = new HashMap<String, Object>();
			if (CollectionUtils.isNotEmpty(rList)) {
				List<Map<String, Object>> rankList = new ArrayList<Map<String, Object>>();
				int myRank = -1;
				for (StudentClassWeekReport r : rList) {
					Map<String, Object> rankMap = new HashMap<String, Object>();
					rankMap.put("id", r.getUserId());
					rankMap.put("rightRate", r.getRightRate());
					rankMap.put("rank", r.getRightRateRank());
					rankMap.put("float", r.getRightRateRankFloat());
					rankMap.put("me", r.getUserId() == userId);
					if (r.getUserId() == userId) {
						if (r.getRightRateRank() != null && r.getRightRate() != null) {
							myRank = r.getRightRateRank();
						}
					}
					rankList.add(rankMap);
				}
				// 没有排名存-1
				classMap.put("myRank", myRank);
				temp.put("ranks", rankList);

			}
			temp.put("class", classMap);
			classReportList.add(temp);
		}
		StudentWeekReport stuWeekReport = stuWeekReportDAO.getByUser(userId, startDate, endDate);
		stuWeekReport.setRightRateClassRanks(JSON.toJSONString(classReportList));
		stuWeekReportDAO.save(stuWeekReport);
	}
}

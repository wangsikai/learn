package com.lanking.cloud.job.paperReport.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.stat.StudentPaperReport;
import com.lanking.cloud.domain.yoomath.stat.StudentPaperReportRecord;
import com.lanking.cloud.domain.yoomath.stat.StudentPaperReportStatus;
import com.lanking.cloud.job.paperReport.dao.HomeworkClazzDAO;
import com.lanking.cloud.job.paperReport.dao.HomeworkStudentClazzDAO;
import com.lanking.cloud.job.paperReport.dao.KnowledgePointDAO;
import com.lanking.cloud.job.paperReport.dao.SectionDAO;
import com.lanking.cloud.job.paperReport.dao.StudentDAO;
import com.lanking.cloud.job.paperReport.dao.StudentPaperReportDAO;
import com.lanking.cloud.job.paperReport.dao.StudentPaperReportRecordDAO;
import com.lanking.cloud.job.paperReport.dao.TeacherDAO;
import com.lanking.cloud.job.paperReport.service.MasterStatus;
import com.lanking.cloud.job.paperReport.service.StudentPaperReportRecordService;
import com.lanking.cloud.sdk.util.CollectionUtils;

@Transactional(readOnly = true)
@Service
public class StudentPaperReportRecordServiceImpl implements StudentPaperReportRecordService {

	@Autowired
	private StudentPaperReportRecordDAO stuParperRecordDAO;

	@Qualifier("paperReportHomeworkStudentClazzDAO")
	@Autowired
	private HomeworkStudentClazzDAO hStuClazzDAO;

	@Autowired
	private StudentPaperReportDAO stuPaperReportDAO;

	@Qualifier("paperReportHomeworkClazzDAO")
	@Autowired
	private HomeworkClazzDAO homeworkClazzDAO;

	@Qualifier("paperReportTeacherDAO")
	@Autowired
	private TeacherDAO teacherDAO;

	@Qualifier("paperReportSectionDAO")
	@Autowired
	private SectionDAO sectionDAO;

	@Qualifier("paperReportStudentDAO")
	@Autowired
	private StudentDAO studentDAO;

	@Qualifier("knowledgePointDAO")
	@Autowired
	private KnowledgePointDAO knowledgePointDAO;

	private Logger logger = LoggerFactory.getLogger(StudentPaperReportRecordServiceImpl.class);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	@Override
	public int productData() {
		List<StudentPaperReportRecord> list = stuParperRecordDAO.findDataProductingList();
		for (StudentPaperReportRecord record : list) {
			try {
				Long classId = record.getClassId();
				Date startDate = record.getStartDate();
				Date endDate = record.getEndDate(); // 包含

				// 查询截止时间点
				Calendar cal = Calendar.getInstance();
				cal.setTime(endDate);
				cal.add(Calendar.DAY_OF_YEAR, 1);

				HomeworkClazz hClazz = homeworkClazzDAO.get(classId);
				// 四川平台会出现班级老师为空的情况，这里不考虑
				if (hClazz.getTeacherId() == null) {
					continue;
				}
				Teacher teacher = teacherDAO.get(hClazz.getTeacherId());
				if (teacher.getTextbookCode() == null) {
					continue;
				}
				// 数据生成中的记录，有可能存在数据跑了一半的班级
				stuPaperReportDAO.deleteReport(classId, startDate, endDate);
				// 获取班级掌握情况
				List<Map> clazzSectionMapList = stuPaperReportDAO.getSectionMapByClassId(classId, startDate,
						cal.getTime(), teacher.getTextbookCode());

				Map<Long, Map<String, Object>> clazz = this.getClassMap(clazzSectionMapList);
				// 获取班级正确率和完整率
				Map<String, Object> clazzStat = stuPaperReportDAO.getClazzStat(classId, startDate, cal.getTime());
				// 统计要统计这个班所有学生的数据，方便排名，由产生纸质报告的相关人员负责获取对应的学生数据。
				// 按学生正确率倒序
				List<Map> studentStatList = stuPaperReportDAO.getStuStat(classId, startDate, cal.getTime());
				// 班级成员的自主练习数(2017.11.25)
				Long selfClazzCount = stuPaperReportDAO.clazzSelfCount(classId, startDate, cal.getTime());
				// 获取班级知识点做题情况 --- 2017-11-25新增
				List<Map> clazzKpMapList = stuPaperReportDAO.getKpMapByClassId(classId, startDate, cal.getTime(),
						teacher.getTextbookCode());
				// 班级
				Map clazzMap1 = stuPaperReportDAO.findClazzHkMap(classId, startDate, cal.getTime());
				clazzMap1.put("stuNum", hClazz.getStudentNum());
				Map clazzMap2 = stuPaperReportDAO.findClazzHkMap2(classId, startDate, cal.getTime());
				int rank = 1;
				int realRank = 1;
				String rankKey = "";
				for (Map stuStatMap : studentStatList) {
					Long studentId = Long.parseLong(String.valueOf(stuStatMap.get("student_id")));
					List<Map> studentMapList = stuPaperReportDAO.getSectionMapByStuId(studentId, startDate,
							cal.getTime(), teacher.getTextbookCode());
					if (CollectionUtils.isEmpty(studentMapList)) {
						continue;
					}
					List<Map> stu = this.getStuMap(studentMapList, clazz);
					// 保存StudentPaperReport
					StudentPaperReport stuPaperReport = new StudentPaperReport();
					stuPaperReport.setClassId(classId);
					stuPaperReport.setEndDate(endDate);
					stuPaperReport.setRecordId(record.getId());
					// 章节掌握情况
					stuPaperReport.setSectionMap(JSON.toJSONString(stu));
					stuPaperReport.setStartDate(startDate);
					stuPaperReport.setStudentId(studentId);
					// 学生个人的
					if (stuStatMap.get("completion_rate") != null) {
						Integer completionRate = Integer.parseInt(stuStatMap.get("completion_rate").toString());
						stuPaperReport.setCompletionRate(completionRate);
					} else {
						stuPaperReport.setCompletionRate(0);
					}
					if (stuStatMap.get("right_rate") != null) {
						Integer rightRate = Integer.parseInt(stuStatMap.get("right_rate").toString());
						stuPaperReport.setRightRate(rightRate);
					}
					String tt = stuPaperReport.getRightRate() + "," + stuPaperReport.getCompletionRate();
					if (tt.equals(rankKey)) {
						// 正确率排名
						stuPaperReport.setRightRateRank(realRank);
					} else {
						realRank = rank;
						stuPaperReport.setRightRateRank(rank);
					}
					rankKey = tt;
					// 班级的
					if (clazzStat.get("completion_rate") != null) {
						Integer classCompletionRate = Integer.parseInt(clazzStat.get("completion_rate").toString());
						stuPaperReport.setClassCompletionRate(classCompletionRate);
					} else {
						stuPaperReport.setClassCompletionRate(0);
					}
					if (clazzStat.get("right_rate") != null) {
						Integer classRightRate = Integer.parseInt(clazzStat.get("right_rate").toString());
						stuPaperReport.setClassRightRate(classRightRate);
					}

					// 获取小章节的掌握情况
					List<Map> smallSectionList = stuPaperReportDAO.getSmallSectionMapByStuId(studentId, startDate,
							cal.getTime(), teacher.getTextbookCode());
					if (CollectionUtils.isNotEmpty(smallSectionList)) {
						Map<String, Object> pp = this.dealSmallSectionList(smallSectionList);
						if (CollectionUtils.isNotEmpty(pp)) {
							stuPaperReport.setComment(JSON.toJSONString(pp));
						}
					}

					// 新增知识点题目数统计(2017.11.25)
					// 学生知识点做题数Map
					Map<Long, Integer> stuKpRateMap = this.getStuKpMap(studentMapList);
					List<Map> knowledges = this.getKnowledges(clazzKpMapList, stuKpRateMap);
					if (CollectionUtils.isNotEmpty(knowledges)) {
						stuPaperReport.setKnowledges(JSON.toJSONString(knowledges));
					}
					// 学生班级最近10次，作业情况
					List<Map> hkList = this.findHkList(studentId, classId, startDate, cal.getTime());
					Map comprehensiveMap = this.useSituation(studentId, classId, startDate, cal.getTime(),
							stuPaperReport.getClassCompletionRate(), selfClazzCount, clazzMap1, clazzMap2);
					if (CollectionUtils.isNotEmpty(hkList)) {
						comprehensiveMap.put("hkList", hkList);
						stuPaperReport.setComprehensiveMap(
								JSON.toJSONString(comprehensiveMap, SerializerFeature.DisableCircularReferenceDetect));
					}
					stuPaperReportDAO.save(stuPaperReport);
					rank++;
				}

				record.setStatus(StudentPaperReportStatus.FILE_PRODUCING);
			} catch (Exception e) {
				record.setStatus(StudentPaperReportStatus.FAIL);
				logger.error("productData error", e);
			}
			//
			stuParperRecordDAO.save(record);
		}
		return list.size();
	}

	/**
	 * 处理节的掌握度，返回掌握度优秀（正序）、掌握度一般和薄弱（倒序）的节
	 * 
	 * @param smallSectionList
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> dealSmallSectionList(List<Map> smallSectionList) {
		Map<Long, Long> rightMap = new HashMap<Long, Long>();
		Map<Long, Long> wrongMap = new HashMap<Long, Long>();
		List<Long> sectionCodes = new ArrayList<Long>();
		// QuestionId，sectionCode组合，同一章节下一个题目算一题
		List<String> questionStrs = new ArrayList<String>();
		for (Map map : smallSectionList) {
			if (map.get("result") == null) {
				continue;
			}
			Integer result = Integer.parseInt(String.valueOf(map.get("result")));
			Long sectionCode = Long.parseLong(String.valueOf(map.get("section_code")));
			Long questionId = Long.parseLong(String.valueOf(map.get("question_id")));
			String str = sectionCode + "," + questionId;
			if (!questionStrs.contains(str)) {
				questionStrs.add(str);
				if (!sectionCodes.contains(sectionCode)) {
					sectionCodes.add(sectionCode);
				}
				if (result == HomeworkAnswerResult.RIGHT.getValue()) {
					if (rightMap.get(sectionCode) == null) {
						rightMap.put(sectionCode, 1L);
					} else {
						rightMap.put(sectionCode, rightMap.get(sectionCode) + 1);
					}
				}
				if (result == HomeworkAnswerResult.WRONG.getValue()) {
					if (wrongMap.get(sectionCode) == null) {
						wrongMap.put(sectionCode, 1L);
					} else {
						wrongMap.put(sectionCode, wrongMap.get(sectionCode) + 1);
					}
				}
			}
		}
		Map<Long, Section> sectionMap = sectionDAO.mget(sectionCodes);
		List<Map> weakList = new ArrayList<Map>();
		List<Map> excellentList = new ArrayList<Map>();
		List<Map> goodList = new ArrayList<Map>();
		// 只需要记录优秀、一般、薄弱的章节，良好的不记录
		for (Long sectionCode : sectionCodes) {
			Long wrongCount = wrongMap.get(sectionCode) == null ? 0L : wrongMap.get(sectionCode);
			Long rightCount = rightMap.get(sectionCode) == null ? 0L : rightMap.get(sectionCode);
			MasterStatus status = this.getMasterStatus(rightCount + wrongCount, rightCount);
			Map temp = new HashMap();
			temp.put("code", sectionCode);
			temp.put("name", sectionMap.get(sectionCode).getName());
			temp.put("status", status);
			temp.put("rate", this.getMasterRate(rightCount + wrongCount, rightCount));
			if (status == MasterStatus.EXCELLENT) {
				excellentList.add(temp);
			} else if (status == MasterStatus.WEAK || status == MasterStatus.COMMONLY) {
				weakList.add(temp);
			} else if (status == MasterStatus.GOOD) {
				goodList.add(temp);
			}
		}
		// 掌握度优秀章节，按掌握度降序
		Collections.sort(excellentList, new masterRateDownComparator());
		// 掌握度薄弱、一般章节，按掌握度升序
		Collections.sort(weakList, new masterRateUpComparator());
		Map<String, Object> pp = new HashMap<String, Object>();
		if (CollectionUtils.isNotEmpty(excellentList)) {
			pp.put("excellent", excellentList);
		}
		if (CollectionUtils.isNotEmpty(weakList)) {
			pp.put("week", weakList);
		}
		if (CollectionUtils.isNotEmpty(goodList)) {
			pp.put("good", goodList);
		}
		return pp;
	}

	/**
	 * 按掌握度升序排序
	 * 
	 * @author wangsenhao
	 *
	 */
	@SuppressWarnings("rawtypes")
	static class masterRateUpComparator implements Comparator {
		public int compare(Object object1, Object object2) {
			Map p1 = (Map) object1;
			Map p2 = (Map) object2;
			Integer a = Integer.parseInt(p1.get("rate").toString());
			Integer b = Integer.parseInt(p2.get("rate").toString());
			return a.compareTo(b);
		}
	}

	/**
	 * 按掌握度降序排序
	 * 
	 * @author wangsenhao
	 *
	 */
	@SuppressWarnings("rawtypes")
	static class masterRateDownComparator implements Comparator {
		public int compare(Object object1, Object object2) {
			Map p1 = (Map) object1;
			Map p2 = (Map) object2;
			Integer a = Integer.parseInt(p1.get("rate").toString());
			Integer b = Integer.parseInt(p2.get("rate").toString());
			return b.compareTo(a);
		}
	}

	/**
	 * 学生章节数据相关处理
	 * 
	 * @param studentMapList
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> getStuMap(List<Map> studentMapList, Map<Long, Map<String, Object>> clazzMap) {
		Map<Long, Long> rightMap = new HashMap<Long, Long>();
		Map<Long, Long> wrongMap = new HashMap<Long, Long>();
		List<Long> sectionCodes = new ArrayList<Long>();
		// QuestionId，sectionCode组合，同一章节下一个题目算一题
		List<String> questionStrs = new ArrayList<String>();
		for (Map map : studentMapList) {
			if (map.get("result") == null) {
				continue;
			}
			Integer result = Integer.parseInt(String.valueOf(map.get("result")));
			Long sectionCode = Long.parseLong(String.valueOf(map.get("section_code")));
			Long questionId = Long.parseLong(String.valueOf(map.get("question_id")));
			String str = sectionCode + "," + questionId;
			if (!questionStrs.contains(str)) {
				questionStrs.add(str);
				if (!sectionCodes.contains(sectionCode)) {
					sectionCodes.add(sectionCode);
				}
				if (result == HomeworkAnswerResult.RIGHT.getValue()) {
					if (rightMap.get(sectionCode) == null) {
						rightMap.put(sectionCode, 1L);
					} else {
						rightMap.put(sectionCode, rightMap.get(sectionCode) + 1);
					}
				}
				if (result == HomeworkAnswerResult.WRONG.getValue()) {
					if (wrongMap.get(sectionCode) == null) {
						wrongMap.put(sectionCode, 1L);
					} else {
						wrongMap.put(sectionCode, wrongMap.get(sectionCode) + 1);
					}
				}
			}
		}
		Map<Long, Section> sectionMap = sectionDAO.mget(sectionCodes);
		List<Map> sList = new ArrayList<Map>();
		for (Long sectionCode : sectionCodes) {
			Long wrongCount = wrongMap.get(sectionCode) == null ? 0L : wrongMap.get(sectionCode);
			Long rightCount = rightMap.get(sectionCode) == null ? 0L : rightMap.get(sectionCode);
			Map temp = new HashMap();
			temp.put("code", sectionCode);
			temp.put("name", sectionMap.get(sectionCode).getName());
			temp.put("stu_status", this.getMasterStatus(rightCount + wrongCount, rightCount));
			temp.put("stu_masterRate", this.getMasterRate(rightCount + wrongCount, rightCount));
			if (clazzMap.get(sectionCode) != null) {
				temp.put("clazz_status", clazzMap.get(sectionCode).get("status"));
				temp.put("clazz_masterRate", clazzMap.get(sectionCode).get("masterRate"));
			}
			sList.add(temp);
		}
		return sList;
	}

	/**
	 * 班级相关平均数据处理
	 * 
	 * @param clazzSectionMapList
	 * @return
	 */
	public Map<Long, Map<String, Object>> getClassMap(List<Map> clazzSectionMapList) {
		List<Map> list = new ArrayList<Map>();
		List<Long> sectionCodes = new ArrayList<Long>();
		for (Map map : clazzSectionMapList) {
			Long sectionCode = Long.parseLong(String.valueOf(map.get("section_code")));
			sectionCodes.add(sectionCode);
		}
		Map<Long, Section> sectionMap = sectionDAO.mget(sectionCodes);
		Map<Long, Map<String, Object>> sMap = new HashMap<Long, Map<String, Object>>();
		for (Map map : clazzSectionMapList) {
			Map<String, Object> temp = new HashMap<String, Object>();
			Long rightCount = Long.parseLong(String.valueOf(map.get("right_count")));
			Long wrongCount = Long.parseLong(String.valueOf(map.get("wrong_count")));
			Long sectionCode = Long.parseLong(String.valueOf(map.get("section_code")));
			temp.put("code", sectionCode);
			temp.put("name", sectionMap.get(sectionCode).getName());
			temp.put("status", this.getMasterStatus(rightCount + wrongCount, rightCount));
			temp.put("masterRate", this.getMasterRate(rightCount + wrongCount, rightCount));
			sMap.put(sectionCode, temp);
		}
		return sMap;
	}

	/**
	 * 学生知识点对应情况(2017.11.25)
	 * 
	 * @param studentMapList
	 * @return
	 */
	public Map<Long, Integer> getStuKpMap(List<Map> studentMapList) {
		Map<Long, Long> rightMap = new HashMap<Long, Long>();
		Map<Long, Long> wrongMap = new HashMap<Long, Long>();
		List<Long> kpCodes = new ArrayList<Long>();
		// QuestionId，sectionCode组合，同一章节下一个题目算一题
		List<String> questionStrs = new ArrayList<String>();
		for (Map map : studentMapList) {
			if (map.get("result") == null) {
				continue;
			}
			Integer result = Integer.parseInt(String.valueOf(map.get("result")));
			Long kpCode = Long.parseLong(String.valueOf(map.get("knowledge_code")));
			Long questionId = Long.parseLong(String.valueOf(map.get("question_id")));
			String str = kpCode + "," + questionId;
			if (!questionStrs.contains(str)) {
				questionStrs.add(str);
				if (!kpCodes.contains(kpCode)) {
					kpCodes.add(kpCode);
				}
				if (result == HomeworkAnswerResult.RIGHT.getValue()) {
					if (rightMap.get(kpCode) == null) {
						rightMap.put(kpCode, 1L);
					} else {
						rightMap.put(kpCode, rightMap.get(kpCode) + 1);
					}
				}
				if (result == HomeworkAnswerResult.WRONG.getValue()) {
					if (wrongMap.get(kpCode) == null) {
						wrongMap.put(kpCode, 1L);
					} else {
						wrongMap.put(kpCode, wrongMap.get(kpCode) + 1);
					}
				}
			}
		}
		Map<Long, Integer> stuKpMap = new HashMap<Long, Integer>();
		for (Long kpCode : kpCodes) {
			Long wrongCount = wrongMap.get(kpCode) == null ? 0L : wrongMap.get(kpCode);
			Long rightCount = rightMap.get(kpCode) == null ? 0L : rightMap.get(kpCode);
			stuKpMap.put(kpCode, this.getMasterRate(rightCount + wrongCount, rightCount));
		}
		return stuKpMap;
	}

	/**
	 * 知识点按题目数量排序(2017.11.25)
	 * 
	 * @author wangsenhao
	 *
	 */
	@SuppressWarnings("rawtypes")
	static class kpQuestionDownComparator implements Comparator {
		public int compare(Object object1, Object object2) {
			Map p1 = (Map) object1;
			Map p2 = (Map) object2;
			Long a = Long.parseLong(p1.get("right_count").toString())
					+ Long.parseLong(p1.get("wrong_count").toString());
			Long b = Long.parseLong(p2.get("right_count").toString())
					+ Long.parseLong(p2.get("wrong_count").toString());
			return b.compareTo(a);
		}
	}

	/**
	 * 获取班级和学生整体使用情况(2017.11.25)
	 * 
	 * @param studentId
	 * @param classId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public Map<String, Map<String, Object>> useSituation(Long studentId, Long classId, Date startTime, Date endTime,
			Integer classCompletionRate, Long selfClazzCount, Map clazzMap1, Map clazzMap2) {

		Map<String, Map<String, Object>> mm = new HashMap<String, Map<String, Object>>();
		Map<String, Object> clazzSituation = new HashMap<String, Object>();
		if (CollectionUtils.isNotEmpty(clazzMap1)) {
			clazzSituation.put("hkCount", clazzMap2.get("count0"));
			clazzSituation.put("questionCount",
					clazzMap2.get("question_count") == null ? 0 : clazzMap2.get("question_count"));
			clazzSituation.put("rightRate", clazzMap1.get("right_rate") == null ? 0 : clazzMap1.get("right_rate"));
			clazzSituation.put("completionRate", classCompletionRate == null ? 0 : classCompletionRate);
			Integer stuNum = Integer.parseInt(clazzMap1.get("stuNum").toString());
			clazzSituation.put("selfDoCount",
					new BigDecimal(Double.valueOf(selfClazzCount) / stuNum).setScale(0, BigDecimal.ROUND_HALF_UP));
		}
		mm.put("clazzSituation", clazzSituation);
		Map<String, Object> stuSituation = new HashMap<String, Object>();
		Map stuMap = stuPaperReportDAO.findStuHkMap(classId, studentId, startTime, endTime);
		Map stuMap2 = stuPaperReportDAO.findStuHkMap2(classId, studentId, startTime, endTime);
		if (CollectionUtils.isNotEmpty(stuMap)) {
			stuSituation.put("hkCount", stuMap2.get("count0"));
			stuSituation.put("completionRate",
					stuMap.get("completion_rate") == null ? 0 : stuMap.get("completion_rate"));
			stuSituation.put("rightRate", stuMap.get("right_rate") == null ? 0 : stuMap.get("right_rate"));
			stuSituation.put("questionCount",
					stuMap2.get("question_count") == null ? 0 : stuMap2.get("question_count"));
			stuSituation.put("selfDoCount", stuPaperReportDAO.stuSelfCount(studentId, startTime, endTime));
		}
		mm.put("stuSituation", stuSituation);
		// 获取自主练习题目数
		return mm;
	}

	/**
	 * 获取学生和班级对应的最近10次作业数据对比(2017.11.25)
	 * 
	 * @param studentId
	 * @param classId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<Map> findHkList(Long studentId, Long classId, Date startTime, Date endTime) {
		List<Map> list = stuPaperReportDAO.findLastHkList(classId, studentId, startTime, endTime);
		List<Map> tempList = new ArrayList<Map>();
		for (Map pa : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("homeworkId", pa.get("id"));
			map.put("name", pa.get("name"));
			map.put("time", pa.get("start_time"));
			map.put("classRate", pa.get("class_rate"));
			map.put("stuRate", pa.get("stu_rate"));
			tempList.add(map);
		}
		return tempList;
	}

	/**
	 * (2017.11.25)
	 * 
	 * @param clazzKpMapList
	 * @param stuKpRateMap
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> getKnowledges(List<Map> clazzKpMapList, Map<Long, Integer> stuKpRateMap) {
		Collections.sort(clazzKpMapList, new kpQuestionDownComparator());
		List<Long> kpCodes = Lists.newArrayList();
		for (Map pa : clazzKpMapList) {
			Long code = Long.parseLong(String.valueOf(pa.get("knowledge_code")));
			kpCodes.add(code);
		}
		List<Map> knowledges = new ArrayList<Map>();
		Map<Long, KnowledgePoint> kpMap = knowledgePointDAO.mget(kpCodes);
		for (Map pa : clazzKpMapList) {
			Map temp = new HashMap();
			Long code = Long.parseLong(String.valueOf(pa.get("knowledge_code") == null ? 0 : pa.get("knowledge_code")));
			temp.put("code", code);
			temp.put("name", kpMap.get(code).getName());
			temp.put("difficulty", pa.get("difficulty"));
			long rightCount = Long.parseLong(String.valueOf(pa.get("right_count") == null ? 0 : pa.get("right_count")));
			long wrongCount = Long.parseLong(String.valueOf(pa.get("wrong_count") == null ? 0 : pa.get("wrong_count")));
			long halfWrongCount = Long
					.parseLong(String.valueOf(pa.get("half_wrong_count") == null ? 0 : pa.get("half_wrong_count")));
			long questionCount = Long.parseLong(String.valueOf(pa.get("q_count") == null ? 0 : pa.get("q_count")));
			temp.put("questionCount", questionCount);
			temp.put("class_masterRate", this.getMasterRate(rightCount + wrongCount + halfWrongCount, rightCount));
			if (stuKpRateMap.get(code) != null) {
				temp.put("stu_masterRate", stuKpRateMap.get(code));
				knowledges.add(temp);
			}
		}
		return knowledges;
	}

	/**
	 * 获取掌握情况
	 * 
	 * @param doCount
	 * @param rightCount
	 * @return
	 */
	public MasterStatus getMasterStatus(Long doCount, Long rightCount) {
		// 做平滑处理 (n+1)/(N+2)
		Double tempRate = new BigDecimal((rightCount + 1) * 100d / (doCount + 2)).setScale(0, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
		if (tempRate > 90 && tempRate <= 100) {
			return MasterStatus.EXCELLENT;
		} else if (tempRate > 60 && tempRate <= 90) {
			return MasterStatus.GOOD;
		} else if (tempRate > 30 && tempRate <= 60) {
			return MasterStatus.COMMONLY;
		} else if (tempRate >= 0 && tempRate <= 30) {
			return MasterStatus.WEAK;
		}
		return MasterStatus.NO_PRACTICE;
	}

	/**
	 * 获取掌握度
	 * 
	 * @param doCount
	 * @param rightCount
	 * @return
	 */
	public Integer getMasterRate(Long doCount, Long rightCount) {
		// 做平滑处理 (n+1)/(N+2)
		Integer tempRate = new BigDecimal((rightCount + 1) * 100d / (doCount + 2)).setScale(0, BigDecimal.ROUND_HALF_UP)
				.intValue();
		return tempRate;
	}

	public List<StudentPaperReportRecord> findDataToFileList() {
		return stuParperRecordDAO.findDataToFileList();
	}

	@Override
	@Transactional
	public void successFile(Collection<Long> recordIds) {
		stuParperRecordDAO.successFile(recordIds);
	}

	@Override
	public Map<Long, HomeworkClazz> mgetHomeworkClazz(Collection<Long> classIds) {
		return homeworkClazzDAO.mget(classIds);
	}

	@Override
	public Map<Long, Student> mgetStudent(Collection<Long> studentIds) {
		return studentDAO.mget(studentIds);
	}
}

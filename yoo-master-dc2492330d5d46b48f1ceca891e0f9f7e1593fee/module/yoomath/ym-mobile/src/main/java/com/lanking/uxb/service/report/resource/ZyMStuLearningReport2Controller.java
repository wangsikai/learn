package com.lanking.uxb.service.report.resource;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.member.UserMember;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.stat.StudentHomeworkRightRateStat;
import com.lanking.cloud.domain.yoomath.stat.StudentHomeworkStatistic;
import com.lanking.cloud.domain.yoomath.stat.StudentWeekReport;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.KnowledgeSectionService;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.diagnostic.api.DiagnosticStudentClassKnowpointService;
import com.lanking.uxb.service.diagnostic.value.MasterStatus;
import com.lanking.uxb.service.report.api.StudentClassWeekReportService;
import com.lanking.uxb.service.report.api.StudentHomeworkRightRateStatService;
import com.lanking.uxb.service.report.api.StudentHomeworkStatisticService;
import com.lanking.uxb.service.report.api.StudentWeekReportService;
import com.lanking.uxb.service.report.convert.StudentWeekReportConvert;
import com.lanking.uxb.service.report.value.VStudentWeekReport;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.thirdparty.api.ShareLogService;
import com.lanking.uxb.service.user.api.StudentService;
import com.lanking.uxb.service.user.api.UserMemberService;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;

/**
 * 学生端--新学情分析
 * 
 * @author wangsenhao
 *
 */
@RestController
@RequestMapping("zy/m/s/report/learning2")
public class ZyMStuLearningReport2Controller {

	@Autowired
	private StudentHomeworkRightRateStatService stuHkRateStatService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private DiagnosticStudentClassKnowpointService stuKpService;
	@Autowired
	private TextbookService textbookService;
	@Autowired
	private TextbookConvert textbookConvert;
	@Autowired
	private KnowledgePointService kpService;
	@Autowired
	private StudentWeekReportService stuWeekReportService;
	@Autowired
	private StudentWeekReportConvert stuWeekReportConvert;
	@Autowired
	private ZyHomeworkStudentClazzService stuClassService;
	@Autowired
	private StudentClassWeekReportService stuClassWeekReportService;
	@Autowired
	private ZyHomeworkClassService zyHomeworkClassService;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private KnowledgeSectionService knowSectionService;
	@Autowired
	private StudentHomeworkStatisticService stuhkStatService;
	@Autowired
	private ShareLogService shareLogService;
	@Autowired
	private UserMemberService userMemberService;

	/**
	 * 班级数据统计<br>
	 * 当前学生所有班级的
	 * 
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "classData", method = { RequestMethod.POST, RequestMethod.GET })
	public Value classData() {
		Map<String, Object> data = new HashMap<String, Object>();
		StudentHomeworkStatistic ss = stuhkStatService.get(Security.getUserId());
		if (ss == null) {
			data.put("homeworkNum", 0);
		} else {
			data.put("homeworkNum", ss.getHomeWorkCount());
			if (ss.getRightRate() != null) {
				data.put("rightRate", ss.getRightRate());
			}
			if (ss.getCompletionRate() != null) {
				data.put("completionRate", ss.getCompletionRate());
			}
		}
		data.put("stuName", userConvert.get(Security.getUserId()).getName());
		List<StudentWeekReport> studentWeekReportList = stuWeekReportService.getByUserId(Security.getUserId());
		if (CollectionUtils.isEmpty(studentWeekReportList)) {
			data.put("hasWeekReport", false);
		} else {
			data.put("hasWeekReport", true);
			// 获取最新周报告
			StudentWeekReport newReport = studentWeekReportList.get(studentWeekReportList.size() - 1);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			data.put("startDate", sdf.format(newReport.getStartDate()));
			data.put("endDate", sdf.format(newReport.getEndDate()));
			data.put("userId", Security.getUserId());
		}
		return new Value(data);
	}

	/**
	 * 获取正确率变化
	 * 
	 * @param days
	 *            7,30,90
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@SuppressWarnings("rawtypes")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "rightRateList", method = { RequestMethod.POST, RequestMethod.GET })
	public Value rightRateList(Integer days) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<StudentHomeworkRightRateStat> list = stuHkRateStatService.findList(days, Security.getUserId());
		if (CollectionUtils.isEmpty(list)) {
			return new Value(map);
		}
		List<Map> rightRateList = new ArrayList<Map>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (StudentHomeworkRightRateStat stat : list) {
			Map<String, Object> rateMap = new HashMap<String, Object>();
			rateMap.put("time", sdf.format(stat.getStatisticsTime()));
			rateMap.put("rightRate", stat.getRightRate());
			rightRateList.add(rateMap);
		}
		map.put("rightRateList", rightRateList);
		return new Value(map);
	}

	/**
	 * 章节掌握情况
	 * 
	 * @param textbookCode
	 *            教材textbookCode
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@MemberAllowed
	@SuppressWarnings("rawtypes")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "sectionMaster", method = { RequestMethod.POST, RequestMethod.GET })
	public Value sectionMaster(Integer textbookCode) {
		Map<String, Object> data = new HashMap<String, Object>();
		MemberType memberType = SecurityContext.getMemberType();
		if (memberType == MemberType.NONE || memberType == null) {
			data.put("isMember", false);
			return new Value(data);
		}
		data.put("isMember", true);
		// 默认情况code不需要传,取当前学生的教材
		if (textbookCode == null) {
			Student student = (Student) studentService.getUser(Security.getUserId());
			if (student.getTextbookCode() == null) {
				// 教材未选异常
				return new Value(
						new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_STU_TEXTBOOKCODE_NULL));
			}
			textbookCode = student.getTextbookCode();
			int subjectCode = student.getPhaseCode() == PhaseService.PHASE_HIGH ? SubjectService.PHASE_3_MATH
					: SubjectService.PHASE_2_MATH;
			List<Textbook> textbookList = textbookService.find(student.getPhaseCode(),
					student.getTextbookCategoryCode(), subjectCode);
			data.put("textbookList", textbookConvert.to(textbookList));
		}
		// 选中的教材
		data.put("selTextbookCode", textbookCode);
		List<VSection> tempList = sectionConvert.to(sectionService.findByTextbookCode(textbookCode, 1));
		List<VSection> vList = new ArrayList<VSection>();
		// 不显示本章综合与测试
		for (VSection v : tempList) {
			if (!v.getName().equals("本章综合与测试")) {
				vList.add(v);
			}
		}
		List<Long> sectionCodes = new ArrayList<Long>();
		for (VSection v : vList) {
			sectionCodes.add(v.getCode());
		}
		Long tempClassId = null;
		List<HomeworkStudentClazz> stuClazzs = stuClassService.listCurrentClazzsHasTeacher(Security.getUserId());
		if (CollectionUtils.isNotEmpty(stuClazzs)) {
			tempClassId = stuClazzs.get(0).getClassId();
		}
		// 学生诊断每个班级的数据存的一样的，取一个学生的班级传进去,不然会重复计算
		List<Map> sectionData = new ArrayList<Map>();
		if (sectionCodes.size() != 0) {
			sectionData = stuKpService.getSectionDoCountMap(sectionCodes, Security.getUserId(), tempClassId);
		}
		Map<Long, Long> doCountMap = new HashMap<Long, Long>();
		Map<Long, Long> rightCountMap = new HashMap<Long, Long>();
		for (Map pa : sectionData) {
			Long doCount = Long.parseLong(String.valueOf(pa.get("docount")));
			Long rightCount = Long.parseLong(String.valueOf(pa.get("rightcount")));
			Long sectionCode = Long.parseLong(String.valueOf(pa.get("section_code")));
			doCountMap.put(sectionCode, doCount);
			rightCountMap.put(sectionCode, rightCount);
		}
		List<Map> sectionKpList = new ArrayList<Map>();
		for (VSection v : vList) {
			Map<String, Object> sectionMap = new HashMap<String, Object>();
			sectionMap.put("name", v.getName());
			sectionMap.put("code", v.getCode());
			Long doCount = doCountMap.get(v.getCode());
			Long rightCount = rightCountMap.get(v.getCode());
			sectionMap.put("status", this.getMasterStatus(doCount, rightCount));
			sectionKpList.add(sectionMap);
		}
		data.put("sectionKpList", sectionKpList);
		return new Value(data);
	}

	/**
	 * 获取掌握情况
	 * 
	 * @param doCount
	 * @param rightCount
	 * @return
	 */
	public MasterStatus getMasterStatus(Long doCount, Long rightCount) {
		if (doCount == null || doCount == 0) {
			return MasterStatus.NO_PRACTICE;
		} else {
			// 做平滑处理 (n+1)/(N+2)
			Double tempRate = new BigDecimal((rightCount + 1) * 100d / (doCount + 2))
					.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
			if (tempRate > 90 && tempRate <= 100) {
				return MasterStatus.EXCELLENT;
			} else if (tempRate > 60 && tempRate <= 90) {
				return MasterStatus.GOOD;
			} else if (tempRate > 30 && tempRate <= 60) {
				return MasterStatus.COMMONLY;
			} else if (tempRate >= 0 && tempRate <= 30) {
				return MasterStatus.WEAK;
			}
		}
		return MasterStatus.NO_PRACTICE;
	}

	/**
	 * 章节详情
	 * 
	 * @param sectionCode
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@SuppressWarnings("rawtypes")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "sectionDetail", method = { RequestMethod.POST, RequestMethod.GET })
	public Value sectionDetail(Long sectionCode) {
		Map<String, Object> data = new HashMap<String, Object>();
		// 获取章节下小节点集合
		List<Long> childCodes = sectionService.findSectionChildren(sectionCode);
		List<VSection> tempList = sectionConvert.to(sectionService.mgetList(childCodes));
		List<VSection> vList = new ArrayList<VSection>();
		for (VSection v : tempList) {
			if (!v.getName().equals("本章综合与测试")) {
				vList.add(v);
			}
		}
		List<VSection> treeList = sectionConvert.assemblySectionTree(vList);
		// 去除当前sectionCode
		List<Long> newCodes = new ArrayList<Long>();
		for (Long code : childCodes) {
			if (code != sectionCode) {
				newCodes.add(code);
			}
		}
		// 小节和知识点情况对应关系
		Map<Long, List<Map<String, Object>>> sectionKpMap = new HashMap<Long, List<Map<String, Object>>>();
		Long tempClassId = null;
		List<HomeworkStudentClazz> stuClazzs = stuClassService.listCurrentClazzsHasTeacher(Security.getUserId());
		if (CollectionUtils.isNotEmpty(stuClazzs)) {
			tempClassId = stuClazzs.get(0).getClassId();
		}
		// 学生诊断每个班级的数据存的一样的，取一个学生的班级传进去,不然会重复计算
		List<Map> sectionKpList = stuKpService.getKpDataBySectioncodes(newCodes, Security.getUserId(), tempClassId);
		List<Map> newSectionKpList = this.handle(sectionKpList, newCodes);
		for (Map map : newSectionKpList) {
			Long kpCode = Long.parseLong(String.valueOf(map.get("knowpoint_code")));
			Long sCode = Long.parseLong(String.valueOf(map.get("section_code")));
			Long doCount = Long.parseLong(String.valueOf(map.get("docount")));
			Long rightCount = Long.parseLong(String.valueOf(map.get("rightcount")));
			Map<String, Object> kpMap = new HashMap<String, Object>();
			kpMap.put("kpCode", kpCode);
			// 直接查的缓存
			kpMap.put("kpName", kpService.get(kpCode).getName());
			kpMap.put("doCount", doCount);
			kpMap.put("status", this.getMasterStatus(doCount, rightCount));
			if (sectionKpMap.get(sCode) == null) {
				List<Map<String, Object>> kpList = new ArrayList<Map<String, Object>>();
				kpList.add(kpMap);
				sectionKpMap.put(sCode, kpList);
			} else {
				sectionKpMap.get(sCode).add(kpMap);
			}
		}
		List<VSection> childList = treeList.get(0).getChildren();
		List<Map> childKpList = new ArrayList<Map>();
		// 最多只考虑3层，这边查节和小节
		for (VSection v : childList) {
			Map<String, Object> childMap = new HashMap<String, Object>();
			childMap.put("code", v.getCode());
			childMap.put("name", v.getName());
			if (CollectionUtils.isNotEmpty(v.getChildren())) {
				List<Map> childKp2List = new ArrayList<Map>();
				for (VSection vv : v.getChildren()) {
					Map<String, Object> child2Map = new HashMap<String, Object>();
					child2Map.put("code", vv.getCode());
					child2Map.put("name", vv.getName());
					List<Map<String, Object>> kpList = sectionKpMap.get(vv.getCode());
					child2Map.put("kpList", kpList);
					if (CollectionUtils.isNotEmpty(kpList)) {
						List<Long> weekCodes = this.getWeekKpCodes(kpList);
						if (CollectionUtils.isNotEmpty(weekCodes)) {
							child2Map.put("weekCodes", weekCodes);
						}
					}

					child2Map.put("kpCount",
							sectionKpMap.get(vv.getCode()) == null ? 0 : sectionKpMap.get(vv.getCode()).size());
					childKp2List.add(child2Map);
				}
				childMap.put("children", childKp2List);
			} else {
				List<Map<String, Object>> kpList = sectionKpMap.get(v.getCode());
				childMap.put("kpList", kpList);
				if (CollectionUtils.isNotEmpty(kpList)) {
					List<Long> weekCodes = this.getWeekKpCodes(kpList);
					if (CollectionUtils.isNotEmpty(weekCodes)) {
						childMap.put("weekCodes", weekCodes);
					}
				}
				childMap.put("kpCount",
						sectionKpMap.get(v.getCode()) == null ? 0 : sectionKpMap.get(v.getCode()).size());
			}
			childKpList.add(childMap);
		}
		data.put("childKpList", childKpList);
		data.put("sectionName", sectionService.get(sectionCode).getName());
		return new Value(data);
	}

	/**
	 * 获取薄弱或一般的知识点code集合
	 * 
	 * @return
	 */
	public List<Long> getWeekKpCodes(List<Map<String, Object>> list) {
		List<Long> codes = new ArrayList<Long>();
		for (Map<String, Object> map : list) {
			MasterStatus status = (MasterStatus) map.get("status");
			Long kpCode = Long.parseLong(map.get("kpCode").toString());
			if (status == MasterStatus.COMMONLY || status == MasterStatus.WEAK) {
				codes.add(kpCode);
			}
		}
		return codes;
	}

	/**
	 * 章节code和知识点联合key
	 * 
	 * @param sectionKpList
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, Object> initSectionkpMap(List<Map> sectionKpList) {
		Map<String, Object> temp = new HashMap<String, Object>();
		for (Map map : sectionKpList) {
			Long sCode = Long.parseLong(String.valueOf(map.get("section_code")));
			Long kpCode = Long.parseLong(String.valueOf(map.get("knowpoint_code")));
			temp.put(sCode + ":" + kpCode, map);
		}
		return temp;
	}

	/**
	 * 处理将未练习的知识点也补充上,默认做题数0，对题数0
	 * 
	 * @param sectionKpList
	 * @param newCodes
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> handle(List<Map> sectionKpList, List<Long> newCodes) {
		Map<String, Object> temp = initSectionkpMap(sectionKpList);
		List<Map> newSectionKpList = new ArrayList<Map>();
		// 完整的章节对应的知识点列表，不管有没有做题
		Map<Long, List<Long>> sectionAllKpMap = knowSectionService.mGetKnowledgeSectionMap(newCodes);
		for (Long sCode : sectionAllKpMap.keySet()) {
			for (Long kpCode : sectionAllKpMap.get(sCode)) {
				String key = sCode + ":" + kpCode;
				if (temp.get(key) != null) {
					newSectionKpList.add((Map) temp.get(key));
				} else {
					Map pa = new HashMap();
					pa.put("knowpoint_code", kpCode);
					pa.put("section_code", sCode);
					pa.put("docount", 0);
					pa.put("rightcount", 0);
					newSectionKpList.add(pa);
				}
			}
		}
		return newSectionKpList;
	}

	/**
	 * 学情分析--周统计第一页(含无数据)
	 * 
	 * @param userId
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "getReport1", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getReport1(Long userId, Boolean shareFlag, String startDate, String endDate) {
		if (userId == null) {
			userId = Security.getUserId();
		}
		Map<String, Object> data = new HashMap<String, Object>();
		List<StudentWeekReport> studentWeekReportList = stuWeekReportService.getByUserId(userId);
		// 当前周
		if (CollectionUtils.isEmpty(studentWeekReportList)) {
			return new Value(data);
		}
		VStudentWeekReport newReport = null;
		StudentWeekReport report = null;
		if (startDate != null) {
			report = stuWeekReportService.findWeekReport(userId, startDate, endDate);
			newReport = stuWeekReportConvert.to(report);
		} else {
			report = studentWeekReportList.get(studentWeekReportList.size() - 1);
			newReport = stuWeekReportConvert.to(report);
		}
		if (shareFlag != null && shareFlag) {
			boolean isShare = shareLogService.isShare(Biz.STUDENT_WEEK_REPORT, report.getId());
			if (!isShare) {
				return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_REPORT_SHARE_ERROR));
			}
		}

		this.dealClassRanks(Lists.newArrayList(newReport));
		// 未做作业score存null,需要前端判断
		data.put("new", newReport);
		if (shareFlag != null && shareFlag) {
			data.put("all", Lists.newArrayList(newReport));
		} else {
			List<VStudentWeekReport> vList = stuWeekReportConvert.to(studentWeekReportList);
			this.dealClassRanks(vList);
			data.put("all", vList);
		}
		data.put("stuName", userConvert.get(userId).getName());
		return new Value(data);
	}

	/**
	 * 数据库已经按加入班级的时间排序
	 * 
	 * <pre>
	 * 1、本周有排名的班级
	 * 2、选择班级人数最多的班级
	 * 3、若人数相同，则最新加入的班级
	 * </pre>
	 * 
	 * @param reportList
	 */
	public void dealClassRanks(List<VStudentWeekReport> reportList) {
		for (VStudentWeekReport v : reportList) {
			if (v.getRightRateClassRanks() != null) {
				List<Map> rightRateClassRanks = JSONArray.parseArray(v.getRightRateClassRanks(), Map.class);
				if (CollectionUtils.isNotEmpty(rightRateClassRanks)) {
					Map<String, Object> showClassMap = new HashMap<String, Object>();
					int max = -1;
					for (Map map : rightRateClassRanks) {
						Map classMap = (Map) map.get("class");
						int stuNum = Integer.parseInt(classMap.get("stuNum").toString());
						if (classMap.get("myRank") != null) {
							int myRank = Integer.parseInt(classMap.get("myRank").toString());
							// 有数据
							if (myRank != -1 && stuNum > max) {
								max = stuNum;
								showClassMap.put("className", classMap.get("name"));
								showClassMap.put("myRank", classMap.get("myRank"));
							}
						}
					}
					v.setThisWeekClassMap(showClassMap);
					v.setRightRateClassRanks(null);
				}

			}
		}

	}

	/**
	 * 学情分析--周统计第二页
	 * 
	 * @param userId
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(anyone = true)
	@MemberAllowed
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "getReport2", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getReport2(Long userId, String startDate, String endDate) {
		if (userId == null) {
			userId = Security.getUserId();
		}
		StudentWeekReport report = stuWeekReportService.findWeekReport(userId, startDate, endDate);
		Map data = new HashMap();
		UserMember userMember = userMemberService.findByUserId(userId);
		if (userMember != null) {
			if (userMember.getMemberType() == MemberType.NONE) {
				data.put("isMember", false);
				return new Value(data);
			} else {
				if (userMember.getEndAt().before(new Date())) {
					data.put("isMember", false);
					return new Value(data);
				}
			}
		} else {
			data.put("isMember", false);
			return new Value(data);
		}
		if (report == null) {
			return new Value(data);
		}
		data = (Map) JSON.parse(report.getHomeworkAnalysis());
		data.put("isMember", true);
		if (CollectionUtils.isEmpty(data)) {
			return new Value(data);
		}
		List<Map> homeworks = (List<Map>) data.get("homeworks");
		data.put("homeworks", homeworks);
		Map maxRightRate = (Map) data.get("maxRightRate");
		Map minRightRate = (Map) data.get("minRightRate");
		data.put("maxRightRate", maxRightRate);
		data.put("minRightRate", minRightRate);
		return new Value(data);
	}

	/**
	 * 学情分析--周统计第三页
	 * 
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(anyone = true)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "getReport3", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getReport3(Long userId, String startDate, String endDate) {
		if (userId == null) {
			userId = Security.getUserId();
		}
		List<Map<String, Object>> classReportList = new ArrayList<Map<String, Object>>();
		StudentWeekReport report = stuWeekReportService.findWeekReport(userId, startDate, endDate);
		List<Map> rightRateClassRanks = JSONArray.parseArray(report.getRightRateClassRanks(), Map.class);
		for (Map data : rightRateClassRanks) {
			Map classMap = (Map) data.get("class");
			List<Map> ranks = (List<Map>) data.get("ranks");
			Long classId = Long.parseLong(String.valueOf(classMap.get("id")));
			String className = String.valueOf(classMap.get("name"));
			Map<String, Object> pa = new HashMap<String, Object>();
			pa.put("classId", classId);
			pa.put("className", className);
			if (CollectionUtils.isNotEmpty(ranks)) {
				Map<String, List<Map>> reportTempMap = this.dealRanks(ranks);
				pa.put("reportList", reportTempMap.get("reportList"));
				pa.put("myReport", reportTempMap.get("myReport"));
			}
			classReportList.add(pa);
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("classReportList", classReportList);
		return new Value(data);
	}

	/**
	 * 处理班级排名列表
	 * 
	 * @param ranks
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, List<Map>> dealRanks(List<Map> ranks) {
		List<Long> userIds = new ArrayList<Long>();
		int rank = 1;
		for (Map pp : ranks) {
			Long stuId = Long.parseLong(String.valueOf(pp.get("id")));
			userIds.add(stuId);
		}
		Map<Long, VUser> userMap = userConvert.mget(userIds);
		Map<String, List<Map>> bigMap = new HashMap<String, List<Map>>();
		// 班级全部学生的正确率情况
		List<Map> reportList = new ArrayList<Map>();
		List<Map> myReport = new ArrayList<Map>();
		for (Map map : ranks) {
			Boolean me = (Boolean) map.get("me");
			Long stuId = Long.parseLong(String.valueOf(map.get("id")));
			Map<String, Object> tt = new HashMap<String, Object>();
			tt.put("rightRate", map.get("rightRate"));
			tt.put("rightRateRank", rank);
			tt.put("rightRateRankFloat", map.get("float"));
			tt.put("vuser", userMap.get(stuId));
			if (me) {
				myReport.add(tt);
			}
			reportList.add(tt);
			rank++;
		}
		if (CollectionUtils.isNotEmpty(reportList)) {
			if (reportList.size() > 5) {
				reportList = reportList.subList(0, 5);
			}
		}
		bigMap.put("reportList", reportList);
		bigMap.put("myReport", myReport);
		return bigMap;
	}

	/**
	 * 学情分析--周统计第四页
	 * 
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(anyone = true)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "getReport4", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getReport4(Long userId, String startDate, String endDate) {
		if (userId == null) {
			userId = Security.getUserId();
		}
		StudentWeekReport report = stuWeekReportService.findWeekReport(userId, startDate, endDate);
		List<Map> sectionAnalysisMaps = Lists.newArrayList();
		if (report != null && report.getSectionAnalysis() != null) {
			sectionAnalysisMaps = JSONArray.parseArray(report.getSectionAnalysis(), Map.class);
		}
		List<Map> tempList = new ArrayList<Map>();
		// 只统计节，不统计小节
		int weakCount = 0;
		if (CollectionUtils.isNotEmpty(sectionAnalysisMaps)) {
			for (Map pa : sectionAnalysisMaps) {
				int masterStatus = Integer.parseInt(String.valueOf(pa.get("masterStatus")));
				MasterStatus tempStatus = MasterStatus.findByValue(masterStatus);
				pa.put("masterStatus", tempStatus);
				pa.put("level", 2);
				tempList.add(pa);
				if (tempStatus == MasterStatus.COMMONLY || tempStatus == MasterStatus.WEAK) {
					weakCount = weakCount + 1;
				}
				List<Map> children = (List<Map>) pa.get("children");
				if (CollectionUtils.isNotEmpty(children)) {
					for (Map pb : children) {
						int status = Integer.parseInt(String.valueOf(pb.get("masterStatus")));
						pb.put("masterStatus", MasterStatus.findByValue(status));
						pb.put("level", 3);
						tempList.add(pb);
					}
				}
			}
		}
		Map<String, Object> data = new HashMap<String, Object>();
		if (CollectionUtils.isEmpty(sectionAnalysisMaps)) {
			data.put("sectionCount", 0);
		} else {
			data.put("sectionCount", sectionAnalysisMaps.size());
		}
		data.put("weakCount", weakCount);
		data.put("sectionList", tempList);
		data.put("bizId", report.getId());
		data.put("biz", Biz.STUDENT_WEEK_REPORT);
		return new Value(data);
	}

	/**
	 * 临时，造数据
	 * 
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "test", method = { RequestMethod.POST, RequestMethod.GET })
	public Value test() {
		stuHkRateStatService.inintData();
		return new Value();
	}

}

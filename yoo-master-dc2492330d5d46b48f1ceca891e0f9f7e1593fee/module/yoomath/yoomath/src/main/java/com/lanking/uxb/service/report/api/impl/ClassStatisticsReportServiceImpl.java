package com.lanking.uxb.service.report.api.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassKnowpoint;
import com.lanking.cloud.domain.yoomath.stat.ClassStatisticsReport;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.diagnostic.value.MasterStatus;
import com.lanking.uxb.service.report.api.ClassStatisticsReportService;
import com.lanking.uxb.service.report.value.VStudySection;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkService;
import com.lanking.uxb.service.zuoye.convert.ZyStudentExerciseSectionConvert;
import com.lanking.uxb.service.zuoye.value.VStudentExerciseSection;

@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
@Service
public class ClassStatisticsReportServiceImpl implements ClassStatisticsReportService {

	@Autowired
	private ZyHomeworkClassService classService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private ZyHomeworkService homeworkService;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private ZyStudentExerciseSectionConvert studentExerciseSectionConvert;
	@Autowired
	private ZyHomeworkClassService zyHomeworkClassService;

	@Autowired
	@Qualifier("ClassStatisticsReportRepo")
	private Repo<ClassStatisticsReport, Long> classStatRepo;

	@Autowired
	@Qualifier("DiagnosticClassKnowpointRepo")
	private Repo<DiagnosticClassKnowpoint, Long> classKpRepo;

	List<Map<String, Object>> getAnalysisMaps(List<VStudentExerciseSection> studentExerciseSections,
			List<Map<String, Object>> knowpointAnalysisMaps) {
		for (VStudentExerciseSection v : studentExerciseSections) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("complete", v.getLastMonthcomplete());
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
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M");
		for (ClassStatisticsReport r : statReport) {
			reportClassMap.put(format.format(r.getCalDate()) + r.getClassId().toString(), r.getClassId());
		}
		return reportClassMap;
	}

	@Override
	public List<HomeworkClazz> getClazzByMinDate(List<Long> allclassIds, Date minDate, Date maxDate) {
		List<Long> clazzIds = classStatRepo.find("$getClazzByMinDate",
				Params.param("minDate", minDate).put("maxDate", maxDate).put("classIds", allclassIds)).list(Long.class);
		if (clazzIds.size() > 0) {
			Map<Long, HomeworkClazz> clazzMap = zyHomeworkClassService.mget(clazzIds);
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
	public List<Map> getDatesByMinDate(Long clazzId, Date minDate, Date maxDate) {
		Params params = Params.param("minDate", minDate).put("maxDate", maxDate);
		if (clazzId != null) {
			params.put("clazzId", clazzId);
		}
		return classStatRepo.find("$getDatesByMinDate", params).list(Map.class);
	}

	@Override
	public boolean existReport(int year, int month, long classId) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M");
		try {
			return classStatRepo.find("$existReport",
					Params.param("classId", classId).put("calDate", format.parseObject(year + "-" + month))).count() > 0;
		} catch (ParseException e) {
		}
		return false;
	}

	@Override
	public List<Map> findSectionMasterList(Long classId) {
		return classKpRepo.find("$findSectionMasterList", Params.param("classId", classId)).list(Map.class);
	}

	@Override
	public List<VStudySection> handle(Long classId, List<VSection> vsections) {
		List<Map> list = this.findSectionMasterList(classId);
		Map<Long, Long> doCountMap = new HashMap<Long, Long>();
		Map<Long, Long> rightCountMap = new HashMap<Long, Long>();
		for (Map map : list) {
			Long sectionCode = Long.parseLong(String.valueOf(map.get("section_code")));
			Long doCount = Long.parseLong(String.valueOf(map.get("docount")));
			Long rightCount = Long.parseLong(String.valueOf(map.get("rightcount")));
			doCountMap.put(sectionCode, doCount);
			rightCountMap.put(sectionCode, rightCount);
		}
		List<VStudySection> dest = Lists.newArrayList();
		for (VSection v : vsections) {
			VStudySection vs = new VStudySection();
			vs.setCode(v.getCode());
			vs.setLevel(v.getLevel());
			vs.setName(v.getName());
			vs.setPcode(v.getPcode());
			vs.setTextbookCode(v.getTextbookCode());
			if (doCountMap.containsKey(v.getCode())) {
				vs.setDoCount(doCountMap.get(v.getCode()));
			}
			if (rightCountMap.containsKey(v.getCode())) {
				vs.setRightCount(rightCountMap.get(v.getCode()));
			}

			internalAssemblySectionTree(dest, vs);
		}
		assemblyMasterStatus(dest);
		for (VStudySection vs : dest) {
			dealMasterStatus(vs);
			if (CollectionUtils.isNotEmpty(vs.getChildren())) {
				for (VStudySection vvs : vs.getChildren()) {
					dealMasterStatus(vvs);
					if (CollectionUtils.isNotEmpty(vvs.getChildren())) {
						for (VStudySection vvvs : vvs.getChildren()) {
							dealMasterStatus(vvvs);
						}
					}
				}
			}
		}
		return dest;
	}

	private void dealMasterStatus(VStudySection vs) {
		if (vs.getDoCount() == 0) {
			vs.setMasterStatus(MasterStatus.NO_PRACTICE);
		} else {
			// 做平滑处理 (n+1)/(N+2)
			Double tempRate = new BigDecimal((vs.getRightCount() + 1) * 100d / (vs.getDoCount() + 2)).setScale(0,
					BigDecimal.ROUND_HALF_UP).doubleValue();
			if (tempRate > 90 && tempRate <= 100) {
				vs.setMasterStatus(MasterStatus.EXCELLENT);
			} else if (tempRate > 60 && tempRate <= 90) {
				vs.setMasterStatus(MasterStatus.GOOD);
			} else if (tempRate > 30 && tempRate <= 60) {
				vs.setMasterStatus(MasterStatus.COMMONLY);
			} else if (tempRate >= 0 && tempRate <= 30) {
				vs.setMasterStatus(MasterStatus.WEAK);
			}
		}
	}

	private void internalAssemblySectionTree(List<VStudySection> dest, VStudySection v) {
		if (v.getLevel() == 1) {
			dest.add(v);
		} else {
			for (VStudySection pc : dest) {
				if (pc.getCode() == v.getPcode()) {
					pc.getChildren().add(v);
					break;
				} else {
					this.internalAssemblySectionTree(pc.getChildren(), v);
				}
			}
		}
	}

	private void assemblyMasterStatus(List<VStudySection> dest) {
		for (VStudySection v : dest) {
			assemblyMasterStatus(v);
		}
		for (VStudySection v : dest) {
			assemblyMasterStatus(v);
		}
	}

	private void assemblyMasterStatus(VStudySection v) {
		List<VStudySection> children = v.getChildren();
		if (children.size() > 0) {
			long doCount = 0;
			long rightCount = 0;
			for (VStudySection c : children) {
				doCount += c.getDoCount();
				rightCount += c.getRightCount();
				assemblyMasterStatus(c);
			}
			v.setDoCount(doCount);
			v.setRightCount(rightCount);
		}
	}

	@Override
	public String getLastest(Long classId) {
		Params params = Params.param("classId", classId);
		return classStatRepo.find("$getLastest", params).get(String.class);
	}

	@Override
	public Long getMaxSection(Long classId, Integer textbookCode) {
		return classStatRepo.find("$getMaxSection",
				Params.param("classId", classId).put("textbookCode", textbookCode + "%")).get(Long.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Long, Integer> getSectionDoCountMap(Long classId, List<Long> sectionCodes) {
		List<Map> list = classKpRepo.find("$getSectionDoCountMap",
				Params.param("classId", classId).put("sectionCodes", sectionCodes)).list(Map.class);
		Map<Long, Integer> map = new HashMap<Long, Integer>();
		for (Map pa : list) {
			Long sectionCode = Long.parseLong(String.valueOf(pa.get("section_code")));
			Integer count0 = Integer.parseInt(String.valueOf(pa.get("count0")));
			map.put(sectionCode, count0);
		}
		return map;
	}

	@Override
	public List<Long> getWeakKpListBySectionCode(long classId, long sectionCode) {
		return classKpRepo.find("$getWeakKpListBySectionCode",
				Params.param("classId", classId).put("sectionCode", sectionCode)).list(Long.class);
	}
}

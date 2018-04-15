package com.lanking.uxb.service.diagnostic.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.domain.common.baseData.TextbookCategory;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClass;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassKnowpoint;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassLatestHomework;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassLatestHomeworkKnowpoint;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.KnowledgeSectionService;
import com.lanking.uxb.service.code.api.KnowledgeSystemService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.convert.KnowledgeSystemConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VKnowledgeSystem;
import com.lanking.uxb.service.diagnostic.api.DiagnosticStudentClassKnowpointService;
import com.lanking.uxb.service.diagnostic.api.DiagnosticStudentClassLatestHomeworkKnowpointService;
import com.lanking.uxb.service.diagnostic.api.DiagnosticStudentClassLatestHomeworkService;
import com.lanking.uxb.service.diagnostic.api.DiagnosticStudentClassService;
import com.lanking.uxb.service.diagnostic.api.DiagnosticStudentClassTextbookService;
import com.lanking.uxb.service.diagnostic.convert.DiagnosticKnowledgeSystemConvert;
import com.lanking.uxb.service.diagnostic.convert.DiagnosticStudentClassConvert;
import com.lanking.uxb.service.diagnostic.convert.DiagnosticStudentClassKnowpointConvert;
import com.lanking.uxb.service.diagnostic.convert.DiagnosticStudentClassLatestHomeworkConvert;
import com.lanking.uxb.service.diagnostic.convert.DiagnosticStudentClassLatestHomeworkKnowpointConvert;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticKnowledgeSystem;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticStudentClassKnowpoint;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClassConvertOption;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkStudentClazzConvert;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;
import com.lanking.uxb.service.zuoye.value.VHomeworkStudentClazz;

/**
 * 学生诊断
 * 
 * @since 2.0
 * @author wangsenhao
 *
 */
@RestController
@RequestMapping("zy/s/dia")
public class ZyStuDiagnosticController {

	@Autowired
	private DiagnosticStudentClassLatestHomeworkService diagStuLatestHkService;
	@Autowired
	private DiagnosticStudentClassLatestHomeworkConvert diagStuLatestHkConvert;
	@Autowired
	private DiagnosticStudentClassLatestHomeworkKnowpointService diagStuLatestHkKpService;
	@Autowired
	private DiagnosticStudentClassLatestHomeworkKnowpointConvert diagStuLatestHkKpConvert;
	@Autowired
	private DiagnosticStudentClassKnowpointService diagStuKpService;
	@Autowired
	private DiagnosticStudentClassKnowpointConvert diagStuKpConvert;
	@Autowired
	private DiagnosticStudentClassService diagStuService;
	@Autowired
	private DiagnosticStudentClassConvert diagStuConvert;
	@Autowired
	private KnowledgeSectionService knowledgeService;
	@Autowired
	private ZyHomeworkStudentClazzService stuClazzService;
	@Autowired
	private ZyHomeworkClazzConvert clazzConvert;
	@Autowired
	private DiagnosticStudentClassTextbookService textbookService;
	@Autowired
	private TextbookConvert textbookConvert;
	@Autowired
	private DiagnosticStudentClassKnowpointService diagnosticStudentClassKnowpointService;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private ZyHomeworkStudentClazzConvert hkStuClazzConvert;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private TextbookCategoryService textbookCategoryService;
	@Autowired
	private UserService userService;
	@Autowired
	private KnowledgeSystemService knowledgeSystemService;
	@Autowired
	private DiagnosticKnowledgeSystemConvert knowledgeSystemConvert;
	@Autowired
	private KnowledgeSystemConvert ksConvert;
	@Autowired
	private KnowledgePointService kpService;

	/**
	 * 查询学生对应的班级、教材等基本信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "queryBase", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryBase(Long userId, Long classId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (userId == null) {
			userId = Security.getUserId();
			List<HomeworkStudentClazz> clazzList = stuClazzService.listCurrentClazzs(userId);
			List<VHomeworkStudentClazz> vstuClazzs = hkStuClazzConvert.to(clazzList);
			List<Long> classIds = new ArrayList<Long>();
			for (HomeworkStudentClazz clazz : clazzList) {
				classIds.add(clazz.getClassId());
			}
			Map<Long, HomeworkClazz> clazzs = zyHkClassService.mget(classIds);
			Map<Long, VHomeworkClazz> vclazzs = clazzConvert.to(clazzs,
					new ZyHomeworkClassConvertOption(true, false, false));
			List<VHomeworkClazz> list = new ArrayList<VHomeworkClazz>();
			for (VHomeworkStudentClazz v : vstuClazzs) {
				list.add(vclazzs.get(v.getClassId()));
			}
			map.put("classList", list);
		} else {
			// 从教师诊断跳到学生诊断,查出当前学生的名称
			map.put("stuName", userService.getUser(userId).getName());
			map.put("classList", clazzConvert.to(zyHkClassService.get(classId)));
		}
		return new Value(map);
	}

	/**
	 * 教材列表
	 * 
	 * @param classId
	 * @return
	 */
	@MemberAllowed(memberType = "VIP")
	@RequestMapping(value = "queryTextbookList", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryTextbookList(Long classId, Long userId) {
		if (userId == null) {
			userId = Security.getUserId();
		}
		HomeworkClazz clazz = zyHkClassService.get(classId);
		Teacher teacher = (Teacher) teacherService.getUser(clazz.getTeacherId());
		TextbookCategory tc = textbookCategoryService.get(teacher.getTextbookCategoryCode());
		List<Integer> codes = textbookService.queryTextBookList(classId, userId, false, tc.getCode());
		List<Integer> sortCodes = textbookService.queryTextBookList(classId, userId, true, tc.getCode());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("textBookList", textbookConvert.mgetList(sortCodes));
		if (CollectionUtils.isNotEmpty(codes)) {
			map.put("selectTb", textbookConvert.get(codes.get(0)));
		}
		map.put("name", tc.getName());
		return new Value(map);
	}

	/**
	 * 作业平均正确率统计,
	 * 
	 * @param classId
	 * @param userId
	 * @param times
	 *            最近多少次作业(7,15,30)
	 * @return
	 */
	@RequestMapping(value = "queryHkRightRateList", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryHkRightRateList(Long classId, Long userId, @RequestParam(defaultValue = "30") Integer times,
			@RequestParam(defaultValue = "6") Integer kpCount) {
		if (userId == null) {
			userId = Security.getUserId();
		}
		List<DiagnosticStudentClassLatestHomework> list = diagStuLatestHkService.queryStat(userId, classId, times);
		List<DiagnosticStudentClassLatestHomeworkKnowpoint> weakList = diagStuLatestHkKpService.queryWeakList(userId,
				classId, times, kpCount);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", diagStuLatestHkConvert.to(list));
		if (CollectionUtils.isNotEmpty(weakList)) {
			map.put("weakList", diagStuLatestHkKpConvert.to(weakList));
		}
		return new Value(map);
	}

	/**
	 * 我的整体概况,分析与练习建议
	 * 
	 * @param classId
	 * @param textbookCode
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@MemberAllowed(memberType = "VIP")
	@RequestMapping(value = "queryPracRecommend", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryPracRecommend(Long classId, Long textbookCode, Long userId) {
		if (userId == null) {
			userId = Security.getUserId();
		}
		DiagnosticStudentClass d = diagStuService.queryListByTextbookCode(textbookCode, userId, classId);
		Map classMap = diagStuService.getClassAvgDoQuestion(textbookCode, classId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", diagStuConvert.to(d));
		map.put("classMap", classMap);
		return new Value(map);
	}

	/**
	 * 我的整体技能图谱，蜘蛛图数据,全部的
	 * 
	 * @param classId
	 * @param textbookCode
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@MemberAllowed(memberType = "VIP")
	@RequestMapping(value = "querySkillMap", method = { RequestMethod.GET, RequestMethod.POST })
	public Value querySkillMap(Long classId, Integer textbookCode, Long userId) {
		if (userId == null) {
			userId = Security.getUserId();
		}
		// 知识点集合
		List<Long> knowledgeList = knowledgeService.getByTextbook(textbookCode);
		// 小专题集合
		Set<Long> codes = new HashSet<Long>();
		for (Long k : knowledgeList) {
			codes.add(Long.parseLong(k.toString().substring(0, 7)));
		}
		List<DiagnosticStudentClassKnowpoint> list = diagnosticStudentClassKnowpointService.querySamllTopicList(codes,
				userId, classId);
		List<Map> classList = diagnosticStudentClassKnowpointService.querySmallTopicClassRateList(codes, classId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", diagStuKpConvert.to(list));
		map.put("classRightRateList", classList);
		return new Value(map);
	}

	/**
	 * 整体技能图谱,点击小专题或者点击知识专项
	 * 
	 * @param classId
	 * @param pcode
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@MemberAllowed(memberType = "VIP")
	@RequestMapping(value = "queryKnowledgeListByCode", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryKnowledgeListByCode(Long classId, Long pcode, Long userId) {
		if (userId == null) {
			userId = Security.getUserId();
		}
		List<DiagnosticStudentClassKnowpoint> list = diagnosticStudentClassKnowpointService
				.queryknowListBySpecial(pcode, userId, classId);
		List<Map> classRightRateList = diagnosticStudentClassKnowpointService.getClassAvgRightRateByPcode(pcode,
				classId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", diagStuKpConvert.to(list));
		map.put("classRightRateList", classRightRateList);
		return new Value(map);
	}

	/**
	 * 查询知识图谱(知识专项)
	 * 
	 * @param classId
	 * @return
	 */
	@MemberAllowed(memberType = "VIP")
	@RequestMapping(value = "queryKnowAtlas", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryKnowAtlas(Long classId, Long userId) {
		Map<String, Object> retMap = new HashMap<String, Object>(2);
		if (userId == null) {
			userId = Security.getUserId();
		}
		HomeworkClazz clazz = zyHkClassService.get(classId);
		Teacher teacher = (Teacher) teacherService.getUser(clazz.getTeacherId());
		List<KnowledgeSystem> systems = knowledgeSystemService.getBySubjectAndLevel(3, teacher.getSubjectCode());
		List<VDiagnosticKnowledgeSystem> vList = knowledgeSystemConvert.to(systems);
		List<DiagnosticStudentClassKnowpoint> specialKnowList = diagStuKpService.queryListByLevel(3, userId, classId);
		List<VDiagnosticStudentClassKnowpoint> vsList = diagStuKpConvert.to(specialKnowList);
		if (CollectionUtils.isNotEmpty(specialKnowList)) {
			Map<Long, VDiagnosticStudentClassKnowpoint> pointMap = new HashMap<Long, VDiagnosticStudentClassKnowpoint>(
					vList.size());
			for (VDiagnosticStudentClassKnowpoint vck : vsList) {
				pointMap.put(vck.getKnowpointCode(), vck);
			}
			for (VDiagnosticKnowledgeSystem v : vList) {
				v.setHasData(pointMap.get(v.getCode()) != null);
			}
			retMap.put("classPoints", pointMap);
		}
		retMap.put("maps", vList);
		retMap.put("phaseCode", teacher.getPhaseCode());
		return new Value(retMap);
	}

	/**
	 * 查询知识列表 2017-2-13新增
	 * 
	 * @author wangsenhao
	 * @param classId
	 * @param userId
	 * @return
	 */
	@MemberAllowed(memberType = "VIP")
	@RequestMapping(value = "queryKnowList", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryKnowList(Long classId, Long userId) {
		if (userId == null) {
			userId = Security.getUserId();
		}
		HomeworkClazz clazz = zyHkClassService.get(classId);
		Teacher teacher = (Teacher) teacherService.getUser(clazz.getTeacherId());
		List<KnowledgeSystem> systems = knowledgeSystemService.findAllBySubject(teacher.getSubjectCode());
		// 获取三层的知识树结构
		List<VKnowledgeSystem> tree = ksConvert.assembleTree(ksConvert.to(systems));
		List<DiagnosticStudentClassKnowpoint> knowList = diagStuKpService.queryListByLevel(null, userId, classId);
		List<VDiagnosticStudentClassKnowpoint> vsList = diagStuKpConvert.to(knowList);
		Map<String, VDiagnosticStudentClassKnowpoint> pointMap = new HashMap<String, VDiagnosticStudentClassKnowpoint>(
				tree.size());
		for (VDiagnosticStudentClassKnowpoint vck : vsList) {
			pointMap.put(vck.getKnowpointCode().toString(), vck);
		}
		for (VKnowledgeSystem v : tree) {
			VDiagnosticStudentClassKnowpoint tmp = pointMap.get(String.valueOf(v.getCode()));
			v.setHasData(tmp != null);
			if (tmp != null) {
				v.setMasterRate(tmp.getMasterRate());
			}
			for (VKnowledgeSystem vv : v.getChildren()) {
				VDiagnosticStudentClassKnowpoint tmp1 = pointMap.get(String.valueOf(vv.getCode()));
				vv.setHasData(tmp1 != null);
				if (tmp1 != null) {
					vv.setMasterRate(tmp1.getMasterRate());
				}
				for (VKnowledgeSystem vvv : vv.getChildren()) {
					VDiagnosticStudentClassKnowpoint tmp2 = pointMap.get(String.valueOf(vvv.getCode()));
					vvv.setHasData(pointMap.get(String.valueOf(vvv.getCode())) != null);
					if (tmp2 != null) {
						vvv.setMasterRate(tmp2.getMasterRate());
					}
				}
			}
		}
		return new Value(tree);
	}

	/**
	 * 通过知识专项查找所含知识点的统计信息
	 * 
	 * @param code
	 * @param classId
	 * @return
	 */
	@MemberAllowed(memberType = "VIP")
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "queryknowListBySpecial", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryknowListBySpecial(Long code, Long classId, Long userId,
			@RequestParam(defaultValue = "false") Boolean isShowAllKp) {
		if (userId == null) {
			userId = Security.getUserId();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<DiagnosticStudentClassKnowpoint> knowlist = diagStuKpService.queryknowListBySpecial(code, userId, classId);
		List<Map> classRightRateList = diagStuKpService.getClassAvgRightRateByPcode(code, classId);
		DiagnosticStudentClassKnowpoint dc = diagStuKpService.get(code, userId, classId);
		KnowledgeSystem ks = knowledgeSystemService.get(code);
		KnowledgeSystem ks_parent = knowledgeSystemService.get(ks.getPcode());
		if (isShowAllKp) {
			// 查询当前知识专项下面全部的知识点
			List<KnowledgePoint> kpList = kpService.findByPcode(code);
			List<VDiagnosticStudentClassKnowpoint> vDiagKpList = new ArrayList<VDiagnosticStudentClassKnowpoint>();
			for (KnowledgePoint k : kpList) {
				boolean flag = false;
				for (DiagnosticStudentClassKnowpoint dk : knowlist) {
					if (k.getCode() == dk.getKnowpointCode()) {
						vDiagKpList.add(diagStuKpConvert.to(dk));
						flag = true;
					}
				}
				if (!flag) {
					VDiagnosticStudentClassKnowpoint v = new VDiagnosticStudentClassKnowpoint();
					v.setDoCount(0);
					v.setKnowpointCode(k.getCode());
					v.setKnowpointName(k.getName());
					v.setHasData(false);
					vDiagKpList.add(v);
				}
			}
			map.put("knowlist", vDiagKpList);
		} else {
			map.put("knowlist", diagStuKpConvert.to(knowlist));
		}
		map.put("classRightRateList", classRightRateList);
		if (dc != null) {
			map.put("dc", diagStuKpConvert.to(dc));
		} else {
			map.put("dc", ksConvert.to(ks));
		}

		// 对应的小专题名称
		map.put("smallName", ks_parent.getName());
		return new Value(map);
	}

	/**
	 * 历史问题最多的知识点
	 * 
	 * @param classId
	 * @param userId
	 * @param day0
	 *            0-全部,7-最近一周,30-最近一个月,90-最近3个月
	 * @return
	 */
	@MemberAllowed(memberType = "VIP")
	@RequestMapping(value = "queryHistoryWeakList", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryHistoryWeakList(Long classId, Long userId, Integer day0) {
		if (userId == null) {
			userId = Security.getUserId();
		}
		List<DiagnosticStudentClassKnowpoint> weakList = diagStuKpService.queryHistoryWeakList(userId, classId, day0);
		return new Value(diagStuKpConvert.to(weakList));
	}
}

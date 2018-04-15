package com.lanking.uxb.service.web.resource;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassLatestHomework;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassLatestHomeworkKnowpoint;
import com.lanking.cloud.domain.yoomath.stat.StudentHomeworkStat;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.diagnostic.api.DiagnosticStudentClassLatestHomeworkKnowpointService;
import com.lanking.uxb.service.diagnostic.api.DiagnosticStudentClassLatestHomeworkService;
import com.lanking.uxb.service.diagnostic.convert.DiagnosticStudentClassLatestHomeworkConvert;
import com.lanking.uxb.service.diagnostic.convert.DiagnosticStudentClassLatestHomeworkKnowpointConvert;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.zuoye.api.ZyHkStuClazzKnowpointStatService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkStatService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClassConvertOption;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkStudentClazzConvert;
import com.lanking.uxb.service.zuoye.convert.ZyStudentHomeworkStatConvert;
import com.lanking.uxb.service.zuoye.value.VHkStuClazzKnowpointStat;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;
import com.lanking.uxb.service.zuoye.value.VHomeworkStudentClazz;
import com.lanking.uxb.service.zuoye.value.VStudentHomeworkStat;

/**
 * 学生的统计相关接口
 * 
 * @since yoomath V1.4
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年9月21日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/s/statistic")
public class ZyStuStatisticController {

	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private ZyHomeworkClazzConvert zyHkClassConvert;
	@Autowired
	private ZyHomeworkStudentClazzService zyHkStuClazzService;
	@Autowired
	private ZyHomeworkStudentClazzConvert zyHkStuClazzConvert;
	@Autowired
	private ZyStudentHomeworkStatService zyStuHkStatService;
	@Autowired
	private ZyStudentHomeworkStatConvert zyStuHkStatConvert;
	@Autowired
	private ZyStudentHomeworkService zyStuHkService;
	@Autowired
	private ZyHkStuClazzKnowpointStatService zyHkStuClazzKnowpointStatService;
	@Autowired
	private ZyHomeworkQuestionService zyHomeworkQuestionService;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private DiagnosticStudentClassLatestHomeworkKnowpointService diagStuLatestHkKpService;
	@Autowired
	private DiagnosticStudentClassLatestHomeworkKnowpointConvert diagStuLatestHkKpConvert;
	@Autowired
	private DiagnosticStudentClassLatestHomeworkService diagStuLatestHkService;
	@Autowired
	private DiagnosticStudentClassLatestHomeworkConvert diagStuLatestHkConvert;

	/**
	 * 学生查看班级统计(学生首页、我的班级、查看作业的总体统计使用)
	 * 
	 * @since yoomathV1.4
	 * @param classId
	 *            班级ID
	 * @param needClassList
	 *            是否需要返回班级列表
	 * @param one
	 *            是否为一个班级（此时needClassList失效）
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@RequestMapping(value = "clazz", method = { RequestMethod.POST, RequestMethod.GET })
	public Value statisticClazz(@RequestParam(required = false) Long classId,
			@RequestParam(defaultValue = "false") boolean needClassList,
			@RequestParam(defaultValue = "false") boolean one, @RequestParam(required = false) Long studentId) {
		Map<String, Object> data = new HashMap<String, Object>(5);
		VUser user = userConvert.get(Security.getUserId());
		data.put("isChannelUser", user.isChannelUser());
		if (needClassList && !one) {
			List<HomeworkStudentClazz> studentClazzs = zyHkStuClazzService.listCurrentClazzs(Security.getUserId());
			if (CollectionUtils.isNotEmpty(studentClazzs)) {
				List<VHomeworkStudentClazz> vstuClazzs = zyHkStuClazzConvert.to(studentClazzs);
				// 封装班级信息
				List<Long> classIds = new ArrayList<Long>(studentClazzs.size());
				for (VHomeworkStudentClazz v : vstuClazzs) {
					classIds.add(v.getClassId());
				}
				Map<Long, HomeworkClazz> clazzs = zyHkClassService.mget(classIds);
				Map<Long, VHomeworkClazz> vclazzs = zyHkClassConvert.to(clazzs,
						new ZyHomeworkClassConvertOption(true, false, false));
				for (VHomeworkStudentClazz v : vstuClazzs) {
					v.setHomeworkClazz(vclazzs.get(v.getClassId()));
				}
				// 封装学生作业统计信息
				List<StudentHomeworkStat> stats = zyStuHkStatService.getByHomeworkClassIds(Security.getUserId(),
						classIds);
				List<VStudentHomeworkStat> vstats = zyStuHkStatConvert.to(stats);
				Map<Long, VStudentHomeworkStat> vstatsMap = new HashMap<Long, VStudentHomeworkStat>(vstats.size());
				for (VStudentHomeworkStat v : vstats) {
					vstatsMap.put(v.getHomeworkClassId(), v);
				}
				for (VHomeworkStudentClazz v : vstuClazzs) {
					VStudentHomeworkStat vstuStat = vstatsMap.get(v.getClassId());
					if (vstuStat == null) {
						vstuStat = new VStudentHomeworkStat();
						vstuStat.setHomeworkClassId(v.getClassId());
						vstuStat.setUserId(Security.getUserId());
						vstuStat.setRightRate(null);
					}
					v.setHomeworkStat(vstuStat);
				}
				data.put("clazzs", vstuClazzs);
				if (classId == null) {
					classId = vstuClazzs.get(0).getClassId();
				}
			}
			// 用于判断该学生是否从未加入过班级
			data.put("clazzCount", zyHkStuClazzService.countStudentClazz(Security.getUserId(), null));
		}
		if (classId != null) {
			// 字段为stu_right_rate class_right_rate homework_name homework_id
			List<Map> statisticList = zyStuHkService.statisticLatestHomework(classId,
					studentId == null ? Security.getUserId() : studentId, 20);
			for (Map map : statisticList) {
				map.put("student_homework_id", ((BigInteger) map.get("student_homework_id")).longValue());
			}
			data.put("statistic", statisticList);
			if (one) {
				HomeworkStudentClazz studentClazz = zyHkStuClazzService.findAll(classId,
						studentId == null ? Security.getUserId() : studentId);
				if (studentClazz != null) {
					VHomeworkStudentClazz vStudentClazz = zyHkStuClazzConvert.to(studentClazz);
					vStudentClazz
							.setHomeworkClazz(zyHkClassConvert.to(zyHkClassService.get(vStudentClazz.getClassId())));
					StudentHomeworkStat homeworkStat = zyStuHkStatService.getByHomeworkClassId(
							studentId == null ? Security.getUserId() : studentId, vStudentClazz.getClassId());
					if (homeworkStat == null) {
						VStudentHomeworkStat vStudentHomeworkStat = new VStudentHomeworkStat();
						vStudentHomeworkStat.setHomeworkClassId(vStudentClazz.getClassId());
						vStudentHomeworkStat.setUserId(Security.getUserId());
						vStudentClazz.setHomeworkStat(vStudentHomeworkStat);
					} else {
						vStudentClazz.setHomeworkStat(zyStuHkStatConvert.to(homeworkStat));
					}
					data.put("clazz", vStudentClazz);
				}

			}
		}
		// 获取班级知识点统计
		List<Map> knowpointStat = zyHkStuClazzKnowpointStatService.getStuLowKnowpointStat(Security.getUserId(),
				classId);
		data.put("knowpointStat", knowpointStat);
		return new Value(data);
	}

	/**
	 * 学生查看班级统计(学生首页、我的班级、查看作业的总体统计使用)
	 * 
	 * @version 20170426 senhao.wang
	 * @param classId
	 *            班级ID
	 * @param needClassList
	 *            是否需要返回班级列表
	 * @param one
	 *            是否为一个班级（此时needClassList失效）
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@RequestMapping(value = "clazz2", method = { RequestMethod.POST, RequestMethod.GET })
	public Value statisticClazz2(@RequestParam(required = false) Long classId,
			@RequestParam(defaultValue = "false") boolean needClassList,
			@RequestParam(defaultValue = "false") boolean one, @RequestParam(required = false) Long studentId,
			@RequestParam(defaultValue = "1") Integer version) {
		Map<String, Object> data = new HashMap<String, Object>(5);
		VUser user = userConvert.get(Security.getUserId());
		data.put("isChannelUser", user.isChannelUser());
		if (needClassList && !one) {
			List<HomeworkStudentClazz> studentClazzs = zyHkStuClazzService.listCurrentClazzs(Security.getUserId());
			if (CollectionUtils.isNotEmpty(studentClazzs)) {
				List<VHomeworkStudentClazz> vstuClazzs = zyHkStuClazzConvert.to(studentClazzs);
				// 封装班级信息
				List<Long> classIds = new ArrayList<Long>(studentClazzs.size());
				for (VHomeworkStudentClazz v : vstuClazzs) {
					classIds.add(v.getClassId());
				}
				Map<Long, HomeworkClazz> clazzs = zyHkClassService.mget(classIds);
				Map<Long, VHomeworkClazz> vclazzs = zyHkClassConvert.to(clazzs,
						new ZyHomeworkClassConvertOption(true, false, false));
				for (VHomeworkStudentClazz v : vstuClazzs) {
					v.setHomeworkClazz(vclazzs.get(v.getClassId()));
				}
				// 封装学生作业统计信息
				List<StudentHomeworkStat> stats = zyStuHkStatService.getByHomeworkClassIds(Security.getUserId(),
						classIds);
				List<VStudentHomeworkStat> vstats = zyStuHkStatConvert.to(stats);
				Map<Long, VStudentHomeworkStat> vstatsMap = new HashMap<Long, VStudentHomeworkStat>(vstats.size());
				for (VStudentHomeworkStat v : vstats) {
					vstatsMap.put(v.getHomeworkClassId(), v);
				}
				for (VHomeworkStudentClazz v : vstuClazzs) {
					VStudentHomeworkStat vstuStat = vstatsMap.get(v.getClassId());
					if (vstuStat == null) {
						vstuStat = new VStudentHomeworkStat();
						vstuStat.setHomeworkClassId(v.getClassId());
						vstuStat.setUserId(Security.getUserId());
						vstuStat.setRightRate(null);
					}
					v.setHomeworkStat(vstuStat);
				}
				data.put("clazzs", vstuClazzs);
				if (classId == null) {
					classId = vstuClazzs.get(0).getClassId();
				}
			}
			// 用于判断该学生是否从未加入过班级
			data.put("clazzCount", zyHkStuClazzService.countStudentClazz(Security.getUserId(), null));
		}
		if (classId != null) {
			List<DiagnosticStudentClassLatestHomework> list = diagStuLatestHkService.queryStat(Security.getUserId(),
					classId, 30);
			data.put("statistic", diagStuLatestHkConvert.to(list));
			if (one) {
				HomeworkStudentClazz studentClazz = zyHkStuClazzService.findAll(classId,
						studentId == null ? Security.getUserId() : studentId);
				if (studentClazz != null) {
					VHomeworkStudentClazz vStudentClazz = zyHkStuClazzConvert.to(studentClazz);
					vStudentClazz
							.setHomeworkClazz(zyHkClassConvert.to(zyHkClassService.get(vStudentClazz.getClassId())));
					StudentHomeworkStat homeworkStat = zyStuHkStatService.getByHomeworkClassId(
							studentId == null ? Security.getUserId() : studentId, vStudentClazz.getClassId());
					if (homeworkStat == null) {
						VStudentHomeworkStat vStudentHomeworkStat = new VStudentHomeworkStat();
						vStudentHomeworkStat.setHomeworkClassId(vStudentClazz.getClassId());
						vStudentHomeworkStat.setUserId(Security.getUserId());
						vStudentClazz.setHomeworkStat(vStudentHomeworkStat);
					} else {
						vStudentClazz.setHomeworkStat(zyStuHkStatConvert.to(homeworkStat));
					}
					data.put("clazz", vStudentClazz);
				}

			}
		}
		// 获取班级知识点统计
		List<DiagnosticStudentClassLatestHomeworkKnowpoint> weakList = diagStuLatestHkKpService
				.queryWeakList(Security.getUserId(), classId, 30, 6);
		data.put("knowpointStat", diagStuLatestHkKpConvert.to(weakList));
		return new Value(data);
	}

	/**
	 * 学生知识点(总体统计使用)
	 * 
	 * @since yoomath V1.4
	 * @param classId
	 *            班级ID
	 * @param studentId
	 *            学生ID(老师查看学生)
	 * @param version
	 *            区分新旧知识点不同版本
	 * @return {@link Value}
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "getStuKnowpointStat", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getStuKnowpointStat(Long classId, @RequestParam(required = false) Long studentId) {
		List<Map> dataList = zyHkStuClazzKnowpointStatService
				.getStuKnowpointStat(studentId == null ? Security.getUserId() : studentId, classId);
		List<VHkStuClazzKnowpointStat> list = new ArrayList<VHkStuClazzKnowpointStat>();
		for (Map map : dataList) {
			VHkStuClazzKnowpointStat vks = new VHkStuClazzKnowpointStat();
			vks.setClassRightRate(Long.parseLong(String.valueOf(map.get("classrate"))));
			vks.setStuRightRate(Long.parseLong(String.valueOf(map.get("right_rate"))));
			vks.setKnowPointName(String.valueOf(map.get("name")));
			vks.setStuRank(Integer.parseInt(String.valueOf(map.get("rank"))));
			vks.setWrongNum(Long.parseLong(String.valueOf(map.get("wrong_num"))));
			vks.setTotal(Long.parseLong(String.valueOf(map.get("wrong_num")))
					+ Long.parseLong(String.valueOf(map.get("right_num"))));
			vks.setCode(Long.parseLong(String.valueOf(map.get("knowpoint_code"))));
			list.add(vks);
		}
		return new Value(list);
	}

	/**
	 * 获取班级每个习题的正确率情况
	 * 
	 * @param homeworkId
	 * @return
	 */
	@RequestMapping(value = "getClazzQuestionStat", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getClazzQuestionStat(Long homeworkId) {
		List<Double> list = zyHomeworkQuestionService.getHkQuestion(homeworkId);
		return new Value(list);
	}
}

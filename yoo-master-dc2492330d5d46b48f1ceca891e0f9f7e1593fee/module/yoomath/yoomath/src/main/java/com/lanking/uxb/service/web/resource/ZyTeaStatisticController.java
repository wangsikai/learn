package com.lanking.uxb.service.web.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassKnowpoint;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.MetaKnowSectionService;
import com.lanking.uxb.service.code.api.MetaKnowpointService;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.KnowledgePointConvert;
import com.lanking.uxb.service.code.convert.MetaKnowpointConvert;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.diagnostic.api.DiagnosticClassKnowpointService;
import com.lanking.uxb.service.diagnostic.convert.DiagnosticClassKnowpointConvert;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.convert.HomeworkConvert;
import com.lanking.uxb.service.resources.convert.HomeworkConvertOption;
import com.lanking.uxb.service.resources.convert.HomeworkQuestionConvert;
import com.lanking.uxb.service.resources.value.VHomework;
import com.lanking.uxb.service.resources.value.VHomeworkQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzStatService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClassConvertOption;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkStudentClazzStatConvert;
import com.lanking.uxb.service.zuoye.convert.ZyStudentExerciseKnowpointConvert;
import com.lanking.uxb.service.zuoye.convert.ZyStudentExerciseSectionConvert;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;
import com.lanking.uxb.service.zuoye.value.VHomeworkStudentClazzStat;
import com.lanking.uxb.service.zuoye.value.VStudentExerciseKnowpoint;
import com.lanking.uxb.service.zuoye.value.VStudentExerciseSection;

/**
 * 老师的统计相关接口
 * 
 * @since yoomath V1.4
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年9月21日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/t/statistic")
public class ZyTeaStatisticController {

	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private ZyHomeworkClazzConvert zyHkClassConvert;
	@Autowired
	private HomeworkConvert homeworkConvert;
	@Autowired
	private ZyHomeworkService homeworkService;
	@Autowired
	private HomeworkQuestionConvert hqConvert;
	@Autowired
	private ZyHomeworkQuestionService hkQuestionService;
	@Autowired
	private ZyHomeworkStudentClazzStatService zyHomeworkStudentClazzStatService;
	@Autowired
	private ZyHomeworkStudentClazzStatConvert zyHomeworkStudentClazzStatConvert;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private ZyStudentExerciseSectionConvert studentExerciseSectionConvert;
	@Autowired
	private MetaKnowSectionService metaKnowSectionService;
	@Autowired
	private MetaKnowpointService metaKnowpointService;
	@Autowired
	private MetaKnowpointConvert metaKnowpointConvert;
	@Autowired
	private ZyStudentExerciseKnowpointConvert studentExerciseKnowpointConvert;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private TextbookService tbService;
	@Autowired
	private TextbookConvert tbConvert;
	@Autowired
	private TextbookCategoryService tbCateService;
	@Autowired
	private TextbookCategoryConvert tbCateConvert;
	@Autowired
	private KnowledgePointConvert kpConvert;
	@Autowired
	private DiagnosticClassKnowpointService diagKpService;
	@Autowired
	private HomeworkService hkService;
	@Autowired
	private DiagnosticClassKnowpointConvert diagKpConvert;

	/**
	 * 教师查看班级统计(教师首页和班级管理使用)
	 * 
	 * @since yoomath V1.4
	 * @param classId
	 *            班级ID
	 * @return {@link Value}
	 */
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "clazz", method = { RequestMethod.POST, RequestMethod.GET })
	public Value statisticClazz(@RequestParam(required = false) Long classId,
			@RequestParam(value = "history", defaultValue = "false") boolean history,
			@RequestParam(value = "topStudentSize", defaultValue = "5") int size) {
		size = Math.min(size, 10);
		Map<String, Object> data = new HashMap<String, Object>(2);
		List<HomeworkClazz> list = Lists.newArrayList();

		HomeworkClazz homeworkClazz = null;
		if (classId != null && classId > 0) {
			homeworkClazz = zyHkClassService.get(classId);
		}

		if (history || (homeworkClazz != null && homeworkClazz.getStatus() == Status.DISABLED)) {
			list = zyHkClassService.listHistoryClazzs(Security.getUserId());
		} else {
			list = zyHkClassService.listCurrentClazzs(Security.getUserId());
		}

		List<VHomeworkClazz> vs = null;
		if (CollectionUtils.isEmpty(list)) {
			vs = Collections.EMPTY_LIST;
		} else {
			vs = zyHkClassConvert.to(list, new ZyHomeworkClassConvertOption(false, true, true));
			if (classId != null && classId > 0) {
				List<VHomework> latestHomework = homeworkConvert.to(homeworkService.getLatestIssuedHomeWorks(classId,
						20));
				data.put("latestHomework", latestHomework);
				List<VHomeworkStudentClazzStat> vhks = zyHomeworkStudentClazzStatConvert
						.to(zyHomeworkStudentClazzStatService.findTopStudent(classId, size));
				data.put("topStudents", vhks);
			} else {
				List<VHomework> latestHomework = homeworkConvert.to(homeworkService.getLatestIssuedHomeWorks(list
						.get(0).getId(), 20));
				data.put("latestHomework", latestHomework);
				List<VHomeworkStudentClazzStat> vhks = zyHomeworkStudentClazzStatConvert
						.to(zyHomeworkStudentClazzStatService.findTopStudent(list.get(0).getId(), size));
				data.put("topStudents", vhks);
			}
		}
		data.put("clazzs", vs);
		return new Value(data);
	}

	/**
	 * 统计一次布置的作业结果
	 * 
	 * @since yoomath V1.4
	 * @param exerciseId
	 *            习题ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "oneHomework", method = { RequestMethod.POST, RequestMethod.GET })
	public Value statisticOneHomework(long exerciseId) {
		List<Homework> homeworks = homeworkService.findByExercise(Security.getUserId(), Lists.newArrayList(exerciseId));
		List<Homework> ps = new ArrayList<Homework>(homeworks.size());
		for (Homework homework : homeworks) {
			if (homework.getStatus() == HomeworkStatus.ISSUED) {
				ps.add(homework);
			}
		}
		List<VHomework> vs = homeworkConvert.to(ps, new HomeworkConvertOption(false));
		if (CollectionUtils.isNotEmpty(vs)) {
			List<Long> hkIds = new ArrayList<Long>(vs.size());
			List<Long> classIds = new ArrayList<Long>(vs.size());
			for (VHomework v : vs) {
				hkIds.add(v.getId());
				classIds.add(v.getHomeworkClazzId());
			}
			Map<Long, VHomeworkClazz> vmap = zyHkClassConvert.to(zyHkClassService.mget(classIds));
			for (VHomework v : vs) {
				v.setHomeworkClazz(vmap.get(v.getHomeworkClazzId()));
			}
			Map<Long, List<VHomeworkQuestion>> map = new HashMap<Long, List<VHomeworkQuestion>>(hkIds.size());
			for (Long hkId : hkIds) {
				map.put(hkId, new ArrayList<VHomeworkQuestion>());
			}
			List<VHomeworkQuestion> homeworkQuestions = hqConvert.to(hkQuestionService.findByHomework(hkIds));
			for (VHomeworkQuestion v : homeworkQuestions) {
				map.get(v.getHomeworkId()).add(v);
			}
			for (VHomework v : vs) {
				v.setHomeworkQuestions(map.get(v.getId()));
			}
		}
		return new Value(vs);
	}

	/**
	 * 统计一次布置的作业结果 <br>
	 * 2016/12/20 wangsenhao
	 * 
	 * @param exerciseId
	 *            习题ID
	 * @param homeworkId
	 *            作业ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "oneHomework2", method = { RequestMethod.POST, RequestMethod.GET })
	public Value statisticOneHomework2(long exerciseId, Long homeworkId) {
		List<Homework> homeworks = homeworkService.findByExercise(Security.getUserId(), Lists.newArrayList(exerciseId));
		List<Homework> ps = new ArrayList<Homework>(homeworks.size());
		for (Homework homework : homeworks) {
			if (homework.isAllCorrectComplete()) {
				ps.add(homework);
			}
		}
		List<VHomework> vs = homeworkConvert.to(ps, new HomeworkConvertOption(false));
		Map<String, Object> data = new HashMap<String, Object>(2);
		if (CollectionUtils.isNotEmpty(vs)) {
			List<Long> hkIds = new ArrayList<Long>(vs.size());
			List<Long> classIds = new ArrayList<Long>(vs.size());
			for (VHomework v : vs) {
				hkIds.add(v.getId());
				classIds.add(v.getHomeworkClazzId());
			}
			Map<Long, VHomeworkClazz> vmap = zyHkClassConvert.to(zyHkClassService.mget(classIds));
			for (VHomework v : vs) {
				v.setHomeworkClazz(vmap.get(v.getHomeworkClazzId()));
			}
			Map<Long, List<VHomeworkQuestion>> map = new HashMap<Long, List<VHomeworkQuestion>>(hkIds.size());
			for (Long hkId : hkIds) {
				map.put(hkId, new ArrayList<VHomeworkQuestion>());
			}
			List<VHomeworkQuestion> homeworkQuestions = hqConvert.to(hkQuestionService.findByHomework(hkIds));
			for (VHomeworkQuestion v : homeworkQuestions) {
				map.get(v.getHomeworkId()).add(v);
			}
			for (VHomework v : vs) {
				v.setHomeworkQuestions(map.get(v.getId()));
			}
			if (homeworkId != null) {
				Homework homework = hkService.get(homeworkId);
				// 查询薄弱知识点
				List<Long> knowledgePoints = homeworks.get(0).getKnowledgePoints();
				if (CollectionUtils.isNotEmpty(knowledgePoints)) {
					// 包含的知识点是当前班级薄弱知识点的查询出来
					List<DiagnosticClassKnowpoint> diagList = diagKpService.findWeakDatasByKps(
							homework.getHomeworkClassId(), knowledgePoints);
					data.put("weakPoints", diagKpConvert.to(diagList));

				} else {
					// 如果新知识点为空，查看旧知识点是否为空,说明新知识上线前下发的作业
					List<Long> oldKps = homeworks.get(0).getMetaKnowpoints();
					if (CollectionUtils.isNotEmpty(oldKps)) {
						List<Integer> oldWeakPoints = homeworkService.queryWeakByOldCodes(homeworkId, oldKps);
						if (CollectionUtils.isNotEmpty(oldWeakPoints)) {
							data.put("weakPoints",
									metaKnowpointConvert.to(metaKnowpointService.mgetList(oldWeakPoints)));
						}
					}
				}
			}
			data.put("vs", vs);
		}
		return new Value(data);
	}

	/**
	 * 得到班级下的学生章节掌握情况
	 *
	 * @param classId
	 *            班级id
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "sectionComplete", method = { RequestMethod.GET, RequestMethod.POST })
	public Value sectionComplete(long classId, int textbookCode) {
		List<VSection> sections = sectionConvert.to(sectionService.findByTextbookCode(textbookCode));
		List<VStudentExerciseSection> vs = studentExerciseSectionConvert.to(sections);
		studentExerciseSectionConvert.statisticsByClassId(vs, classId, null);

		return new Value(studentExerciseSectionConvert.assembleTree(vs));
	}

	/**
	 * 知识点掌握情况
	 *
	 * @param sectionCodes
	 *            章节码列表
	 * @param classId
	 *            班级id
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "knowpointComplete", method = { RequestMethod.GET, RequestMethod.POST })
	public Value knowpointComplete(@RequestParam(value = "sectionCodes") List<Long> sectionCodes,
			@RequestParam(value = "classId") long classId) {
		List<Integer> metaCodes = metaKnowSectionService.findBySectionCodes(sectionCodes);

		if (CollectionUtils.isEmpty(metaCodes)) {
			return new Value();
		}

		List<MetaKnowpoint> points = metaKnowpointService.mgetList(metaCodes);
		List<VStudentExerciseKnowpoint> vs = studentExerciseKnowpointConvert.to(points);

		studentExerciseKnowpointConvert.assembleDataByClassId(metaCodes, vs, classId);

		return new Value(vs);
	}

	/**
	 * 获得教师的教材版本
	 *
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "teacherCate", method = { RequestMethod.GET, RequestMethod.POST })
	public Value teacherCate() {
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		Map<String, Object> dataMap = new HashMap<String, Object>(3);
		if (teacher.getTextbookCategoryCode() != null) {
			dataMap.put("textbookCategory", tbCateConvert.to(tbCateService.get(teacher.getTextbookCategoryCode())));
			dataMap.put(
					"textbooks",
					tbConvert.to(tbService.find(teacher.getPhaseCode(), teacher.getTextbookCategoryCode(),
							teacher.getSubjectCode())));
			dataMap.put("subjectCode", teacher.getSubjectCode());
		}

		return new Value(dataMap);
	}
}

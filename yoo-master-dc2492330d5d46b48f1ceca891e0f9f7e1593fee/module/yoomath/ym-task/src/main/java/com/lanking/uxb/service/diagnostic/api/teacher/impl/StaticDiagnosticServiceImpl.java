package com.lanking.uxb.service.diagnostic.api.teacher.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.lanking.cloud.component.mq.common.constants.MqYoomathDiagnoRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.KnowledgeSectionService;
import com.lanking.uxb.service.code.api.QuestionKnowledgeService;
import com.lanking.uxb.service.diagnostic.api.StaticHomeworkClassService;
import com.lanking.uxb.service.diagnostic.api.StaticHomeworkService;
import com.lanking.uxb.service.diagnostic.api.StaticHomeworkStudentClazzService;
import com.lanking.uxb.service.diagnostic.api.StaticTeacherService;
import com.lanking.uxb.service.diagnostic.api.teacher.StaticDiagnosticService;
import com.lanking.uxb.service.diagnostic.api.teacher.TaskDiagnosticClassKnowpointService;
import com.lanking.uxb.service.diagnostic.api.teacher.TaskDiagnosticClassLatestHomeworkKnowpointService;
import com.lanking.uxb.service.diagnostic.api.teacher.TaskDiagnosticClassLatestHomeworkService;
import com.lanking.uxb.service.diagnostic.api.teacher.TaskDiagnosticClassService;

/**
 * 教学诊断 - 统计服务实现.
 * 
 * @author wlche
 * 
 * @since 教师端 v1.3.0，2017-6-29 数据跟随相关整理
 *
 */
@Service
public class StaticDiagnosticServiceImpl implements StaticDiagnosticService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TaskDiagnosticClassLatestHomeworkService diagnosticClassLatestHomeworkService;

	@Autowired
	private TaskDiagnosticClassLatestHomeworkKnowpointService diagnosticClassLatestHomeworkKnowpointService;

	@Autowired
	private TaskDiagnosticClassKnowpointService diagnosticClassKnowpointService;

	@Autowired
	private StaticHomeworkService homeworkService;

	@Autowired
	private QuestionKnowledgeService questionKnowledgeService;

	@Autowired
	private StaticTeacherService teacherService;

	@Autowired
	private KnowledgeSectionService knowledgeSectionService;

	@Autowired
	private TaskDiagnosticClassService diagnosticClassService;

	@Autowired
	private StaticHomeworkClassService homeworkClassService;

	@Autowired
	private StaticHomeworkStudentClazzService homeworkStudentClazzService;

	@Autowired
	private MqSender mqSender;

	@SuppressWarnings("rawtypes")
	@Override
	public void initDiagnosticClass(HomeworkClazz homeworkClazz) throws ParseException {

		// 获取班级所有已下发作业数据
		List<Homework> hks = homeworkService.findAllByClassId(homeworkClazz.getId());
		if (CollectionUtils.isEmpty(hks)) {
			return;
		}

		List<Long> hkIds = new ArrayList<Long>(hks.size());
		for (Homework latestHomework : hks) {
			hkIds.add(latestHomework.getId());
		}

		// 作业题目数据
		List<Map> hkQuestions = homeworkService.findHomeworkQuestion(hkIds, null);
		if (CollectionUtils.isEmpty(hkQuestions)) {
			return;
		}

		this.staticDiagnosticClassIncrDatas(homeworkClazz, hkIds, hkQuestions, true, true);
	}

	/**
	 * 根据作业进行统计
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void staticDiagnosticByHomework(HomeworkClazz clazz, Homework homework) {

		// 当前班级教师
		Teacher teacher = teacherService.get(clazz.getTeacherId());

		// 获取班级最近30天的已下发作业
		// @since 小优快批，2018-3-9，变更为已批改完成的作业
		List<Homework> latestHks = homeworkService.getLatestHks(clazz.getId());

		if (CollectionUtils.isNotEmpty(latestHks)) {

			// 清空班级下的最近n次作业记录
			diagnosticClassLatestHomeworkService.deleteByClassId(clazz.getId());

			// 保存新的最近n次作业
			diagnosticClassLatestHomeworkService.saveDiagnosticClassLatestHomeworks(latestHks);

			// 最近30天作业ID集合
			List<Long> homeworkIds = new ArrayList<Long>();
			for (Homework latestHomework : latestHks) {
				homeworkIds.add(latestHomework.getId());
			}

			// 获取指定作业下的所有习题统计数据
			List<Map> hkQuestions = homeworkService.findHomeworkQuestion(homeworkIds, null);

			// 获取题目知识点对应关系
			Map<Long, Set<Integer>> knowledgeTextbookMap = new HashMap<Long, Set<Integer>>(); // 经过教师教材版本过滤后的知识点对应的教材
			Map<Long, List<Long>> questionKnowledgeMap = this.getQuestionKnowledgeMap(hkQuestions, teacher,
					knowledgeTextbookMap);

			if (CollectionUtils.isEmpty(questionKnowledgeMap)) {
				return;
			}

			// 获取最近7次、15次作业的题目答题情况
			List<Map> hkQuestions_15 = new ArrayList<Map>();
			List<Map> hkQuestions_7 = new ArrayList<Map>();
			List<Long> homeworkIds_15 = new ArrayList<Long>();
			List<Long> homeworkIds_7 = new ArrayList<Long>();
			homeworkIds_15 = homeworkIds.size() >= 15 ? homeworkIds.subList(0, 15) : homeworkIds;
			homeworkIds_7 = homeworkIds.size() >= 7 ? homeworkIds.subList(0, 7) : homeworkIds;
			for (Map map : hkQuestions) {
				Long hkId = Long.parseLong(String.valueOf(map.get("homework_id")));
				if (homeworkIds_15.contains(hkId)) {
					hkQuestions_15.add(map);
				}
				if (homeworkIds_7.contains(hkId)) {
					hkQuestions_7.add(map);
				}
			}

			// 清空班级下的最近n次作业薄弱知识点数据
			diagnosticClassLatestHomeworkKnowpointService.deleteByClassId(clazz.getId());

			// 统计最近7次、15次、30次作业知识点答题情况
			diagnosticClassLatestHomeworkKnowpointService.doKnowledgeStat(clazz.getId(), hkQuestions,
					questionKnowledgeMap, 30);
			diagnosticClassLatestHomeworkKnowpointService.doKnowledgeStat(clazz.getId(), hkQuestions_15,
					questionKnowledgeMap, 15);
			diagnosticClassLatestHomeworkKnowpointService.doKnowledgeStat(clazz.getId(), hkQuestions_7,
					questionKnowledgeMap, 7);

			if (homework != null) {
				List<Map> hkQuestion = homeworkService.findHomeworkQuestion(homework.getId());

				// 教学诊断，理班级相关增减量统计数据
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("classId", clazz.getId());
				jsonObject.put("incr", true);
				jsonObject.put("homeworkIds", Lists.newArrayList(homework.getId()));
				jsonObject.put("hkQuestion", hkQuestion);
				jsonObject.put("allStatic", true);
				mqSender.send(MqYoomathDiagnoRegistryConstants.EX_YM_DIAGNO_TASK,
						MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_CLASS_INCR,
						MQ.builder().data(jsonObject).build());
			}
		}
	}

	/**
	 * 根据学生进行统计
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public void staticDiagnosticByStudent(HomeworkClazz clazz, long studentId, boolean isJoin) {
		// 当前班级教师
		Teacher teacher = teacherService.get(clazz.getTeacherId());

		// 获取班级最近30天的已下发作业
		List<Homework> latestHks = homeworkService.getLatestHks(clazz.getId());

		if (CollectionUtils.isNotEmpty(latestHks)) {
			// 清空班级下的最近n次作业记录
			diagnosticClassLatestHomeworkService.deleteByClassId(clazz.getId());

			// 保存新的最近n次作业
			diagnosticClassLatestHomeworkService.saveDiagnosticClassLatestHomeworks(latestHks);

			// 最近30天作业ID集合
			List<Long> homeworkIds = new ArrayList<Long>();
			for (Homework latestHomework : latestHks) {
				homeworkIds.add(latestHomework.getId());
			}

			// 获取指定作业下的所有习题统计数据
			List<Map> hkQuestions = homeworkService.findHomeworkQuestion(homeworkIds, null);

			// 获取题目知识点对应关系
			Map<Long, Set<Integer>> knowledgeTextbookMap = new HashMap<Long, Set<Integer>>(); // 经过教师教材版本过滤后的知识点对应的教材
			Map<Long, List<Long>> questionKnowledgeMap = this.getQuestionKnowledgeMap(hkQuestions, teacher,
					knowledgeTextbookMap);

			// 清空班级下的最近n次作业薄弱知识点数据
			diagnosticClassLatestHomeworkKnowpointService.deleteByClassId(clazz.getId());

			if (CollectionUtils.isNotEmpty(questionKnowledgeMap)) {
				// 获取最近7次、15次作业的题目答题情况
				List<Map> hkQuestions_15 = new ArrayList<Map>();
				List<Map> hkQuestions_7 = new ArrayList<Map>();
				List<Long> homeworkIds_15 = new ArrayList<Long>();
				List<Long> homeworkIds_7 = new ArrayList<Long>();
				homeworkIds_15 = homeworkIds.size() >= 15 ? homeworkIds.subList(0, 15) : homeworkIds;
				homeworkIds_7 = homeworkIds.size() >= 7 ? homeworkIds.subList(0, 7) : homeworkIds;
				for (Map map : hkQuestions) {
					Long hkId = Long.parseLong(String.valueOf(map.get("homework_id")));
					if (homeworkIds_15.contains(hkId)) {
						hkQuestions_15.add(map);
					}
					if (homeworkIds_7.contains(hkId)) {
						hkQuestions_7.add(map);
					}
				}

				// 统计最近7次、15次、30次作业知识点答题情况
				diagnosticClassLatestHomeworkKnowpointService.doKnowledgeStat(clazz.getId(), hkQuestions,
						questionKnowledgeMap, 30);
				diagnosticClassLatestHomeworkKnowpointService.doKnowledgeStat(clazz.getId(), hkQuestions_15,
						questionKnowledgeMap, 15);
				diagnosticClassLatestHomeworkKnowpointService.doKnowledgeStat(clazz.getId(), hkQuestions_7,
						questionKnowledgeMap, 7);
			}
		}

		// 对于diagno_class以及diagno原增量数据需要单独处理

		Long queryClassId = clazz.getId();
		if (!isJoin) {
			// 如果是退出班级，需要判断该学生加入班级之前是否有带入数据，如果有，需要一并删除
			HomeworkStudentClazz homeworkStudentClazz = homeworkStudentClazzService.getByStudentExit(clazz.getId(),
					studentId);
			if (homeworkStudentClazz != null) {
				Date joinAt = homeworkStudentClazz.getJoinAt(); // 该学生当初加入的时间
				// 如果在这个加入时间以前本班级没有该学生作业数据
				boolean hasData = homeworkService.hasStudentHomework(clazz.getId(), studentId, joinAt);
				if (!hasData) {
					queryClassId = null;
				}
			}
		}

		int size = 5000;
		int pageNo = 1;
		Page<Map> page = homeworkService.findHomeworkQuestionByStudent(queryClassId, studentId,
				P.offset((pageNo - 1) * size, size));
		while (page.isNotEmpty()) {
			List<Map> hkQuestion = page.getItems();

			// 教学诊断，理班级相关增减量统计数据
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("classId", clazz.getId());
			jsonObject.put("incr", isJoin);
			jsonObject.put("allStatic", true);

			// 注意学生加入退出班级时 hkQuestion 已经限定了学生
			jsonObject.put("homeworkIds", Lists.newArrayList());
			jsonObject.put("hkQuestion", hkQuestion);
			mqSender.send(MqYoomathDiagnoRegistryConstants.EX_YM_DIAGNO_TASK,
					MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_CLASS_INCR,
					MQ.builder().data(jsonObject).build());

			pageNo = pageNo + 1;
			page = homeworkService.findHomeworkQuestionByStudent(queryClassId, studentId,
					P.offset((pageNo - 1) * size, size));
		}
	}

	/**
	 * 根据学生进行统计（学生加入班级）
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void staticDiagnosticByStudentOfEmpty(HomeworkClazz clazz, long studentId) {

		// 对于diagno_class以及diagno原增量数据需要单独处理
		int size = 5000;
		int pageNo = 1;

		// 取学生在所有已下发作业数据
		Page<Map> page = homeworkService.findHomeworkQuestionByStudent(null, studentId,
				P.offset((pageNo - 1) * size, size));
		while (page.isNotEmpty()) {
			List<Map> hkQuestion = page.getItems();

			// 教学诊断，理班级相关增减量统计数据
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("classId", clazz.getId());
			jsonObject.put("incr", true);
			jsonObject.put("allStatic", false);

			// 注意学生加入退出班级时 hkQuestion 已经限定了学生
			jsonObject.put("homeworkIds", Lists.newArrayList());
			jsonObject.put("hkQuestion", hkQuestion);
			mqSender.send(MqYoomathDiagnoRegistryConstants.EX_YM_DIAGNO_TASK,
					MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_CLASS_INCR,
					MQ.builder().data(jsonObject).build());

			pageNo = pageNo + 1;
			page = homeworkService.findHomeworkQuestionByStudent(clazz.getId(), studentId,
					P.offset((pageNo - 1) * size, size));
		}
	}

	/**
	 * 获取题目与知识点的对应关系.
	 * 
	 * @param hkQuestions
	 *            习题统计数据集合.
	 * @param teacher
	 *            老师
	 * @param knowledgeTextbookMap
	 *            经过教师教材版本过滤后的知识点对应的教材，需要填充则传入空集合
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Map<Long, List<Long>> getQuestionKnowledgeMap(List<Map> hkQuestions, Teacher teacher,
			Map<Long, Set<Integer>> knowledgeTextbookMap) {
		if (CollectionUtils.isEmpty(hkQuestions)) {
			return Maps.newHashMap();
		}
		Set<Long> questionIds = new HashSet<Long>(hkQuestions.size());
		for (Map map : hkQuestions) {
			Long questionId = Long.parseLong(map.get("question_id").toString());
			questionIds.add(questionId);
		}
		Map<Long, List<Long>> questionKnowledgeMap = questionKnowledgeService.mgetByQuestions(questionIds);

		// 过滤知识点，不在老师当前教材下的知识点不统计
		if (teacher.getTextbookCategoryCode() != null) {
			Set<Long> allKnowledgeCodes = new HashSet<Long>();
			for (List<Long> codes : questionKnowledgeMap.values()) {
				allKnowledgeCodes.addAll(codes);
			}

			// 全部知识点对应的教材
			knowledgeTextbookMap.putAll(knowledgeSectionService.queryTextbookByKnowledgeRelation(allKnowledgeCodes));

			for (Long questionId : questionIds) {
				List<Long> codes = questionKnowledgeMap.get(questionId);
				if (codes == null) {
					continue;
				}
				for (int i = codes.size() - 1; i >= 0; i--) {
					Set<Integer> textbookCodes = knowledgeTextbookMap.get(codes.get(i));
					if (CollectionUtils.isEmpty(textbookCodes)) {
						knowledgeTextbookMap.remove(codes.get(i));
						codes.remove(i);
						continue;
					}
					Set<Integer> newTextbookCodes = new HashSet<Integer>();
					for (Integer textbookCode : textbookCodes) {
						if (teacher.getTextbookCategoryCode() == (int) (textbookCode / 1000000)) {
							newTextbookCodes.add(textbookCode);
						}
					}
					if (newTextbookCodes.size() > 0) {
						knowledgeTextbookMap.put(codes.get(i), newTextbookCodes);
					} else {
						knowledgeTextbookMap.remove(codes.get(i));
						codes.remove(i);
					}
				}
				if (codes.size() == 0) {
					questionKnowledgeMap.remove(questionId);
				}
			}
		}
		return questionKnowledgeMap;
	}

	/**
	 * 处理班级增减量数据统计.
	 * 
	 * @param clazz
	 *            班级
	 * @param hkIds
	 *            该班级作业ID集合
	 * @param hkQuestion
	 *            习题相关数据
	 * @param incr
	 *            是否为增量
	 * @param allStatic
	 *            是否需要统计所有学生
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void staticDiagnosticClassIncrDatas(HomeworkClazz clazz, List<Long> hkIds, List<Map> hkQuestion,
			boolean incr, boolean allStatic) {

		logger.info("[staticDiagnosticClassIncrDatas] -> clazz=" + clazz.getId() + ", incr=" + incr + ", allStatic="
				+ allStatic);

		if (allStatic) {
			// 班级知识点统计时，需要对学生所在的所有班级都进行统计
			Map<Long, List<Long>> classIdMap = homeworkClassService.findAllByOneClassStudent(clazz.getId());
			Map<Long, HomeworkClazz> clazzs = homeworkClassService.mget(classIdMap.keySet());

			if (clazzs.get(clazz.getId()) == null) {
				// 说明是学生离开了某个班级

				// 当前班级教师
				Teacher teacher = teacherService.get(clazz.getTeacherId());

				// 获取题目知识点对应关系
				Map<Long, Set<Integer>> knowledgeTextbookMap = new HashMap<Long, Set<Integer>>(); // 经过教师教材版本过滤后的知识点对应的教材
				Map<Long, List<Long>> questionKnowledgeMap = this.getQuestionKnowledgeMap(hkQuestion, teacher,
						knowledgeTextbookMap);

				if (CollectionUtils.isEmpty(questionKnowledgeMap)) {
					return;
				}

				// 统计班级知识点答题情况
				diagnosticClassKnowpointService.doKnowledgeStat(clazz.getId(), hkQuestion, questionKnowledgeMap, !incr);

				// 统计班级教材答题情况
				if (teacher.getTextbookCategoryCode() != null) {
					diagnosticClassService.doDiagnosticClassStat(hkQuestion, teacher.getTextbookCategoryCode(),
							questionKnowledgeMap, knowledgeTextbookMap, clazz.getId(), !incr);

					diagnosticClassService.doClassTextbookStat(clazz.getId());
				}
			} else {
				for (Long clazzId : classIdMap.keySet()) {
					HomeworkClazz homeworkClazz = clazzs.get(clazzId);
					if (homeworkClazz == null) {
						continue;
					}

					// 当前班级教师
					Teacher teacher = teacherService.get(homeworkClazz.getTeacherId());

					// 如果不是本班，并且来自于下发作业，需要根据不同的班级取共同学生的那部分作业习题数据
					List<Map> tempHkQuestion = null;
					if (clazzId != clazz.getId().longValue() && CollectionUtils.isNotEmpty(hkIds)) {
						List<Long> studentIds = classIdMap.get(clazzId);
						tempHkQuestion = homeworkService.findHomeworkQuestion(hkIds, studentIds);
					} else {
						tempHkQuestion = hkQuestion;
					}

					// 获取题目知识点对应关系
					Map<Long, Set<Integer>> knowledgeTextbookMap = new HashMap<Long, Set<Integer>>(); // 经过教师教材版本过滤后的知识点对应的教材
					Map<Long, List<Long>> questionKnowledgeMap = this.getQuestionKnowledgeMap(tempHkQuestion, teacher,
							knowledgeTextbookMap);

					if (CollectionUtils.isEmpty(questionKnowledgeMap)) {
						continue;
					}

					// 统计班级知识点答题情况
					diagnosticClassKnowpointService.doKnowledgeStat(homeworkClazz.getId(), tempHkQuestion,
							questionKnowledgeMap, !incr);

					// 统计班级教材答题情况
					if (teacher.getTextbookCategoryCode() != null) {
						diagnosticClassService.doDiagnosticClassStat(tempHkQuestion, teacher.getTextbookCategoryCode(),
								questionKnowledgeMap, knowledgeTextbookMap, homeworkClazz.getId(), !incr);

						diagnosticClassService.doClassTextbookStat(clazz.getId());
					}
				}
			}
		} else {
			// 只需要对本班进行统计

			// 当前班级教师
			Teacher teacher = teacherService.get(clazz.getTeacherId());

			// 获取题目知识点对应关系
			Map<Long, Set<Integer>> knowledgeTextbookMap = new HashMap<Long, Set<Integer>>(); // 经过教师教材版本过滤后的知识点对应的教材
			Map<Long, List<Long>> questionKnowledgeMap = this.getQuestionKnowledgeMap(hkQuestion, teacher,
					knowledgeTextbookMap);

			if (CollectionUtils.isEmpty(questionKnowledgeMap)) {
				return;
			}

			// 统计班级知识点答题情况
			diagnosticClassKnowpointService.doKnowledgeStat(clazz.getId(), hkQuestion, questionKnowledgeMap, !incr);

			// 统计班级教材答题情况
			if (teacher.getTextbookCategoryCode() != null) {
				diagnosticClassService.doDiagnosticClassStat(hkQuestion, teacher.getTextbookCategoryCode(),
						questionKnowledgeMap, knowledgeTextbookMap, clazz.getId(), !incr);

				diagnosticClassService.doClassTextbookStat(clazz.getId());
			}
		}
	}
}

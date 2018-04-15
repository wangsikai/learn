package com.lanking.uxb.zycon.activity.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.domain.yoomath.fallible.TeacherFallibleQuestion;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.code.api.KnowledgeSystemService;
import com.lanking.uxb.zycon.activity.api.ZycHolidayActivity01ExerciseQuestionService;
import com.lanking.uxb.zycon.activity.api.ZycHolidayActivity01ExerciseService;
import com.lanking.uxb.zycon.activity.api.ZycHolidayActivity01Service;

/**
 * 寒假作业活动相关.
 * 
 * @since 教师端 v1.2.0
 *
 */
@RestController
@RequestMapping(value = "zyc/holidayActivity01")
public class ZycHolidayActivity01Controller {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ZycHolidayActivity01Service holidayActivity01Service;

	@Autowired
	private ZycHolidayActivity01ExerciseQuestionService exerciseQuestionService;

	@Autowired
	private ZycHolidayActivity01ExerciseService exerciseService;

	@Autowired
	private KnowledgeSystemService knowledgeSystemService;
	

	/**
	 * 知识点习题初始化.
	 * 
	 * @return
	 */
	@RequestMapping(value = "knowledgeQuestion/init")
	public Value knowledgeQuestionInit() {
		exerciseService.handleAllKnowpointQuestions();
		return new Value();
	}

	/**
	 * 练习初始化（需要首先初始化知识点习题）.
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "exercise/init", method = { RequestMethod.POST, RequestMethod.GET })
	public Value exerciseInit(Long code) {
		if (code == null) {
			code = 1L;
		}

		if (holidayActivity01Service.get(code) == null) {
			return new Value(new IllegalArgException());
		}

		// 首先清空数据
		holidayActivity01Service.deleteAllExerciseAndQuestion(code);

		// 错题本习题数据
		long t1 = System.currentTimeMillis();
		int pageSize = 600;
		int offset = 0;
		Page<TeacherFallibleQuestion> fallibleQuestionPage = exerciseQuestionService
				.initFindFallibleQuestions(P.offset(offset, pageSize));
		exerciseQuestionService.saveTeacherFallibleQuestions(code, fallibleQuestionPage);
		while (fallibleQuestionPage.isNotEmpty()) {
			offset += pageSize;
			fallibleQuestionPage = exerciseQuestionService.initFindFallibleQuestions(P.offset(offset, pageSize));
			exerciseQuestionService.saveTeacherFallibleQuestions(code, fallibleQuestionPage);
		}

		long t2 = System.currentTimeMillis();
		logger.info("[2018寒假作业活动] 错题练习初始化完成，共耗时" + ((t2 - t1) / 1000) + "秒");

		// 弱项习题数据

		// 获取小专题
		List<KnowledgeSystem> knowledgeSystems = knowledgeSystemService.getBySubjectAndLevel(2, 202);
		knowledgeSystems.addAll(knowledgeSystemService.getBySubjectAndLevel(2, 302));
		Map<Long, KnowledgeSystem> knowledgeSystemMap = new HashMap<Long, KnowledgeSystem>(knowledgeSystems.size());
		for (KnowledgeSystem knowledgeSystem : knowledgeSystems) {
			knowledgeSystemMap.put(knowledgeSystem.getCode(), knowledgeSystem);
		}

		// 获取教师弱项知识点个数
		List<Map> teacherWeakpointCounts = exerciseService.listTeacherWeakpointCount();
		int count = 0;
		List<Long> teacherIds = new ArrayList<Long>();
		for (Map countMap : teacherWeakpointCounts) {
			int num = Integer.parseInt(countMap.get("num").toString());
			long teacherId = Long.parseLong(countMap.get("teacher_id").toString());
			count += num;
			teacherIds.add(teacherId);
			if (count > 1000) {
				// 每次过滤1000个左右知识点进行处理，最终按照小专题15个最大值过滤
				this.handleWeakQuestions(code, teacherIds, knowledgeSystemMap);
				count = 0;
				teacherIds = new ArrayList<Long>();
			}
		}

		if (count != 0) {
			this.handleWeakQuestions(code, teacherIds, knowledgeSystemMap);
			count = 0;
			teacherIds = new ArrayList<Long>();
		}

		long t3 = System.currentTimeMillis();
		logger.info("[2018寒假作业活动] 薄弱专项练习初始化完成，共耗时" + ((t3 - t2) / 1000) + "秒");

		return new Value();
	}

	/**
	 * 处理弱项练习.
	 * 
	 * @param teacherIds
	 *            教师ID集合
	 */
	private void handleWeakQuestions(Long activityCode, List<Long> teacherIds,
			Map<Long, KnowledgeSystem> knowledgeSystemMap) {

		// 获取弱项知识点集合
		Map<Long, Map<Long, List<Long>>> weakpointMap = exerciseService.listTeacherWeakpoint(teacherIds);

		if (weakpointMap.size() != 0) {
			for (Entry<Long, Map<Long, List<Long>>> entry : weakpointMap.entrySet()) {
				long teacherId = entry.getKey();

				// 小专题排序
				List<Long> l2KnowledgeCodes = new ArrayList<Long>(entry.getValue().keySet());
				Collections.sort(l2KnowledgeCodes);
				exerciseQuestionService.saveTeacherWeakpointQuestions(activityCode, teacherId, l2KnowledgeCodes,
						knowledgeSystemMap, entry.getValue());
			}
		}
	}

	/**
	 * 活动数据初始化，临时接口，后面会放到创建新的活动抽奖里面初始化
	 * 
	 * @return
	 */
	@RequestMapping(value = "activityInit", method = { RequestMethod.POST, RequestMethod.GET })
	public Value activityInit(Long seasonId) {
		holidayActivity01Service.init(seasonId,null);
		return new Value();
	}
}

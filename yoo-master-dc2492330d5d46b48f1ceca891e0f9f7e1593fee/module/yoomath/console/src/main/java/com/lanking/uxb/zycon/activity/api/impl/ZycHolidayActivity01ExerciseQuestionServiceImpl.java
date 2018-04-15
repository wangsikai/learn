package com.lanking.uxb.zycon.activity.api.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Exercise;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01ExerciseQuestion;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01ExerciseType;
import com.lanking.cloud.domain.yoomath.fallible.TeacherFallibleQuestion;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.zycon.activity.api.ZycHolidayActivity01ExerciseQuestionService;
import com.lanking.uxb.zycon.activity.api.ZycHolidayActivity01ExerciseService;
import com.lanking.uxb.zycon.activity.cache.ZycHolidayActivity01CacheService;

/**
 * 寒假作业活动-习题相关接口实现.
 * 
 * @since 教师端 v1.2.0
 *
 */
@Transactional(readOnly = true)
@Service
public class ZycHolidayActivity01ExerciseQuestionServiceImpl implements ZycHolidayActivity01ExerciseQuestionService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier("HolidayActivity01ExerciseQuestionRepo")
	private Repo<HolidayActivity01ExerciseQuestion, Long> holidayActivity01ExerciseQuestionRepo;

	@Autowired
	@Qualifier("TeacherFallibleQuestionRepo")
	private Repo<TeacherFallibleQuestion, Long> teacherFallibleQuestionRepo;

	@Autowired
	@Qualifier("HolidayActivity01ExerciseRepo")
	private Repo<HolidayActivity01Exercise, Long> holidayActivity01ExerciseRepo;

	@Override
	public Page<TeacherFallibleQuestion> initFindFallibleQuestions(Pageable pageable) {
		return teacherFallibleQuestionRepo.find("$initFindFallibleQuestions").fetch(pageable);
	}

	@Autowired
	private ZycHolidayActivity01ExerciseService holidayActivity01ExerciseService;

	@Autowired
	private ZycHolidayActivity01CacheService holidayActivity01CacheService;

	@Override
	@Transactional
	public void saveTeacherFallibleQuestions(Long activityCode, Page<TeacherFallibleQuestion> fallibleQuestionPage) {
		if (fallibleQuestionPage.getItemSize() == 0) {
			return;
		}
		List<TeacherFallibleQuestion> questions = fallibleQuestionPage.getItems();
		Map<Long, HolidayActivity01Exercise> teacherExerciseMap = new HashMap<Long, HolidayActivity01Exercise>();

		for (TeacherFallibleQuestion question : questions) {
			HolidayActivity01Exercise exercise = teacherExerciseMap.get(question.getTeacherId());
			if (exercise == null) {
				// 获取该老师已有的最后一个练习
				exercise = holidayActivity01ExerciseService.getLastTeacherExercise(activityCode,
						question.getTeacherId(), HolidayActivity01ExerciseType.FALLIBLE);
			}

			if (exercise == null || exercise.getQuestionCount() == 15) {
				int sequence = exercise == null ? 1 : (exercise.getSequence() + 1);
				exercise = new HolidayActivity01Exercise();
				exercise.setName("寒假错题专练（" + sequence + "）");
				exercise.setType(HolidayActivity01ExerciseType.FALLIBLE);
				exercise.setUserId(question.getTeacherId());
				exercise.setSequence(sequence);
				exercise.setQuestionCount(0);
				exercise.setActivityCode(activityCode);
				holidayActivity01ExerciseRepo.save(exercise);
			}
			teacherExerciseMap.put(question.getTeacherId(), exercise);

			// 创建习题
			HolidayActivity01ExerciseQuestion eq = new HolidayActivity01ExerciseQuestion();
			eq.setHolidayActivity01ExerciseId(exercise.getId());
			eq.setQuestionId(question.getQuestionId());
			eq.setSequence(exercise.getQuestionCount() + 1);
			eq.setActivityCode(activityCode);
			holidayActivity01ExerciseQuestionRepo.save(eq);

			exercise.setQuestionCount(eq.getSequence());
			holidayActivity01ExerciseRepo.save(exercise);
		}

		logger.info("[2018寒假作业活动] 正在初始化错题练习中... 共" + fallibleQuestionPage.getTotalCount() + "题，已处理"
				+ (fallibleQuestionPage.getOffset() + fallibleQuestionPage.getItemSize()) + "题");
	}

	@Override
	@Transactional
	public void saveTeacherWeakpointQuestions(Long activityCode, long teacherId, List<Long> l2KnowledgeCodes,
			Map<Long, KnowledgeSystem> knowledgeSystemMap, Map<Long, List<Long>> knowledgeQuestions) {
		int sequence = 0;
		for (Long l2KnowledgeCode : l2KnowledgeCodes) {

			// 小专题
			KnowledgeSystem knowledgeSystem = knowledgeSystemMap.get(l2KnowledgeCode);

			HolidayActivity01Exercise exercise = new HolidayActivity01Exercise();
			exercise.setName(knowledgeSystem.getName());
			exercise.setType(HolidayActivity01ExerciseType.KNOWLEDGE_POINT);
			exercise.setUserId(teacherId);
			exercise.setSequence(sequence);
			exercise.setQuestionCount(0);
			exercise.setActivityCode(activityCode);

			// 获取小专题下的习题
			Set<Long> questionIds = this.processQuestionIds(knowledgeQuestions.get(l2KnowledgeCode));

			exercise.setQuestionCount(questionIds.size());
			holidayActivity01ExerciseRepo.save(exercise);

			// 创建习题
			Iterator<Long> iterator = questionIds.iterator();
			int i = 1;
			while (iterator.hasNext()) {
				HolidayActivity01ExerciseQuestion eq = new HolidayActivity01ExerciseQuestion();
				eq.setHolidayActivity01ExerciseId(exercise.getId());
				eq.setQuestionId(iterator.next());
				eq.setSequence(i);
				eq.setActivityCode(activityCode);
				holidayActivity01ExerciseQuestionRepo.save(eq);
				i++;
			}

			sequence++;
		}
	}

	/**
	 * 按规则填充薄弱知识小专题题目，直到满足15题.
	 * 
	 * @param knowledgeCodes
	 *            小专题下的底层薄弱知识点集合
	 */
	private Set<Long> processQuestionIds(List<Long> knowledgeCodes) {
		Set<Long> questionIds = new HashSet<Long>(15);
		List<List<Long>> knowledgeQuestions = holidayActivity01CacheService.mGetQuestions(knowledgeCodes);

		// 整理薄弱知识点与习题
		Map<Long, List<Long>> knowledgeQuestionMap = new HashMap<Long, List<Long>>(knowledgeCodes.size());
		for (int i = 0; i < knowledgeQuestions.size(); i++) {
			knowledgeQuestionMap.put(knowledgeCodes.get(i),
					knowledgeQuestions.get(i) == null ? new ArrayList<Long>(0) : knowledgeQuestions.get(i));
		}

		// 递归处理返回的习题列表
		this.fillQuestionIds(questionIds, knowledgeQuestionMap);
		return questionIds;
	}

	/**
	 * 按规则填充薄弱知识小专题题目，直到满足15题.
	 * 
	 * @param questionIds
	 *            习题列表
	 * @param knowledgeQuestionMap
	 *            小专题下的底层薄弱知识点集合
	 */
	private void fillQuestionIds(Set<Long> questionIds, Map<Long, List<Long>> knowledgeQuestionMap) {
		if (questionIds.size() >= 15) {
			return;
		}
		Set<Entry<Long, List<Long>>> entrySet = knowledgeQuestionMap.entrySet();
		int zeroCount = 0;
		for (Entry<Long, List<Long>> entry : entrySet) {
			if (entry.getValue().size() > 0) {
				questionIds.add(entry.getValue().get(0));
				entry.getValue().remove(0);
				if (questionIds.size() >= 15) {
					return;
				}
			} else {
				zeroCount++;
			}
		}
		if (zeroCount == entrySet.size()) {
			return;
		}
		if (questionIds.size() < 15) {
			this.fillQuestionIds(questionIds, knowledgeQuestionMap);
		}
	}
}

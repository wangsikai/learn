package com.lanking.uxb.service.diagnostic.api.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClass;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassLatestHomeworkKnowpoint;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassTextbook;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.code.api.KnowledgeSectionService;
import com.lanking.uxb.service.code.api.QuestionKnowledgeService;
import com.lanking.uxb.service.diagnostic.api.StaticDiagnosticClassDayService;
import com.lanking.uxb.service.diagnostic.api.StaticDiagnosticKpService;
import com.lanking.uxb.service.diagnostic.form.StaticClassQuestionDifficultyForm;
import com.lanking.uxb.service.diagnostic.type.QuestionDifficultyType;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
@Service
@Transactional
public class StaticDiagnosticKpServiceImpl implements StaticDiagnosticKpService {
	@Autowired
	@Qualifier("DiagnosticClassLatestHomeworkKnowpointRepo")
	private Repo<DiagnosticClassLatestHomeworkKnowpoint, Long> diaClassLatestHkKnowpointRepo;

	@Autowired
	@Qualifier("DiagnosticClassTextbookRepo")
	private Repo<DiagnosticClassTextbook, Long> diaClassTextbookRepo;

	@Autowired
	@Qualifier("TeacherRepo")
	private Repo<Teacher, Long> teacherRepo;

	@Autowired
	@Qualifier("DiagnosticClassRepo")
	private Repo<DiagnosticClass, Long> diaClassRepo;

	@Autowired
	private QuestionKnowledgeService questionKnowledgeService;
	@Autowired
	private KnowledgeSectionService knowledgeSectionService;
	@Autowired
	private StaticDiagnosticClassDayService diagnosticClassDayService;

	/**
	 * 统计知识点的数据
	 *
	 * hkQuestion -> <br/>
	 * right_count: 正确的数量<br/>
	 * wrong_count: 错误的数量<br/>
	 * question_id: 题目id<br/>
	 * difficulty: 题目的难度<br/>
	 *
	 * @param homeworkClassId
	 *            班级id
	 * @param teacherId
	 *            教师对象
	 * @param hkQuestion
	 *            作业各题目完成情况数据列表
	 */
	@Override
	@Transactional
	public void doKnowledgeStat(Long homeworkClassId, long teacherId, List<Map> hkQuestion, boolean genClassKp,
			int times) {
		Set<Integer> inTextbookCodes = Sets.newHashSet();
		Teacher teacher = teacherRepo.get(teacherId);

		Map<Long, List<StaticClassQuestionDifficultyForm>> difficultMap = new HashMap<Long, List<StaticClassQuestionDifficultyForm>>(
				hkQuestion.size());
		Map<Long, List<StaticClassQuestionDifficultyForm>> parentDifficultMap = new HashMap<Long, List<StaticClassQuestionDifficultyForm>>();
		for (Map m : hkQuestion) {
			List<Long> knowledgeCodes = Lists.newArrayList();
			Long rightCount = m.get("right_count") == null ? 0 : ((BigDecimal) m.get("right_count")).longValue();
			Long wrongCount = m.get("wrong_count") == null ? 0 : ((BigDecimal) m.get("wrong_count")).longValue();
			Long halfWrongCount = m.get("half_wrong_count") == null ? 0 : ((BigDecimal) m.get("half_wrong_count"))
					.longValue();
			Long nowDoCount = rightCount + wrongCount + halfWrongCount;
			Long questionId = ((BigInteger) m.get("question_id")).longValue();
			Double difficulty = new BigDecimal((Double) m.get("difficulty")).setScale(2, RoundingMode.HALF_UP)
					.doubleValue();

			// 得到此次作业各种难度题目数量相关数据
			StaticClassQuestionDifficultyForm form = new StaticClassQuestionDifficultyForm();
			form.setDoCount(nowDoCount);
			form.setRightCount(rightCount);
			if (difficulty >= 0.8 && difficulty <= 1) {
				form.setType(QuestionDifficultyType.SIMPLE);
			} else if (difficulty >= 0.4 && difficulty < 0.8) {
				form.setType(QuestionDifficultyType.MIDDLE);
			} else {
				form.setType(QuestionDifficultyType.DIFFICULT);
			}

			form.setDifficulty(difficulty);

			List<Long> codes = questionKnowledgeService.queryKnowledgeByQuestionId(questionId);
			List<Integer> textbookCodes = knowledgeSectionService.queryTextbookByKnowledge(codes);
			for (Long co : codes) {
				boolean inTeacherCategory = false;
				for (Integer tCode : textbookCodes) {
					if (tCode.toString().startsWith(teacher.getTextbookCategoryCode().toString())) {
						inTeacherCategory = true;
						inTextbookCodes.add(tCode);
					}
				}

				// 当题目当前对应的知识点并不在老师当前的版本下面则不计入统计
				if (!inTeacherCategory) {
					continue;
				}
				List<StaticClassQuestionDifficultyForm> forms = difficultMap.get(co);
				if (forms == null) {
					forms = Lists.newArrayList();
				}
				form.setTextbookCodes(inTextbookCodes);

				forms.add(form);

				difficultMap.put(co, forms);

				// 统计题目相关的知识点
				if (!genClassKp) {
					doClassLatestKpStat(rightCount, nowDoCount, co, homeworkClassId, teacher.getTextbookCode(), times);
				}
				// 当前教师版本下有效的知识点
				knowledgeCodes.add(co);
			}

			// 统计题目父级知识专项等数据
			List<Long> parentCodes = questionKnowledgeService.queryParentKnowledgeCodes(knowledgeCodes);
			for (Long c : parentCodes) {
				if (!genClassKp) {
					doClassLatestKpStat(rightCount, nowDoCount, c, homeworkClassId, teacher.getTextbookCode(), times);
				}

				List<StaticClassQuestionDifficultyForm> parentForms = parentDifficultMap.get(c);
				if (parentForms == null) {
					parentForms = Lists.newArrayList();
				}

				parentForms.add(form);

				parentDifficultMap.put(c, parentForms);

			}

		}

		// 对知识点数据统一处理
		if (genClassKp) {
			diagnosticClassDayService.doClassKpStat(homeworkClassId, difficultMap);
			diagnosticClassDayService.doClassKpStat(homeworkClassId, parentDifficultMap);
			doClassTextbookStat(hkQuestion, teacher.getTextbookCategoryCode(), homeworkClassId);

			// 有效版本下的教材判断是否有数据
			doClassTextbook(homeworkClassId, inTextbookCodes);
		}
	}

	@Transactional
	private void doClassTextbookStat(List<Map> hkQuestion, Integer textbookCategoryCode, Long classId) {
		Map<Integer, List<StaticClassQuestionDifficultyForm>> textbookMap = new HashMap<Integer, List<StaticClassQuestionDifficultyForm>>();
		for (Map m : hkQuestion) {
			Long rightCount = m.get("right_count") == null ? 0 : ((BigDecimal) m.get("right_count")).longValue();
			Long wrongCount = m.get("wrong_count") == null ? 0 : ((BigDecimal) m.get("wrong_count")).longValue();
			Long halfWrongCount = m.get("half_wrong_count") == null ? 0 : ((BigDecimal) m.get("half_wrong_count"))
					.longValue();
			Long nowDoCount = rightCount + wrongCount + halfWrongCount;
			Long questionId = ((BigInteger) m.get("question_id")).longValue();
			Double difficulty = new BigDecimal((Double) m.get("difficulty")).setScale(2, RoundingMode.HALF_UP)
					.doubleValue();
			List<Long> codes = questionKnowledgeService.queryKnowledgeByQuestionId(questionId);
			// 优化:提高运行的效率一次将一题对应的章节全部取出
			List<Integer> textbookCodes = knowledgeSectionService.queryTextbookByKnowledge(codes);
			for (Integer tCode : textbookCodes) {
				if (tCode.toString().startsWith(textbookCategoryCode.toString())) {
					List<StaticClassQuestionDifficultyForm> forms = textbookMap.get(tCode);
					if (forms == null) {
						forms = Lists.newArrayList();
					}

					StaticClassQuestionDifficultyForm form = new StaticClassQuestionDifficultyForm();
					form.setDoCount(nowDoCount);
					form.setRightCount(rightCount);
					if (difficulty >= 0.8 && difficulty <= 1) {
						form.setType(QuestionDifficultyType.SIMPLE);
					} else if (difficulty >= 0.4 && difficulty < 0.8) {
						form.setType(QuestionDifficultyType.MIDDLE);
					} else {
						form.setType(QuestionDifficultyType.DIFFICULT);
					}

					forms.add(form);

					textbookMap.put(tCode, forms);
				}
			}

		}

		// 保存班级教材数据
		for (Map.Entry<Integer, List<StaticClassQuestionDifficultyForm>> entry : textbookMap.entrySet()) {
			Integer textbookCode = entry.getKey();
			DiagnosticClass diaClass = diaClassRepo.find("$ymGetByClassAndTextbook",
					Params.param("classId", classId).put("code", textbookCode)).get();

			int doHard1Count = 0, rightHard1Count = 0, doHard2Count = 0, rightHard2Count = 0, doHard3Count = 0, rightHard3Count = 0;
			BigDecimal rightRate = null;

			for (StaticClassQuestionDifficultyForm f : entry.getValue()) {
				switch (f.getType()) {
				case SIMPLE:
					doHard1Count += f.getDoCount();
					rightHard1Count += f.getRightCount();
					break;
				case MIDDLE:
					doHard2Count += f.getDoCount();
					rightHard2Count += f.getRightCount();
					break;
				case DIFFICULT:
					doHard3Count += f.getDoCount();
					rightHard3Count += f.getRightCount();
					break;
				}
			}

			if (diaClass == null) {
				diaClass = new DiagnosticClass();
				diaClass.setTextbookCode(textbookCode);
				diaClass.setCreateAt(new Date());
				diaClass.setClassId(classId);
			} else {
				doHard1Count += diaClass.getDoHard1Count();
				rightHard1Count += diaClass.getRightHard1Count();

				doHard2Count += diaClass.getDoHard2Count();
				rightHard2Count += diaClass.getRightHard2Count();

				doHard3Count += diaClass.getDoHard3Count();
				rightHard3Count += diaClass.getRightHard3Count();
			}

			diaClass.setDoHard1Count(doHard1Count);
			diaClass.setRightHard1Count(rightHard1Count);
			diaClass.setDoHard2Count(doHard2Count);
			diaClass.setRightHard2Count(rightHard2Count);
			diaClass.setDoHard3Count(doHard3Count);
			diaClass.setRightHard3Count(rightHard3Count);
			diaClass.setDoCountMonth(doHard1Count + doHard2Count + doHard3Count);
			diaClass.setRightCountMonth(rightHard1Count + rightHard2Count + rightHard3Count);

			diaClass.setUpdateAt(new Date());

			diaClassRepo.save(diaClass);

		}

	}

	/**
	 * 处理班级教材下的相关数据
	 *
	 * @param classId
	 *            班级id
	 * @param inTextbookCodes
	 *            所有当前版本下的教材码
	 */
	@Transactional
	private void doClassTextbook(Long classId, Collection<Integer> inTextbookCodes) {
		for (Integer c : inTextbookCodes) {
			DiagnosticClassTextbook classTextbook = diaClassTextbookRepo.find("$ymFindByClassAndTextbook",
					Params.param("textbookCode", c).put("classId", classId)).get();

			if (classTextbook == null) {
				classTextbook = new DiagnosticClassTextbook();
				classTextbook.setClassId(classId);
				classTextbook.setTextbookCode(c);
				classTextbook.setCreateAt(new Date());
				classTextbook.setUpdateAt(new Date());

				diaClassTextbookRepo.save(classTextbook);
			} else {
				classTextbook.setUpdateAt(new Date());
				diaClassTextbookRepo.save(classTextbook);
			}
		}
	}

	/**
	 * 处理班级知识点相关数据
	 *
	 * @param rightCount
	 *            正确数量
	 * @param doCount
	 *            练习量
	 * @param code
	 *            知识点代码
	 * @param classId
	 *            班级id
	 * @param textbookCode
	 *            教材码
	 */
	@Override
	@Transactional
	public void doClassLatestKpStat(Long rightCount, Long doCount, Long code, Long classId, Integer textbookCode,
			int times) {

		DiagnosticClassLatestHomeworkKnowpoint hkKnowpointStat = diaClassLatestHkKnowpointRepo.find(
				"$ymFindByClassAndKnowpoint",
				Params.param("classId", classId).put("knowpoint", code).put("times", times)).get();

		if (hkKnowpointStat == null) {
			hkKnowpointStat = new DiagnosticClassLatestHomeworkKnowpoint();
			hkKnowpointStat.setClassId(classId);
			hkKnowpointStat.setDoCount(doCount.intValue());
			hkKnowpointStat.setKnowpointCode(code);
			hkKnowpointStat.setRightCount(rightCount.intValue());
			hkKnowpointStat.setTimes(times);
			if (doCount.intValue() == 0) {
				hkKnowpointStat.setRightRate(new BigDecimal(0));
			} else {
				BigDecimal rightRate = new BigDecimal(rightCount * 100d / doCount).setScale(0, RoundingMode.HALF_UP);
				hkKnowpointStat.setRightRate(rightRate);
			}

			diaClassLatestHkKnowpointRepo.save(hkKnowpointStat);
		} else {
			hkKnowpointStat.setDoCount(hkKnowpointStat.getDoCount() + doCount.intValue());
			hkKnowpointStat.setRightCount(hkKnowpointStat.getRightCount() + rightCount.intValue());
			if (hkKnowpointStat.getDoCount() == 0) {
				hkKnowpointStat.setRightRate(new BigDecimal(0));
			} else {
				BigDecimal rightRate = new BigDecimal(hkKnowpointStat.getRightCount() * 100d
						/ hkKnowpointStat.getDoCount()).setScale(0, BigDecimal.ROUND_HALF_UP);

				hkKnowpointStat.setRightRate(rightRate);
			}

			diaClassLatestHkKnowpointRepo.save(hkKnowpointStat);
		}
	}
}

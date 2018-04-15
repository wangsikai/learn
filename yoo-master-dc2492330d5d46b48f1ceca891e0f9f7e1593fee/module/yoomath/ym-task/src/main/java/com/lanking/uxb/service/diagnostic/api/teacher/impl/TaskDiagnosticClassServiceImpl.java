package com.lanking.uxb.service.diagnostic.api.teacher.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClass;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassTextbook;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.diagnostic.api.teacher.TaskDiagnosticClassService;
import com.lanking.uxb.service.diagnostic.type.QuestionDifficultyType;

/**
 * 班级-教材维度统计服务接口实现.
 * 
 * @author wlche
 * 
 * @since 教师端 v1.3.0，2017-6-30 数据跟随相关整理
 *
 */

@Service
@Transactional(readOnly = true)
public class TaskDiagnosticClassServiceImpl implements TaskDiagnosticClassService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier("DiagnosticClassRepo")
	private Repo<DiagnosticClass, Long> diaClassRepo;

	@Autowired
	@Qualifier("DiagnosticClassTextbookRepo")
	private Repo<DiagnosticClassTextbook, Long> diaClassTextbookRepo;

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public void doDiagnosticClassStat(List<Map> hkQuestion, int textbookCategoryCode,
			Map<Long, List<Long>> questionKnowledgeMap, Map<Long, Set<Integer>> knowledgeTextbookMap, long classId,
			boolean reduce) {

		// 获取所有教材下的数据
		Set<Integer> textbookcodes = new HashSet<Integer>();
		for (Set<Integer> tbs : knowledgeTextbookMap.values()) {
			textbookcodes.addAll(tbs);
		}

		if (textbookcodes.size() == 0) {
			return;
		}
		List<DiagnosticClass> diaClazzs = diaClassRepo
				.find("$ymGetByClassAndTextbooks", Params.param("classId", classId).put("codes", textbookcodes)).list();

		// 转成教材对应统计数据
		Map<Integer, DiagnosticClass> textbookDiagnosticClassMap = new HashMap<Integer, DiagnosticClass>();
		for (DiagnosticClass diagnosticClass : diaClazzs) {
			textbookDiagnosticClassMap.put(diagnosticClass.getTextbookCode(), diagnosticClass);
		}

		for (Map m : hkQuestion) {
			Long questionId = Long.parseLong(m.get("question_id").toString());
			List<Long> knowpointCodes = questionKnowledgeMap.get(questionId);
			if (CollectionUtils.isEmpty(knowpointCodes)) {
				continue;
			}

			int rightCount = m.get("right_count") == null ? 0 : Integer.parseInt(m.get("right_count").toString());
			int wrongCount = m.get("wrong_count") == null ? 0 : Integer.parseInt(m.get("wrong_count").toString());
			int halfWrongCount = m.get("half_wrong_count") == null ? 0
					: Integer.parseInt(m.get("half_wrong_count").toString());
			int nowDoCount = rightCount + wrongCount + halfWrongCount;
			double difficulty = new BigDecimal((Double) m.get("difficulty")).setScale(2, RoundingMode.HALF_UP)
					.doubleValue();

			if (reduce) {
				// 减量
				rightCount = 0 - rightCount;
				wrongCount = 0 - wrongCount;
				nowDoCount = 0 - nowDoCount;
			}

			// 得到此次作业各种难度题目数量相关数据
			QuestionDifficultyType difficultyType = null;
			if (difficulty >= 0.8 && difficulty <= 1) {
				difficultyType = QuestionDifficultyType.SIMPLE;
			} else if (difficulty >= 0.4 && difficulty < 0.8) {
				difficultyType = QuestionDifficultyType.MIDDLE;
			} else {
				difficultyType = QuestionDifficultyType.DIFFICULT;
			}

			// 根据知识点做数据处理
			Set<Integer> textbookCodeTemps = new HashSet<Integer>(); // 对于一道题同一本教材已经添加过了，不再处理
			for (Long knowpointCode : knowpointCodes) {
				// 找到知识点对应的教材
				Set<Integer> textbookCodes = knowledgeTextbookMap.get(knowpointCode);
				if (CollectionUtils.isNotEmpty(textbookCodes)) {
					for (Integer textbookCode : textbookCodes) {
						if (!textbookCodeTemps.contains(textbookCode)) {
							this.fillData(classId, textbookCode, textbookDiagnosticClassMap, rightCount, nowDoCount,
									difficulty, difficultyType);
							textbookCodeTemps.add(textbookCode);
						}
					}
				}
			}
		}

		if (textbookDiagnosticClassMap.size() > 0) {
			diaClassRepo.save(textbookDiagnosticClassMap.values());
		}
	}

	/**
	 * 填充班级教材统计数据.
	 * 
	 * @param homeworkClassId
	 * @param textbookCode
	 * @param textbookDiagnosticClassMap
	 * @param rightCount
	 * @param wrongCount
	 * @param nowDoCount
	 * @param difficulty
	 * @param difficultyType
	 */
	private void fillData(long homeworkClassId, int textbookCode,
			Map<Integer, DiagnosticClass> textbookDiagnosticClassMap, int rightCount, int nowDoCount, double difficulty,
			QuestionDifficultyType difficultyType) {
		DiagnosticClass diaClass = textbookDiagnosticClassMap.get(textbookCode);

		int doHard1Count = 0, rightHard1Count = 0, doHard2Count = 0, rightHard2Count = 0, doHard3Count = 0,
				rightHard3Count = 0;

		switch (difficultyType) {
		case SIMPLE:
			doHard1Count += nowDoCount;
			rightHard1Count += rightCount;
			break;
		case MIDDLE:
			doHard2Count += nowDoCount;
			rightHard2Count += rightCount;
			break;
		case DIFFICULT:
			doHard3Count += nowDoCount;
			rightHard3Count += rightCount;
			break;
		}

		if (diaClass == null) {
			diaClass = new DiagnosticClass();
			diaClass.setTextbookCode(textbookCode);
			diaClass.setCreateAt(new Date());
			diaClass.setClassId(homeworkClassId);
			textbookDiagnosticClassMap.put(textbookCode, diaClass);
		} else {
			doHard1Count += diaClass.getDoHard1Count();
			rightHard1Count += diaClass.getRightHard1Count();

			doHard2Count += diaClass.getDoHard2Count();
			rightHard2Count += diaClass.getRightHard2Count();

			doHard3Count += diaClass.getDoHard3Count();
			rightHard3Count += diaClass.getRightHard3Count();
		}

		boolean error = false;
		if (doHard1Count < 0) {
			doHard1Count = 0;
			error = true;
		}
		if (doHard2Count < 0) {
			doHard2Count = 0;
			error = true;
		}
		if (doHard3Count < 0) {
			doHard3Count = 0;
			error = true;
		}
		if (rightHard1Count < 0) {
			rightHard1Count = 0;
			error = true;
		}
		if (rightHard2Count < 0) {
			rightHard2Count = 0;
			error = true;
		}
		if (rightHard2Count < 0) {
			rightHard2Count = 0;
			error = true;
		}
		if (error) {
			logger.error(
					"[teacher diagno incr -> fillData] class=" + homeworkClassId + ", textbookCode=" + textbookCode);
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
	}

	@Override
	@Transactional
	public void doClassTextbookStat(long classId) {
		diaClassTextbookRepo.execute("$taskDelByClass", Params.param("classId", classId));
		List<Textbook> textbooks = diaClassTextbookRepo
				.find("$findTextbookByDiaClassKp", Params.param("classId", classId)).list(Textbook.class);
		Date date = new Date();
		for (Textbook textbook : textbooks) {
			DiagnosticClassTextbook classTextbook = new DiagnosticClassTextbook();
			classTextbook.setClassId(classId);
			classTextbook.setTextbookCode(textbook.getCode());
			classTextbook.setCreateAt(date);
			classTextbook.setUpdateAt(date);
			diaClassTextbookRepo.save(classTextbook);
		}
	}
}

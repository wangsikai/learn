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
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassKnowpoint;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.diagnostic.api.teacher.TaskDiagnosticClassKnowpointService;
import com.lanking.uxb.service.diagnostic.type.QuestionDifficultyType;

/**
 * 班级-知识点维度统计服务接口实现.
 * 
 * @author wlche
 * 
 * @since 教师端 v1.3.0，2017-6-30 数据跟随相关整理
 *
 */
@Service
@Transactional(readOnly = true)
public class TaskDiagnosticClassKnowpointServiceImpl implements TaskDiagnosticClassKnowpointService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	@Qualifier("DiagnosticClassKnowpointRepo")
	private Repo<DiagnosticClassKnowpoint, Long> diaClassKpRepo;

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public void doKnowledgeStat(Long homeworkClassId, List<Map> hkQuestion, Map<Long, List<Long>> questionKnowledgeMap,
			boolean reduce) {

		// 批量获取当前班级知识点统计数据
		Set<Long> allKnowledgeCodes = new HashSet<Long>();
		for (List<Long> codes : questionKnowledgeMap.values()) {
			for (Long code : codes) {
				allKnowledgeCodes.add(code);
				Long third = code / 100;
				Long second = code / 1000;
				Long first = code / 100000;
				allKnowledgeCodes.add(third);
				allKnowledgeCodes.add(second);
				allKnowledgeCodes.add(first);
			}
		}

		List<DiagnosticClassKnowpoint> diagnosticClassKnowpoints = diaClassKpRepo.find("$ymGetByClassAndKnowpoints",
				Params.param("classId", homeworkClassId).put("codes", allKnowledgeCodes)).list();

		// 处理知识点统计数据
		Map<Long, DiagnosticClassKnowpoint> knowledgeDataMap = new HashMap<Long, DiagnosticClassKnowpoint>();
		for (DiagnosticClassKnowpoint dck : diagnosticClassKnowpoints) {
			knowledgeDataMap.put(dck.getKnowpointCode(), dck);
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
			if (reduce) {
				// 减量
				rightCount = 0 - rightCount;
				wrongCount = 0 - wrongCount;
				nowDoCount = 0 - nowDoCount;
			}
			double difficulty = new BigDecimal((Double) m.get("difficulty")).setScale(2, RoundingMode.HALF_UP)
					.doubleValue();

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
			for (Long knowpointCode : knowpointCodes) {
				fillKnowpoint(knowpointCode, knowledgeDataMap, rightCount, wrongCount, nowDoCount, difficulty,
						difficultyType, homeworkClassId);

				// 填充父级知识专项等数据
				Long third = knowpointCode / 100;
				Long second = knowpointCode / 1000;
				Long first = knowpointCode / 100000;
				fillKnowpoint(third, knowledgeDataMap, rightCount, wrongCount, nowDoCount, difficulty, difficultyType,
						homeworkClassId);
				fillKnowpoint(second, knowledgeDataMap, rightCount, wrongCount, nowDoCount, difficulty, difficultyType,
						homeworkClassId);
				fillKnowpoint(first, knowledgeDataMap, rightCount, wrongCount, nowDoCount, difficulty, difficultyType,
						homeworkClassId);
			}
		}

		if (knowledgeDataMap.size() > 0) {
			diaClassKpRepo.save(knowledgeDataMap.values());
		}
	}

	/**
	 * 填充班级知识点诊断数据.
	 * 
	 * @param code
	 * @param knowledgeDataMap
	 * @param rightCount
	 * @param wrongCount
	 * @param nowDoCount
	 * @param difficulty
	 * @param difficultyType
	 * @param homeworkClassId
	 */
	private void fillKnowpoint(long code, Map<Long, DiagnosticClassKnowpoint> knowledgeDataMap, int rightCount,
			int wrongCount, int nowDoCount, double difficulty, QuestionDifficultyType difficultyType,
			long homeworkClassId) {
		DiagnosticClassKnowpoint diagnosticClassKnowpoint = knowledgeDataMap.get(code);

		Double minDifficulty = diagnosticClassKnowpoint == null ? 0d
				: diagnosticClassKnowpoint.getMinDifficulty().doubleValue();
		Double maxDifficulty = diagnosticClassKnowpoint == null ? 1d
				: diagnosticClassKnowpoint.getMaxDifficulty().doubleValue();
		int doHard1Count = 0, rightHard1Count = 0, doHard2Count = 0, rightHard2Count = 0, doHard3Count = 0,
				rightHard3Count = 0;
		BigDecimal rightRate = null;

		if (difficulty > minDifficulty) {
			minDifficulty = difficulty;
		}
		if (difficulty < maxDifficulty) {
			maxDifficulty = difficulty;
		}
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

		if (diagnosticClassKnowpoint == null) {
			diagnosticClassKnowpoint = new DiagnosticClassKnowpoint();
			diagnosticClassKnowpoint.setKnowpointCode(code);
			diagnosticClassKnowpoint.setCreateAt(new Date());
			diagnosticClassKnowpoint.setClassId(homeworkClassId);
			knowledgeDataMap.put(code, diagnosticClassKnowpoint);
		} else {
			doHard1Count += diagnosticClassKnowpoint.getDoHard1Count();
			rightHard1Count += diagnosticClassKnowpoint.getRightHard1Count();

			doHard2Count += diagnosticClassKnowpoint.getDoHard2Count();
			rightHard2Count += diagnosticClassKnowpoint.getRightHard2Count();

			doHard3Count += diagnosticClassKnowpoint.getDoHard3Count();
			rightHard3Count += diagnosticClassKnowpoint.getRightHard3Count();

			// 处理异常数据
			doHard1Count = this.passCount(doHard1Count);
			rightHard1Count = this.passCount(rightHard1Count);
			doHard2Count = this.passCount(doHard2Count);
			rightHard2Count = this.passCount(rightHard2Count);
			doHard3Count = this.passCount(doHard3Count);
			rightHard3Count = this.passCount(rightHard3Count);
			if (doHard1Count < rightHard1Count) {
				doHard1Count = rightHard1Count;
			}
			if (doHard2Count < rightHard2Count) {
				doHard2Count = rightHard2Count;
			}
			if (doHard3Count < rightHard3Count) {
				doHard3Count = rightHard3Count;
			}
		}

		diagnosticClassKnowpoint.setDoHard1Count(doHard1Count);
		diagnosticClassKnowpoint.setRightHard1Count(rightHard1Count);
		diagnosticClassKnowpoint.setDoHard2Count(doHard2Count);
		diagnosticClassKnowpoint.setRightHard2Count(rightHard2Count);
		diagnosticClassKnowpoint.setDoHard3Count(doHard3Count);
		diagnosticClassKnowpoint.setRightHard3Count(rightHard3Count);
		diagnosticClassKnowpoint.setDoCount(doHard1Count + doHard2Count + doHard3Count);
		diagnosticClassKnowpoint.setRightCount(rightHard1Count + rightHard2Count + rightHard3Count);

		if (diagnosticClassKnowpoint.getDoCount() > 0) {
			rightRate = new BigDecimal(
					diagnosticClassKnowpoint.getRightCount() * 100d / diagnosticClassKnowpoint.getDoCount()).setScale(0,
							BigDecimal.ROUND_HALF_UP);
		} else {
			rightRate = new BigDecimal(0);
			logger.error("[teacher diagno incr -> fillKnowpoint] class=" + homeworkClassId + ", code=" + code);
		}

		diagnosticClassKnowpoint.setRightRate(rightRate);
		diagnosticClassKnowpoint.setMaxDifficulty(new BigDecimal(maxDifficulty));
		diagnosticClassKnowpoint.setMinDifficulty(new BigDecimal(minDifficulty));
		diagnosticClassKnowpoint.setUpdateAt(new Date());
	}

	private int passCount(int in) {
		return in < 0 ? 0 : in;
	}

	@Override
	public boolean hasClassKpData() {
		Long num = diaClassKpRepo.find("$ymHasData", Params.param()).count();
		return num != null && num > 0;
	}
}

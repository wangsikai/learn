package com.lanking.uxb.service.diagnostic.api.teacher.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassLatestHomeworkKnowpoint;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.diagnostic.api.teacher.TaskDiagnosticClassLatestHomeworkKnowpointService;

/**
 * 班级-最近n次作业薄弱知识点服务实现.
 * 
 * @author wlche
 * 
 * @since 教师端 v1.3.0，2017-6-30 数据跟随相关整理
 *
 */
@Service
@Transactional(readOnly = true)
public class TaskDiagnosticClassLatestHomeworkKnowpointServiceImpl
		implements TaskDiagnosticClassLatestHomeworkKnowpointService {

	@Autowired
	@Qualifier("DiagnosticClassLatestHomeworkKnowpointRepo")
	private Repo<DiagnosticClassLatestHomeworkKnowpoint, Long> diaClassLatestKpRepo;

	@Autowired
	@Qualifier("TeacherRepo")
	private Repo<Teacher, Long> teacherRepo;

	@Autowired
	@Qualifier("DiagnosticClassLatestHomeworkKnowpointRepo")
	private Repo<DiagnosticClassLatestHomeworkKnowpoint, Long> diaClassLatestHkKnowpointRepo;

	@Override
	@Transactional
	public void deleteByClassId(long classId) {
		diaClassLatestKpRepo.execute("$ymDeleteByClassId", Params.param("classId", classId));
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public void doKnowledgeStat(long homeworkClassId, List<Map> hkQuestion, Map<Long, List<Long>> questionKnowledgeMap,
			int times) {

		// 处理知识点统计数据
		Map<Long, DiagnosticClassLatestHomeworkKnowpoint> knowledgeDataMap = new HashMap<Long, DiagnosticClassLatestHomeworkKnowpoint>();

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

			Set<Long> parentCodes = new HashSet<Long>(); // 该题对应的所有父级知识点
			for (Long knowpointCode : knowpointCodes) {
				fillKnowpoint(knowpointCode, knowledgeDataMap, rightCount, wrongCount, nowDoCount, homeworkClassId,
						times);

				// 填充父级知识专项等数据
				Long third = knowpointCode / 100;
				Long second = knowpointCode / 1000;
				Long first = knowpointCode / 100000;
				parentCodes.add(third);
				parentCodes.add(second);
				parentCodes.add(first);
			}

			// 填充父级知识点
			for (Long knowpointCode : parentCodes) {
				fillKnowpoint(knowpointCode, knowledgeDataMap, rightCount, wrongCount, nowDoCount, homeworkClassId,
						times);
			}
		}

		if (knowledgeDataMap.size() > 0) {
			diaClassLatestHkKnowpointRepo.save(knowledgeDataMap.values());
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
	 * @param homeworkClassId
	 * @param times
	 */
	private void fillKnowpoint(long code, Map<Long, DiagnosticClassLatestHomeworkKnowpoint> knowledgeDataMap,
			int rightCount, int wrongCount, int nowDoCount, long homeworkClassId, int times) {
		DiagnosticClassLatestHomeworkKnowpoint entity = knowledgeDataMap.get(code);
		if (entity == null) {
			BigDecimal rightRate = new BigDecimal(rightCount * 100d / nowDoCount).setScale(0, RoundingMode.HALF_UP);
			entity = new DiagnosticClassLatestHomeworkKnowpoint();
			entity.setClassId(homeworkClassId);
			entity.setKnowpointCode(code);
			entity.setRightCount(rightCount);
			entity.setDoCount(nowDoCount);
			entity.setRightRate(rightRate);
			entity.setTimes(times);
			knowledgeDataMap.put(code, entity);
		} else {
			BigDecimal rightRate = new BigDecimal(entity.getRightCount() * 100d / entity.getDoCount()).setScale(0,
					BigDecimal.ROUND_HALF_UP);
			entity.setDoCount(entity.getDoCount() + nowDoCount);
			entity.setRightCount(entity.getRightCount() + rightCount);
			entity.setRightRate(rightRate);
		}
	}
}

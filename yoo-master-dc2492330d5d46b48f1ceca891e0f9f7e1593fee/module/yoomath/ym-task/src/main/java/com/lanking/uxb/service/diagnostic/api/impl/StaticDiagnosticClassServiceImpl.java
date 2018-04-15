package com.lanking.uxb.service.diagnostic.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassLatestHomework;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassLatestHomeworkKnowpoint;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.diagnostic.api.StaticDiagnosticClassService;
import com.lanking.uxb.service.diagnostic.api.StaticDiagnosticKpService;
import com.lanking.uxb.service.diagnostic.api.StaticHolidayHomeworkService;
import com.lanking.uxb.service.diagnostic.api.StaticHomeworkService;

/**
 * @see StaticDiagnosticClassService
 * @author xinyu.zhou
 * @since 2.1.0
 */
@Service
@Transactional
public class StaticDiagnosticClassServiceImpl implements StaticDiagnosticClassService {

	@Autowired
	@Qualifier("DiagnosticClassLatestHomeworkRepo")
	private Repo<DiagnosticClassLatestHomework, Long> diaClassLatestHkRepo;

	@Autowired
	@Qualifier("DiagnosticClassLatestHomeworkKnowpointRepo")
	private Repo<DiagnosticClassLatestHomeworkKnowpoint, Long> diaClassLatestKpRepo;

	@Autowired
	private StaticHomeworkService homeworkService;
	@Autowired
	private StaticHolidayHomeworkService holidayHomeworkService;
	@Autowired
	private StaticDiagnosticKpService diagnosticKpService;

	@Override
	@Transactional
	@Deprecated
	public void staticByClass(long classId, Long hkId) {

		diaClassLatestHkRepo.execute("$ymDeleteByClassId", Params.param("classId", classId));
		diaClassLatestKpRepo.execute("$ymDeleteByClassId", Params.param("classId", classId));
		List<Homework> latestHks = homeworkService.getLatestHks(classId);
		List<Long> homeworkIds = new ArrayList<Long>(latestHks.size());
		Long teacherId = latestHks.get(0).getCreateId();
		for (Homework hk : latestHks) {
			homeworkIds.add(hk.getId());
			// 统计班级最新作业情况
			DiagnosticClassLatestHomework diaClassLatestHk = new DiagnosticClassLatestHomework();
			diaClassLatestHk.setClassId(hk.getHomeworkClassId());
			diaClassLatestHk.setDifficulty(hk.getDifficulty());
			diaClassLatestHk.setHomeworkId(hk.getId());
			diaClassLatestHk.setName(hk.getName());
			diaClassLatestHk.setRightRate(hk.getRightRate());
			diaClassLatestHk.setStartTime(hk.getStartTime());

			List<Homework> sameClassHks = homeworkService.getByExerciseId(hk.getExerciseId());
			if (CollectionUtils.isEmpty(sameClassHks)) {
				diaClassLatestHk.setClassRank(1);
			} else {
				int rank = 1;
				for (Homework s : sameClassHks) {
					if (s.getId().equals(hk.getId())) {
						diaClassLatestHk.setClassRank(rank);
					} else {
						diaClassLatestHkRepo.execute("$ymUpdateByHomeworkId",
								Params.param("homeworkId", s.getId()).put("rank", rank));
					}

					rank++;
				}

			}

			diaClassLatestHkRepo.save(diaClassLatestHk);
		}

		List<Map> hkQuestions = homeworkService.findHomeworkQuestion(homeworkIds, null);
		List<Map> hkQuestions_15 = new ArrayList<Map>();
		List<Map> hkQuestions_7 = new ArrayList<Map>();
		List<Long> homeworkIds_15 = new ArrayList<Long>();
		List<Long> homeworkIds_7 = new ArrayList<Long>();
		homeworkIds_15 = homeworkIds.size() >= 15 ? homeworkIds.subList(0, 15) : homeworkIds;
		homeworkIds_7 = homeworkIds.size() >= 7 ? homeworkIds.subList(0, 7) : homeworkIds;
		for (Map map : hkQuestions) {
			Long homeworkId = Long.parseLong(String.valueOf(map.get("homework_id")));
			if (homeworkIds_15.contains(homeworkId)) {
				hkQuestions_15.add(map);
			}
			if (homeworkIds_7.contains(homeworkId)) {
				hkQuestions_7.add(map);
			}
		}
		diagnosticKpService.doKnowledgeStat(classId, teacherId, hkQuestions, false, 30);
		diagnosticKpService.doKnowledgeStat(classId, teacherId, hkQuestions_15, false, 15);
		diagnosticKpService.doKnowledgeStat(classId, teacherId, hkQuestions_7, false, 7);

		// 统计班级最新作业知识点相关情况
		if (hkId != null) {
			List<Map> hkQuestion = homeworkService.findHomeworkQuestion(hkId);
			// 统计作业对应的知识点数据
			diagnosticKpService.doKnowledgeStat(classId, teacherId, hkQuestion, true, 30);
		}

	}

	@Override
	@Transactional
	@Deprecated
	public void staticByHolidayHomework(long id, long classId, long createId) {
		List<Map> questionMap = holidayHomeworkService.findHolidayHkItemQuestion(id);

		// 统计作业对应的知识点数据
		diagnosticKpService.doKnowledgeStat(classId, createId, questionMap, true, 30);
	}

}

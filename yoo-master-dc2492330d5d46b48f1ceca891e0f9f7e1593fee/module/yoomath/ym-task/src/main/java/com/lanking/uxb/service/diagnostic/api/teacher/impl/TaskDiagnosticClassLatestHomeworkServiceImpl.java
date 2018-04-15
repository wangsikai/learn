package com.lanking.uxb.service.diagnostic.api.teacher.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassLatestHomework;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.diagnostic.api.StaticHomeworkService;
import com.lanking.uxb.service.diagnostic.api.teacher.TaskDiagnosticClassLatestHomeworkService;

/**
 * 班级-最近n次作业记录服务实现.
 * 
 * @author wlche
 * 
 * @since 教师端 v1.3.0，2017-6-30 数据跟随相关整理
 *
 */
@Service
@Transactional(readOnly = true)
public class TaskDiagnosticClassLatestHomeworkServiceImpl implements TaskDiagnosticClassLatestHomeworkService {

	@Autowired
	private StaticHomeworkService homeworkService;

	@Autowired
	@Qualifier("DiagnosticClassLatestHomeworkRepo")
	private Repo<DiagnosticClassLatestHomework, Long> diaClassLatestHkRepo;

	@Override
	@Transactional
	public void deleteByClassId(long classId) {
		diaClassLatestHkRepo.execute("$ymDeleteByClassId", Params.param("classId", classId));
	}

	@Override
	@Transactional
	public void saveDiagnosticClassLatestHomeworks(List<Homework> latestHks) {
		Set<Long> exerciseIds = new HashSet<Long>();
		for (Homework hk : latestHks) {
			if (hk.getExerciseId() != null) {
				exerciseIds.add(hk.getExerciseId());
			}
		}

		// 批量查找最近n次作业练习对应的作业集合
		Map<Long, List<Homework>> exerciseHomeworkMap = homeworkService.mgetByExerciseIds(exerciseIds);

		for (Homework hk : latestHks) {
			// 统计班级最新作业情况
			DiagnosticClassLatestHomework diaClassLatestHk = new DiagnosticClassLatestHomework();
			diaClassLatestHk.setClassId(hk.getHomeworkClassId());
			diaClassLatestHk.setDifficulty(hk.getDifficulty());
			diaClassLatestHk.setHomeworkId(hk.getId());
			diaClassLatestHk.setName(hk.getName());
			diaClassLatestHk.setRightRate(hk.getRightRate());
			diaClassLatestHk.setStartTime(hk.getStartTime());

			List<Homework> sameClassHks = exerciseHomeworkMap.get(hk.getExerciseId());
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
	}
}

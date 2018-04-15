package com.lanking.uxb.service.ranking.api.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionClassStat;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionSchoolStat;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.ranking.api.StaticDoQuestionSchoolStatService;

@Transactional(readOnly = true)
@Service
public class StaticDoQuestionSchoolStatServiceImpl implements StaticDoQuestionSchoolStatService {

	@Autowired
	@Qualifier("DoQuestionSchoolStatRepo")
	private Repo<DoQuestionSchoolStat, Long> doQuestionSchoolStatRepo;

	@Autowired
	@Qualifier("DoQuestionClassStatRepo")
	private Repo<DoQuestionClassStat, Long> doQuestionClassStatRepo;

	@Override
	public CursorPage<Long, DoQuestionClassStat> queryDoQuestionClassStat(CursorPageable<Long> pageable) {
		return doQuestionClassStatRepo.find("$taskQueryDoQuestionClassStat").fetch(pageable);
	}

	@Transactional(readOnly = false)
	@Override
	public void staticDoQuestionClassStat(List<DoQuestionClassStat> classStats) {
		Map<String, DoQuestionSchoolStat> map = Maps.newHashMap();
		for (DoQuestionClassStat classStat : classStats) {
			int day0 = classStat.getDay();
			long schoolId = classStat.getSchoolId();
			int phaseCode = classStat.getPhaseCode();
			long classId = classStat.getClassId();
			String key = day0 + "_" + schoolId + "_" + phaseCode + "_" + classId;
			Params params = Params.param("day0", day0).put("schoolId", schoolId).put("phaseCode", phaseCode)
					.put("classId", classId);
			DoQuestionSchoolStat schoolStat = map.get(key);
			if (schoolStat == null) {
				schoolStat = doQuestionSchoolStatRepo.find("$taskFindOne", params).get();
			}
			if (schoolStat == null) {
				schoolStat = new DoQuestionSchoolStat();
				schoolStat.setDoCount(0);
				schoolStat.setRightCount(0);
				schoolStat.setStatus(Status.DISABLED);
				schoolStat.setRightRate(BigDecimal.valueOf(0));
			}
			schoolStat.setSchoolId(schoolId);
			schoolStat.setClassId(classId);
			schoolStat.setPhaseCode(phaseCode);
			schoolStat.setDay(day0);

			schoolStat.setDoCount(schoolStat.getDoCount() + classStat.getDoCount());
			schoolStat.setRightCount(schoolStat.getRightCount() + classStat.getRightCount());
			if (schoolStat.getRightCount() == 0) {
				schoolStat.setRightRate(BigDecimal.valueOf(0));
			} else {
				schoolStat.setRightRate(BigDecimal.valueOf(schoolStat.getRightCount() * 100f / schoolStat.getDoCount())
						.setScale(0, BigDecimal.ROUND_HALF_UP));
			}
			map.put(key, schoolStat);
		}
		doQuestionSchoolStatRepo.save(map.values());
	}

	@Transactional(readOnly = false)
	@Override
	public void refreshDoQuestionSchoolStat() {
		doQuestionSchoolStatRepo.execute("$taskDeleteOldDoQuestionSchoolStat");
		doQuestionSchoolStatRepo.execute("$taskEnableNewDoQuestionSchoolStat");
	}

}

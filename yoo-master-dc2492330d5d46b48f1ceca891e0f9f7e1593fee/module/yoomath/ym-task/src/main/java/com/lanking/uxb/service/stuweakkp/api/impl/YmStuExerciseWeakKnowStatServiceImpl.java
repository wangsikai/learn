package com.lanking.uxb.service.stuweakkp.api.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.yoomath.stat.StudentExerciseKnowpoint;
import com.lanking.cloud.domain.yoomath.stat.StudentExerciseWeakKnowpointStat;
import com.lanking.cloud.sdk.data.CursorGetter;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.stuweakkp.api.YmStuExerciseWeakKnowStatService;

/**
 * @see YmStuExerciseWeakKnowStatService
 * @author xinyu.zhou
 * @since 2.6.0
 */
@Service
@Transactional(readOnly = true)
public class YmStuExerciseWeakKnowStatServiceImpl implements YmStuExerciseWeakKnowStatService {

	@Autowired
	@Qualifier("StudentExerciseKnowpointRepo")
	private Repo<StudentExerciseKnowpoint, Long> exerciseRepo;

	@Autowired
	@Qualifier("StudentExerciseWeakKnowpointStatRepo")
	private Repo<StudentExerciseWeakKnowpointStat, Long> weakStatRepo;
	@Autowired
	private KnowledgePointService knowledgePointService;

	@SuppressWarnings("rawtypes")
	@Transactional
	@Override
	public void saveWeakStat(List<Map> weakList) {
		Long studentId = 0L;
		Long knowpointCode = 0L;
		Double rightRate = 0.0;
		List<Long> codes = new ArrayList<Long>();
		for (Map map : weakList) {
			knowpointCode = Long.parseLong(String.valueOf(map.get("knowpoint_code")));
			codes.add(knowpointCode);
		}
		Map<Long, KnowledgePoint> kpMap = knowledgePointService.mget(codes);
		for (Map map : weakList) {
			studentId = Long.parseLong(String.valueOf(map.get("student_id")));
			knowpointCode = Long.parseLong(String.valueOf(map.get("knowpoint_code")));
			rightRate = Double.parseDouble(String.valueOf(map.get("rightrate")));
			StudentExerciseWeakKnowpointStat weakStat = new StudentExerciseWeakKnowpointStat();
			weakStat.setCreateAt(new Date());
			weakStat.setStudentId(studentId);
			weakStat.setKnowpointCode(knowpointCode);
			weakStat.setRightRate(BigDecimal.valueOf(rightRate * 100));
			weakStat.setSubjectCode(kpMap.get(knowpointCode).getSubjectCode());
			weakStat.setPhaseCode(kpMap.get(knowpointCode).getPhaseCode());
			weakStatRepo.save(weakStat);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public CursorPage<Long, Map> queryStuWeak(CursorPageable<Long> pageable, Double minRate, int version) {
		return exerciseRepo.find("$queryStuWeak", Params.param("minRate", minRate).put("version", version)).fetch(
				pageable, Map.class, new CursorGetter<Long, Map>() {
					@Override
					public Long getCursor(Map bean) {
						return Long.parseLong(String.valueOf(bean.get("id")));
					}
				});
	}

	@Transactional
	@Override
	public void clearWeakStat() {
		weakStatRepo.execute("$ymClearWeakStat");
	}
}

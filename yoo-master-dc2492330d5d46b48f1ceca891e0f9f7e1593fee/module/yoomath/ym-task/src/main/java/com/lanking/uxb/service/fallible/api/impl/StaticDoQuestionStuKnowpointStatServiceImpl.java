package com.lanking.uxb.service.fallible.api.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionStuKnowpointStat;
import com.lanking.cloud.domain.yoomath.stat.StudentQuestionAnswer;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.fallible.api.StaticDoQuestionStuKnowpointStatService;

@Transactional(readOnly = true)
@Service
public class StaticDoQuestionStuKnowpointStatServiceImpl implements StaticDoQuestionStuKnowpointStatService {

	@Autowired
	@Qualifier("StudentQuestionAnswerRepo")
	private Repo<StudentQuestionAnswer, Long> studentQuestionAnswerRepo;

	@Autowired
	@Qualifier("DoQuestionStuKnowpointStatRepo")
	private Repo<DoQuestionStuKnowpointStat, Long> doQuestionStuKnowpointStatRepo;

	@Transactional
	@SuppressWarnings("rawtypes")
	@Override
	public void stat(Collection<Long> studentIds, boolean isFirstDayInMonth) {
		Date startTime = null;
		Date endTime = null;
		if (isFirstDayInMonth) {
			startTime = StaticDoQuestionStuNaturalMonthStatServiceImpl.getMonthStartTime(5);
			endTime = StaticDoQuestionStuNaturalMonthStatServiceImpl.getMonthEndTime(0);
		} else {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			endTime = calendar.getTime();
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			startTime = calendar.getTime();
		}
		Params params = Params.param("startTime", startTime).put("endTime", endTime);
		List<Map> list = studentQuestionAnswerRepo.find("$taskStaticDoQuestionStudentKpStat",
				params.put("studentIds", studentIds)).list(Map.class);
		// student_id + knowpoint_code 为主键
		Map<String, DoQuestionStuKnowpointStat> stats = new HashMap<String, DoQuestionStuKnowpointStat>(list.size());
		if (CollectionUtils.isNotEmpty(list)) {
			for (Map map : list) {
				Long studentId = ((BigInteger) map.get("student_id")).longValue();
				long cou = ((BigInteger) map.get("cou")).longValue();
				Long knowledgeCode = ((BigInteger) map.get("knowledge_code")).longValue();
				int result = ((Byte) map.get("result")).intValue();
				DoQuestionStuKnowpointStat stat = stats.get(studentId.toString() + knowledgeCode.toString());
				if (stat == null) {
					stat = new DoQuestionStuKnowpointStat();
					stat.setStudentId(studentId);
					stat.setDoCount(0);
					stat.setRightCount(0);
					stat.setRightRate(BigDecimal.valueOf(0));
					stat.setKnowpointCode(knowledgeCode);
					stat.setCreateAt(new Date());
				}
				stat.setDoCount(stat.getDoCount() + cou);
				if (result == 1) {
					stat.setRightCount(stat.getRightCount() + cou);
				}
				if (stat.getRightCount() == 0) {
					stat.setRightRate(BigDecimal.valueOf(0));
				} else {
					stat.setRightRate(BigDecimal.valueOf(stat.getRightCount() * 100f / stat.getDoCount()).setScale(0,
							BigDecimal.ROUND_HALF_UP));
				}
				stats.put(studentId.toString() + knowledgeCode.toString(), stat);
			}
			if (stats.size() > 0) {
				if (!isFirstDayInMonth) {
					for (DoQuestionStuKnowpointStat stat : stats.values()) {
						DoQuestionStuKnowpointStat kpStat = this.get(stat.getKnowpointCode(), stat.getStudentId());
						if (kpStat != null) {
							kpStat.setDoCount(kpStat.getDoCount() + stat.getDoCount());
							kpStat.setRightCount(kpStat.getRightCount() + stat.getRightCount());
							if (kpStat.getRightCount() == 0) {
								kpStat.setRightRate(BigDecimal.valueOf(0));
							} else {
								kpStat.setRightRate(BigDecimal.valueOf(
										kpStat.getRightCount() * 100f / kpStat.getDoCount()).setScale(0,
										BigDecimal.ROUND_HALF_UP));
							}
							doQuestionStuKnowpointStatRepo.save(kpStat);
						} else {
							doQuestionStuKnowpointStatRepo.save(stat);
						}
					}
				} else {
					doQuestionStuKnowpointStatRepo.save(stats.values());
				}
			}
		}
	}

	@Transactional
	@Override
	public void deleteKpStat() {
		doQuestionStuKnowpointStatRepo.execute("$taskDeleteKpStat");
	}

	@Override
	public DoQuestionStuKnowpointStat get(long knowpointCode, long studentId) {
		return doQuestionStuKnowpointStatRepo.find("$getDoQuestionStuKnowpointStat",
				Params.param("knowpointCode", knowpointCode).put("studentId", studentId)).get();
	}
}

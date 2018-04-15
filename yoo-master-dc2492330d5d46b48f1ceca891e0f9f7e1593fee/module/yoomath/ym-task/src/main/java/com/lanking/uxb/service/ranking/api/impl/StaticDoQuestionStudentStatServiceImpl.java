package com.lanking.uxb.service.ranking.api.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionStudentStat;
import com.lanking.cloud.domain.yoomath.stat.StudentQuestionAnswer;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.CursorGetter;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.ranking.api.StaticDoQuestionStudentStatService;

@Transactional(readOnly = true)
@Service
public class StaticDoQuestionStudentStatServiceImpl implements StaticDoQuestionStudentStatService {

	@Autowired
	@Qualifier("UserRepo")
	private Repo<User, Long> userRepo;

	@Autowired
	@Qualifier("StudentQuestionAnswerRepo")
	private Repo<StudentQuestionAnswer, Long> studentQuestionAnswerRepo;

	@Autowired
	@Qualifier("DoQuestionStudentStatRepo")
	private Repo<DoQuestionStudentStat, Long> doQuestionStudentStatRepo;

	@SuppressWarnings("rawtypes")
	@Override
	public CursorPage<Long, Map> queryUserId(CursorPageable<Long> pageable) {
		return userRepo.find("$taskQueryUser").fetch(pageable, Map.class, new CursorGetter<Long, Map>() {
			@Override
			public Long getCursor(Map bean) {
				return Long.parseLong(String.valueOf(bean.get("id")));
			}
		});
	}

	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = false)
	@Override
	public void staticDoQuestionStudentStat(int day, List<Long> userIds) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -day);
		List<Map> list = studentQuestionAnswerRepo.find("$taskStaticDoQuestionStudentStat",
				Params.param("startAt", calendar.getTime()).put("userIds", userIds)).list(Map.class);
		if (CollectionUtils.isNotEmpty(list)) {
			Map<Long, DoQuestionStudentStat> stats = new HashMap<Long, DoQuestionStudentStat>(list.size());
			for (Map map : list) {
				long studentId = ((BigInteger) map.get("student_id")).longValue();
				long cou = ((BigInteger) map.get("cou")).longValue();
				int result = ((Byte) map.get("result")).intValue();
				DoQuestionStudentStat stat = stats.get(studentId);
				if (stat == null) {
					stat = new DoQuestionStudentStat();
					stat.setUserId(studentId);
					stat.setDay(day);
					stat.setStatus(Status.DISABLED);

					stat.setDoCount(0);
					stat.setRightCount(0);
					stat.setRightRate(BigDecimal.valueOf(0));
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
				stats.put(studentId, stat);
			}
			if (stats.size() > 0) {
				doQuestionStudentStatRepo.save(stats.values());
			}
		}
	}

	@Transactional(readOnly = false)
	@Override
	public void refreshDoQuestionStudentStat() {
		doQuestionStudentStatRepo.execute("$taskDeleteOldDoQuestionStudentStat");
		doQuestionStudentStatRepo.execute("$taskEnableNewDoQuestionStudentStat");
	}
}

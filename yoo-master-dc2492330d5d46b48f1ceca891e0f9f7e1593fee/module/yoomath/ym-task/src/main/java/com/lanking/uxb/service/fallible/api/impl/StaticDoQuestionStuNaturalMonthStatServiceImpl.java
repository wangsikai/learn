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
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionStuNaturalMonthStat;
import com.lanking.cloud.domain.yoomath.stat.StudentQuestionAnswer;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.fallible.api.StaticDoQuestionStuNaturalMonthStatService;

@Transactional(readOnly = true)
@Service
public class StaticDoQuestionStuNaturalMonthStatServiceImpl implements StaticDoQuestionStuNaturalMonthStatService {

	@Autowired
	@Qualifier("StudentRepo")
	private Repo<Student, Long> studentRepo;

	@Autowired
	@Qualifier("StudentQuestionAnswerRepo")
	private Repo<StudentQuestionAnswer, Long> studentQuestionAnswerRepo;

	@Autowired
	@Qualifier("DoQuestionStuNaturalMonthStatRepo")
	private Repo<DoQuestionStuNaturalMonthStat, Long> doQuestionStuNaturalMonthStatRepo;

	@Transactional
	@SuppressWarnings({ "rawtypes", "static-access" })
	@Override
	public void statByMonth(int month, Collection<Long> studentIds) {
		Date startTime = this.getMonthStartTime(month);
		Date endTime = this.getMonthEndTime(month);
		Params params = Params.param("startTime", startTime).put("endTime", endTime);
		List<Map> list = studentQuestionAnswerRepo.find("$taskStaticDoQuestionStudentStat2",
				params.put("studentIds", studentIds)).list(Map.class);
		if (CollectionUtils.isNotEmpty(list)) {
			Map<Long, DoQuestionStuNaturalMonthStat> stats = new HashMap<Long, DoQuestionStuNaturalMonthStat>(
					list.size());
			for (Map map : list) {
				long studentId = ((BigInteger) map.get("student_id")).longValue();
				long cou = ((BigInteger) map.get("cou")).longValue();
				int result = ((Byte) map.get("result")).intValue();
				DoQuestionStuNaturalMonthStat stat = stats.get(studentId);
				if (stat == null) {
					stat = new DoQuestionStuNaturalMonthStat();
					stat.setStudentId(studentId);
					stat.setMonth(month + 1);
					stat.setDoCount(0);
					stat.setRightCount(0);
					stat.setRightRate(BigDecimal.valueOf(0));
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
				stats.put(studentId, stat);
			}
			if (stats.size() > 0) {
				doQuestionStuNaturalMonthStatRepo.save(stats.values());
			}
		}
	}

	/**
	 * 获取某个月的开始时间
	 * 
	 * @param month
	 * @return
	 */
	public static Date getMonthStartTime(int month) {
		Calendar cal = Calendar.getInstance();
		int nowMonth = cal.get(Calendar.MONTH) + 1;
		cal.set(Calendar.MONTH, nowMonth - month);
		cal.add(Calendar.MONTH, -1);
		int MinDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), MinDay, 00, 00, 00);
		return cal.getTime();
	}

	/**
	 * 获取某个月的结束时间
	 * 
	 * @param month
	 * @return
	 */
	public static Date getMonthEndTime(int month) {
		Calendar cal = Calendar.getInstance();
		int nowMonth = cal.get(Calendar.MONTH) + 1;
		cal.set(Calendar.MONTH, nowMonth - month);
		cal.add(Calendar.MONTH, -1);
		// 得到一个月最最后一天日期(31/30/29/28)
		int MaxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), MaxDay, 23, 59, 59);
		return cal.getTime();
	}

	@Override
	public CursorPage<Long, Long> getAllStudent(CursorPageable<Long> cursorPageable) {
		return studentRepo.find("$taskGetAllByPage").fetch(cursorPageable, Long.class);
	}

	@Transactional
	@Override
	public void deleteMonthStat() {
		doQuestionStuNaturalMonthStatRepo.execute("$taskDeleteMonthStat");
	}
}

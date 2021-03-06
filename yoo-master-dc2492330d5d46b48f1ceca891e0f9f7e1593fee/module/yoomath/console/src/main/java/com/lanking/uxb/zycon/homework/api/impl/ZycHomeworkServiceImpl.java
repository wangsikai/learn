package com.lanking.uxb.zycon.homework.api.impl;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.zycon.homework.api.ZycHomeworkService;

/**
 * @author xinyu.zhou
 * @since yoomath V1.5
 */
@Transactional(readOnly = true)
@Service
public class ZycHomeworkServiceImpl implements ZycHomeworkService {

	@Autowired
	@Qualifier("HomeworkRepo")
	private Repo<Homework, Long> repo;

	@Override
	public Homework get(long id) {
		return repo.get(id);
	}

	@Override
	public Map<Long, Homework> mget(Collection<Long> ids) {
		return repo.mget(ids);
	}

	@Override
	@Transactional
	public void remove(long homeworkId) {
		repo.execute("$zycRemoveHomework", Params.param("homeworkId", homeworkId));
	}

	@Override
	public List<Homework> listByTime(long userId, long clazzId, int year, int month) {
		// 获取当月第一天时间和最后一天
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		// 下个月第一天减去一天就是这个月最后一天
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date endTime = cal.getTime();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date beginTime = cal.getTime();
		return repo.find(
				"$zycListByTime",
				Params.param("beginTime", beginTime).put("endTime", endTime).put("createId", userId)
						.put("clazzId", clazzId)).list();
	}

	@Override
	public Homework getFirstIssuedHomeworkInMonth(int year, int month, long classId) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		// 下个月第一天减去一天就是这个月最后一天
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date endTime = cal.getTime();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date beginTime = cal.getTime();
		List<Homework> hkList = repo.find("$zycGetFirstIssuedHomeworkInMonth",
				Params.param("beginTime", beginTime).put("endTime", endTime).put("classId", classId)).list();
		if (CollectionUtils.isEmpty(hkList)) {
			return null;
		}
		return hkList.get(0);
	}
}

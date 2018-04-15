package com.lanking.cloud.job.stuHkRateStat.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoomath.stat.StudentHomeworkRightRateStat;
import com.lanking.cloud.job.stuHkRateStat.dao.StudentHomeworkRightRateStatDAO;
import com.lanking.cloud.job.stuHkRateStat.dao.StudentHomeworkStatDAO;
import com.lanking.cloud.job.stuHkRateStat.dao.UserDAO;
import com.lanking.cloud.job.stuHkRateStat.service.TaskStuHkRateStatService;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;

@Transactional(readOnly = true)
@Service
public class TaskStuHkRateStatServiceImpl implements TaskStuHkRateStatService {

	@Autowired
	private StudentHomeworkStatDAO stuHkStatDAO;
	@Autowired
	private StudentHomeworkRightRateStatDAO stuHkRateDAO;
	@Qualifier("stuHkRateStatUserDAO")
	@Autowired
	private UserDAO userDAO;

	private Logger logger = LoggerFactory.getLogger(TaskStuHkRateStatServiceImpl.class);

	@Override
	public Integer getAvgRate(Long studentId) {
		return stuHkStatDAO.getAvgRate(studentId);
	}

	@Transactional
	@Override
	public void stat(Long studentId) {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		Integer avgRate = this.getAvgRate(studentId);
		if (avgRate != null) {
			StudentHomeworkRightRateStat rateStat = new StudentHomeworkRightRateStat();
			rateStat.setUserId(studentId);
			rateStat.setRightRate(BigDecimal.valueOf(avgRate));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				rateStat.setStatisticsTime(format.parse(year + "-" + month + "-" + day));
				stuHkRateDAO.save(rateStat);
			} catch (Exception e) {
				logger.error("TaskStuHkRateStatServiceImpl time format error", e);
			}
		}

	}

	@Transactional
	@SuppressWarnings("rawtypes")
	@Override
	public void taskStuHkRateStat(int modVal) {
		int fetchCount = 200;
		CursorPage<Long, Map> ids = userDAO.queryUserId(CP.cursor(Long.MAX_VALUE, fetchCount), modVal);

		while (ids.isNotEmpty()) {
			// 统计每个用户
			for (Map map : ids) {
				Long studentId = ((BigInteger) map.get("id")).longValue();
				this.stat(studentId);
			}
			// 获取下一页
			ids = userDAO.queryUserId(CP.cursor(ids.getNextCursor(), fetchCount), modVal);
		}
	}

}

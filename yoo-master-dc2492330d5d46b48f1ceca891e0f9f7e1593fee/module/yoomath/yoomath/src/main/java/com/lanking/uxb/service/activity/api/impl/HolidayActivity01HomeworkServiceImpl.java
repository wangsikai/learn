package com.lanking.uxb.service.activity.api.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Cfg;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Homework;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01User;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.activity.api.HolidayActivity01HomeworkService;
import com.lanking.uxb.service.activity.api.HolidayActivity01UserService;

/**
 * 假期活动01参与活动的用户布置的作业接口实现
 * 
 * @author peng.zhao
 *
 */
@Service
@Transactional(readOnly = true)
public class HolidayActivity01HomeworkServiceImpl implements HolidayActivity01HomeworkService {

	@Autowired
	@Qualifier("HolidayActivity01HomeworkRepo")
	private Repo<HolidayActivity01Homework, Long> repo;
	@Autowired
	@Qualifier("HolidayActivity01UserRepo")
	private Repo<HolidayActivity01User, Long> holidayActivity01UserRepo;
	@Autowired
	private HolidayActivity01UserService holidayActivity01UserService;

	@Override
	public Map<String, Object> getPercent(long code, long userId, HolidayActivity01Cfg cfg) {
		Map<String, Object> retMap = new HashMap<>();
		HolidayActivity01User user = holidayActivity01UserService.getByUserId(code, userId);
		int homeWorkCount = 0;
		List<HolidayActivity01Homework> hList = repo
				.find("$findHolidayActivity01ClassUser", Params.param("userId", userId).put("activityCode", code))
				.list();
		if (hList != null) {
			homeWorkCount = hList.size();
		}
		// 按照50-80,80-100,100进行拆分
		List<HolidayActivity01Homework> lowList = new ArrayList<>();
		List<HolidayActivity01Homework> middleList = new ArrayList<>();
		List<HolidayActivity01Homework> highList = new ArrayList<>();
		// 提交率阈值
		List<Integer> submitRateThreshold = cfg.getSubmitRateThreshold();
		int lowRate = submitRateThreshold.get(2);
		int middleRate = submitRateThreshold.get(1);
		int highRate = submitRateThreshold.get(0);
		for (HolidayActivity01Homework h : hList) {
			if (h.getSubmitRate() != null) {
				if (h.getSubmitRate().compareTo(BigDecimal.valueOf(lowRate)) >= 0
						&& h.getSubmitRate().compareTo(BigDecimal.valueOf(middleRate)) < 0) {
					lowList.add(h);
				}
				if (h.getSubmitRate().compareTo(BigDecimal.valueOf(middleRate)) >= 0
						&& h.getSubmitRate().compareTo(BigDecimal.valueOf(highRate)) < 0) {
					middleList.add(h);
				}
				if (h.getSubmitRate().compareTo(BigDecimal.valueOf(highRate)) == 0) {
					highList.add(h);
				}
			}
		}

		retMap.put("homeWorkCount", homeWorkCount);
		retMap.put("lowSize", lowList.size());
		retMap.put("middleSize", middleList.size());
		retMap.put("highSize", highList.size());
		retMap.put("luckyDraw", user == null ? 0 : (user.getLuckyDraw() - user.getCostLuckyDraw()));
		return retMap;
	}

	@Transactional
	@Override
	public void create(Collection<HolidayActivity01Homework> homeworks, HolidayActivity01 holidayActivity01,
			long teacherId) {
		if (homeworks.size() > 0) {
			HolidayActivity01Homework one = homeworks.iterator().next();
			// 处理开始时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String startAt = sdf.format(one.getStartTime());
			Params countParams = Params.param("activityCode", holidayActivity01.getCode())
					.put("userId", one.getUserId()).put("startAt", startAt);
			long count = repo.find("$countSameStartTimeHomework", countParams).count();
			// 获取抽奖次数
			int luckyDrawOneHomework = holidayActivity01.getCfg().getLuckyDrawOneHomework(); // 一次作业获得的抽奖次数
			for (HolidayActivity01Homework holidayActivity01Homework : homeworks) {
				count++;
				if (count < 1000) {
					holidayActivity01Homework.setStartTime(
							new Date(holidayActivity01Homework.getStartTime().getTime() / 1000 * 1000 + count));
				}
				holidayActivity01Homework.setLuckyDraw(luckyDrawOneHomework);
			}
			repo.save(homeworks);

			// 增加抽奖次数
			Params params = Params.param("activityCode", holidayActivity01.getCode()).put("userId", one.getUserId())
					.put("num", homeworks.size() * luckyDrawOneHomework).put("isCost", false).put("isNew", false);
			holidayActivity01UserRepo.execute("$taskAddUserLuckyDraw", params);
		}
	}

	@Override
	public Long countHk(long code, long userId) {
		return repo.find("$countHk", Params.param("code", code).put("userId", userId)).get(Long.class);
	}
}

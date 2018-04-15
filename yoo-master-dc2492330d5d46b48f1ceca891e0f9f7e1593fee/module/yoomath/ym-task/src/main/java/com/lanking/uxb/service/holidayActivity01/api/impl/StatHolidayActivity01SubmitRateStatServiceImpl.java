package com.lanking.uxb.service.holidayActivity01.api.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Class;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01ClassUser;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Homework;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01User;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.holidayActivity01.api.StatHolidayActivity01SubmitRateStatService;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;

@Service
@Transactional
public class StatHolidayActivity01SubmitRateStatServiceImpl implements StatHolidayActivity01SubmitRateStatService {

	@Autowired
	@Qualifier("HolidayActivity01HomeworkRepo")
	private Repo<HolidayActivity01Homework, Long> holidayActivity01HomeworkRepo;

	@Autowired
	@Qualifier("HolidayActivity01ClassRepo")
	private Repo<HolidayActivity01Class, Long> holidayActivity01ClassRepo;

	@Autowired
	@Qualifier("HolidayActivity01ClassUserRepo")
	private Repo<HolidayActivity01ClassUser, Long> holidayActivity01ClassUserRepo;

	@Autowired
	@Qualifier("HolidayActivity01Repo")
	private Repo<HolidayActivity01, Long> holidayActivity01Repo;

	@Autowired
	@Qualifier("HomeworkRepo")
	private Repo<Homework, Long> homeworkRepo;

	@Autowired
	@Qualifier("HolidayActivity01UserRepo")
	private Repo<HolidayActivity01User, Long> holidayActivity01UserRepo;

	@Autowired
	private HomeworkService homeworkService;

	@Autowired
	private StudentHomeworkService stuHomeworkService;

	@Transactional
	@Override
	public void statHkSubmitRate(List<Long> ids) {
		// 活动集合，处理用户获奖次数使用
		Map<Long, HolidayActivity01> activityMap = new HashMap<Long, HolidayActivity01>();
		List<HolidayActivity01Homework> list = holidayActivity01HomeworkRepo.find("$mgetHolidayActivity01Homework",
				Params.param("ids", ids)).list();
		List<Long> hkIds = new ArrayList<Long>();
		for (HolidayActivity01Homework activityhk : list) {
			hkIds.add(activityhk.getHomeworkId());
		}
		Map<Long, Homework> hkMap = homeworkService.mget(hkIds);
		for (HolidayActivity01Homework activityhk : list) {
			Homework h = hkMap.get(activityhk.getHomeworkId());
			BigDecimal submitRate = null;
			if (h != null && h.getDistributeCount() == 0) {
				submitRate = BigDecimal.valueOf(0);
			} else if (h != null){
				submitRate = BigDecimal.valueOf(Double.valueOf(h.getCommitCount() * 100) / h.getDistributeCount())
						.setScale(0, BigDecimal.ROUND_HALF_UP);
			}
			activityhk.setSubmitRate(submitRate);

			// 处理用户获奖次数
			this.handleUserLuckyDraw(activityhk.getActivityCode(), activityhk, activityMap, submitRate == null ? 0.0 : submitRate.doubleValue());

			holidayActivity01HomeworkRepo.save(activityhk);
		}
	}

	/**
	 * 处理用户的获奖次数.
	 * 
	 * @param activityCode
	 *            活动CODE
	 * @param activityhk
	 *            作业
	 * @param activityMap
	 *            活动集合
	 * @param submitRate
	 *            提交率
	 */
	@Transactional
	private void handleUserLuckyDraw(Long activityCode, HolidayActivity01Homework activityhk,
			Map<Long, HolidayActivity01> activityMap, double submitRate) {

		// 只有有效的作业才处理获奖次数
		if (activityhk.getValid() == null || !activityhk.getValid()) {
			return;
		}

		HolidayActivity01 activity = activityMap.get(activityhk.getActivityCode());
		if (activity == null) {
			activity = holidayActivity01Repo.get(activityhk.getActivityCode());
			activityMap.put(activity.getCode(), activity);
		}
		int luckyDrawOneHomework = activity.getCfg().getLuckyDrawOneHomework(); // 一次作业获得的抽奖次数
		List<Integer> submitRateThreshold = activity.getCfg().getSubmitRateThreshold(); // 提交率
		List<Integer> luckyDrawThreshold = activity.getCfg().getLuckyDrawThreshold(); // 获奖次数
		if (CollectionUtils.isEmpty(submitRateThreshold) || CollectionUtils.isEmpty(luckyDrawThreshold)
				|| submitRateThreshold.size() != luckyDrawThreshold.size()) {
			return;
		}
		Collections.sort(submitRateThreshold);
		Collections.sort(luckyDrawThreshold);

		if (activityhk.getLuckyDraw() == 0) {
			activityhk.setLuckyDraw(luckyDrawOneHomework); // 修复初始的抽奖次数
		}
		int addDraw = 0; // 本次需要增加的抽奖次数
		int newLuckyDraw = 0; // 当前作业需要记录的抽奖次数
		int mdraw = activityhk.getLuckyDraw() - luckyDrawOneHomework; // 排除布置作业的抽奖次数
		for (int i = 0; i < submitRateThreshold.size(); i++) {
			int rate = submitRateThreshold.get(i);
			int draw = luckyDrawThreshold.get(i);
			if (submitRate >= rate && mdraw < draw) {
				// 若当前提交率大于等于规则的提交率，并且当前作业因提交率获得抽奖次数小于规则抽奖次数
				addDraw = draw - mdraw;
				newLuckyDraw = draw + luckyDrawOneHomework;
			}
		}
		if (addDraw != 0) {
			activityhk.setLuckyDraw(newLuckyDraw);

			// 增加抽奖次数
			Params params = Params.param("activityCode", activityCode).put("userId", activityhk.getUserId())
					.put("num", addDraw).put("isCost", false).put("isNew", true);
			holidayActivity01UserRepo.execute("$taskAddUserLuckyDraw", params);
		}
	}

	@Transactional
	@Override
	public void statClassSubmitRate(List<Long> ids) {
		// 重置班级的提交率
		this.resetClassSubmitRate(ids);
		List<HolidayActivity01Class> list = holidayActivity01ClassRepo.find("$mgetHolidayActivity01Class",
				Params.param("ids", ids)).list();
		for (HolidayActivity01Class hclass : list) {
			Integer temp = this.classSubmitRate(hclass.getUserId(), hclass.getClassId(), hclass.getActivityCode());
			BigDecimal submitRate = BigDecimal.valueOf(temp == null ? 0 : temp);
			hclass.setSubmitRate(submitRate);
			holidayActivity01ClassRepo.save(hclass);
		}
	}

	@Transactional
	@Override
	public void statClassUserSubmitRate(List<Long> ids) {
		List<HolidayActivity01ClassUser> list = holidayActivity01ClassUserRepo.find("$mgetHolidayActivity01ClassUser",
				Params.param("ids", ids)).list();
		for (HolidayActivity01ClassUser classUser : list) {
			Integer temp = this.userSubmitRate(classUser.getUserId(), classUser.getClassId(),
					classUser.getActivityCode());
			BigDecimal submitRate = BigDecimal.valueOf(temp == null ? 0 : temp);
			classUser.setSubmitRate(submitRate);
			holidayActivity01ClassUserRepo.save(classUser);
		}
	}

	@Override
	public CursorPage<Long, Long> findHolidayActivity01HkList(CursorPageable<Long> cursorPageable) {
		return holidayActivity01HomeworkRepo.find("$taskGetAllByPage").fetch(cursorPageable, Long.class);
	}

	@Override
	public CursorPage<Long, Long> findClassUserList(CursorPageable<Long> cursorPageable) {
		return holidayActivity01ClassUserRepo.find("$findClassUserList").fetch(cursorPageable, Long.class);
	}

	@Transactional
	@Override
	public void resetClassSubmitRate(List<Long> ids) {
		holidayActivity01ClassRepo.execute("$resetClassSubmitRate", Params.param("ids", ids));
	}

	@Override
	public HolidayActivity01Class get(long activityCode, long classId) {
		return holidayActivity01ClassRepo.find("$getHoliday01Class", Params.param("activityCode", activityCode)).get();
	}

	@Override
	public Integer userSubmitRate(long studentId, long classId, long code) {
		return holidayActivity01HomeworkRepo.find("$userSubmitRate",
				Params.param("studentId", studentId).put("classId", classId).put("code", code)).get(Integer.class);
	}

	@Override
	public CursorPage<Long, Long> findClassList(CursorPageable<Long> cursorPageable) {
		return holidayActivity01ClassRepo.find("$findClassList").fetch(cursorPageable, Long.class);
	}

	@Override
	public Integer classSubmitRate(long teacherId, long classId, long code) {
		return holidayActivity01HomeworkRepo.find("$classSubmitRate",
				Params.param("teacherId", teacherId).put("classId", classId).put("code", code)).get(Integer.class);
	}
}

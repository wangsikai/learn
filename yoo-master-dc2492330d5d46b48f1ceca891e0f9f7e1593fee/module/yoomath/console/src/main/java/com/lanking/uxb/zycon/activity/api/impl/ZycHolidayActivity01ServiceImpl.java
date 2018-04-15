package com.lanking.uxb.zycon.activity.api.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Cfg;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Exercise;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01ExerciseQuestion;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Grade;
import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotterySeason;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.activity.api.ZycHolidayActivity01Service;
import com.lanking.uxb.zycon.mall.form.LotterySeasonForm;

/**
 * 寒假作业活动相关接口实现.
 * 
 * @since 教师端 v1.2.0
 *
 */
@Transactional(readOnly = true)
@Service
public class ZycHolidayActivity01ServiceImpl implements ZycHolidayActivity01Service {

	@Autowired
	@Qualifier("HolidayActivity01Repo")
	private Repo<HolidayActivity01, Long> holidayActivity01Repo;

	@Autowired
	@Qualifier("HolidayActivity01ExerciseRepo")
	private Repo<HolidayActivity01Exercise, Long> holidayActivity01ExerciseRepo;

	@Autowired
	@Qualifier("HolidayActivity01ExerciseQuestionRepo")
	private Repo<HolidayActivity01ExerciseQuestion, Long> holidayActivity01ExerciseQuestionRepo;

	@Autowired
	@Qualifier("CoinsLotterySeasonRepo")
	private Repo<CoinsLotterySeason, Long> lotterSeasonrepo;

	@Override
	public HolidayActivity01 get(long id) {
		return holidayActivity01Repo.get(id);
	}

	@Transactional
	@Override
	public void init(long seasonId,LotterySeasonForm form) {
		HolidayActivity01 h = new HolidayActivity01();
		CoinsLotterySeason season = lotterSeasonrepo.get(seasonId);
		h.setCode(countHolidayActivity01() + 1);
		h.setCreateAt(new Date());
		h.setName(season.getName());
		h.setStartTime(season.getStartTime());
		h.setEndTime(season.getEndTime());
		HolidayActivity01Cfg cfg = new HolidayActivity01Cfg();
		cfg.setCode(countHolidayActivity01() + 1);
		cfg.setLuckyDrawOneHomework(1);
		cfg.setMinClassStudents(20);
		List<Integer> submitRateThreshold = new ArrayList<Integer>();
		submitRateThreshold.add(100);
		submitRateThreshold.add(80);
		submitRateThreshold.add(50);
		cfg.setSubmitRateThreshold(submitRateThreshold);
		List<Integer> luckyDrawThreshold = new ArrayList<Integer>();
		luckyDrawThreshold.add(10);
		luckyDrawThreshold.add(8);
		luckyDrawThreshold.add(6);
		cfg.setLuckyDrawThreshold(luckyDrawThreshold);
		cfg.setSeasonId(seasonId);
		// 初中(苏科版15/沪科新版27/人教新版30/23华师大版/19鲁五四新版/31北师新版 柴林森提供)
		List<Integer> textbookCategoryCodes2 = Lists.newArrayList();
		textbookCategoryCodes2.add(15);
		textbookCategoryCodes2.add(27);
		textbookCategoryCodes2.add(30);
		textbookCategoryCodes2.add(23);
		textbookCategoryCodes2.add(19);
		textbookCategoryCodes2.add(31);
		cfg.setTextbookCategoryCodes2(textbookCategoryCodes2);
//		// 高中(苏教版12/人教A版13/人教B版14 柴林森提供 2017.6.23)
//		List<Integer> textbookCategoryCodes3 = Lists.newArrayList();
//		textbookCategoryCodes3.add(12);
//		textbookCategoryCodes3.add(13);
//		textbookCategoryCodes3.add(14);
//		cfg.setTextbookCategoryCodes3(textbookCategoryCodes3);
		List<HolidayActivity01Grade> grades2 = Lists.newArrayList();
		grades2.add(HolidayActivity01Grade.PHASE_2_1);
		grades2.add(HolidayActivity01Grade.PHASE_2_2);
		grades2.add(HolidayActivity01Grade.PHASE_2_3);
		grades2.add(HolidayActivity01Grade.PHASE_2_4);
		cfg.setGrades2(grades2);
//		List<HolidayActivity01Grade> grades3 = Lists.newArrayList();
//		grades3.add(HolidayActivity01Grade.PHASE_3_1);
//		grades3.add(HolidayActivity01Grade.PHASE_3_2);
//		cfg.setGrades3(grades3);
		// 时间段，小宋完善
		List<List<Long>> periods = new ArrayList<List<Long>>();
		List<Long> period1 = Lists.newArrayList();
		
		Date phase1Start = null;
		Date phase1End = null;
		Date phase2Start = null;
		Date phase2End = null;
		Date phase3Start = null;
		Date phase3End = null;
		Date phase4Start = null;
		Date phase4End = null;
		
		SimpleDateFormat simFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		try {
			phase1Start = form.getPhase1Start() == null ? null : simFormat.parse(form.getPhase1Start());
			phase1End = form.getPhase1End() == null ? null : simFormat.parse(form.getPhase1End());
			phase2Start = form.getPhase2Start() == null ? null : simFormat.parse(form.getPhase2Start());
			phase2End = form.getPhase2End() == null ? null : simFormat.parse(form.getPhase2End());
			phase3Start = form.getPhase3Start() == null ? null : simFormat.parse(form.getPhase3Start());
			phase3End = form.getPhase3End() == null ? null : simFormat.parse(form.getPhase3End());
			phase4Start = form.getPhase4Start() == null ? null : simFormat.parse(form.getPhase4Start());
			phase4End = form.getPhase4End() == null ? null : simFormat.parse(form.getPhase4End());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		period1.add(phase1Start.getTime());
		period1.add(phase1End.getTime());
		periods.add(period1);
		List<Long> period2 = Lists.newArrayList();
		period2.add(phase2Start.getTime());
		period2.add(phase2End.getTime());
		periods.add(period2);
		List<Long> period3 = Lists.newArrayList();
		period3.add(phase3Start.getTime());
		period3.add(phase3End.getTime());
		periods.add(period3);
		List<Long> period4 = Lists.newArrayList();
		period4.add(phase4Start.getTime());
		period4.add(phase4End.getTime());
		periods.add(period4);
		cfg.setPeriods(periods);
		h.setCfg(cfg);
		holidayActivity01Repo.save(h);
	}

	@Override
	@Transactional
	public void deleteAllExerciseAndQuestion(Long activityCode) {
		holidayActivity01ExerciseQuestionRepo.execute("$deleteAll", Params.param("activityCode", activityCode));
		holidayActivity01ExerciseRepo.execute("$deleteAll", Params.param("activityCode", activityCode));
	}

	@Override
	public Long countHolidayActivity01() {
		return holidayActivity01Repo.find("$countHolidayActivity01").count();
	}
}

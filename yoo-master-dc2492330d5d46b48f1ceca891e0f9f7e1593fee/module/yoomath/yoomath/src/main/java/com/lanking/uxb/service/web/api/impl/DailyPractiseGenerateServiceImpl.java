package com.lanking.uxb.service.web.api.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPracticeDifficulty;
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPracticeSettings;
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPractise;
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPractisePeriod;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.web.api.DailyPractiseGenerateService;
import com.lanking.uxb.service.web.api.PullQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyDailyPracticeSettingsService;
import com.lanking.uxb.service.zuoye.api.ZyDailyPractisePeriodService;
import com.lanking.uxb.service.zuoye.api.ZyDailyPractiseQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyDailyPractiseService;
import com.lanking.uxb.service.zuoye.form.DailyPractiseSaveForm;
import com.lanking.uxb.service.zuoye.form.PullQuestionForm;
import com.lanking.uxb.service.zuoye.form.PullQuestionType;

/**
 * @see DailyPractiseGenerateService
 * @author xinyu.zhou
 * @since 2.0.3
 */
@Service
public class DailyPractiseGenerateServiceImpl implements DailyPractiseGenerateService {
	private static Logger logger = LoggerFactory.getLogger(DailyPractiseGenerateServiceImpl.class);

	@Autowired
	private ZyDailyPractiseService dailyPractiseService;
	@Autowired
	private PullQuestionService pullQuestionService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private ZyDailyPractisePeriodService dailyPractisePeriodService;
	@Autowired
	private ZyDailyPracticeSettingsService dailyPracticeSettingsService;
	@Autowired
	private ZyDailyPractiseQuestionService dailyPractiseQuestionService;

	@Override
	public Map<String, Object> generate(Student student, int count) {

		boolean setting = true;

		Long sectionCode = null;
		Long nextSectionCode = null;
		DailyPractisePeriod dailyPractisePeriod = null;
		Map<String, Object> retMap = new HashMap<String, Object>(3);
		retMap.put("finish", false);
		DailyPracticeSettings settings = dailyPracticeSettingsService.findByTextbookCode(student.getId(),
				student.getTextbookCode());

		DailyPractise dailyPractise = dailyPractiseService.getLatest(student.getId(), student.getTextbookCode());
		if (dailyPractise == null || !DateUtils.isSameDay(new Date(), dailyPractise.getCreateAt())) {

			if (settings == null) {
				Section section = sectionService.getFirstLeafSectionByTextbookCode(student.getTextbookCode());
				// 学生所选的教材下面并没有章节这个特殊情况处理： 用户显示未设置进度，未完成
				if (section == null) {
					logger.info("the student choose textbook has not section code: {}", student.getTextbookCode());
					retMap.put("setting", false);
					return retMap;
				} else {
					sectionCode = sectionService.getFirstLeafSectionByTextbookCode(student.getTextbookCode()).getCode();
					nextSectionCode = getNextSectionCode(sectionCode, student.getTextbookCode());
				}
			} else {
				sectionCode = settings.getCurSectionCode();
				// 当此时cur_section_code为null的时候，说明本教材已经练习完成
				if (sectionCode != null) {
					dailyPractisePeriod = dailyPractisePeriodService.findBySectionCode(sectionCode);
					if (dailyPractisePeriod == null) {
						// 此章节只有一次练习
						nextSectionCode = getNextSectionCode(sectionCode, student.getTextbookCode());
						settings.setCurPeriod(1);
					} else {
						if (settings.getCurPeriod().equals(dailyPractisePeriod.getPeriod())) {
							// 此章节所有课时全部练习完成
							nextSectionCode = getNextSectionCode(sectionCode, student.getTextbookCode());
							settings.setCurPeriod(1);
						} else if (settings.getCurPeriod() < dailyPractisePeriod.getPeriod()) {
							// 此章节的上一个课时已经做完，开始进行下一个课时
							settings.setCurPeriod(settings.getCurPeriod() + 1);
						}
					}
				}
			}

			if (sectionCode != null) {
				if (settings == null) {
					try {
						settings = dailyPracticeSettingsService.set(student.getId(), student.getTextbookCode(), null,
								null);
						settings.setCurPeriod(1);
					} catch (Exception e) {
						logger.info("set daily practice settings error:", e);
					}
				}
				if (settings != null) {
					// 设置设置下一个章节码
					settings.setCurSectionCode(nextSectionCode);

					BigDecimal minDifficulty = null;
					BigDecimal maxDifficulty = null;

					DailyPracticeDifficulty difficultySetting = null;
					if (settings.getDifficulty() == null) {
						difficultySetting = ZyDailyPracticeSettingsService.DEF;
					} else {
						difficultySetting = settings.getDifficulty();
					}

					if (difficultySetting == DailyPracticeDifficulty.LEVEL_1) {
						minDifficulty = new BigDecimal(0.8);
						maxDifficulty = new BigDecimal(1.0);
					} else if (difficultySetting == DailyPracticeDifficulty.LEVEL_2) {
						minDifficulty = new BigDecimal(0.6);
						maxDifficulty = new BigDecimal(0.8);
					} else if (difficultySetting == DailyPracticeDifficulty.LEVEL_3) {
						minDifficulty = new BigDecimal(0.3);
						maxDifficulty = new BigDecimal(0.6);
					} else {
						minDifficulty = new BigDecimal(0);
						maxDifficulty = new BigDecimal(0.3);
					}

					// 本章节已经做过的题目不再进行操作
					List<Long> pulledQuestionIds = dailyPractiseQuestionService.findPulledQuestionIds(student.getId(),
							sectionCode);

					PullQuestionForm form = new PullQuestionForm();
					form.setCount(count);
					form.setMinDifficulty(minDifficulty);
					form.setMaxDifficulty(maxDifficulty);
					form.setSectionCode(sectionCode);
					form.setType(PullQuestionType.DAILY_PRACTISE);
					form.setqIds(pulledQuestionIds);

					double difficulty = 0d;
					List<Long> qIds = pullQuestionService.pull(form);
					// 当没有符合难度区间的题目时则取所有难度的题目
					if (CollectionUtils.isEmpty(qIds)) {
						form.setMaxDifficulty(new BigDecimal(1.0));
						form.setMinDifficulty(new BigDecimal(0));
						qIds = pullQuestionService.pull(form);
					}
					if (CollectionUtils.isNotEmpty(qIds)) {
						// 计算平均难度
						List<Question> questions = questionService.mgetList(qIds);
						for (Question q : questions) {
							difficulty += q.getDifficulty();
						}

						difficulty = difficulty / qIds.size();
						difficulty = new BigDecimal(difficulty).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

						/*
						 * 每日练习名称 规则: 1. 没有设置章节课时或章节课时只有1课时 -> 不显示课时名 例如: 1.1.集合
						 * 2. 如果章节课时数 > 1课时 -> 显示课时数 例如: 1.1.集合(1) 1.2.数组(2)
						 */
						String name;
						if (dailyPractisePeriod == null || dailyPractisePeriod.getPeriod() <= 1) {
							name = sectionService.get(sectionCode).getName();
						} else {
							name = sectionService.get(sectionCode).getName() + "(" + settings.getCurPeriod() + ")";
						}

						DailyPractiseSaveForm practiseSaveForm = new DailyPractiseSaveForm(student.getId(),
								sectionCode, qIds, difficulty, name, settings, student.getTextbookCode());
						dailyPractise = dailyPractiseService.save(practiseSaveForm);
					}

					retMap.put("finish", false);
				}
			} else {
				retMap.put("finish", true);
			}
		}
		if (settings != null && settings.getSectionCode() == null) {
			setting = false;
		}

		retMap.put("practise", dailyPractise);
		retMap.put("setting", setting);
		retMap.put("sectionCode", sectionCode);

		return retMap;
	}

	/**
	 * 得到此章节下一章节
	 *
	 * @param sectionCode
	 *            章节码
	 * @return 下一章节码
	 */
	private Long getNextSectionCode(long sectionCode, int textbookCode) {
		Section section = sectionService.getNextSection(sectionCode, textbookCode);
		if (section == null) {
			return null;
		}
		return section.getCode();
	}
}

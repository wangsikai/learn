package com.lanking.uxb.service.zuoye.api.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPracticeDifficulty;
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPracticeSettings;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.zuoye.api.ZyDailyPracticeSettingsService;

@Transactional(readOnly = true)
@Service
public class ZyDailyPracticeSettingsServiceImpl implements ZyDailyPracticeSettingsService {

	@Autowired
	@Qualifier("DailyPracticeSettingsRepo")
	Repo<DailyPracticeSettings, Long> dailyPracticeSettingsRepo;

	@Override
	public DailyPracticeSettings findByTextbookCode(long userId, int textbookCode) {
		return dailyPracticeSettingsRepo
				.find("$zyFindByTextbookCode", Params.param("userId", userId).put("textbookCode", textbookCode)).get();

	}

	@Transactional
	@Override
	public DailyPracticeSettings set(long userId, int textbookCode, DailyPracticeDifficulty difficulty,
			Long sectionCode) {
		DailyPracticeSettings settings = findByTextbookCode(userId, textbookCode);
		if (settings == null) {
			settings = new DailyPracticeSettings();
			settings.setTextbookCode(textbookCode);
			settings.setUserId(userId);
			settings.setCreateAt(new Date());
			if (difficulty != null) {
				settings.setDifficulty(difficulty);
			}
			if (sectionCode != null) {
				settings.setSectionCode(sectionCode);
				settings.setCurSectionCode(sectionCode);
			}
			settings.setUpdateAt(settings.getCreateAt());
		} else {
			if (difficulty != null) {
				settings.setDifficulty(difficulty);
			}
			if (sectionCode != null) {
				settings.setSectionCode(sectionCode);
				settings.setCurSectionCode(sectionCode);
			}
			settings.setUpdateAt(new Date());
		}
		return dailyPracticeSettingsRepo.save(settings);
	}

	@Override
	@Transactional
	public DailyPracticeSettings set(long id, Long curSectionCode, int curPeriod) {
		DailyPracticeSettings settings = dailyPracticeSettingsRepo.get(id);
		settings.setCurPeriod(curPeriod);
		settings.setCurSectionCode(curSectionCode);
		return dailyPracticeSettingsRepo.save(settings);
	}
}

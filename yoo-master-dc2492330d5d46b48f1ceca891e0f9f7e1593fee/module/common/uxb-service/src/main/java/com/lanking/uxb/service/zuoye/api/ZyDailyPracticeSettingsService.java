package com.lanking.uxb.service.zuoye.api;

import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPracticeDifficulty;
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPracticeSettings;

/**
 * 每日练设置接口
 * 
 * @since yoomath(mobile) V1.0.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年11月23日
 */
public interface ZyDailyPracticeSettingsService {

	DailyPracticeDifficulty DEF = DailyPracticeDifficulty.LEVEL_2;

	/**
	 * 获取用户某个教材下的每日练设置
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param userId
	 *            用户ID
	 * @param textbookCode
	 *            教材代码
	 * @return 每日练设置
	 */
	DailyPracticeSettings findByTextbookCode(long userId, int textbookCode);

	/**
	 * @since yoomath(mobile) V1.0.0
	 * @param userId
	 *            用户ID
	 * @param textbookCode
	 *            教材代码
	 * @param difficulty
	 *            难度
	 * @param sectionCode
	 *            章节代码
	 * @return 每日练设置
	 */
	DailyPracticeSettings set(long userId, int textbookCode, DailyPracticeDifficulty difficulty, Long sectionCode);

	/**
	 * 更新练习的当前章节和课时
	 *
	 * @param id
	 *            设置的id
	 * @param curSectionCode
	 *            当前练习的章节码
	 * @param curPeriod
	 *            当前课时
	 * @return {@link DailyPracticeSettings}
	 */
	DailyPracticeSettings set(long id, Long curSectionCode, int curPeriod);
}

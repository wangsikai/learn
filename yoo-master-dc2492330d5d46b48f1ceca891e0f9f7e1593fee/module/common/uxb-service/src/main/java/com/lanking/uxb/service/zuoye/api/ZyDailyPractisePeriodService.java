package com.lanking.uxb.service.zuoye.api;

import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPractisePeriod;

/**
 * 章节课时
 *
 * @author xinyu.zhou
 * @since yoomath(mobile) V1.0.0
 */
public interface ZyDailyPractisePeriodService {

	/**
	 * 保存章节课时
	 *
	 * @param sectionCode
	 *            章节码
	 * @param period
	 *            课时数
	 * @return {@link DailyPractisePeriod}
	 */
	DailyPractisePeriod save(long sectionCode, int period);

	/**
	 * 根据章节码查找章节课时
	 *
	 * @param sectionCode
	 *            章节码
	 * @return {@link DailyPractisePeriod}
	 */
	DailyPractisePeriod findBySectionCode(long sectionCode);
}

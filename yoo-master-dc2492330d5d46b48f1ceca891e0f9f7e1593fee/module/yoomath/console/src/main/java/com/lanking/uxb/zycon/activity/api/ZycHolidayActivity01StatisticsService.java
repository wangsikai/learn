package com.lanking.uxb.zycon.activity.api;

import java.util.Map;

import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.zycon.activity.form.ZycHolidayActivity01Form;

/**
 * 暑期作业活动-统计相关
 * 
 * @since 教师端 v1.2.0
 *
 */
public interface ZycHolidayActivity01StatisticsService {

	Page<Map> queryActivityRank(ZycHolidayActivity01Form form, Pageable p);

}

package com.lanking.uxb.service.report.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.report.cache.LearnReportCacheService;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 学情报告相关
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月30日
 */
@RestController
@RequestMapping("zy/m/s/report/learning")
public class ZyMStuLearningReportController {

	@Autowired
	private LearnReportCacheService learnReportCacheService;

	/**
	 * 删除首页上当前学业报告的提示
	 * 
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "M")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "clearCurMonthTips", method = { RequestMethod.POST, RequestMethod.GET })
	public Value clearCurMonthTips() {
		learnReportCacheService.setCurWeekTips(Security.getUserId(), true);
		return new Value();
	}
}

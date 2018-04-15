package com.lanking.cloud.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.job.correctUserDayStat.service.UserMonthStatService;
import com.lanking.cloud.sdk.value.Value;

@RestController
@RequestMapping("user/month/stat")
public class UserMonthStatJobTestController {

	@Autowired
	private UserMonthStatService userMonthStatService;
	
	@RequestMapping(value = "start", method = { RequestMethod.POST, RequestMethod.GET })
	public Value start() {
		userMonthStatService.doUserMonthStat();
		
		return new Value();
	}

}

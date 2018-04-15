package com.lanking.cloud.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.job.correctUserDayStat.service.UserDayStatService;
import com.lanking.cloud.sdk.value.Value;

@RestController
@RequestMapping("user/day/stat")
public class UserDayStatJobTestController {

	@Autowired
	private UserDayStatService userDayStatService;
	
	@RequestMapping(value = "start", method = { RequestMethod.POST, RequestMethod.GET })
	public Value start() {
		userDayStatService.doUserDayStat();
		
		return new Value();
	}
}

package com.lanking.uxb.service.temp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.temp.api.CoinsGrowthLogHandleService;

@RestController
@RequestMapping(value = "temp")
public class TempHandleController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private CoinsGrowthLogHandleService logHandleService;

	/**
	 * 金币成长值--班级人数添加至20人时历史数据处理<br>
	 * 1.处理丢失的金币、成长值日志<br>
	 * 2.处理用户相应的成长值和金币值
	 * 
	 * @return
	 */
	@RequestMapping(value = "coinsLogHandle", method = { RequestMethod.GET, RequestMethod.POST })
	public Value coinsLogHandle() {
		logger.info("---【开始】班级人数添加至20人时历史数据处理---");
		logHandleService.handle();
		logger.info("---【结束】班级人数添加至20人时历史数据处理---");
		return new Value();
	}
}

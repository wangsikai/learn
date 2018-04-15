package com.lanking.stuWeekReport.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.stuWeekReport.service.TaskStuWeekReportService;

/**
 * 重跑学生周报告接口
 *
 * @author xinyu.zhou
 * @since yoomath V1.4
 */
@RestController
@RequestMapping(value = "zyc/weekreport/")
public class ZycWeekreportController {
	@Autowired
	private TaskStuWeekReportService reportService;

	private Logger logger = LoggerFactory.getLogger(ZycWeekreportController.class);
	

	/**
	 * 重新生成某段时间的学生报告
	 *
	 * @return Value
	 */
	@RequestMapping(value = "regenerate")
	public Value regenerate(String time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
			format.setLenient(false);
			format.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return new Value(new IllegalArgException());
		} 
		 
		logger.info("开始重跑");
		reportService.statClassWeek(time);
		reportService.statWeek(time);
		logger.info("结束重跑");
		
		return new Value();
	}

}

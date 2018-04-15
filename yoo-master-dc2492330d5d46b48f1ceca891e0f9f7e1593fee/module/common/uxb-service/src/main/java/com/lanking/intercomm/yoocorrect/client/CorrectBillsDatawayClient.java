package com.lanking.intercomm.yoocorrect.client;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lanking.intercomm.yoocorrect.dto.CorrectBillDayData;

/**
 * 用户流水数据.
 * 
 * @author wanlong.che
 *
 */
@FeignClient("${correct-server.name}")
public interface CorrectBillsDatawayClient {

	/**
	 * 查询用户日流水.
	 * 
	 * @param userId
	 *            批改用户ID
	 * @param lastBillDate
	 *            上一条日期时间戳
	 * @param size
	 *            获取条数
	 * @return
	 */
	@RequestMapping(value = "/bill/queryDayBillData", method = { RequestMethod.POST })
	List<CorrectBillDayData> queryDayBillData(@RequestParam("userId") Long userId,
			@RequestParam("lastBillDate") Long lastBillDate, @RequestParam("size") Integer size);
}

package com.lanking.intercomm.yoocorrect.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.intercomm.yoocorrect.client.CorrectBillsDatawayClient;
import com.lanking.intercomm.yoocorrect.dto.CorrectBillDayData;

/**
 * 用户流水数据.
 * 
 * @author wanlong.che
 *
 */
@Component
public class CorrectBillsDatawayService {

	@Autowired
	private CorrectBillsDatawayClient correctBillsDatawayClient;

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
	public List<CorrectBillDayData> queryDayBillData(long userId, Long lastBillDate, int size) {
		List<CorrectBillDayData> correctBillDayDatas = correctBillsDatawayClient.queryDayBillData(userId, lastBillDate,
				size);

		return correctBillDayDatas;
	}
}

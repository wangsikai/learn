package com.lanking.uxb.channelSales.channel.api;

import java.util.List;
import java.util.Map;

/**
 * 学生汇总接口
 * 
 * @author wangsenhao
 *
 */
public interface CsAggStudentService {

	/**
	 * 查询渠道下学校的学生数、会员数、转化率等，按转化率排序
	 * 
	 * @param channelCode
	 * @param schoolName
	 * @param phaseCode
	 * @return
	 */
	List<Map<String, Object>> queryList(Integer channelCode, String schoolName, Integer phaseCode);

}

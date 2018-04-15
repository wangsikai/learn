package com.lanking.uxb.channelSales.channel.api;

import java.util.List;
import java.util.Map;

/**
 * 班级作业汇总表历史接口
 * 
 * @author wangsenhao
 *
 */
public interface CsAggClassHomeworkHisService {

	/**
	 * 
	 * @param classId
	 * @param day0
	 * @return
	 */
	List<Map<String, Object>> hkStat(Long classId, Integer day0);
}

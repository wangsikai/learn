package com.lanking.uxb.channelSales.channel.api;

import java.util.List;
import java.util.Map;

/**
 * 班级汇总表接口
 * 
 * @author wangsenhao
 *
 */
public interface CsAggClassHomeworkService {

	/**
	 * 查询渠道下学校的作业数
	 * 
	 * @param schoolName
	 * @param channelCode
	 * @param phaseCode
	 * @return
	 */
	List<Map<String, Object>> queryList(String schoolName, Integer channelCode, Integer phaseCode);

	/**
	 * 通过班级查询作业量，提交率，人数等数据
	 * 
	 * @param classId
	 * @return
	 */
	Map<String, Object> queryMapByClass(Long classId);

}

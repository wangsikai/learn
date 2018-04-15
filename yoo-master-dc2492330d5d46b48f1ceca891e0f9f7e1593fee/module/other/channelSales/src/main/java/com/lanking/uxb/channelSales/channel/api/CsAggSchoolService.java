package com.lanking.uxb.channelSales.channel.api;

import java.util.List;
import java.util.Map;

/**
 * 学校汇总接口
 * 
 * @author zemin.song
 *
 */
public interface CsAggSchoolService {

	/**
	 * 学校学生数据统计
	 * 
	 * @param schoolCode
	 *            学校code
	 * @param channelId
	 *            渠道ID
	 * @return
	 */
	Map<String, Object> studentStat(String schoolCode, Integer channelId);

	/**
	 * 学校老师数据统计
	 * 
	 * @param schoolCode
	 *            学校code
	 * @param channelId
	 *            渠道ID
	 * @return
	 */
	Map<String, Object> teacherStat(String schoolCode, Integer channelId);

	/**
	 * 班级布置作业量统计
	 * 
	 * @param schoolCode
	 *            学校code
	 * @param channelId
	 *            渠道ID
	 * @return
	 */
	Long getClazzHkCount(String schoolCode, Integer channelId);

	/**
	 * 作业未提交统计
	 * 
	 * @param schoolCode
	 * @return
	 */
	Map<String, Object> getNohomeWorkCount(String schoolCode, Integer channelId);

	/**
	 * 作业率统计
	 * 
	 * @param schoolCode
	 * @return
	 */
	List<Map<String, Object>> getAggHomeWork(String schoolCode, Integer channelId, Integer day);

	/**
	 * 活跃统计
	 * 
	 * @param schoolCode
	 * @return
	 */
	List<Map<String, Object>> getRegisterCount(String schoolCode, Integer channelId, Integer day0);

	/**
	 * 按年批量查找班级
	 * 
	 * @param schoolCode
	 * @return
	 */
	List<Map<String, Object>> getClazzDateByYeart(String schoolCode, Integer channelId, Integer year);

	/**
	 * 按天查找未布置作业的班级
	 * 
	 * @param schoolCode
	 * @return
	 */
	List<Map<String, Object>> getNoClazzDateByDay(String schoolCode, Integer channelId, Integer day0);

}

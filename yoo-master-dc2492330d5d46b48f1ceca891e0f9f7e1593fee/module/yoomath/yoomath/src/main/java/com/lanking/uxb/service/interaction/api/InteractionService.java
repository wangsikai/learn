package com.lanking.uxb.service.interaction.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.interaction.Interaction;
import com.lanking.cloud.domain.yoomath.interaction.InteractionStatus;
import com.lanking.cloud.domain.yoomath.interaction.InteractionType;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.service.interaction.value.VInteraction;

/**
 * 师生互动service
 * 
 * @since 2.0.3
 * @author wangsenhao
 *
 */
public interface InteractionService {
	/**
	 * 获取老师的师生互动
	 * 
	 * @param userId
	 * @param p
	 * @return
	 */
	Page<Interaction> query(Long userId, Pageable p);

	/**
	 * 获取首页的荣耀榜单,只获取表扬的
	 * 
	 * @param classIds
	 *            当前用户所在的班级集合
	 * @return
	 */
	List<Interaction> queryIndexHonourList(List<Long> classIds);

	/**
	 * 获取荣耀榜单(全部或者个人的)
	 * 
	 * @param classIds
	 * @param p
	 * @param userId
	 *            用户ID为空表示查所有的
	 * @return
	 */
	Page<Interaction> queryHonourList(List<Long> classIds, Pageable p, Long userId);

	/**
	 * 更新该条互动状态
	 * 
	 * @param id
	 * @param status
	 */
	void updateStatus(Long id, InteractionStatus status);

	/**
	 * 获取单条师生互动
	 * 
	 * @param id
	 * @return
	 */
	Interaction get(Long id);

	/**
	 * 获取首页师生互动
	 * 
	 * @param userId
	 * @return
	 */
	List<Interaction> queryIndex(Long userId);

	/**
	 * 首页删除记录
	 * 
	 * @param id
	 */
	void delIndexInteraction(Long id);

	/**
	 * 保存师生互动消息
	 * 
	 * @param classId
	 * @param studentId
	 * @param teacherId
	 * @param type
	 *            互动类型
	 * @param status
	 *            互动状态
	 */
	void saveInteraction(Long classId, Long studentId, Long teacherId, InteractionType type, InteractionStatus status,
			Integer p1, Integer p2);

	/**
	 * 师生互动，进步最快数据处理
	 * 
	 * @param classId
	 *            班级Id
	 */
	List<Long> mostImprovedHandle(Long classId);

	/**
	 * 师生互动，单次作业前五数据处理
	 * 
	 * @param homeworkId
	 *            作业Id
	 */
	void oneHomeworkTop5Handle(Long homeworkId);

	/**
	 * 师生互动，班级作业前五数据处理
	 * 
	 * @param classId
	 */
	void classHomeworkTop5Handle(Long classId);

	/**
	 * 师生互动，退步最快数据处理
	 * 
	 * @param classId
	 * 
	 * @param improveUserIds
	 *            进步最快的名单(可以为空)
	 */
	void mostBackwardHandle(Long classId, List<Long> improveUserIds);

	/**
	 * 师生互动，连续三次没有提交作业数据处理
	 * 
	 * @param classId
	 */
	void seriesNotsubmitHandle(Long homeworkId);

	/**
	 * 获取首页师生互动(新) 2016.12.12加
	 * 
	 * @param userId
	 * @return
	 */
	Map<Long, List<VInteraction>> findByClassIds(Long userId, List<Long> classIds);

}

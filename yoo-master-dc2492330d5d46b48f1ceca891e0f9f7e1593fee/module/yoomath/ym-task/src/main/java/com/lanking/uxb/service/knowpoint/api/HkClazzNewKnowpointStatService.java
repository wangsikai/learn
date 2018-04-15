package com.lanking.uxb.service.knowpoint.api;

/**
 * 普通作业，班级知识点统计(新知识点)
 * 
 * @since 2.6.0
 * @author wangsenhao
 *
 */
public interface HkClazzNewKnowpointStatService {

	/**
	 * 下发作业后统计此次作业的知识点数据
	 * 
	 * @since 2.6.0
	 * @param homeworkId
	 */
	void statisticAfterHomework(long homeworkId);

	/**
	 * 删除
	 */
	void deleteNewAll();
}

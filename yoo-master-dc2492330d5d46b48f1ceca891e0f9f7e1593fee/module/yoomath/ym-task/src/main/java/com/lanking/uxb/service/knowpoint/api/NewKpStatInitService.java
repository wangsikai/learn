package com.lanking.uxb.service.knowpoint.api;

import java.util.Date;

/**
 * 班级和学生新知识点统计初始化接口
 * 
 * @since 2.6.0
 * @author wangsenhao
 *
 */
public interface NewKpStatInitService {
	/**
	 * 初始化学生知识点
	 */
	void initStuKpStat(Date nowTime);

	/**
	 * 初始化班级知识点
	 */
	void initClazzKpStat(Date nowTime);
}

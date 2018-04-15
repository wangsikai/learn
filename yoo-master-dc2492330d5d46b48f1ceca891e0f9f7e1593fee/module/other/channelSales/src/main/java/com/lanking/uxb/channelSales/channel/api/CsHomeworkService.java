package com.lanking.uxb.channelSales.channel.api;

import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

/**
 * 普通作业接口
 * 
 * @author wangsenhao
 *
 */
public interface CsHomeworkService {
	/**
	 * 获取班级对应的作业列表
	 * 
	 * @param classId
	 * @return
	 */

	Page<Homework> query(CsHomeworkQuery query, Pageable p);
}

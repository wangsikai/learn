/**
 * 
 */
package com.lanking.uxb.zycon.activity.api;

import com.lanking.cloud.domain.yoo.activity.exam001.ExamActivity001;

/**
 * 期末活动接口
 * 
 * @author <a href="mailto:qiuxue.jiang@elanking.com">qiuxue.jiang</a>
 */
public interface ZycExaminationActivity01Service {

	/**
	 * 获取期末活动
	 * 
	 * @param code
	 *            活动code
	 */
	ExamActivity001 get(long code);

}

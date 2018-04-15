package com.lanking.uxb.service.examactivity001.api;

import com.lanking.cloud.domain.yoo.activity.exam001.ExamActivity001;

/**
 * 期末活动基本信息接口
 * 
 * @author qiuxue.jiang
 *
 */
public interface ExamActivity01Service {

	/**
	 * 获取期末活动对象
	 * 
	 * @param code
	 *            页面链接传过来的活动 code
	 * @return
	 */
	ExamActivity001 getActivity(Long code);

}

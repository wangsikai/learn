/**
 * 
 */
package com.lanking.uxb.zycon.activity.api;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivity;

/**
 * 科举活动接口
 * 
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 */
public interface ZycImperialExaminationActivityService {

	/**
	 * 获取科举活动
	 * 
	 * @param code
	 *            活动code
	 */
	ImperialExaminationActivity get(long code);

}

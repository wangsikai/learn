/**
 * 
 */
package com.lanking.uxb.zycon.activity.api;

import java.util.Map;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityRank;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.zycon.activity.form.ZycActivityUserForm;

/**
 * 科举活动相关接口
 * 
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 */
public interface ZycImperialExaminationActivityRankService {

	/**
	 * 活动排名
	 * 
	 * @param activityCode
	 *            活动code
	 */
	Page<Map> queryActivityRank(ZycActivityUserForm form, Pageable p);

	int updateScore(long rankId, int manualScore);

	ImperialExaminationActivityRank get(long rankId);

}

/**
 * 
 */
package com.lanking.uxb.zycon.activity.api;

import java.util.Map;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityLottery;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.zycon.activity.form.ZycActivityUserForm;

/**
 * 科举活动最终排名接口
 * 
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 */
public interface ZycImperialExaminationActivityLotteryUserService {

	Page<Map> queryActivityLotteryUser(ZycActivityUserForm form, Pageable p);

	/**
	 * 获取中奖用户
	 * 
	 * @param id
	 * @return
	 */
	ImperialExaminationActivityLottery get(Long id);


	/**
	 * 更新领奖状态
	 * 
	 * @param id
	 * @param received
	 * @return
	 */
	public void updateReceived(long id, Integer received);

}

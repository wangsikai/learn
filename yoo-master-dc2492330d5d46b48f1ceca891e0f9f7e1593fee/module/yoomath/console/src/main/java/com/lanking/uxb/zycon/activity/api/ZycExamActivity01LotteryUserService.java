/**
 * 
 */
package com.lanking.uxb.zycon.activity.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.activity.exam001.ExamActivity001UserQ;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.zycon.activity.form.ZycActivityUserForm;

/**
 * 期末活动后台接口
 * 
 * @author <a href="mailto:qiuxue.jiang@elanking.com">qiuxue.jiang</a>
 */
public interface ZycExamActivity01LotteryUserService {

	Page<Map> queryActivityLotteryUser(ZycActivityUserForm form, Pageable p);

	/**
	 * 获取用户中奖详情
	 * 
	 * @param id
	 * @return
	 */
	List<ExamActivity001UserQ> get(Long code,Long userId);
	
	/**
	 * 获取中奖用户总数
	 * 
	 * @param id
	 * @return
	 */
	Long getLotteryUserCount(Long code);
	
	/**
	 * 获取中奖Q点总数
	 * 
	 * @param id
	 * @return
	 */
	Long getLotteryTotalQ(Long code);


	/**
	 * 更新兑换状态
	 * 
	 * @param id
	 * @param received
	 * @return
	 */
	public void updateReceived(long id, Integer received);

}

package com.lanking.uxb.service.imperial.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityAward;

/**
 * 科举活动颁奖相关接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2017年4月5日
 */
public interface ImperialExaminationActivityAwardService {

	/**
	 * 获取颁奖列表数据.
	 * 
	 * @param code
	 *            活动CODE
	 * 
	 * @param num
	 *            获取个数
	 * @return
	 */
	List<ImperialExaminationActivityAward> awardList(long code, int num);

	/**
	 * 根据Id获取获奖排名信息.
	 * 
	 * @return
	 */
	ImperialExaminationActivityAward get(long id);

	/**
	 * 完善领奖信息.
	 * 
	 * @param awardId
	 *            获奖排名ID
	 * @param contact
	 *            联系人
	 * @param mobile
	 *            联系人电话
	 * @param address
	 *            联系地址
	 */
	void improve(long awardId, String contact, String mobile, String address);

	/**
	 * 获取用户的领奖信息.
	 * 
	 * @param code
	 *            活动CODE
	 * @param userId
	 *            用户ID
	 * @return
	 */
	ImperialExaminationActivityAward getByUser(long code, long userId);

	/**
	 * 获取颁奖列表数据.
	 * 
	 * @param code
	 *            活动CODE
	 * 
	 * @param num
	 *            获取个数
	 * @param room
	 *            考场
	 * @return
	 */
	List<ImperialExaminationActivityAward> awardListByRoom(long code, int num, int room);
}

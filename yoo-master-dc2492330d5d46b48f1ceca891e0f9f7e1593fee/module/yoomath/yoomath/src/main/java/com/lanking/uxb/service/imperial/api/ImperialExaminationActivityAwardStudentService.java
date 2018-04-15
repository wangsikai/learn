package com.lanking.uxb.service.imperial.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityAwardStudent;

/**
 * 科举活动学生颁奖相关接口.
 * 
 * @author peng.zhao
 *
 * @version 2017年11月7日
 */
public interface ImperialExaminationActivityAwardStudentService {

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
	List<ImperialExaminationActivityAwardStudent> awardList(long code, int num);

	/**
	 * 获取用户的领奖信息.
	 * 
	 * @param code
	 *            活动CODE
	 * @param userId
	 *            用户ID
	 * @return
	 */
	ImperialExaminationActivityAwardStudent getByUser(long code, long userId);

	/**
	 * 根据Id获取获奖排名信息.
	 * 
	 * @return
	 */
	ImperialExaminationActivityAwardStudent get(long id);

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
}

/**
 * 
 */
package com.lanking.uxb.zycon.activity.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityAward;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.zycon.activity.form.ZycActivityUserForm;

/**
 * 科举活动最终排名接口
 * 
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 */
public interface ZycImperialExaminationActivityAwardService {

	Page<Map> queryActivityAward(ZycActivityUserForm form, Pageable p);

	/**
	 * 获取Award
	 * 
	 * @param id
	 * @return
	 */
	ImperialExaminationActivityAward get(Long id);

	/**
	 * 获取Award
	 * 
	 * @param id
	 * @return
	 */
	ImperialExaminationActivityAward get(long code, Long userId);

	/**
	 * 冻结/解冻
	 * 
	 * @param id
	 * @param status
	 * @param room
	 * @return
	 */
	void updateStatus(long id, Long code, Status status,Integer room);

	/**
	 * 更新
	 * 
	 * @param id
	 * @param score
	 * @param doTime
	 * @param score
	 * @return
	 */
	void update(long id, int score, int doTime, long clazzId);

	/**
	 * 查询用户综合分数和平均时间
	 * 
	 * @param userId
	 * @return
	 */
	List<Map> getActivityClazzScore(long code, Long userId);
	
	/**
	 * 存储
	 * 
	 * @param awards
	 */
	void save(Collection<ImperialExaminationActivityAward> awards);

}

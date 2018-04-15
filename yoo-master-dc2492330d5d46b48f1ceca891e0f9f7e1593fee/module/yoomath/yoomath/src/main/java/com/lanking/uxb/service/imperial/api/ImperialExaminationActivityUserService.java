package com.lanking.uxb.service.imperial.api;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityUser;
import com.lanking.uxb.service.imperial.form.ImperialExaminationActivityUserForm;

/**
 * 科举报名接口
 * 
 * @author wangsenhao
 *
 */
public interface ImperialExaminationActivityUserService {

	/**
	 * 报名科举考试
	 * 
	 * @param form
	 */
	void signUp(ImperialExaminationActivityUserForm form);

	/**
	 * 获取用户报名数据.
	 * 
	 * @param code
	 *            活动code
	 * @param userId
	 *            用户ID.
	 * @return
	 */
	ImperialExaminationActivityUser getUser(long code, long userId);

}

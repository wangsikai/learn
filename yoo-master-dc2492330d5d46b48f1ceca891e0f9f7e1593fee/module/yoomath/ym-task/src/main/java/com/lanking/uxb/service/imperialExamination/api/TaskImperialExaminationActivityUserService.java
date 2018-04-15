package com.lanking.uxb.service.imperialExamination.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivity;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityUser;

public interface TaskImperialExaminationActivityUserService {

	/**
	 * 根据code查询报名用户
	 * 
	 * @param code
	 *            活动代码
	 * @return {@link ImperialExaminationActivity}
	 */
	List<ImperialExaminationActivityUser> get(long code, Integer room, Integer category);

}

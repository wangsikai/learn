package com.lanking.uxb.service.honor.api.impl.processor.userTask;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.uxb.service.honor.api.UserUpLevelService;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractUserTaskProcessor;

/**
 * 成就任务：
 *
 * 提升等级后任务
 *
 *
 * @author xinyu.zhou
 * @since 4.1.0
 */
@Component
public class UpLevelTaskProcessor extends AbstractUserTaskProcessor {
	@Autowired
	private UserUpLevelService upLevelService;

	@Override
	public boolean accept(int code) {
		return getCode() == code;
	}

	@Override
	public int getCode() {
		return 101020001;
	}

	public void process(UserTask userTask, long userId, Map<String, Object> params) {
		int level = (int) params.get("level");
		boolean reach = false;
		if (params != null && params.get("reach") != null) {
			reach = Boolean.parseBoolean(params.get("reach").toString());
		}
		UserHonor userHonor = (UserHonor) params.get("userHonor");
		upLevelService.updateLevel(userId, level, reach, userHonor);
	}
}

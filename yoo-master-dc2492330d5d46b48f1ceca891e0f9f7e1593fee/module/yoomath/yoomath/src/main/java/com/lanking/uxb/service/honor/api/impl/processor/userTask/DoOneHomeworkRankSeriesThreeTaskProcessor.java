package com.lanking.uxb.service.honor.api.impl.processor.userTask;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLogStatus;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskRuleCfg;
import com.lanking.uxb.service.honor.api.UserTaskLogService;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractUserTaskProcessor;
import com.lanking.uxb.service.honor.form.UserTaskLogUpdateForm;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;

/**
 * 近3次作业连续排名前5 code: 101020013
 *
 * @author xinyu.zhou
 * @since 4.1.0
 */
@Component
public class DoOneHomeworkRankSeriesThreeTaskProcessor extends AbstractUserTaskProcessor {
	@Autowired
	private UserTaskLogService userTaskLogService;
	@Autowired
	private StudentHomeworkService studentHomeworkService;

	private static final int LATELY_SIZE = 3;
	private static final int COMMIT_COUNT = 20;
	private static final int RANK = 5;

	@Override
	public boolean accept(int code) {
		return getCode() == code;
	}

	@Override
	public int getCode() {
		return 101020013;
	}

	public void process(UserTask userTask, long userId, Map<String, Object> params) {

		int commitCount = (int) params.get("commitCount");
		Integer rank = params.get("rank") == null ? null : Integer.parseInt(String.valueOf(params.get("rank")));

		UserTaskLog userTaskLog = userTaskLogService.findByCodeAndUser(userTask.getCode(), userId);
		// 只可以领取一次
		if (userTaskLog != null
				&& (userTaskLog.getStatus() == UserTaskLogStatus.COMPLETE || userTaskLog.getStatus() == UserTaskLogStatus.RECEIVE)) {
			return;
		}

		UserTaskRuleCfg cfg = userTask.getUserTaskRuleCfg();
		UserTaskLogUpdateForm form = new UserTaskLogUpdateForm();
		if (commitCount >= COMMIT_COUNT && rank != null && rank <= RANK) {
			if (userTaskLog == null) {
				int coins = cfg.getCoinsValue();
				int growth = cfg.getGrowthValue();
				form.setUserId(userId);
				form.setCoins(coins);
				form.setGrowth(growth);
				form.setUserId(userId);
				form.setTaskCode(userTask.getCode());
				form.setType(userTask.getType());
				JSONObject contentJsonObj = new JSONObject();
				contentJsonObj.put("content", userTask.getType().getName() + ":" + userTask.getName());
				contentJsonObj.put("items", Collections.EMPTY_LIST);
				contentJsonObj.put("doCount", 1);
				contentJsonObj.put("allCount", LATELY_SIZE);
				form.setContent(contentJsonObj.toJSONString());
				form.setStatus(UserTaskLogStatus.TASKING);
			} else {
				form.setId(userTaskLog.getId());
				String content = userTaskLog.getContent();
				JSONObject contentJsonObj = JSONObject.parseObject(content);
				int doCount = contentJsonObj.getInteger("doCount");
				contentJsonObj.put("doCount", doCount + 1);
				if (doCount + 1 == LATELY_SIZE) {
					contentJsonObj.put("completeAt", System.currentTimeMillis());
					form.setStatus(UserTaskLogStatus.COMPLETE);
					form.setCompleteAt(new Date());
				} else {
					form.setStatus(UserTaskLogStatus.TASKING);
				}
				form.setContent(contentJsonObj.toJSONString());
			}

		} else {
			// 需要把do_count置为0
			if (userTaskLog != null) {
				form.setId(userTaskLog.getId());
				String content = userTaskLog.getContent();
				JSONObject contentJsonObj = JSONObject.parseObject(content);
				contentJsonObj.put("doCount", 0);
				form.setContent(contentJsonObj.toJSONString());
			}
		}
		userTaskLogService.update(form);

	}
}

package com.lanking.uxb.service.honor.api.impl.processor.userTask;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractUserTaskProcessor;

/**
 * 成就任务：练习1000题
 * 
 * @author xinyu.zhou
 * @since 4.1.0
 */
@Component
public class ExerciseQuestion1000TaskProcessor extends AbstractUserTaskProcessor {
	private static final int REWARD_QUESTION_COUNT_1000 = 1000;
	private static final int REWARD_QUESTION_COUNT_10000 = 10000;
	private static final int REWARD_QUESTION_COUNT_20000 = 20000;

	@Autowired
	private ExerciseQuestionProcessorUtil exerciseQuestionProcessorUtil;

	@Override
	public boolean accept(int code) {
		return getCode() == code;
	}

	@Override
	public int getCode() {
		return 101020016;
	}

	public void process(UserTask userTask, long userId, Map<String, Object> params) {
		exerciseQuestionProcessorUtil.process(userTask, userId, params, REWARD_QUESTION_COUNT_1000);

		// 同时判断 10000、20000任务
		exerciseQuestionProcessorUtil.process(super.getUserTask(101020017), userId, params,
				REWARD_QUESTION_COUNT_10000);
		exerciseQuestionProcessorUtil.process(super.getUserTask(101020018), userId, params,
				REWARD_QUESTION_COUNT_20000);
	}
}

package com.lanking.uxb.service.honor.api.impl.processor.userTask;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractUserTaskProcessor;

/**
 * 成就任务：练习10000题 （外部仅调用1000任务，暂设置失效）
 * 
 * @see ExerciseQuestion1000TaskProcessor
 * 
 * @author xinyu.zhou
 * @since 4.1.0
 */
@Deprecated
public class ExerciseQuestion10000TaskProcessor extends AbstractUserTaskProcessor {
	private static final int REWARD_QUESTION_COUNT = 10000;

	@Autowired
	private ExerciseQuestionProcessorUtil exerciseQuestionProcessorUtil;

	@Override
	public boolean accept(int code) {
		return getCode() == code;
	}

	@Override
	public int getCode() {
		return 101020017;
	}

	public void process(UserTask userTask, long userId, Map<String, Object> params) {
		exerciseQuestionProcessorUtil.process(userTask, userId, params, REWARD_QUESTION_COUNT);
	}
}

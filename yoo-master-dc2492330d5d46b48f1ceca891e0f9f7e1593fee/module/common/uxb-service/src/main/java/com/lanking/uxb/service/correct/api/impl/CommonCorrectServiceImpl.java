package com.lanking.uxb.service.correct.api.impl;

import java.util.Collection;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.yoomath.homework.QuestionCorrectLog;
import com.lanking.uxb.service.correct.api.CommonCorrectService;
import com.lanking.uxb.service.correct.api.CorrectLogService;
import com.lanking.uxb.service.correct.api.CorrectStudentHomeworkQuestionService;

/**
 * 批改流程通用接口实现.
 * 
 * @author wanlong.che
 * @since 小优快批
 */
@Service
public class CommonCorrectServiceImpl implements CommonCorrectService {

	@Autowired
	private CorrectStudentHomeworkQuestionService correctStudentHomeworkQuestionService;

	@Autowired
	private CorrectLogService correctLogService;

	@Autowired
	@Qualifier("executor")
	private Executor executor;

	@Override
	public void asyncCreateStudentHomeworkCorrectQuestion(long studentHomeworkQuestionId) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				correctStudentHomeworkQuestionService.createStudentHomeworkCorrectQuestion(studentHomeworkQuestionId);
			}
		});

	}

	@Override
	public void asyncCreateStudentHomeworkCorrectQuestion(Collection<Long> studentHomeworkQuestionIds) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				correctStudentHomeworkQuestionService.createStudentHomeworkCorrectQuestion(studentHomeworkQuestionIds);
			}
		});
	}

	@Override
	public void asyncCreateCorrectLog(QuestionCorrectLog log) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				correctLogService.create(log);
			}
		});

	}

	@Override
	public void asyncCreateCorrectLog(Collection<QuestionCorrectLog> logs) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				correctLogService.create(logs);
			}
		});
	}

}

/**
 * 
 */
package com.lanking.uxb.zycon.task.resource;

import java.util.List;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskStatus;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.zycon.task.api.ZycUserTaskLogService;
import com.lanking.uxb.zycon.task.api.ZycUserTaskService;
import com.lanking.uxb.zycon.task.api.impl.init.ZycUserTaskBindMobileInitService;
import com.lanking.uxb.zycon.task.api.impl.init.ZycUserTaskBindQQInitService;
import com.lanking.uxb.zycon.task.api.impl.init.ZycUserTaskBindWxInitService;
import com.lanking.uxb.zycon.task.api.impl.init.ZycUserTaskCompleteInfoInitService;
import com.lanking.uxb.zycon.task.api.impl.init.ZycUserTaskSetGoalInitService;
import com.lanking.uxb.zycon.task.convert.ZycUserTaskConvert;
import com.lanking.uxb.zycon.task.form.ZycUserTaskForm;
import com.lanking.uxb.zycon.task.value.VZycUserTask;

/**
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 */
@RestController
@RequestMapping(value = "zyc/userTask")
public class ZycUserTaskController {
	@Autowired
	private ZycUserTaskService userTaskService;
	@Autowired
	private ZycUserTaskBindQQInitService bindQQInitService;
	@Autowired
	private ZycUserTaskBindWxInitService bindWxInitService;
	@Autowired
	private ZycUserTaskCompleteInfoInitService completeInfoInitService;
	@Autowired
	private ZycUserTaskSetGoalInitService setGoalInitService;
	@Autowired
	private ZycUserTaskBindMobileInitService bindMobileInitService;

	@Autowired
	private ZycUserTaskConvert userTaskConvert;
	@Autowired
	private ZycUserTaskLogService logService;
	@Autowired
	@Qualifier("executor")
	private Executor executor;

	private static final Logger logger = LoggerFactory.getLogger(ZycUserTaskController.class);

	@RequestMapping(value = "list", method = { RequestMethod.GET, RequestMethod.POST })
	public Value query(@RequestParam(value = "type") int type) {
		List<UserTask> userTasks = userTaskService.list(type);
		List<VZycUserTask> VUserTasks = userTaskConvert.to(userTasks);
		return new Value(VUserTasks);
	}

	/* 不需要添加 */
	@RequestMapping(value = "create", method = { RequestMethod.GET, RequestMethod.POST })
	public Value create(ZycUserTaskForm form) {
		if (form.getCode() == null || form.getCode() == null) {
			return new Value(new MissingArgumentException());
		}
		userTaskService.create(form);
		return new Value();
	}

	@RequestMapping(value = "update", method = { RequestMethod.GET, RequestMethod.POST })
	public Value update(ZycUserTaskForm form) {
		if (form.getCode() == null || form.getCode() == null) {
			return new Value(new MissingArgumentException());
		}
		userTaskService.update(form);
		// 非新手任务
		if (form.getType() > 0 && form.getUserScope() != null) {
			logService.disabled(form.getCode());
		}
		return new Value();
	}

	@RequestMapping(value = "move", method = { RequestMethod.GET, RequestMethod.POST })
	public Value domove(@RequestParam(value = "code1") int code1, @RequestParam(value = "code2") int code2) {
		userTaskService.doMove(code1, code2);
		return new Value();
	}

	@RequestMapping(value = "open", method = { RequestMethod.GET, RequestMethod.POST })
	public Value open(final int code) {
		if (code <= 0) {
			return new Value(new IllegalArgException());
		}

		try {
			userTaskService.open(code);
		} catch (IllegalArgException e) {
			return new Value(e);
		}

		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					if (code <= 101000006) {
						UserTask userTask = userTaskService.get(code);
						if (userTask.getStatus() != UserTaskStatus.PROCESS_DATA) {
							switch (code) {
							case 101000003:
								bindQQInitService.init();
								break;
							case 101000004:
								bindWxInitService.init();
								break;
							case 101000001:
								completeInfoInitService.init();
								break;
							case 101000002:
								setGoalInitService.init();
								break;
							case 101000006:
								bindMobileInitService.init();
								break;
							}
						}
					}
				} catch (Exception e) {
					logger.error("open user task {} has error: ", code, e);
				}
			}
		});

		return new Value();
	}

	@RequestMapping(value = "close", method = { RequestMethod.GET, RequestMethod.POST })
	public Value disabled(final int code) {
		if (code <= 0) {
			return new Value(new IllegalArgException());
		}

		try {

			userTaskService.close(code);
		} catch (IllegalArgException e) {
			return new Value(e);
		}

		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {

					if (code <= 101000006) {
						UserTask userTask = userTaskService.get(code);
						if (userTask.getStatus() != UserTaskStatus.PROCESS_DATA) {
							switch (code) {
							case 101000003:
								bindQQInitService.disabled();
								break;
							case 101000004:
								bindWxInitService.disabled();
								break;
							case 101000001:
								completeInfoInitService.disabled();
								break;
							case 101000002:
								setGoalInitService.disabled();
								break;
							case 101000006:
								bindMobileInitService.disabled();
								break;
							}
						}
					}

				} catch (Exception e) {
					logger.error("close user task {}, has error: ", code, e);
				}
			}
		});
		return new Value();
	}
}

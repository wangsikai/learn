/**
 * 
 */
package com.lanking.uxb.zycon.task.api.impl;

import java.util.List;

import com.lanking.uxb.zycon.task.api.ZycUserTaskLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskStatus;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskType;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.zycon.task.api.ZycUserTaskService;
import com.lanking.uxb.zycon.task.form.ZycUserTaskForm;

/**
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 */
@Transactional(readOnly = true)
@Service
public class ZycUserTaskServiceImpl implements ZycUserTaskService {

	@Autowired
	@Qualifier("UserTaskRepo")
	private Repo<UserTask, Integer> repo;
	@Autowired
	private ZycUserTaskLogService logService;

	@Override
	public UserTask get(int code) {
		return repo.get(code);
	}

	@Override
	public List<UserTask> list(Integer type) {
		Params params = Params.param();
		if (type != null) {
			params.put("type", type);
		}
		return repo.find("$zycUserTaskList", params).list();
	}

	@Transactional
	@Override
	public void update(ZycUserTaskForm form) {
		UserTask userTask = repo.get(form.getCode());
		if (StringUtils.isNotBlank(form.getName())) {
			userTask.setName(form.getName());
		}
		if (form.getIcon() != null) {
			userTask.setIcon(form.getIcon());
		}
		if (form.getUserScope() != null) {
			userTask.setUserScope(form.getUserScope());
		}
		if (form.getUserTaskRuleCfg() != null) {
			userTask.setUserTaskRuleCfg(form.getUserTaskRuleCfg());
		}
		if (form.getNote() != null) {
			userTask.setNote(form.getNote());
		}
		if (form.getCoinsNote() != null) {
			userTask.setCoinsNote(form.getCoinsNote());
		}
		if (form.getGrowthNote() != null) {
			userTask.setGrowthNote(form.getGrowthNote());
		}
		if (form.getUserTaskStatus() != null) {
			userTask.setStatus(form.getUserTaskStatus());
		}
		repo.save(userTask);
	}

	@Transactional
	@Override
	public void doMove(int code1, int code2) {
		UserTask userTask1 = repo.get(code1);
		UserTask userTask2 = repo.get(code2);
		int temp = userTask1.getSequence();
		userTask1.setSequence(userTask2.getSequence());
		userTask2.setSequence(temp);
		repo.save(userTask1);
		repo.save(userTask2);
	}

	@Transactional
	@Override
	public void create(ZycUserTaskForm form) {
		UserTask userTask = new UserTask();
		userTask.setCode(form.getCode());
		userTask.setName(form.getName());
		userTask.setType(UserTaskType.findByValue(form.getType()));
		userTask.setUserScope(form.getUserScope());
		userTask.setUserTaskRuleCfg(form.getUserTaskRuleCfg());
		userTask.setSequence(form.getSequence());
		repo.save(userTask);
	}

	@Override
	@Transactional
	public void open(int code) {
		UserTask userTask = repo.get(code);
		if (null == userTask || userTask.getStatus() == UserTaskStatus.PROCESS_DATA
		        || userTask.getStatus() == UserTaskStatus.DELETED
		        || userTask.getStatus() == UserTaskStatus.OPEN) {
			throw new IllegalArgException();
		}
		if (userTask.getType() != UserTaskType.NEW_USER) {
			userTask.setStatus(UserTaskStatus.OPEN);
			repo.save(userTask);
		}
	}

	@Override
	@Transactional
	public void close(int code) {
		UserTask userTask = repo.get(code);
		if (null == userTask || userTask.getStatus() == UserTaskStatus.PROCESS_DATA
		        || userTask.getStatus() == UserTaskStatus.DELETED
		        || userTask.getStatus() == UserTaskStatus.DISABLED) {
			throw new IllegalArgException();
		}
		if (userTask.getType() != UserTaskType.NEW_USER) {
			userTask.setStatus(UserTaskStatus.DISABLED);
			logService.disabled(code);
			repo.save(userTask);
		}
	}

	@Override
	@Transactional
	public void completeInitData(int code) {
		UserTask userTask = repo.get(code);
		if (userTask != null && userTask.getStatus() == UserTaskStatus.PROCESS_DATA) {
			userTask.setStatus(UserTaskStatus.OPEN);
			repo.save(userTask);
		}
	}
}

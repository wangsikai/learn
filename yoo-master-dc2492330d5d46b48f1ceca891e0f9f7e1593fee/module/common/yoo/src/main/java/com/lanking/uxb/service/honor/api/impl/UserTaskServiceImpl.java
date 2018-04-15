package com.lanking.uxb.service.honor.api.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.honor.api.UserTaskService;
import com.lanking.uxb.service.honor.form.UserTaskQueryForm;

/**
 * @author xinyu.zhou
 * @since 4.0.0
 */
@Service
@Transactional(readOnly = true)
public class UserTaskServiceImpl implements UserTaskService {
	@Autowired
	@Qualifier(value = "UserTaskRepo")
	private Repo<UserTask, Integer> repo;

	@Override
	public UserTask get(int code) {
		return repo.get(code);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<Integer, UserTask> mget(Collection<Integer> codes) {
		if (CollectionUtils.isEmpty(codes)) {
			return Collections.EMPTY_MAP;
		}
		return repo.mget(codes);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<UserTask> mgetList(Collection<Integer> codes) {
		if (CollectionUtils.isEmpty(codes)) {
			return Collections.EMPTY_LIST;
		}
		return repo.mgetList(codes);
	}

	@Override
	public List<UserTask> find(UserTaskQueryForm form) {
		Params params = Params.param();
		if (form.getStatus() != null) {
			params.put("status", form.getStatus().getValue());
		}
		if (form.getType() != null) {
			params.put("type", form.getType().getValue());
		}
		if (form.getUserType() != null) {
			params.put("userType", form.getUserType().getValue());
		}
		if (form.getScope() != null) {
			params.put("scope", form.getScope().getValue());
		}
		return repo.find("$query", params).list();
	}

	@Override
	public List<UserTask> findNotFindFinishUserTask(UserTaskQueryForm form, long userId) {
		Params params = Params.param();
		if (form.getUserType() != null) {
			params.put("userType", form.getUserType().getValue());
		}
		if (form.getScope() != null) {
			params.put("scope", form.getScope().getValue());
		}
		if (form.getStatus() != null) {
			params.put("status", form.getStatus().getValue());
		}
		params.put("userId", userId);
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		try {
			params.put("date", format.parse(format.format(new Date())));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return repo.find("$queryUserNotFinishTask", params).list();
	}
}

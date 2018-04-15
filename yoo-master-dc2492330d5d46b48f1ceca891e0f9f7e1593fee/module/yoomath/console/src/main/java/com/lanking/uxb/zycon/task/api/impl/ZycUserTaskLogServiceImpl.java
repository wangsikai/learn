package com.lanking.uxb.zycon.task.api.impl;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskType;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.zycon.task.api.ZycUserTaskLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xinyu.zhou
 * @since 4.1.0
 */
@Service
@Transactional(readOnly = true)
public class ZycUserTaskLogServiceImpl implements ZycUserTaskLogService {
	@Autowired
	@Qualifier(value = "UserTaskLogRepo")
	private Repo<UserTaskLog, Long> repo;

	@Override
	@Transactional
	public void disabled(int taskCode) {
		repo.execute("$zycDisabled", Params.param("taskCode", taskCode));
	}

	@Override
	@Transactional
	public void disabled(Collection<Long> ids) {
		repo.execute("$zycDisabledByIds", Params.param("ids", ids));
	}

	@Override
	public CursorPage<Long, UserTaskLog> fetch(CursorPageable<Long> cursorPageable, int taskCode) {
		return repo.find("$zycQuery", Params.param("taskCode", taskCode)).fetch(cursorPageable);
	}

	@Override
	@Transactional
	public void enabled(UserTaskType type) {
		repo.execute("$zycEnableByType", Params.param("type", type.getValue()));
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<UserTaskLog> mgetListByCodeAndUsers(Collection<Long> userIds, int code) {
		if (CollectionUtils.isEmpty(userIds)) {
			return Collections.EMPTY_LIST;
		}

		List<UserTaskLog> logs = repo
		        .find("$zycFindByCodeAndUsers", Params.param("userIds", userIds).put("code", code))
		        .list();
		return logs;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<Long, UserTaskLog> mgetByCodeAndUsers(Collection<Long> userIds, int code) {
		if (CollectionUtils.isEmpty(userIds)) {
			return Collections.EMPTY_MAP;
		}

		List<UserTaskLog> logs = this.mgetListByCodeAndUsers(userIds, code);
		if (CollectionUtils.isEmpty(logs)) {
			return Collections.EMPTY_MAP;
		}

		Map<Long, UserTaskLog> logMap = new HashMap<Long, UserTaskLog>(logs.size());
		for (UserTaskLog l : logs) {
			logMap.put(l.getUserId(), l);
		}
		return logMap;
	}

	@Override
	public UserTaskLog findByUserTaskAndUser(int taskCode, long userId) {
		Params params = Params.param("taskCode", taskCode);
		params.put("userId", userId);
		return repo.find("$findByUserTaskAndUser", params).get();
	}
}

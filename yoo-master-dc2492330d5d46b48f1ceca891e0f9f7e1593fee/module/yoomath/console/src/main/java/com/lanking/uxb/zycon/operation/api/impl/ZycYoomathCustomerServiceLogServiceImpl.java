package com.lanking.uxb.zycon.operation.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.customerService.CustomerLogReadStatus;
import com.lanking.cloud.domain.yoo.customerService.YoomathCustomerServiceLog;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.operation.api.ZycYoomathCustomerServiceLogService;

/**
 * @author xinyu.zhou
 * @since yoomath V1.7
 */
@Service
@Transactional(readOnly = true)
public class ZycYoomathCustomerServiceLogServiceImpl implements ZycYoomathCustomerServiceLogService {

	@Autowired
	@Qualifier("YoomathCustomerServiceLogRepo")
	private Repo<YoomathCustomerServiceLog, Long> repo;

	@Override
	@Transactional
	public CursorPage<Long, YoomathCustomerServiceLog> pull(CursorPageable<Long> cursorPageable, long userId,
			CustomerLogReadStatus status, long csId) {
		Params params = Params.param("userId", userId);
		params.put("csId", csId);
		if (status != null) {
			params.put("status", status.getValue());
		}
		CursorPage<Long, YoomathCustomerServiceLog> page = repo.find("$zycPull", params).fetch(cursorPageable);
		List<YoomathCustomerServiceLog> items = page.getItems();
		if (items.size() > 0) {
			List<Long> ids = new ArrayList<Long>(items.size());
			for (YoomathCustomerServiceLog log : items) {
				if (log.getCustomerReadStatus() == CustomerLogReadStatus.UNREAD) {
					ids.add(log.getId());
				}
			}

			// 更新拉取出来的对话信息为已读状态
			repo.execute("$zycUpdateReadStatus", Params.param("ids", ids));
		}

		return page;
	}

	@Override
	public Long getUnreadCount() {
		return repo.find("$zycFindUnreadCount", Params.param()).count();
	}

	@Override
	public Map<Long, Long> mgetUserUnreadCount(Collection<Long> userIds) {
		List<Map> list = repo.find("$zycGetUsersunreadCount", Params.param("userIds", userIds)).list(Map.class);
		Map<Long, Long> map = Maps.newHashMap();
		for (Map m : list) {
			map.put(Long.valueOf(m.get("userid").toString()), Long.valueOf(m.get("unreadcount").toString()));
		}
		return map;
	}

	@Override
	public Long getUserUnreadCount(long userId) {
		return repo.find("$zycGetUserunreadCount", Params.param("userId", userId)).count();
	}
}

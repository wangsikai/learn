package com.lanking.uxb.service.zuoye.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.customerService.CustomerLogReadStatus;
import com.lanking.cloud.domain.yoo.customerService.YoomathCustomerServiceLog;
import com.lanking.cloud.domain.yoo.customerService.YoomathCustomerServiceSession;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.zuoye.api.ZyYoomathCustomerService;

/**
 * @see ZyYoomathCustomerService
 * @author xinyu.zhou
 * @since yoomath V1.7
 */
@Service
@Transactional(readOnly = true)
public class ZyYoomathCustomerServiceImpl implements ZyYoomathCustomerService {
	@Autowired
	@Qualifier("YoomathCustomerServiceLogRepo")
	private Repo<YoomathCustomerServiceLog, Long> logRepo;
	@Autowired
	@Qualifier("YoomathCustomerServiceSessionRepo")
	private Repo<YoomathCustomerServiceSession, Long> sessionRepo;

	@Override
	public YoomathCustomerServiceLog pullNewestOne(long userId) {
		return logRepo.find("$zyPullNewestOne", Params.param("userId", userId)).get();
	}

	@Override
	@Transactional
	public CursorPage<Long, YoomathCustomerServiceLog> pull(CursorPageable<Long> cursorPageable, long userId,
			CustomerLogReadStatus status) {
		Params params = Params.param("userId", userId);
		if (status != null) {
			params.put("status", status.getValue());
		}
		CursorPage<Long, YoomathCustomerServiceLog> page = logRepo.find("$zyPull", params).fetch(cursorPageable);
		List<YoomathCustomerServiceLog> items = page.getItems();
		if (items.size() > 0) {
			List<Long> ids = new ArrayList<Long>(items.size());
			for (YoomathCustomerServiceLog log : items) {
				if (log.getUserReadStatus() == CustomerLogReadStatus.UNREAD) {
					ids.add(log.getId());
				}
			}

			// 更新拉取出来的对话信息为已读状态
			if (ids.size() > 0) {
				logRepo.execute("$zyUpdateReadStatus", Params.param("ids", ids));
			}
		}

		return page;
	}

	@Transactional
	@Override
	public void send(long userId, long customerId, String content) {
		// 保存log
		YoomathCustomerServiceLog log = new YoomathCustomerServiceLog();
		log.setContent(content);
		log.setCreateAt(new Date());
		log.setCustomerReadStatus(CustomerLogReadStatus.UNREAD);
		log.setCustomerServiceId(customerId);
		log.setFromUser(true);
		log.setStatus(Status.ENABLED);
		log.setUserId(userId);
		log.setUserReadStatus(CustomerLogReadStatus.READ);
		logRepo.save(log);
		// 更新session
		YoomathCustomerServiceSession session = sessionRepo
				.find("$zyFindSession", Params.param("userId", userId).put("customerId", customerId)).get();
		if (session == null) {
			session = new YoomathCustomerServiceSession();
			session.setCreateAt(new Date());
			session.setUpdateAt(session.getCreateAt());
		} else {
			session.setUpdateAt(new Date());
		}
		session.setContent(log.getContent());
		session.setCustomerServiceId(log.getCustomerServiceId());
		session.setFromUser(log.isFromUser());
		session.setImgId(log.getImgId());
		session.setStatus(Status.ENABLED);
		session.setUserId(log.getUserId());
		session.setLogId(log.getId());
		sessionRepo.save(session);
	}
}

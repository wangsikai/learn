package com.lanking.uxb.zycon.operation.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.base.session.Session;
import com.lanking.cloud.domain.base.session.SessionHistory;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.session.api.SessionService;
import com.lanking.uxb.service.session.cache.SessionCacheService;
import com.lanking.uxb.zycon.operation.api.ZycAccountService;
import com.lanking.uxb.zycon.operation.api.ZycSessionHistoryService;
import com.lanking.uxb.zycon.operation.api.ZycSessionService;
import com.lanking.uxb.zycon.operation.convert.SessionConvert;
import com.lanking.uxb.zycon.operation.convert.SessionHistoryConvert;
import com.lanking.uxb.zycon.operation.form.UserSearchForm;
import com.lanking.uxb.zycon.operation.value.CSession;

@RestController
@RequestMapping("zyc/online")
public class OnlineUserController {
	@Autowired
	private ZycSessionService zycSessionService;
	@Autowired
	private ZycSessionHistoryService zycSessionHistoryService;
	@Autowired
	private SessionHistoryConvert shConvert;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private SessionConvert sessionConvert;
	@Autowired
	private ZycAccountService accountService;
	@Autowired
	private SessionCacheService sessionCacheService;

	@RequestMapping(value = "list", method = { RequestMethod.POST })
	public Value list(@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, UserSearchForm searchForm) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		Pageable pageable = P.offset((page - 1) * pageSize, pageSize);
		Page<Session> sessionPage = zycSessionService.getSessionByParams(searchForm, pageable);
		List<Session> sessionList = sessionPage.getItems();
		List<CSession> vSessionList = sessionConvert.to(sessionList);
		VPage<CSession> vPage = new VPage<CSession>();
		vPage.setCurrentPage(page);
		vPage.setItems(vSessionList);
		vPage.setPageSize(pageSize);
		vPage.setTotal(sessionPage.getTotalCount());
		vPage.setTotalPage(sessionPage.getPageCount());
		data.put("page", vPage);
		data.put("tokenSize", sessionCacheService.size());
		return new Value(data);
	}

	@RequestMapping(value = "historyList", method = { RequestMethod.POST })
	public Value historyList(@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, UserSearchForm searchForm) {
		Pageable pageable = P.offset((page - 1) * pageSize, pageSize);
		Page<SessionHistory> sessionHistoryPage = zycSessionHistoryService.getHistoryUsers(searchForm, pageable);
		List<SessionHistory> sessionList = sessionHistoryPage.getItems();
		List<CSession> vSessionList = shConvert.to(sessionList);
		VPage<CSession> vPage = new VPage<CSession>();

		vPage.setCurrentPage(page);
		vPage.setItems(vSessionList);
		vPage.setPageSize(pageSize);
		vPage.setTotal(sessionHistoryPage.getTotalCount());
		vPage.setTotalPage(sessionHistoryPage.getPageCount());
		return new Value(vPage);
	}

	@RequestMapping(value = "forbidden", method = { RequestMethod.POST })
	public Value forbidden(@RequestParam(value = "accountId", required = true) Long accountId,
			@RequestParam(value = "token", required = true) String token) {
		accountService.forbiddenAccount(accountId);
		// 强制下线
		sessionService.forceOffline(token);
		return new Value();
	}

	@RequestMapping(value = "offLine", method = { RequestMethod.POST })
	public Value offLine(@RequestParam(value = "token") String token) {
		// 强制下线
		sessionService.forceOffline(token);
		return new Value();
	}
}

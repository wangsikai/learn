package com.lanking.uxb.service.session.api.impl;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.session.Session;
import com.lanking.cloud.domain.base.session.SessionGuestHistory;
import com.lanking.cloud.domain.base.session.SessionHistory;
import com.lanking.cloud.domain.base.session.SessionStatus;
import com.lanking.cloud.domain.base.session.api.Cookies;
import com.lanking.cloud.domain.base.session.api.SessionConstants;
import com.lanking.cloud.sdk.bean.Userable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.web.WebUtils;
import com.lanking.uxb.service.session.api.SessionPacket;
import com.lanking.uxb.service.session.api.SessionService;
import com.lanking.uxb.service.session.api.SessionUserService;
import com.lanking.uxb.service.session.cache.SessionCacheService;
import com.lanking.uxb.service.session.ex.SessionException;

@Service
@Transactional(readOnly = true)
public class SessionServiceImpl implements SessionService, ApplicationContextAware, InitializingBean {

	@Autowired
	@Qualifier("SessionRepo")
	private Repo<Session, Long> sessionRepo;
	@Autowired
	@Qualifier("SessionHistoryRepo")
	private Repo<SessionHistory, Long> sessionHistoryRepo;
	@Autowired
	@Qualifier("SessionGuestHistoryRepo")
	private Repo<SessionGuestHistory, Long> sessionGuestHistoryRepo;
	@Autowired
	private SessionCacheService sessionCacheService;

	private ApplicationContext appContext;

	private List<SessionUserService> sessionUserServices = Lists.newArrayList();

	@Transactional(readOnly = true)
	@Override
	public Session getSession(long id) throws SessionException {
		return sessionRepo.get(id);
	}

	@Transactional(readOnly = true)
	@Override
	public Session getSession(String token) throws SessionException {
		return sessionRepo.find("$getSessionByToken", Params.param("token", token)).get();
	}

	@Transactional(readOnly = true)
	@Override
	public SessionPacket getSessionPacket(String token) throws SessionException {
		SessionPacket packet = sessionCacheService.getSessionPacket(token);
		if (packet != null) {
			return packet;
		}
		Session session = getSession(token);
		if (session != null) {
			packet = createSessionPacket(session);
		}
		if (packet != null) {
			sessionCacheService.setSessionPacket(packet);
		}
		return packet;
	}

	@Transactional(readOnly = true)
	@Override
	public SessionPacket createSessionPacket(Session session) throws SessionException {
		SessionPacket packet = new SessionPacket();
		packet.setId(session.getId());
		packet.setStatus(session.getStatus());
		packet.setToken(session.getToken());
		packet.setUserId(session.getUserId());
		packet.setAccountId(session.getAccountId());
		packet.setUserType(session.getUserType());
		return packet;
	}

	@Transactional
	@Override
	public Session saveSession(Session session) throws SessionException {
		return sessionRepo.save(session);
	}

	@Transactional
	@Override
	public void updateSession(Session session) throws SessionException {
		if (session.getId() != null && session.getId() != 0) {
			Session s = getSession(session.getId());
			if (session.getStatus() != null) {
				s.setStatus(session.getStatus());
			}
			if (session.getActiveAt() != 0) {
				s.setActiveAt(session.getActiveAt());
			}
			if (session.getEndAt() != 0) {
				s.setEndAt(session.getEndAt());
			}
			s.setStartActiveAt(session.getStartActiveAt());
			saveSession(s);
		}
	}

	@Transactional
	@Override
	public void refreshCurrentSession(SessionPacket csp, boolean refreshDb) {
		final SessionPacket packet = getSessionPacket(Security.getToken());
		boolean uptStatus = false;
		if (csp.getStatus() != null && csp.getStatus() != packet.getStatus()) {
			packet.setStatus(csp.getStatus());
			uptStatus = true;
		}
		boolean uptUserType = false;
		if (csp.getUserType() != null && csp.getUserType() != packet.getUserType()) {
			packet.setUserType(csp.getUserType());
			uptUserType = true;
		}
		boolean uptUser = false;
		if (csp.getUserId() != packet.getUserId() && csp.getUserId() != 0) {
			packet.setUserId(csp.getUserId());
			packet.setAccountId(csp.getAccountId());
			uptUser = true;
		}
		boolean uptAttrSession = false;
		if (csp.getAttrSession() != null) {
			packet.updateAttrSession(csp.getAttrSession());
			uptAttrSession = true;
		}
		boolean uptLoginSource = false;
		if (csp.getLoginSource() != null) {
			packet.setLoginSource(csp.getLoginSource());
			uptLoginSource = true;
		}
		if (uptStatus || uptUser || uptUserType || uptAttrSession || uptLoginSource) {
			sessionCacheService.setSessionPacket(packet);
			SessionContext.setSession(new AbstractSession(packet.getToken()) {
				private com.lanking.cloud.domain.base.session.Session session;
				private Userable user;
				private Set<Long> roles;

				@Override
				public Userable getUser() {
					if (user == null) {
						for (SessionUserService sessionUserService : sessionUserServices) {
							if (sessionUserService.getLoginUserType() == getPacket().getUserType()) {
								user = sessionUserService.getUser(session.getUserId());
								break;
							}
						}
					}
					return user;
				}

				@Override
				public Set<Long> getRoles() {
					if (roles == null) {
						for (SessionUserService sessionUserService : sessionUserServices) {
							if (sessionUserService.getLoginUserType() == getPacket().getUserType()) {
								roles = sessionUserService.getRoles(getPacket().getUserId());
								break;
							}
						}
					}
					return roles;
				}

				@Override
				public Session getInternalSession() {
					if (session == null) {
						session = getSession(getPacket().getToken());
					}
					return session;
				}

				@Override
				protected SessionPacket initPacket() {
					return packet;
				}
			});
			if (refreshDb) {
				long currentTime = System.currentTimeMillis();
				Session dbs = getSession(packet.getToken());
				if (dbs.getUserId() == SessionConstants.GUEST_ID && packet.getUserId() != SessionConstants.GUEST_ID) {
					dbs.setLoginAt(currentTime);
				}
				dbs.setAccountId(packet.getAccountId());
				dbs.setUserId(packet.getUserId());
				dbs.setLoginSource(packet.getLoginSource());
				dbs.setDeviceType(packet.getDeviceType());
				dbs.setUserType(packet.getUserType());
				dbs.setStatus(packet.getStatus());
				if ((dbs.getStatus() == SessionStatus.ACTIVE || dbs.getStatus() == SessionStatus.UNACTIVE)
						&& (packet.getStatus() == SessionStatus.TIMEOUT
								|| packet.getStatus() == SessionStatus.LOGOUT)) {
					dbs.setEndAt(currentTime);
				}
				saveSession(dbs);
			}
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		for (SessionUserService sessionUserService : appContext.getBeansOfType(SessionUserService.class).values()) {
			sessionUserServices.add(sessionUserService);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.appContext = applicationContext;
	}

	@Transactional
	@Override
	public void offline(HttpServletRequest request, HttpServletResponse response) {
		if (Security.isLogin()) {
			String token = Security.getToken();
			// refresh cache session packet
			final SessionPacket sp = sessionCacheService.getSessionPacket(token);
			sp.setStatus(SessionStatus.LOGOUT);
			sessionCacheService.setSessionPacket(sp);

			// update db session
			Session session = getSession(sp.getId());
			session.setEndAt(System.currentTimeMillis());
			session.setStatus(SessionStatus.LOGOUT);
			saveSession(session);

			WebUtils.removeCookie(request, response, Cookies.SECURITY_LOGIN_STATUS);
			WebUtils.removeCookie(request, response, Cookies.SECURITY_TOKEN);
			SessionContext.clearContext();
		}
	}

	@Transactional
	@Override
	public void flush2History(Session session) {
		if (session.getUserId() == SessionConstants.GUEST_ID) {// 游客会话
			SessionGuestHistory sh = new SessionGuestHistory();
			sh.setActiveAt(session.getActiveAt());
			sh.setAgent(session.getAgent());
			sh.setCreateAt(session.getCreateAt());
			sh.setDeviceId(session.getDeviceId());
			sh.setEndAt(session.getEndAt());
			sh.setIp(session.getIp());
			sh.setLoginAt(session.getLoginAt());
			sh.setStatus(session.getStatus());
			sh.setToken(session.getToken());
			sh.setUserId(session.getUserId());
			sh.setAccountId(session.getAccountId());
			sh.setUserType(session.getUserType());
			sh.setLoginSource(session.getLoginSource());
			sh.setDeviceType(session.getDeviceType());
			sh.setOnline(session.getActiveAt() - session.getLoginAt());
			sh.setActiveTime(session.getActiveTime());
			sessionGuestHistoryRepo.save(sh);
		} else {
			SessionHistory sh = new SessionHistory();
			sh.setActiveAt(session.getActiveAt());
			sh.setAgent(session.getAgent());
			sh.setCreateAt(session.getCreateAt());
			sh.setDeviceId(session.getDeviceId());
			sh.setEndAt(session.getEndAt());
			sh.setIp(session.getIp());
			sh.setLoginAt(session.getLoginAt());
			sh.setStatus(session.getStatus());
			sh.setToken(session.getToken());
			sh.setUserId(session.getUserId());
			sh.setAccountId(session.getAccountId());
			sh.setUserType(session.getUserType());
			sh.setLoginSource(session.getLoginSource());
			sh.setDeviceType(session.getDeviceType());
			sh.setOnline(session.getActiveAt() - session.getLoginAt());
			sh.setActiveTime(session.getActiveTime());
			sessionHistoryRepo.save(sh);
		}
		// 删除会话表
		sessionRepo.deleteById(session.getId());
	}

	@Transactional(readOnly = true)
	@Override
	public List<Session> findInvalidWebSession(long timeOutTime, int fetchCount) {
		return sessionRepo
				.find("$findInvalidWebSession", Params.param("timeOutTime", timeOutTime).put("fetchCount", fetchCount))
				.list();
	}

	@Transactional(readOnly = true)
	@Override
	public List<Session> findInvalidMobileSession(long timeOutTime, int fetchCount) {
		return sessionRepo.find("$findInvalidMobileSession",
				Params.param("timeOutTime", timeOutTime).put("fetchCount", fetchCount)).list();
	}

	@Transactional
	@Override
	public void delHistory(String token) {
		sessionHistoryRepo.execute("$delHistory", Params.param("token", token));
		sessionGuestHistoryRepo.execute("$delGuestHistory", Params.param("token", token));
	}

	@Transactional
	@Override
	public void forceOffline(long userId, String excludeToken) {
		List<Session> sessions = sessionRepo.find("$findSessionByUserId", Params.param("userId", userId)).list();
		for (Session session : sessions) {
			if (session.getToken().equals(excludeToken)) {
				continue;
			}
			// refresh cache session packet
			final SessionPacket sp = sessionCacheService.getSessionPacket(session.getToken());
			if (sp != null) {
				sp.setStatus(SessionStatus.LOGOUT);
				sessionCacheService.setSessionPacket(sp);
			}
			// update db session
			session.setEndAt(System.currentTimeMillis());
			session.setStatus(SessionStatus.LOGOUT);
			saveSession(session);
		}
	}

	@Transactional
	@Override
	public void updateDeviceId(long id, long deviceId) {
		Session session = getSession(id);
		session.setDeviceId(deviceId);
		sessionRepo.save(session);
	}

	@Transactional
	@Override
	public void forceOffline(String token) {
		// refresh cache session packet
		final SessionPacket sp = sessionCacheService.getSessionPacket(token);
		if (sp != null) {
			sp.setStatus(SessionStatus.LOGOUT);
			sessionCacheService.setSessionPacket(sp);
		}
		// update db session
		Session session = getSession(sp.getId());
		if (session != null) {
			session.setEndAt(System.currentTimeMillis());
			session.setStatus(SessionStatus.LOGOUT);
			saveSession(session);
		}
	}

}

package com.lanking.uxb.service.session.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lanking.cloud.domain.base.session.Session;
import com.lanking.uxb.service.session.ex.SessionException;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月29日
 *
 */
public interface SessionService {

	Session getSession(long id) throws SessionException;

	Session getSession(String token) throws SessionException;

	SessionPacket getSessionPacket(String token) throws SessionException;

	SessionPacket createSessionPacket(Session session) throws SessionException;

	Session saveSession(Session session) throws SessionException;

	void updateSession(Session session) throws SessionException;

	void refreshCurrentSession(SessionPacket csp, boolean refreshDb);

	void offline(HttpServletRequest request, HttpServletResponse response);

	void flush2History(Session session);

	void delHistory(String token);

	List<Session> findInvalidWebSession(long timeOutTime, int fetchCount);

	List<Session> findInvalidMobileSession(long timeOutTime, int fetchCount);

	void forceOffline(long userId, String excludeToken);

	void updateDeviceId(long id, long deviceId);

	void forceOffline(String token);

}

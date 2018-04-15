package com.lanking.uxb.service.session.api.impl;

import com.lanking.uxb.service.session.api.Session;

final class SessionContext {

	private static ThreadLocal<SessionContext> LOCAL = new InheritableThreadLocal<SessionContext>() {
		@Override
		protected SessionContext initialValue() {
			return new SessionContext();
		}
	};

	private Session session;

	public static SessionContext getContext() {
		return LOCAL.get();
	}

	public static void clearContext() {
		LOCAL.remove();
	}

	public static Session getSession() {
		return getContext().session;
	}

	public static void setSession(Session session) {
		getContext().session = session;
	}

	private SessionContext() {
	}
}

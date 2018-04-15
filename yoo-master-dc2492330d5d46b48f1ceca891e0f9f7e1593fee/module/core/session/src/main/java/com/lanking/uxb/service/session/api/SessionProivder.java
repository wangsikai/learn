package com.lanking.uxb.service.session.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SessionProivder {

	Session getSession(HttpServletRequest request, HttpServletResponse response);
}

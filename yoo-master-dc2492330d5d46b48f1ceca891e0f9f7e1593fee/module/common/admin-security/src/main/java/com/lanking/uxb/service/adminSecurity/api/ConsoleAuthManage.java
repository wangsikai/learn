package com.lanking.uxb.service.adminSecurity.api;

import com.lanking.cloud.domain.support.common.auth.ConsoleUser;
import com.lanking.uxb.service.session.api.SessionUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
public interface ConsoleAuthManage extends SessionUserService {

	void handleLogin(ConsoleUser consoleUser, HttpServletRequest request, HttpServletResponse response);
}

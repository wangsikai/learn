package com.lanking.uxb.service.session.api;

import java.util.Set;

import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Userable;

/**
 * 会话用户接口,业务中需要实现
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年11月24日
 *
 */
public interface SessionUserService {

	UserType getLoginUserType();

	Userable getUser(long id);

	Set<Long> getRoles(long id);

	void handleLogin();

}

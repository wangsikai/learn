package com.lanking.uxb.service.session.api.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;
import com.lanking.cloud.domain.base.session.api.SessionConstants;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.NotImplementedException;
import com.lanking.cloud.sdk.bean.Userable;
import com.lanking.uxb.service.session.api.SessionService;
import com.lanking.uxb.service.session.api.SessionUserService;

/**
 * SessionUserService的默认实现,如果业务模块中有实现则使用业务模块中的
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年11月24日
 *
 */
@Service
public class DefaultSessionUserService implements SessionUserService {

	@Autowired
	private SessionService sessionService;

	@Override
	public Userable getUser(long id) {
		Userable u = new Userable() {

			private long id;

			@Override
			public Long getId() {
				return id;
			}

			@Override
			public void setId(Long id) {
				this.id = id;
			}
		};
		u.setId(SessionConstants.GUEST_ID);
		return u;
	}

	@Override
	public UserType getLoginUserType() {
		return UserType.NULL;
	}

	@Override
	public void handleLogin() {
		throw new NotImplementedException();
	}

	@Override
	public Set<Long> getRoles(long id) {
		return Sets.newHashSet();
	}
}

package com.lanking.uxb.zycon.operation.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.base.session.Session;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.zycon.operation.api.ZycAccountService;
import com.lanking.uxb.zycon.operation.value.CSession;

/**
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version V1.0.0,2015年1月22日 上午10:52:26
 *
 */
@Component
public class SessionConvert extends Converter<CSession, Session, Long> {

	@Autowired
	private ZycAccountService accountService;

	@Override
	protected Long getId(Session s) {
		return s.getId();
	}

	@Override
	protected CSession convert(Session s) {
		CSession session = new CSession();
		session.setId(s.getId());
		session.setActiveAt(s.getActiveAt());
		session.setUserId(s.getUserId());
		session.setAgent(s.getAgent());
		session.setLoginAt(s.getLoginAt());
		session.setIp(s.getIp());
		session.setAccountId(s.getAccountId());
		session.setToken(s.getToken());
		session.setDevice(s.getDeviceType().name());
		return session;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// account Name 账户名
		assemblers.add(new ConverterAssembler<CSession, Session, Long, Account>() {
			@Override
			public boolean accept(Session s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Session s, CSession d) {
				return s.getAccountId();

			}

			@Override
			public void setValue(Session s, CSession d, Account value) {
				d.setAccountName(value.getName());
			}

			@Override
			public Account getValue(Long key) {
				return accountService.getAccount(key);

			}

			@Override
			public Map<Long, Account> mgetValue(Collection<Long> keys) {
				return accountService.mgeAccount(keys);
			}

		});

	}
}

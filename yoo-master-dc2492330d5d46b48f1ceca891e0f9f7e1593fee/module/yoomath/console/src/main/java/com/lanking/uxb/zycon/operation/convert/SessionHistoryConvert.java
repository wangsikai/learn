package com.lanking.uxb.zycon.operation.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.base.session.SessionHistory;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.zycon.operation.api.ZycAccountService;
import com.lanking.uxb.zycon.operation.value.CSession;

/**
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年10月14日 上午11:43:11
 */
@Component
public class SessionHistoryConvert extends Converter<CSession, SessionHistory, Long> {

	@Autowired
	private ZycAccountService accountService;

	@Override
	protected Long getId(SessionHistory s) {
		return s.getId();
	}

	@Override
	protected CSession convert(SessionHistory s) {
		CSession session = new CSession();
		session.setId(s.getId());
		session.setActiveAt(s.getActiveAt());
		session.setUserId(s.getUserId());
		session.setAgent(s.getAgent());
		session.setLoginAt(s.getLoginAt());
		session.setIp(s.getIp());
		session.setAccountId(s.getAccountId());
		session.setToken(s.getToken());
		return session;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// account Name 账户名
		assemblers.add(new ConverterAssembler<CSession, SessionHistory, Long, Account>() {
			@Override
			public boolean accept(SessionHistory s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(SessionHistory s, CSession d) {
				return s.getAccountId();

			}

			@Override
			public void setValue(SessionHistory s, CSession d, Account value) {
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

package com.lanking.uxb.service.user.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.PasswordQuestion;
import com.lanking.cloud.domain.yoo.account.AccountPasswordQuestion;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.code.api.PasswordQuestionService;
import com.lanking.uxb.service.code.convert.PasswordQuestionConvert;
import com.lanking.uxb.service.user.value.VAccountPasswordQuestion;

@Component
public class AccountPasswordQuestionConvert extends Converter<VAccountPasswordQuestion, AccountPasswordQuestion, Long> {

	@Autowired
	private PasswordQuestionService pqService;
	@Autowired
	private PasswordQuestionConvert pqConvert;

	public VAccountPasswordQuestion to(AccountPasswordQuestion s, boolean initAnswer) {
		s.setInitAnswer(initAnswer);
		return super.to(s);
	}

	public List<VAccountPasswordQuestion> to(List<AccountPasswordQuestion> ss, boolean initAnswer) {
		for (AccountPasswordQuestion s : ss) {
			s.setInitAnswer(initAnswer);
		}
		return super.to(ss);
	}

	public Map<Long, VAccountPasswordQuestion> to(Map<Long, AccountPasswordQuestion> sMap, boolean initAnswer) {
		for (AccountPasswordQuestion s : sMap.values()) {
			s.setInitAnswer(initAnswer);
		}
		return super.to(sMap);
	}

	public Map<Long, VAccountPasswordQuestion> toMap(List<AccountPasswordQuestion> ss, boolean initAnswer) {
		for (AccountPasswordQuestion s : ss) {
			s.setInitAnswer(initAnswer);
		}
		return super.toMap(ss);
	}

	@Override
	protected Long getId(AccountPasswordQuestion s) {
		return s.getId();
	}

	@Override
	protected VAccountPasswordQuestion convert(AccountPasswordQuestion s) {
		VAccountPasswordQuestion v = new VAccountPasswordQuestion();
		v.setId(s.getId());
		v.setAccountId(s.getAccountId());
		v.setPasswordQuestionCode(s.getPasswordQuestionCode());
		if (s.isInitAnswer()) {
			v.setAnswer(s.getAnswer());
		} else {
			v.setAnswer(StringUtils.EMPTY);
		}
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers
				.add(new ConverterAssembler<VAccountPasswordQuestion, AccountPasswordQuestion, Integer, PasswordQuestion>() {

					@Override
					public boolean accept(AccountPasswordQuestion s) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Integer getKey(AccountPasswordQuestion s, VAccountPasswordQuestion d) {
						return s.getPasswordQuestionCode();
					}

					@Override
					public void setValue(AccountPasswordQuestion s, VAccountPasswordQuestion d, PasswordQuestion value) {
						if (value != null) {
							d.setPasswordQuestion(pqConvert.to(value));
						}
					}

					@Override
					public PasswordQuestion getValue(Integer key) {
						return pqService.get(key);
					}

					@Override
					public Map<Integer, PasswordQuestion> mgetValue(Collection<Integer> keys) {
						return pqService.mget(keys);
					}

				});
	}
}

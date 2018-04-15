package com.lanking.uxb.zycon.operation.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.support.console.common.CorrectUser;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.zycon.operation.value.CCorrectUser;

/**
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version V1.0.0,2015年1月22日 上午10:52:26
 *
 */
@Component
public class CorrectUserConvert extends Converter<CCorrectUser, CorrectUser, Long> {

	@Override
	protected Long getId(CorrectUser s) {
		return s.getId();
	}

	@Override
	protected CCorrectUser convert(CorrectUser s) {
		CCorrectUser cu = new CCorrectUser();
		cu.setId(s.getId());
		cu.setName(s.getName());
		cu.setTel(s.getMobile());
		cu.setStatus(s.getStatus());
		return cu;
	}

}

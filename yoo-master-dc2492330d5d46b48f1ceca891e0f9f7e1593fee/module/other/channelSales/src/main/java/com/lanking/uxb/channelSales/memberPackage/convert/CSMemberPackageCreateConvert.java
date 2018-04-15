package com.lanking.uxb.channelSales.memberPackage.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.support.common.auth.ConsoleUser;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.channelSales.memberPackage.value.VMemberPackageCreateUser;

@Component
public class CSMemberPackageCreateConvert extends Converter<VMemberPackageCreateUser, ConsoleUser, Long> {

	@Override
	protected VMemberPackageCreateUser convert(ConsoleUser c) {
		VMemberPackageCreateUser vo = new VMemberPackageCreateUser();
		vo.setCreateName(c.getName());
		vo.setCreateId(c.getId());
		return vo;
	}

	@Override
	protected Long getId(ConsoleUser c) {
		return c.getId();
	}

}

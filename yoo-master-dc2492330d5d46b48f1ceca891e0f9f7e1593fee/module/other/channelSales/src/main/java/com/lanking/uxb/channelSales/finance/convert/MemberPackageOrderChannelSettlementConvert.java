package com.lanking.uxb.channelSales.finance.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderChannelSettlement;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.channelSales.finance.value.VMemberPackageOrderChannelSettlement;

@Component
public class MemberPackageOrderChannelSettlementConvert extends
		Converter<VMemberPackageOrderChannelSettlement, MemberPackageOrderChannelSettlement, Long> {

	@Override
	protected Long getId(MemberPackageOrderChannelSettlement arg0) {
		return arg0.getId();
	}

	@Override
	protected VMemberPackageOrderChannelSettlement convert(MemberPackageOrderChannelSettlement s) {
		VMemberPackageOrderChannelSettlement v = new VMemberPackageOrderChannelSettlement();
		v.setAdminTransactionAmount(s.getAdminTransactionAmount());
		v.setChannelCode(s.getChannelCode());
		v.setChannelProfits(s.getChannelProfits());
		v.setChannelTransactionAmount(s.getChannelTransactionAmount());
		v.setId(s.getId());
		v.setOnlineTransactionAmount(s.getOnlineTransactionAmount());
		v.setProfits(s.getProfits());
		v.setSettlementMonth(s.getSettlementMonth());
		v.setSettlementYear(s.getSettlementYear());
		v.setStatus(s.getStatus());
		v.setTransactionAmount(s.getTransactionAmount());
		v.setProfitsGap(s.getProfitsGap());
		v.setSettlementProfits(s.getSettlementProfits());
		return v;
	}

}

package com.lanking.uxb.zycon.operation.convert;

import com.lanking.cloud.domain.yoo.customerService.YoomathCustomerServiceLog;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.zycon.operation.value.VZycYoomathCustomerServiceLog;

import org.springframework.stereotype.Component;

/**
 * YoomathCustomerServiceLog -> VZycYoomathCustomerServiceLog
 *
 * @author xinyu.zhou
 * @since yoomath V1.7
 */
@Component
public class ZycYoomathCustomerServiceLogConvert extends
		Converter<VZycYoomathCustomerServiceLog, YoomathCustomerServiceLog, Long> {

	@Override
	protected Long getId(YoomathCustomerServiceLog yoomathCustomerServiceLog) {
		return yoomathCustomerServiceLog.getId();
	}

	@Override
	protected VZycYoomathCustomerServiceLog convert(YoomathCustomerServiceLog yoomathCustomerServiceLog) {
		VZycYoomathCustomerServiceLog v = new VZycYoomathCustomerServiceLog();
		v.setId(yoomathCustomerServiceLog.getId());
		v.setCustomerServiceId(yoomathCustomerServiceLog.getCustomerServiceId());
		v.setImage(FileUtil.getUrl(yoomathCustomerServiceLog.getImgId()));
		v.setUserId(yoomathCustomerServiceLog.getUserId());
		v.setFromUser(yoomathCustomerServiceLog.isFromUser());
		v.setContent(yoomathCustomerServiceLog.getContent());
		return v;
	}
}

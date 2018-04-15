package com.lanking.uxb.service.zuoye.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.customerService.YoomathCustomerServiceLog;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.service.zuoye.value.VYoomathCustomerServiceLog;

/**
 * YoomathCustomerServiceLog -> VYoomathCustomerServiceLog
 *
 * @author xinyu.zhou
 * @since yoomath V1.7
 */
@Component
public class ZyYoomathCustomerServiceLogConvert extends
		Converter<VYoomathCustomerServiceLog, YoomathCustomerServiceLog, Long> {

	@Override
	protected Long getId(YoomathCustomerServiceLog yoomathCustomerServiceLog) {
		return yoomathCustomerServiceLog.getId();
	}

	@Override
	protected VYoomathCustomerServiceLog convert(YoomathCustomerServiceLog yoomathCustomerServiceLog) {
		VYoomathCustomerServiceLog v = new VYoomathCustomerServiceLog();
		v.setId(yoomathCustomerServiceLog.getId());
		v.setContent(yoomathCustomerServiceLog.getContent());
		v.setUserId(yoomathCustomerServiceLog.getUserId());
		v.setFromUser(yoomathCustomerServiceLog.isFromUser());
		v.setImage(FileUtil.getUrl(yoomathCustomerServiceLog.getImgId()));
		v.setCreateAt(yoomathCustomerServiceLog.getCreateAt());
		return v;
	}
}

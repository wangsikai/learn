package com.lanking.uxb.service.thirdparty.resource;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeUUID;
import com.lanking.cloud.domain.yoo.thirdparty.ShareLog;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.thirdparty.api.ShareLogService;
import com.lanking.uxb.service.thirdparty.convert.ShareLogConvert;
import com.lanking.uxb.service.thirdparty.form.ShareForm;

/**
 * 分享相关restAPI接口
 * 
 * @since yoomath V1.9
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年1月11日
 */
@RestController
@RequestMapping("thirdparty/share")
public class ThirdpartyShareController {

	@Autowired
	private ShareLogService logService;
	@Autowired
	private ShareLogConvert logConvert;

	@RequestMapping(value = "log", method = { RequestMethod.POST, RequestMethod.GET })
	public Value log(ShareForm form) {
		if (form.getType() == null || form.getBiz() == null) {
			return new Value(new IllegalArgException());
		}
		form.setId(SnowflakeUUID.next());
		form.setCreateAt(new Date());
		form.setUserId(Security.getUserId());
		ShareLog log = logService.log(form);
		return new Value(logConvert.to(log));
	}

	@RolesAllowed(anyone = true)
	@RequestMapping(value = "reportInfo", method = { RequestMethod.POST, RequestMethod.GET })
	public Value reportInfo(long id) {
		return new Value(logConvert.to(logService.get(id)));
	}
}

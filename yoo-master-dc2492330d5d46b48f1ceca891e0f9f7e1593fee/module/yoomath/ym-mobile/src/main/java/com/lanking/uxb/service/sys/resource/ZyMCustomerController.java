package com.lanking.uxb.service.sys.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.api.ZyYoomathCustomerService;

/**
 * 客服相关接口
 * 
 * @since yoomath(mobile) V1.1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月22日
 */
@RestController
@RequestMapping(value = "zy/m/customer")
public class ZyMCustomerController {

	@Autowired
	private ZyYoomathCustomerService yoomathCustomerService;

	/**
	 * @since yoomath(mobile) V1.1.0
	 * @param content
	 *            内容
	 * @param contact
	 *            联系方式
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "M")
	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@RequestMapping(value = { "send" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value send(String content, String contact) {
		if (StringUtils.isBlank(content)) {
			return new Value(new IllegalArgException());
		}
		if (StringUtils.isNotBlank(contact)) {
			content += "\n联系方式:" + contact;
		}
		yoomathCustomerService.send(Security.getUserId(), ZyYoomathCustomerService.DEF_CUSTOMER_ID, content);
		return new Value();
	}
}

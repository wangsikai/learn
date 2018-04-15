package com.lanking.uxb.service.base.api;

import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.uxb.service.user.form.RegisterForm;

/**
 * 第三方注册接口
 * 
 * @since yoomath(mobile) V1.1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月24日
 */
public interface ThirdPartyRegisterService {

	Account register(RegisterForm form);

}

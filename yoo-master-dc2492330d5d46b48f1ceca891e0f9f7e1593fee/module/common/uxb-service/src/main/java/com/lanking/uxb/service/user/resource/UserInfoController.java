package com.lanking.uxb.service.user.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.convert.UserProfileConvert;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月9日
 *
 */
@RestController
@RequestMapping("user/info")
public class UserInfoController {

	@Autowired
	@Qualifier("userService")
	private UserService userService;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private UserProfileConvert userProfileConvert;

	@RequestMapping(value = "simple", method = { RequestMethod.POST, RequestMethod.GET })
	public Value simple(@RequestParam(defaultValue = "0", value = "userId") long userId) {
		long uid = userId == 0 ? Security.getUserId() : userId;
		return new Value(userConvert.get(uid));
	}

	@RequestMapping(value = "profile", method = { RequestMethod.POST, RequestMethod.GET })
	public Value profile() {
		return new Value(userProfileConvert.get(Security.getUserId()));
	}
}

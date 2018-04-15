package com.lanking.uxb.service.user.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.member.UserMember;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.UserMemberService;
import com.lanking.uxb.service.user.convert.UserMemberConvert;

/**
 * 用户会员相关.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月28日
 */
@RestController
@RequestMapping("user/member")
public class UserMemberController {
	@Autowired
	private UserMemberService userMemberService;
	@Autowired
	private UserMemberConvert userMemberConvert;

	/**
	 * 获得当前用户会员信息.
	 * 
	 * @return
	 */
	@RequestMapping("get")
	public Value get() {
		UserMember um = userMemberService.$findByUserId(Security.getUserId());
		if (Security.isClient()) {
			return new Value(userMemberConvert.to(um));
		} else {
			if (um.getMemberType() == MemberType.NONE) {
				return new Value();
			} else {
				return new Value(userMemberConvert.to(um));
			}
		}

	}
}

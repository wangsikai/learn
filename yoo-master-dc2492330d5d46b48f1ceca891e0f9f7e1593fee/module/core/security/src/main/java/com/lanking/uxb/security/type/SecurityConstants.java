package com.lanking.uxb.security.type;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.security.ex.SecurityException;

public class SecurityConstants {

	public static final String MSG_NO_PERMISSION_VALUE = JSONObject.toJSON(new Value(new NoPermissionException()))
			.toString();
	public static final String MSG_NEED_LOGIN_VALUE = JSONObject.toJSON(
			new Value(new SecurityException(SecurityException.NEED_LOGIN))).toString();
}

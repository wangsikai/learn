package com.lanking.uxb.service.user.api;

import com.lanking.cloud.domain.yoo.user.UserType;

public interface UserTypeService {

	UserType getType();

	boolean accept(UserType userType);
}

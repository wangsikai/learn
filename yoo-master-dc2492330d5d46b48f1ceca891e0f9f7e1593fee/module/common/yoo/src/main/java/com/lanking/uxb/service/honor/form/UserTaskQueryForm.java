package com.lanking.uxb.service.honor.form;

import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskStatus;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskType;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskUserScope;
import com.lanking.cloud.domain.yoo.user.UserType;

import java.util.List;

/**
 * @author xinyu.zhou
 */
public class UserTaskQueryForm {
	private UserType userType;
	private UserTaskUserScope scope;
	private UserTaskStatus status;
	private UserTaskType type;
	private List<UserTaskType> types;

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public UserTaskUserScope getScope() {
		return scope;
	}

	public void setScope(UserTaskUserScope scope) {
		this.scope = scope;
	}

	public UserTaskStatus getStatus() {
		return status;
	}

	public void setStatus(UserTaskStatus status) {
		this.status = status;
	}

	public UserTaskType getType() {
		return type;
	}

	public void setType(UserTaskType type) {
		this.type = type;
	}

	public List<UserTaskType> getTypes() {
		return types;
	}

	public void setTypes(List<UserTaskType> types) {
		this.types = types;
	}
}

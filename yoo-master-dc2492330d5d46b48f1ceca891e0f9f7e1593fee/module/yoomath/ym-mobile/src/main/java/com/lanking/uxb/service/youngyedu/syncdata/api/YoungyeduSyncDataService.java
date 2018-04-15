package com.lanking.uxb.service.youngyedu.syncdata.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.uxb.service.youngyedu.syncdata.form.YoungyeduClass;
import com.lanking.uxb.service.youngyedu.syncdata.form.YoungyeduUser;

/**
 * @author xinyu.zhou
 * @since 3.0.3
 */
public interface YoungyeduSyncDataService {

	/**
	 * 同步数据
	 *
	 * @param users
	 *            用户列表
	 * @param code
	 *            融捷渠道商code
	 */
	void sync(List<YoungyeduUser> users, int code);

	/**
	 * 创建用户
	 *
	 * @param user
	 *            {@link YoungyeduUser}
	 */
	User createUser(YoungyeduUser user, int code);

	/**
	 * 创建班级
	 *
	 * @param clazz
	 *            {@link YoungyeduClass}
	 * @param user
	 *            {@link User}
	 */
	void createClass(YoungyeduClass clazz, User user);

}

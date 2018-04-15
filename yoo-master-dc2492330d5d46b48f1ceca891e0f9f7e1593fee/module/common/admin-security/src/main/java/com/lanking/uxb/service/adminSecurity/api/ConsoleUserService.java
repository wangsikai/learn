package com.lanking.uxb.service.adminSecurity.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.support.common.auth.ConsoleUser;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.service.adminSecurity.form.ConsoleUserForm;
import com.lanking.uxb.service.adminSecurity.type.ConsoleOperateType;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
public interface ConsoleUserService {

	ConsoleUser save(ConsoleUserForm form);

	ConsoleUser update(ConsoleUserForm form);

	void delete(Long id);

	ConsoleUser get(Long id);

	ConsoleUser getByName(String name, Long systemId);

	/**
	 * 重置密码
	 *
	 * @param id
	 *            用户的id
	 * @param oldPassword
	 *            旧密码
	 * @param newPassword
	 *            新密码
	 * @return
	 */
	ConsoleUser resetPassword(Long id, String oldPassword, String newPassword);

	void updateUserRole(Long userId, Long roleId, ConsoleOperateType type);

	List<ConsoleUser> mgetList(Collection<Long> ids);

	List<ConsoleUser> getUsersByGroup(Long groupId);

	Page<ConsoleUser> query(Long systemId, String name, Pageable pageable);

	/**
	 * 更新用户状态
	 *
	 * @param userId
	 *            用户id
	 * @param status
	 *            {@link Status}
	 */
	void updateUserStatus(long userId, Status status);

	/**
	 * 根据系统查找用户列表
	 *
	 * @param systemId
	 *            系统id
	 * @return {@link List}
	 */
	List<ConsoleUser> findBySystem(long systemId);

	Map<Long, ConsoleUser> mget(Collection<Long> ids);
}

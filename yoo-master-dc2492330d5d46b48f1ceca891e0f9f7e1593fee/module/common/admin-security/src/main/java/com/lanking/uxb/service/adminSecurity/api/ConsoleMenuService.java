package com.lanking.uxb.service.adminSecurity.api;

import com.lanking.cloud.domain.support.common.auth.ConsoleMenuEntity;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.service.adminSecurity.form.ConsoleMenuForm;

import java.util.Collection;
import java.util.List;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
public interface ConsoleMenuService {
	ConsoleMenuEntity save(ConsoleMenuForm form);

	ConsoleMenuEntity update(ConsoleMenuForm form);

	void delete(Long id);

	/**
	 * 按系统来获取菜单数据
	 *
	 * @param systemId
	 *            系统id
	 * @param status
	 *            菜单状态
	 * @return {@link List}
	 */
	List<ConsoleMenuEntity> getBySystem(Long systemId, Status status);

	/**
	 * 根据当前用户得到当前用户的菜单
	 *
	 * @param userId
	 *            用户id
	 * @param systemId
	 *            管控台系统id
	 * @return 用户拥有权限的菜单
	 */
	List<ConsoleMenuEntity> findByUser(Long systemId, Long userId);

	/**
	 * 更新菜单状态
	 *
	 * @param ids
	 *            菜单id列表
	 * @param status
	 *            {@link Status}
	 */
	void updateStatus(Collection<Long> ids, Status status);
}

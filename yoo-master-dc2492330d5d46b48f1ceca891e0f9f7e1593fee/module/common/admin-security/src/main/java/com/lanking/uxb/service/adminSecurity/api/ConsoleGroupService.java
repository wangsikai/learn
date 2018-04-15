package com.lanking.uxb.service.adminSecurity.api;

import com.lanking.cloud.domain.support.common.auth.ConsoleGroup;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.service.adminSecurity.form.ConsoleGroupForm;
import com.lanking.uxb.service.adminSecurity.type.ConsoleOperateType;

import java.util.List;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
public interface ConsoleGroupService {
	ConsoleGroup save(ConsoleGroupForm form);

	void updateGroupUsers(Long groupId, List<Long> userIds);

	ConsoleGroup get(Long id);

	void updateGroupRole(Long groupId, Long roleId, ConsoleOperateType type);

	/**
	 * 分页查询用户组
	 *
	 * @param systemId
	 *            系统id
	 * @param name
	 *            用户组名
	 * @param pageable
	 *            参数
	 * @return {@link Page}
	 */
	Page<ConsoleGroup> query(Long systemId, String name, Pageable pageable);
}

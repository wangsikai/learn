package com.lanking.uxb.service.adminSecurity.api.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.support.common.auth.ConsoleRole;
import com.lanking.cloud.domain.support.common.auth.ConsoleRoleGroup;
import com.lanking.cloud.domain.support.common.auth.ConsoleRoleMenu;
import com.lanking.cloud.domain.support.common.auth.ConsoleUserRole;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.adminSecurity.api.ConsoleRoleService;
import com.lanking.uxb.service.adminSecurity.form.ConsoleRoleForm;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@Service
@Transactional(readOnly = true)
public class ConsoleRoleServiceImpl implements ConsoleRoleService {
	@Autowired
	@Qualifier("ConsoleRoleRepo")
	private Repo<ConsoleRole, Long> repo;
	@Autowired
	@Qualifier("ConsoleRoleMenuRepo")
	private Repo<ConsoleRoleMenu, Long> consoleRoleMenuRepo;
	@Autowired
	@Qualifier("ConsoleUserRoleRepo")
	private Repo<ConsoleUserRole, Long> consoleUserRoleRepo;
	@Autowired
	@Qualifier("ConsoleRoleGroupRepo")
	private Repo<ConsoleRoleGroup, Long> consoleRoleGroupRepo;

	@Override
	@Transactional(readOnly = false)
	public ConsoleRole save(ConsoleRoleForm form) {
		ConsoleRole consoleRole = new ConsoleRole();
		consoleRole.setCreateAt(new Date());
		consoleRole.setName(form.getName());
		consoleRole.setStatus(Status.ENABLED);
		consoleRole.setCode(form.getCode());
		repo.save(consoleRole);

		if (CollectionUtils.isNotEmpty(form.getMenuIds())) {
			for (Long menuId : form.getMenuIds()) {
				ConsoleRoleMenu consoleRoleMenu = new ConsoleRoleMenu();
				consoleRoleMenu.setMenuId(menuId);
				consoleRoleMenu.setRoleId(consoleRole.getId());
				consoleRoleMenuRepo.save(consoleRoleMenu);
			}
		}

		return consoleRole;
	}

	@Override
	@Transactional(readOnly = false)
	public ConsoleRole update(ConsoleRoleForm form) {
		ConsoleRole consoleRole = repo.get(form.getId());
		consoleRole.setName(form.getName());
		consoleRole.setCode(form.getCode());
		consoleRole.setUpdateAt(new Date());
		return repo.save(consoleRole);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(Long id) {
		ConsoleRole consoleRole = repo.get(id);
		consoleRole.setStatus(Status.DELETED);
		repo.save(consoleRole);
	}

	@Override
	public Page<ConsoleRole> query(String name, Pageable pageable) {
		Params params = Params.param();
		if (name != null) {
			params.put("name", "%" + name + "%");
		}
		return repo.find("$query", params).fetch(pageable);
	}

	@Override
	@Transactional
	public void updateUserRole(long userId, long roleId, boolean isRemove) {
		if (isRemove) {
			consoleUserRoleRepo.execute("$deleteUserRole", Params.param("userId", userId).put("roleId", roleId));
		} else {
			ConsoleUserRole has = consoleUserRoleRepo
					.find("$findByUserAndRole", Params.param("userId", userId).put("roleId", roleId)).get();
			if (has == null) {
				ConsoleUserRole consoleUserRole = new ConsoleUserRole();
				consoleUserRole.setRoleId(roleId);
				consoleUserRole.setUserId(userId);

				consoleUserRoleRepo.save(consoleUserRole);
			}
		}
	}

	@Override
	@Transactional
	public void updateGroupRole(long groupId, long roleId, boolean isRemove) {
		if (isRemove) {
			consoleRoleGroupRepo.execute("$deleteGroupRole", Params.param("groupId", groupId).put("roleId", roleId));
		} else {
			ConsoleRoleGroup has = consoleRoleGroupRepo
					.find("$findByGroupAndRole", Params.param("groupId", groupId).put("roleId", roleId)).get();
			if (has == null) {
				ConsoleRoleGroup consoleRoleGroup = new ConsoleRoleGroup();
				consoleRoleGroup.setGroupId(groupId);
				consoleRoleGroup.setRoleId(roleId);
				consoleRoleGroupRepo.save(consoleRoleGroup);
			}

		}
	}

	@Override
	public List<Long> getRoleGroupUser(long roleId) {
		Params params = Params.param("roleId", roleId);
		List<Long> ids = consoleUserRoleRepo.find("$findByRole", params).list(Long.class);
		if (CollectionUtils.isEmpty(ids)) {
			ids = Lists.newArrayList();
		}

		List<Long> groupIds = consoleRoleGroupRepo.find("$findByRole", params).list(Long.class);
		if (CollectionUtils.isNotEmpty(groupIds)) {
			ids.addAll(groupIds);
		}
		return ids;
	}

	@Override
	public List<Long> getRoleSystemMenus(long roleId, long systemId) {
		Params params = Params.param("roleId", roleId);
		params.put("systemId", systemId);

		return consoleRoleMenuRepo.find("$getRoleSystemMenus", params).list(Long.class);
	}

	@Override
	@Transactional
	public void addRoleMenus(long roleId, List<Long> menuIds) {
		List<ConsoleRoleMenu> consoleRoleMenus = consoleRoleMenuRepo
				.find("$findByRoleMenus", Params.param("roleId", roleId).put("menuIds", menuIds)).list();

		Map<Long, ConsoleRoleMenu> map = new HashMap<Long, ConsoleRoleMenu>(consoleRoleMenus.size());

		for (ConsoleRoleMenu c : consoleRoleMenus) {
			map.put(c.getMenuId(), c);
		}

		for (Long menuId : menuIds) {
			if (map.get(menuId) == null) {
				ConsoleRoleMenu obj = new ConsoleRoleMenu();
				obj.setMenuId(menuId);
				obj.setRoleId(roleId);

				consoleRoleMenuRepo.save(obj);
			}
		}
	}

	@Override
	@Transactional
	public void removeRoleMenus(long roleId, List<Long> menuIds) {
		consoleRoleMenuRepo.execute("$deleteRoleMenu", Params.param("roleId", roleId).put("menuIds", menuIds));
	}

	@Override
	public List<ConsoleRole> getUserRoles(long userId) {
		return repo.find("$queryUserRole", Params.param("userId", userId)).list();
	}

}

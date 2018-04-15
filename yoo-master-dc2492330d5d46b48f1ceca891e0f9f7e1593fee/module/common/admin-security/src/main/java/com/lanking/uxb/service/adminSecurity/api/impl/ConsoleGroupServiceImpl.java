package com.lanking.uxb.service.adminSecurity.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.support.common.auth.ConsoleGroup;
import com.lanking.cloud.domain.support.common.auth.ConsoleRoleGroup;
import com.lanking.cloud.domain.support.common.auth.ConsoleUserGroup;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.adminSecurity.api.ConsoleGroupService;
import com.lanking.uxb.service.adminSecurity.form.ConsoleGroupForm;
import com.lanking.uxb.service.adminSecurity.type.ConsoleOperateType;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@Service
@Transactional(readOnly = true)
public class ConsoleGroupServiceImpl implements ConsoleGroupService {

	@Autowired
	@Qualifier("ConsoleGroupRepo")
	private Repo<ConsoleGroup, Long> repo;
	@Autowired
	@Qualifier("ConsoleUserGroupRepo")
	private Repo<ConsoleUserGroup, Long> consoleUserGroupRepo;
	@Autowired
	@Qualifier("ConsoleRoleGroupRepo")
	private Repo<ConsoleRoleGroup, Long> consoleRoleGroupRepo;

	@Override
	@Transactional(readOnly = false)
	public ConsoleGroup save(ConsoleGroupForm form) {
		ConsoleGroup consoleGroup = new ConsoleGroup();
		consoleGroup.setStatus(Status.ENABLED);
		consoleGroup.setSystemId(form.getSystemId());
		consoleGroup.setName(form.getName());

		return repo.save(consoleGroup);
	}

	@Override
	@Transactional(readOnly = false)
	public void updateGroupUsers(Long groupId, List<Long> userIds) {
		List<ConsoleUserGroup> consoleUserGroups = consoleUserGroupRepo
				.find("$getUserGroupByGroupId", Params.param("groupId", groupId)).list();
		List<Long> oldUserIds = Lists.newArrayList();
		List<Long> needAddUserIds = Lists.newArrayList();
		List<Long> needDeleteUserIds = Lists.newArrayList();
		for (ConsoleUserGroup g : consoleUserGroups) {
			oldUserIds.add(g.getUserId());
			if (userIds.contains(g.getUserId()))
				continue;
			needDeleteUserIds.add(g.getUserId());
		}
		for (Long userId : userIds) {
			if (oldUserIds.contains(userId))
				continue;
			needAddUserIds.add(userId);
		}

		if (CollectionUtils.isNotEmpty(needDeleteUserIds)) {
			Params deleteParam = Params.param();
			deleteParam.put("userIds", needDeleteUserIds);
			deleteParam.put("groupId", groupId);
			consoleUserGroupRepo.execute("$deleteByGroupAndUser", deleteParam);
		}

		for (Long userId : needAddUserIds) {
			ConsoleUserGroup consoleUserGroup = new ConsoleUserGroup();
			consoleUserGroup.setGroupId(groupId);
			consoleUserGroup.setUserId(userId);
			consoleUserGroupRepo.save(consoleUserGroup);
		}
	}

	@Override
	public ConsoleGroup get(Long id) {
		return repo.get(id);
	}

	@Override
	@Transactional(readOnly = false)
	public void updateGroupRole(Long groupId, Long roleId, ConsoleOperateType type) {
		if (type == ConsoleOperateType.ADD) {
			ConsoleRoleGroup consoleRoleGroup = new ConsoleRoleGroup();
			consoleRoleGroup.setRoleId(roleId);
			consoleRoleGroup.setGroupId(groupId);
			consoleRoleGroupRepo.save(consoleRoleGroup);
		} else {
			Params params = Params.param();
			params.put("groupId", groupId);
			params.put("roleId", roleId);
			consoleRoleGroupRepo.execute("$deleteGroupRole", params);
		}
	}

	@Override
	public Page<ConsoleGroup> query(Long systemId, String name, Pageable pageable) {
		Params params = Params.param();

		if (systemId != null) {
			params.put("systemId", systemId);
		}
		if (name != null) {
			params.put("name", "%" + name + "%");
		}

		return repo.find("$query", params).fetch(pageable);
	}
}

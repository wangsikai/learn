package com.lanking.uxb.service.adminSecurity.api.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.support.common.auth.ConsoleUser;
import com.lanking.cloud.domain.support.common.auth.ConsoleUserRole;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.adminSecurity.api.ConsoleUserService;
import com.lanking.uxb.service.adminSecurity.ex.AdminSecurityException;
import com.lanking.uxb.service.adminSecurity.form.ConsoleUserForm;
import com.lanking.uxb.service.adminSecurity.type.ConsoleOperateType;

import httl.util.StringUtils;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@Service
@Transactional(readOnly = true)
public class ConsoleUserServiceImpl implements ConsoleUserService {

	@Autowired
	@Qualifier("ConsoleUserRepo")
	private Repo<ConsoleUser, Long> repo;
	@Autowired
	@Qualifier("ConsoleUserRoleRepo")
	private Repo<ConsoleUserRole, Long> consoleUserRoleRepo;

	@Override
	@Transactional(readOnly = false)
	public ConsoleUser save(ConsoleUserForm form) {
		ConsoleUser consoleUser = null;
		if (form.getId() != null) {
			consoleUser = repo.get(form.getId());
			consoleUser.setUpdateAt(new Date());
		} else {
			consoleUser = new ConsoleUser();
			consoleUser.setStatus(Status.ENABLED);
			consoleUser.setCreateAt(new Date());
		}
		consoleUser.setName(form.getName());
		consoleUser.setRealName(form.getRealName());
		consoleUser.setSystemId(form.getSystemId());
		if (StringUtils.isNotEmpty(form.getPlainPassword())) {
			consoleUser.setPassword(Codecs.md5Hex(form.getPlainPassword()));
		}
		return repo.save(consoleUser);
	}

	@Override
	@Transactional(readOnly = false)
	public ConsoleUser update(ConsoleUserForm form) {
		ConsoleUser consoleUser = repo.get(form.getId());
		if (form.getRealName() != null) {
			consoleUser.setRealName(form.getRealName());
		}
		if (form.getPlainPassword() != null) {
			consoleUser.setPassword(Codecs.md5Hex(form.getPlainPassword().getBytes()));
		}
		consoleUser.setUpdateAt(new Date());
		return repo.save(consoleUser);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(Long id) {
		ConsoleUser consoleUser = repo.get(id);
		consoleUser.setStatus(Status.DELETED);
		repo.save(consoleUser);
	}

	@Override
	public ConsoleUser get(Long id) {
		return repo.get(id);
	}

	@Override
	public ConsoleUser getByName(String name, Long systemId) {
		Params param = Params.param("name", name);
		param.put("systemId", systemId);
		return repo.find("$getByName", param).get();
	}

	@Override
	@Transactional(readOnly = false)
	public ConsoleUser resetPassword(Long id, String oldPassword, String newPassword) {
		ConsoleUser consoleUser = repo.get(id);
		if (consoleUser == null) {
			throw new AdminSecurityException(AdminSecurityException.ADMIN_SECURITY_NOT_EXISTS);
		}
		String oldPasswordHex = Codecs.md5Hex(oldPassword);
		if (!consoleUser.getPassword().equals(oldPasswordHex)) {
			throw new AdminSecurityException(AdminSecurityException.ADMIN_SECURITY_PASSWORD_ERROR);
		}
		consoleUser.setPassword(Codecs.md5Hex(newPassword));
		return repo.save(consoleUser);
	}

	@Override
	public void updateUserRole(Long userId, Long roleId, ConsoleOperateType type) {
		if (type == ConsoleOperateType.ADD) {
			ConsoleUserRole consoleUserRole = new ConsoleUserRole();
			consoleUserRole.setUserId(userId);
			consoleUserRole.setRoleId(roleId);
			consoleUserRoleRepo.save(consoleUserRole);
		} else {
			Params deleteParam = Params.param();
			deleteParam.put("userId", userId);
			deleteParam.put("roleId", roleId);
			consoleUserRoleRepo.execute("$deleteUserRole", deleteParam);
		}
	}

	@Override
	public List<ConsoleUser> mgetList(Collection<Long> ids) {
		if (CollectionUtils.isEmpty(ids))
			return Lists.newArrayList();
		return repo.mgetList(ids);
	}

	@Override
	public List<ConsoleUser> getUsersByGroup(Long groupId) {
		return repo.find("$getByGroup", Params.param("groupId", groupId)).list();
	}

	@Override
	public Page<ConsoleUser> query(Long systemId, String name, Pageable pageable) {
		Params params = Params.param("systemId", systemId);
		if (name != null) {
			params.put("name", "%" + name + "%");
		}
		return repo.find("$query", params).fetch(pageable);
	}

	@Override
	@Transactional
	public void updateUserStatus(long userId, Status status) {
		ConsoleUser user = repo.get(userId);
		if (user == null) {
			throw new AdminSecurityException(AdminSecurityException.ADMIN_SECURITY_NOT_EXISTS);
		}
		if (user.getSystemId() == 0) {
			throw new NoPermissionException();
		}

		user.setStatus(status);
		repo.save(user);
	}

	@Override
	public List<ConsoleUser> findBySystem(long systemId) {

		return repo.find("$findBySystem", Params.param("systemId", systemId)).list();
	}

	@Override
	public Map<Long, ConsoleUser> mget(Collection<Long> ids) {
		return repo.mget(ids);
	}
}

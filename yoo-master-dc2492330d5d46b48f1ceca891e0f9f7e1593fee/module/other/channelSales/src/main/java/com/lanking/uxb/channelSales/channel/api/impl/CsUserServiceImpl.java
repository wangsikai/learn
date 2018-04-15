package com.lanking.uxb.channelSales.channel.api.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.channelSales.channel.api.CsUserService;
import com.lanking.uxb.channelSales.channel.form.ChannelUserQueryForm;

/**
 * @author xinyu.zhou
 * @since 2.5.0
 */
@Service
@Transactional(readOnly = true)
public class CsUserServiceImpl implements CsUserService {
	@Autowired
	@Qualifier("UserRepo")
	private Repo<User, Long> userRepo;

	@Override
	public User get(long id) {
		return userRepo.get(id);
	}

	@Override
	public Map<Long, User> mget(Collection<Long> ids) {
		return userRepo.mget(ids);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> findBySchool(long schoolId, UserType userType) {
		if (userType == UserType.TEACHER) {
			return userRepo.find("$csFindTeacherUserBySchool", Params.param("schoolId", schoolId)).list();
		} else if (userType == UserType.STUDENT) {
			return userRepo.find("$csFindStudentUserBySchool", Params.param("schoolId", schoolId)).list();
		} else {
			return Collections.EMPTY_LIST;
		}
	}

	@Override
	public List<User> mgetList(Collection<Long> ids) {
		return userRepo.mgetList(ids);
	}

	@Override
	public Page<Map> findByChannel(ChannelUserQueryForm query, Pageable p) {
		Params params = Params.param("code", query.getCode());
		if (null != query.getPhaseCode()) {
			params.put("phaseCode", query.getPhaseCode());
		}
		if (null != query.getUserType()) {
			params.put("userType", query.getUserType().getValue());
		}
		if (null != query.getMemberType()) {
			params.put("memberType", query.getMemberType().getValue());
		} else {
			params.put("memberType", -1);
		}
		if (null != query.getSex()) {
			params.put("sex", query.getSex());
		}
		if (null != query.getSchoolId()) {
			params.put("schoolId", query.getSchoolId());
		}
		if (StringUtils.isNotBlank(query.getAccountName())) {
			params.put("accountName", query.getAccountName());
		}
		if (StringUtils.isNotBlank(query.getUserName())) {
			params.put("userName", query.getUserName());
		}
		if (StringUtils.isNotBlank(query.getMobile())) {
			params.put("mobile", query.getMobile());
		}
		params.put("nowDate", new Date());
		return userRepo.find("$csGetUsers", params).fetch(p, Map.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> findByAccountNames(List<String> names) {
		if (CollectionUtils.isEmpty(names)) {
			return Collections.EMPTY_LIST;
		}

		Params params = Params.param("names", names);

		return userRepo.find("$csFindByAccountNames", params).list();
	}
}

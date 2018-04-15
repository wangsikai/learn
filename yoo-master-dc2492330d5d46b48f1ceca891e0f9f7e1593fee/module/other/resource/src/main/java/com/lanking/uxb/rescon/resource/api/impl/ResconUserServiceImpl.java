package com.lanking.uxb.rescon.resource.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.resource.api.ResconUserService;
import com.lanking.uxb.rescon.resource.value.VResconUser;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@Service
@Transactional(readOnly = true)
public class ResconUserServiceImpl implements ResconUserService {
	@Autowired
	@Qualifier("UserRepo")
	private Repo<User, Long> repo;

	@Override
	public VResconUser get(long id) {
		Params params = Params.param();
		params.put("id", id);
		Map map = repo.find("$resconGetUser", params).get(Map.class);
		VResconUser v = new VResconUser();
		v.setId(Long.parseLong(map.get("id").toString()));
		v.setName((String) map.get("name"));
		v.setSchoolName((String) map.get("schoolName"));
		return v;
	}

	@Override
	public Map<Long, VResconUser> mget(Collection<Long> ids) {
		Params params = Params.param();
		params.put("ids", ids);
		List<Map> listMap = repo.find("$resconGetUsers", params).list(Map.class);
		Map<Long, VResconUser> resconUserMap = Maps.newHashMap();
		for (Map m : listMap) {
			VResconUser v = new VResconUser();
			v.setId(Long.parseLong(m.get("id").toString()));
			v.setName((String) m.get("name"));
			v.setSchoolName((String) m.get("schoolName"));
			resconUserMap.put(v.getId(), v);
		}
		return resconUserMap;
	}

	@Override
	public List<VResconUser> megtList(List<Long> userIds) {
		Params params = Params.param();
		params.put("ids", userIds);
		List<Map> listMap = repo.find("$resconGetUsers", params).list(Map.class);
		List<VResconUser> userList = Lists.newArrayList();
		for (Map m : listMap) {
			VResconUser v = new VResconUser();
			v.setId(Long.parseLong(m.get("id").toString()));
			v.setName((String) m.get("name"));
			v.setSchoolName((String) m.get("schoolName"));
			userList.add(v);
		}
		return userList;
	}
}

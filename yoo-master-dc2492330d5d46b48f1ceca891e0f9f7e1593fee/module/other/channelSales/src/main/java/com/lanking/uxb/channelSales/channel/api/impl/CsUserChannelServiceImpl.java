package com.lanking.uxb.channelSales.channel.api.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.support.common.auth.ConsoleUser;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.channelSales.channel.api.CsUserChannelService;

/**
 * @author xinyu.zhou
 * @since 2.5.0
 */
@Service
@Transactional(readOnly = true)
public class CsUserChannelServiceImpl implements CsUserChannelService {

	@Autowired
	@Qualifier("UserChannelRepo")
	private Repo<UserChannel, Integer> userChannelRepo;

	@Override
	public UserChannel get(int code) {
		return userChannelRepo.get(code);
	}

	@Override
	public Map<Integer, UserChannel> mget(Collection<Integer> codes) {
		return userChannelRepo.mget(codes);
	}

	@SuppressWarnings("rawtypes")
	@Transactional
	@Override
	public UserChannel create(String name) {
		UserChannel channel = new UserChannel();
		Map maxMap = userChannelRepo.find("$csgetMaxCode").get(Map.class);
		channel.setCode(((Short) maxMap.get("code")).intValue() + 1);
		channel.setOriginalName(name);
		channel.setName(name);
		channel.setOpenMemberLimit(new BigDecimal(0.00));
		channel.setOpenedMember(new BigDecimal(0.00));
		return userChannelRepo.save(channel);
	}

	@Override
	public UserChannel findBySchool(long schoolId) {
		return userChannelRepo.find("$csFindBySchool", Params.param("schoolId", schoolId)).get();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<Long, UserChannel> mgetBySchools(Collection<Long> schoolIds) {
		if (CollectionUtils.isEmpty(schoolIds)) {
			return Collections.EMPTY_MAP;
		}
		List<Map> channelList = userChannelRepo.find("$csFindBySchools", Params.param("schoolIds", schoolIds)).list(
				Map.class);
		if (CollectionUtils.isEmpty(channelList)) {
			return Collections.EMPTY_MAP;
		}
		Map<Long, UserChannel> channelMap = new HashMap<Long, UserChannel>(channelList.size());
		for (Map u : channelList) {
			UserChannel userChannel = new UserChannel();
			userChannel.setName((String) u.get("name"));
			userChannel.setCode(Integer.parseInt(String.valueOf(u.get("code"))));
			channelMap.put(((BigInteger) u.get("school_id")).longValue(), userChannel);
		}

		return channelMap;
	}

	@Override
	public Page<UserChannel> query(Pageable p) {
		return userChannelRepo.find("$csQuery").fetch(p);
	}

	@Transactional
	@Override
	public int bindConName(Long code, String conName) {
		Params params = Params.param("conName", conName);
		// 判断当前帐号是否存在
		long count = userChannelRepo.find("$csGetConUserByName", params).count();
		ConsoleUser user = new ConsoleUser();
		user.setName(conName);
		UserChannel uc = getChannelByName(conName);
		if (count == 0 || null != uc) {
			return 0;
		}
		params.put("code", code);
		return userChannelRepo.execute("$csBindConName", params);
	}

	@Transactional
	@Override
	public int removeBind(Long code) {
		Params params = Params.param("code", code);
		return userChannelRepo.execute("$csRemoveBind", params);
	}

	@Override
	public UserChannel getChannelByUser(Long conUserId) {
		Params params = Params.param("userId", conUserId);
		return userChannelRepo.find("$csGetChannel", params).get();
	}

	@Override
	public UserChannel getChannelByName(String conName) {
		Params params = Params.param("conName", conName);
		return userChannelRepo.find("$csGetChannelByName", params).get();
	}

	@Transactional
	@Override
	public UserChannel update(int code, String name) {
		UserChannel channel = userChannelRepo.get(code);
		channel.setName(name);
		return userChannelRepo.save(channel);
	}

	@Transactional
	@Override
	public UserChannel updateLimit(int code, BigDecimal limit) {
		UserChannel channel = userChannelRepo.get(code);
		channel.setOpenMemberLimit(limit);
		return userChannelRepo.save(channel);
	}

	@Override
	public List<UserChannel> findAllChannelList() {
		return userChannelRepo.getAll();
	}

	@Override
	public int getFirstYearByChannel(int code) {
		List<Integer> list = userChannelRepo.find("$csGetFirstYearByChannelSettlement", Params.param("code", code))
				.list(Integer.class);
		if (list.size() == 0) {
			Calendar c = Calendar.getInstance();
			return c.get(Calendar.YEAR);

		} else {
			return list.get(0);
		}
	}

}

package com.lanking.uxb.zycon.user.api.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.user.api.ZycUserChannelService;

@Transactional(readOnly = true)
@Service
public class ZycUserChannelServiceImpl implements ZycUserChannelService {

	@Autowired
	@Qualifier("UserChannelRepo")
	Repo<UserChannel, Integer> userChannelRepo;

	@Autowired
	@Qualifier("UserRepo")
	Repo<User, Integer> userRepo;

	@Override
	public Page<UserChannel> query(Pageable p) {
		return userChannelRepo.find("$query").fetch(p);
	}

	@Override
	public UserChannel get(int code) {
		return userChannelRepo.get(code);
	}

	@Override
	public Map<Integer, UserChannel> mget(Collection<Integer> codes) {
		return userChannelRepo.mget(codes);
	}

	@Override
	public List<UserChannel> getAll() {
		return userChannelRepo.getAll();
	}

	@SuppressWarnings("rawtypes")
	@Transactional
	@Override
	public UserChannel create(String name) {
		UserChannel channel = new UserChannel();
		Map maxMap = userChannelRepo.find("$getMaxCode").get(Map.class);
		channel.setCode(((Short) maxMap.get("code")).intValue() + 1);
		channel.setOriginalName(name);
		channel.setName(name);
		channel.setOpenedMember(new BigDecimal(0.00));
		channel.setOpenMemberLimit(new BigDecimal(0.00));
		return userChannelRepo.save(channel);
	}

	@Transactional
	@Override
	public UserChannel update(int code, String name) {
		UserChannel channel = userChannelRepo.get(code);
		channel.setName(name);
		return userChannelRepo.save(channel);
	}

	@SuppressWarnings("rawtypes")
	@Transactional
	@Override
	public void staticChannelUserCount() {
		List<UserChannel> channels = getAll();
		for (UserChannel userChannel : channels) {
			List<Map> data = userRepo.find("$getStaticChannelUserCount", Params.param("code", userChannel.getCode()))
					.list(Map.class);
			for (Map map : data) {
				UserType type = UserType.findByValue(((Byte) map.get("user_type")).intValue());
				long count = ((BigInteger) map.get("cou")).longValue();
				if (type == UserType.TEACHER) {
					userChannel.setTeacherCount(count);
				} else if (type == UserType.STUDENT) {
					userChannel.setStudentCount(count);
				}
			}
			userChannelRepo.save(userChannel);
		}
	}

}

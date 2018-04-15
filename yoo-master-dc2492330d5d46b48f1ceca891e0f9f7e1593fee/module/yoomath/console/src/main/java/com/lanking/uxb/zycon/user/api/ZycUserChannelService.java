package com.lanking.uxb.zycon.user.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

public interface ZycUserChannelService {

	Page<UserChannel> query(Pageable p);

	UserChannel get(int code);

	Map<Integer, UserChannel> mget(Collection<Integer> codes);

	List<UserChannel> getAll();

	UserChannel create(String name);

	UserChannel update(int code, String name);

	void staticChannelUserCount();
}

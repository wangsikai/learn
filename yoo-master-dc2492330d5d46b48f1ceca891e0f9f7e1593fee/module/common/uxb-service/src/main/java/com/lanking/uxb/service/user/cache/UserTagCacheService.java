package com.lanking.uxb.service.user.cache;

import java.util.List;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.yoo.common.Tag;
import com.lanking.cloud.domain.yoo.user.UserTag;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

@SuppressWarnings("unchecked")
@Service
public class UserTagCacheService extends AbstractCacheService {

	private ValueOperations<String, List<UserTag>> userTagsOpt;
	private ValueOperations<String, List<Tag>> tagsOpt;

	private static final String TAGS_KEY = "t";
	private static final String USERTAGS_KEY = "ut";

	@Override
	public String getNs() {
		return "t";
	}

	@Override
	public String getNsCn() {
		return "标签";
	}

	private String getUserTagsKey(long userId) {
		return assemblyKey(USERTAGS_KEY, userId);
	}

	public void setTags(List<Tag> tags) {
		tagsOpt.set(TAGS_KEY, tags);
	}

	public List<Tag> getTags() {
		return tagsOpt.get(TAGS_KEY);
	}

	public void invalidTags() {
		getRedisTemplate().delete(TAGS_KEY);
	}

	public void setUserTags(long userId, List<UserTag> userTags) {
		userTagsOpt.set(getUserTagsKey(userId), userTags);
	}

	public List<UserTag> getUserTags(long userId) {
		return userTagsOpt.get(getUserTagsKey(userId));
	}

	public void invalidUserTags(long userId) {
		getRedisTemplate().delete(getUserTagsKey(userId));
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		userTagsOpt = getRedisTemplate().opsForValue();
		tagsOpt = getRedisTemplate().opsForValue();
	}
}

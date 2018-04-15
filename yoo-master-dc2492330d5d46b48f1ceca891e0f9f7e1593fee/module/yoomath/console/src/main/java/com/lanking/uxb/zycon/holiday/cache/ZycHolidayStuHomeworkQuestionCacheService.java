package com.lanking.uxb.zycon.holiday.cache;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.cache.api.SerializerType;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 假期学生作业批改题目缓存Service
 *
 * @author xinyu.zhou
 * @since yoomath V1.9
 */
@Component
public class ZycHolidayStuHomeworkQuestionCacheService extends AbstractCacheService {
	private static String STUDENTHQKKEY = "shqk";
	private ZSetOperations<String, String> listOps;

	private String getKey() {
		return STUDENTHQKKEY;
	}

	/**
	 * 将推送给批改员进行批改的用户的题目push进来
	 *
	 * @param studentQuestionId
	 *            HolidayStuHomeworkItemQuestion Id
	 */
	public void push(Long studentQuestionId) {
		String key = getKey();
		listOps.add(key, studentQuestionId.toString(), System.currentTimeMillis());
	}

	/**
	 * 查询已经推送过的题目id
	 *
	 * @return 题目id列表
	 */
	public List<Long> alreadyPushedIds() {
		String key = getKey();
		long lastCommitMinutes = TimeUnit.MINUTES.toMillis(Env.getInt("zycon.homework.allcommit.then"));
		Set<ZSetOperations.TypedTuple<String>> values;
		long min = 0;
		long currentMillis = System.currentTimeMillis();
		List<Long> ids = Lists.newArrayList();
		values = listOps.rangeByScoreWithScores(key, min, currentMillis, 0, 10000);
		for (ZSetOperations.TypedTuple<String> v : values) {
			min = v.getScore().longValue();
			boolean timeout = min < (currentMillis - lastCommitMinutes);
			if (timeout) {
				listOps.remove(key, v.getValue());
			} else {
				ids.add(Long.valueOf(v.getValue()));
			}
		}
		return ids;
	}

	/**
	 * 去除已经推送过的题目
	 *
	 * @param homeworkId
	 *            作业id
	 * @param ids
	 *            需要去除的题目id
	 */
	public void remove(Long homeworkId, List<Long> ids) {
		String key = getKey();
		for (Long id : ids) {
			listOps.remove(key, id.toString());
		}
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		listOps = getRedisTemplate().opsForZSet();
	}

	@Override
	public SerializerType getSerializerType() {
		return SerializerType.STRING;
	}

	@Override
	public String getNs() {
		return "zyc-sqhhq";
	}

	@Override
	public String getNsCn() {
		return "悠数学管控台假期学生作业缓存";
	}
}

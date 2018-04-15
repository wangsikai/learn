package com.lanking.uxb.service.question.cache.word;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.common.resource.question.QuestionWordMLData;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 习题WordML预处理缓存服务.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年8月4日
 */
@Service
public class QuestionWordMLCacheService extends AbstractCacheService {
	private static final String COMMENT_KEY = "ct";
	private ValueOperations<String, QuestionWordMLData> questionWordmlOpt;

	@Override
	public String getNs() {
		return "q-wrd";
	}

	@Override
	public String getNsCn() {
		return "习题WordML缓存";
	}

	/**
	 * 获得KEY.
	 * 
	 * @param id
	 *            习题ID.
	 * @return
	 */
	private String getKey(long id) {
		return assemblyKey(COMMENT_KEY, id);
	}

	/**
	 * 存储习题缓存.
	 * 
	 * @param questionWordMLData
	 */
	public void set(QuestionWordMLData questionWordMLData) {
		if (questionWordMLData != null) {
			questionWordmlOpt.set(getKey(questionWordMLData.getId()), questionWordMLData);
		}
	}

	/**
	 * 批量习题存储.
	 * 
	 * @param questionWordMLDatas
	 */
	public void mutilSet(Collection<QuestionWordMLData> questionWordMLDatas) {
		Map<String, QuestionWordMLData> map = new HashMap<String, QuestionWordMLData>(questionWordMLDatas.size());
		for (QuestionWordMLData data : questionWordMLDatas) {
			map.put(getKey(data.getId()), data);
		}
		questionWordmlOpt.multiSet(map);
	}

	/**
	 * 获取习题缓存.
	 * 
	 * @param id
	 *            习题ID
	 * @return
	 */
	public QuestionWordMLData get(long id) {
		return questionWordmlOpt.get(getKey(id));
	}

	/**
	 * 批量获取习题缓存.
	 * 
	 * @param ids
	 * @return
	 */
	public Map<Long, QuestionWordMLData> mget(Collection<Long> ids) {
		Map<Long, QuestionWordMLData> map = new HashMap<Long, QuestionWordMLData>(0);
		if (ids == null) {
			return map;
		}
		List<String> keys = new ArrayList<String>(ids.size());
		for (Long id : ids) {
			keys.add(getKey(id));
		}
		List<QuestionWordMLData> list = questionWordmlOpt.multiGet(keys);
		for (QuestionWordMLData data : list) {
			if (data != null) {
				map.put(data.getId(), data);
			}
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		questionWordmlOpt = getRedisTemplate().opsForValue();
	}

}

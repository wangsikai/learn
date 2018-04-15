package com.lanking.uxb.service.zuoye.cache;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.uxb.service.cache.api.SerializerType;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;
import com.lanking.uxb.service.zuoye.value.VQuestionCar;

/**
 * 题目篮子(类似于会话级别的购物车)<br>
 * 
 * 缓存需要跟当前会话token关联
 * 
 * @since yoomathV1.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年9月6日
 */
@Service
public final class QuestionCarCacheService extends AbstractCacheService {
	private static final String QUESTION_CAR_HASH_KEY = "qch";
	private static final String QUESTION_SORTED_CAR_KEY = "sqc";
	// 存储用户添加题目顺序
	private static final String QUESTION_ID_SORTED_CAR = "qisc";
	private HashOperations<String, String, String> qcHash;
	// 排序后的题目List
	private ListOperations<String, String> sortedQuestionsList;

	private ListOperations<String, String> sortedQuestionIdsList;

	private String getQuestionCarHashKey(String token) {
		return assemblyKey(QUESTION_CAR_HASH_KEY, token);
	}

	private String getSortedQuestionListKey(String token) {
		return assemblyKey(QUESTION_CAR_HASH_KEY, token, QUESTION_SORTED_CAR_KEY);
	}

	private String getSortedQuestionIdsListKey(String token) {
		return assemblyKey(QUESTION_CAR_HASH_KEY, token, QUESTION_ID_SORTED_CAR);
	}

	/**
	 * 添加题目至作业篮子
	 *
	 * @param token
	 *            token
	 * @param id
	 *            题目的id
	 * @param difficult
	 *            难度系数
	 */
	public void addToCar(String token, Long id, Double difficult, Integer type) {
		String key = getQuestionCarHashKey(token);
		String listKey = getSortedQuestionListKey(token);
		String value = qcHash.get(key, id.toString());
		// 如果已经存在则不再进行添加操作
		if (value == null) {
			String storageValue = null;
			if (type == null) {
				storageValue = difficult.toString();
			} else {
				storageValue = difficult.toString() + "," + type.toString();
			}

			qcHash.put(key, id.toString(), storageValue);

			// 当排序队列当中已经有数据的时候则直接往后添加
			if (sortedQuestionsList.size(listKey) != 0) {
				sortedQuestionsList.rightPush(listKey, id.toString());
			}
		}
	}

	public long countQuestions(String token) {
		return qcHash.size(getQuestionCarHashKey(token));
	}

	/**
	 * 从作业篮子里删除数据
	 *
	 * @param token
	 *            token
	 * @param id
	 *            题目的id
	 */
	public void removeFromCar(String token, Long id) {
		String key = getQuestionCarHashKey(token);
		qcHash.delete(key, id.toString());
		sortedQuestionsList.remove(getSortedQuestionListKey(token), 1, id.toString());
		sortedQuestionIdsList.remove(getSortedQuestionIdsListKey(token), 1, id.toString());
	}

	/**
	 * 删除作业篮子中所有题目
	 *
	 * @param token
	 *            token
	 */
	public void removeAll(String token) {
		String key = getQuestionCarHashKey(token);
		qcHash.getOperations().delete(key);
		sortedQuestionsList.getOperations().delete(getSortedQuestionListKey(token));
		sortedQuestionIdsList.getOperations().delete(getSortedQuestionIdsListKey(token));
	}

	/**
	 * 得到题目的id及对应的难度系数
	 *
	 * @param token
	 *            token
	 * @return map类型数据
	 */
	public Map<Long, Double> getAll(String token) {
		String key = getQuestionCarHashKey(token);
		Map<String, String> allValueMap = qcHash.entries(key);
		Map<Long, Double> finalMap = Maps.newHashMap();
		for (Map.Entry<String, String> entry : allValueMap.entrySet()) {
			finalMap.put(Long.parseLong(entry.getKey()), Double.valueOf(entry.getValue()));
		}

		return finalMap;
	}

	/**
	 * 获得VQuestionCar List类型的数据
	 *
	 * @param token
	 *            token
	 * @return list类型的数据
	 */
	public List<VQuestionCar> mgetList(String token) {
		String key = getQuestionCarHashKey(token);
		Map<String, String> allValueMap = qcHash.entries(key);
		List<VQuestionCar> list = Lists.newArrayList();
		for (Map.Entry<String, String> entry : allValueMap.entrySet()) {
			VQuestionCar v = new VQuestionCar();
			v.setId(Long.valueOf(entry.getKey()));
			v.setDifficult(Double.valueOf(entry.getValue().split(",")[0]));
			v.setType(Question.Type.findByValue(Integer.valueOf(entry.getValue().split(",")[1])));
			list.add(v);
		}

		return list;
	}

	/**
	 * 获得现在作业篮子里所有作业id
	 *
	 * @param token
	 *            token
	 * @return 作业id
	 */
	public List<Long> mgetListIds(String token) {
		String key = getQuestionCarHashKey(token);
		Map<String, String> allValueMap = qcHash.entries(key);
		List<Long> ids = Lists.newArrayList();
		for (Map.Entry<String, String> entry : allValueMap.entrySet()) {
			ids.add(Long.valueOf(entry.getKey()));
		}

		return ids;
	}

	/**
	 * 将排序过的题目顺序存储下来
	 *
	 * @param token
	 *            token or userId
	 * @param ids
	 *            题目的id集合
	 */
	public void addSortedQuestion(String token, Collection<Long> ids) {
		String key = getSortedQuestionListKey(token);
		sortedQuestionsList.getOperations().delete(key);
		for (Long id : ids) {
			sortedQuestionsList.rightPush(key, String.valueOf(id));
		}
	}

	/**
	 * 获得排序过后的题目id集合
	 *
	 * @param token
	 *            token or userId
	 * @return 题目id集合
	 */
	public List<Long> getSortedQuestionIds(String token) {
		String key = getSortedQuestionListKey(token);
		List<String> queryResult = sortedQuestionsList.range(key, 0, sortedQuestionsList.size(key));
		List<Long> ids = Lists.newArrayList();
		for (String i : queryResult) {
			ids.add(Long.parseLong(i));
		}

		return ids;
	}

	/**
	 * 存储所有的题目id
	 *
	 * @param token
	 *            token or userId
	 * @param ids
	 *            题目的id集合
	 */
	public void addQuestionIds(String token, Collection<Long> ids) {
		String key = getSortedQuestionIdsListKey(token);
		sortedQuestionIdsList.getOperations().delete(key);
		for (Long id : ids) {
			sortedQuestionIdsList.rightPush(key, String.valueOf(id));
		}
	}

	/**
	 * 获得所有的题目id，按照存储顺序取
	 *
	 * @param token
	 *            token or userId
	 * @return 题目id集合
	 */
	public List<Long> getQuestionIds(String token) {
		String key = getSortedQuestionIdsListKey(token);
		List<String> queryResult = sortedQuestionIdsList.range(key, 0, sortedQuestionIdsList.size(key));
		List<Long> ids = Lists.newArrayList();
		for (String i : queryResult) {
			ids.add(Long.parseLong(i));
		}

		return ids;
	}

	@Override
	public String getNs() {
		return "tcar";
	}

	@Override
	public String getNsCn() {
		return "题目购物车";
	}

	@Override
	public SerializerType getSerializerType() {
		return SerializerType.STRING;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		qcHash = getRedisTemplate().opsForHash();
		sortedQuestionsList = getRedisTemplate().opsForList();
		sortedQuestionIdsList = getRedisTemplate().opsForList();
	}
}

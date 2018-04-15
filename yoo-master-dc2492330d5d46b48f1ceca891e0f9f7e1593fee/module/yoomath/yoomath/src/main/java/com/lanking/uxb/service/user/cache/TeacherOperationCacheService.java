package com.lanking.uxb.service.user.cache;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 记录教师操作的相关缓存
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年4月19日
 */
@Service
public class TeacherOperationCacheService extends AbstractCacheService {

	private ValueOperations<String, String> stringOpt;

	// 上次选择的教辅图书
	private static final String HOMEWORKBOOK_SCHOOLBOOK = "hkbsb";
	// 教师的当前进度(章节)
	public static final String PROGRESS = "progress";
	// 教师的当前进度(教材)
	public static final String PROGRESS_TEXTBOOK = "pgtextbook";
	// 记录上次布置作业选择的班级ID
	private static final String HOMEWORK_CLASS_ID = "hkcid";
	// 推荐来源
	private static final String RECOMMEND_SOURCE = "rcdsource";
	// 记录上次布置作业选择的groupID
	private static final String HOMEWORK_CLASS_GROUP_ID = "hcgid";
	// 记录选择的教辅，按顺序存
	private static final String RESOURCE_BOOK_ID = "rbid";

	/**
	 * 教师节活动分享存储分享链接参数临时使用.
	 */
	private static final String SHARE_TEMP_PARAMS = "stp";

	public Long getHomeworkBookSchoolBook(long userId) {
		String value = stringOpt.get(assemblyKey(HOMEWORKBOOK_SCHOOLBOOK, String.valueOf(userId)));
		if (StringUtils.isBlank(value)) {
			return null;
		} else {
			return Long.parseLong(value);
		}
	}

	public void setHomeworkBookSchoolBook(long userId, Long schoolBookId) {
		stringOpt.set(assemblyKey(HOMEWORKBOOK_SCHOOLBOOK, String.valueOf(userId)), schoolBookId.toString());
	}

	public Long getProgress(long userId, String progess) {
		String value = stringOpt.get(assemblyKey(progess, String.valueOf(userId)));
		if (StringUtils.isBlank(value)) {
			return null;
		} else {
			return Long.parseLong(value);
		}
	}

	public void setProgress(long userId, Long value, String progess) {
		stringOpt.set(assemblyKey(progess, String.valueOf(userId)), value.toString());
	}

	public List<Long> getHomeworkClass(long userId) {
		String value = stringOpt.get(assemblyKey(HOMEWORK_CLASS_ID, String.valueOf(userId)));
		List<Long> ids = new ArrayList<>();
		if (StringUtils.isBlank(value)) {
			return ids;
		} else {
			String[] values = value.split(",");
			for (int i = 0; i < values.length; i++) {
				ids.add(Long.parseLong(values[i]));
			}

			return ids;
		}
	}

	/**
	 * 获取上一次选择的组
	 * 
	 * @param userId
	 * @return
	 */
	public List<Long> getHomeworkGroup(long userId) {
		String value = stringOpt.get(assemblyKey(HOMEWORK_CLASS_GROUP_ID, String.valueOf(userId)));
		List<Long> ids = new ArrayList<>();
		if (StringUtils.isBlank(value)) {
			return ids;
		} else {
			String[] values = value.split(",");
			for (int i = 0; i < values.length; i++) {
				ids.add(Long.parseLong(values[i]));
			}

			return ids;
		}
	}

	/**
	 * 获取上一次选择的教辅书本
	 * 
	 * @param userId
	 * @return
	 */
	public List<Long> getSelectBooks(long userId) {
		String value = stringOpt.get(assemblyKey(RESOURCE_BOOK_ID, String.valueOf(userId)));
		List<Long> ids = new ArrayList<>();
		if (StringUtils.isBlank(value)) {
			return ids;
		} else {
			String[] values = value.split(",");
			for (int i = 0; i < values.length; i++) {
				ids.add(Long.parseLong(values[i]));
			}

			return ids;
		}
	}

	public void setSelectBooks(long userId, List<Long> bookIds) {
		StringBuilder builder = new StringBuilder();
		for (Long id : bookIds) {
			builder.append(id);
			builder.append(",");
		}
		stringOpt.set(assemblyKey(RESOURCE_BOOK_ID, String.valueOf(userId)), builder.toString());
	}

	public void setHomeworkClass(long userId, List<Long> homeworkClassIds) {
		StringBuilder builder = new StringBuilder();
		for (Long id : homeworkClassIds) {
			builder.append(id);
			builder.append(",");
		}
		stringOpt.set(assemblyKey(HOMEWORK_CLASS_ID, String.valueOf(userId)), builder.toString());
	}

	/**
	 * 记入当前选择的组
	 * 
	 * @param userId
	 * @param groupIds
	 */
	public void setHomeworkGroup(long userId, List<Long> groupIds) {
		StringBuilder builder = new StringBuilder();
		for (Long id : groupIds) {
			builder.append(id);
			builder.append(",");
		}
		stringOpt.set(assemblyKey(HOMEWORK_CLASS_GROUP_ID, String.valueOf(userId)), builder.toString());
	}

	public List<Integer> getRecommendSource(long userId) {
		String value = stringOpt.get(assemblyKey(RECOMMEND_SOURCE, String.valueOf(userId)));
		List<Integer> valueList = new ArrayList<>();
		if (StringUtils.isBlank(value)) {
			return valueList;
		} else {
			String[] values = value.split(",");
			for (int i = 0; i < values.length; i++) {
				try {
					valueList.add(Integer.parseInt(values[i]));
				} catch (NumberFormatException e) {
				}
			}

			return valueList;
		}
	}

	public void setRecommendSource(long userId, List<Integer> sources) {
		List<String> temp = Lists.newArrayList();
		for (Integer i : sources) {
			temp.add(String.valueOf(i));
		}
		stringOpt.set(assemblyKey(RECOMMEND_SOURCE, String.valueOf(userId)), String.join(",", temp));
	}

	/**
	 * 存储活动参数.
	 * 
	 * @param uuid
	 * @param params
	 */
	public void setActivityShareParams(String uuid, String params) {
		stringOpt.set(assemblyKey(SHARE_TEMP_PARAMS, uuid), params);
	}

	/**
	 * 获取活动参数.
	 * 
	 * @param uuid
	 * @return
	 */
	public String getActivityShareParams(String uuid) {
		return stringOpt.get(assemblyKey(SHARE_TEMP_PARAMS, uuid));
	}

	@Override
	public String getNs() {
		return "mop";
	}

	@Override
	public String getNsCn() {
		return "手机端操作的相关缓存";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		stringOpt = getRedisTemplate().opsForValue();
	}

}

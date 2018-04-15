package com.lanking.uxb.service.resources.api;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.homework.Exercise;

/**
 * 提供练习相关接口
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月14日
 */
public interface ExerciseService {

	/**
	 * 获取一个练习
	 * 
	 * @param id
	 *            练习ID
	 * @return 练习对象
	 */
	Exercise get(long id);

	/**
	 * 获取多个练习
	 * 
	 * @param id
	 *            练习ID
	 * @return 练习对象
	 */
	Map<Long, Exercise> mget(Collection<Long> ids);

}

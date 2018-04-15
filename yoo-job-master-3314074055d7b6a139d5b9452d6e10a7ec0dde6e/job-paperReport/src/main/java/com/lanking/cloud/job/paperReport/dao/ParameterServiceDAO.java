package com.lanking.cloud.job.paperReport.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;

public interface ParameterServiceDAO {

	/**
	 * 获得Parameter数据
	 *
	 * 例如:
	 * 
	 * <pre>
	 * n = test
	 * mm = ${n}hello[0],world[1]
	 * args传两个值分别为 1、2
	 *
	 * 则获得的value值为
	 * testhello1,world2
	 * </pre>
	 *
	 * @param product
	 *            {@link Product}
	 * @param key
	 *            key值
	 * @param args
	 *            需要注入替换的参数
	 * @return {@link Parameter}
	 */
	Parameter get(Product product, String key, String... args);

	/**
	 * 根据key值列表批量获得Parameter Map对象
	 *
	 * @param product
	 *            {@link Product}
	 * @param keys
	 *            key值列表
	 * @return {@link Map}
	 */
	Map<String, Parameter> mget(Product product, Collection<String> keys);

	/**
	 * 根据key值列表批量获得Parameter Map对象 <strong> 支持注入参数替换 </strong>
	 *
	 * @param product
	 *            {@link Product}
	 * @param keys
	 *            key值列表
	 * @param values
	 *            需要注入的参数
	 * @return {@link Map}
	 */
	Map<String, Parameter> mget(Product product, Collection<String> keys, Map<String, String[]> values);

	/**
	 * 根据同一key值，不同注入参数，批量获取Parameter中的value.
	 * 
	 * @param product
	 *            {@link Product}
	 * @param key
	 *            key值
	 * @param argsList
	 *            需要注入的参数
	 * @return
	 */
	List<String> mgetValueList(Product product, String key, List<String[]> argsList);
}

package com.lanking.uxb.service.code.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.District;

public interface DistrictService {

	/**
	 * 根据层级获取地域列表
	 * 
	 * @param level
	 *            层级
	 * @return 地域列表
	 */
	List<District> getDistrictByLevel(int level);

	/**
	 * 根据地域代码获取地域列表
	 * 
	 * @param code
	 *            地域代码
	 * @return 地域列表
	 */
	List<District> getDistrictByPcode(long code);

	/**
	 * 根据地域代码获取地域
	 * 
	 * @param code
	 *            地域代码
	 * @return 地域
	 */
	District getDistrict(long code);

	/**
	 * 根据地域代码获取父级地域
	 * 
	 * @param code
	 *            地域代码
	 * @return 父级地域
	 */
	District getPDistrict(long code);

	/**
	 * 根据地域代码获取地域名称
	 * 
	 * @param code
	 *            地域代码
	 * @return 地域名称(全称)
	 */
	String getDistrictName(Long code);

	/**
	 * 根据地域代码集合获取地域名称Map
	 * 
	 * @param codes
	 *            地域代码集合
	 * @return 地域名称Map(全称)
	 */
	Map<Long, String> mgetDistrictName(Collection<Long> codes);

	Map<Long, District> mget(Collection<Long> ids);

	/**
	 * 获得所有地区
	 *
	 * @return
	 */
	List<District> findAll();

}

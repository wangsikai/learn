package com.lanking.uxb.channelSales.channel.api;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

/**
 * 渠道管理service
 *
 * @author xinyu.zhou
 * @since 2.5.0
 */
public interface CsUserChannelService {
	/**
	 * 创建渠道
	 *
	 * @param name
	 *            渠道名称
	 *
	 * @return UserChannel
	 */
	UserChannel create(String name);

	/**
	 * 更新渠道名称
	 * 
	 * @param code
	 *            渠道code
	 * @param name
	 * @return UserChannel
	 */
	UserChannel update(int code, String name);

	/**
	 * 更新渠商额度
	 * 
	 * @param limit
	 *            渠道商户额度
	 * @param code
	 * @return UserChannel
	 */
	UserChannel updateLimit(int code, BigDecimal limit);

	/**
	 * 查询渠道
	 *
	 * @param
	 *
	 * @return {@link Page}
	 */
	Page<UserChannel> query(Pageable p);

	/**
	 * 根据code获得数据
	 *
	 * @param code
	 *            渠道商代码
	 * @return {@link UserChannel}
	 */
	UserChannel get(int code);

	/**
	 * mget 渠道商数据
	 *
	 * @param codes
	 *            渠道商代码列表
	 * @return {@link Map}
	 */
	Map<Integer, UserChannel> mget(Collection<Integer> codes);

	/**
	 * 绑定渠道
	 *
	 * @param codes
	 *            渠道码
	 * @return conName用户帐号
	 */
	int bindConName(Long code, String conName);

	/**
	 * 解除绑定
	 *
	 * @param codes
	 *            渠道码
	 */
	int removeBind(Long code);

	/**
	 * 查询渠道
	 *
	 * @param ConsoleUserId
	 * @return
	 */
	UserChannel getChannelByUser(Long conUserId);

	/**
	 * 查询渠道
	 *
	 * @param conName
	 * @return
	 */
	UserChannel getChannelByName(String conName);

	/**
	 * 根据学校id得到对应的渠道数据
	 *
	 * @param schoolId
	 *            学校id
	 * @return {@link UserChannel}
	 */
	UserChannel findBySchool(long schoolId);

	/**
	 * 根据学校id列表mget数据
	 *
	 * @param schoolIds
	 *            学校id列表
	 * @return {@link Map}
	 */
	Map<Long, UserChannel> mgetBySchools(Collection<Long> schoolIds);

	/**
	 * 获取所有的渠道列表
	 * 
	 * @return
	 */
	List<UserChannel> findAllChannelList();

	/**
	 * 查询渠道最早开始年份
	 * 
	 * @return
	 */
	int getFirstYearByChannel(int code);

}

package com.lanking.uxb.channelSales.memberPackage.api;

import java.util.Collection;
import java.util.List;

import com.lanking.cloud.domain.yoo.member.MemberPackageGroupChannel;

/**
 * 会员套餐组-渠道-学校
 * 
 * @author zemin.song
 * @version 2017年3月6日
 */
public interface CsMemberPackageGroupChannelService {

	/**
	 * 通过套餐组ID查询渠道-学校
	 * 
	 * @param groupIds
	 * @return
	 */
	List<MemberPackageGroupChannel> getSchoolByGroupIds(Collection<Long> groupIds);

	/**
	 * 通过学校sID查询与套餐组对应关系
	 * 
	 * @param schoolIds
	 * @return
	 */
	List<MemberPackageGroupChannel> getGetyGroupBySchools(Collection<Long> schoolIds);

	/**
	 * 通过渠道组ID与套餐组对应关系
	 * 
	 * @param schoolId为0查询
	 *            只与组对应关系
	 * @param schoolId为空
	 *            查询所有对应关系
	 * @param channel
	 * @return
	 */
	List<MemberPackageGroupChannel> getGetyGroupByChannelCodes(Collection<Integer> channelCodes, Long schoolId);

}

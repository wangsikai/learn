package com.lanking.uxb.service.channel.api.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.channel.api.UserChannelCountService;

@Transactional(readOnly = true)
@Service
public class UserChannelCountServiceImpl implements UserChannelCountService {
	@Autowired
	@Qualifier("UserChannelRepo")
	Repo<UserChannel, Integer> userChannelRepo;

	@Transactional
	@Override
	public void staticChannelUserCount() {
		// 查询所有渠道
		List<UserChannel> channels = userChannelRepo.getAll();
		List<UserChannel> newChannels = new ArrayList<UserChannel>();
		for (UserChannel userChannel : channels) {
			this.initUserChannel(userChannel);
			// 获取当前渠道下的用户数量
			List<Map> userCountData = userChannelRepo
					.find("$taskGetStaticChannelUserCount", Params.param("code", userChannel.getCode()))
					.list(Map.class);
			// 获取当前渠道下签约学校的数量
			Long schoolCount = userChannelRepo
					.find("$taskGetStaticChannelSchoolCount", Params.param("code", userChannel.getCode())).count();
			// 获取当前渠道下会员用户数量
			List<Map> vipCountDate = userChannelRepo.find("$taskGetStaticChannelVipCount",
					Params.param("code", userChannel.getCode()).put("nowDate", new Date())).list(Map.class);
			for (Map map : userCountData) {
				UserType type = UserType.findByValue(((Byte) map.get("user_type")).intValue());
				long count = ((BigInteger) map.get("cou")).longValue();
				if (type == UserType.TEACHER) {
					userChannel.setTeacherCount(count);
				} else if (type == UserType.STUDENT) {
					userChannel.setStudentCount(count);
				}
			}
			userChannel.setSchoolCount(Integer.valueOf(schoolCount.toString()));
			for (Map map : vipCountDate) {
				UserType type = UserType.findByValue(((Byte) map.get("user_type")).intValue());
				if (map.get("member_type") == null) {
					continue;
				}
				MemberType mtype = MemberType.findByValue(((Byte) map.get("member_type")).intValue());
				long count = ((BigInteger) map.get("cou")).longValue();
				if (type == UserType.TEACHER && mtype == MemberType.VIP) {
					userChannel.setTeacherVipCount(count);
				} else if (type == UserType.STUDENT && mtype == MemberType.VIP) {
					userChannel.setStudentVipCount(count);
				} else if (type == UserType.TEACHER && mtype == MemberType.SCHOOL_VIP) {
					userChannel.setTeacherSchoolVipCount(count);
				}
			}
			newChannels.add(userChannel);
		}
		userChannelRepo.save(newChannels);

	}

	/**
	 * 初始化数量
	 * 
	 * @param userChannel
	 */
	public void initUserChannel(UserChannel userChannel) {
		userChannel.setSchoolCount(0);
		userChannel.setStudentCount(0);
		userChannel.setTeacherCount(0);
		userChannel.setTeacherVipCount(0);
		userChannel.setStudentVipCount(0);
		userChannel.setTeacherSchoolVipCount(0);
	}

}

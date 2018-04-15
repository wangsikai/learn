package com.lanking.uxb.channelSales.channel.convert;

import httl.util.CollectionUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.support.common.auth.ConsoleUser;
import com.lanking.cloud.domain.yoo.channel.ChannelSchool;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.domain.yoo.member.MemberPackageGroupChannel;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.channelSales.channel.api.CsChannelSchoolService;
import com.lanking.uxb.channelSales.channel.value.VSchool;
import com.lanking.uxb.channelSales.channel.value.VUserChannel;
import com.lanking.uxb.channelSales.memberPackage.api.CsMemberPackageGroupChannelService;
import com.lanking.uxb.channelSales.memberPackage.api.CsMemberPackageService;
import com.lanking.uxb.service.adminSecurity.api.ConsoleUserService;

/**
 * UserChannel -> VUserChannel
 *
 * @author xinyu.zhou
 * @since 2.5.0
 */
@Component
public class CsUserChannelConvert extends Converter<VUserChannel, UserChannel, Integer> {

	@Autowired
	private ConsoleUserService consoleUserService;
	@Autowired
	private CsChannelSchoolService csChannelSchoolService;
	@Autowired
	private CsChannelSchoolTreeConvert csChannelSchooltoSchoolConvert;
	@Autowired
	private CsMemberPackageService csMemberPackageService;
	@Autowired
	private CsMemberPackageGroupChannelService csMemberPackageGroupChannelService;

	@Override
	protected Integer getId(UserChannel userChannel) {
		return userChannel.getCode();
	}

	@Override
	protected VUserChannel convert(UserChannel userChannel) {
		VUserChannel v = new VUserChannel();
		v.setCode(userChannel.getCode());
		v.setName(userChannel.getName());
		v.setOriginalName(userChannel.getOriginalName());
		v.setSchoolCount(userChannel.getSchoolCount());
		v.setStudentCount(userChannel.getStudentCount());
		v.setStudentVipCount(userChannel.getStudentVipCount());
		v.setTeacherCount(userChannel.getTeacherCount());
		v.setTeacherSchoolVipCount(userChannel.getTeacherSchoolVipCount());
		v.setTeacherVipCount(userChannel.getTeacherVipCount());
		v.setOpenMemberLimit(userChannel.getOpenMemberLimit());
		v.setOpenedMember(userChannel.getOpenedMember());
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VUserChannel, UserChannel, Long, ConsoleUser>() {

			@Override
			public boolean accept(UserChannel s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(UserChannel s, VUserChannel d) {
				return s.getConUserId();
			}

			@Override
			public void setValue(UserChannel s, VUserChannel d, ConsoleUser value) {
				d.setConUserName(value.getName());
			}

			@Override
			public ConsoleUser getValue(Long key) {
				return consoleUserService.get(key);
			}

			@Override
			public Map<Long, ConsoleUser> mgetValue(Collection<Long> keys) {
				Map<Long, ConsoleUser> retMap = new HashMap<Long, ConsoleUser>(keys.size());
				List<ConsoleUser> conUserlist = consoleUserService.mgetList(keys);
				for (ConsoleUser user : conUserlist) {
					if (user != null) {
						retMap.put(user.getId(), user);
					}
				}
				return retMap;
			}

		});

		assemblers.add(new ConverterAssembler<VUserChannel, UserChannel, Integer, MemberPackageGroupChannel>() {

			@Override
			public boolean accept(UserChannel arg0) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> arg0) {
				return true;
			}

			@Override
			public Integer getKey(UserChannel u, VUserChannel v) {
				return u.getCode();
			}

			@Override
			public void setValue(UserChannel u, VUserChannel v, MemberPackageGroupChannel m) {
				if (m != null) {
					v.setClickDisabled(true);
					v.setMemberPackageGroupName(csMemberPackageService.getGroupById(m.getMemberPackageGroupId())
							.getName());
				}
			}

			@Override
			public MemberPackageGroupChannel getValue(Integer key) {
				return null;
			}

			@Override
			public Map<Integer, MemberPackageGroupChannel> mgetValue(Collection<Integer> keys) {
				List<MemberPackageGroupChannel> list = csMemberPackageGroupChannelService.getGetyGroupByChannelCodes(
						keys, 0L);
				Map<Integer, MemberPackageGroupChannel> map = new HashMap<Integer, MemberPackageGroupChannel>(keys
						.size());
				for (Integer key : keys) {
					for (MemberPackageGroupChannel mpgc : list) {
						if (key.longValue() == mpgc.getUserChannelCode()) {
							map.put(key, mpgc);
						}
					}
				}
				return map;

			}

		});

		assemblers.add(new ConverterAssembler<VUserChannel, UserChannel, Integer, List<ChannelSchool>>() {

			@Override
			public boolean accept(UserChannel arg0) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> arg0) {
				return true;
			}

			@Override
			public Integer getKey(UserChannel u, VUserChannel v) {
				return u.getCode();
			}

			@Override
			public List<ChannelSchool> getValue(Integer key) {
				return null;
			}

			@Override
			public Map<Integer, List<ChannelSchool>> mgetValue(Collection<Integer> keys) {
				return csChannelSchoolService.mgetSchoolByChannel(keys);
			}

			@Override
			public void setValue(UserChannel u, VUserChannel v, List<ChannelSchool> csList) {
				if (CollectionUtils.isNotEmpty(csList)) {
					List<VSchool> sList = csChannelSchooltoSchoolConvert.to(csList);
					v.setChildren(sList);
				}
			}

		});

	}
}

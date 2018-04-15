package com.lanking.uxb.channelSales.memberPackage.convert;

import httl.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.domain.yoo.member.MemberPackageGroup;
import com.lanking.cloud.domain.yoo.member.MemberPackageGroupChannel;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.channelSales.channel.api.CsUserChannelService;
import com.lanking.uxb.channelSales.channel.convert.CsSchoolConvert;
import com.lanking.uxb.channelSales.channel.convert.CsUserChannelConvert;
import com.lanking.uxb.channelSales.channel.value.VSchool;
import com.lanking.uxb.channelSales.channel.value.VUserChannel;
import com.lanking.uxb.channelSales.memberPackage.api.CsMemberPackageGroupChannelService;
import com.lanking.uxb.channelSales.memberPackage.value.VMemberPackageGroup;
import com.lanking.uxb.service.code.api.SchoolService;

@Component
public class CsMemberPackageGroupConvert extends Converter<VMemberPackageGroup, MemberPackageGroup, Long> {

	@Autowired
	private CsMemberPackageGroupChannelService csMemberPackageGroupChannelService;

	@Autowired
	private CsSchoolConvert csSchoolConvert;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private CsUserChannelService csUserChannelService;
	@Autowired
	private CsUserChannelConvert csUserChannelConvert;

	@Override
	protected Long getId(MemberPackageGroup s) {
		return s.getId();
	}

	@Override
	protected VMemberPackageGroup convert(MemberPackageGroup m) {
		VMemberPackageGroup vo = new VMemberPackageGroup();
		vo.setId(m.getId());
		vo.setMemberType(m.getMemberType());
		vo.setName(m.getName());
		vo.setProfits1(m.getProfits1());
		vo.setProfits2(m.getProfits2());
		vo.setType(m.getType());
		vo.setUserType(m.getUserType());
		return vo;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {

		// 转换Account
		assemblers
				.add(new ConverterAssembler<VMemberPackageGroup, MemberPackageGroup, Long, List<MemberPackageGroupChannel>>() {

					@Override
					public boolean accept(MemberPackageGroup arg0) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> arg0) {
						return true;
					}

					@Override
					public Long getKey(MemberPackageGroup m, VMemberPackageGroup v) {
						return m.getId();
					}

					@Override
					public List<MemberPackageGroupChannel> getValue(Long key) {
						return null;
					}

					@Override
					public Map<Long, List<MemberPackageGroupChannel>> mgetValue(Collection<Long> keys) {
						List<MemberPackageGroupChannel> list = csMemberPackageGroupChannelService
								.getSchoolByGroupIds(keys);
						Map<Long, List<MemberPackageGroupChannel>> map = new HashMap<Long, List<MemberPackageGroupChannel>>(
								keys.size());
						for (Long key : keys) {
							List<MemberPackageGroupChannel> temp = Lists.newArrayList();
							for (MemberPackageGroupChannel mpgc : list) {
								if (mpgc.getMemberPackageGroupId() == key.longValue()) {
									temp.add(mpgc);
								}
							}
							if (CollectionUtils.isNotEmpty(temp)) {
								map.put(key, temp);
							}
						}
						return map;
					}

					@Override
					public void setValue(MemberPackageGroup m, VMemberPackageGroup v,
							List<MemberPackageGroupChannel> list) {
						// tree
						Set<Integer> channelCodes = new HashSet<Integer>();
						List<Long> schoolIds = Lists.newArrayList();
						for (MemberPackageGroupChannel mgc : list) {
							schoolIds.add(mgc.getSchoolId());
							channelCodes.add(mgc.getUserChannelCode());
						}
						Collection<UserChannel> ucList = csUserChannelService.mget(channelCodes).values();
						List<VUserChannel> vus = csUserChannelConvert.to(Lists.newArrayList(ucList));
						Collection<School> schools = schoolService.mget(schoolIds).values();
						List<VSchool> vschools = csSchoolConvert.to(Lists.newArrayList(schools));
						for (VUserChannel vu : vus) {
							vu.setChildren(new ArrayList<VSchool>());
							for (VSchool vs : vschools) {
								if (vu.getCode() == vs.getChannelCode().longValue()) {
									vu.getChildren().add(vs);
								}
							}

						}
						v.setUserChannel(vus);
					}

				});

	}
}

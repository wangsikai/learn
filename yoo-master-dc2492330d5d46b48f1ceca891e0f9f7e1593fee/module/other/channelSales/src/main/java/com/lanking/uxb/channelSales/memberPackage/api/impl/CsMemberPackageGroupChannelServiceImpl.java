package com.lanking.uxb.channelSales.memberPackage.api.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.member.MemberPackageGroupChannel;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.channelSales.memberPackage.api.CsMemberPackageGroupChannelService;

@Service
@Transactional(readOnly = true)
public class CsMemberPackageGroupChannelServiceImpl implements CsMemberPackageGroupChannelService {
	@Autowired
	@Qualifier("MemberPackageGroupChannelRepo")
	private Repo<MemberPackageGroupChannel, Long> memberPackageGroupChannelRepo;

	@Override
	public List<MemberPackageGroupChannel> getSchoolByGroupIds(Collection<Long> keys) {
		return memberPackageGroupChannelRepo.find("$csGetSchoolByGroupIds", Params.param("groupIds", keys)).list();
	}

	@Override
	public List<MemberPackageGroupChannel> getGetyGroupBySchools(Collection<Long> schoolIds) {
		return memberPackageGroupChannelRepo.find("$csGetyGroupBySchoolIds", Params.param("schoolIds", schoolIds))
				.list();
	}

	@Override
	public List<MemberPackageGroupChannel> getGetyGroupByChannelCodes(Collection<Integer> channelCodes, Long schoolId) {
		Params prams = Params.param("channelCodes", channelCodes);
		if (schoolId != null) {
			prams.put("schoolId", schoolId);
		}
		return memberPackageGroupChannelRepo.find("$csGetyGroupByChannelCodes", prams).list();
	}

}

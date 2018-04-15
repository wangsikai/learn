package com.lanking.uxb.channelSales.channel.convert;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.yoo.channel.ChannelSchool;
import com.lanking.cloud.domain.yoo.member.MemberPackageGroupChannel;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.channelSales.channel.value.VSchool;
import com.lanking.uxb.channelSales.memberPackage.api.CsMemberPackageGroupChannelService;
import com.lanking.uxb.channelSales.memberPackage.api.CsMemberPackageService;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.service.code.convert.SchoolConvert;

/**
 * ChannelSchool -> VSchool
 *
 */
@Component
public class CsChannelSchoolTreeConvert extends Converter<VSchool, ChannelSchool, Long> {

	@Autowired
	private SchoolService schoolService;
	@Autowired
	private SchoolConvert schoolConvert;
	@Autowired
	private CsMemberPackageGroupChannelService csMemberPackageGroupChannelService;
	@Autowired
	private CsMemberPackageService csMemberPackageService;

	@Override
	protected VSchool convert(ChannelSchool c) {
		VSchool vo = new VSchool();
		vo.setId(c.getSchoolId());
		return vo;
	}

	@Override
	protected Long getId(ChannelSchool c) {
		return c.getSchoolId();
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 学校
		assemblers.add(new ConverterAssembler<VSchool, ChannelSchool, Long, School>() {

			@Override
			public boolean accept(ChannelSchool arg0) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> arg0) {
				return true;
			}

			@Override
			public Long getKey(ChannelSchool c, VSchool v) {
				return c.getSchoolId();
			}

			@Override
			public School getValue(Long key) {
				return schoolService.get(key);
			}

			@Override
			public Map<Long, School> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}
				return schoolService.mget(keys);
			}

			@Override
			public void setValue(ChannelSchool c, VSchool vo, School s) {
				vo.setName(s.getName());
			}
		});

		assemblers.add(new ConverterAssembler<VSchool, ChannelSchool, Long, MemberPackageGroupChannel>() {

			@Override
			public boolean accept(ChannelSchool arg0) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> arg0) {
				return true;
			}

			@Override
			public Long getKey(ChannelSchool c, VSchool v) {
				return c.getSchoolId();
			}

			@Override
			public MemberPackageGroupChannel getValue(Long keys) {
				return null;
			}

			@Override
			public Map<Long, MemberPackageGroupChannel> mgetValue(Collection<Long> keys) {
				List<MemberPackageGroupChannel> list = csMemberPackageGroupChannelService.getGetyGroupBySchools(keys);
				Map<Long, MemberPackageGroupChannel> map = new HashMap<Long, MemberPackageGroupChannel>(keys.size());
				for (Long key : keys) {
					for (MemberPackageGroupChannel mpgc : list) {
						if (key.longValue() == mpgc.getSchoolId()) {
							map.put(key, mpgc);
						}
					}
				}
				return map;
			}

			@Override
			public void setValue(ChannelSchool c, VSchool v, MemberPackageGroupChannel m) {
				if (m != null) {
					v.setClickDisabled(true);
					v.setMemberPackageGroupName(csMemberPackageService.getGroupById(m.getMemberPackageGroupId())
							.getName());
				}

			}

		});
	}
}

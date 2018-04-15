package com.lanking.uxb.service.activity.convert;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.type.FileStyle;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01ClassUser;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.member.UserMember;
import com.lanking.cloud.domain.yoo.user.UserInfo;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.activity.value.VHolidayActivity01ClassUser;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.service.user.api.UserMemberService;
import com.lanking.uxb.service.user.api.UserService;

@Component
public class HolidayActivity01ClassUserConvert
		extends Converter<VHolidayActivity01ClassUser, HolidayActivity01ClassUser, Long> {

	@Autowired
	private UserMemberService userMemberService;
	@Autowired
	private UserService userService;

	@Override
	protected Long getId(HolidayActivity01ClassUser s) {
		return s.getId();
	}

	@Override
	protected VHolidayActivity01ClassUser convert(HolidayActivity01ClassUser s) {
		VHolidayActivity01ClassUser v = new VHolidayActivity01ClassUser();
		v.setUserId(s.getUserId());
		v.setSubmitRate(s.getSubmitRate());
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 转换会员状态
		assemblers.add(
				new ConverterAssembler<VHolidayActivity01ClassUser, HolidayActivity01ClassUser, Long, UserMember>() {
					@Override
					public boolean accept(HolidayActivity01ClassUser s) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(HolidayActivity01ClassUser s, VHolidayActivity01ClassUser d) {
						return s.getUserId();
					}

					@Override
					public void setValue(HolidayActivity01ClassUser s, VHolidayActivity01ClassUser d,
							UserMember value) {
						if (value == null) {
							d.setMemberType(MemberType.NONE);
						} else {
							if (value.getEndTime().getTime() < System.currentTimeMillis()) {
								d.setMemberType(MemberType.NONE);
							} else {
								d.setMemberType(value.getMemberType());
							}
						}
					}

					@Override
					public UserMember getValue(Long key) {
						return userMemberService.findSafeByUserId(key);
					}

					@SuppressWarnings("unchecked")
					@Override
					public Map<Long, UserMember> mgetValue(Collection<Long> keys) {
						if (CollectionUtils.isEmpty(keys)) {
							return Collections.EMPTY_MAP;
						}

						return userMemberService.mgetByUserIds(keys);
					}

				});
		// 基础信息
		assemblers
				.add(new ConverterAssembler<VHolidayActivity01ClassUser, HolidayActivity01ClassUser, Long, UserInfo>() {

					@Override
					public boolean accept(HolidayActivity01ClassUser s) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(HolidayActivity01ClassUser s, VHolidayActivity01ClassUser d) {
						return s.getUserId();
					}

					@Override
					public void setValue(HolidayActivity01ClassUser s, VHolidayActivity01ClassUser d, UserInfo value) {
						d.setName(value.getName());
						d.setAvatarId(value.getAvatarId() == null ? 0 : value.getAvatarId());
						if (value.getAvatarId() != null && value.getAvatarId() != 0) {
							d.setAvatarUrl(FileUtil.getUrl(value.getAvatarId()));
							d.setMinAvatarUrl(FileUtil.getUrl(FileStyle.AVATAR_MID, value.getAvatarId()));
						}
					}

					@Override
					public UserInfo getValue(Long key) {
						return userService.getUser(key);
					}

					@Override
					public Map<Long, UserInfo> mgetValue(Collection<Long> keys) {
						Map<Long, UserInfo> ret = new HashMap<>();
						for (Long id : keys) {
							UserInfo info = userService.getUser(id);
							if (info != null) {
								ret.put(info.getId(), info);
							}
						}
						return ret;
					}

				});
	}
}

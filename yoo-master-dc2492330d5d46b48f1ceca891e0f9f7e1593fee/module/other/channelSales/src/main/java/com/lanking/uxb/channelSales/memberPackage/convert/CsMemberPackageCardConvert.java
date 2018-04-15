package com.lanking.uxb.channelSales.memberPackage.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.support.common.auth.ConsoleUser;
import com.lanking.cloud.domain.yoo.member.MemberPackageCard;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.channelSales.channel.api.CsUserService;
import com.lanking.uxb.channelSales.memberPackage.api.CsConsoleUserService;
import com.lanking.uxb.channelSales.memberPackage.value.VMemberPackageCard;
import com.lanking.uxb.channelSales.openmember.api.CsAccountService;

@Component
public class CsMemberPackageCardConvert extends Converter<VMemberPackageCard, MemberPackageCard, String> {

	@Autowired
	private CsConsoleUserService consoleUserService;

	@Autowired
	private CsUserService userService;

	@Autowired
	private CsAccountService accountService;

	@Override
	protected String getId(MemberPackageCard s) {
		return s.getCode();
	}

	@Override
	protected VMemberPackageCard convert(MemberPackageCard s) {
		VMemberPackageCard vo = new VMemberPackageCard();
		vo.setCode(s.getCode());
		vo.setUserType(s.getUserType());
		vo.setMemberType(s.getMemberType());
		vo.setMonth(s.getMonth());
		vo.setPrice(s.getPrice());
		vo.setCreateAt(s.getCreateAt());
		vo.setEndAt(s.getEndAt());
		vo.setStatus(s.getStatus());
		vo.setUpdateAt(s.getUpdateAt());
		vo.setUseTime(s.getUsedAt());
		vo.setMemo(s.getMemo());
		return vo;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {

		// 转换Account
		assemblers.add(new ConverterAssembler<VMemberPackageCard, MemberPackageCard, Long, ConsoleUser>() {

			@Override
			public boolean accept(MemberPackageCard arg0) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> arg0) {
				return true;
			}

			@Override
			public Long getKey(MemberPackageCard m, VMemberPackageCard v) {
				return m.getCreateId();
			}

			@Override
			public ConsoleUser getValue(Long id) {
				return consoleUserService.get(id);
			}

			@Override
			public Map<Long, ConsoleUser> mgetValue(Collection<Long> ids) {
				return consoleUserService.mget(ids);
			}

			@Override
			public void setValue(MemberPackageCard m, VMemberPackageCard v, ConsoleUser a) {
				if (a != null) {
					v.setCreateName(a.getName());
				}
			}

		});

		assemblers.add(new ConverterAssembler<VMemberPackageCard, MemberPackageCard, Long, User>() {

			@Override
			public boolean accept(MemberPackageCard arg0) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> arg0) {
				return true;
			}

			@Override
			public Long getKey(MemberPackageCard m, VMemberPackageCard v) {
				return m.getUserId();
			}

			@Override
			public User getValue(Long id) {
				if (id != null) {
					return userService.get(id);
				}
				return null;
			}

			@Override
			public Map<Long, User> mgetValue(Collection<Long> ids) {
				return userService.mget(ids);
			}

			@Override
			public void setValue(MemberPackageCard m, VMemberPackageCard v, User a) {
				if (a != null) {
					v.setUpdateName(accountService.get(a.getAccountId()).getName());
				}
			}

		});

	}
}

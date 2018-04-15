package com.lanking.uxb.channelSales.user.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.support.channelSales.user.ChannelUserOperateLog;
import com.lanking.cloud.domain.support.common.auth.ConsoleUser;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.channelSales.user.value.VChannelUserOperateLog;
import com.lanking.uxb.service.adminSecurity.api.ConsoleUserService;

/**
 * 操作log--convert
 * 
 * @author wangsenhao
 *
 */
@Component
public class ChannelUserOperateLogConvert extends Converter<VChannelUserOperateLog, ChannelUserOperateLog, Long> {

	@Autowired
	private ConsoleUserService consoleUserService;

	@Override
	protected Long getId(ChannelUserOperateLog arg0) {
		return arg0.getId();
	}

	@Override
	protected VChannelUserOperateLog convert(ChannelUserOperateLog c) {
		VChannelUserOperateLog v = new VChannelUserOperateLog();
		v.setCreateAt(c.getCreateAt());
		v.setType(c.getOperateType());
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VChannelUserOperateLog, ChannelUserOperateLog, Long, ConsoleUser>() {

			@Override
			public boolean accept(ChannelUserOperateLog arg0) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> arg0) {
				return true;
			}

			@Override
			public Long getKey(ChannelUserOperateLog arg0, VChannelUserOperateLog arg1) {
				return arg0.getCreateId();
			}

			@Override
			public ConsoleUser getValue(Long arg0) {
				return consoleUserService.get(arg0);
			}

			@Override
			public Map<Long, ConsoleUser> mgetValue(Collection<Long> arg0) {
				return consoleUserService.mget(arg0);
			}

			@Override
			public void setValue(ChannelUserOperateLog arg0, VChannelUserOperateLog arg1, ConsoleUser arg2) {
				arg1.setCreatName(arg2.getName());
			}
		});

	}
}

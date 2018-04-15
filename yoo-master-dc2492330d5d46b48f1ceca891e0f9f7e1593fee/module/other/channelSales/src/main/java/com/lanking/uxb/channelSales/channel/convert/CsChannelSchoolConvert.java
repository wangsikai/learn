package com.lanking.uxb.channelSales.channel.convert;

import com.lanking.cloud.domain.yoo.channel.ChannelSchool;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.channelSales.channel.api.CsUserChannelService;
import com.lanking.uxb.channelSales.channel.value.VChannelSchool;
import com.lanking.uxb.channelSales.channel.value.VUserChannel;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.service.code.convert.SchoolConvert;
import com.lanking.uxb.service.code.value.VSchool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * ChannelSchool -> VChannelSchool
 *
 * @author xinyu.zhou
 * @since 2.5.0
 */
@Component
public class CsChannelSchoolConvert extends Converter<VChannelSchool, ChannelSchool, Long> {
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private SchoolConvert schoolConvert;
	@Autowired
	private CsUserChannelService userChannelService;
	@Autowired
	private CsUserChannelConvert userChannelConvert;

	@Override
	protected Long getId(ChannelSchool channelSchool) {
		return channelSchool.getId();
	}

	@Override
	protected VChannelSchool convert(ChannelSchool channelSchool) {
		VChannelSchool v = new VChannelSchool();
		v.setId(channelSchool.getId());
		return v;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 学校
		assemblers.add(new ConverterAssembler<VChannelSchool, ChannelSchool, Long, VSchool>() {

			@Override
			public boolean accept(ChannelSchool channelSchool) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ChannelSchool channelSchool, VChannelSchool vChannelSchool) {
				return channelSchool.getSchoolId();
			}

			@Override
			public void setValue(ChannelSchool channelSchool, VChannelSchool vChannelSchool, VSchool value) {
				vChannelSchool.setSchool(value);
			}

			@Override
			public VSchool getValue(Long key) {
				return schoolConvert.to(schoolService.get(key));
			}

			@Override
			@SuppressWarnings("unchecked")
			public Map<Long, VSchool> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}

				return schoolConvert.to(schoolService.mget(keys));
			}
		});

		// 转换
		assemblers.add(new ConverterAssembler<VChannelSchool, ChannelSchool, Integer, VUserChannel>() {

			@Override
			public boolean accept(ChannelSchool channelSchool) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(ChannelSchool channelSchool, VChannelSchool vChannelSchool) {
				return channelSchool.getChannelCode();
			}

			@Override
			public void setValue(ChannelSchool channelSchool, VChannelSchool vChannelSchool, VUserChannel value) {
				vChannelSchool.setUserChannel(value);
			}

			@Override
			public VUserChannel getValue(Integer key) {
				return userChannelConvert.to(userChannelService.get(key));
			}

			@Override
			@SuppressWarnings("unchecked")
			public Map<Integer, VUserChannel> mgetValue(Collection<Integer> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}
				return userChannelConvert.to(userChannelService.mget(keys));
			}
		});
	}
}

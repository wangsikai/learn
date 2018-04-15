package com.lanking.uxb.channelSales.channel.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.channel.ChannelFile;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.channelSales.channel.value.VChannelFile;
import com.lanking.uxb.service.file.util.FileUtil;

/**
 * 渠道资料转换.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2017年3月3日
 */
@Component
public class CsChannelFileConvert extends Converter<VChannelFile, ChannelFile, Long> {

	@Override
	protected Long getId(ChannelFile s) {
		return s.getId();
	}

	@Override
	protected VChannelFile convert(ChannelFile s) {
		if (s != null) {
			VChannelFile v = new VChannelFile();
			v.setId(s.getId());
			v.setCoverId(s.getCoverId());
			v.setCoverUrl(FileUtil.getUrl(s.getCoverId()));
			v.setCreateAt(s.getCreateAt());
			v.setCreateId(s.getCreateId());
			v.setFileId(s.getFileId());
			v.setName(s.getName());
			v.setUpdateAt(s.getUpdateAt());
			v.setUpdateId(s.getUpdateId());
			v.setUri(s.getUri());
			return v;
		}
		return null;
	}

}

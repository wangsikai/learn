package com.lanking.uxb.zycon.wx.convert;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.thirdparty.weixin.response.WXButton;
import com.lanking.uxb.zycon.wx.value.VWXButton;

/**
 * 菜单转换工具.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年7月28日
 */
@Component
public class ZycWXButtonConvert extends Converter<VWXButton, WXButton, String> {

	@Override
	protected String getId(WXButton s) {
		return s.getKey();
	}

	@Override
	protected VWXButton convert(WXButton s) {
		if (s != null) {
			VWXButton vo = new VWXButton();
			vo.setKey(s.getKey());
			vo.setMediaId(s.getMedia_id());
			vo.setName(s.getName());
			vo.setType(s.getType());
			vo.setUrl(s.getUrl());
			if (s.getSub_button() != null && s.getSub_button().size() > 0) {
				vo.setSubButtons(this.to(s.getSub_button()));
			} else {
				vo.setSubButtons(new ArrayList<VWXButton>(0));
			}
			return vo;
		}
		return null;
	}
}

package com.lanking.uxb.zycon.user.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.zycon.user.api.ZycUserChannelService;

/**
 * 用户渠道管理
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年4月16日
 */
@RestController
@RequestMapping("zyc/userChannel")
public class ZycUserChannelController {

	@Autowired
	private ZycUserChannelService zycUserChannelService;

	@RequestMapping(value = "query")
	public Value query(Integer pageSize, Integer page) {
		pageSize = pageSize == null ? 20 : (Math.min(pageSize, 20));
		page = page == null ? 1 : page;
		VPage<UserChannel> voPage = new VPage<UserChannel>();
		Page<UserChannel> poPage = zycUserChannelService.query(P.index(page, pageSize));
		voPage.setPageSize(pageSize);
		voPage.setCurrentPage(page);
		voPage.setTotalPage(poPage.getPageCount());
		voPage.setTotal(poPage.getTotalCount());
		voPage.setItems(poPage.getItems());
		return new Value(voPage);
	}

	@RequestMapping(value = "create")
	public Value create(String name) {
		zycUserChannelService.create(name);
		return new Value();
	}

	@RequestMapping(value = "update")
	public Value update(int code, String name) {
		zycUserChannelService.update(code, name);
		return new Value();
	}

	@RequestMapping(value = "staticUserCount")
	public Value staticUserCount() {
		zycUserChannelService.staticChannelUserCount();
		return new Value();
	}
}

package com.lanking.uxb.zycon.operation.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.lanking.cloud.domain.yoo.customerService.CustomerLogReadStatus;
import com.lanking.cloud.domain.yoo.customerService.YoomathCustomerServiceLog;
import com.lanking.cloud.domain.yoo.customerService.YoomathCustomerServiceSession;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.value.VCursorPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.zycon.homework.api.ZycUserService;
import com.lanking.uxb.zycon.operation.api.ZycYoomathCustomerServiceLogService;
import com.lanking.uxb.zycon.operation.api.ZycYoomathCustomerServiceSessionService;
import com.lanking.uxb.zycon.operation.convert.ZycYoomathCustomerServiceLogConvert;
import com.lanking.uxb.zycon.operation.convert.ZycYoomathCustomerServiceSessionConvert;
import com.lanking.uxb.zycon.operation.value.VZycYoomathCustomerServiceLog;
import com.lanking.uxb.zycon.user.convert.ZycUserConvert;

/**
 * 后台客服相关接口
 *
 * @author xinyu.zhou
 * @since yoomath V1.7
 */
@RestController
@RequestMapping(value = "zyc/customer")
public class ZycCustomerController {
	@Autowired
	private ZycYoomathCustomerServiceLogService yoomathCustomerServiceLogService;
	@Autowired
	private ZycYoomathCustomerServiceLogConvert yoomathCustomerServiceLogConvert;
	@Autowired
	private ZycYoomathCustomerServiceSessionService yoomathCustomerServiceSessionService;
	@Autowired
	private ZycYoomathCustomerServiceSessionConvert yoomathCustomerServiceSessionConvert;
	@Autowired
	private ZycUserService zycUserService;
	@Autowired
	private ZycUserConvert zycUserConvert;

	/**
	 * 得到最新的消息
	 *
	 * @param cursor
	 *            游标
	 * @param size
	 *            查询大小
	 * @return {@link Value}
	 */
	@RequestMapping(value = "pull", method = { RequestMethod.GET, RequestMethod.POST })
	public Value pull(@RequestParam(value = "cursor", defaultValue = "0") long cursor,
			@RequestParam(value = "size", defaultValue = "20") int size,
			@RequestParam(value = "status", required = false) CustomerLogReadStatus status,
			@RequestParam(value = "csId") long csId, @RequestParam(value = "userId") long userId) {

		CursorPageable<Long> cursorPageable = CP.cursor(cursor, size);
		CursorPage<Long, YoomathCustomerServiceLog> page = yoomathCustomerServiceLogService.pull(cursorPageable,
				userId, status, csId);

		List<YoomathCustomerServiceLog> logs = page.getItems();
		VCursorPage<VZycYoomathCustomerServiceLog> vPage = new VCursorPage<VZycYoomathCustomerServiceLog>();
		List<VZycYoomathCustomerServiceLog> vLogs = yoomathCustomerServiceLogConvert.to(logs);

		vPage.setItems(vLogs);
		vPage.setCursor(page.getNextCursor() == null ? 0 : page.getNextCursor());

		return new Value(vPage);
	}

	/**
	 * 得到在线用户
	 *
	 * @param ids
	 *            用户id
	 * @return {@link Value}
	 */
	@RequestMapping(value = "get_online_user", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getOnlineUser(String ids) {
		List<Long> idList = JSON.parseArray(ids, Long.class);
		return new Value(zycUserConvert.to(zycUserService.mgetList(idList)));
	}

	/**
	 * 后台登录后显示未读信息数
	 *
	 * @return 未读信息数
	 */
	@RequestMapping(value = "get_un_read_count", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getUnReadCount() {
		return new Value(yoomathCustomerServiceLogService.getUnreadCount());
	}

	/**
	 * 得到所有的会话信息，并且得到未读数
	 *
	 * @return {@link Value}
	 */
	@RequestMapping(value = "get_all_users", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getAllUsers() {
		List<YoomathCustomerServiceSession> list = yoomathCustomerServiceSessionService.findAll();
		return new Value(yoomathCustomerServiceSessionConvert.to(yoomathCustomerServiceSessionService.findAll()));
	}

	/**
	 * 更新对话信息状态
	 *
	 * @param userId
	 *            用户id
	 * @return {@link Value}
	 */
	@RequestMapping(value = "update_status", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updateStatus(@RequestParam(value = "userId") long userId, @RequestParam(value = "status") Status status) {
		yoomathCustomerServiceSessionService.update(userId, status);
		return new Value();
	}
}

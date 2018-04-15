package com.lanking.uxb.service.web.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoo.customerService.CustomerLogReadStatus;
import com.lanking.cloud.domain.yoo.customerService.YoomathCustomerServiceLog;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.value.VCursorPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.api.ZyYoomathCustomerService;
import com.lanking.uxb.service.zuoye.convert.ZyYoomathCustomerServiceLogConvert;
import com.lanking.uxb.service.zuoye.value.VYoomathCustomerServiceLog;

/**
 * 悠数学在线客服相关接口
 *
 * @author xinyu.zhou
 * @since yoomath V1.7
 */
@RestController
@RequestMapping(value = "zy/customer")
public class ZyCustomerController {
	@Autowired
	private ZyYoomathCustomerService yoomathCustomerService;
	@Autowired
	private ZyYoomathCustomerServiceLogConvert yoomathCustomerServiceLogConvert;

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
			@RequestParam(value = "status", required = false) CustomerLogReadStatus status) {

		CursorPageable<Long> cursorPageable = CP.cursor(cursor, size);
		CursorPage<Long, YoomathCustomerServiceLog> page = yoomathCustomerService.pull(cursorPageable,
				Security.getUserId(), status);

		List<YoomathCustomerServiceLog> logs = page.getItems();
		VCursorPage<VYoomathCustomerServiceLog> vPage = new VCursorPage<VYoomathCustomerServiceLog>();
		List<VYoomathCustomerServiceLog> vLogs = yoomathCustomerServiceLogConvert.to(logs);

		vPage.setItems(vLogs);
		vPage.setCursor(page.getNextCursor() == null ? 0 : page.getNextCursor());

		return new Value(vPage);
	}
}

package com.lanking.uxb.service.user.resource;

import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.yoo.common.PracticeHistory;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.value.VCursorPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.convert.PracticeHistoryConvert;
import com.lanking.uxb.service.user.value.VPracticeHistory;
import com.lanking.uxb.service.zuoye.api.PracticeHistoryService;

/**
 * 练习历史
 * 
 * @since yoomath(mobile) V1.1.0
 * @author wangsenhao
 *
 */
@ApiAllowed
@RestController
@RequestMapping("zy/m/s/history")
public class ZyMStuPracticeHistoryController {
	@Autowired
	private PracticeHistoryService practiceHistoryService;
	@Autowired
	private PracticeHistoryConvert practiceHistoryConvert;

	/**
	 * 查询联系历史
	 * 
	 * @param Biz
	 *            1:章节练习2:每日练3:智能试卷<br>
	 *            不传表示查询全部，不过滤
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "queryPracticeHistory", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryPracticeHistory(Integer type, @RequestParam(defaultValue = "0", required = false) long cursor,
			@RequestParam(defaultValue = "20", required = false) int count) {
		Date cursorDate = new Date(cursor);
		if (cursor == 0) {
			cursorDate = new Date();
		}
		CursorPage<Long, PracticeHistory> cp = practiceHistoryService
				.queryHistory(type, CP.cursor(cursor == 0 ? Long.MAX_VALUE : cursor, Math.min(count, 20)), cursorDate,
						Security.getUserId());
		VCursorPage<VPracticeHistory> vCursePage = new VCursorPage<VPracticeHistory>();
		if (cp.isNotEmpty()) {
			vCursePage.setCursor(cp.getItems().get(cp.getItems().size() - 1).getCreateAt().getTime());
			vCursePage.setItems(practiceHistoryConvert.to(cp.getItems()));
		} else {
			vCursePage.setCursor(cursor);
			vCursePage.setItems(Collections.EMPTY_LIST);
		}
		return new Value(vCursePage);
	}
}

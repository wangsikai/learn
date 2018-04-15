package com.lanking.uxb.rescon.book.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.rescon.book.api.ResconBookHistoryManage;
import com.lanking.uxb.rescon.book.convert.ResconBookHistoryConvert;

/**
 * 书本历史.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年10月21日
 */
@RestController
@RequestMapping("rescon/book/his")
@RolesAllowed(userTypes = { "VENDOR_ADMIN", "VENDOR_HEAD", "VENDOR_BUILD", "VENDOR_CHECK" })
public class ResconBookHistoryController {
	@Autowired
	private ResconBookHistoryConvert bookHistoryConvert;

	@Autowired
	private ResconBookHistoryManage bookHistoryManage;

	@RequestMapping(value = "list", method = RequestMethod.POST)
	public Value listHistory(Long bookId, Integer count) {
		return new Value(bookHistoryConvert.to(bookHistoryManage.listCountHistory(bookId, count)));
	}
}

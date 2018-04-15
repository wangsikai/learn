package com.lanking.uxb.zycon.operation.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.zycon.operation.api.ZycCorrectUserService;
import com.lanking.uxb.zycon.operation.convert.CorrectUserConvert;

/**
 * U数学 短信通知管理相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年10月14日 下午5:22:46
 */
@RestController
@RequestMapping("zyc/message")
public class MessageNoticeController {

	@Autowired
	private ZycCorrectUserService cuService;
	@Autowired
	private CorrectUserConvert cuConvert;

	@RequestMapping("list")
	public Value list() {
		return new Value(cuConvert.to(cuService.list()));
	}

	@RequestMapping("add")
	public Value add(String name, String tel) {
		return new Value(cuConvert.to(cuService.add(name, tel)));
	}

	@RequestMapping("editStatus")
	public Value changeVersionLog(Status status, Long id) {
		return new Value(cuConvert.to(cuService.edit(id, status)));
	}
}

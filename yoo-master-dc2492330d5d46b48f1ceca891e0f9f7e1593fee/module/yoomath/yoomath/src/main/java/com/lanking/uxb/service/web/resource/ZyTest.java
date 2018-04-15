package com.lanking.uxb.service.web.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkCorrectStatus;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.correct.api.CorrectProcessor;
import com.lanking.uxb.service.payment.alipay.api.AlipayService;
import com.lanking.uxb.service.payment.alipay.request.ToAccountTransferData;
import com.lanking.uxb.service.payment.alipay.response.ToAccountTransferResponse;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkService;

/**
 * 测试使用（请暂时不要删除）.
 * 
 * @author wanlong.che
 *
 */
@RestController
@RequestMapping("zy/test")
public class ZyTest {

	@Autowired
	private AlipayService alipayService;
	@Autowired
	private ZyStudentHomeworkService zyStuHkService;
	@Autowired
	private StudentHomeworkService stuHkService;
	@Autowired
	private CorrectProcessor correctProcessor;

	@RolesAllowed(anyone = true)
	@RequestMapping(value = "t1", method = { RequestMethod.POST, RequestMethod.GET })
	public Value t1() {
		ToAccountTransferData data = new ToAccountTransferData();
		data.setAmount("0.01");
		data.setOut_biz_no("CS20180322001");
		data.setPayee_account("cwlopq@163.com");
		data.setPayee_real_name("车万龙");
		data.setPayee_type("ALIPAY_LOGONID");
		data.setPayer_show_name("Elanking");
		data.setRemark("转账测试");
		try {
			ToAccountTransferResponse response = alipayService.toTransfer(data);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new Value();
	}

	/**
	 * 处理因异常处于批改中或者未批改的作业.
	 * 
	 * @since 小优快批，2018-4-3
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "t2", method = { RequestMethod.POST, RequestMethod.GET })
	public Value t2() {
		CursorPage<Long, StudentHomework> page = zyStuHkService
				.queryNotCompleteStudentHomework(CP.cursor(Long.MAX_VALUE, 50));
		while (page.isNotEmpty()) {
			correctProcessor.notCompleteStudentHomeworkHandle(page.getItems());
			page = zyStuHkService.queryNotCompleteStudentHomework(CP.cursor(page.getLast().getId(), 50));
		}
		return new Value();
	}

	/**
	 * 处理因异常处于批改中或者未批改的作业.
	 * 
	 * @since 小优快批，2018-4-3
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "t3", method = { RequestMethod.POST, RequestMethod.GET })
	public Value t3(Long studentHomeworkId) {
		if (studentHomeworkId == null) {
			return new Value(new IllegalArgException());
		}
		StudentHomework studentHomework = stuHkService.get(studentHomeworkId);
		if (studentHomework.getCorrectStatus() == null
				|| studentHomework.getCorrectStatus() == StudentHomeworkCorrectStatus.DEFAULT
				|| studentHomework.getCorrectStatus() == StudentHomeworkCorrectStatus.AUTO_CORRECTING) {
			correctProcessor.notCompleteStudentHomeworkHandle(Lists.newArrayList(studentHomework));
		}
		return new Value();
	}
}

package com.lanking.uxb.service.user.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.StudentService;

/**
 * 学生个人资料设置
 * 
 * @since yoomath V2.3.0
 * @author zemin.song
 * @version 2016年12月13日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/s/p")
public class ZyStuProfileController {

	@Autowired
	private StudentService studentService;
	@Autowired
	private TextbookService textbookService;

	/**
	 * 设置学生教材及入学年份
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "uptTextbook", method = { RequestMethod.POST, RequestMethod.GET })
	public Value uptTextbook(Integer textbookCode, Integer enterYear) {
		if (textbookCode == null) {
			return new Value(new MissingArgumentException());
		}
		Textbook tb = textbookService.get(textbookCode);
		if (tb != null && tb.getYoomathStatus() == Status.ENABLED) {
			studentService.setTextbook(Security.getUserId(), tb.getPhaseCode(), tb.getCategoryCode(), textbookCode);
		}
		if (enterYear != null) {
			studentService.setYear(Security.getUserId(), enterYear);
		}
		return new Value();
	}
}

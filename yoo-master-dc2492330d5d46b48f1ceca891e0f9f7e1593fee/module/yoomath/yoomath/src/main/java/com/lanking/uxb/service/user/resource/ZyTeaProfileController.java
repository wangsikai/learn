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
import com.lanking.uxb.service.user.api.TeacherService;

/**
 * 教师个人资料设置
 * 
 * @since yoomath V1.7
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年11月9日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/t/p")
public class ZyTeaProfileController {

	@Autowired
	private TeacherService teacherService;
	@Autowired
	private TextbookService textbookService;

	/**
	 * 设置教材
	 * 
	 * @since yoomath V1.7
	 * @param textbookCode
	 *            教材代码
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "uptTextbook", method = { RequestMethod.POST, RequestMethod.GET })
	public Value uptTextbook(Integer textbookCode) {
		if (textbookCode == null) {
			return new Value(new MissingArgumentException());
		}
		Textbook tb = textbookService.get(textbookCode);
		if (tb != null && tb.getYoomathStatus() == Status.ENABLED) {
			teacherService.updateCategory(Security.getUserId(), tb.getCategoryCode(), textbookCode);
		}
		return new Value();
	}
}

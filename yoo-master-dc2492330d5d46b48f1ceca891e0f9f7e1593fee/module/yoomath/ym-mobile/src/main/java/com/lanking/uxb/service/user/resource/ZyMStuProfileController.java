package com.lanking.uxb.service.user.resource;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.PhaseConvert;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.StudentService;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.user.form.EditProfileForm;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;

/**
 * 悠数学移动端(个人资料相关接口)
 * 
 * @since yoomath(mobile) V1.0.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年11月9日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/m/s/p")
public class ZyMStuProfileController extends ZyMBaseProfileController {

	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private ZyHomeworkStudentClazzService hkStuClazzService;
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private PhaseConvert phaseConvert;
	@Autowired
	private TextbookCategoryService tbCateService;
	@Autowired
	private TextbookCategoryConvert tbCateConvert;
	@Autowired
	private TextbookService tbService;
	@Autowired
	private TextbookConvert tbConvert;
	@Autowired
	private StudentService studentService;
	@Autowired
	private MqSender mqSender;

	/**
	 * 设置版本教材信息
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param textbookCode
	 *            教材代码
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "chooseTextbook", method = { RequestMethod.POST, RequestMethod.GET })
	public Value chooseTextbook(Integer textbookCode) {
		if (textbookCode == null) {
			return new Value(new IllegalArgException());
		}
		Textbook textbook = tbService.get(textbookCode);
		if (textbook == null) {
			return new Value(new IllegalArgException());
		}
		studentService.setTextbook(Security.getUserId(), textbook.getPhaseCode(), textbook.getCategoryCode(),
				textbook.getCode());
		return new Value();
	}

	/**
	 * 补充信息设置教材，入学年份
	 *
	 * @param textbookCode
	 *            教材code
	 * @param year
	 *            入学年份
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "fillInformation", method = { RequestMethod.GET, RequestMethod.POST })
	public Value fillInformation(int textbookCode, int year) {
		if (textbookCode <= 0 || year <= 0 || year > Calendar.getInstance().get(Calendar.YEAR)) {
			return new Value(new IllegalArgException());
		}
		Textbook textbook = tbService.get(textbookCode);
		if (null == textbook) {
			return new Value(new IllegalArgException());
		}

		studentService.setTextbook(Security.getUserId(), textbook.getPhaseCode(), textbook.getCategoryCode(),
				textbook.getCode());

		EditProfileForm form = new EditProfileForm();
		form.setId(Security.getUserId());
		form.setEnterYear(year);

		studentService.updateStudent(form);

		JSONObject messageObj = new JSONObject();
		messageObj.put("taskCode", 101000001);
		messageObj.put("userId", Security.getUserId());
		Map<String, Object> params = new HashMap<String, Object>(1);
		params.put("item", "year");
		messageObj.put("params", params);
		mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
				MQ.builder().data(messageObj).build());

		return new Value();
	}

	/**
	 * 学生更新入学年份
	 *
	 * @param year
	 *            入学年份
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "updateYear", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updateYear(Integer year) {
		if (year == null || year <= 0 || year > Calendar.getInstance().get(Calendar.YEAR)) {
			return new Value(new IllegalArgException());
		}

		try {

			EditProfileForm form = new EditProfileForm();
			form.setId(Security.getUserId());
			form.setEnterYear(year);

			studentService.updateStudent(form);

			JSONObject messageObj = new JSONObject();
			messageObj.put("taskCode", 101000001);
			messageObj.put("userId", Security.getUserId());
			Map<String, Object> params = new HashMap<String, Object>(1);
			params.put("item", "year");
			messageObj.put("params", params);
			mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
					MQ.builder().data(messageObj).build());

		} catch (AbstractException e) {
			return new Value(e);
		}
		return new Value();
	}

}

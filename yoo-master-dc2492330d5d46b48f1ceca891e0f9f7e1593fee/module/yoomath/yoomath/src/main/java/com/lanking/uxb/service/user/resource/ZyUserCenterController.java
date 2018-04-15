package com.lanking.uxb.service.user.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.StudentService;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.convert.UserProfileConvert;
import com.lanking.uxb.service.user.form.EditProfileForm;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;

import httl.util.StringUtils;

/**
 * 个人中心相关接口
 * 
 * @since yoomath V1.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月11日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/ucenter")
public class ZyUserCenterController {
	@Autowired
	private UserProfileConvert userProfileConvert;
	@Autowired
	private TextbookCategoryService textbookCategoryService;
	@Autowired
	private TextbookCategoryConvert textbookCategoryConvert;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private ZyHomeworkClassService homeworkClassService;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private AccountService accountService;
	@Autowired
	private MqSender mqSender;

	/**
	 * 获取用户中心数据
	 * 
	 * @return {@link Value}
	 */
	@RequestMapping(value = "index", method = { RequestMethod.POST, RequestMethod.GET })
	public Value index() {
		return new Value(userProfileConvert.get(Security.getUserId()));
	}

	/**
	 * 获取用户可选教材列表
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "textBookCategoryList", method = { RequestMethod.POST, RequestMethod.GET })
	public Value textBookCategoryList() {
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		return new Value(
				textbookCategoryConvert.to(textbookCategoryService.find(Product.YOOMATH, teacher.getPhaseCode())));
	}

	/**
	 * 更改用户资料
	 * 
	 * @param fm
	 *            信息form
	 * @return
	 */
	@MemberAllowed
	@RequestMapping(value = "uptProfileInfo", method = RequestMethod.POST)
	public Value uptProfileInfo(EditProfileForm fm) {
		Value value = new Value();
		VUser vuser = userConvert.get(Security.getUserId());
		// 用户名修改
		if (StringUtils.isNotBlank(fm.getAccountName())) {
			Account account = accountService.getAccount(Security.getAccountId());
			// 判断帐号可修改
			if (!account.getName().equalsIgnoreCase(fm.getAccountName())
					&& account.getNameUpdateStatus() == Status.ENABLED
					&& null == accountService.getAccount(GetType.NAME, fm.getAccountName())) {
				accountService.updateName(Security.getAccountId(), fm.getAccountName());
			}
		}
		if (fm.getType() == UserType.TEACHER) {
			fm.setId(Security.getUserId());
			try {
				Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
				MemberType memberType = SecurityContext.getMemberType();
				if (memberType == MemberType.SCHOOL_VIP && fm.getSchoolCode() != null
						&& !teacher.getSchoolId().equals(fm.getSchoolCode())) {
					return new Value(new ZuoyeException(ZuoyeException.ZUOYE_SCHOOLVIP_UPDATE_SCHOOL));
				}
				if (vuser.isChannelUser() && fm.getSchoolCode() != null
						&& !teacher.getSchoolId().equals(fm.getSchoolCode())) {
					return new Value(new ZuoyeException(ZuoyeException.ZUOYE_CHANNELUSER_UPDATE_SCHOOL));
				}
				teacherService.updateTeacher(fm);
				homeworkClassService.clearBookSetting(Security.getUserId());
			} catch (AbstractException e) {
				return new Value(e);
			}
		}
		if (fm.getType() == UserType.STUDENT) {
			fm.setId(Security.getUserId());
			try {
				Student student = (Student) studentService.getUser(UserType.STUDENT, Security.getUserId());
				if (vuser.isChannelUser() && fm.getSchoolCode() != null
						&& !student.getSchoolId().equals(fm.getSchoolCode())) {
					return new Value(new ZuoyeException(ZuoyeException.ZUOYE_CHANNELUSER_UPDATE_SCHOOL));
				}
				studentService.updateStudent(fm);
			} catch (AbstractException e) {
				return new Value(e);
			}
		}
		JSONObject messageObj = new JSONObject();
		messageObj.put("taskCode", 101000001);
		messageObj.put("userId", Security.getUserId());
		messageObj.put("isClient", Security.isClient());
		mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
				MQ.builder().data(messageObj).build());
		return value;
	}
}

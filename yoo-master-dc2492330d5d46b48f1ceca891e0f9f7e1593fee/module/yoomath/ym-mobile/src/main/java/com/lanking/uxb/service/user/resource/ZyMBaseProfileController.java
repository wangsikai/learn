package com.lanking.uxb.service.user.resource;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.MappedSuperclass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.Sex;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.account.util.ValidateUtils;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.StudentService;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.form.EditProfileForm;
import com.lanking.uxb.service.user.value.VUser;

/**
 * 个人中心相关设置公共接口
 * 
 * @since yoomath(mobile) V1.0.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年11月20日
 */
@MappedSuperclass
public class ZyMBaseProfileController {

	@Qualifier("accountService")
	@Autowired
	private AccountService accountService;
	@Qualifier("userService")
	@Autowired
	private UserService userService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private MqSender mqSender;

	/**
	 * 设置头像
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param avatar
	 *            文件ID
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "TEACHER", "STUDENT" })
	@RequestMapping(value = "updateAvatar", method = { RequestMethod.POST, RequestMethod.GET })
	public Value updateAvatar(long avatar) {
		User user = accountService.getUserByUserId(Security.getUserId());
		userService.updateAvatar(user.getUserType(), user.getId(), avatar);
		return new Value();
	}

	/**
	 * 更新真实姓名
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param name
	 *            正式姓名
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "M")
	@RolesAllowed(userTypes = { "TEACHER", "STUDENT" })
	@RequestMapping(value = "updateName", method = { RequestMethod.POST, RequestMethod.GET })
	public Value updateName(String name) {
		if (StringUtils.isBlank(name)) {
			return new Value(new IllegalArgException());
		}
		try {
			if (Security.getUserType() == UserType.STUDENT) {
				EditProfileForm ef = new EditProfileForm();
				ef.setId(Security.getUserId());
				ef.setName(name);

				// 用户任务
				JSONObject messageObj = new JSONObject();
				messageObj.put("taskCode", 101000001);
				messageObj.put("userId", Security.getUserId());
				Map<String, Object> params = new HashMap<String, Object>(1);
				params.put("item", "name");
				messageObj.put("params", params);
				messageObj.put("isClient", Security.isClient());
				mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
						MQ.builder().data(messageObj).build());

				studentService.updateStudent(ef);
			} else if (Security.getUserType() == UserType.TEACHER) {
				EditProfileForm ef = new EditProfileForm();
				ef.setId(Security.getUserId());
				ef.setName(name);
				teacherService.updateTeacher(ef);
			}
			return new Value();
		} catch (AbstractException e) {
			return new Value(e);
		}

	}

	/**
	 * 更新性别
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param sex
	 *            男女
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "TEACHER", "STUDENT" })
	@RequestMapping(value = "updateSex", method = { RequestMethod.POST, RequestMethod.GET })
	public Value updateSex(Sex sex) {
		if (sex == null) {
			return new Value(new IllegalArgException());
		}
		try {
			if (Security.getUserType() == UserType.STUDENT) {
				EditProfileForm ef = new EditProfileForm();
				ef.setId(Security.getUserId());
				ef.setSex(sex);
				studentService.updateStudent(ef);

				JSONObject messageObj = new JSONObject();
				messageObj.put("taskCode", 101000001);
				messageObj.put("userId", Security.getUserId());
				Map<String, Object> params = new HashMap<String, Object>(1);
				params.put("item", "sex");
				messageObj.put("params", params);
				messageObj.put("isClient", Security.isClient());
				mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
						MQ.builder().data(messageObj).build());

			} else if (Security.getUserType() == UserType.TEACHER) {
				EditProfileForm ef = new EditProfileForm();
				ef.setId(Security.getUserId());
				ef.setSex(sex);
				teacherService.updateTeacher(ef);
			}
		} catch (AbstractException e) {
			return new Value(e);
		}

		return new Value();
	}

	/**
	 * 更新生日
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param birthDay
	 *            生日(yyyy-MM-dd)
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "TEACHER", "STUDENT" })
	@RequestMapping(value = "updateBirthDay", method = { RequestMethod.POST, RequestMethod.GET })
	public Value updateBirthDay(String birthDay) {
		if (StringUtils.isBlank(birthDay)) {
			return new Value(new IllegalArgException());
		}
		try {
			if (Security.getUserType() == UserType.STUDENT) {
				EditProfileForm ef = new EditProfileForm();
				ef.setId(Security.getUserId());
				ef.setBirthDay(birthDay);
				studentService.updateStudent(ef);

				JSONObject messageObj = new JSONObject();
				messageObj.put("taskCode", 101000001);
				messageObj.put("userId", Security.getUserId());
				Map<String, Object> params = new HashMap<String, Object>(1);
				params.put("item", "birthday");
				messageObj.put("params", params);
				messageObj.put("isClient", Security.isClient());
				mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
						MQ.builder().data(messageObj).build());

			} else if (Security.getUserType() == UserType.TEACHER) {
				EditProfileForm ef = new EditProfileForm();
				ef.setId(Security.getUserId());
				ef.setBirthDay(birthDay);
				teacherService.updateTeacher(ef);
			}
		} catch (AbstractException e) {
			return new Value(e);
		}

		return new Value();
	}

	/**
	 * 更新学校
	 * 
	 * @since 2.0.3
	 * @param id
	 *            学校ID
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@MemberAllowed
	@RolesAllowed(userTypes = { "TEACHER", "STUDENT" })
	@RequestMapping(value = "updateSchool", method = { RequestMethod.POST, RequestMethod.GET })
	public Value updateSchool(Long id) {
		if (id == null) {
			return new Value(new IllegalArgException());
		}
		if (SecurityContext.getMemberType() == MemberType.SCHOOL_VIP) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_SCHOOLVIP_UPDATE_SCHOOL));
		}
		VUser vuser = userConvert.get(Security.getUserId());
		if (vuser.isChannelUser() && !id.equals(Security.getUserId())) {
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_USERCHANNEL_UPDATESCHOOL));
		}
		School school = null;
		if (id != -1) {
			school = schoolService.get(id);
			if (school == null) {
				return new Value(new IllegalArgException());
			}
		}
		try {
			if (Security.getUserType() == UserType.STUDENT) {
				EditProfileForm ef = new EditProfileForm();
				ef.setId(Security.getUserId());
				ef.setSchoolCode(id);
				ef.setSchoolName(school == null ? StringUtils.EMPTY : school.getName());
				studentService.updateStudent(ef);

				JSONObject messageObj = new JSONObject();
				messageObj.put("taskCode", 101000001);
				messageObj.put("userId", Security.getUserId());
				Map<String, Object> params = new HashMap<String, Object>(1);
				params.put("item", "school");
				messageObj.put("params", params);
				messageObj.put("isClient", Security.isClient());
				mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
						MQ.builder().data(messageObj).build());

			} else if (Security.getUserType() == UserType.TEACHER) {
				EditProfileForm ef = new EditProfileForm();
				ef.setId(Security.getUserId());
				ef.setSchoolCode(id);
				ef.setSchoolName(school == null ? StringUtils.EMPTY : school.getName());
				teacherService.updateTeacher(ef);
			}
		} catch (AbstractException e) {
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 用户更新用户名
	 *
	 * @param name
	 *            新的用户名
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "TEACHER", "STUDENT" })
	@RequestMapping(value = "updateAccountName", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updateAccountName(String name) {
		try {
			ValidateUtils.validateName(name);
		} catch (AccountException e) {
			return new Value(e);
		}

		Account a = accountService.getAccountByUserId(Security.getUserId());
		// 之前已经修改过用户名
		if (a.getNameUpdateStatus() != Status.ENABLED) {
			// 只能修改一次用户名
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_NAME_MODIFIED_ONCE));
		}

		// 用户名重复
		Account findAccount = accountService.getAccount(GetType.NAME, name);
		if (findAccount != null) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_NAME_EXIST));
		}

		accountService.updateName(a.getId(), name);

		return new Value();
	}
}

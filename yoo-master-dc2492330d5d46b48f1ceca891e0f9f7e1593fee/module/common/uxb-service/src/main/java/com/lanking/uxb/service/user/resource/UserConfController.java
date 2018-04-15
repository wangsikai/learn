package com.lanking.uxb.service.user.resource;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.baseData.Duty;
import com.lanking.cloud.domain.common.baseData.Title;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.DutyService;
import com.lanking.uxb.service.code.api.TitleService;
import com.lanking.uxb.service.code.convert.DutyConvert;
import com.lanking.uxb.service.code.convert.TitleConvert;
import com.lanking.uxb.service.search.api.IndexService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.thirdparty.api.ThirdpartyService;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.StudentService;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.user.ex.UserException;
import com.lanking.uxb.service.user.form.EditProfileForm;
import com.lanking.uxb.service.user.value.VTEditInfo;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月9日
 *
 */
@RestController
@RequestMapping("user/conf")
public class UserConfController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier("userService")
	private UserService userService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private IndexService indexService;
	@Autowired
	private TitleService titleService;
	@Autowired
	private DutyService dutyService;
	@Autowired
	private TitleConvert titleConvert;
	@Autowired
	private DutyConvert dutyConvert;
	@Autowired
	private StudentService studentService;
	@Autowired
	private ThirdpartyService thirdpartyService;

	@RequestMapping(value = "upt_avatar", method = RequestMethod.POST)
	public Value configAvatar(@RequestParam(value = "avatar") long avatar) {
		User user = accountService.getUserByUserId(Security.getUserId());
		userService.updateAvatar(user.getUserType(), user.getId(), avatar);
		return new Value();
	}

	/**
	 * 处理第三方登录后的头像上传.
	 * 
	 * @param url
	 *            第三方头像的URL.
	 * @param upurl
	 *            上传服务地址URL.
	 * @param request
	 * @param response
	 * @return
	 */

	@RequestMapping(value = "upt_third_avatar", method = RequestMethod.POST)
	public Value configThirdAvatar(String url, String upurl, HttpServletRequest request, HttpServletResponse response) {
		User user = accountService.getUserByUserId(Security.getUserId());
		try {
			long avatar = thirdpartyService.loadThirdImage(url, upurl, request);
			if (avatar > 0) {
				userService.updateAvatar(user.getUserType(), user.getId(), avatar);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return new Value();
	}

	/**
	 * 设置阶段和学科（只能设置一次）
	 * 
	 * @since 2.1
	 * @param phaseCode
	 *            阶段代码
	 * @param subjectCode
	 *            学科代码
	 * @return {@link Value}
	 */

	@RequestMapping(value = "upt_phase_subject", method = { RequestMethod.GET, RequestMethod.POST })
	public Value uptPhaseSubject(int phaseCode, int subjectCode) {
		try {
			teacherService.setPhaseSubject(Security.getUserId(), phaseCode, subjectCode);
			return new Value();
		} catch (UserException e) {
			return new Value(e);
		}
	}

	/**
	 * 更改个人资料
	 * 
	 * @since 2.1
	 * @param fm
	 *            个人资料 form
	 * @return {@link Value}
	 */
	@RequestMapping(value = "uptProfileInfo", method = RequestMethod.POST)
	public Value uptProfileInfo(EditProfileForm fm) {
		Value value = new Value();
		if (fm.getType() == UserType.TEACHER) {
			fm.setId(Security.getUserId());
			try {
				teacherService.updateTeacher(fm);
			} catch (AbstractException e) {
				return new Value(e);
			}
		}
		if (fm.getType() == UserType.STUDENT) {
			fm.setId(Security.getUserId());
			try {
				studentService.updateStudent(fm);
			} catch (AbstractException e) {
				return new Value(e);
			}
		}
		return value;
	}

	/**
	 * 获取初始化编辑页面所需要的数据
	 * 
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "getUptProfileInfo", method = RequestMethod.POST)
	public Value getUptProfileInfo() {
		Value value = new Value();
		VTEditInfo vt = new VTEditInfo();
		List<Title> titleList = titleService.getAll();
		List<Duty> dutyList = dutyService.getAll();
		vt.setDutyList(dutyConvert.to(dutyList));
		vt.setTitleList(titleConvert.to(titleList));
		value.setRet(vt);
		return value;
	}
}

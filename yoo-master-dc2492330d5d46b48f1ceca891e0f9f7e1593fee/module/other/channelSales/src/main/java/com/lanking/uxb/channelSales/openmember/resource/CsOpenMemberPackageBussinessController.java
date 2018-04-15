package com.lanking.uxb.channelSales.openmember.resource;

import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.channelSales.base.api.CsHomeworkClassService;
import com.lanking.uxb.channelSales.base.convert.CsHomeworkClassConvert;
import com.lanking.uxb.channelSales.base.form.HomeworkClazzForm;
import com.lanking.uxb.channelSales.base.value.VCsHomeworkClass;
import com.lanking.uxb.channelSales.channel.api.CsChannelSchoolService;
import com.lanking.uxb.channelSales.channel.api.CsUserChannelService;
import com.lanking.uxb.channelSales.channel.convert.CsSchoolConvert;
import com.lanking.uxb.channelSales.channel.form.ChannelSchoolQueryForm;
import com.lanking.uxb.channelSales.channel.value.VSchool;
import com.lanking.uxb.channelSales.common.ex.ChannelSalesConsoleException;
import com.lanking.uxb.channelSales.openmember.api.CsMemberPackageOrderService;
import com.lanking.uxb.channelSales.openmember.api.CsStudentService;
import com.lanking.uxb.channelSales.openmember.api.CsTeacherService;
import com.lanking.uxb.channelSales.openmember.convert.CsStudentConvert;
import com.lanking.uxb.channelSales.openmember.convert.CsTeacherConvert;
import com.lanking.uxb.channelSales.openmember.form.OpenMemberPackageForm;
import com.lanking.uxb.channelSales.openmember.form.UserQueryForm;
import com.lanking.uxb.channelSales.openmember.value.VCsUser;
import com.lanking.uxb.service.adminSecurity.support.ConsoleRolesAllowed;
import com.lanking.uxb.service.session.api.impl.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 渠道商开通会员管理相关处理
 *
 * @author xinyu.zhou
 * @since 3.9.2
 */
@RestController
@RequestMapping(value = "channelSales/op/bu")
public class CsOpenMemberPackageBussinessController {
	@Autowired
	private CsHomeworkClassService homeworkClassService;
	@Autowired
	private CsHomeworkClassConvert homeworkClassConvert;
	@Autowired
	private CsUserChannelService userChannelService;
	@Autowired
	private CsMemberPackageOrderService memberPackageOrderService;
	@Autowired
	private CsTeacherService teacherService;
	@Autowired
	private CsTeacherConvert teacherConvert;
	@Autowired
	private CsStudentService studentService;
	@Autowired
	private CsStudentConvert studentConvert;
	@Autowired
	private CsChannelSchoolService channelSchoolService;
	@Autowired
	private CsSchoolConvert schoolConvert;

	/**
	 * 通过Excel导入数据
	 *
	 * @param request
	 *            {@link HttpServletRequest}
	 * @param userType
	 *            用户类型
	 * @param memberType
	 *            会员类型
	 * @return {@link Value}
	 */
	@ConsoleRolesAllowed(roleCodes = { "CSBUSINESS" })
	@RequestMapping(value = "importMemberUser", method = {RequestMethod.GET, RequestMethod.POST })
	public Value importMemberUser(HttpServletRequest request, UserType userType,
	        MemberType memberType) {
		if (userType == null || memberType == null) {
			return new Value(new IllegalArgException());
		}
		UserChannel userChannel = userChannelService.getChannelByUser(Security.getUserId());

		if (userChannel == null) {
			return new Value(new IllegalArgException());
		}

		try {
			List<Long> users = memberPackageOrderService.importMemberUser(request,
			        userChannel.getCode(), userType, memberType);

			if (CollectionUtils.isEmpty(users)) {
				return new Value(new IllegalArgException());
			}

			switch (userType) {
				case TEACHER:
					List<Teacher> teachers = teacherService.mgetList(users);
					return new Value(teacherConvert.to(teachers));
				case STUDENT:
					List<Student> students = studentService.mgetList(users);
					return new Value(studentConvert.to(students));
			}
		} catch (ChannelSalesConsoleException e) {
			return new Value(e);
		}

		return new Value();
	}

	/**
	 * 查询教师或者学生列表
	 *
	 * @param userQueryForm
	 *            {@link UserQueryForm}
	 * @return {@link Value}
	 */
	@ConsoleRolesAllowed(roleCodes = { "CSBUSINESS" })
	@RequestMapping(value = "queryTeacherStudent", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryTeacherStudent(UserQueryForm userQueryForm) {
		VPage<VCsUser> retPage = new VPage<VCsUser>();
		UserChannel userChannel = userChannelService.getChannelByUser(Security.getUserId());
		if (userChannel == null) {
			return new Value(new IllegalArgException());
		}
		userQueryForm.setChannelName(userChannel.getName());
		if (userQueryForm.getUserType() == UserType.STUDENT) {
			Page<Student> students = studentService.query(userQueryForm);

			List<VCsUser> users = studentConvert.to(students.getItems());

			retPage.setCurrentPage(userQueryForm.getPage());
			retPage.setItems(users);
			retPage.setPageSize(userQueryForm.getSize());
			retPage.setTotal(students.getTotalCount());
			retPage.setTotalPage(students.getPageCount());
		} else {
			Page<Teacher> teachers = teacherService.queryTeacher(userQueryForm);

			List<VCsUser> users = teacherConvert.to(teachers.getItems());

			retPage.setCurrentPage(userQueryForm.getPage());
			retPage.setItems(users);
			retPage.setPageSize(userQueryForm.getSize());
			retPage.setTotal(teachers.getTotalCount());
			retPage.setTotalPage(teachers.getPageCount());
		}
		return new Value(retPage);
	}

	@ConsoleRolesAllowed(roleCodes = { "CSBUSINESS" })
	@RequestMapping(value = "querySchool", method = { RequestMethod.GET, RequestMethod.POST })
	public Value querySchool(ChannelSchoolQueryForm form) {
		UserChannel userChannel = userChannelService.getChannelByUser(Security.getUserId());

		if (userChannel == null) {
			return new Value(new IllegalArgException());
		}

		form.setChannelCode(String.valueOf(userChannel.getCode()));
		Page<School> page = channelSchoolService.querySchool(form);
		VPage<VSchool> retPage = new VPage<VSchool>();

		List<VSchool> vs = schoolConvert.to(page.getItems());

		retPage.setItems(vs);
		retPage.setCurrentPage(form.getPage());
		retPage.setPageSize(form.getSize());
		retPage.setTotal(page.getTotalCount());
		retPage.setTotalPage(page.getPageCount());

		return new Value(retPage);
	}

	/**
	 * 分页查询班级数据
	 *
	 * @param page
	 *            当前页
	 * @param size
	 *            每页大小
	 * @param form
	 *            {@link HomeworkClazzForm}
	 * @return {@link Value}
	 */
	@ConsoleRolesAllowed(roleCodes = { "CSBUSINESS" })
	@RequestMapping(value = "queryClass", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryClass(@RequestParam(value = "page", defaultValue = "1") int page,
							@RequestParam(value = "size", defaultValue = "20") int size, HomeworkClazzForm form) {
		UserChannel userChannel = userChannelService.getChannelByUser(Security.getUserId());

		if (userChannel == null) {
			return new Value(new IllegalArgException());
		}

		form.setChannelCode(userChannel.getCode());

		Pageable pageable = P.index(page, size);
		Page<HomeworkClazz> queryPage = homeworkClassService.query(form, pageable);

		List<VCsHomeworkClass> vs = homeworkClassConvert.to(queryPage.getItems());
		VPage<VCsHomeworkClass> retPage = new VPage<VCsHomeworkClass>();
		retPage.setTotalPage(queryPage.getPageCount());
		retPage.setTotal(queryPage.getTotalCount());
		retPage.setPageSize(size);
		retPage.setCurrentPage(page);
		retPage.setItems(vs);

		return new Value(retPage);
	}

	/**
	 * 开通会员套餐
	 *
	 * @param form
	 *            {@link OpenMemberPackageForm}
	 * @return {@link Value}
	 */
	@ConsoleRolesAllowed(roleCodes = { "CSBUSINESS" })
	@RequestMapping(value = "save", method = { RequestMethod.GET, RequestMethod.POST })
	public Value save(OpenMemberPackageForm form) {
		UserChannel userChannel = userChannelService.getChannelByUser(Security.getUserId());
		if (userChannel == null) {
			return new Value(new IllegalArgException());
		}

		try {
			form.setChannelCode(userChannel.getCode());
			memberPackageOrderService.create(form, Security.getUserId());
		} catch (AbstractException e) {
			return new Value(e);
		}

		return new Value();
	}
}

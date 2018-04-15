package com.lanking.uxb.channelSales.openmember.resource;

import java.util.Collections;
import java.util.List;

import com.lanking.uxb.channelSales.base.api.CsHomeworkClassService;
import com.lanking.uxb.channelSales.base.convert.CsHomeworkClassConvert;
import com.lanking.uxb.channelSales.base.form.HomeworkClazzForm;
import com.lanking.uxb.channelSales.base.value.VCsHomeworkClass;
import com.lanking.uxb.channelSales.channel.api.CsUserChannelService;
import com.lanking.uxb.channelSales.common.ex.ChannelSalesConsoleException;
import com.lanking.uxb.service.adminSecurity.support.ConsoleRolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.yoo.channel.ChannelSchool;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.domain.yoo.member.MemberPackage;
import com.lanking.cloud.domain.yoo.member.MemberPackageGroupType;
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
import com.lanking.uxb.channelSales.channel.api.CsChannelSchoolService;
import com.lanking.uxb.channelSales.channel.convert.CsChannelSchoolConvert;
import com.lanking.uxb.channelSales.channel.convert.CsSchoolConvert;
import com.lanking.uxb.channelSales.channel.form.ChannelSchoolQueryForm;
import com.lanking.uxb.channelSales.channel.value.VChannelSchool;
import com.lanking.uxb.channelSales.channel.value.VSchool;
import com.lanking.uxb.channelSales.memberPackage.api.CsMemberPackageService;
import com.lanking.uxb.channelSales.memberPackage.convert.CsMemberPackageConvert;
import com.lanking.uxb.channelSales.openmember.api.CsMemberPackageOrderService;
import com.lanking.uxb.channelSales.openmember.api.CsStudentService;
import com.lanking.uxb.channelSales.openmember.api.CsTeacherService;
import com.lanking.uxb.channelSales.openmember.convert.CsStudentConvert;
import com.lanking.uxb.channelSales.openmember.convert.CsTeacherConvert;
import com.lanking.uxb.channelSales.openmember.form.OpenMemberPackageForm;
import com.lanking.uxb.channelSales.openmember.form.UserQueryForm;
import com.lanking.uxb.channelSales.openmember.value.VCsUser;
import com.lanking.uxb.service.session.api.impl.Security;

import javax.servlet.http.HttpServletRequest;

/**
 * 开通会员套餐
 *
 * @author xinyu.zhou
 * @since 2.5.0
 */
@RestController
@RequestMapping(value = "channelSales/open/mp")
public class CsOpenMemberPackageController {

	@Autowired
	private CsChannelSchoolService channelSchoolService;
	@Autowired
	private CsChannelSchoolConvert channelSchoolConvert;
	@Autowired
	private CsMemberPackageService memberPackageService;
	@Autowired
	private CsMemberPackageConvert memberPackageConvert;
	@Autowired
	private CsStudentService studentService;
	@Autowired
	private CsTeacherService teacherService;
	@Autowired
	private CsStudentConvert studentConvert;
	@Autowired
	private CsTeacherConvert teacherConvert;
	@Autowired
	private CsMemberPackageOrderService packageOrderService;
	@Autowired
	private CsSchoolConvert schoolConvert;
	@Autowired
	private CsHomeworkClassService homeworkClassService;
	@Autowired
	private CsHomeworkClassConvert homeworkClassConvert;
	@Autowired
	private CsUserChannelService userChannelService;

	/**
	 * 查询所有的套餐情况
	 *
	 * @return {@link Value}
	 */
	@ConsoleRolesAllowed(roleCodes = { "CSADMIN", "CSBUSINESS" })
	@RequestMapping(value = "findAll", method = { RequestMethod.GET, RequestMethod.POST })
	public Value findAll() {
		List<MemberPackage> packages = memberPackageService.findAll();

		return new Value(memberPackageConvert.to(packages));
	}

	/**
	 * 根据条件查询套餐
	 *
	 * @param userType
	 *            用户类型
	 * @param memberType
	 *            会员类型
	 * @param schoolId
	 *            学校id
	 * @param groupType
	 *            会员套餐组类型
	 * @return {@link Value}
	 */
	@RequestMapping(value = "findPackage", method = { RequestMethod.GET, RequestMethod.POST })
	@ConsoleRolesAllowed(roleCodes = { "CSADMIN", "CSBUSINESS"})
	public Value findPackage(UserType userType, MemberType memberType, Long schoolId,
	        MemberPackageGroupType groupType, Integer channelCode) {
		UserChannel userChannel = userChannelService.getChannelByUser(Security.getUserId());
		if (userChannel != null) {
			channelCode = userChannel.getCode();
		}
		if (schoolId != null) {
			userChannel = userChannelService.findBySchool(schoolId);
			channelCode = userChannel == null ? null : userChannel.getCode();
			List<MemberPackage> packages = memberPackageService.findPackage(userType, memberType,
					schoolId, channelCode, groupType);

			return new Value(memberPackageConvert.to(packages));
		} else {
			List<MemberPackage> packages = memberPackageService.findPackage(userType, memberType,
					null, channelCode, groupType);
			if (CollectionUtils.isEmpty(packages)) {
				packages = memberPackageService.findPackage(userType, memberType,
						null, null, groupType);
			}
			return new Value(memberPackageConvert.to(packages));
		}

	}

	/**
	 * 查询渠道商对应的学校
	 *
	 * @param form
	 *            {@link ChannelSchoolQueryForm}
	 * @return {@link Value}
	 */
	@ConsoleRolesAllowed(roleCodes = { "CSADMIN" })
	@RequestMapping(value = "queryChannelSchool", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryChannelSchool(ChannelSchoolQueryForm form) {
		Page<ChannelSchool> page = channelSchoolService.query(form);
		VPage<VChannelSchool> retPage = new VPage<VChannelSchool>();
		List<VChannelSchool> vs = channelSchoolConvert.to(page.getItems());

		retPage.setItems(vs);
		retPage.setCurrentPage(form.getPage());
		retPage.setPageSize(form.getSize());
		retPage.setTotal(page.getTotalCount());
		retPage.setTotalPage(page.getPageCount());

		return new Value(retPage);
	}

	@ConsoleRolesAllowed(roleCodes = { "CSADMIN" })
	@RequestMapping(value = "querySchool", method = { RequestMethod.GET, RequestMethod.POST })
	public Value querySchool(ChannelSchoolQueryForm form) {
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
	 * 查询教师或者学生列表
	 *
	 * @param userQueryForm
	 *            {@link UserQueryForm}
	 * @return {@link Value}
	 */
	@ConsoleRolesAllowed(roleCodes = { "CSADMIN" })
	@RequestMapping(value = "queryTeacherStudent", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryTeacherStudent(UserQueryForm userQueryForm) {
		VPage<VCsUser> retPage = new VPage<VCsUser>();
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

	/**
	 * 开通会员接口
	 *
	 * @param form
	 *            {@link OpenMemberPackageForm}
	 * @return {@link Value}
	 */
	@ConsoleRolesAllowed(roleCodes = { "CSADMIN" })
	@RequestMapping(value = "save", method = { RequestMethod.GET, RequestMethod.POST })
	public Value save(OpenMemberPackageForm form) {
		try {
			packageOrderService.create(form, Security.getUserId());
		} catch (AbstractException e) {
			return new Value(e);
		}

		return new Value();
	}

	/**
	 * 查找渠道商学校下的教师
	 *
	 * @param schoolId
	 *            学校id
	 * @return {@link Value}
	 */
	@ConsoleRolesAllowed(roleCodes = { "CSADMIN" })
	@RequestMapping(value = "findAllSchoolUser", method = { RequestMethod.GET, RequestMethod.POST })
	public Value findAllSchoolUser(long schoolId) {
		List<Teacher> teachers = teacherService.findChannelSchoolTeacher(schoolId);
		return new Value(teacherConvert.to(teachers));
	}

	/**
	 * 通过Excel导入会员信息
	 *
	 * @param request
	 *            {@link HttpServletRequest}
	 * @return {@link Value}
	 */
	@ConsoleRolesAllowed(roleCodes = { "CSADMIN" })
	@RequestMapping(value = "importMemberUser", method = { RequestMethod.GET, RequestMethod.POST })
	public Value importMemberUser(HttpServletRequest request, UserType userType,
	        MemberType memberType, Integer channelCode) {
		if (userType == null || memberType == null) {
			return new Value(new IllegalArgException());
		}
		try {
			List<Long> users = packageOrderService.importMemberUser(request,
			        channelCode == null || channelCode <= 0 ? null : channelCode, userType,
			        memberType);
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

	@ConsoleRolesAllowed(roleCodes = { "CSADMIN" })
	@RequestMapping(value = "queryClass", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryClass(@RequestParam(value = "size", defaultValue = "20") int size,
							@RequestParam(value = "page", defaultValue = "1") int page, HomeworkClazzForm form) {
		Pageable pageable = P.index(page, size);

		Page<HomeworkClazz> classPage = homeworkClassService.query(form, pageable);

		VPage<VCsHomeworkClass> retPage = new VPage<VCsHomeworkClass>();
		List<VCsHomeworkClass> vs = homeworkClassConvert.to(classPage.getItems());
		retPage.setItems(vs);
		retPage.setTotal(classPage.getTotalCount());
		retPage.setCurrentPage(page);
		retPage.setPageSize(size);
		retPage.setTotalPage(classPage.getPageCount());

		return new Value(retPage);
	}

	/**
	 * 根据用户类型及id列表查询用户数据
	 *
	 * @param ids
	 *            用户id列表
	 * @param userType
	 *            用户类型
	 * @return {@link Value}
	 */
	@ConsoleRolesAllowed(roleCodes = { "CSADMIN", "CSBUSINESS" })
	@RequestMapping(value = "getTeacherOrStudents", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getTeachersOrStudents(@RequestParam(value = "ids") List<Long> ids, UserType userType) {
		if (CollectionUtils.isEmpty(ids)) {
			return new Value(Collections.EMPTY_LIST);
		}

		if (null == userType) {
			return new Value(new IllegalArgException());
		}
		switch (userType) {
			case TEACHER:
				return new Value(teacherConvert.to(teacherService.mgetList(ids)));
			case STUDENT:
				return new Value(studentConvert.to(studentService.mgetList(ids)));
		}

		return new Value();
	}
}

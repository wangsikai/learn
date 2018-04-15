package com.lanking.uxb.service.user.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.common.baseData.Duty;
import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.common.baseData.TextbookCategory;
import com.lanking.cloud.domain.common.baseData.Title;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.IllegalArgFormatException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.code.api.DutyService;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.api.TitleService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.form.EditProfileForm;
import com.lanking.uxb.service.user.form.PerfectDataForm;
import com.lanking.uxb.service.user.value.VUser;

@ApiAllowed
@RestController
@RequestMapping("zy/m/t/p")
public class ZyMTeaProfileController extends ZyMBaseProfileController {

	@Autowired
	private TeacherService teacherService;
	@Autowired
	private TextbookService tbService;
	@Autowired
	private TitleService titleService;
	@Autowired
	private DutyService dutyService;
	@Autowired
	private TextbookCategoryService tbcService;
	@Autowired
	private UserConvert userConvert;

	/**
	 * 完善信息
	 * 
	 * @param textbookCode
	 *            教材代码
	 * @param name
	 *            真实姓名
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "M")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "fillProfile", method = { RequestMethod.POST, RequestMethod.GET })
	public Value fillProfile(Integer textbookCode, String realname) {
		if (textbookCode == null) {
			return new Value(new IllegalArgFormatException());
		}
		Textbook textbook = tbService.get(textbookCode);
		if (textbook == null) {
			return new Value(new IllegalArgFormatException());
		}
		EditProfileForm ef = new EditProfileForm();
		ef.setId(Security.getUserId());
		ef.setName(realname);
		teacherService.updateTeacher(ef);
		teacherService.updateCategory(Security.getUserId(), textbook.getCategoryCode(), textbookCode);
		return new Value();
	}

	/**
	 * 更新教师的工作时间
	 * 
	 * @since 2.0.3
	 * @param workAt
	 *            开始工作时间
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "updateWorkAt", method = { RequestMethod.POST, RequestMethod.GET })
	public Value updateWorkAt(String workAt) {
		if (StringUtils.isBlank(workAt)) {
			return new Value(new IllegalArgException());
		}
		try {
			EditProfileForm ef = new EditProfileForm();
			ef.setId(Security.getUserId());
			ef.setWorkAt(workAt);
			teacherService.updateTeacher(ef);
		} catch (AbstractException e) {
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 更新教师职称
	 * 
	 * @since 2.0.3
	 * @param code
	 *            职称代码
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "updateTitle", method = { RequestMethod.POST, RequestMethod.GET })
	public Value updateTitle(Integer code) {
		if (code == null || code <= 0) {
			return new Value(new IllegalArgException());
		}
		Title title = titleService.getTitle(code);
		if (title == null) {
			return new Value(new IllegalArgException());
		}
		try {
			EditProfileForm ef = new EditProfileForm();
			ef.setId(Security.getUserId());
			ef.setTitleCode(code);
			teacherService.updateTeacher(ef);
		} catch (AbstractException e) {
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 更新教师职务
	 * 
	 * @since 2.0.3
	 * @param code
	 *            职称代码
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "updateDuty", method = { RequestMethod.POST, RequestMethod.GET })
	public Value updateDuty(Integer code) {
		if (code == null || code <= 0) {
			return new Value(new IllegalArgException());
		}
		Duty duty = dutyService.get(code);
		if (duty == null) {
			return new Value(new IllegalArgException());
		}
		try {
			EditProfileForm ef = new EditProfileForm();
			ef.setId(Security.getUserId());
			ef.setDutyCode(code);
			teacherService.updateTeacher(ef);
		} catch (AbstractException e) {
			return new Value(e);
		}
		return new Value();
	}

	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "updateTextbookCategory", method = { RequestMethod.POST, RequestMethod.GET })
	public Value updateTextbookCategory(Integer code) {
		if (code == null) {
			return new Value(new IllegalArgFormatException());
		}
		TextbookCategory tbc = tbcService.get(code);
		if (tbc == null || tbc.getYoomathStatus() != Status.ENABLED) {
			return new Value(new IllegalArgFormatException());
		}
		Teacher teacher = (Teacher) teacherService.getUser(Security.getUserId());
		if (teacher.getPhaseCode() == PhaseService.PHASE_MIDDLE && tbc.getMiddleStatus() != Status.ENABLED) {
			return new Value(new IllegalArgFormatException());
		}
		if (teacher.getPhaseCode() == PhaseService.PHASE_HIGH && tbc.getHighStatus() != Status.ENABLED) {
			return new Value(new IllegalArgFormatException());
		}
		Integer textbookCode = null;
		List<Textbook> textbooks = null;
		if (teacher.getPhaseCode() == PhaseService.PHASE_MIDDLE) {
			textbooks = tbService.find(Product.YOOMATH, teacher.getPhaseCode(), SubjectService.PHASE_2_MATH, code);
		} else if (teacher.getPhaseCode() == PhaseService.PHASE_HIGH) {
			textbooks = tbService.find(Product.YOOMATH, teacher.getPhaseCode(), SubjectService.PHASE_3_MATH, code);
		}
		if (CollectionUtils.isNotEmpty(textbooks)) {
			for (Textbook textbook : textbooks) {
				if (textbookCode == null && textbook.getYoomathStatus() == Status.ENABLED) {
					textbookCode = textbook.getCode();
				}
			}
		}
		if (textbookCode != null) {
			teacherService.updateCategory(Security.getUserId(), code, textbookCode);
		}
		return new Value();
	}

	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "updatePhaseSubject", method = { RequestMethod.POST, RequestMethod.GET })
	public Value updatePhaseSubject(int phaseCode, int subjectCode) {
		VUser user = userConvert.get(Security.getUserId());
		if (user.isChannelUser()) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_USERCHANNEL_UPDATEPHASE));
		}
		teacherService.setPhaseSubject(Security.getUserId(), phaseCode, subjectCode);
		return new Value();
	}

	/**
	 * 完善资料的更新 <br>
	 * 2017.7.19
	 * 
	 * @param data
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "perfectData", method = { RequestMethod.POST, RequestMethod.GET })
	public Value perfectData(PerfectDataForm data) {
		if (data.getTextBookCode() == null) {
			return new Value(new IllegalArgFormatException());
		}
		if (data.getTextBookCategoryCode() == null) {
			return new Value(new IllegalArgFormatException());
		}
		EditProfileForm form0 = new EditProfileForm();
		form0.setId(Security.getUserId());
		form0.setName(data.getName());
		form0.setPhaseCode(data.getPhaseCode());
		form0.setSchoolCode(data.getSchoolCode());
		form0.setSchoolName(data.getSchoolName());
		form0.setTextBookCode(data.getTextBookCode());
		form0.setTextBookCategoryCode(data.getTextBookCategoryCode());
		teacherService.updateTeacher(form0);
		return new Value();
	}
}

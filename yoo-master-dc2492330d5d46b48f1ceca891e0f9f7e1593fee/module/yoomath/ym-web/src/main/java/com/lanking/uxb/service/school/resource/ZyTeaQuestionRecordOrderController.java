package com.lanking.uxb.service.school.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.order.questionRecord.QuestionRecordOrder;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.school.QuestionSchool;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.school.api.QuestionRecordOrderService;
import com.lanking.uxb.service.school.api.SchoolQuestionService;
import com.lanking.uxb.service.school.convert.QuestionRecordOrderConvert;
import com.lanking.uxb.service.school.form.QuestionRecordOrderCreateForm;
import com.lanking.uxb.service.school.value.VQuestionRecordOrder;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.QuestionSchoolService;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;

/**
 * 教师录题需求Controller
 *
 * @author xinyu.zhou
 * @since 2.6.0
 */
@RestController
@RequestMapping(value = "zy/t/qro")
public class ZyTeaQuestionRecordOrderController {
	@Autowired
	private QuestionRecordOrderService qroService;
	@Autowired
	private QuestionRecordOrderConvert qroConvert;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private TextbookCategoryService textbookCategoryService;
	@Autowired
	private TextbookCategoryConvert textbookCategoryConvert;
	@Autowired
	private SchoolQuestionService schoolQuestionService;
	@Autowired
	private QuestionSchoolService questionSchoolService;
	@Autowired
	private AccountService accountService;

	/**
	 * index接口 返回教材版本 及 学校当前录入量以及剩余的题目量
	 *
	 * @return {@link Value}
	 */
	@MemberAllowed(memberType = "SCHOOL_VIP")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "index", method = { RequestMethod.GET, RequestMethod.POST })
	public Value index() {
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		Map<String, Object> retMap = new HashMap<String, Object>(5);
		retMap.put("teacherCategoryCode", teacher.getTextbookCategoryCode());
		retMap.put("textbookCategoryList",
				textbookCategoryConvert.to(textbookCategoryService.find(Product.YOOMATH, teacher.getPhaseCode())));

		Long questionCount = schoolQuestionService.countBySchool(teacher.getSchoolId());
		retMap.put("questionCount", questionCount);
		// 此学校还没有校本资源
		QuestionSchool questionSchool = questionSchoolService.getBySchool(teacher.getSchoolId());
		retMap.put("recordCount", questionSchool == null ? 0L : questionSchool.getRecordQuestionCount() == null ? 0L
				: questionSchool.getRecordQuestionCount());

		Account account = accountService.getAccountByUserId(teacher.getId());
		String mobile = account.getMobile();
		if (StringUtils.isNotBlank(mobile)) {
			// retMap.put("mobile", StringUtils.getMaskMobile(mobile));
			retMap.put("mobile", account.getMobile());
		}

		return new Value(retMap);
	}

	/**
	 * 提交请求
	 *
	 * @param form
	 *            {@link QuestionRecordOrderCreateForm}
	 * @return {@link Value}
	 */
	@MemberAllowed(memberType = "SCHOOL_VIP")
	@RolesAllowed(userTypes = "TEACHER")
	@RequestMapping(value = "save", method = { RequestMethod.GET, RequestMethod.POST })
	public Value save(QuestionRecordOrderCreateForm form) {
		if (form.getCategoryCode() <= 0 || StringUtils.isBlank(form.getDescription()) || form.getType() == null) {
			return new Value(new MissingArgumentException());
		}
		// 验证提交
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		QuestionSchool questionSchool = questionSchoolService.getBySchool(teacher.getSchoolId());
		Long questionCount = schoolQuestionService.countBySchool(teacher.getSchoolId());
		Long recordCount = questionSchool == null ? 0L : questionSchool.getRecordQuestionCount();
		if (recordCount - questionCount <= 0) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_QUESTION_RECORD_LIMIT));
		}
		if (StringUtils.isBlank(form.getMobile())) {
			Account account = accountService.getAccountByUserId(Security.getUserId());
			if (account.getMobile() == null) {
				return new Value(new MissingArgumentException());
			}

			form.setMobile(account.getMobile());
		}

		qroService.create(form, Security.getUserId());
		qroService.asyncNoticeUsers();
		return new Value();
	}

	/**
	 * 删除请求记录数据
	 *
	 * @param id
	 *            记录id
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = "TEACHER")
	@RequestMapping(value = "delete", method = { RequestMethod.GET, RequestMethod.POST })
	public Value delete(long id) {
		try {
			qroService.delete(id);
		} catch (AbstractException e) {
			return new Value(e);
		}

		return new Value();
	}

	/**
	 * 关闭请求
	 *
	 * @param id
	 *            请求记录id
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = "TEACHER")
	@RequestMapping(value = "close", method = { RequestMethod.GET, RequestMethod.POST })
	public Value close(long id) {
		try {
			qroService.close(id);
		} catch (AbstractException e) {
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 分页查询一个用户所请求的记录数据
	 *
	 * @param page
	 *            当前页
	 * @param size
	 *            每页大小
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = "TEACHER")
	@RequestMapping(value = "query")
	public Value query(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "20") int size) {
		Pageable pageable = P.index(page, size);

		Page<QuestionRecordOrder> orderPage = qroService.page(pageable, Security.getUserId());
		List<QuestionRecordOrder> orders = orderPage.getItems();

		List<VQuestionRecordOrder> vs = qroConvert.to(orders);

		VPage<VQuestionRecordOrder> retPage = new VPage<VQuestionRecordOrder>();
		retPage.setCurrentPage(page);
		retPage.setPageSize(size);
		retPage.setItems(vs);
		retPage.setTotal(orderPage.getTotalCount());
		retPage.setTotalPage(orderPage.getPageCount());

		return new Value(retPage);
	}
}

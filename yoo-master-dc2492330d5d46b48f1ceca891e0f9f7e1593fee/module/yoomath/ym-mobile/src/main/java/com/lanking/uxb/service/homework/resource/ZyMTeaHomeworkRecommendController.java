package com.lanking.uxb.service.homework.resource;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.common.resource.book.BookVersion;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.SchoolConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.homework.form.SetProgressForm;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.web.resource.ZyTeaRecommendHKController;
import com.lanking.uxb.service.zuoye.api.ZyBookService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.convert.ZyBookCatalogConvert;
import com.lanking.uxb.service.zuoye.convert.ZyBookVersionConvert;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.value.VBookCatalog;
import com.lanking.uxb.service.zuoye.value.VBookVersion;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

/**
 * 移动端推荐作业相关
 * 
 * @since 2.1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年5月30日
 */
@RestController
@RequestMapping("zy/m/t/hk/recommend")
public class ZyMTeaHomeworkRecommendController {

	@Autowired
	private ZyTeaRecommendHKController teaRecommendHKController;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private ZyBookService zyBookService;
	@Autowired
	private ZyBookVersionConvert zyBookVersionConvert;
	@Autowired
	private TextbookService tbService;
	@Autowired
	private TextbookConvert tbConvert;
	@Autowired
	private SchoolConvert schoolConvert;
	@Autowired
	private ZyBookCatalogConvert bookCatalogConvert;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private ZyHomeworkClazzConvert homeworkClassConvert;

	/**
	 * @since 2.0.1
	 * @param classId
	 *            要设置的班级ID
	 * @param textbookCode
	 * @return
	 */
	@MemberAllowed
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "bookData", method = { RequestMethod.POST, RequestMethod.GET })
	public Value bookData(Long clazzId, Integer textbookCode) {
		Teacher teacher = (Teacher) teacherService.getUser(Security.getUserId());
		if (teacher.getTextbookCode() == null) {
			// 如果没有设置教材则提示客户端设置教材
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_NEED_FILL_PROFILE));
		}
		HomeworkClazz clazz = zyHkClassService.get(clazzId);
		if (clazz == null || clazz.getTeacherId().longValue() != Security.getUserId()) {
			return new Value(new IllegalArgException());
		}
		VHomeworkClazz vclazz = homeworkClassConvert.to(clazz);

		BookVersion bookVersion = null;
		if (vclazz.getBookVersionId() != null && vclazz.getBookCataId() != null && vclazz.getBookCataId() > 0) {
			bookVersion = zyBookService.getBookVersion(vclazz.getBookVersionId());
			if (textbookCode == null) {
				textbookCode = bookVersion.getTextbookCode();
			}
		}
		ValueMap vm = ValueMap.value();
		// 可选教材列表
		List<Textbook> tbs = tbService.find(Product.YOOMATH, teacher.getPhaseCode(), teacher.getSubjectCode(),
				teacher.getTextbookCategoryCode());
		vm.put("textbooks", tbConvert.to(tbs));

		if (textbookCode == null) {
			// 当前教材
			textbookCode = teacher.getTextbookCode();
		}
		vm.put("textbookCode", textbookCode);

		// 判断用户有没有设置过推荐作业的教辅
		List<BookVersion> userBooks = null;
		if (bookVersion != null && bookVersion.getTextbookCode().intValue() == textbookCode) {
			userBooks = Lists.newArrayList(bookVersion);
		}
		if (CollectionUtils.isNotEmpty(userBooks)) {
			List<VBookVersion> vuserBooks = zyBookVersionConvert.to(userBooks);
			vm.put("userBooks", vuserBooks);
			vm.put("userBookId", bookVersion.getId());
			List<VBookCatalog> vs = bookCatalogConvert.to(zyBookService.getBookCatalogs(bookVersion.getId()));
			for (VBookCatalog vBookCatalog : vs) {
				if (vBookCatalog.getCode() == vclazz.getBookCataId()) {
					vBookCatalog.setSelected(true);
					break;
				}
			}
			vm.put("sections", bookCatalogConvert.assemblySectionTree(vs));
		} else {
			vm.put("userBooks", Collections.EMPTY_LIST);
		}

		// 当前用户的会员类型
		vm.put("memberType", SecurityContext.getMemberType());
		// 当前教材下的可选校本图书
		if (teacher.getSchoolId() != null && SecurityContext.getMemberType() == MemberType.SCHOOL_VIP) {
			Page<BookVersion> schoolBookPage = zyBookService.getSchoolBook(teacher.getTextbookCategoryCode(),
					textbookCode, teacher.getSchoolId(), P.index(1, 100));
			List<BookVersion> schoolBooks = schoolBookPage.getItems();
			vm.put("school", schoolConvert.get(teacher.getSchoolId()));
			if (CollectionUtils.isNotEmpty(schoolBooks)) {
				List<VBookVersion> vschoolBooks = zyBookVersionConvert.to(schoolBooks);
				if (vclazz.getBookVersionId() != null) {
					for (VBookVersion vbook : vschoolBooks) {
						vbook.setSchoolId(teacher.getSchoolId());
						if (vclazz.getBookVersionId().longValue() == vbook.getId()) {
							vbook.setSelected(true);
						} else {
							vbook.setSelected(false);
						}
					}
				}
				vm.put("schoolBooks", vschoolBooks);
			} else {
				vm.put("schoolBooks", Collections.EMPTY_LIST);
			}
		}
		// 当前教材下的可选免费图书
		Page<BookVersion> freeBookPage = zyBookService.getFreeBook(teacher.getTextbookCategoryCode(), textbookCode,
				P.index(1, 100));
		List<BookVersion> freeBooks = freeBookPage.getItems();
		if (CollectionUtils.isNotEmpty(freeBooks)) {
			List<VBookVersion> vfreeBooks = zyBookVersionConvert.to(freeBooks);
			if (vclazz.getBookVersionId() != null) {
				for (VBookVersion vbook : vfreeBooks) {
					vbook.setSchoolId(null);
					if (vclazz.getBookVersionId().longValue() == vbook.getId()) {
						vbook.setSelected(true);
					} else {
						vbook.setSelected(false);
					}
				}
			}
			vm.put("freeBooks", vfreeBooks);
		} else {
			vm.put("freeBooks", Collections.EMPTY_LIST);
		}
		return new Value(vm);
	}

	/**
	 * 设置推荐作业的进度
	 * 
	 * @since 2.1.0
	 * @param form
	 *            设置进度的参数
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "setProgress", method = { RequestMethod.POST, RequestMethod.GET })
	public Value setProgress(SetProgressForm form) {
		teaRecommendHKController.setClassesBook(form.getBookVersionId(), form.getBookCataId(), form.getClassIds());
		return new Value();
	}
}

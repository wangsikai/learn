package com.lanking.uxb.service.web.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.common.resource.book.BookCatalog;
import com.lanking.cloud.domain.common.resource.book.BookVersion;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.code.value.VTextbookCategory;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.zuoye.api.ZyBookService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.convert.ZyBookCatalogConvert;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;

/**
 * 教师推荐作业Controller
 *
 * @author xinyu.zhou
 * @since 2.0.3
 */
@RestController
@RequestMapping(value = "zy/t/recommend/hk")
public class ZyTeaRecommendHKController {
	@Autowired
	private ZyHomeworkClassService homeworkClassService;
	@Autowired
	private ZyBookService bookService;
	@Autowired
	private TextbookConvert tbConvert;
	@Autowired
	private TextbookService tbService;
	@Autowired
	private TextbookCategoryConvert tbcConvert;
	@Autowired
	private TextbookCategoryService tbcService;
	@Autowired
	private UserService userService;
	@Autowired
	private ZyBookCatalogConvert bookCatalogConvert;

	/**
	 * 设置进度和章节数据
	 *
	 * @param bookVersionId
	 *            图书id
	 * @param bookCataId
	 *            图书章节id
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "setBook", method = { RequestMethod.GET, RequestMethod.POST })
	public Value setBook(long bookVersionId, long bookCataId, long classId) {
		Map<String, Object> retMap = new HashMap<String, Object>(2);
		try {
			homeworkClassService.setBook(classId, bookVersionId, bookCataId, Security.getUserId());
			Map<String, Object> recommendMap = bookService.getRecommendCatalogs(bookVersionId, bookCataId,
					Security.getUserId());
			retMap.put("catalogs", bookCatalogConvert.to((List<BookCatalog>) recommendMap.get("catalogs")));
			retMap.put("levelOneCatalog", bookCatalogConvert.to((BookCatalog) recommendMap.get("levelOneCatalog")));
		} catch (ZuoyeException e) {
			return new Value(e);
		}

		return new Value(retMap);
	}

	/**
	 * 批量设置班级的教辅进度
	 *
	 * @param bookVersionId
	 *            图书版本id
	 * @param bookCataId
	 *            图书章节id
	 * @param classIds
	 *            班级id列表
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "setClassesBook", method = { RequestMethod.GET, RequestMethod.POST })
	public Value setClassesBook(long bookVersionId, long bookCataId,
			@RequestParam(value = "classIds") List<Long> classIds) {
		Map<Long, Map<String, Object>> retMap = new HashMap<Long, Map<String, Object>>(classIds.size());
		try {
			homeworkClassService.setBook(classIds, bookVersionId, bookCataId, Security.getUserId());

			for (Long classId : classIds) {
				Map<String, Object> recommendMap = bookService.getRecommendCatalogs(bookVersionId, bookCataId,
						Security.getUserId());
				Map<String, Object> classCatalogsMap = new HashMap<String, Object>(2);
				classCatalogsMap.put("catalogs",
						bookCatalogConvert.to((List<BookCatalog>) recommendMap.get("catalogs")));
				classCatalogsMap.put("levelOneCatalog",
						bookCatalogConvert.to((BookCatalog) recommendMap.get("levelOneCatalog")));

				retMap.put(classId, classCatalogsMap);
			}
		} catch (ZuoyeException e) {
			return new Value(e);
		}

		return new Value(retMap);
	}

	/**
	 * 取得书本对应的版本信息.供布置作业及首页选择教辅用
	 *
	 * @param bookVersionId
	 *            图书版本id
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "textbooks", method = { RequestMethod.GET, RequestMethod.POST })
	public Value textbooks(Long bookVersionId) {
		Map<String, Object> retMap = new HashMap<String, Object>(4);
		if (bookVersionId != null) {
			BookVersion bookVersion = bookService.getBookVersion(bookVersionId);
			if (bookVersion == null) {
				return new Value(new IllegalArgException());
			}

			List<VTextbookCategory> categories = null;
			categories = Lists.newArrayList(tbcConvert.to(tbcService.get(bookVersion.getTextbookCategoryCode())));

			retMap.put("textbookCode", bookVersion.getTextbookCode());
			retMap.put("textbookCategoryCode", bookVersion.getTextbookCategoryCode());
			List<VTextbook> textbooks = tbConvert.to(tbService.find(Product.YOOMATH, bookVersion.getPhaseCode(),
					bookVersion.getSubjectCode(), Lists.newArrayList(bookVersion.getTextbookCategoryCode())));
			retMap.put("textbooks", textbooks);
			retMap.put("categories", categories);
		} else {
			Teacher teacher = (Teacher) userService.getUser(UserType.TEACHER, Security.getUserId());
			// 设置返回版本列表
			List<VTextbookCategory> categories;
			if (teacher.getTextbookCategoryCode() == null) {
				categories = tbcConvert.to(tbcService.find(Product.YOOMATH, teacher.getPhaseCode()));
				retMap.put("textbookCategoryCode", categories.get(0).getCode());
			} else {
				categories = Lists.newArrayList(tbcConvert.to(tbcService.get(teacher.getTextbookCategoryCode())));
				retMap.put("textbookCategoryCode", teacher.getTextbookCategoryCode());
			}

			List<Integer> categoryCodes = new ArrayList<Integer>(categories.size());
			for (VTextbookCategory v : categories) {
				categoryCodes.add(v.getCode());
			}

			retMap.put("categories", categories);

			List<VTextbook> textbooks = tbConvert.to(
					tbService.find(Product.YOOMATH, teacher.getPhaseCode(), teacher.getSubjectCode(), categoryCodes));
			retMap.put("textbooks", textbooks);

			if (teacher.getTextbookCode() == null) {
				for (VTextbook v : textbooks) {
					if (v.getCategoryCode() == categories.get(0).getCode()) {
						retMap.put("textbookCode", v.getCode());
						break;
					}
				}
			} else {
				retMap.put("textbookCode", teacher.getTextbookCode());
			}

		}

		return new Value(retMap);
	}

}

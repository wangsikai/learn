package com.lanking.uxb.rescon.book.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.resource.book.Book;
import com.lanking.cloud.domain.common.resource.book.BookCatalog;
import com.lanking.cloud.domain.common.resource.book.BookQuestion;
import com.lanking.cloud.domain.common.resource.book.BookQuestionCategory;
import com.lanking.cloud.domain.common.resource.book.BookStatus;
import com.lanking.cloud.domain.common.resource.book.BookVersion;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.rescon.account.api.ResconVendorUserManage;
import com.lanking.uxb.rescon.book.api.ResconBookCatalogManage;
import com.lanking.uxb.rescon.book.api.ResconBookManage;
import com.lanking.uxb.rescon.book.api.ResconBookQuestionCategoryManage;
import com.lanking.uxb.rescon.book.api.ResconBookQuestionManage;
import com.lanking.uxb.rescon.book.convert.ResconBookQuestionCategoryConvert;
import com.lanking.uxb.rescon.book.form.BookQuestionQueryForm;
import com.lanking.uxb.rescon.common.ex.ResourceConsoleException;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;
import com.lanking.uxb.rescon.question.convert.ResconQuestionConvert;
import com.lanking.uxb.rescon.question.value.VQuestion;
import com.lanking.uxb.service.counter.api.impl.BookCatalogCounterProvider;
import com.lanking.uxb.service.counter.api.impl.BookCounterProvider;
import com.lanking.uxb.service.search.api.IndexService;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 书本题目.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年10月28日
 */
@RestController
@RequestMapping("rescon/book/que")
@RolesAllowed(userTypes = { "VENDOR_ADMIN", "VENDOR_HEAD", "VENDOR_BUILD", "VENDOR_CHECK" })
public class ResconBookQuestionController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ResconQuestionManage questionManage;
	@Autowired
	private ResconBookManage bookManage;
	@Autowired
	private ResconBookQuestionManage bookQuestionManage;
	@Autowired
	private ResconQuestionConvert questionConvert;
	@Autowired
	private BookCounterProvider bookCounterProvider;
	@Autowired
	private BookCatalogCounterProvider bookCatalogCounterProvider;
	@Autowired
	private ResconVendorUserManage vendorUserManage;
	@Autowired
	@Qualifier("bookCatalogManage")
	private ResconBookCatalogManage bookCatalogManage;
	@Autowired
	private IndexService indexService;
	@Autowired
	private ResconBookQuestionCategoryManage bookQuestionCategoryManage;
	@Autowired
	private ResconBookQuestionCategoryConvert bookQuestionCategoryConvert;

	/**
	 * 批量添加题目.
	 * 
	 * @param bookQuestionCategoryId
	 *            目录习题分类结构的ID
	 * 
	 * @return
	 */
	@RequestMapping(value = "addQuestions", method = RequestMethod.POST)
	public Value addQuestions(Long bookVersionId, Long catalogId, Long bookQuestionCategoryId,
			String... questionCodes) {
		if (null == bookVersionId || null == catalogId || questionCodes == null || questionCodes.length == 0) {
			return new Value(new MissingArgumentException());
		}
		Map<String, Object> map = new HashMap<String, Object>();
		BookCatalog bookCatalog = bookCatalogManage.get(catalogId);
		if (bookCatalog != null && bookVersionId != bookCatalog.getBookVersionId().longValue()) {
			// 版本与目录不对应
			return new Value(new ResourceConsoleException(ResourceConsoleException.VERSION_CATALOG_NOT_MATCH));
		}

		BookVersion bookVersion = bookManage.getVersion(bookVersionId);
		if (bookVersion.getStatus() == BookStatus.PASS) {
			// 版本已经被发布
			return new Value(new ResourceConsoleException(ResourceConsoleException.BOOK_VERSION_PUBLISHED));
		}

		List<Question> questionList = questionManage.findQuestionByCode(Lists.newArrayList(questionCodes), null);
		Map<String, Question> codeMap = new HashMap<String, Question>(questionList.size());

		List<String> errorNullCodes = Lists.newArrayList();
		List<String> errorPhaseCodes = Lists.newArrayList();
		List<String> errorSchoolCodes = Lists.newArrayList();
		List<String> statusCodes = Lists.newArrayList(); // 只能添加已通过题目
		if (questionList.size() == 0) {
			// 题目都不存在
			map.put("errorNullCodes", Lists.newArrayList(questionCodes));
			return new Value(map);
		}

		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());

		for (Question question : questionList) {
			codeMap.put(question.getCode(), question);
		}

		// 校验空题、学科阶段不一致、校本题目
		List<Long> questionIds = new ArrayList<Long>();
		for (String questionCode : questionCodes) {
			Question question = codeMap.get(questionCode);
			if (question == null) {
				errorNullCodes.add(questionCode);
			} else if (question.getPhaseCode().intValue() != bookVersion.getPhaseCode().intValue()
					|| question.getSubjectCode().intValue() != bookVersion.getSubjectCode().intValue()) {
				errorPhaseCodes.add(questionCode);
			} else if (question.getSchoolId() > 0) {
				errorSchoolCodes.add(questionCode);
			} else if (question.getStatus() != CheckStatus.PASS) {
				statusCodes.add(question.getCode());
			}
			questionIds.add(question.getId());
		}
		if (errorNullCodes.size() > 0 || errorPhaseCodes.size() > 0 || errorSchoolCodes.size() > 0
				|| statusCodes.size() > 0) {
			map.put("errorNullCodes", errorNullCodes);
			map.put("errorPhaseCodes", errorPhaseCodes);
			map.put("errorSchoolCodes", errorSchoolCodes);
			map.put("statusCodes", statusCodes);
			return new Value(map);
		}

		try {
			// 判断原书本已经包含的题目
			List<String> errorHasCodes = bookQuestionManage.hasQuestion(bookVersionId,
					Lists.newArrayList(questionCodes));
			if (errorHasCodes.size() > 0) {
				map.put("errorHasCodes", errorHasCodes);
				return new Value(map);
			}

			List<BookQuestion> bookQuestions = new ArrayList<BookQuestion>(questionCodes.length);
			int maxSequence = bookQuestionManage.getMaxSequence(catalogId);
			Date date = new Date();
			for (Question question : questionList) {
				BookQuestion bookQuestion = new BookQuestion();
				bookQuestion.setBookCatalogId(catalogId);
				bookQuestion.setBookVersionId(bookVersionId);
				bookQuestion.setCreateAt(date);
				bookQuestion.setCreateId(user.getId());
				bookQuestion.setQuestionId(question.getId());
				bookQuestion.setSequence(maxSequence + 1);
				if (bookQuestionCategoryId != null) {
					bookQuestion.setBookQuestionCatalogId(bookQuestionCategoryId);
				}
				bookQuestions.add(bookQuestion);
				maxSequence++;
			}
			bookQuestionManage.addQuestions(bookQuestions);

			// 统计
			bookCounterProvider.incrResourceCount(bookVersion.getId(), bookQuestions.size());
			bookCatalogCounterProvider.incrResourceCount(catalogId, bookQuestions.size());

			// 索引处理
			indexService.syncUpdate(IndexType.QUESTION, questionIds);
		} catch (ResourceConsoleException e) {
			return new Value(e);
		}

		return new Value();
	}

	/**
	 * 单独添加某题.
	 * 
	 * @param bookVersionId
	 * @param catalogId
	 * @param questionId
	 * @param bookQuestionCategoryId
	 *            目录习题分类结构的ID
	 * @return
	 */
	@RequestMapping(value = "addQuestionFromId", method = RequestMethod.POST)
	public Value addQuestionFromId(Long bookVersionId, Long catalogId, Long questionId, Long bookQuestionCategoryId) {
		if (null == bookVersionId || null == catalogId || questionId == null) {
			return new Value(new MissingArgumentException());
		}
		BookCatalog bookCatalog = bookCatalogManage.get(catalogId);
		if (bookCatalog != null && bookVersionId != bookCatalog.getBookVersionId().longValue()) {
			// 版本与目录不对应
			return new Value(new ResourceConsoleException(ResourceConsoleException.VERSION_CATALOG_NOT_MATCH));
		}

		BookVersion bookVersion = bookManage.getVersion(bookVersionId);
		if (bookVersion.getStatus() == BookStatus.PASS) {
			// 版本已经被发布
			return new Value(new ResourceConsoleException(ResourceConsoleException.BOOK_VERSION_PUBLISHED));
		}

		Book book = bookManage.get(bookVersion.getBookId());
		Question question = questionManage.get(questionId);

		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
		int maxSequence = bookQuestionManage.getMaxSequence(catalogId);

		BookQuestion bookQuestion = new BookQuestion();
		bookQuestion.setBookCatalogId(catalogId);
		bookQuestion.setBookVersionId(bookVersionId);
		bookQuestion.setCreateAt(new Date());
		bookQuestion.setCreateId(user.getId());
		bookQuestion.setQuestionId(question.getId());
		bookQuestion.setSequence(maxSequence + 1);
		if (bookQuestionCategoryId != null) {
			bookQuestion.setBookQuestionCatalogId(bookQuestionCategoryId);
		}
		bookQuestionManage.addQuestion(bookQuestion);

		if (book.getSchoolId() != null) {
			questionManage.saveQuestionSchool(questionId, book.getSchoolId());
			// questionManage.updateQuestionSchoolCount(book.getSchoolId(), 1);
		}

		// 统计
		bookCounterProvider.incrResourceCount(bookVersion.getId(), 1);
		bookCatalogCounterProvider.incrResourceCount(catalogId, 1);

		// 索引处理
		indexService.syncUpdate(IndexType.QUESTION, questionId);

		return new Value();
	}

	/**
	 * 获取书本题目.
	 * 
	 * @param bookVersionId
	 *            书本版本
	 * @param catalogIds
	 *            顺序子章节集合.
	 * @return
	 */
	@RequestMapping(value = "query", method = RequestMethod.POST)
	public Value query(BookQuestionQueryForm form) {
		if (form == null || form.getBookVersionId() == null) {
			return new Value(new MissingArgumentException());
		}

		int offset = (form.getPage() - 1) * form.getPageSize();
		int size = form.getPageSize();
		Page<Question> questionPage = bookQuestionManage.query(form, P.offset(offset, size));
		List<VQuestion> questions = questionConvert.to(questionPage.getItems());

		VPage<VQuestion> vPage = new VPage<VQuestion>();
		vPage.setCurrentPage(form.getPage());
		vPage.setPageSize(form.getPageSize());
		vPage.setTotal(questionPage.getTotalCount());
		vPage.setTotalPage(questionPage.getPageCount());
		vPage.setItems(questions);
		return new Value(vPage);
	}

	/**
	 * 删除题目.
	 * 
	 * @param bookCatalogId
	 *            章节节点ID
	 * @param questionId
	 *            题目ID
	 * @return
	 */
	@RequestMapping(value = "del", method = RequestMethod.POST)
	public Value deleteQuestion(Long bookVersionId, Long bookCatalogId, Long questionId) {
		if (bookCatalogId == null || questionId == null) {
			return new Value(new MissingArgumentException());
		}
		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
		try {
			BookVersion bookVersion = bookManage.getVersion(bookVersionId);
			if (bookVersion.getStatus() == BookStatus.PASS) {
				// 版本已经被发布
				return new Value(new ResourceConsoleException(ResourceConsoleException.BOOK_VERSION_PUBLISHED));
			}

			bookQuestionManage.deleteQuestion(questionId, bookCatalogId, user.getId());

			// 统计
			if (bookCatalogId != 0) {
				BookCatalog catalog = bookCatalogManage.get(bookCatalogId);
				bookCounterProvider.incrResourceCount(catalog.getBookVersionId(), -1);
				bookCatalogCounterProvider.incrResourceCount(catalog.getId(), -1);
			} else {
				// 无分组删除
				bookCounterProvider.incrNoCatalogResourceCount(bookVersionId, -1);
				bookCounterProvider.incrResourceCount(bookVersionId, -1);
			}

			// 删除分类结构关系
			bookQuestionCategoryManage.delBookQuestionCategoryRelation(bookCatalogId, Lists.newArrayList(questionId));

			// 索引处理
			indexService.syncUpdate(IndexType.QUESTION, questionId);
		} catch (ResourceConsoleException e) {
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 移动题目.
	 * 
	 * @param bookCatalogId
	 *            章节节点ID
	 * @param questionId
	 *            题目ID
	 * @param flag
	 *            移动位置
	 * @return
	 */
	@RequestMapping(value = "move", method = RequestMethod.POST)
	public Value moveQuestion(Long bookCatalogId, Long questionId, Integer flag) {
		if (bookCatalogId == null || questionId == null || (flag != 1 && flag != -1)) {
			return new Value(new MissingArgumentException());
		}
		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
		try {
			bookQuestionManage.moveQuestion(questionId, bookCatalogId, flag, user.getId());
		} catch (ResourceConsoleException e) {
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 移动题目.
	 * 
	 * @param bookCatalogId
	 *            新章节节点ID
	 * @param questionId
	 *            题目ID
	 * @return
	 */
	@RequestMapping(value = "moveTo", method = RequestMethod.POST)
	public Value moveQuestionTo(Long bookVersionId, Long bookCatalogId, Long questionId) {
		if (bookCatalogId == null || questionId == null) {
			return new Value(new MissingArgumentException());
		}
		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
		try {
			bookQuestionManage.moveQuestionTo(bookVersionId, questionId, bookCatalogId, user.getId());

			// 删除分类结构关系
			bookQuestionCategoryManage.delBookQuestionCategoryRelation(bookCatalogId, Lists.newArrayList(questionId));

		} catch (ResourceConsoleException e) {
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 更新习题的分类及标签.
	 * 
	 * @return
	 */
	@RequestMapping(value = "updateCategoryAndTag", method = RequestMethod.POST)
	public Value updateCategoryAndTag(Long questionId, @RequestParam(required = false) List<Long> questionCategorys,
			@RequestParam(required = false) List<Long> questionTags) {
		if (questionId == null) {
			return new Value(new MissingArgumentException());
		}
		questionManage.saveQuestionCategorys(questionId, questionCategorys);
		questionManage.saveQuestionTags(questionId, questionTags);
		Question question = questionManage.get(questionId);
		Map<String, Object> map = new HashMap<String, Object>(1);
		map.put("question", questionConvert.to(question));
		return new Value(map);
	}

	/**
	 * 创建习题分类.
	 * 
	 * @return
	 */
	@RequestMapping(value = "createQuestionCategory", method = RequestMethod.POST)
	public Value createQuestionCategory(Long questionCategoryId, Long bookVersionId, Long bookCatalogId, String name) {
		if (bookVersionId == null || bookCatalogId == null || name == null) {
			return new Value(new MissingArgumentException());
		}
		BookQuestionCategory bookQuestionCategory = bookQuestionCategoryManage.createOrUpdate(questionCategoryId,
				bookVersionId, bookCatalogId, name);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bookQuestionCategory", bookQuestionCategoryConvert.to(bookQuestionCategory));
		return new Value(map);
	}

	/**
	 * 查询结构.
	 * 
	 * @param bookCatalogId
	 *            目录ID
	 * @return
	 */
	@RequestMapping(value = "queryQuestionCategory", method = RequestMethod.POST)
	public Value queryQuestionCategory(Long bookCatalogId) {
		if (bookCatalogId == null) {
			return new Value(new MissingArgumentException());
		}
		Map<String, Object> map = new HashMap<String, Object>();

		List<BookQuestionCategory> bookQuestionCategorys = bookQuestionCategoryManage
				.queryQuestionCategory(bookCatalogId);
		map.put("bookQuestionCategorys", bookQuestionCategoryConvert.to(bookQuestionCategorys));
		
		int questionCount = bookQuestionManage.countCatalog(bookCatalogId);
		map.put("questionCount", questionCount);
		return new Value(map);
	}

	/**
	 * 删除习题分类.
	 * 
	 * @return
	 */
	@RequestMapping(value = "delQuestionCategory", method = RequestMethod.POST)
	public Value delQuestionCategory(Long bookCatalogId, Long questionCategoryId) {
		if (bookCatalogId == null || questionCategoryId == null) {
			return new Value(new MissingArgumentException());
		}

		try {
			bookQuestionCategoryManage.delBookQuestionCategory(bookCatalogId, questionCategoryId);
		} catch (AbstractException e) {
			logger.error(e.getMessage(), e);
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 移动习题分类.
	 * 
	 * @param bookCatalogId
	 *            目录
	 * @param bookQuestionCategoryId
	 *            原分类
	 * @param selectMoveCategoryId
	 *            新的分类
	 * @param questionIds
	 *            习题ID集合
	 * @return
	 */
	@RequestMapping(value = "moveToCategory", method = RequestMethod.POST)
	public Value moveToCategory(Long bookCatalogId, Long bookQuestionCategoryId, Long selectMoveCategoryId,
			@RequestParam(name = "questionIds", required = false) List<Long> questionIds, Long bookVersionId) {
		if (bookCatalogId == null || selectMoveCategoryId == null || CollectionUtils.isEmpty(questionIds)
				|| bookCatalogId == 0) {
			return new Value(new MissingArgumentException());
		}
		try {
			// 校验章节被删除
			BookCatalog bookCatalog = bookCatalogManage.get(bookCatalogId);
			if (bookCatalog == null) {
				return new Value(new EntityNotFoundException());
			}

			// 校验分类被删除
			if (selectMoveCategoryId != 0) { // 0说明是未分类,不走校验
				BookQuestionCategory bookQuestionCategory = bookQuestionCategoryManage.get(selectMoveCategoryId);
				if (bookQuestionCategory == null) {
					return new Value(new EntityNotFoundException());
				}
			}

			VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
			for (Long questionId : questionIds) {
				bookQuestionManage.moveQuestionTo(bookVersionId, questionId, bookCatalogId, user.getId());
			}

			// 删除分类结构关系
			bookQuestionCategoryManage.delBookQuestionCategoryRelation(bookCatalogId, questionIds);

			// 移动
			bookQuestionCategoryManage.moveToCategory(bookCatalogId, bookQuestionCategoryId, selectMoveCategoryId,
					questionIds);
		} catch (AbstractException e) {
			logger.error(e.getMessage(), e);
			return new Value(e);
		}
		return new Value();
	}
}

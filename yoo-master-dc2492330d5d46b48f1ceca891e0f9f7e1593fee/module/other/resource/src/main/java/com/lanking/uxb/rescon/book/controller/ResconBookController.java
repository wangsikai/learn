package com.lanking.uxb.rescon.book.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.component.searcher.api.SearchService;
import com.lanking.cloud.domain.base.counter.Counter;
import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.common.resource.book.Book;
import com.lanking.cloud.domain.common.resource.book.Book2Tag;
import com.lanking.cloud.domain.common.resource.book.BookCatalog;
import com.lanking.cloud.domain.common.resource.book.BookCatalogSection;
import com.lanking.cloud.domain.common.resource.book.BookHistory;
import com.lanking.cloud.domain.common.resource.book.BookHistory.OperateType;
import com.lanking.cloud.domain.common.resource.book.BookQuestion;
import com.lanking.cloud.domain.common.resource.book.BookQuestionCategory;
import com.lanking.cloud.domain.common.resource.book.BookStatus;
import com.lanking.cloud.domain.common.resource.book.BookVersion;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.data.Order;
import com.lanking.cloud.sdk.data.Order.Direction;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.rescon.account.api.ResconVendorUserManage;
import com.lanking.uxb.rescon.book.api.ResconBookCatalogManage;
import com.lanking.uxb.rescon.book.api.ResconBookCatalogSectionManage;
import com.lanking.uxb.rescon.book.api.ResconBookHistoryManage;
import com.lanking.uxb.rescon.book.api.ResconBookManage;
import com.lanking.uxb.rescon.book.api.ResconBookQuestionCategoryManage;
import com.lanking.uxb.rescon.book.api.ResconBookQuestionManage;
import com.lanking.uxb.rescon.book.api.ResconSchoolBookManage;
import com.lanking.uxb.rescon.book.convert.ResconBookConvert;
import com.lanking.uxb.rescon.book.convert.ResconBookVersionConvert;
import com.lanking.uxb.rescon.book.form.BookForm;
import com.lanking.uxb.rescon.book.form.BookQueryForm;
import com.lanking.uxb.rescon.book.value.VBook;
import com.lanking.uxb.rescon.book.value.VBookVersion;
import com.lanking.uxb.rescon.common.ex.ResourceConsoleException;
import com.lanking.uxb.service.code.convert.SchoolConvert;
import com.lanking.uxb.service.counter.api.impl.BookCatalogCounterProvider;
import com.lanking.uxb.service.counter.api.impl.BookCounterProvider;
import com.lanking.uxb.service.counter.api.impl.BooksCounterProvider;
import com.lanking.uxb.service.search.api.IndexService;
import com.lanking.uxb.service.search.api.Page;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 书本.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年10月21日
 */
@RestController
@RequestMapping("rescon/book")
@RolesAllowed(userTypes = { "VENDOR_ADMIN", "VENDOR_HEAD", "VENDOR_BUILD", "VENDOR_CHECK" })
public class ResconBookController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ResconVendorUserManage vendorUserManage;
	@Autowired
	private ResconBookManage resconBookManage;
	@Autowired
	private IndexService indexService;
	@Autowired
	private SearchService searchService;
	@Autowired
	private ResconBookVersionConvert bookVersionConvert;
	@Autowired
	private BooksCounterProvider booksCounterProvider;
	@Autowired
	private BookCounterProvider bookCounterProvider;
	@Autowired
	private ResconBookQuestionManage bookQuestionManage;
	@Autowired
	private ResconBookCatalogManage bookCatalogManage;
	@Autowired
	private BookCatalogCounterProvider bookCatalogCounterProvider;
	@Autowired
	private ResconBookHistoryManage bookHistoryManage;
	@Autowired
	private ResconBookManage bookManage;
	@Autowired
	private ResconBookConvert bookConvert;
	@Autowired
	private ResconSchoolBookManage resconSchoolBookManage;
	@Autowired
	private SchoolConvert schoolConvert;
	@Autowired
	private ResconBookCatalogSectionManage resconBookCatalogSectionManage;
	@Autowired
	private ResconBookQuestionCategoryManage resconBookQuestionCategoryManage;

	/**
	 * 创建书本.
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public Value create(String json) {
		BookForm bookForm = JSON.parseObject(json, BookForm.class);

		if (null == bookForm || StringUtils.isBlank(bookForm.getName()) || null == bookForm.getPhaseCode()
				|| null == bookForm.getSubjectCode()
				|| (null == bookForm.getTextbookCategoryCode() && bookForm.getResourceCategoryCode() == 501)) {
			return new Value(new MissingArgumentException());
		}

		if (bookForm.getName().length() > 100) {
			return new Value(new ResourceConsoleException(ResourceConsoleException.NAME_OUT_OF_LENGTH));
		}

		if (bookForm.getDescription() != null && bookForm.getDescription().length() > 500) {
			return new Value(new ResourceConsoleException(ResourceConsoleException.DESC_OUT_OF_LENGTH));
		}

		if (!Security.isLogin()) {
			return new Value(new NoPermissionException());
		}

		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());

		try {
			BookVersion bookVersion = resconBookManage.saveBook(bookForm, user);
			long bookId = bookVersion.getBookId();

			if (bookForm.getBookId() == null) {
				// 记录历史
				BookHistory history = new BookHistory();
				history.setBookId(bookId);
				history.setCreateAt(new Date());
				history.setCreateId(user.getId());
				history.setType(OperateType.CREATE);
				history.setVersion(bookVersion.getVersion());
				bookHistoryManage.saveHistory(history);

				// 计数，书本总量
				booksCounterProvider.incrTotalCount(user.getVendorId(), 1);
				// 计数，编辑中书本数量
				booksCounterProvider.incrEditCount(user.getVendorId(), 1);
			}

			// 添加索引
			if (bookForm.getBookId() == null) {
				this.addBookIndex(bookId);
			} else {
				this.updateBookIndex(bookId);
			}

			if (bookForm.getBookId() == null) {
				// 新建返回book
				return new Value(bookConvert.to(bookManage.get(bookVersion.getBookId())));
			} else {
				// 编辑返回version
				return new Value(bookVersionConvert.to(bookVersion));
			}
		} catch (ResourceConsoleException e) {
			return new Value(e);
		}
	}

	/**
	 * 复制一份新版本.
	 * 
	 * @param bookVersionId
	 *            已发布版本的ID
	 * @return
	 */
	@RequestMapping(value = "copy", method = RequestMethod.POST)
	public Value createFromPublish(Long bookVersionId) {
		if (null == bookVersionId) {
			return new Value(new MissingArgumentException());
		}
		try {
			Date date = new Date();
			VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
			BookVersion bookVersion = resconBookManage.getVersion(bookVersionId);
			if (bookVersion.getStatus() == BookStatus.PASS) {
				bookVersion.setId(null);
				bookVersion.setMainFlag(false);
				bookVersion.setStatus(BookStatus.EDITING);
				bookVersion.setVersion(bookVersion.getVersion() + 1);
				bookVersion.setCreateAt(date);
				bookVersion.setCreateId(Security.getUserId());
				resconBookManage.saveBookVersion(bookVersion);

				// 复制book_tag关系
				List<Book2Tag> book2Tags = resconBookManage.listBook2Tags(bookVersionId);
				if (CollectionUtils.isNotEmpty(book2Tags)) {
					List<String> tagNames = new ArrayList<String>(book2Tags.size());
					for (Book2Tag book2Tag : book2Tags) {
						tagNames.add(book2Tag.getName());
					}
					resconBookManage.saveBook2Tags(bookVersion.getId(), tagNames);
				}

				// 复制目录
				List<BookCatalog> catalogs = bookCatalogManage.listCatalogs(bookVersionId, false);
				Set<Long> oldBookCatalogIds = new HashSet<Long>(catalogs.size());
				for (BookCatalog bookCatalog : catalogs) {
					oldBookCatalogIds.add(bookCatalog.getId());
				}

				// 如果是教辅图书，需要复制目录章节关系
				Map<Long, BookCatalogSection> bookCatalogSectionMap = new HashMap<Long, BookCatalogSection>();
				if (bookVersion.getResourceCategoryCode() != null && bookVersion.getResourceCategoryCode() == 501) {
					List<BookCatalogSection> bookCatalogSections = resconBookCatalogSectionManage
							.findByBookVersion(bookVersionId);
					bookCatalogSectionMap = new HashMap<Long, BookCatalogSection>(bookCatalogSections.size());
					for (BookCatalogSection bookCatalogSection : bookCatalogSections) {
						bookCatalogSectionMap.put(bookCatalogSection.getBookCatalogId(), bookCatalogSection);
					}
				}
				Map<Long, BookCatalog> catalogMap = bookCatalogManage.saveCopy(bookVersion.getId(), catalogs,
						user.getId(), bookCatalogSectionMap);

				// 复制章节目录习题分类结构
				Map<Long, List<BookQuestionCategory>> oldBookQuestionCategoryMap = resconBookQuestionCategoryManage
						.queryQuestionCategory(oldBookCatalogIds);
				Map<Long, BookQuestionCategory> newBookQuestionCategoryMap = new HashMap<Long, BookQuestionCategory>(); // 新旧分类结构关系
				for (Long oldBookCatalogId : oldBookCatalogIds) {
					BookCatalog newBookCatalog = catalogMap.get(oldBookCatalogId);
					List<BookQuestionCategory> oldBqcs = oldBookQuestionCategoryMap.get(oldBookCatalogId);
					if (CollectionUtils.isNotEmpty(oldBqcs)) {
						for (BookQuestionCategory oldBqc : oldBqcs) {
							BookQuestionCategory newBookQuestionCategory = resconBookQuestionCategoryManage
									.createOrUpdate(null, bookVersionId, newBookCatalog.getId(), oldBqc.getName());

							newBookQuestionCategoryMap.put(oldBqc.getId(), newBookQuestionCategory);
						}
					}
				}

				// 复制题目
				Map<Long, Integer> catalogQuestionCountMap = new HashMap<Long, Integer>();
				com.lanking.cloud.sdk.data.Page<BookQuestion> page = bookQuestionManage.query(bookVersionId,
						P.offset(0, 400));
				this.copyQuestions(page.getItems(), catalogMap, bookVersion.getId(), user.getId(),
						catalogQuestionCountMap, newBookQuestionCategoryMap);
				if (page.getPageCount() > 1) {
					for (int i = 1; i < page.getPageCount(); i++) {
						page = bookQuestionManage.query(bookVersionId, P.offset(i * 400, 400));
						this.copyQuestions(page.getItems(), catalogMap, bookVersion.getId(), user.getId(),
								catalogQuestionCountMap, newBookQuestionCategoryMap);
					}
				}

				// 统计，版本内的题目数量
				int totalCount = (int) page.getTotalCount();
				int noCatalogCount = (int) bookCounterProvider.getNoCatalogResourceCount(bookVersionId);
				bookCounterProvider.incrResourceCount(bookVersion.getId(), totalCount);
				bookCounterProvider.incrNoCatalogResourceCount(bookVersion.getId(), noCatalogCount);

				if (catalogQuestionCountMap.size() > 0) {
					bookCatalogCounterProvider.incrResourceCounts(catalogQuestionCountMap);
				}
				// 计数，编辑中书本数量
				booksCounterProvider.incrEditCount(user.getVendorId(), 1);

				// 记录历史
				BookHistory history = new BookHistory();
				history.setBookId(bookVersion.getBookId());
				history.setCreateAt(date);
				history.setCreateId(user.getId());
				history.setType(OperateType.CREATE);
				history.setVersion(bookVersion.getVersion());
				bookHistoryManage.saveHistory(history);

				// 更新索引
				this.updateBookIndex(bookVersion.getBookId());
			}
			return new Value(bookVersionConvert.to(bookVersion));
		} catch (ResourceConsoleException e) {
			return new Value(e);
		}
	}

	/**
	 * 保存拷贝变更的书本习题.
	 * 
	 * @param questions
	 *            习题集合.
	 * @param catalogMap
	 *            目录
	 * @param bookVersionId
	 *            新版本的ID
	 * @param createId
	 *            创建人ID
	 */
	private void copyQuestions(List<BookQuestion> questions, Map<Long, BookCatalog> catalogMap, Long bookVersionId,
			Long createId, Map<Long, Integer> catalogQuestionCountMap,
			Map<Long, BookQuestionCategory> newBookQuestionCategoryMap) {
		Date date = new Date();
		List<BookQuestion> newQuesions = new ArrayList<BookQuestion>();
		for (BookQuestion bookQuestion : questions) {
			BookCatalog catalog = catalogMap.get(bookQuestion.getBookCatalogId());
			if (bookQuestion.getBookCatalogId() == 0 || catalog != null) {
				long catalogId = catalog != null ? catalog.getId() : 0;
				bookQuestion.setId(null);
				bookQuestion.setBookVersionId(bookVersionId);
				bookQuestion.setBookCatalogId(catalogId);
				bookQuestion.setCreateAt(date);
				bookQuestion.setCreateId(createId);

				if (bookQuestion.getBookQuestionCatalogId() != null && bookQuestion.getBookQuestionCatalogId() != 0) {
					BookQuestionCategory newBookQuestionCategory = newBookQuestionCategoryMap
							.get(bookQuestion.getBookQuestionCatalogId());
					bookQuestion.setBookQuestionCatalogId(newBookQuestionCategory.getId());
				}

				Integer count = catalogQuestionCountMap.get(catalogId);
				catalogQuestionCountMap.put(catalogId, count == null ? 1 : (count + 1));
				newQuesions.add(bookQuestion);
			}
		}
		bookQuestionManage.addQuestions(newQuesions);
	}

	/**
	 * 查询书本.
	 * 
	 * @param form
	 *            查询参数
	 * @return
	 */
	@RequestMapping(value = "query", method = RequestMethod.POST)
	public Value query(BookQueryForm form) {
		// from search
		// return this.queryBookToBookIdsFromSearch(form);

		// from DB
		return this.queryBookToBookIdsFromDB(form);
	}

	// 通过search服务搜索
	private Value queryBookToBookIdsFromSearch(BookQueryForm form) {
		int offset = 0;
		int size = 0;

		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());

		List<IndexTypeable> types = Lists.<IndexTypeable>newArrayList(IndexType.BOOK); // 搜索书本
		BoolQueryBuilder qb = null;
		List<Order> orders = new ArrayList<Order>();
		try {
			offset = (form.getPage() - 1) * form.getPageSize();
			size = form.getPageSize();
			orders = new ArrayList<Order>();
			orders.add(new Order("createAt", Direction.DESC));

			qb = QueryBuilders.boolQuery();
			qb.must(QueryBuilders.termQuery("vendorId", user.getVendorId())); // 供应商限定
			if (null != form.getCreateId()) {
				qb.must(QueryBuilders.termQuery("createId", form.getCreateId()));
			}
			if (StringUtils.isNotBlank(form.getKey())) {
				qb.must(QueryBuilders.multiMatchQuery(form.getKey(), "name1", "name2", "isbn1", "isbn2"));
			}
			if (StringUtils.isNotBlank(form.getBookCode())) {
				try {
					qb.must(QueryBuilders.termQuery("id", Long.parseLong(form.getBookCode())));
				} catch (Exception e) {
				}
			}
			if (null != form.getPhaseCode()) {
				qb.must(QueryBuilders.termQuery("phaseCode", form.getPhaseCode()));
			}
			if (null != form.getSubjectCode()) {
				qb.must(QueryBuilders.termQuery("subjectCode", form.getSubjectCode()));
			}
			if (null != form.getTextbookCategoryCode()) {
				BoolQueryBuilder qbi = QueryBuilders.boolQuery();
				qbi.should(QueryBuilders.termQuery("textbookCategoryCode1", form.getTextbookCategoryCode()));
				qbi.should(QueryBuilders.termQuery("textbookCategoryCode2", form.getTextbookCategoryCode()));
				qb.must(qbi);
			}
			if (null != form.getTextbookCode()) {
				BoolQueryBuilder qbi = QueryBuilders.boolQuery();
				qbi.should(QueryBuilders.termQuery("textbookCode1", form.getTextbookCode()));
				qbi.should(QueryBuilders.termQuery("textbookCode2", form.getTextbookCode()));
				qb.must(qbi);
			}
			if (null != form.getSectionCode()) {
				BoolQueryBuilder qbi = QueryBuilders.boolQuery();
				qbi.should(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("sectionCodes1",
						Lists.newArrayList(form.getSectionCode())));
				qbi.should(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("sectionCodes2",
						Lists.newArrayList(form.getSectionCode())));
				qb.must(qbi);
			}
			if (null != form.getStatus()) {
				BoolQueryBuilder qbi = QueryBuilders.boolQuery();
				qbi.should(QueryBuilders.termQuery("status1", form.getStatus().getValue()));
				qbi.should(QueryBuilders.termQuery("status2", form.getStatus().getValue()));
				qb.must(qbi);
			}
			if (null != form.getResourceCategoryCode()) {
				BoolQueryBuilder qbi = QueryBuilders.boolQuery();
				qbi.should(QueryBuilders.termQuery("resourceCategoryCode1", form.getResourceCategoryCode()));
				qbi.should(QueryBuilders.termQuery("resourceCategoryCode2", form.getResourceCategoryCode()));
				qb.must(qbi);
			}
			if (form.getCreateBt() != null && form.getCreateEt() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date(form.getCreateEt()));
				cal.add(Calendar.DAY_OF_YEAR, 1);
				qb.must(QueryBuilders.rangeQuery("createAt").gte(form.getCreateBt()).lt(cal.getTime().getTime()));
			}
		} catch (Exception e) {
			return new Value(new IllegalArgException());
		}

		Order[] orderArray = new Order[orders.size()];
		for (int i = 0; i < orders.size(); i++) {
			Order order = orders.get(i);
			orderArray[i] = order;
		}
		Page docPage = searchService.search(types, offset, size, qb, null, orderArray);

		// 查询数据库
		List<Long> bookIds = new ArrayList<Long>();
		for (Document document : docPage.getDocuments()) {
			if (document.getId() != null && !document.getId().equals("null")) {
				bookIds.add(Long.parseLong(document.getId()));
			}
		}
		List<VBookVersion> bookVersions = bookVersionConvert.to(resconBookManage.listBookVersion(bookIds)); // 版本
		Map<Long, VBook> bookMap = bookConvert.to(bookManage.mgetBook(bookIds)); // 书本
		for (VBookVersion bookVersion : bookVersions) {
			VBook book = bookMap.get(bookVersion.getBookId());
			if (book == null) {
				book = new VBook();
				book.setId(bookVersion.getBookId());
				book.setNum(1);
				bookMap.put(bookVersion.getBookId(), book);
			} else {
				book.setNum(book.getNum() + 1);
			}
			if (bookVersion.isMainFlag()) {
				book.setMainVersion(bookVersion);
			} else {
				book.setDeputyVersion(bookVersion);
			}
		}

		List<VBook> books = new ArrayList<VBook>(bookMap.size());
		for (Long id : bookIds) {
			books.add(bookMap.get(id));
		}

		VPage<VBook> vPage = new VPage<VBook>();
		vPage.setCurrentPage(form.getPage());
		vPage.setPageSize(form.getPageSize());
		vPage.setTotal(docPage.getTotalCount());
		vPage.setTotalPage(docPage.getTotalPage());
		vPage.setItems(books);
		return new Value(vPage);
	}

	// 通过db服务搜索
	private Value queryBookToBookIdsFromDB(BookQueryForm form) {
		int offset = (form.getPage() - 1) * form.getPageSize();
		int size = form.getPageSize();

		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId()); // 供应商限定
		com.lanking.cloud.sdk.data.Page<Long> page = bookManage.queryBook(user.getVendorId(), form,
				P.offset(offset, size));

		// 查询数据库
		List<Long> bookIds = page.getItems();
		List<VBookVersion> bookVersions = bookVersionConvert.to(resconBookManage.listBookVersion(bookIds)); // 版本
		Map<Long, VBook> bookMap = bookConvert.to(bookManage.mgetBook(bookIds)); // 书本
		for (VBookVersion bookVersion : bookVersions) {
			VBook book = bookMap.get(bookVersion.getBookId());
			if (book == null) {
				book = new VBook();
				book.setId(bookVersion.getBookId());
				book.setNum(1);
				bookMap.put(bookVersion.getBookId(), book);
			} else {
				book.setNum(book.getNum() + 1);
			}
			if (bookVersion.isMainFlag()) {
				book.setMainVersion(bookVersion);
			} else {
				book.setDeputyVersion(bookVersion);
			}
		}

		List<VBook> books = new ArrayList<VBook>(bookMap.size());
		for (Long id : bookIds) {
			books.add(bookMap.get(id));
		}

		VPage<VBook> vPage = new VPage<VBook>();
		vPage.setCurrentPage(form.getPage());
		vPage.setPageSize(form.getPageSize());
		vPage.setTotal(page.getTotalCount());
		vPage.setTotalPage(page.getPageCount());
		vPage.setItems(books);
		return new Value(vPage);
	}

	/**
	 * 获得书本库基本信息.
	 * 
	 * 
	 * @return
	 */
	@RequestMapping(value = "booksDatas", method = RequestMethod.POST)
	public Value booksDatas() {
		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());

		Map<String, Object> map = new HashMap<String, Object>(4);
		Counter counter = booksCounterProvider.getCounter(user.getVendorId());
		map.put("totalCount", counter == null ? 0 : counter.getCount1());
		map.put("publishCount", counter == null ? 0 : counter.getCount2());
		map.put("noPublishCount", counter == null ? 0 : counter.getCount3());
		map.put("editCount", counter == null ? 0 : counter.getCount4());
		return new Value(map);
	}

	/**
	 * 获得书本基本信息.
	 * 
	 * 
	 * @return
	 */
	@RequestMapping(value = "bookDatas", method = RequestMethod.POST)
	public Value bookDatas(Long bookVersionId) {
		Map<String, Object> map = new HashMap<String, Object>(4);
		map.put("totalCount", bookCounterProvider.getResourceCount(bookVersionId));

		// 查找书本计数
		Map<CheckStatus, Integer> countMap = bookQuestionManage.getBookCounts(bookVersionId);
		map.put("passCount", countMap.get(CheckStatus.PASS) == null ? 0 : countMap.get(CheckStatus.PASS).intValue());
		map.put("noCheckCount",
				countMap.get(CheckStatus.EDITING) == null ? 0 : countMap.get(CheckStatus.EDITING).intValue());
		map.put("onePassCount",
				countMap.get(CheckStatus.ONEPASS) == null ? 0 : countMap.get(CheckStatus.ONEPASS).intValue());
		map.put("noPassCount",
				countMap.get(CheckStatus.NOPASS) == null ? 0 : countMap.get(CheckStatus.NOPASS).intValue());
		map.put("draftCount", countMap.get(CheckStatus.DRAFT) == null ? 0 : countMap.get(CheckStatus.DRAFT).intValue());
		return new Value(map);
	}

	/**
	 * 提交版本.
	 * 
	 * @param bookVersionId
	 *            版本ID
	 * @return
	 */
	@RequestMapping(value = "submitVersion", method = RequestMethod.POST)
	public Value submitVersion(Long bookVersionId) {
		if (null == bookVersionId) {
			return new Value(new MissingArgumentException());
		}
		try {
			VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
			BookVersion bookVersion = resconBookManage.getVersion(bookVersionId);

			if (bookVersion.getStatus() == BookStatus.EDITING) {
				resconBookManage.submitBookVersion(bookVersionId);

				// 统计
				booksCounterProvider.incrNoPublishCount(user.getVendorId(), 1);
				booksCounterProvider.incrEditCount(user.getVendorId(), -1);

				// 更新索引
				this.updateBookIndex(bookVersion.getBookId());
			}
		} catch (ResourceConsoleException e) {
			return new Value(e);
		}

		return new Value();
	}

	/**
	 * 发布版本.
	 * 
	 * @param bookVersionId
	 *            版本ID
	 * @return
	 */
	@RolesAllowed(userTypes = { "VENDOR_ADMIN", "VENDOR_HEAD" })
	@RequestMapping(value = "publishVersion", method = RequestMethod.POST)
	public Value publishVersion(Long bookVersionId) {
		if (null == bookVersionId) {
			return new Value(new MissingArgumentException());
		}
		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
		BookVersion bookVersion = resconBookManage.getVersion(bookVersionId);

		// 检测状态
		if (bookVersion.getStatus() != BookStatus.NOCHECK) {
			// 非待发布状态
			return new Value(bookVersion.getStatus());
		}

		try {
			// 统计
			List<BookVersion> bookversions = resconBookManage.listBookVersion(bookVersion.getBookId());
			boolean hasPublish = false;
			for (BookVersion bv : bookversions) {
				if (bv.getStatus() == BookStatus.PASS) {
					hasPublish = true;
					break;
				}
			}

			resconBookManage.publishBook(bookVersion.getBookId(), bookVersionId, user.getId());

			// 记录历史
			BookHistory history = new BookHistory();
			history.setBookId(bookVersion.getBookId());
			history.setCreateAt(new Date());
			history.setCreateId(user.getId());
			history.setType(OperateType.PUBLISH);
			history.setVersion(bookVersion.getVersion());
			bookHistoryManage.saveHistory(history);

			if (!hasPublish) {
				// 发布数量值记录书本
				booksCounterProvider.incrPublishCount(user.getVendorId(), 1);
			}
			// 未发布数量记录版本
			booksCounterProvider.incrNoPublishCount(user.getVendorId(), -1);

			// 更新索引
			this.updateBookIndex(bookVersion.getBookId());
		} catch (ResourceConsoleException e) {
			return new Value(e);
		}

		return new Value(bookVersion.getStatus());
	}

	/**
	 * 更新书本封面.
	 * 
	 * @param bookVersionId
	 *            书本版本ID.
	 * @param fileId
	 *            文件ID.
	 * @return
	 */
	@RequestMapping(value = "updateCover", method = RequestMethod.POST)
	public Value updateCover(Long bookVersionId, Long fileId) {
		if (null == bookVersionId || null == fileId) {
			return new Value(new MissingArgumentException());
		}
		try {
			resconBookManage.updateCover(bookVersionId, fileId);
		} catch (ResourceConsoleException e) {
			return new Value(e);
		}

		return new Value();
	}

	/**
	 * 待发布版本变更成录入中状态.
	 * 
	 * @param bookVersionId
	 *            版本ID
	 * @return
	 */
	@RequestMapping(value = "changeNocheck", method = RequestMethod.POST)
	public Value changeNocheck(Long bookVersionId) {
		if (null == bookVersionId) {
			return new Value(new MissingArgumentException());
		}
		BookVersion bookVersion = resconBookManage.getVersion(bookVersionId);
		if (bookVersion.getStatus() == BookStatus.NOCHECK) {
			VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
			try {
				resconBookManage.changeBookVersionStatus(bookVersionId, BookStatus.EDITING);

				// 统计，未发布数量记录版本
				booksCounterProvider.incrNoPublishCount(user.getVendorId(), -1);
				booksCounterProvider.incrEditCount(user.getVendorId(), 1);

				// 更新索引
				this.updateBookIndex(bookVersion.getBookId());
			} catch (ResourceConsoleException e) {
				return new Value(e);
			}
		}
		return new Value();
	}

	/**
	 * 书本详情页面.
	 * 
	 * @param bookId
	 *            书本ID.
	 * @return
	 */
	@RequestMapping(value = "bookDetail", method = RequestMethod.POST)
	public Value bookDetail(Long bookId) {
		if (null == bookId) {
			return new Value(new MissingArgumentException());
		}
		Book book = bookManage.get(bookId);
		if (null == book) {
			return new Value(new EntityNotFoundException());
		}
		List<VBookVersion> bookVersions = bookVersionConvert.to(resconBookManage.listBookVersion(bookId)); // 版本
		VBook vBook = bookConvert.to(book); // 书本
		for (VBookVersion bookVersion : bookVersions) {
			vBook.setNum(vBook.getNum() + 1);
			if (bookVersion.isMainFlag()) {
				vBook.setMainVersion(bookVersion);
			} else {
				vBook.setDeputyVersion(bookVersion);
			}
		}
		return new Value(vBook);
	}

	/**
	 * 建立书本索引.
	 * 
	 * @param bookId
	 */
	private void addBookIndex(long bookId) {
		indexService.add(IndexType.BOOK, bookId);
	}

	/**
	 * 更新书本索引.
	 * 
	 * @param bookId
	 */
	private void updateBookIndex(long bookId) {
		indexService.update(IndexType.BOOK, bookId);
	}

	/**
	 * 书本整体统计数据.
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "booksCounts", method = RequestMethod.POST)
	public Value booksCounts() {
		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId()); // 供应商限定

		Map<String, Object> map = new HashMap<String, Object>(3);
		List<Map> datas = bookManage.queryBooksCounts(user.getVendorId());
		List<Integer> juniors = Lists.newArrayList(0, 0, 0, 0, 0, 0, 0, 0, 0); // 初中
		List<Integer> highs = Lists.newArrayList(0, 0, 0, 0, 0, 0, 0, 0, 0); // 高中
		int total = 0;
		for (Map data : datas) {
			int pcode = Integer.parseInt(data.get("pcode").toString());
			int scode = Integer.parseInt(data.get("scode").toString());
			int counts = Integer.parseInt(data.get("counts").toString());
			total += counts;
			if (pcode == 2) {
				switch (scode) {
				case 202:
					juniors.set(0, counts); // 数学
					break;
				case 203:
					juniors.set(1, counts); // 英语
					break;
				case 201:
					juniors.set(2, counts); // 语文
					break;
				case 204:
					juniors.set(3, counts); // 物理
					break;
				case 205:
					juniors.set(4, counts); // 化学
					break;
				case 208:
					juniors.set(5, counts); // 生物
					break;
				case 206:
					juniors.set(6, counts); // 历史
					break;
				case 207:
					juniors.set(7, counts); // 地理
					break;
				case 209:
					juniors.set(8, counts); // 政治
					break;
				default:
				}
			} else if (pcode == 3) {
				switch (scode) {
				case 302:
					highs.set(0, counts); // 数学
					break;
				case 303:
					highs.set(1, counts); // 英语
					break;
				case 301:
					highs.set(2, counts); // 语文
					break;
				case 304:
					highs.set(3, counts); // 物理
					break;
				case 305:
					highs.set(4, counts); // 化学
					break;
				case 308:
					highs.set(5, counts); // 生物
					break;
				case 306:
					highs.set(6, counts); // 历史
					break;
				case 307:
					highs.set(7, counts); // 地理
					break;
				case 309:
					highs.set(8, counts); // 政治
					break;
				default:
				}
			}
		}
		map.put("juniors", juniors);
		map.put("highs", highs);
		map.put("total", total);
		return new Value(map);
	}

	/**
	 * 学科书本统计数据.
	 * 
	 * @return
	 */
	@RequestMapping(value = "subjectBooksCounts", method = RequestMethod.POST)
	public Value subjectBooksCounts(Integer subjectCode) {
		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId()); // 供应商限定
		Map<BookStatus, Integer> datas = bookManage.subjectBooksCounts(subjectCode, user.getVendorId());
		return new Value(datas);
	}

	/**
	 * 书本统计书本查询.
	 * 
	 * @param form
	 *            查询条件
	 * @return
	 */
	@RequestMapping(value = "queryCountBook", method = RequestMethod.POST)
	public Value queryCountBook(BookQueryForm form) {
		int offset = (form.getPage() - 1) * form.getPageSize();
		int size = form.getPageSize();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>(size);

		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId()); // 供应商限定
		com.lanking.cloud.sdk.data.Page<Long> page = bookManage.queryBook(user.getVendorId(), form,
				P.offset(offset, size));

		// 查询数据库
		List<Long> bookIds = page.getItems();
		List<VBookVersion> bookVersions = bookVersionConvert.to(resconBookManage.listBookVersion(bookIds)); // 版本集合
		Map<Long, Book> bookMap = bookManage.mgetBook(bookIds); // 书本

		List<Long> currentVersionIds = new ArrayList<Long>(bookIds.size()); // 当前版本ID集合

		for (Long bookId : bookIds) {
			Map<String, Object> data = new HashMap<String, Object>();
			Book book = bookMap.get(bookId);
			data.put("id", book.getId());
			data.put("schoolId", book.getSchoolId());
			for (int i = bookVersions.size() - 1; i >= 0; i--) {
				VBookVersion bookVersion = bookVersions.get(i);
				if (bookVersion.getBookId().longValue() == bookId
						&& bookVersion.getSubject().getCode() == form.getSubjectCode()) {
					VBookVersion publishVersion = null;
					if (data.get("name") == null) { // 当前版本
						data.put("name", bookVersion.getName());
						data.put("textbookCategoryName", bookVersion.getTextbookCategory().getName());
						if (bookVersion.getTextbook() != null) {
							data.put("textBookName", bookVersion.getTextbook().getName());
						} else {
							data.put("textBookName", "");
						}
						data.put("currentVersion", bookVersion);
						data.put("creator", bookVersion.getCreator().getRealName());
						currentVersionIds.add(bookVersion.getId());
					}
					if (bookVersion.getStatus() == BookStatus.PASS
							&& (publishVersion == null || publishVersion.getVersion() < bookVersion.getVersion())) {
						publishVersion = bookVersion;
					}
					bookVersions.remove(i);
					data.put("publishVersion", publishVersion);
				}
			}
			data.put("openStatus", book.getOpenStatus());
			datas.add(data);
		}

		// 查询发布版本时间
		Map<Long, Date> publishAts = bookHistoryManage.mgetTimeByLastPublish(bookIds);

		// 查询当前版本题目统计
		List<Map<CheckStatus, Integer>> questionCounts = bookQuestionManage.getBookCounts(currentVersionIds);
		for (int i = 0; i < datas.size(); i++) {
			Map<CheckStatus, Integer> map = questionCounts.get(i);
			int total = 0;
			for (Entry<CheckStatus, Integer> entry : map.entrySet()) {
				total += entry.getValue().intValue();
				if (entry.getKey() == CheckStatus.PASS) {
					datas.get(i).put("passQuestions", entry.getValue().intValue());
				}
			}
			datas.get(i).put("totalQuestions", total);
			Date date = publishAts.get((Long) datas.get(i).get("id"));
			if (date != null) {
				datas.get(i).put("publishVersionTime", date);
			}
		}

		VPage<Map<String, Object>> vPage = new VPage<Map<String, Object>>();
		vPage.setCurrentPage(form.getPage());
		vPage.setPageSize(form.getPageSize());
		vPage.setTotal(page.getTotalCount());
		vPage.setTotalPage(page.getPageCount());
		vPage.setItems(datas);
		return new Value(vPage);
	}

	/**
	 * 获得书本授权学校信息.
	 * 
	 * @param bookId
	 *            书本ID
	 * @return
	 */
	@RequestMapping(value = "getBookSchooDetail", method = RequestMethod.POST)
	public Value getBookSchooDetail(Long bookId) {
		if (bookId == null) {
			return new Value(new MissingArgumentException());
		}
		List<School> schools = resconSchoolBookManage.listSchool(bookId);
		if (CollectionUtils.isEmpty(schools)) {
			return new Value(Lists.newArrayList());
		} else {
			return new Value(schoolConvert.to(schools));
		}
	}

	/**
	 * 获取版本信息.
	 * 
	 * @param bookVersionId
	 *            版本ID
	 * @return
	 */
	@RequestMapping(value = "getBookVersion", method = RequestMethod.POST)
	public Value getBookVersion(Long bookVersionId) {
		if (bookVersionId == null) {
			return new Value(new MissingArgumentException());
		}
		BookVersion bookVersion = resconBookManage.getVersion(bookVersionId);

		return new Value(bookVersionConvert.to(bookVersion));
	}
}

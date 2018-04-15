package com.lanking.uxb.rescon.book.api.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.book.Book;
import com.lanking.cloud.domain.common.resource.book.Book2Tag;
import com.lanking.cloud.domain.common.resource.book.BookHistory;
import com.lanking.cloud.domain.common.resource.book.BookStatus;
import com.lanking.cloud.domain.common.resource.book.BookVersion;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.PageImpl;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.book.api.ResconBookCatalogSectionManage;
import com.lanking.uxb.rescon.book.api.ResconBookManage;
import com.lanking.uxb.rescon.book.form.BookForm;
import com.lanking.uxb.rescon.book.form.BookQueryForm;
import com.lanking.uxb.rescon.common.ex.ResourceConsoleException;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;
import com.lanking.uxb.rescon.question.api.ResconSchoolQuestionManage;
import com.lanking.uxb.service.search.api.IndexService;

/**
 * 书本接口实现.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年10月22日
 */
@SuppressWarnings("unchecked")
@Service
@Transactional(readOnly = true)
public class ResconBookManageImpl implements ResconBookManage {

	@Autowired
	@Qualifier("BookRepo")
	Repo<Book, Long> bookRepo;
	@Autowired
	@Qualifier("BookVersionRepo")
	Repo<BookVersion, Long> bookVersionRepo;
	@Autowired
	@Qualifier("BookHistoryRepo")
	Repo<BookHistory, Long> bookHistoryRepo;
	@Autowired
	@Qualifier("QuestionRepo")
	Repo<Question, Long> questionRepo;
	@Autowired
	@Qualifier("Book2TagRepo")
	Repo<Book2Tag, Long> book2TagRepo;

	@Autowired
	private IndexService indexService;
	@Autowired
	private ResconQuestionManage questionManage;
	@Autowired
	private ResconSchoolQuestionManage resconSchoolQuestionManage;
	@Autowired
	private ResconBookCatalogSectionManage resconBookCatalogSectionManage;

	@Override
	@Transactional(readOnly = true)
	public Book get(long bookId) {
		return bookRepo.get(bookId);
	}

	@Override
	@Transactional(readOnly = true)
	public BookVersion getVersion(long bookVersionId) {
		return bookVersionRepo.get(bookVersionId);
	}

	@Override
	@Transactional
	public BookVersion saveBook(BookForm form, VendorUser creater) throws ResourceConsoleException {
		Date date = new Date();
		Long bookId = form.getBookId();
		BookVersion bookVersion = null;

		if (bookId == null) {
			// 新创建书本
			Book book = new Book();
			book.setVendorId(creater.getVendorId());
			book.setCreateAt(date);
			book.setCreateId(creater.getId());
			book.setProductId(form.getProductId());
			book.setSchoolId(form.getSchoolId());
			bookRepo.save(book);
			bookId = book.getId();
		} else {
			Book book = bookRepo.get(bookId);
			if (book.getSchoolId() != null && form.getSchoolId() == null) {
				Long oldSchoolId = book.getSchoolId();
				// 校本图书变为普通图书
				book.setSchoolId(null);
				bookRepo.save(book);
				// 设置题目
				this.setQuestionSchoolNull(bookId, oldSchoolId);
			}
		}

		if (form.getBookVersionId() == null) {
			// 新创建版本
			bookVersion = new BookVersion();
			if (null != form.getProductId()) {
				bookVersion.setStatus(BookStatus.PASS);
			} else {
				bookVersion.setStatus(BookStatus.EDITING);
			}

			bookVersion.setMainFlag(true);
			bookVersion.setCreateId(creater.getId());
			bookVersion.setCreateAt(date);
		} else {
			// 更新版本
			bookVersion = bookVersionRepo.get(form.getBookVersionId());

			if (bookVersion.getResourceCategoryCode() != null && bookVersion.getResourceCategoryCode() == 501) {
				if (form.getResourceCategoryCode() == null || form.getResourceCategoryCode() != 501
						|| form.getTextbookCode() == null
						|| bookVersion.getTextbookCode().intValue() != form.getTextbookCode()) {
					// 如果一个教辅图书变成了非教辅图书，或者对应的教材改变了，则需要删除目录章节对应关系
					resconBookCatalogSectionManage.deleteCatalogRelationByBookVersion(bookVersion.getId());
				}
			}

			if (bookVersion.getStatus() == BookStatus.PASS) {
				// 已发布的书本再编辑将生成一份新的版本
				int version = bookVersion.getVersion() + 1;
				bookVersion = new BookVersion();
				bookVersion.setStatus(BookStatus.EDITING);
				bookVersion.setMainFlag(false);
				bookVersion.setVersion(version);
				bookVersion.setCreateId(creater.getId());
				bookVersion.setCreateAt(date);
			}
		}

		bookVersion.setDescription(form.getDescription());
		bookVersion.setIsbn(form.getIsbn());
		bookVersion.setName(form.getName());
		if (StringUtils.isNotBlank(form.getSname())) {
			bookVersion.setShortName(form.getSname());
		} else {
			bookVersion.setShortName(null);
		}
		bookVersion.setPhaseCode(form.getPhaseCode());
		bookVersion.setResourceCategoryCode(form.getResourceCategoryCode());
		bookVersion.setPress(form.getPress());

		if (null != form.getSectionCodes() && form.getSectionCodes().size() > 0) {
			// 找到节点顺序
			bookVersion.setSectionCode(form.getSectionCodes().get(form.getSectionCodes().size() - 1));
			bookVersion.setSectionCodes(form.getSectionCodes());
		} else {
			bookVersion.setSectionCode(null);
			bookVersion.setSectionCodes(null);
		}
		bookVersion.setSubjectCode(form.getSubjectCode());
		bookVersion.setTextbookCategoryCode(form.getTextbookCategoryCode());
		bookVersion.setTextbookCode(form.getTextbookCode());
		bookVersion.setBookId(bookId);

		bookVersionRepo.save(bookVersion);

		return bookVersion;
	}

	/**
	 * 题目学校属性设空.
	 * 
	 * @param bookid
	 */
	@Async
	private void setQuestionSchoolNull(long bookId, long oldSchoolId) {
		// 所有图书中的题目都将变更
		int size = 200;
		Page<Long> page = this.findQuestionIdByBook(bookId, P.offset(0, size));
		int pageCount = page.getPageCount();
		if (page.getItemSize() > 0) {
			this.updateSchoolNull(page.getItems());
			// 索引处理
			indexService.syncUpdate(IndexType.QUESTION, page.getItems());

			// 只有已通过的题目进行校本题目计数
			List<Question> qs = questionManage.mgetList(page.getItems());
			List<Long> qss = new ArrayList<Long>();
			for (Question question : qs) {
				if (question.getStatus() == CheckStatus.PASS) {
					qss.add(question.getId());
				}
			}
			if (qss.size() > 0) {
				questionManage.updateQuestionSchoolCount(oldSchoolId, -qss.size());
				resconSchoolQuestionManage.delSchoolQuestion(oldSchoolId, qss);
			}
		}
		if (pageCount > 1) {
			for (int i = 1; i < pageCount; i++) {
				page = this.findQuestionIdByBook(bookId, P.offset(i * size, size));
				this.updateSchoolNull(page.getItems());
				// 索引处理
				indexService.syncUpdate(IndexType.QUESTION, page.getItems());

				// 只有已通过的题目进行校本题目计数
				List<Question> qs = questionManage.mgetList(page.getItems());
				List<Long> qss = new ArrayList<Long>();
				for (Question question : qs) {
					if (question.getStatus() == CheckStatus.PASS) {
						qss.add(question.getId());
					}
				}
				if (qss.size() > 0) {
					questionManage.updateQuestionSchoolCount(oldSchoolId, -qss.size());
					resconSchoolQuestionManage.delSchoolQuestion(oldSchoolId, qss);
				}
			}
		}

		// 只有已通过的题目进行校本题目计数
		// questionManage.updateQuestionSchoolCount(oldSchoolId, -(int)
		// page.getTotalCount());
	}

	@Transactional(readOnly = true)
	private Page<Long> findQuestionIdByBook(long bookId, Pageable p) {
		return bookRepo.find(
				"select distinct(bq.question_id) from book_question bq"
						+ " inner join book_version bv ON bv.id=bq.book_version_id AND bv.book_id = :bookId",
				Params.param("bookId", bookId)).fetch(p, Long.class);
	}

	@Transactional
	private void updateSchoolNull(List<Long> questionIds) {
		questionRepo.execute("update question set school_id = 0 where id in (:ids)", Params.param("ids", questionIds));
	}

	@Override
	@Transactional
	public void submitBookVersion(long bookVersionId) throws ResourceConsoleException {
		BookVersion bookVersion = bookVersionRepo.get(bookVersionId);
		bookVersion.setStatus(BookStatus.NOCHECK);
		bookVersionRepo.save(bookVersion);
	}

	@Override
	@Transactional
	public void saveBookVersion(BookVersion bookVersion) throws ResourceConsoleException {
		bookVersionRepo.save(bookVersion);
	}

	@Override
	@Transactional
	public void publishBook(long bookId, long bookVersionId, long createId) throws ResourceConsoleException {
		List<BookVersion> bookVersions = this.listBookVersion(bookId);
		for (BookVersion bookVersion : bookVersions) {
			if (bookVersion.getId().longValue() == bookVersionId) {
				bookVersion.setStatus(BookStatus.PASS);
				bookVersion.setMainFlag(true);
			} else {
				bookVersion.setStatus(BookStatus.DELETED);
			}
			bookVersionRepo.save(bookVersion);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookVersion> listBookVersion(long bookId) {
		return bookVersionRepo.find("$listBookVersion", Params.param("bookId", bookId)).list();
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookVersion> listBookVersion(Collection<Long> bookIds) {
		return this.listBookVersion(bookIds, null);
	}

	@Override
	public List<BookVersion> listBookVersion(Collection<Long> bookIds, BookStatus bookStatus) {
		if (bookIds == null || bookIds.size() == 0) {
			return Lists.newArrayList();
		}
		Params params = Params.param("bookIds", bookIds);
		if (bookStatus != null) {
			params.put("bookStatus", bookStatus.getValue());
		}
		return bookVersionRepo.find("$mListBookVersion", params).list();
	}

	@Override
	public List<BookVersion> listMainBookVersion(Collection<Long> bookIds, BookStatus bookStatus) {
		if (bookIds == null || bookIds.size() == 0) {
			return Lists.newArrayList();
		}
		Params params = Params.param("bookIds", bookIds);
		if (bookStatus != null) {
			params.put("bookStatus", bookStatus.getValue());
		}
		return bookVersionRepo.find("$mListMainBookVersion", params).list();
	}

	@Override
	@Transactional(readOnly = true)
	public Map<Long, Book> mgetBook(Collection<Long> bookIds) {
		return bookRepo.mget(bookIds);
	}

	@Override
	@Transactional
	public void updateCover(long bookVersionId, long fileId) throws ResourceConsoleException {
		BookVersion bookVersion = bookVersionRepo.get(bookVersionId);
		bookVersion.setCoverId(fileId);
		bookVersionRepo.save(bookVersion);
	}

	@Override
	@Transactional
	public void changeBookVersionStatus(long bookVersionId, BookStatus status) throws ResourceConsoleException {
		BookVersion bookVersion = bookVersionRepo.get(bookVersionId);
		bookVersion.setStatus(status);
		bookVersionRepo.save(bookVersion);
	}

	@Override
	public Book getFromProduct(Long productId) {
		return bookRepo.find("$getFromProduct", Params.param("productId", productId)).get();
	}

	@Override
	public Page<Long> queryBook(Long vendorId, BookQueryForm form, Pageable pageable) {
		Params params = Params.param("vendorId", vendorId);
		if (null != form) {
			if (StringUtils.isNotBlank(form.getKey())) {
				params.put("key", "%" + form.getKey() + "%");
			}
			if (StringUtils.isNotBlank(form.getBookCode())) {
				try {
					params.put("bookCode", Long.parseLong(form.getBookCode()));
				} catch (NumberFormatException e) {
					return new PageImpl<Long>();
				}
			}
			if (null != form.getCreateId()) {
				params.put("createId", form.getCreateId());
			}
			if (null != form.getStatus()) {
				params.put("status", form.getStatus().getValue());
			}
			if (null != form.getCreateBt()) {
				params.put("createBt", new Date(form.getCreateBt()));
			}
			if (null != form.getCreateEt()) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date(form.getCreateEt()));
				cal.add(Calendar.DAY_OF_YEAR, 1);
				params.put("createEt", cal.getTime());
			}
			if (null != form.getPhaseCode()) {
				params.put("phaseCode", form.getPhaseCode());
			}
			if (null != form.getSubjectCode()) {
				params.put("subjectCode", form.getSubjectCode());
			}
			if (null != form.getTextbookCategoryCode()) {
				params.put("textbookCategoryCode", form.getTextbookCategoryCode());
			}
			if (null != form.getTextbookCode()) {
				params.put("textbookCode", form.getTextbookCode());
			}
			if (null != form.getTextbookCategoryCode()) {
				params.put("textbookCategoryCode", form.getTextbookCategoryCode());
			}
			if (null != form.getSectionCode()) {
				params.put("sectionCode", form.getSectionCode());
			}
			if (null != form.getResourceCategoryCode()) {
				params.put("resourceCategoryCode", form.getResourceCategoryCode());
			}
			if (StringUtils.isNotBlank(form.getName())) {
				params.put("name", "%" + form.getName() + "%");
			}
			if (null != form.getPublishBt()) {
				params.put("publishBt", new Date(form.getPublishBt()));
			}
			if (null != form.getPublishEt()) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date(form.getPublishEt()));
				cal.add(Calendar.DAY_OF_YEAR, 1);
				params.put("publishEt", cal.getTime());
			}
			if (form.getOrderType() != null) {
				params.put("orderType", form.getOrderType().getValue());
				params.put("direction", form.getDirection().getValue());
			}
		}
		return bookRepo.find("$queryBooks", params).fetch(pageable, Long.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> queryBooksCounts(Long vendorId) {
		return bookRepo.find("$queryBooksCounts", Params.param("vendorId", vendorId)).list(Map.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<BookStatus, Integer> subjectBooksCounts(Integer subjectCode, Long vendorId) {
		Params params = Params.param("vendorId", vendorId);
		if (null != subjectCode) {
			params.put("subjectCode", subjectCode);
		}
		List<Map> list = bookRepo.find("$subjectBooksCounts", params).list(Map.class);
		Map<BookStatus, Integer> returnMap = new HashMap<BookStatus, Integer>();
		for (Map map : list) {
			BookStatus status = BookStatus.findByValue(Integer.parseInt(map.get("status").toString()));
			int count = Integer.parseInt(map.get("counts").toString());
			returnMap.put(status, count);
		}
		return returnMap;
	}

	@Override
	public List<Book2Tag> listBook2Tags(long bookVersionId) {
		return book2TagRepo.find("listBook2Tags", Params.param("bookVersionId", bookVersionId)).list();
	}

	@Override
	@Transactional
	public void saveBook2Tags(long bookVersionId, List<String> tagNames) {
		for (String name : tagNames) {
			Book2Tag book2Tag = new Book2Tag();
			book2Tag.setBookVersionId(bookVersionId);
			book2Tag.setName(name);
			book2TagRepo.save(book2Tag);
		}
	}
}
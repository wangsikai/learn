package com.lanking.uxb.service.index.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.domain.common.resource.book.Book;
import com.lanking.cloud.domain.common.resource.book.BookVersion;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.index.convert.BookIndexConvert;
import com.lanking.uxb.service.index.value.BookIndexDoc;
import com.lanking.uxb.service.search.api.impl.AbstractIndexHandle;

@Service
@Transactional(readOnly = true)
public class BookIndexHandle extends AbstractIndexHandle {
	@Autowired
	private BookIndexConvert bookIndexConvert;

	@Autowired
	@Qualifier("BookVersionRepo")
	Repo<BookVersion, Long> bookVersionRepo;
	@Autowired
	@Qualifier("BookRepo")
	Repo<Book, Long> bookRepo;

	@Override
	public String getDescription() {
		return "书本索引";
	}

	@Override
	public boolean accept(IndexTypeable type) {
		return IndexType.BOOK == type;
	}

	@Override
	public IndexTypeable getType() {
		return IndexType.BOOK;
	}

	@Override
	public long dataCount() {
		return bookRepo.find("$indexDataCount").count();
	}

	@Override
	public void add(Long id) {
		Book book = bookRepo.get(id);
		List<BookVersion> bookVersions = bookVersionRepo.find("$listIndexBookVersion", Params.param("bookId", id))
				.list();
		BookVersion bookVersion1 = null;
		BookVersion bookVersion2 = null;
		for (BookVersion bookVersion : bookVersions) {
			if (bookVersion.isMainFlag()) {
				bookVersion1 = bookVersion;
			} else {
				bookVersion2 = bookVersion;
			}
		}

		putDocument(this.convert(bookIndexConvert.to(book, bookVersion1, bookVersion2)));
	}

	@Override
	public void add(Collection<Long> ids) {
		Map<Long, List<BookVersion>> map = new HashMap<Long, List<BookVersion>>();
		Map<Long, Book> books = bookRepo.mget(ids);
		List<BookVersion> bookVersions = bookVersionRepo.find("$mListIndexBookVersion", Params.param("bookIds", ids))
				.list();
		for (BookVersion bookVersion : bookVersions) {
			if (map.get(bookVersion.getBookId()) == null) {
				map.put(bookVersion.getBookId(), Lists.newArrayList(bookVersion));
			} else {
				map.get(bookVersion.getBookId()).add(bookVersion);
			}
		}
		List<BookIndexDoc> docs = new ArrayList<BookIndexDoc>(map.size());
		for (List<BookVersion> bvs : map.values()) {
			docs.add(bookIndexConvert.to(bvs, books));
		}

		putDocuments(this.convert(docs));
	}

	@Override
	public void delete(Long id) {
		deleteDocument(IndexType.BOOK, id.toString());
	}

	@Override
	public void delete(Collection<Long> bizIds) {
		for (Long bizId : bizIds) {
			this.delete(bizId);
		}
	}

	@Override
	public void reindex() {
		int page = 1;
		reindexHandle(page, null);
	}

	private void reindexHandle(int page, Integer endPage) {
		Page<Long> p = bookRepo.find("$indexQueryBookByPage", Params.param()).fetch(P.index(page, PAGE_SIZE),
				Long.class);
		while (p.isNotEmpty()) {
			this.add(p.getItems());
			// 如果已经到了结束页，结束重建索引
			if (endPage != null && page == endPage) {
				return;
			}
			page++;
			if (page % 50 == 0) {
				bookRepo.clear();
			}
			p = bookRepo.find("$indexQueryBookByPage", Params.param()).fetch(P.index(page, PAGE_SIZE), Long.class);
		}
	}

	@Override
	public void deleteReindex() {
		deleteByType(getType());
		reindex();
	}

	private Document convert(BookIndexDoc doc) {
		return new Document(IndexType.BOOK.toString(), String.valueOf(doc.getBookId()), doc.documentValue());
	}

	private List<Document> convert(List<BookIndexDoc> docs) {
		List<Document> list = new ArrayList<Document>(docs.size());
		for (BookIndexDoc bookIndexDoc : docs) {
			list.add(this.convert(bookIndexDoc));
		}
		return list;
	}

	@Override
	public void continueReindex(Integer startPage, Integer endPage) {
		int page = 1;
		if (startPage != null) {
			page = startPage;
		} else {
			long indexCount = this.indexCount();
			// 如果大于0说明之前已经跑过索引,判断接下来从哪页开始重建
			if (indexCount > 0) {
				page = (int) (indexCount / PAGE_SIZE + 1);
			}
		}
		reindexHandle(page, endPage);
	}
}

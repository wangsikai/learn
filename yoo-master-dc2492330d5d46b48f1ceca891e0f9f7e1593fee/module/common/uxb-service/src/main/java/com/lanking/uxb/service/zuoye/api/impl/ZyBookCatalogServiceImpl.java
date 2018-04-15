package com.lanking.uxb.service.zuoye.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.book.BookCatalog;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.zuoye.api.ZyBookCatalogService;

@Transactional(readOnly = true)
@Service
public class ZyBookCatalogServiceImpl implements ZyBookCatalogService {
	@Autowired
	@Qualifier("BookCatalogRepo")
	Repo<BookCatalog, Long> bookCatalogRepo;

	@Override
	public BookCatalog get(long id) {
		return bookCatalogRepo.get(id);
	}

	@Override
	public Map<Long, BookCatalog> mget(Collection<Long> ids) {
		return bookCatalogRepo.mget(ids);
	}

	@Override
	public List<BookCatalog> listBookCatalog(long bookVersionId, long textbookCode, long sectionCode) {
		Params params = Params.param();
		params.put("bookVersionId", bookVersionId);
		params.put("textbookCode", textbookCode);
		params.put("sectionCode", sectionCode);

		return bookCatalogRepo.find("$getBookCatalogByVersionAndSection", params).list();
	}
}

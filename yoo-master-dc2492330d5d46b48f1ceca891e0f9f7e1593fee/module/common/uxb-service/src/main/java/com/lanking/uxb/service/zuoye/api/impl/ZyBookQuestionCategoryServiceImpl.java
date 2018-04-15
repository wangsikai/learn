package com.lanking.uxb.service.zuoye.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.book.BookQuestionCategory;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.zuoye.api.ZyBookQuestionCategoryService;

@Transactional(readOnly = true)
@Service
public class ZyBookQuestionCategoryServiceImpl implements ZyBookQuestionCategoryService{

	@Autowired
	@Qualifier("BookQuestionCategoryRepo")
	Repo<BookQuestionCategory, Long> bookQuestionCategoryRepo;

	@Override
	public List<BookQuestionCategory> findListByCatalogId(Long bookCatalogId) {
		return bookQuestionCategoryRepo.find("$findListByCatalogId", Params.param("bookCatalogId", bookCatalogId)).list();
	}

	@Override
	public Map<Long, BookQuestionCategory> mgetByCatalogIds(
			Collection<Long> ids) {
		return bookQuestionCategoryRepo.mget(ids);
	}

}

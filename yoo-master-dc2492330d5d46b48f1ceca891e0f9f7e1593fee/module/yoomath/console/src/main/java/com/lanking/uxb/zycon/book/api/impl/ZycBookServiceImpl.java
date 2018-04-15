package com.lanking.uxb.zycon.book.api.impl;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.book.Book;
import com.lanking.uxb.zycon.book.api.ZycBookService;

@Transactional(readOnly = true)
@Service
public class ZycBookServiceImpl implements ZycBookService {

	@Autowired
	@Qualifier("BookRepo")
	Repo<Book, Long> schoolBookRepo;

	@Override
	public Book get(Long id) {
		return schoolBookRepo.get(id);
	}

	@Override
	public Map<Long, Book> mget(Collection<Long> ids) {
		return schoolBookRepo.mget(ids);
	}
}

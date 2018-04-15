package com.lanking.uxb.zycon.book.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.book.Book2Tag;
import com.lanking.cloud.domain.common.resource.book.BookTag;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.book.api.ZycBookTagService;

@Transactional(readOnly = true)
@Service
public class ZycBookTagServiceImpl  implements ZycBookTagService{

	@Autowired
	@Qualifier("BookTagRepo")
	Repo<BookTag, Long> bookTagRepo;
	
	@Autowired
	@Qualifier("Book2TagRepo")
	Repo<Book2Tag, Long> book2TagRepo;
	
	@Override
	public List<BookTag> findTagList() {
		return bookTagRepo.find("$findTagList").list();
	}

	@Override
	public Book2Tag findByBookVersionId(Long bookVersionId) {
		return book2TagRepo.find("$findByBookVersionId", Params.param("bookVersionId", bookVersionId)).get();
	}

	@Transactional
	@Override
	public void setTag(Long bookVersionId, String name) {
		Book2Tag book2Tag = this.findByBookVersionId(bookVersionId);
		if(book2Tag == null){
			book2Tag = new Book2Tag();
			book2Tag.setBookVersionId(bookVersionId);
			book2Tag.setName(name);
		}else{
			book2Tag.setName(name);
		}
		book2TagRepo.save(book2Tag);
	}

}

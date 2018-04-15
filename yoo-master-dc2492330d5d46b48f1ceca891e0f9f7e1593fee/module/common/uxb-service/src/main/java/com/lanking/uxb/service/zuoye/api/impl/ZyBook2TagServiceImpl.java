package com.lanking.uxb.service.zuoye.api.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.book.Book2Tag;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.zuoye.api.ZyBook2TagService;

@Transactional(readOnly = true)
@Service
public class ZyBook2TagServiceImpl implements ZyBook2TagService{

	@Autowired
	@Qualifier("Book2TagRepo")
	Repo<Book2Tag, Long> book2TagRepo;
	
	@Override
	public Book2Tag getByBookVersionId(Long versionId) {
		return book2TagRepo.find("$getByBookVersionId", Params.param("versionId", versionId)).get();
	}

	@Override
	public Map<Long, Book2Tag> mgetByBookVersionIds(Collection<Long> versionIds) {
		List<Book2Tag> list = book2TagRepo.find("$mgetByBookVersionIds", Params.param("versionIds", versionIds)).list();
		Map<Long, Book2Tag> map = new HashMap<Long, Book2Tag>();
		for(Book2Tag tag : list){
			map.put(tag.getBookVersionId(), tag);
		}
		return map;
	}

}

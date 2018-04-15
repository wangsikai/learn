package com.lanking.uxb.zycon.book.api.impl;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.book.Book;
import com.lanking.cloud.domain.common.resource.book.BookOpenStatus;
import com.lanking.cloud.domain.yoomath.school.SchoolBook;
import com.lanking.cloud.domain.yoomath.school.UserSchoolBook;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.book.api.ZycSchoolBookService;

@Transactional(readOnly = true)
@Service
public class ZycSchoolBookServiceImpl implements ZycSchoolBookService {

	@Autowired
	@Qualifier("SchoolBookRepo")
	Repo<SchoolBook, Long> schoolBookRepo;

	@Autowired
	@Qualifier("UserSchoolBookRepo")
	Repo<UserSchoolBook, Long> userSchoolBookRepo;

	@Autowired
	@Qualifier("BookRepo")
	Repo<Book, Long> bookRepo;

	@Override
	public SchoolBook get(Long schoolId, Long bookId) {
		return schoolBookRepo.find("$getSchoolBook", Params.param("schoolId", schoolId).put("bookId", bookId)).get();
	}

	@Transactional
	@Override
	public void delSchookBook(Long schoolId, Long bookId) {
		SchoolBook schoolBook = this.get(schoolId, bookId);
		schoolBook.setStatus(Status.DELETED);
		schoolBookRepo.save(schoolBook);
		int count = this.get(bookId).size();
		Book book = bookRepo.get(bookId);
		// 校本题库里面添加的只能是学校开放或者不开放的书本
		// 若删除的是这本书的最后一个学校，需要更改书本状态,不是最后一个则不需要
		if (book.getOpenStatus() == BookOpenStatus.SCHOOL_OPEN) {
			if (count == 0) {
				book.setOpenStatus(BookOpenStatus.NOT_OPEN);
				bookRepo.save(book);
			}
		}
		Params params = Params.param("schoolBookId", schoolBook.getId());
		userSchoolBookRepo.execute("$updateUserSchoolBook", params.put("status", Status.DELETED.getValue()));
	}

	@Transactional
	@Override
	public void addSchoolBook(Long bookId, Long schoolId) {
		SchoolBook schoolBook = this.get(schoolId, bookId);
		if (schoolBook == null) {
			schoolBook = new SchoolBook();
		} else {
			// 把之前是删除的状态修改回来
			schoolBook.setStatus(Status.ENABLED);
		}
		schoolBook.setBookId(bookId);
		schoolBook.setSchoolId(schoolId);
		schoolBook.setCreateAt(new Date());
		schoolBookRepo.save(schoolBook);
		Book book = bookRepo.get(bookId);
		// 原来是不公开的书本，若添加进学校，则状态变为对指定学校公
		if (book.getOpenStatus() == BookOpenStatus.NOT_OPEN) {
			book.setOpenStatus(BookOpenStatus.SCHOOL_OPEN);
			bookRepo.save(book);
		}
	}

	@Override
	public List<SchoolBook> get(Long bookId) {
		return schoolBookRepo.find("$getSbList", Params.param("bookId", bookId)).list();
	}

	@Override
	public Map<Long, List<SchoolBook>> mget(Collection<Long> bookIds) {
		List<SchoolBook> list = schoolBookRepo.find("$mgetSbList", Params.param("bookIds", bookIds)).list();
		Map<Long, List<SchoolBook>> map = Maps.newHashMap();
		for (Long bookId : bookIds) {
			map.put(bookId, Lists.<SchoolBook> newArrayList());
		}
		for (SchoolBook sb : list) {
			List<SchoolBook> as = map.get(sb.getBookId());
			as.add(sb);
			map.put(sb.getBookId(), as);
		}
		return map;
	}

	@Transactional
	@Override
	public void delSchoolBookByBookId(Long bookId) {
		schoolBookRepo.execute("$delSchoolBookByBookId", Params.param("bookId", bookId));
	}

	@Override
	public Long getBookCount(Long schoolId) {
		return schoolBookRepo.find("$getBookCount", Params.param("schoolId", schoolId)).count();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Long, Long> mgetBookCount(Collection<Long> schoolIds) {
		List<Map> list = schoolBookRepo.find("$mgetBookCount", Params.param("schoolIds", schoolIds)).list(Map.class);
		Map<Long, Long> map = new HashMap<Long, Long>();
		for (Map pa : list) {
			map.put(((BigInteger) pa.get("school_id")).longValue(), ((BigInteger) pa.get("count1")).longValue());
		}
		return map;
	}

	@Transactional
	@Override
	public void updateSchoolBook(Long schoolId, Status status) {
		// 1.如果禁用的学校,刚好是某本书指定的最后一个学校,则对应的书本的开放状态也要更新为不公开
		List<Long> bookIds = schoolBookRepo.find("$getBookIdsBySchool", Params.param("schoolId", schoolId)).list(
				Long.class);
		if (status != Status.ENABLED) {
			if (bookIds.size() > 0) {
				for (Long bookId : bookIds) {
					List<SchoolBook> sbList = this.get(bookId);
					if (sbList.size() == 1) {
						Book book = bookRepo.get(bookId);
						book.setOpenStatus(BookOpenStatus.NOT_OPEN);
						bookRepo.save(book);
						// 如果学校被禁用,对应的用户学校书本也要禁用
						SchoolBook schoolBook = this.get(schoolId, bookId);
						if (schoolBook != null) {
							Params params = Params.param("schoolBookId", schoolBook.getId());
							userSchoolBookRepo.execute("$updateUserSchoolBook",
									params.put("status", Status.DELETED.getValue()));
						}
					}
				}
			}
			// 如果是恢复，学校书本不需要修改状态
			schoolBookRepo.execute("$updateSchoolBook",
					Params.param("status", status.getValue()).put("schoolId", schoolId));
		}

	}
}

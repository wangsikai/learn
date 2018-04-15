package com.lanking.uxb.zycon.book.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.common.resource.book.Book;
import com.lanking.cloud.domain.common.resource.book.BookOpenStatus;
import com.lanking.cloud.domain.common.resource.book.BookVersion;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoomath.school.SchoolBook;
import com.lanking.cloud.domain.yoomath.school.UserSchoolBook;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.zycon.book.api.BookQuery;
import com.lanking.uxb.zycon.book.api.ZycBookManageService;
import com.lanking.uxb.zycon.book.api.ZycSchoolBookService;
import com.lanking.uxb.zycon.book.form.BookForm;

@Transactional(readOnly = true)
@Service
public class ZycBookManageServiceImpl implements ZycBookManageService {

	@Autowired
	@Qualifier("BookVersionRepo")
	Repo<BookVersion, Long> bookVersionRepo;

	@Autowired
	@Qualifier("SchoolRepo")
	Repo<School, Long> schoolRepo;

	@Autowired
	@Qualifier("BookRepo")
	Repo<Book, Long> bookRepo;

	@Autowired
	@Qualifier("SchoolBookRepo")
	Repo<SchoolBook, Long> schoolBookRepo;

	@Autowired
	@Qualifier("UserSchoolBookRepo")
	Repo<UserSchoolBook, Long> userSchoolBookRepo;

	@Autowired
	@Qualifier("TeacherRepo")
	Repo<Teacher, Long> teacherRepo;

	@Autowired
	private ZycSchoolBookService zycSchoolBookService;

	@Override
	public Page<BookVersion> queryBook(BookQuery bq) {
		Params param = Params.param();
		if (bq.getKey() != null) {
			param.put("keystr", "%" + bq.getKey() + "%");
		}
		if (bq.getSubjectCode() != null) {
			param.put("subjectCode", bq.getSubjectCode());
		}
		if (bq.getBookType() != null) {
			param.put("resourceCategoryCode", bq.getBookType());
		}
		if (bq.getOpenStatus() != null) {
			param.put("status", bq.getOpenStatus().getValue());
		}
		if (bq.getCategoryCode() != null) {
			param.put("categroyCode", bq.getCategoryCode());
		}
		if (bq.getTextbookCode() != null) {
			param.put("textbookCode", bq.getTextbookCode());
		}
		if (bq.getSectionCode() != null) {
			param.put("sectionCode", bq.getSectionCode());
		}
		if (bq.isStatusfilter()) {
			param.put("statusfilter", bq.isStatusfilter());
		}
		if (bq.getSchoolId() != null) {
			param.put("schoolId", bq.getSchoolId());
		}
		// 只查询数学科目的书本
		List<Integer> subjectList = new ArrayList<Integer>();
		subjectList.add(SubjectService.PHASE_2_MATH);
		subjectList.add(SubjectService.PHASE_3_MATH);
		param.put("subjects", subjectList);
		return bookVersionRepo.find("$queryBook", param).fetch(P.index(bq.getPage(), bq.getPageSize()));
	}

	@Override
	public Page<School> getQuestionSchoolList(Long bookId, Pageable p) {
		return schoolRepo.find("$getQuestionSchoolList", Params.param("bookId", bookId)).fetch(p);
	}

	@Override
	public List<School> getQuestionSchoolList(Long bookId) {
		return schoolRepo.find("$getQuestionSchoolList", Params.param("bookId", bookId)).list();
	}

	@Transactional
	@Override
	public void updateBookStatus(BookForm bookForm) {
		Book book = bookRepo.get(bookForm.getBookId());
		if (bookForm.getOpenStatus() == BookOpenStatus.NOT_OPEN) {
			List<UserSchoolBook> usbListTemp = this.getUserSchoolBookByBookId(bookForm.getBookId());
			List<Long> usbIdsTemp = Lists.newArrayList();
			for (UserSchoolBook userSchoolBook : usbListTemp) {
				usbIdsTemp.add(userSchoolBook.getId());
			}
			this.updateUserSchoolBookStatus(usbIdsTemp, Status.DISABLED);
		}
		book.setOpenStatus(bookForm.getOpenStatus());
		// 如果为完全开放、学校开放则更新开放时间
		if (bookForm.getOpenStatus() != BookOpenStatus.NOT_OPEN) {
			book.setOpenAt(new Date());
		}
		bookRepo.save(book);
		// 如果修改为不是指定的学校开放，则删除之前对应的学校书本
		if (bookForm.getOpenStatus() != BookOpenStatus.SCHOOL_OPEN) {
			zycSchoolBookService.delSchoolBookByBookId(bookForm.getBookId());
		}
		// 为指定学校公开时
		if (bookForm.getSchoolIds() != null && bookForm.getOpenStatus() == BookOpenStatus.SCHOOL_OPEN) {
			zycSchoolBookService.delSchoolBookByBookId(bookForm.getBookId());
			for (Long schoolId : bookForm.getSchoolIds()) {
				SchoolBook schoolBook = zycSchoolBookService.get(schoolId, bookForm.getBookId());
				if (schoolBook == null) {
					schoolBook = new SchoolBook();
					schoolBook.setBookId(bookForm.getBookId());
					schoolBook.setSchoolId(schoolId);
					schoolBook.setCreateAt(new Date());
				} else {
					schoolBook.setStatus(Status.ENABLED);
					schoolBook.setUpdateAt(new Date());
				}
				schoolBookRepo.save(schoolBook);
			}
			// 判断选择过该书本的用户是否属于当前指定的学校
			List<UserSchoolBook> usbList = this.getUserSchoolBookByBookId(bookForm.getBookId());
			List<Long> userIdS = Lists.newArrayList();
			for (UserSchoolBook userSchoolBook : usbList) {
				userIdS.add(userSchoolBook.getUserId());
			}
			Map<Long, Teacher> teacherMap = teacherRepo.mget(userIdS);
			List<Long> chooseStatusUsbIds = Lists.newArrayList();
			for (UserSchoolBook usb : usbList) {
				Long userSchoolId = teacherMap.get(usb.getUserId()).getSchoolId();
				if (!bookForm.getSchoolIds().contains(userSchoolId)) {
					chooseStatusUsbIds.add(usb.getId());
				}
			}
			if (CollectionUtils.isNotEmpty(chooseStatusUsbIds)) {
				this.updateUserSchoolBookStatus(chooseStatusUsbIds, Status.DISABLED);
			}

		}

	}

	@Override
	public Page<BookVersion> getBookBySchool(Long schoolId, Integer phaseCode, Pageable p) {
		Params param = Params.param("schoolId", schoolId);
		if (phaseCode != null) {
			param.put("phaseCode", phaseCode);
		}
		return bookVersionRepo.find("$getBookBySchool", param).fetch(p);
	}

	@Override
	public List<UserSchoolBook> getUserSchoolBookByBookId(long bookId) {
		return userSchoolBookRepo.find("$getUserSchoolBookByBookId", Params.param("bookId", bookId)).list();
	}

	@Transactional
	@Override
	public void updateUserSchoolBookStatus(List<Long> ids, Status status) {
		// 若此本书原未绑定学校则不需要更新状态
		if (CollectionUtils.isNotEmpty(ids)) {
			userSchoolBookRepo.execute("$updateUserSchoolBookStatus",
					Params.param("ids", ids).put("status", status.getValue()));
		}
	}
}

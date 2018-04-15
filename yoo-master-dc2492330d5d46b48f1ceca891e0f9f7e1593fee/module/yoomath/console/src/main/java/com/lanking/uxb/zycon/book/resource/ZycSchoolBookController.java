package com.lanking.uxb.zycon.book.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.resource.book.BookVersion;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.zycon.book.api.ZycBookManageService;
import com.lanking.uxb.zycon.book.api.ZycSchoolBookService;
import com.lanking.uxb.zycon.book.convert.BookVersionConvert;
import com.lanking.uxb.zycon.book.value.VZycBook;

/**
 * 校本图书
 * 
 * @since yoomath V1.6
 * @author wangsenhao
 *
 */
@RestController
@RequestMapping("zyc/schoolBook")
public class ZycSchoolBookController {
	@Autowired
	private ZycBookManageService zycBookManageService;
	@Autowired
	private BookVersionConvert bookVersionConvert;
	@Autowired
	private ZycSchoolBookService zycSchoolBookService;

	/**
	 * 获取当前学校对应的校本图书
	 * 
	 * @param schoolId
	 * @return
	 */
	@RequestMapping(value = "getBookBySchool")
	public Value getBookBySchool(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int pageSize, Long schoolId, Integer phaseCode) {
		Page<BookVersion> cp = zycBookManageService.getBookBySchool(schoolId, phaseCode, P.index(page, pageSize));
		VPage<VZycBook> vp = new VPage<VZycBook>();
		int tPage = (int) (cp.getTotalCount() + pageSize - 1) / pageSize;
		vp.setPageSize(pageSize);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		vp.setItems(bookVersionConvert.to(cp.getItems()));
		return new Value(vp);
	}

	/**
	 * 删除校本图书
	 * 
	 * @return
	 */
	@RequestMapping(value = "delSchoolBook")
	public Value delSchoolBook(Long schoolId, Long bookId) {
		zycSchoolBookService.delSchookBook(schoolId, bookId);
		return new Value();
	}

	/**
	 * 添加新书
	 * 
	 * @param bookId
	 * @param schoolId
	 * @return
	 */
	@RequestMapping(value = "addSchoolBook")
	public Value addSchoolBook(Long bookId, Long schoolId) {
		zycSchoolBookService.addSchoolBook(bookId, schoolId);
		return new Value();
	}
}

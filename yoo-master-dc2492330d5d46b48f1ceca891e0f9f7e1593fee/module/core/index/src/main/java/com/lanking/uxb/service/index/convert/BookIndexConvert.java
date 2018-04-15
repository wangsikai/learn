package com.lanking.uxb.service.index.convert;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.common.resource.book.Book;
import com.lanking.cloud.domain.common.resource.book.BookVersion;
import com.lanking.uxb.service.index.value.BookIndexDoc;

/**
 * 书本索引转换.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年10月23日
 */
@Component
@Transactional(readOnly = true)
public class BookIndexConvert {

	public BookIndexDoc to(Book book, BookVersion bv1, BookVersion bv2) {
		BookIndexDoc doc = new BookIndexDoc();
		if (!bv1.isMainFlag()) {
			BookVersion temp = bv1;
			bv1 = bv2;
			bv2 = temp;
		}
		doc.setBookId(bv1.getBookId());
		doc.setCreateAt(book.getCreateAt().getTime());
		doc.setCreateId(book.getCreateId());
		doc.setIsbn1(bv1.getIsbn());
		doc.setName1(bv1.getName());
		doc.setPhaseCode(bv1.getPhaseCode());
		doc.setSectionCode1(bv1.getSectionCode());
		doc.setSectionCodes1(bv1.getSectionCodes());
		doc.setStatus1(bv1.getStatus().getValue());
		doc.setSubjectCode(bv1.getSubjectCode());
		doc.setTextbookCategoryCode1(bv1.getTextbookCategoryCode());
		doc.setTextbookCode1(bv1.getTextbookCode());
		doc.setResourceCategoryCode1(bv1.getResourceCategoryCode());
		doc.setVendorId(book.getVendorId());
		doc.setSchoolId(book.getSchoolId());
		if (bv2 != null) {
			doc.setIsbn2(bv2.getIsbn());
			doc.setName2(bv2.getName());
			doc.setSectionCode2(bv2.getSectionCode());
			doc.setSectionCodes2(bv2.getSectionCodes());
			doc.setStatus2(bv2.getStatus().getValue());
			doc.setTextbookCategoryCode2(bv2.getTextbookCategoryCode());
			doc.setTextbookCode2(bv2.getTextbookCode());
			doc.setResourceCategoryCode2(bv2.getResourceCategoryCode());
		}
		doc.setStatus(book.getStatus().getValue());
		return doc;
	}

	public BookIndexDoc to(List<BookVersion> bvs, Map<Long, Book> books) {
		BookVersion bv1 = bvs.get(0);
		BookVersion bv2 = bvs.size() < 2 ? null : bvs.get(1);
		return this.to(books.get(bv1.getBookId()), bv1, bv2);
	}

}

package com.lanking.uxb.zycon.book.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.ResourceCategory;
import com.lanking.cloud.domain.common.baseData.Subject;
import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.common.baseData.TextbookCategory;
import com.lanking.cloud.domain.common.resource.book.Book;
import com.lanking.cloud.domain.common.resource.book.BookOpenStatus;
import com.lanking.cloud.domain.common.resource.book.BookVersion;
import com.lanking.cloud.domain.yoomath.school.SchoolBook;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.ResourceCategoryService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.SchoolConvert;
import com.lanking.uxb.service.code.value.VSchool;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.zycon.book.api.ZycBookService;
import com.lanking.uxb.zycon.book.api.ZycSchoolBookService;
import com.lanking.uxb.zycon.book.value.VZycBook;

/**
 * 书本版本转换
 * 
 * @author wangsenhao
 *
 */
@Component
public class BookVersionConvert extends Converter<VZycBook, BookVersion, Long> {

	@Autowired
	private ZycBookService zycBookService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private ResourceCategoryService resourceCategoryService;
	@Autowired
	private TextbookCategoryService textbookCategoryService;
	@Autowired
	private TextbookService textbookService;
	@Autowired
	private ZycSchoolBookService zycSchoolBookService;
	@Autowired
	private SchoolConvert schoolConvert;

	@Override
	protected Long getId(BookVersion s) {
		return s.getId();
	}

	@Override
	protected VZycBook convert(BookVersion s) {
		VZycBook vZycBook = new VZycBook();
		vZycBook.setBookId(s.getBookId());
		vZycBook.setBookVersionId(s.getId());
		vZycBook.setCoverUrl(FileUtil.getUrl(s.getCoverId()));
		vZycBook.setIsbn(s.getIsbn());
		vZycBook.setName(s.getName());
		vZycBook.setVersion("V" + s.getVersion());
		vZycBook.setPhaseName(PhaseService.PHASE_HIGH == s.getPhaseCode() ? "高中" : "初中");
		return vZycBook;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 公开状态
		assemblers.add(new ConverterAssembler<VZycBook, BookVersion, Long, Book>() {

			@Override
			public boolean accept(BookVersion s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(BookVersion s, VZycBook d) {
				return s.getBookId();
			}

			@Override
			public void setValue(BookVersion s, VZycBook d, Book value) {
				if (value.getOpenStatus() == BookOpenStatus.NOT_OPEN) {
					d.setOpenstatus("不公开");
				}
				if (value.getOpenStatus() == BookOpenStatus.SCHOOL_OPEN) {
					d.setOpenstatus("向指定学校公开");
				}
				if (value.getOpenStatus() == BookOpenStatus.OPEN) {
					d.setOpenstatus("完全公开");
				}
				if (value.getSchoolId() != null) {
					d.setSchoolId(value.getSchoolId());
				}
			}

			@Override
			public Book getValue(Long key) {
				return zycBookService.get(key);
			}

			@Override
			public Map<Long, Book> mgetValue(Collection<Long> keys) {
				return zycBookService.mget(keys);
			}

		});
		// 学科
		assemblers.add(new ConverterAssembler<VZycBook, BookVersion, Integer, Subject>() {

			@Override
			public boolean accept(BookVersion s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(BookVersion s, VZycBook d) {
				return s.getSubjectCode();
			}

			@Override
			public void setValue(BookVersion s, VZycBook d, Subject value) {
				d.setSubjectName(value.getName());
			}

			@Override
			public Subject getValue(Integer key) {
				return subjectService.get(key);
			}

			@Override
			public Map<Integer, Subject> mgetValue(Collection<Integer> keys) {
				return subjectService.mget(keys);
			}

		});
		// 书本类型
		assemblers.add(new ConverterAssembler<VZycBook, BookVersion, Integer, ResourceCategory>() {

			@Override
			public boolean accept(BookVersion s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(BookVersion s, VZycBook d) {
				return s.getResourceCategoryCode();
			}

			@Override
			public void setValue(BookVersion s, VZycBook d, ResourceCategory value) {
				d.setBookType(value.getName());
			}

			@Override
			public ResourceCategory getValue(Integer key) {
				return resourceCategoryService.getResCategory(key);
			}

			@Override
			public Map<Integer, ResourceCategory> mgetValue(Collection<Integer> keys) {
				return resourceCategoryService.mget(keys);
			}

		});

		assemblers.add(new ConverterAssembler<VZycBook, BookVersion, Integer, TextbookCategory>() {

			@Override
			public boolean accept(BookVersion s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(BookVersion s, VZycBook d) {
				return s.getTextbookCategoryCode();
			}

			@Override
			public void setValue(BookVersion s, VZycBook d, TextbookCategory value) {
				d.setCategoryName(value.getName());

			}

			@Override
			public TextbookCategory getValue(Integer key) {
				return textbookCategoryService.get(key);
			}

			@Override
			public Map<Integer, TextbookCategory> mgetValue(Collection<Integer> keys) {
				return textbookCategoryService.mget(keys);
			}

		});

		assemblers.add(new ConverterAssembler<VZycBook, BookVersion, Integer, Textbook>() {

			@Override
			public boolean accept(BookVersion s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(BookVersion s, VZycBook d) {
				return s.getTextbookCode();
			}

			@Override
			public void setValue(BookVersion s, VZycBook d, Textbook value) {
				d.setTextbookName(value.getName());

			}

			@Override
			public Textbook getValue(Integer key) {
				return textbookService.get(key);
			}

			@Override
			public Map<Integer, Textbook> mgetValue(Collection<Integer> keys) {
				return textbookService.mget(keys);
			}
		});

		assemblers.add(new ConverterAssembler<VZycBook, BookVersion, Long, List<SchoolBook>>() {

			@Override
			public boolean accept(BookVersion s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(BookVersion s, VZycBook d) {
				return s.getBookId();
			}

			@Override
			public void setValue(BookVersion s, VZycBook d, List<SchoolBook> value) {
				List<Long> list = new ArrayList<Long>();
				for (SchoolBook schoolBook : value) {
					list.add(schoolBook.getSchoolId());
				}
				List<VSchool> schoolList = schoolConvert.mgetList(list);
				d.setSchoolList(schoolList);

			}

			@Override
			public List<SchoolBook> getValue(Long key) {
				return zycSchoolBookService.get(key);
			}

			@Override
			public Map<Long, List<SchoolBook>> mgetValue(Collection<Long> keys) {
				return zycSchoolBookService.mget(keys);
			}

		});
	}
}

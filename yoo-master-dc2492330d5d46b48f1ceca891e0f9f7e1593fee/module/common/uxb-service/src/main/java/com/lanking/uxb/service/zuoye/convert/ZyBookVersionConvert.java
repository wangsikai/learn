package com.lanking.uxb.service.zuoye.convert;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.book.Book;
import com.lanking.cloud.domain.common.resource.book.Book2Tag;
import com.lanking.cloud.domain.common.resource.book.BookVersion;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.service.zuoye.api.ZyBook2TagService;
import com.lanking.uxb.service.zuoye.api.ZyBookService;
import com.lanking.uxb.service.zuoye.value.VBookVersion;
import com.taobao.api.internal.util.StringUtils;

@Component
public class ZyBookVersionConvert extends Converter<VBookVersion, BookVersion, Long> {
	@Autowired
	private ZyBookService bookService;
	@Autowired
	private ZyBook2TagService book2TagService;

	@Override
	protected Long getId(BookVersion s) {
		return s.getId();
	}

	@Override
	protected VBookVersion convert(BookVersion s) {
		VBookVersion v = new VBookVersion();
		v.setId(s.getId());
		v.setBookId(s.getBookId());
		v.setName(s.getName());
		v.setUrl(FileUtil.getUrl(s.getCoverId()));
		v.setShortName(StringUtils.isEmpty(s.getShortName()) ? s.getName() : s.getShortName());
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VBookVersion, BookVersion, Long, Book>() {

			@Override
			public boolean accept(BookVersion bookVersion) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(BookVersion bookVersion, VBookVersion vBookVersion) {
				return bookVersion.getBookId();
			}

			@Override
			public void setValue(BookVersion bookVersion, VBookVersion vBookVersion, Book value) {
				if (value != null) {
					vBookVersion.setOpenStatus(value.getOpenStatus());
					vBookVersion.setSchoolId(value.getSchoolId());
				}
			}

			@Override
			public Book getValue(Long key) {
				return bookService.getBook(key);
			}

			@Override
			@SuppressWarnings("unchecked")
			public Map<Long, Book> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}

				return bookService.mget(keys);
			}
		});
		
		assemblers.add(new ConverterAssembler<VBookVersion, BookVersion, Long, Book2Tag>() {

			@Override
			public boolean accept(BookVersion s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(BookVersion s, VBookVersion d) {
				return s.getId();
			}

			@Override
			public void setValue(BookVersion s, VBookVersion d, Book2Tag value) {
				d.setTagName(value.getName());
			}

			@Override
			public Book2Tag getValue(Long key) {
				return book2TagService.getByBookVersionId(key);
			}

			@Override
			public Map<Long, Book2Tag> mgetValue(Collection<Long> keys) {
				return book2TagService.mgetByBookVersionIds(keys);
			}
			
		});
	}
}

package com.lanking.uxb.rescon.book.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.book.BookQuestionCategory;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.rescon.book.api.ResconBookQuestionCategoryManage;
import com.lanking.uxb.rescon.book.value.VBookQuestionCategory;

@Component
public class ResconBookQuestionCategoryConvert extends Converter<VBookQuestionCategory, BookQuestionCategory, Long> {

	@Autowired
	ResconBookQuestionCategoryManage bookQuestionCategoryManage;

	@Override
	protected VBookQuestionCategory convert(BookQuestionCategory bookQuestionCategory) {
		if (bookQuestionCategory != null) {
			VBookQuestionCategory v = new VBookQuestionCategory();
			v.setBookSectionId(bookQuestionCategory.getBookSectionId());
			v.setBookVersionId(bookQuestionCategory.getBookVersionId());
			v.setId(bookQuestionCategory.getId());
			v.setName(bookQuestionCategory.getName());
			return v;
		}
		return null;
	}

	@Override
	protected Long getId(BookQuestionCategory arg0) {
		return arg0.getId();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {

		// 习题个数
		assemblers.add(new ConverterAssembler<VBookQuestionCategory, BookQuestionCategory, Long, Integer>() {

			@Override
			public boolean accept(BookQuestionCategory arg0) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> arg0) {
				return true;
			}

			@Override
			public Long getKey(BookQuestionCategory arg0, VBookQuestionCategory arg1) {
				return arg0.getId();
			}

			@Override
			public Integer getValue(Long arg0) {
				return bookQuestionCategoryManage.getQuestionCount(arg0);
			}

			@Override
			public Map<Long, Integer> mgetValue(Collection<Long> arg0) {
				return bookQuestionCategoryManage.mgetQuestionCount(arg0);
			}

			@Override
			public void setValue(BookQuestionCategory arg0, VBookQuestionCategory arg1, Integer arg2) {
				arg1.setCount(arg2 == null ? 0 : arg2);
			}

		});
	}
}

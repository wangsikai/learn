package com.lanking.uxb.service.code.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.common.baseData.TextbookCategory;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.code.value.VTextbookCategory;

/**
 * 教材相关的接口
 *
 * @author xinyu.zhou
 * @since V2.1
 */
@RestController
@RequestMapping("common/textbook")
public class TextBookController {

	@Autowired
	private TextbookCategoryService textbookCategoryService;
	@Autowired
	private TextbookCategoryConvert textbookCategoryConvert;
	@Autowired
	private TextbookService textbookService;
	@Autowired
	private TextbookConvert textbookConvert;

	@RequestMapping(value = "get_text_book_category", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getTextBookCategory() {
		List<TextbookCategory> categories = textbookCategoryService.getAll();
		List<VTextbookCategory> vCategories = textbookCategoryConvert.to(categories);

		return new Value(vCategories);
	}

	@RequestMapping(value = "getTextBookByCategoryPhaseSubject", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getTextBookByCategoryPhaseSubject(@RequestParam(value = "category") Integer category,
			@RequestParam(value = "phaseCode") Integer phaseCode,
			@RequestParam(value = "subjectCode") Integer subjectCode) {
		List<Textbook> textbooks = textbookService.find(phaseCode, category, subjectCode);
		List<VTextbook> vTextbooks = textbookConvert.to(textbooks);

		return new Value(vTextbooks);
	}

	@RequestMapping(value = "getAll", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getAll() {
		List<Textbook> textbooks = textbookService.getAll();
		List<VTextbook> vTextbooks = textbookConvert.to(textbooks);

		return new Value(vTextbooks);
	}
}

package com.lanking.uxb.zycon.book.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.baseData.ResourceCategory;
import com.lanking.cloud.domain.common.baseData.ResourceCategoryCode;
import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.common.resource.book.Book2Tag;
import com.lanking.cloud.domain.common.resource.book.BookTag;
import com.lanking.cloud.domain.common.resource.book.BookVersion;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.code.api.ResourceCategoryService;
import com.lanking.uxb.service.code.convert.ResourceCatgeoryConvert;
import com.lanking.uxb.service.code.convert.SchoolConvert;
import com.lanking.uxb.service.code.value.VSchool;
import com.lanking.uxb.zycon.book.api.BookQuery;
import com.lanking.uxb.zycon.book.api.ZycBookManageService;
import com.lanking.uxb.zycon.book.api.ZycBookTagService;
import com.lanking.uxb.zycon.book.convert.BookVersionConvert;
import com.lanking.uxb.zycon.book.form.BookForm;
import com.lanking.uxb.zycon.book.value.VZycBook;

/**
 * 书本管理
 * 
 * @since yoomath V1.6
 * @author wangsenhao
 *
 */
@RestController
@RequestMapping("zyc/book")
public class ZycBookManageController {
	@Autowired
	private SchoolConvert schoolConvert;
	@Autowired
	private ZycBookManageService zycBookManageService;
	@Autowired
	private BookVersionConvert bookVersionConvert;
	@Autowired
	private ResourceCategoryService resourceCategoryService;
	@Autowired
	private ResourceCatgeoryConvert resourceCatgeoryConvert;
	@Autowired
	private ZycBookTagService zycBookTagService;

	/**
	 * 查询书本
	 * 
	 * @param bq
	 *            查询条件
	 * @return
	 */
	@RequestMapping(value = "queryBook")
	public Value queryBook(BookQuery bq) {
		Page<BookVersion> cp = zycBookManageService.queryBook(bq);
		VPage<VZycBook> vp = new VPage<VZycBook>();
		int tPage = (int) (cp.getTotalCount() + bq.getPageSize() - 1) / bq.getPageSize();
		vp.setPageSize(bq.getPageSize());
		vp.setCurrentPage(bq.getPage());
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		vp.setItems(bookVersionConvert.to(cp.getItems()));
		return new Value(vp);
	}

	/**
	 * 查询开通了校本题库，且当前书本未选择的学校
	 * 
	 * @return
	 */
	@RequestMapping(value = "getQuestionSchoolList")
	public Value getQuestionSchoolList(Long bookId, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int pageSize) {
		Page<School> cp = zycBookManageService.getQuestionSchoolList(bookId, P.index(page, pageSize));
		VPage<VSchool> vp = new VPage<VSchool>();
		int tPage = (int) (cp.getTotalCount() + pageSize - 1) / pageSize;
		vp.setPageSize(pageSize);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		vp.setItems(schoolConvert.to(cp.getItems()));
		return new Value(vp);
	}

	/**
	 * 查询开通了校本题库，且当前书本未选择的学校(不翻页)
	 * 
	 * @param bookId
	 * @return
	 */
	@RequestMapping(value = "2/getQuestionSchoolList")
	public Value getQuestionSchoolList2(Long bookId) {
		List<School> list = zycBookManageService.getQuestionSchoolList(bookId);
		return new Value(schoolConvert.to(list));
	}

	/**
	 * 书本状态设置
	 * 
	 * @param bookForm
	 * @return
	 */
	@RequestMapping(value = "updateBookStatus")
	public Value updateBookStatus(BookForm bookForm) {
		zycBookManageService.updateBookStatus(bookForm);
		return new Value();
	}

	/**
	 * 获取书本类型列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "getBookTypeList")
	public Value getBookTypeList() {
		List<ResourceCategory> list = resourceCategoryService
				.findCategoryByParent(ResourceCategoryCode.BOOK.getValue());
		return new Value(resourceCatgeoryConvert.to(list));
	}
	
	/**
	 * 通过书本版本Id，获取书本的tag（2017.11.30）
	 * @param bookVersionId
	 * @return
	 */
	@RequestMapping(value = "getTag")
	public Value getTag(Long bookVersionId){
		List<BookTag> bookTagList = zycBookTagService.findTagList();
		List<String> tagList = new ArrayList<String>();
		for(BookTag tag : bookTagList){
			tagList.add(tag.getName());
		}
		Map<String,Object> data = new HashMap<String,Object>();
		//存在的标签列表
		data.put("tagList", tagList);
		//如果已经存在标签，返回给前台
		Book2Tag book2Tag = zycBookTagService.findByBookVersionId(bookVersionId);
		if(book2Tag != null){
			data.put("selTag", book2Tag.getName());
		}
		return new Value(data);
	}
	
	/**
	 * 给书本设tag标签（2017.11.30）
	 * @param bookVersionId
	 * @param tagFlag 是否自定义标签
	 * @param tagName
	 * @return
	 */
	@RequestMapping(value = "setTag")
	public Value setTag(Long bookVersionId,String tagName){
		zycBookTagService.setTag(bookVersionId, tagName);
		return new Value();
	}
}
